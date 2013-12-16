package edu.jhu.cvrg.waveform.main;
/*
Copyright 2013 Johns Hopkins University Institute for Computational Medicine

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/
/**
* @author Michael Shipway, Chris Jurado, Stephen Granite
* 
*/
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.kernel.repository.model.Folder;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;

import edu.jhu.cvrg.dbapi.factory.Connection;
import edu.jhu.cvrg.dbapi.factory.ConnectionFactory;
import edu.jhu.cvrg.waveform.model.Algorithm;
import edu.jhu.cvrg.waveform.model.FileTreeNode;
import edu.jhu.cvrg.waveform.utility.AdditionalParameters;
import edu.jhu.cvrg.waveform.utility.AnalysisInProgress;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

public class AnalysisManager implements Serializable{

	private static final long serialVersionUID = 1L;
	static org.apache.log4j.Logger logger = Logger.getLogger(AnalysisManager.class);

	private boolean verbose = false;
	private AnalysisInProgress aIP;
	private String MISSING_VALUE = "0";

	public AnalysisManager(boolean verbose){	
		
		String dbUser = ResourceUtility.getDbUser();
		String dbPassword = ResourceUtility.getDbPassword();
		String dbUri = ResourceUtility.getDbURI();
		String dbDriver = ResourceUtility.getDbDriver();
		String dbMainDatabase = ResourceUtility.getDbMainDatabase();
		
		if(dbUser.equals(MISSING_VALUE) || 
				dbPassword.equals(MISSING_VALUE) || 
				dbUri.equals(MISSING_VALUE) || 
				dbDriver.equals(MISSING_VALUE) ||
				dbMainDatabase.equals(MISSING_VALUE)){
			
			logger.error("Missing one or more configuration values for the database.");
			return;	
		}
		
		this.verbose = verbose;
		aIP = new AnalysisInProgress();
	}

	public boolean performAnalysis(List<FileTreeNode> selectedNodes, long userId, Algorithm[] selectedAlgorithms ){
		
		try {
			
			Connection dbUtility = ConnectionFactory.createConnection();
			
			Set<AnalysisThread> threadMap = new HashSet<AnalysisThread>();
			
			Map<String, Object> jobs = new HashMap<String, Object>();
			Map<String, FileEntry> filesMap = new HashMap<String, FileEntry>();
			
			for (FileTreeNode node : selectedNodes) {
				
				FileEntry headerFile = (FileEntry) node.getContent();
				
				for (Algorithm algorithm : selectedAlgorithms) {
					
					Map<String, String> parameterMap = new HashMap<String, String>();
					
					parameterMap.put("userID",  String.valueOf(userId));
					parameterMap.put("groupID", String.valueOf(ResourceUtility.getCurrentGroupId()));
					parameterMap.put("folderID", String.valueOf(headerFile.getFolderId()));
					parameterMap.put("subjectID", headerFile.getFolder().getName());
					
					
					
					String fileNameString="";//filename.dat^filename.hea^";
					List<FileEntry> subFiles = DLAppLocalServiceUtil.getFileEntries(ResourceUtility.getCurrentGroupId(), headerFile.getFolderId());
					ArrayList<FileEntry> fileList = getFileList(algorithm, subFiles);
					for (FileEntry fileEntry : fileList) {
						fileNameString += fileEntry.getTitle() + "^";
						filesMap.put(fileEntry.getTitle(), fileEntry);
					}
					parameterMap.put("fileNames", fileNameString); // caret delimited list for backwards compatibility to type1 web services.
					
					//TODO [VILARDO] Not implemented yet
	//				LinkedHashMap<String, String> parameterlistMap = new LinkedHashMap<String, String>();
	//				AdditionalParameters[] commandParameters = null;
	//				if(commandParameters != null){
	//					for(AdditionalParameters parameter : commandParameters){
	//						parameterlistMap.put(parameter.getsParameterFlag(), parameter.getsParameterUserSpecifiedValue());
	//					}
	//				}
	//				algMap.put("parameterList", parameterlistMap);
					
					parameterMap.put("method", algorithm.getsServiceMethod());
					parameterMap.put("serviceName", algorithm.getsServiceName());
					parameterMap.put("URL", algorithm.getsAnalysisServiceURL());
					
					String jobID = "job_" + dbUtility.storeAnalysisJob(node.getDocumentRecordId(), fileList.size(), 0, algorithm.getsAnalysisServiceURL(), algorithm.getsServiceName(), algorithm.getsServiceMethod(), new Date(), ResourceUtility.getCurrentUserId());
					
					parameterMap.put("jobID", jobID);
					
					AnalysisThread t = new AnalysisThread(parameterMap, node.getDocumentRecordId(), algorithm.hasWfdbAnnotationOutput(), fileList, ResourceUtility.getCurrentUserId(), dbUtility);
					
					threadMap.add(t);
					
					jobs.put(jobID, fileNameString);
				}
			}
			
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			
			parameterMap.put("jobs", jobs);
			
			OMElement fileRet = WebServiceUtility.callWebServiceComplexParam(parameterMap, "receiveAnalysisTempFiles", ResourceUtility.getDataTransferServiceName(), ResourceUtility.getAnalysisServiceURL(), null, filesMap);
			
			OMElement status  = (OMElement)fileRet.getChildren().next();
			
			if(status != null  && Boolean.valueOf(status.getText())){
				for (AnalysisThread thread : threadMap) {
					thread.start();	
				}
			}
			
			return true;
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
			e.printStackTrace();
		}
		
		return false;
	}
	
	/** Step 0
	 * The initialization of an analysis Job. Creates an entry in the Jobs-in-Flight database, 
	 * then calls step 1A, to transfer the data files.
	 * 
	 */
	public boolean performAnalysis(FileEntry fileEntry, String userId, Algorithm alDetails ){

		Folder folder = fileEntry.getFolder();
		
		
		this.aIP.setUserId(userId);
		this.aIP.setSubjectId(folder.getName());
		this.aIP.setServiceName( alDetails.getsServiceName());
		this.aIP.setWebServiceMethod(alDetails.getsServiceMethod());
		this.aIP.setDatasetName(folder.getName());
		this.aIP.setAnalysisServiceURL(ResourceUtility.getAnalysisServiceURL());		
		
		try {
			List<FileEntry> subFiles = DLAppLocalServiceUtil.getFileEntries(ResourceUtility.getCurrentGroupId(), fileEntry.getFolderId());
		
			ArrayList<FileEntry> fileList = getFileList(alDetails, subFiles);
			
			String[] dataFileList = new String[fileList.size()];
			String fileNameString="";//filename.dat^filename.hea^";
			LinkedHashMap<String, FileEntry> filesMap = new LinkedHashMap<String, FileEntry>();
			for (int i = 0; i < fileList.size(); i++) {
				FileEntry f = fileList.get(i);
				dataFileList[i] = f.getTitle();
				fileNameString += f.getTitle() + "^";
				filesMap.put(f.getTitle(), f);
			}
			
			this.aIP.setDataFileList(dataFileList);
			
			//String sJobID = anUtil.createAnalysisJob(aIP, alDetails);
			//this.aIP.setJobID(sJobID);
			this.aIP.setJobID("TEST");
			
			LinkedHashMap<String, Object> parameterMap = new LinkedHashMap<String, Object>();
			
			LinkedHashMap<String, String> parameterlistMap = new LinkedHashMap<String, String>();
			
			AdditionalParameters[] commandParameters =  aIP.getaParameterList();
			int commandParametersCount = 0;
			if(commandParameters != null){
				for(AdditionalParameters parameter : commandParameters){
					parameterlistMap.put(parameter.getsParameterFlag(), parameter.getsParameterUserSpecifiedValue());
				}
				commandParametersCount = commandParameters.length;
			}
			
			
			parameterMap.put("jobID", aIP.getJobID());
			parameterMap.put("userID", aIP.getUserId());
			parameterMap.put("folderID", fileEntry.getFolderId());
			parameterMap.put("groupID", fileEntry.getGroupId());
			
			parameterMap.put("parameterCount", String.valueOf(commandParametersCount));
			parameterMap.put("parameterlist", parameterlistMap);
			
			parameterMap.put("fileCount", Integer.toString(fileList.size()));
			parameterMap.put("fileNames", fileNameString); // caret delimited list for backwards compatibility to type1 web services.
			
			WebServiceUtility.callWebServiceComplexParam(parameterMap,aIP.getWebServiceMethod(),aIP.getServiceName(),aIP.getAnalysisServiceURL(),null, filesMap);
		
			return true;
			
		} catch (PortalException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SystemException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return false;	
	}

	private ArrayList<FileEntry> getFileList(Algorithm algorithm, List<FileEntry> subFiles) {
		ArrayList<FileEntry> retFiles = new ArrayList<FileEntry>();
		String needExtentions = "";
		
		switch (algorithm.getType()) {
			case ANN2RR:
			case NGUESS:
			case PNNLIST:
			case TACH:
				needExtentions = ".atr.qrs.hea.dat"; break;
			case SQRS:
			case WQRS:
			case RDSAMP:
			case SIGAAMP:
			case CHESNOKOV:
				needExtentions = ".hea.dat"; break;
			case WRSAMP:
				needExtentions = ".txt"; break;
			default: break;
		}
		
		for (FileEntry file : subFiles) {
			if(needExtentions.contains(file.getExtension())){
				retFiles.add(file);
			}
		}
	
		return retFiles;
	}

	public String retrievePrimaryData(String chesnokovSubjectIds, String chesnokovFiles, String uId, boolean isPublic) {

		OMElement omeResult;

		LinkedHashMap<String, String> parameterMap = new LinkedHashMap<String, String>();

		parameterMap.put("userid", uId);
		parameterMap.put("chesSubjectids", chesnokovSubjectIds);
		parameterMap.put("chesFiles", chesnokovFiles);
		parameterMap.put("publicprivatefolder", String.valueOf(isPublic));
		parameterMap.put("service", ResourceUtility.getDataTransferClass());
		parameterMap.put("logindatetime", new Long(System.currentTimeMillis()).toString());
		parameterMap.put("verbose", String.valueOf(verbose));

		omeResult = WebServiceUtility.callWebService(parameterMap, isPublic, ResourceUtility.getConsolidatePrimaryAndDerivedDataMethod(), ResourceUtility.getNodeDataServiceName(), null);
		return omeResult.getText();
	}	

}
