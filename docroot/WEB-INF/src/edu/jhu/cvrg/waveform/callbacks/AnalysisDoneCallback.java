package edu.jhu.cvrg.waveform.callbacks;
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
* @author Chris Jurado, Mike Shipway
* 
*/
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.Map;

import org.apache.axiom.om.OMElement;

import edu.jhu.cvrg.waveform.utility.ProgressNotification;
import edu.jhu.cvrg.waveform.utility.AnalysisUtility;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

/** Step 2B AnalysisDoneCall */
public class AnalysisDoneCallback extends SvcAxisCallback {

	//private ProgressNotification progressNotification;


	public AnalysisDoneCallback() {
		super();
	}

	@Override
	protected void completeProcess(Map<String, Object> paramMap, String sJobID) {
		
		if(sJobID!=null){
			OMElement filenamelist = (OMElement) paramMap.get("filenamelist");
			String[] sResultHandleList = WebServiceUtility.buildChildArray(filenamelist);

			AnalysisUtility anUtil = new AnalysisUtility();
			anUtil.addResultHandles(sJobID, sResultHandleList);
			aIP = anUtil.getJobDetails(sJobID);
			DateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
			Calendar theCalendar = Calendar.getInstance();
			Date currentTime = theCalendar.getTime();
			String analysisDate = displayFormat.format(currentTime);

			ProgressNotification.step2B_AnalysisDoneCallback(aIP.getUserId(), analysisDate);
			
			copyResultFilesToWaveform();
		}else{
			String error = (String) paramMap.get("error");
			System.err.println("AnalysisDoneCallback() received the following error:");
			System.err.println(error);
		}
	}
	/** Step 3A
	 * 3rd Step in running analysis web services. Tells analysis server to ftp
	 * the result files to the ftpHost.
	 */
	private void copyResultFilesToWaveform() {

		SvcAxisCallback callback = new ResultsAcquiredCallback();

		int fileCount = aIP.getResultHandleList().length;

		String[] sOrigFilePathName = aIP.getDataFileList();
		String ftpRelativePath = sOrigFilePathName[0].substring(0, sOrigFilePathName[0].lastIndexOf("/")+1);

		String ftpHost = ResourceUtility.getFtpHost();
		String ftpUser = ResourceUtility.getFtpUser();
		String ftpPassword = ResourceUtility.getFtpPassword();

		LinkedHashMap<String, String> parameterMap = new LinkedHashMap<String, String>();

		parameterMap.put("service", ResourceUtility.getDataTransferClass());
		parameterMap.put("fileNameList", aIP.getResultFilelistAsString());
		parameterMap.put("fileCount", Integer.toString(fileCount));
		parameterMap.put("relativePath", ftpRelativePath);
		parameterMap.put("ftpHost", ftpHost);
		parameterMap.put("ftpUser", ftpUser);
		parameterMap.put("ftpPassword", ftpPassword);

		parameterMap.put("jobID", aIP.getJobID());
		parameterMap.put("verbose", "true");

		WebServiceUtility.callWebService(parameterMap, true,
				ResourceUtility.getCopyResultFilesFromAnalysis(),
				ResourceUtility.getDataTransferServiceName(), callback);
		DateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar theCalendar = Calendar.getInstance();
		Date currentTime = theCalendar.getTime();
		String analysisDate = displayFormat.format(currentTime);

		ProgressNotification.step3A_CopyResultFilesToECGridToolkit(aIP.getUserId(), analysisDate);

	}
}
