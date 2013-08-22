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
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import org.apache.axiom.om.OMElement;

import edu.jhu.cvrg.waveform.callbacks.FilesAcquiredCallback;
import edu.jhu.cvrg.waveform.callbacks.SvcAxisCallback;
import edu.jhu.cvrg.waveform.model.Algorithm;
import edu.jhu.cvrg.waveform.utility.AnalysisInProgress;
import edu.jhu.cvrg.waveform.utility.AnalysisUtility;
import edu.jhu.cvrg.waveform.utility.ProgressNotification;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

public class AnalysisManager implements Serializable{

	private static final long serialVersionUID = 1L;

	private boolean verbose = false;
	private AnalysisInProgress aIP;
	private AnalysisUtility anUtil;

	public AnalysisManager(boolean verbose){	
		
		anUtil = new AnalysisUtility(ResourceUtility.getDbUser(), ResourceUtility.getDbPassword(), ResourceUtility.getDbURI(),
				ResourceUtility.getDbDriver(), ResourceUtility.getDbMainDatabase());
		
		this.verbose = verbose;
		aIP = new AnalysisInProgress();
	}

	/** Step 0
	 * The initialization of an analysis Job. Creates an entry in the Jobs-in-Flight database, 
	 * then calls step 1A, to transfer the data files.
	 * 
	 */
	public boolean performAnalysis(String sSubjectId, String sUserId, Algorithm alDetails,
			String DatasetName, int iFileCount, String[] saFileNameList, String sFtpRelativePath){

		this.aIP.setUserId(sUserId);
		this.aIP.setSubjectId(sSubjectId);
		this.aIP.setServiceName( alDetails.sServiceName);
		this.aIP.setDatasetName(DatasetName);
		this.aIP.setAnalysisServiceURL(ResourceUtility.getAnalysisServiceURL());		
		String ftpHost = ResourceUtility.getFtpHost();
		String ftpUser = ResourceUtility.getFtpUser();
		String ftpPassword = ResourceUtility.getFtpPassword();

		String[] saFilePathNameList = new String[saFileNameList.length];
		for(int f=0;f < saFileNameList.length;f++){
			saFilePathNameList[f] = sFtpRelativePath + "/" + saFileNameList[f];
		}
		this.aIP.setDataFileList(saFilePathNameList);
		
		sFtpRelativePath = ResourceUtility.getFtpRoot() + sFtpRelativePath;

		String sJobID = anUtil.createAnalysisJob(aIP, alDetails);
		this.aIP.setJobID(sJobID);
		DateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar theCalendar = Calendar.getInstance();
		Date currentTime = theCalendar.getTime();
		String analysisDate = displayFormat.format(currentTime);
		ProgressNotification.step0_jobSubmitted(sUserId, analysisDate);

		return importDataFiles(ftpHost, ftpUser, ftpPassword, iFileCount, saFileNameList, sFtpRelativePath, sJobID);
	}

	/** Step 1A Tells analysis server to ftp the data files from the ftpHost. 
	 * Handles call from ECGridToolkit.processDynamicAnalysisCheckedBoxes() method
	 * @param ftpHost - ftp server which contains the data files
	 * @param ftpUser - userID to used by the service when connecting to ftp
	 * @param ftpPassword - password to used by the service when connecting to ftp
	 * @param analysisServiceURL - URL of the Analysis Web Service to send data files to.
	 * @param asFileNameList - files to be uploaded, without path.
	 * @param ftpRelativePath - path relative to the FTP server's starting directory.
	 * 
	 * @return - dummy value, empty NodeBrokerData class.
	 */ 
	private boolean importDataFiles(String ftpHost, String ftpUser, String ftpPassword, int fileCount, 
			String[] asFileNameList, String ftpRelativePath, String sJobID) {

		//******** old version takes a caret delimited list ************
		String fileNameString="";//filename.dat^filename.hea^";
		for(String fn:asFileNameList){
			fileNameString += fn + "^";
		}
		//*********************Should be replaced with the following when the Web service is updated *********
		//**** create the file subnodes of the fileNameList node. ************
		/*
		LinkedHashMap<String, String> fileMap = new LinkedHashMap<String, String>();
		int f=0;
		for(String fn:asFileNameList){
//			fileNameList += "\t\t<filename>" + fn + "</filename>\n";
			fileMap.put("fileName_" + f, fn);
			f++;
		} */
		//************************************

//		LinkedHashMap<String, Object> parameterMap = new LinkedHashMap<String, Object>();
		LinkedHashMap<String, String> parameterMap = new LinkedHashMap<String, String>();

		parameterMap.put("service", ResourceUtility.getDataTransferClass()); // "DataTransfer"
		parameterMap.put("fileCount", Integer.toString(fileCount)); // 
//		parameterMap.put("fileNameList", fileMap);
		parameterMap.put("fileNameList", fileNameString);
		parameterMap.put("relativePath", ftpRelativePath);
		parameterMap.put("ftpHost", ftpHost);
		parameterMap.put("ftpUser", ftpUser);
		parameterMap.put("ftpPassword", ftpPassword);
		parameterMap.put("jobID", sJobID);
		parameterMap.put("verbose", String.valueOf(verbose));
		
		System.out.println("getCopyFilesMethod: " + ResourceUtility.getCopyFilesMethod());
		System.out.println("getDataTransferServiceName: " + ResourceUtility.getDataTransferServiceName());
		System.out.println("getAnalysisServiceURL: " + ResourceUtility.getAnalysisServiceURL());

		SvcAxisCallback callback = new FilesAcquiredCallback();

//		WebServiceUtility.callWebServiceComplexParam(parameterMap, 
		WebServiceUtility.callWebService(parameterMap, 
				ResourceUtility.getCopyFilesMethod(), // Method of the service which implements the copy. e.g. "copyDataFilesToAnalysis"
				ResourceUtility.getDataTransferServiceName(), // Name of the web service. e.g. "dataTransferService"
				ResourceUtility.getAnalysisServiceURL(), // URL of the Analysis Web Service to send data files to. e.g. "http://icmv058.icm.jhu.edu:8080/axis2/services/";
				callback);
		
		DateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar theCalendar = Calendar.getInstance();
		Date currentTime = theCalendar.getTime();
		String analysisDate = displayFormat.format(currentTime);
		ProgressNotification.step1A_TransferDataFilesToAnalysisCB(this.aIP.getUserId(), analysisDate);
		return true;	
	}

	/** Calls the web service which copies the data files from the FTP repository to the analysis server (the one containing the analysis algorithm).
	 * 
	 * @param analysisBrokerURL - URL of the analysis web service. Should also contain the ICM provided "data-transfer-webservice" service.
	 * @param subjectId
	 * @param analysisName
	 * @param fileCount
	 * @param fileNameList
	 * @param ftpRelativePath
	 * @return
	 */
	/*public AnalysisInProgress transferDataFilesToAnalysis(String analysisBrokerURL, 
			String subjectId, String analysisName, int fileCount, 
			String fileNameList, String ftpRelativePath) {

		String[] SuccessfullList ={"one","two"};
		AnalysisInProgress aIP = new AnalysisInProgress();
		aIP.setSubjectId(subjectId);
		aIP.setServiceName(analysisName);
		aIP.setAnalysisServiceURL(analysisBrokerURL);

		OMElement omeResult;

		LinkedHashMap<String, String> parameterMap = new LinkedHashMap<String, String>();

		parameterMap.put("service", propsUtil.getDataTransferClass());
		parameterMap.put("fileCount", Integer.toString(fileCount));
		parameterMap.put("fileNameList", fileNameList);
		parameterMap.put("relativePath", ftpRelativePath);
		parameterMap.put("verbose", String.valueOf(verbose));

		omeResult = WebServiceUtility.callWebService(parameterMap, false, propsUtil.getTransferToAnalysisMethod(), propsUtil.getDataTransferServiceName(), null);

		Map<String, Object> paramMap = util.buildParamMap(omeResult);
		OMElement filenamelist = (OMElement) paramMap.get("filenamelist");
		SuccessfullList = util.buildChildArray(filenamelist);

		aIP.setDataFileList(SuccessfullList);
		return aIP;		
	}*/

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

	/*public String fetchAlgorithmDetail(String brokerURL) throws Exception {

		String xml="";

		try {
			
			LinkedHashMap<String, String> parameterMap = new LinkedHashMap<String, String>();
			parameterMap.put("verbose", "true");

			OMElement result = WebServiceUtility.callWebService(parameterMap, true, propsUtil.getAlgorithmDetailsMethod(), propsUtil.getPhysionetService(), null);

			// extract the XStream generated XML from the result.
			StringWriter writer = new StringWriter();
			result.serialize(XMLOutputFactory.newInstance().createXMLStreamWriter(writer));
			writer.flush();
			xml = writer.toString();

		} catch (XMLStreamException xe) {
			xe.printStackTrace();
			throw xe;
		}  catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}	

		return xml;
	}*/


}
