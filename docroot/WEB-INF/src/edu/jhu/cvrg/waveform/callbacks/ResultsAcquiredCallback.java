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
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.application.FacesMessage;
import javax.faces.context.FacesContext;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.stream.FactoryConfigurationError;
import javax.xml.stream.XMLOutputFactory;
import javax.xml.stream.XMLStreamException;

import org.apache.axiom.om.OMElement;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import edu.jhu.cvrg.waveform.model.AnnotationData;
import edu.jhu.cvrg.waveform.utility.AnalysisInProgress;
import edu.jhu.cvrg.waveform.utility.AnalysisUtility;
import edu.jhu.cvrg.waveform.utility.AnnotationUtility;
import edu.jhu.cvrg.waveform.utility.FTPUtility;
import edu.jhu.cvrg.waveform.utility.ProgressNotification;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.ResultsStorageDBUtility;
import edu.jhu.cvrg.waveform.utility.ServerUtility;
import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

/** Step 3B Results Acquired Callback Step 4A Saves the result files.<BR>
 * Callback function to by called by copyDataFilesToAnalysis web service when it has finished.
 * It expects a list of data file handles (or file path/names) that the analysis machine will need to find it's local copies.
 *  
 * @return
 */
public class ResultsAcquiredCallback extends SvcAxisCallback{

	public ResultsAcquiredCallback() {
		super();
	}
	
	@Override
	protected void completeProcess(Map<String, Object> paramMap, String sJobID) {
		util.debugPrintln(" step3b beginning - completeProcess()");
		util.debugPrintln("paramMap" + paramMap);
		OMElement filenamelist = (OMElement) paramMap.get("filenamelist");
		String[] saResultFileList = WebServiceUtility.buildChildArray(filenamelist);
		util.debugPrintln("asResultFileList.length" + saResultFileList.length);
		
		AnalysisUtility anUtil = new AnalysisUtility();
		aIP = anUtil.getJobDetails(sJobID);
		aIP.setResultFileList(saResultFileList);
		DateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar theCalendar = Calendar.getInstance();
		Date currentTime = theCalendar.getTime();
		String analysisDate = displayFormat.format(currentTime);
		ProgressNotification.step3B_ResultsAcquiredCallback(aIP.getUserId(), analysisDate);
		
		recordAnalysisResults();
//		cleanupAnalysis(); // no callback, we don't care if the analysis provider cleans up it's own drives and temp files.
		//anUtil.deleteJobDetails(sJobID);  // will instead be partially transfered to main database.
	}
		
	/** 4th Step in running analysis web services. Saves the result files into the Big XML database
	 * 
	 * @param aIP
	 */
	private void recordAnalysisResults(){
		util.debugPrintln("recordAnalysisResults()");
		String[] saResultFileList = aIP.getResultFileList();
//		String[] resultFileList = aIP.getResultHandleList();
		String serviceMethod = aIP.getWebServiceMethod();
		String serviceName = aIP.getServiceName();
		String displayText = aIP.getsDisplayText();
		String subjectID = aIP.getSubjectId();
		String userID = aIP.getUserId();
		
		ResultsStorageDBUtility DBStorage = new ResultsStorageDBUtility();
		
		// Create a timestamp for storage and human readable date for display
		DateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
		DateFormat timestampFormat = new SimpleDateFormat("yyyyMMddhhmmss");
		Calendar theCalendar = Calendar.getInstance();
		Date currentTime = theCalendar.getTime();
		
		String analysisDate = displayFormat.format(currentTime);
		String timeStamp = timestampFormat.format(currentTime);
		
		util.debugPrintln("asResultFileList.length" + saResultFileList.length);
		for(String result:saResultFileList){

			
			// Create an entry in the database for each returned file 
			
			// currently missing filesizes and record names
			// record names can be created with a combination of file name and date
			// may have to open each file, but there needs to be a better way to get file sizes
			

			String recordName = subjectID + "_" + timeStamp;
			result = result.replace("//", "/");
			util.debugPrintln("StoreAnalysisFileMetadata() starting for " + result);
			DBStorage.StoreAnalysisFileMetadata(userID, subjectID, analysisDate, timeStamp, recordName, result, serviceMethod, displayText);
			util.debugPrintln("StoreAnalysisFileMetadata() completed");
			
//			if (ServerUtility.isUnix()) {
//				util.debugPrintln("This is a Unix system, therefore Physionet's rdann program can be run");
				boolean isWFDBAnnotation = bReturnsWFDBAnnotation(aIP);
				util.debugPrintln("isWFDBAnnotation: " + isWFDBAnnotation);
				if(isWFDBAnnotation){
					storeFileAsAnnotations(result);
				}else{
					util.debugPrintln("This analysis does not return a WFDB annotation file.");
				}
//			}else{
//				System.err.println("ERROR, this is not a Unix system, therefore Physionet's rdann program cannot be run to parse WFDB Annotation files.");
//			}

		}
		ProgressNotification.step4_RecordAnalysisResults(aIP.getUserId(), analysisDate);
	}

	private void cleanupAnalysis() {
	
		int dataFileCount=aIP.getDataFileList().length; 

		int resultFileCount=aIP.getResultHandleList().length; 

		String relativePath = aIP.getRelativePath(aIP.getDataFileList()[0]);

		String fileNameList = aIP.getDataFilelistAsString() + aIP.getResultFilelistAsString();

			
			LinkedHashMap<String, String> parameterMap = new LinkedHashMap<String, String>();

			parameterMap.put("service", ResourceUtility.getDataTransferClass());
			parameterMap.put("fileCount", Integer.toString(dataFileCount + resultFileCount));
			parameterMap.put("fileNameList", fileNameList);
			parameterMap.put("relativePath", relativePath);
			parameterMap.put("jobID", aIP.getJobID());
			parameterMap.put("verbose", "true");

			WebServiceUtility.callWebService(parameterMap, true, ResourceUtility.getDeleteFilesFromAnalysis(), ResourceUtility.getDataTransferServiceName(), null);

			DateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
			Calendar theCalendar = Calendar.getInstance();
			Date currentTime = theCalendar.getTime();
			String analysisDate = displayFormat.format(currentTime);

			ProgressNotification.step5_CleanupAnalysis(aIP.getUserId(), analysisDate);
	}
	
	private boolean bReturnsWFDBAnnotation(AnalysisInProgress aIP){
		boolean isWFDBAnnotation = false;
		boolean bMeth = false;
		boolean bSURL = false;
		boolean bSName = false;

		String xml = "";
		String sServiceURL = ResourceUtility.getAnalysisServiceURL(); // e.g. "http://icmv058.icm.jhu.edu:8080/axis2/services"
		String sServiceName = ResourceUtility.getPhysionetAnalysisService(); // e.g. "/physionetAnalysisService"
		String sMethod = ResourceUtility.getAlgorithmDetailsMethod();
		LinkedHashMap<String, String> parameterMap = new LinkedHashMap<String, String>();
		parameterMap.put("verbose", String.valueOf(false));
		
		OMElement result = WebServiceUtility.callWebService(parameterMap, sMethod, sServiceName, sServiceURL, null);

		StringWriter writer = new StringWriter();
		try {
			result.serialize(XMLOutputFactory.newInstance().createXMLStreamWriter(writer));

			writer.flush();
			xml = writer.toString();
			InputStream inStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document document = docBuilder.parse(inStream);

			document.getDocumentElement().normalize();

			NodeList algorithmNodes = document.getElementsByTagName("AlgorithmServiceData");

			for (int i = 0; i < algorithmNodes.getLength(); i++) {

				Node node = algorithmNodes.item(i);
				String analysisServiceURL = "";
				String serviceName = "";
				String serviceMethod = "";

				for (int s = 0; s < node.getChildNodes().getLength(); s++) {
					Node childNode = node.getChildNodes().item(s);

					if (childNode.getNodeName().equals("sAnalysisServiceURL")) {
						analysisServiceURL = childNode.getFirstChild().getNodeValue();
					}
					if (childNode.getNodeName().equals("sServiceName")) {
						serviceName = childNode.getFirstChild().getNodeValue();
					}
					if (childNode.getNodeName().equals("sServiceMethod")) {
						serviceMethod = childNode.getFirstChild().getNodeValue();
					}

					bSURL = analysisServiceURL.equals(aIP.getAnalysisServiceURL());
					bSName = serviceName.equals(aIP.getServiceName());
					bMeth = serviceMethod.equals(aIP.getWebServiceMethod());
				}
				
				if(bSURL & bSName & bMeth){
					for (int j = 0; j < node.getChildNodes().getLength(); j++) {
						Node childNode = node.getChildNodes().item(j);
					
						if (childNode.getNodeName().equals("afOutFileTypes")) {
							NodeList fileTypeNodes = childNode.getChildNodes();
							String sName = "";
							for (int f = 0; f < fileTypeNodes.getLength(); f++) {
								Node fileTypeNode = fileTypeNodes.item(f);
								if (fileTypeNode.getNodeName().equals("serviceDescriptionData.FileTypes")) {
									sName = fileTypeNode.getFirstChild().getFirstChild().getNodeValue();
								}
								if (sName.equals("WFDBqrsAnnotation")) {
									isWFDBAnnotation = true;
								}
							}
						}
					}
				}
			}
		
		} catch (XMLStreamException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (FactoryConfigurationError e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (UnsupportedEncodingException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (ParserConfigurationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SAXException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return isWFDBAnnotation;
	}
	
	/** Fetches a copy of a WFDB annotation file, parses it using rdann from the Physionet library,<BR>
	 *  then saves the individual annotations found therein into the XML database.
	 *  @param sFileName - 
	**/ 
	private boolean storeFileAsAnnotations(String sFileName){
		boolean success = true;
		List<String[]> alistAnnotation;
		
		util.debugPrintln("storeFileAsAnnotations() This should be a WFDB annotation file:" + sFileName);
		
		// downloads the bareFile into sLocalPath + fileName
		if (ServerUtility.isUnix()) {
			String sPath = AnalysisUtility.extractPath(sFileName);
			String sBareFilename = AnalysisUtility.extractName(sFileName);
			String sLocalPath = ResourceUtility.getLocalDownloadFolder();
			FTPUtility.downloadFromRemote(sPath, sBareFilename);
			alistAnnotation = execute_rdann(sLocalPath, sBareFilename);
		}else{
			alistAnnotation = FAKE_execute_rdann();
		}
		alistAnnotation = changePhysioBankToOntology(alistAnnotation);
		success = storeAnnotationList(alistAnnotation);
		
		return success;
	}

	/** Reads a WFDB annotation file and returns an ArrayList of String arrays containing the data from the WFDB annotation file.<BR>
	 * <BR>
	 * Below is an example of  first 2 1/2 seconds from wqrs run on twa01.hea,twa01.dat<BR>
	 * Command that makes the annotation file:  wqrs -r twa01 -v -j<BR>
	 * Command that parses the annotation file: rdann -x -v -r twa01 -a wqrs -t 2.5<BR>
	 array[0]     [1]        [2]    [3]  [4]  [5]  [6]      [7]		 <BR>
	  Seconds   Minutes     Hours  Type  Sub Chan  Num      Aux<BR>
	    0.194   0.00323 0.0000539     N    0    0    0<BR>
	    0.260   0.00433 0.0000722     )    0    0    0<BR>
	    0.752   0.01253 0.0002089     N    0    0    0<BR>
	    0.818   0.01363 0.0002272     )    0    0    0<BR>
	    1.288   0.02147 0.0003578     N    0    0    0<BR>
	    1.354   0.02257 0.0003761     )    0    0    0<BR>
	    1.820   0.03033 0.0005056     N    0    0    0<BR>
	    1.888   0.03147 0.0005244     )    0    0    0<BR>
	    2.362   0.03937 0.0006561     N    0    0    0<BR>
	    2.428   0.04047 0.0006744     )    0    0    0<BR>
	    @see  http://www.physionet.org/physiobank/annotations.shtml
	    @return In each String array the elements contain the following data:<BR>
	 * [0] - Seconds, total time from the beginning of the file in decimal seconds, to 3 decimal places.<BR>
	 * [1] - Minutes, same value as Seconds above, but in units of Minutes, to 5 decimal places (e.g. Seconds/60.0)<BR>
	 * [2] - Hours, same value as Seconds above, but in units of Hours, to 7 decimal places (e.g. Seconds/3600.0)<BR>
	 * [3] - Type, PhysioBank Annotation Code, <BR>
	 * [4] - Sub, Subtype - context-dependent attribute (see the documentation for each Physionet database for details), it's always been zero on our sample data.<BR>
	 * [5] - Chan, Channel(lead) number, zero base integer. (e.g. 0 = leadI, 5 = leadV1)<BR>
	 * [6] - Num, context-dependent attribute (see the documentation for each Physionet database for details), it's always been zero on our sample data.<BR>
	 * [7] - Aux, a free text string, it's always been blank on our sample data.<BR>
	**/
	private List<String[]> execute_rdann(String sLocalPath, String sBareFilename){
		System.out.println("execute_rdann(), only works on Linux with the WFBD library installed.");
		List<String[]> alistAnnotation = new ArrayList<String[]>();
		
		// build command string
		String[] asEnvVar = new String[0];   
		int iIndexPeriod = sBareFilename.lastIndexOf(".");
		String sRecord = sBareFilename.substring(0, iIndexPeriod);
		String annotator = sBareFilename.substring(iIndexPeriod+1); // annotator is the suffix (extension) of the bare name of the annotation file
		try { // rdann -v -x -r twa01 -a wqrs
			String sCommand = "/opt/wfdb/bin/rdann -x -r " + sRecord + " -a " + annotator;
			boolean bRet = util.executeCommand(sCommand, asEnvVar, sLocalPath);
			String line;
				
		    int lineNum = 0;
		    String[] columns;
		    //iterate thru the returned text of the command, one line per annotation.");
//			util.stdReturnHandler();  // replaced with while loop below for specific behavior.
		    while ((line = util.stdInputBuffer.readLine()) != null) {
		    	if(lineNum<10){
		    		System.out.println(lineNum + " of 1st 10) " + line);
		    	}
				// columns: Seconds   Minutes     Hours  Type  Sub Chan  Num      Aux
				columns = line.split("\\s+");
				alistAnnotation.add(columns);
				lineNum++;
			}
			util.debugPrintln("--- execute_rdann() found " + lineNum + " annotations");
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return alistAnnotation;
	}
	
	/** Returns FAKE DATA FOR TESTING ON WINDOWS BOXES.<BR>
	 * This data is in the same format as returned by execute_rdann() on a Linux system with the WFBD library installed.<BR>
	 * The test data consists of the first 4 annotations from wqrs run on twa01.hea,twa01.dat<BR>
	 * Command that makes the annotation file:  wqrs -r twa01 -v -j<BR>
	 * Command that parses the annotation file: rdann -x -v -r twa01 -a wqrs -t 2.5<BR>
	 array[0]     [1]        [2]    [3]  [4]  [5]  [6]      [7]		 <BR>
	  Seconds   Minutes     Hours  Type  Sub Chan  Num      Aux<BR>
	    0.194   0.00323 0.0000539     N    0    0    0<BR>
	    0.260   0.00433 0.0000722     )    0    0    0<BR>
	    0.752   0.01253 0.0002089     N    0    0    0<BR>
	    0.818   0.01363 0.0002272     )    0    0    0<BR>
	*/
	private List<String[]> FAKE_execute_rdann(){
		List<String[]> alistAnnotation = new ArrayList<String[]>();
		System.out.println("FAKE_execute_rdann() being run because this in not a Linux system");
		System.out.println("Returning 4 FAKE annotations");
		
		// in Windows, create fake data for testing.
		String[] column0 = {"0.194","0.00323","0.0000539","N","0","0","0"};
		String[] column1 = {"0.260","0.00433","0.0000722",")","0","0","0"};
		String[] column2 = {"0.752","0.01253","0.0002089","N","0","0","0"};
		String[] column3 = {"0.818","0.01363","0.0002272",")","0","0","0"};
		// add these fake data t the ArrayList
		alistAnnotation.add(column0);
		alistAnnotation.add(column1);
		alistAnnotation.add(column2);
		alistAnnotation.add(column3);

		return alistAnnotation;
	}
	
	
	
	/** Change all of the values from PhysioBank Annotation Codes to the corresponding Bioportal ECG ontology IDs.
	 * STUB METHOD
	 * @param annotationMap - the original annotations with Key =  time stamp, and Value = PhysioBank Annotation Codes
	 * @return - same as the passed in LinkedHashMap but with the Values as Bioportal ECG ontology IDs.
	 */
	private List<String[]> changePhysioBankToOntology(List<String[]>  alistAnnotation){
		// TODO STUB METHOD-Needs to be fully implemented.
//		util.debugPrintln("changePhysioBankToOntology()");
		System.out.println("- changePhysioBankToOntology() alistAnnotation.size():" + alistAnnotation.size());
		String sPhysioBankCode="";
		for(String[] saAnnot : alistAnnotation){
			sPhysioBankCode = saAnnot[3];
			if(sPhysioBankCode.equals("N")){
				saAnnot[3] = "ECGTermsv1:ECG_000000023";
			}else if(sPhysioBankCode.equals(")")){
				saAnnot[3] = "ECGTermsv1:ECG_000000236";
			}							
		}
		return alistAnnotation;
	}
	
	/** Stores the annotations from the ArrayList of String arrays generated by execute_rdann().<BR>
	 * Data are stored in the XML database using AnnotationUtility.storeLeadAnnotationNode().
	 * 
	 * @param alistAnnotation - output of execute_rdann() or changePhysioBankToOntology()
	 * @return true if all stored successfully
	 */	 
	private boolean storeAnnotationList(List<String[]> alistAnnotation){
//		System.out.println("storeAnnotationList()");
//		System.out.println("-alistAnnotation.size():" + alistAnnotation.size());
	    AnnotationUtility annUtil = new AnnotationUtility();
		
//		 * Required values that need to be filled in are:
//			 * 
//			 * created by (x) - the source of this annotation (whether it came from an algorithm or was entered manually)
//			 * concept label - the type of annotation as defined in the annotation's bioportal reference term
//			 * annotation ID - a unique ID used for easy retrieval of the annotation in the database
//			 * onset label - the bioportal reference term for the onset position.  This indicates the start point of an interval
//			 * 					or the location of a single point
//			 * onset y-coordinate - the y coordinate for that point on the ECG wave
//			 * onset t-coordinate - the t coordinate for that point on the ECG wave.
//			 * an "isInterval" boolean - for determining whether this is an interval (and thus needs an offset tag)
//	    	 * Full text description - This is the "value" so to speak, and contains the full definition of the annotation type being used
//			 * 
//			 * Note:  If this is an interval, then an offset label, y-coordinate, and t-coordinate are required for that as well.
		
		for(String[] saAnnot : alistAnnotation){
//			System.out.println("-- saAnnot.length:" + saAnnot.length);

			if( (saAnnot != null) & (saAnnot.length>=6)){
				try {
					double dMilliSec = Double.parseDouble(saAnnot[0]);
					String sOntologyID = saAnnot[3];
					int iLeadIndex = Integer.parseInt(saAnnot[5]);
					double dMicroVolt = lookupVoltage(dMilliSec,iLeadIndex);
//					System.out.println("-- calling WebServiceUtility.lookupOntologyDefinition()");
					String[] saOntDetails = WebServiceUtility.lookupOntologyDefinition(sOntologyID); // ECGTermsv1:ECG_000000103
					String sTermName = saOntDetails[0];
					String sFullAnnotation=saOntDetails[1];
//					System.out.println("-- dMilliSec, dMicroVolt, sTermName, sOntologyID: " +  dMilliSec + ", " + dMicroVolt + ", " + sTermName + ", " +  sOntologyID);
					//****************************
	//				FacesContext context = FacesContext.getCurrentInstance();
					AnnotationData annotationToInsert = annUtil.createAnnotationData();
	
					annotationToInsert.setSubjectID(aIP.getSubjectId());
					annotationToInsert.setDatasetName(aIP.getDatasetName());
					annotationToInsert.setUserID(aIP.getUserId());
	
					annotationToInsert.setCreator(aIP.getJobID()); // GUID to be used as a foreign key to 
					annotationToInsert.setLeadIndex(iLeadIndex);
					annotationToInsert.setLeadName(util.guessLeadName(iLeadIndex, 12));
					annotationToInsert.setMicroVoltStart(dMicroVolt);
					annotationToInsert.setMilliSecondStart(dMilliSec);
	
					annotationToInsert.setConceptLabel(sTermName);
					annotationToInsert.setConceptID(sOntologyID);
					annotationToInsert.setAnnotation(sFullAnnotation);
					
	//			annotationToInsert.setMicroVoltEnd(0.0);
	//			annotationToInsert.setMilliSecondEnd(0.0);
					annotationToInsert.setIsSinglePoint(true);
	
	 //*****************************************
					 
					// Inserting save to XML database
					 boolean insertionSuccess = annUtil.storeLeadAnnotationNode(annotationToInsert);
					 if(!insertionSuccess) {
					        //add facesmessage
					        FacesContext.getCurrentInstance().addMessage(null, new FacesMessage("Failure", "Annotation did not save properly"));
					 }
				} catch (NumberFormatException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (Exception e){
					System.err.println(e.getMessage());
					e.printStackTrace();
				}
			}else{
				System.out.println("-- ERROR bad annotatation");
			}
		}
		
		return false;
	}
	
	/** Looks up the voltage value for the specified timestamp on the specified lead index.
	 * 
	 * @param dMilliSec
	 * @param iLeadIndex
	 * @return
	 */
	private double lookupVoltage(double dMilliSec,int iLeadIndex){
		//TODO implement this, for now zero will work because the dygraph annotation display code ignores the y axis value.
		return 0.0;		
	}
}
