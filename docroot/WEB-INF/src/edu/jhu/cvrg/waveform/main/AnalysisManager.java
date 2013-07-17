package edu.jhu.cvrg.waveform.main;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.LinkedHashMap;

import javax.faces.context.FacesContext;

import org.apache.axiom.om.OMElement;

import edu.jhu.cvrg.waveform.utility.AnalysisInProgress;
import edu.jhu.cvrg.waveform.utility.AlgorithmServiceData;
import edu.jhu.cvrg.waveform.utility.ProgressBean;
import edu.jhu.cvrg.waveform.utility.ProgressNotification;
import edu.jhu.cvrg.waveform.callbacks.FilesAcquiredCallback;
import edu.jhu.cvrg.waveform.callbacks.SvcAxisCallback;
import edu.jhu.cvrg.waveform.utility.AnalysisUtility;
import edu.jhu.cvrg.waveform.utility.CannedAlgorithmList;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

/** Contains all the functions needed for the new Web Service based analysis algorithms.
 * 
 * @author Michael Shipway, Chris Jurado, Stephen Granite
 *
 */
public class AnalysisManager {

	FacesContext context = FacesContext.getCurrentInstance();
//	private ProgressNotification progressNotification;
	private ProgressBean progressBean;// = (ProgressBean) context.getApplication().evaluateExpressionGet(context, "#{progressBean}", ProgressBean.class);
	
	private boolean verbose = false;
	private AnalysisInProgress aIP;
	private AnalysisUtility anUtil = new AnalysisUtility();

	public AnalysisManager(boolean verbose){		 
		this.verbose = verbose;
		//progressBean = (ProgressBean) context.getApplication().evaluateExpressionGet(context, "#{progressBean}", ProgressBean.class);
		aIP = new AnalysisInProgress();
	}

	/** Step 0
	 * The initialization of an analysis Job. Creates an entry in the Jobs-in-Flight database, 
	 * then calls step 1A, to transfer the data files.
	 * 
	 */
	public boolean performAnalysis(String sSubjectId, String sUserId, AlgorithmServiceData alDetails,
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
		
		// add actual ftp root for icmv058
		sFtpRelativePath = "/export/icmv058/cvrgftp" + sFtpRelativePath;

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

	public AlgorithmServiceData[] fetchAlgorithmDetailClassArray() throws Exception {

		//String urlLocation = propsUtil.getAnalysisServiceURL();

		//util.debugPrintln("BrokerServiceImpl.fetchAlgorithmDetail() passed brokerURL: "  + urlLocation);


		AlgorithmServiceData[] algorithmDetails;
		String xml="";
		try {
			
			algorithmDetails = CannedAlgorithmList.getAlgorithmList();
			
			// Temporarily disabled until the web service is ready - Brandon Benitez

/*			// set up web service call
			String sServiceURL = propsUtil.getAnalysisServiceURL(); // e.g. "http://icmv058.icm.jhu.edu:8080/axis2/services"
			String sServiceName = propsUtil.getPhysionetService(); // e.g. "/physionetAnalysisService"
			String sMethod = propsUtil.getAlgorithmDetailsMethod();

			EndpointReference targetEPR = new EndpointReference(sServiceURL + "/" + sServiceName + "/" + sMethod);


			// set up the call to the webservice
			// the OMElement creation happens here.  These OMElements will be passed in to the 
			// service as parameters.
			OMFactory fac = OMAbstractFactory.getOMFactory();

			OMNamespace omNs = fac.createOMNamespace(sServiceURL + "/" + sServiceName , sMethod);
			OMElement fetchDetails = fac.createOMElement(sMethod, omNs);


//*******************************************************list of algorithms***************************************************
//****************************************************************************************************************************			
			util.addOMEChild("verbose", String.valueOf(false), fetchDetails, fac, omNs);

			ServiceClient sender = util.getSender(targetEPR, sServiceURL + "/" + sServiceName);
			// execute up web service call, capturing the result.
			OMElement result = sender.sendReceive(fetchDetails);
			// extract the XStream generated XML from the result.
			StringWriter writer = new StringWriter();
			result.serialize(XMLOutputFactory.newInstance().createXMLStreamWriter(writer));
			writer.flush();
			xml = writer.toString();
			System.out.println("xml length: " + xml.length());

			// convert the returned XML result into an array of AlgorithmServiceData objects
			XStream xstream = new XStream();

			//						algorithmDetails = new AlgorithmServiceData[2];
			//						algorithmDetails[0] = new org.cvrgrid.ecgrid.shared.AlgorithmServiceData();
			//						algorithmDetails[1] = new org.cvrgrid.ecgrid.shared.AlgorithmServiceData();
			//						xstream.alias("People", People.class);
			//						xstream.alias("Organization", Organization.class);
			//						xstream.alias("FileTypes", FileTypes.class);
			//						xstream.alias("AdditionalParameters", AdditionalParameters.class);
			//						xstream.setClassLoader(AlgorithmServiceData.class.getClassLoader());
			//						xstream.setClassLoader(People.class.getClassLoader());
			//						xstream.setClassLoader(Organization.class.getClassLoader());
			//						xstream.setClassLoader(FileTypes.class.getClassLoader());
			//						xstream.setClassLoader(AdditionalParameters.class.getClassLoader());
			//						xstream.alias("AlgorithmServiceData", AlgorithmServiceDataECGrid.class);
			//						ret = (AlgorithmServiceDataECGrid[])xstream.fromXML(xml);
			//						util.debugPrintln("algorithmDetails length: " + ret.length);

			xstream.alias("AlgorithmServiceData", AlgorithmServiceData.class);
			algorithmDetails = (AlgorithmServiceData[]) xstream.fromXML(xml);
			util.debugPrintln("algorithmDetails length: " + algorithmDetails.length);*/


		} /*catch (AxisFault axisFault) {
			axisFault.printStackTrace();
			throw axisFault;
		}*/ catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}	

		return algorithmDetails;
	}

}
