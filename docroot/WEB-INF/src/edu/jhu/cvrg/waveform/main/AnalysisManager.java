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
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.apache.axiom.om.OMElement;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;

import edu.jhu.cvrg.dbapi.dto.Algorithm;
import edu.jhu.cvrg.dbapi.factory.Connection;
import edu.jhu.cvrg.dbapi.factory.ConnectionFactory;
//import edu.jhu.cvrg.waveform.model.Algorithm;
import edu.jhu.cvrg.waveform.model.DocumentDragVO;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.ThreadController;
import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

public class AnalysisManager implements Serializable{

	private static final long serialVersionUID = -6155747608247379918L;
	
	private ThreadController tController;
	
	public boolean performAnalysis(List<DocumentDragVO> selectedNodes, long userId, Algorithm[] selectedAlgorithms ){
		
		try {
			Connection dbUtility = ConnectionFactory.createConnection();
			
			Set<AnalysisThread> threadSet = new HashSet<AnalysisThread>();
			
			Map<String, Object> jobs = new HashMap<String, Object>();
			Map<String, FileEntry> filesMap = new HashMap<String, FileEntry>();
			ThreadGroup analysisGroup = new ThreadGroup("AnalysisGroup");
			
			for (DocumentDragVO node : selectedNodes) {
				
				FileEntry headerFile = (FileEntry) node.getFileNode().getContent();
				
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
					
					parameterMap.put("method", algorithm.getServiceMethod());
					parameterMap.put("serviceName", algorithm.getServiceName());
					parameterMap.put("URL", algorithm.getAnalysisServiceURL());
					
					String jobID = "job_" + dbUtility.storeAnalysisJob(node.getFileNode().getDocumentRecordId(), fileList.size(), 0, algorithm.getAnalysisServiceURL(), algorithm.getServiceName(), algorithm.getServiceMethod(), new Date(), ResourceUtility.getCurrentUserId());
					
					parameterMap.put("jobID", jobID);
					
					AnalysisThread t = new AnalysisThread(parameterMap, node.getFileNode().getDocumentRecordId(), algorithm.hasWfdbAnnotationOutput(), fileList, ResourceUtility.getCurrentUserId(), dbUtility, analysisGroup);
					
					threadSet.add(t);
					
					jobs.put(jobID, fileNameString);
				}
			}
			
			Map<String, Object> parameterMap = new HashMap<String, Object>();
			
			parameterMap.put("jobs", jobs);
			
			OMElement fileRet = WebServiceUtility.callWebServiceComplexParam(parameterMap, "receiveAnalysisTempFiles", ResourceUtility.getDataTransferServiceName(), ResourceUtility.getAnalysisServiceURL(), null, filesMap);
			
			OMElement status  = (OMElement)fileRet.getChildren().next();
			
			if(status != null  && Boolean.valueOf(status.getText())){
				tController = new ThreadController(analysisGroup, threadSet);
				tController.start();
			}
			
			return true;
		} catch (PortalException e) {
			e.printStackTrace();
		} catch (SystemException e) {
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
				needExtentions = ".atr.qrs.wqrs.hea.dat"; break;
			case SQRS:
			case WQRS:
			case RDSAMP:
			case SIGAAMP:
			case CHESNOKOV:
			case SQRS2CSV:
			case WQRS2CSV:
			case SQRS4IHR:
			case WQRS4IHR:
			case SQRS4PNNLIST:
			case WQRS4PNNLIST:
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
		
		omeResult = WebServiceUtility.callWebService(parameterMap, isPublic, ResourceUtility.getConsolidatePrimaryAndDerivedDataMethod(), ResourceUtility.getNodeDataServiceName(), null);
		return omeResult.getText();
	}	

	public int getTotal() {
		return tController.getThreadCount();
	}

	
	public int getDone() {
		int done = 0;
		if(tController != null){
			Collection<AnalysisThread> tCollection = (Collection<AnalysisThread>) tController.getThreads();
			for (AnalysisThread aThread : tCollection) {
				if(aThread.isDone()){
					done++;
				}
			}
		}
		return done;
	}
	
	public List<String> getMessages() {
		List<String> messages = null;
		if(tController != null){
			Collection<AnalysisThread> tCollection = (Collection<AnalysisThread>) tController.getThreads();
			for (AnalysisThread t : tCollection) {
				if(!t.isAlive()){
					if(t.hasError()){
						if(messages == null){
							messages = new ArrayList<String>();
						}
						
						messages.add(t.getErrorMessage());
					}
				}
			}
		}
		return messages;
	}

}
