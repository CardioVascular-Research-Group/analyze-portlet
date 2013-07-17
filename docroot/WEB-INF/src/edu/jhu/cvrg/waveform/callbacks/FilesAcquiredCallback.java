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

import edu.jhu.cvrg.waveform.utility.AdditionalParameters;
import edu.jhu.cvrg.waveform.utility.ProgressNotification;
import edu.jhu.cvrg.waveform.utility.AnalysisUtility;
import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

/** Step 1B FilesAcquiredCallback  and step 2A orderAnalysisWS **/
public class FilesAcquiredCallback extends SvcAxisCallback{

	//private ProgressNotification progressNotification;
	
	/** Step 1B FilesAcquiredCallback */
	public FilesAcquiredCallback() {
		super();
	}
	
	@Override
	protected void completeProcess(Map<String, Object> paramMap, String sJobID) {

		AnalysisUtility anUtil = new AnalysisUtility();
		OMElement filehandlelist = (OMElement) paramMap.get("filehandlelist");
		String[] SuccessfulList = WebServiceUtility.buildChildArray(filehandlelist);
		
		if (sJobID==null) sJobID="test";
		anUtil.addDataHandles(sJobID, SuccessfulList);

		aIP = anUtil.getJobDetails(sJobID);
		DateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar theCalendar = Calendar.getInstance();
		Date currentTime = theCalendar.getTime();
		String analysisDate = displayFormat.format(currentTime);

		ProgressNotification.step1B_FilesAcquiredCallback(aIP.getUserId(), analysisDate);
//		aIP.setDataFileList(SuccessfulList); // already taken care of by getJobDetails() above.
		orderAnalysisWS();
	}

	
	/** 2nd Step in running analysis web services.  Runs the web service, passes it the input file handles.
	 * Handles calls from filesAcquiredCallback()
	 * 
	 * @param aIP
	 * @return
	 */
	private void orderAnalysisWS() {
		
			LinkedHashMap<String, Object> parameterMap = new LinkedHashMap<String, Object>();

			//FIXME: this is done because the parameters where not set to the default values.
			//**** create the parameter subnodes of the "parameterlist" node. ************			
			LinkedHashMap<String, String> parameterlistMap = new LinkedHashMap<String, String>();
			int p=0;
			for(AdditionalParameters parameter:aIP.getaParameterList()){
				parameterlistMap.put(parameter.sParameterFlag, parameter.sParameterUserSpecifiedValue);
				p++;
			} 
//			parameterlistMap.put("fakeParam", "fake");

			//**** create the file subnodes of the filehandlelist node. ************
			String[] asDataFileHandleList = aIP.getDataHandleList();
			String fileNameType1="";
			LinkedHashMap<String, String> datafilehandlelistMap = new LinkedHashMap<String, String>();
			int f=0;
			for(String fn:asDataFileHandleList){
				datafilehandlelistMap.put("fileName_" + f, fn);
				f++;
				fileNameType1 += fn + "^";
			} 

			parameterMap.put("jobID", this.aIP.getJobID());
			parameterMap.put("verbose", "true");
			
//FIXME: this is done because the parameters where not set to the default values.
			parameterMap.put("parameterCount", String.valueOf(this.aIP.getaParameterList().length));
//			parameterMap.put("parameterCount", "1"); 
			parameterMap.put("parameterlist", parameterlistMap);
			
			parameterMap.put("fileCount", String.valueOf(asDataFileHandleList.length));
			parameterMap.put("filehandlelist", datafilehandlelistMap);
			parameterMap.put("fileNames", fileNameType1); // caret delimited list for backwards compatibility to type1 web services.
			SvcAxisCallback callback = new AnalysisDoneCallback();
//			String sWSURL = aIP.getAnalysisServiceURL();
			String sWebServiceName = aIP.getServiceName();
			String sWSMethod = aIP.getWebServiceMethod();
			String analysisServiceURL = aIP.getAnalysisServiceURL();//propsUtil.getAnalysisServiceURL();
//			WebServiceUtility.callWebService(parameterMap, true, sWSMethod, sWebServiceName, callback);
			DateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
			Calendar theCalendar = Calendar.getInstance();
			Date currentTime = theCalendar.getTime();
			String analysisDate = displayFormat.format(currentTime);

			ProgressNotification.step2A_OrderAnalysisWS(aIP.getUserId(), analysisDate);
			WebServiceUtility.callWebServiceComplexParam(parameterMap, 
					sWSMethod, 
					sWebServiceName, 
					analysisServiceURL,
					callback);
			
	}

}
