package edu.jhu.cvrg.waveform.utility;

import java.io.Serializable;
import java.util.UUID;

import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

import edu.jhu.cvrg.dbapi.XMLUtility;
import edu.jhu.cvrg.waveform.model.Algorithm;
import edu.jhu.cvrg.waveform.utility.AnalysisInProgress;
import edu.jhu.cvrg.waveform.utility.AdditionalParameters;
import edu.jhu.cvrg.waveform.utility.AnalysisProgressQueryBuilder;

public class AnalysisUtility extends XMLUtility implements Serializable{

	private static final long serialVersionUID = 1L;
	private AnalysisProgressQueryBuilder analysisInFlightBuilder;
	private AnalysisProgressQueryBuilder analysisInsertion;

	public AnalysisUtility() {
		super(com.liferay.util.portlet.PortletProps.get("dbUser"),
				com.liferay.util.portlet.PortletProps.get("dbPassword"), 
				com.liferay.util.portlet.PortletProps.get("dbURI"),	
				com.liferay.util.portlet.PortletProps.get("dbDriver"), 
				com.liferay.util.portlet.PortletProps.get("dbMainDatabase"));

		// This is for handling Analyses that are in progress
		analysisInFlightBuilder = new AnalysisProgressQueryBuilder(this.dbURI, ResourceUtility.getAnalysisDatabase());
		analysisInsertion = new AnalysisProgressQueryBuilder(this.dbURI, this.dbMainCollection);

		// This is for handling Analysis Results files
		//analysisResultsBuilder
	}

	public AnalysisUtility(String dbUserName, String dbUserPassword, String dbURI, String dbDriver, String dbMainDatabase) {
		super(dbUserName, dbUserPassword, dbURI, dbDriver, dbMainDatabase);

		// This is for handling Analyses that are in progress
		analysisInFlightBuilder = new AnalysisProgressQueryBuilder(dbURI, dbMainDatabase);
		analysisInsertion = new AnalysisProgressQueryBuilder(dbURI, this.dbMainCollection);
	}
	
	public static String extractPath(String sHeaderPathName){
		String sFilePath="";
		int iIndexLastSlash = sHeaderPathName.lastIndexOf("/");
		sFilePath = sHeaderPathName.substring(0,iIndexLastSlash+1);

		return sFilePath;
	}

	public static String extractName(String sFilePathName){
		String sFileName="";
		int iIndexLastSlash = sFilePathName.lastIndexOf("/");
		sFileName = sFilePathName.substring(iIndexLastSlash+1);

		return sFileName;
	}

	private String performQuery(String sQuery){
		String result = "";
		try {
			
			ResourceSet jobDetailResultSet = executeQuery(sQuery);
			if (jobDetailResultSet.getSize() > 0) {
				ResourceIterator iter = jobDetailResultSet.getIterator();
				Resource selection = iter.nextResource();
				if (selection.getContent().toString().endsWith(">")) {
					result = stripTags((selection.getContent()).toString());			
				} else {
					result = selection.getContent().toString();
				}
			} 
			
		}
		catch (Exception ex) {
			System.out.println("AnalysisUtility.performQuery():  AN EXCEPTION HAS BEEN CAUGHT!  IF A LIST IS RETURNED, IT WILL BE EMPTY!!!");
			ex.printStackTrace();
		}
		return result;		 
	}

	private void performUpdateQuery(String sQuery){
		try {
			executeQuery(sQuery);
		}
		catch (Exception ex) {
			System.out.println("AnalysisUtility.performQuery():  AN EXCEPTION HAS BEEN CAUGHT!  IF A LIST IS RETURNED, IT WILL BE EMPTY!!!");
			ex.printStackTrace();
		}
	}

	
	public boolean deleteJobDetails(String sJobID){
		boolean success = true;
		try {
			String sForCollection =  analysisInFlightBuilder.defaultFor();
			String sWhereClause   = "	where jobId=\"" + sJobID + "\" \n";
			String sUpdateClause  = analysisInFlightBuilder.delete("", "");
			String sDestination   = "	$x\n";
			String sQuery = sForCollection + sWhereClause + sUpdateClause + sDestination;

			executeQuery(sQuery);
			success = true;
		} catch (Exception e) {
			e.printStackTrace();
		}			
		return success;
	}

	public String getUserId(String sJobID){
		String sForCollection	= analysisInFlightBuilder.defaultFor();
		String sWhereClause		= "	where jobId='" + sJobID + "' \n";
		String sReturn			= "	return $x/userId\n";
		String sQuery = sForCollection + sWhereClause + sReturn;

		return performQuery(sQuery);
	}

	public  String getSubjectId(String sJobID){
		String sForCollection	= analysisInFlightBuilder.defaultFor();
		String sWhereClause		= "	where jobId='" + sJobID + "' \n";
		String sReturn			= "	return $x/subjectId\n";
		String sQuery = sForCollection + sWhereClause + sReturn;

		return performQuery(sQuery);
	}
	
	
	public  String getDatasetName(String sJobID){
		String sForCollection	= analysisInFlightBuilder.defaultFor();
		String sWhereClause		= "	where jobId='" + sJobID + "' \n";
		String sReturn			= "	return $x/datasetName\n";
		String sQuery = sForCollection + sWhereClause + sReturn;

		return performQuery(sQuery);
	}
	

	public  String getWebServiceName(String sJobID){
		String sForCollection	= analysisInFlightBuilder.defaultFor();
		String sWhereClause		= "	where jobId='" + sJobID + "' \n";
		String sReturn			= "	return $x/algorithmToProcessWith/algorithm/algorithmName\n";
		String sQuery = sForCollection + sWhereClause + sReturn;

		return performQuery(sQuery);
	}

	public  String getWebServiceDisplayText(String sJobID){
		String sForCollection	= analysisInFlightBuilder.defaultFor();
		String sWhereClause		= "	where jobId='" + sJobID + "' \n";
		String sReturn			= "	return $x/algorithmToProcessWith/algorithm/algorithmDisplayText\n";
		String sQuery = sForCollection + sWhereClause + sReturn;

		return performQuery(sQuery);
	}

	public  String getWebServiceMethod(String sJobID){
		String sForCollection	= analysisInFlightBuilder.defaultFor();
		String sWhereClause		= "	where jobId='" + sJobID + "' \n";
		String sReturn			= "	return $x/algorithmToProcessWith/algorithm/algorithmMethod\n";
		String sQuery = sForCollection + sWhereClause + sReturn;

		return performQuery(sQuery);
	}



	public  String getAlgorithmLocation(String sJobID){
		String sForCollection	= analysisInFlightBuilder.defaultFor();
		String sWhereClause		= "	where jobId='" + sJobID + "' \n";
		String sReturn			= "	return $x/algorithmToProcessWith/algorithm/algorithmLocation\n";
		String sQuery = sForCollection + sWhereClause + sReturn;

		return performQuery(sQuery);
	}

	public  AnalysisInProgress getJobDetails(String sJobID){
		AnalysisInProgress jobDetailOld = new AnalysisInProgress();

		jobDetailOld.setJobID(sJobID);
		jobDetailOld.setUserId(getUserId(sJobID));
		jobDetailOld.setSubjectId(getSubjectId(sJobID));
		jobDetailOld.setDatasetName(getDatasetName(sJobID));
		jobDetailOld.setServiceName(getWebServiceName(sJobID)); // e.g. "physionetAnalysisService"
		jobDetailOld.setWebServiceMethod(getWebServiceMethod(sJobID)); // e.g. sqrsWrapperType2
		jobDetailOld.setsDisplayText(getWebServiceDisplayText(sJobID)); // e.g. Detect QRS (sqrs)
		jobDetailOld.setAnalysisServiceURL(getAlgorithmLocation(sJobID));
		jobDetailOld.setaParameterList(getParameterList(sJobID));


		jobDetailOld.setDataFileList(getFilenameList(sJobID));
		jobDetailOld.setDataHandleList(getDataHandleList(sJobID));
		jobDetailOld.setResultHandleList(getResultHandleList(sJobID));

		return jobDetailOld;
	}

	public  AdditionalParameters[] getParameterList(String sJobID){
		AdditionalParameters[] paramList = null;

		try {
			String sForCollection	= analysisInFlightBuilder.defaultFor();
			String sWhereClause		= "	where jobId='" + sJobID + "' \n";
			String sReturn			= "	return $x/algorithmToProcessWith/parameters\n";
			String sQuery = sForCollection + sWhereClause + sReturn;

			ResourceSet ParamListResultSet =  executeQuery(sQuery);
			//					 util.debugPrintln(ParamListResultSet.toString());

			ResourceIterator iter = ParamListResultSet.getIterator();
			Resource selection = null;
			paramList = new AdditionalParameters[(int) ParamListResultSet.getSize()];
			int it=0;
			while(iter.hasMoreResources()) {
				selection = iter.nextResource();
				String jobDetailResultXML = (selection.getContent()).toString();
				paramList[it] = parseParamTags(jobDetailResultXML);
				it++;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return paramList;
	}

	public  String[] getDataHandleList(String sJobID){
		String sReturn	 = "	return $x/dataFileHandle/fileName\n";
		return getHandleList(sJobID, sReturn);
	}


	public  String[] getFilenameList(String sJobID){
		String sReturn = "	return $x/filesToProcess/file\n";
		return getHandleList(sJobID, sReturn);
	}

	public  String[] getResultHandleList(String sJobID){
		String sReturn = "	return $x/resultFileHandle/fileName\n";
		return getHandleList(sJobID, sReturn);
	}

	private  String[] getHandleList(String sJobID, String sReturn){
		String[] fileName = null;
		try {
			String sForCollection	= analysisInFlightBuilder.defaultFor();
			String sWhereClause		= "	where jobId='" + sJobID + "' \n";
			String sQuery = sForCollection + sWhereClause + sReturn;

			ResourceSet jobDetailResultSet =  executeQuery(sQuery);
			//					 util.debugPrintln(jobDetailResultSet.toString());

			ResourceIterator iter = jobDetailResultSet.getIterator();
			Resource selection = null;
			fileName = new String[(int) jobDetailResultSet.getSize()];
			int it=0;
			while(iter.hasMoreResources()) {
				selection = iter.nextResource();
				String jobDetailResultXML = (selection.getContent()).toString();
				fileName[it] = stripTags(jobDetailResultXML);
				it++;
			}
		}
		catch (Exception ex) {
			ex.printStackTrace();
		}
		return fileName;
	}

	// This creates the actual record for the main database
	public  boolean createAnalysisFile(String userID, String subjectID, String fileName, String analyzeName) {

		//*****************************************************************************************************************************
		//FIXME:  Once ready, the new ResultsStorageDBUtility object will handle this functionality.  This method will make 
		// the appropriate calls to that object's methods
		//
		// BB, 4/9/2013
		//*****************************************************************************************************************************

		String newECGFile = "<record> <studyEntry> <study>Mesa</study> <datatype>Rhythm Strips</datatype> <subjectID>" + subjectID + " </subjectID> <submitterID>" + userID + "</submitterID> <recordName>" + subjectID + "_" + analyzeName + "</recordName> " 
		+ "<fileFormat>1</fileFormat> <recordDetails> <fileDetails> <fileLocation>" + fileName + "</fileLocation> <fileSizeBytes>12000</fileSizeBytes> </fileDetails>" + 
		"<samplingRate></samplingRate> <leadCount></leadCount> <numberOfPoints></numberOfPoints> <subjectGender>Unknown</subjectGender> <subjectAgeAtECGRecording></subjectAgeAtECGRecording>" +  
		"<dateOfRecording></dateOfRecording> </recordDetails> </studyEntry></record>";  

		// TODO:  Some annotations operate on leads, while others do not.  There will be no leads for now

		String query = analysisInsertion.create(subjectID, analyzeName, newECGFile);

		executeQuery(query);

		return true;
	}

//	public  String createAnalysisJob(String sSubjectId, String sUserId,  String[] asFileNameList, AlgorithmServiceData alDetails, String sServiceName){
	public  String createAnalysisJob(AnalysisInProgress aIP, Algorithm alDetails){
		UUID uuid = UUID.randomUUID();
		String sJobID = uuid.toString();
		String sSubjectId  = aIP.getSubjectId();
		String sUserId  = aIP.getUserId();
		String[] asFileNameList  = aIP.getDataFileList();
		String DatasetName  = aIP.getDatasetName();

		String filesToProcess = createFilesToProcess(asFileNameList);
		String algorithmToProcessWith = createAlgorithmToProcessWith(alDetails);

		String sXml = 
			"<job>\n" +
			"	<jobId>" + sJobID +"</jobId>\n" +
			"	<userId>" + sUserId +"</userId>\n" +
			"	<subjectId>" + sSubjectId +"</subjectId>\n" +
			"	<datasetName>" + DatasetName +"</datasetName>\n" +
			filesToProcess +
			algorithmToProcessWith + 
			"</job>\n";

		String sForCollection =  analysisInFlightBuilder.ecgFor();
		String sUpdateClause  = "	return update insert \n";
		String sDestination   = "\n	into $x\n";
		String sQuery = sForCollection + sUpdateClause + sXml + sDestination;

		executeQuery(sQuery);

		return sJobID;
	}

	public  String createFilesToProcess(String[] asFileNameList){
		String filesToProcess = "	<filesToProcess>\n";
		for(int f=0;f < asFileNameList.length;f++){
			filesToProcess += 
				"		<file>" +
				asFileNameList[f] + "\n" + 
				"		</file>\n";
		}
		filesToProcess += "	</filesToProcess>\n";

		return filesToProcess;
	}

	public  String createAlgorithmToProcessWith(Algorithm alDetails){
		String AlgorithmToProcess = "	<algorithmToProcessWith>\n" + 
		"		<algorithm>\n" +
		"			<algorithmName>" + alDetails.sServiceName + "</algorithmName>\n" +
		"			<algorithmDisplayText>" + alDetails.sDisplayShortName + "</algorithmDisplayText>\n" +
		"			<algorithmMethod>" + alDetails.sServiceMethod + "</algorithmMethod>\n" +
		"			<algorithmLocation>" + alDetails.sAnalysisServiceURL + "</algorithmLocation>\n" +
		"		</algorithm>\n";

		for(int p=0;p<alDetails.aParameters.length;p++){
			AlgorithmToProcess +=
				"		<parameters>\n" +
				"			<name>" + alDetails.aParameters[p].sParameterFlag + "</name>\n" +
				//FIXME: when annotation parameters are implemented			"			<value>" + alDetails.aParameters[p].sParameterUserSpecifiedValue + "</value>\n" +
				"			<value>" + alDetails.aParameters[p].sParameterDefaultValue + "</value>\n" +
				"			<description>" + alDetails.aParameters[p].sToolTipDescription + "</description>\n" +
				"		</parameters>\n";
		}

		AlgorithmToProcess += "	</algorithmToProcessWith>\n";

		return AlgorithmToProcess;
	}
	
	// This creates the actual record for the Progress Accumulator
	public  boolean createProgressAccumulator(String sUserId, String sDate) {

		String sXml = 
				"<accumulator>\n" +
				"	<userId>" + sUserId +"</userId>\n" +
				"	<jobCount>0</jobCount>\n" +
				"	<jobCompleteCount>0</jobCompleteCount>\n" +
				"	<percentAccumulation>0</percentAccumulation>\n" +
				"	<dateOfAccumulation>" + sDate +"</dateOfAccumulation>\n" +
				"</accumulator>\n";

		String sForCollection =  analysisInFlightBuilder.progressFor();
		String sUpdateClause  = "	return update insert \n";
		String sDestination   = "\n	into $x\n";
		String sQuery = sForCollection + sUpdateClause + sXml + sDestination;

		executeQuery(sQuery);

		return true;
	}


	/** Adds the data handle list to the specified Job in flight database entry.*/
	public  boolean addDataHandles(String sJobID, String[] dataHandleList){
		try {
			String sForCollection =  analysisInFlightBuilder.defaultFor();
			String sWhereClause   = "	where jobId=\"" + sJobID + "\" \n";
			String sUpdateClause  = "	return update insert \n";
			String sDestination   = "\n	into $x\n";

			return addHandles(sForCollection, sWhereClause, sUpdateClause, sDestination, dataHandleList, "data");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}			
	}

	/** Adds the result handle list to the specified Job in flight entry.*/
	public  boolean addResultHandles(String sJobID, String[] resultHandleList){
		try {
			String sForCollection =  analysisInFlightBuilder.defaultFor();
			String sWhereClause   = "	where jobId=\"" + sJobID + "\" \n";
			String sUpdateClause  = "	return update insert \n";
			String sDestination   = "\n	into $x\n";

			return addHandles(sForCollection, sWhereClause, sUpdateClause, sDestination, resultHandleList, "result");

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}			

	}	 

	private  boolean addHandles(String forCollection, String whereClause, String updateClause, String destination, String[] handleList, String type){
		String sXml = "";
		try{
			for(int d = 0; d < handleList.length; d++){				
				sXml = 
					"		<" + type + "FileHandle>\n" +
					"			<location>na</location>\n" +
					"			<filePath>na</filePath>\n" +
					"			<fileName>" + handleList[d] + "</fileName>\n" +
					"		</" + type + "FileHandle>\n";
				String sQuery = forCollection + whereClause + updateClause + sXml + destination;
				executeQuery(sQuery);
			}
		}catch (Exception e) {
			e.printStackTrace();
			return false;
		}	
		return true;
	}	

	/** Gets the total accumulation of Jobs in Flight progress percentages for the day.
	 * Returns 3 integer values in an array: job count, jobs completed count, sum of all percentage dones.
	 * @param sUserId - user login which launched the analysis jobs. e.g. "mshipwa1"
	 * @param sDate - date of interest e.g. "1/2/2013"
	 * @return
	 */
	public int[] getProgressAccumulation(String sUserId, String sDate){
		int[] accumulation = new int[3];
		String[] saResultArray = new String[3];
		
		String sForCollection	= analysisInFlightBuilder.progressAccumFor();
		String sWhereClause		= 	"	where userId='" + sUserId + "'  \n" +
									"	and dateOfAccumulation='" + sDate + "' \n"; 
		String sReturn			= 	"return ( " +
									"	for $accum in $x\n" +
									"		let $count := $accum/jobCount\n" +
									"		let $done := $accum/jobCompleteCount\n" +
									"		let $perc := $accum/percentAccumulation\n" +
									"		let $result := fn:concat(fn:data($count),',',fn:data($done),',',fn:data($perc)) \n" +
									"	return $result \n" +
									")\n";
		String sQuery = sForCollection + sWhereClause + sReturn;
	
		String sResult = performQuery(sQuery);
		
		if (sResult.length() > 0) {
			saResultArray = sResult.split(",");
			for(int i=0;i<saResultArray.length;i++){
				accumulation[i] = Integer.parseInt(saResultArray[i]);
			}
		} else if (sResult.isEmpty()) {
			for(int i=0;i<3;i++){
				accumulation[i] = 0;
			}
		}
		
		return accumulation;
	}

	/** Sets the accumulated progress percentage for the day of  Jobs in Flight.
	 * 
	 * @param sUserId - user login which launched the analysis jobs. e.g. "mshipwa1"
	 * @param sDate - date of interest e.g. "1/2/2013"
	 * @param iNewPercent - new sum of percentages to store, may be more that 100. Divide by job count for average %.  
	 * @return - success/fail
	 */
	public boolean setProgressPercentAccumulation(String sUserId, String sDate, int iNewPercent){
		boolean bSuccess = true;
		
		try {
			String sForCollection	= analysisInFlightBuilder.progressAccumFor();
			String sWhereClause		= 	"	where userId='" + sUserId + "'  \n" +
										"	and dateOfAccumulation='" + sDate + "' \n"; 
			String sReturn			= 	"return update value $x/percentAccumulation with '" + iNewPercent + "'\n";
			String sQuery = sForCollection + sWhereClause + sReturn;

			performUpdateQuery(sQuery);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bSuccess = false;
		}
		
		return bSuccess;
	}

	/** Sets the accumulated progress jobs complete for the day of  Jobs in Flight.
	 * 
	 * @param sUserId - user login which launched the analysis jobs. e.g. "mshipwa1"
	 * @param sDate - date of interest e.g. "1/2/2013"
	 * @param iJobsComplete - new job complete total to store.  
	 * @return - success/fail
	 */
	public boolean setProgressJobComplete(String sUserId, String sDate, int iJobsComplete){
		boolean bSuccess = true;
		
		try {
			String sForCollection	= analysisInFlightBuilder.progressAccumFor();
			String sWhereClause		= 	"	where userId='" + sUserId + "'  \n" +
										"	and dateOfAccumulation='" + sDate + "' \n"; 
			String sReturn			= 	"return update value $x/jobCompleteCount with '" + iJobsComplete + "'\n";
			String sQuery = sForCollection + sWhereClause + sReturn;

			performUpdateQuery(sQuery);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bSuccess = false;
		}
		
		return bSuccess;
	}

	/** Sets the accumulated progress jobs complete for the day of  Jobs in Flight.
	 * 
	 * @param sUserId - user login which launched the analysis jobs. e.g. "mshipwa1"
	 * @param sDate - date of interest e.g. "1/2/2013"
	 * @param iJobTotal - new job total to store.  
	 * @return - success/fail
	 */
	public boolean setProgressJobTotal(String sUserId, String sDate, int iJobsTotal){
		boolean bSuccess = true;
		
		try {
			String sForCollection	= analysisInFlightBuilder.progressAccumFor();
			String sWhereClause		= 	"	where userId='" + sUserId + "'  \n" +
										"	and dateOfAccumulation='" + sDate + "' \n"; 
			String sReturn			= 	"return update value $x/jobCount with '" + iJobsTotal + "'\n";
			String sQuery = sForCollection + sWhereClause + sReturn;

			performUpdateQuery(sQuery);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bSuccess = false;
		}
		
		return bSuccess;
	}
	
	
	/** Increments the count of Jobs Completed for the day of  Jobs in Flight.
	 * 
	 * @param sUserId - user login which launched the analysis jobs. e.g. "mshipwa1"
	 * @param sDate - date of interest e.g. "1/2/2013"
	 * @return - success/fail
	 */
	public boolean incrementProgressJobComplete(String sUserId, String sDate){
		boolean bSuccess = true;
		
		try {
			String sForCollection	= analysisInFlightBuilder.progressAccumFor();
			String sWhereClause		= 	"	where userId='" + sUserId + "'  \n" +
										"	and dateOfAccumulation='" + sDate + "' \n"; 
			String sReturn			= 	"return (\n" +
										"	for $accum in $x\n" +
										"		let $done := fn:number(fn:data($accum/jobCompleteCount))+1\n" +
										"	return update value $x/jobCompleteCount with $done\n" + 
										")";
			String sQuery = sForCollection + sWhereClause + sReturn;

			performUpdateQuery(sQuery);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bSuccess = false;
		}
		
		return bSuccess;
	}

	/** Increments the count of Jobs Completed for the day of  Jobs in Flight.
	 * 
	 * @param sUserId - user login which launched the analysis jobs. e.g. "mshipwa1"
	 * @param sDate - date of interest e.g. "1/2/2013"
	 * @return - success/fail
	 */
	public boolean incrementProgressJobTotal(String sUserId, String sDate){
		boolean bSuccess = true;
		
		try {
			String sForCollection	= analysisInFlightBuilder.progressAccumFor();
			String sWhereClause		= 	"	where userId='" + sUserId + "'  \n" +
										"	and dateOfAccumulation='" + sDate + "' \n"; 
			String sReturn			= 	"return (\n" +
										"	for $accum in $x\n" +
										"		let $count := fn:number(fn:data($accum/jobCount))+1\n" +
										"	return update value $x/jobCount with $count\n" + 
										")";
			String sQuery = sForCollection + sWhereClause + sReturn;

			performUpdateQuery(sQuery);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bSuccess = false;
		}
		
		return bSuccess;
	}


	/** Increment the percentage accumulation by the specified amount.
	 * 
	 * @param sUserId- user login which launched the analysis jobs. e.g. "mshipwa1"
	 * @param sDate - date of interest e.g. "1/2/2013"
	 * @param PercToAdd - amount of percentage to add to the accumulator, result may be more that 100. Divide by job count for average % complete.  
	 * @return - success/fail
	 */
	public boolean incrementProgressPercentAccumulation(String sUserId, String sDate, int PercToAdd){
		boolean bSuccess = true;
		
		try {
			String sForCollection	= analysisInFlightBuilder.progressAccumFor();
			String sWhereClause		= 	"	where userId='" + sUserId + "'  \n" +
										"	and dateOfAccumulation='" + sDate + "' \n"; 
			String sReturn			= 	"return (\n" +
										"	for $accum in $x\n" +
										"		let $perc := fn:number(fn:data($accum/percentAccumulation))+" + PercToAdd + " \n" +
										"	return update value $x/percentAccumulation with $perc\n" + 
										")";
			String sQuery = sForCollection + sWhereClause + sReturn;
			
			performUpdateQuery(sQuery);
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			bSuccess = false;
		}
		
		return bSuccess;
	}

	
	/** Given a single XML node with the tags it returns the value with the tags stripped off the beginning and end.
	 * 
	 * @param xmlNode - XML node with the tags e.g. "[subjectId]a01[/subjectId]"  <BR>
		                     (using [] because javadocs doesn't display proper tags) 		  
	 * @return - inner value, e.g. "a01"
	 */
	private  String stripTags(String xmlNode){
		int start=xmlNode.indexOf(">")+1;
		int end=xmlNode.lastIndexOf("<");
		String value = xmlNode.substring(start,end);
		value = value.replace("\n", "");
		value = value.replace("\t", "");

		return value;
	}

	private  AdditionalParameters parseParamTags(String paramXML){
		AdditionalParameters param = new AdditionalParameters();
		int nameStart  = paramXML.indexOf("<name>")+6;
		int nameEnd    = paramXML.indexOf("</name>");
		int valueStart = paramXML.indexOf("<value>")+7;
		int valueEnd   = paramXML.indexOf("</value>");
		int descStart = paramXML.indexOf("<description>")+13;
		int descEnd   = paramXML.indexOf("</description>");

		if((nameStart != -1) & (nameEnd != -1) & (descStart != -1)  & (descEnd != -1) ){
			param.sParameterFlag = paramXML.substring(nameStart, nameEnd).trim();
			if ((valueStart != -1)  & (valueEnd != -1)){
				param.sParameterUserSpecifiedValue = paramXML.substring(valueStart, valueEnd).trim();
			}else{
				param.sParameterUserSpecifiedValue = ""; // blank value has only <value/>, not <value></value>
			}

			param.sDisplayShortName = paramXML.substring(descStart, descEnd).trim();
		}
		return param;
	}

}
