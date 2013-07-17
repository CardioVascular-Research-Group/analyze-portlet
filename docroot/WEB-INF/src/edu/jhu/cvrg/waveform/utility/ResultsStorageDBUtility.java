package edu.jhu.cvrg.waveform.utility;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Set;

import org.xmldb.api.base.Resource;
import org.xmldb.api.base.ResourceIterator;
import org.xmldb.api.base.ResourceSet;

import com.thoughtworks.xstream.XStream;

import edu.jhu.cvrg.dbapi.XMLUtility;
import edu.jhu.cvrg.waveform.utility.AlgorithmServiceData;
import edu.jhu.cvrg.waveform.utility.AnalysisResultsQueryBuilder;
import edu.jhu.cvrg.waveform.model.AnalysisResult;
import edu.jhu.cvrg.waveform.utility.CannedAlgorithmList;


/**
 * This is the class which manages the storage and retrieval and analysis results.  This object interfaces with the database,
 * manipulates the XML from it, and also converts it in the form of a standard POJO to the front end.
 * 
 * @author bbenite1
 *
 */

public class ResultsStorageDBUtility extends XMLUtility {
//	
//	private AdditionalDatabaseProperties analysisConfig = AdditionalDatabaseProperties.getInstance();
	private AnalysisResultsQueryBuilder analysisResultBuilder;
	

	/**
	 * Default Constructor
	 * 
	 * tells the query builder where to find the database URI and collection
	 */
	
	public ResultsStorageDBUtility() {
		super(); 
		analysisResultBuilder = new AnalysisResultsQueryBuilder(this.dbURI, ResourceUtility.getAnalysisResults());
	}
	
	public ResultsStorageDBUtility(String userName, String userPassword, String uRI, 
			String driver, String mainDatabase) {
		super(com.liferay.util.portlet.PortletProps.get("dbUser"),
				com.liferay.util.portlet.PortletProps.get("dbPassword"), 
				com.liferay.util.portlet.PortletProps.get("dbURI"),	
				com.liferay.util.portlet.PortletProps.get("dbDriver"), 
				com.liferay.util.portlet.PortletProps.get("dbMainDatabase")); 
		
		analysisResultBuilder = new AnalysisResultsQueryBuilder(this.dbURI, ResourceUtility.getAnalysisResults());
	}

	public void StoreAnalysisFileMetadata(String userID, String subjectID, String analysisDate, String timeStamp, String recordName, String fileName, String resultType, String displayName) {
		AnalysisResult analyzeResult = new AnalysisResult();
		
		analyzeResult.setUserID(userID);		
		analyzeResult.setSubjectID(subjectID);
		analyzeResult.setDateOfAnalysis(analysisDate);
		analyzeResult.setRecordName(recordName);
		analyzeResult.setFileName(fileName);
		analyzeResult.setAlgorithmUsed(resultType);
		analyzeResult.setDisplayName(displayName);
		
		// Now we will convert the AnalysisResult object to XML, insert that into the main XML and store it in the database.
		XStream xmlStream = new XStream();
		
		xmlStream.alias("analysisResults", AnalysisResult.class);
		
		String analyzeXML = xmlStream.toXML(analyzeResult);
		
		// setting the root tag to be "analysisFile" so that the schema for this record type can be extended in the future
		String finalXML = "<analysisFile> \n" + analyzeXML + "\n </analysisFile>";
		
		String query = analysisResultBuilder.create(userID, recordName, resultType, timeStamp, finalXML);
		
		executeQuery(query);
	}
	
	/**
	 * This initial version will show all the analysis results from a given user
	 * 
	 * @param userID - the user's login name
	 * @return - A list of AnalysisResult objects containing the metadata used in the display
	 */
	public ArrayList<AnalysisResult> getAnalysisResults(String userID) {
		ArrayList<AnalysisResult> tempList = new ArrayList<AnalysisResult>();
		
		try {			
			
			// create first query to get the entire studyEntry block, the collection() XQuery function does this across
			// all documents in the XML Collection
			
			//  The goal of this query is to find all the data we need for the StudyEntry object based on which user submitted them.  For all the documents searched, the query looks to see
			//  if any files for that subject's ECG repository were submitted by that user.  If any files were submitted, retrieve their metadata contained in the studyEntry block.
			String sQuery = analysisResultBuilder.defaultFor() + 
				analysisResultBuilder.defaultWhere(userID) + 
				analysisResultBuilder.defaultOrderBy() + 
				analysisResultBuilder.defaultReturn();
			
			System.out.println("Query to be executed = " + sQuery);									
			
			ResourceSet resultSet = executeQuery(sQuery);
			ResourceIterator iter = resultSet.getIterator();
			Resource selection = null;
			
			while(iter.hasMoreResources()) {
				selection = iter.nextResource();
				String analysisResult = (selection.getContent()).toString();
				
				
				// Now we will create an XStream object and then put that into our StudyEntry objects
				// the StudyEntry objects are de-serialized versions of the studyEntry blocks.
				XStream xmlStream = new XStream();
				
				xmlStream.alias("analysisResults", AnalysisResult.class);
				
				AnalysisResult newResult = (AnalysisResult)xmlStream.fromXML(analysisResult);
				
				System.out.println(newResult);
				
				//*********************************************************************
				// FIXME:  This is another temporary piece of code which calls the temp method
				//			This will also be removed later
				//			bbenite1 - 4/12/2013
//				String displayName = setDisplayName(newResult.getAlgorithmUsed());
//				newResult.setAlgorithmUsed(displayName);
				//*********************************************************************
				
				// Add it to the return array
				tempList.add(newResult);
			}
		}
		catch (Exception ex) {
			System.out.println("StudyEntryUtility.getEntries():  AN EXCEPTION HAS BEEN CAUGHT!  IF A LIST IS RETURNED, IT WILL BE EMPTY!!!");
			ex.printStackTrace();
		}
		
		return tempList;
	}
	
	/**
	 * This version can be used in the future to apply additional filters for which results wish to be seen
	 * 
	 * @param userID - user's login name
	 * @param additionalFilters - A Map of different filters with which to sort or crop the display.  Uses filter names (subjectID, recordName, etc.) as keys 
	 * @return - A list of AnalysisResult objects containing the metadata used in the display
	 */
	public ArrayList<AnalysisResult> getAnalysisResults(String userID, HashMap<String, String> additionalFilters) {
		System.out.println("In function getEntries");
		
		ArrayList<AnalysisResult> tempList = new ArrayList<AnalysisResult>();
		
		try {
			
			// create first query to get the entire studyEntry block, the collection() XQuery function does this across
			// all documents in the XML Collection
			
			//  The goal of this query is to find all the data we need for the StudyEntry object based on which user submitted them.  For all the documents searched, the query looks to see
			//  if any files for that subject's ECG repository were submitted by that user.  If any files were submitted, retrieve their metadata contained in the studyEntry block.
			//CompiledExpression query = subjectQuery.compile("for $x in collection('" + allConstants.getDBCollection() + "')//record where collection('" + allConstants.getDBCollection() + "')//record/studyEntry/submitterID=\"" + userID + "\" order by $x/studyEntry/subjectID return $x/studyEntry");
			
			//***
			// 02/25/2013 - Brandon Benitez:  Commented this out temporarily in case the query builder version of this failed.
			//***
			//String sQuery = "for $x in collection('" + dbHandle.getMainCollection() + "')//record/studyEntry[studyID=\"" + studyID + "\"] where $x/submitterID=\"" + userID + "\" return $x[datatype=\"" + datatype + "\"]";
			
			String sQuery = analysisResultBuilder.defaultFor() +
			//studyBuilder.customNameBracket(studyID, EnumStudyTreeNode.STUDY) + 
			analysisResultBuilder.defaultWhere(userID);
			
			Set<String> theKeys = additionalFilters.keySet();
			
			for(String key : theKeys) {
				String xmlTerm = key;
				String value = additionalFilters.get(key);
				
				sQuery = sQuery + analysisResultBuilder.defaultAndWhere(xmlTerm, value);
			}
			
			sQuery = sQuery + analysisResultBuilder.defaultReturn();
			
			System.out.println("Query to be executed = " + sQuery);
			
			ResourceSet resultSet = executeQuery(sQuery);
			ResourceIterator iter = resultSet.getIterator();
			Resource selection = null;
			
			while(iter.hasMoreResources()) {
				selection = iter.nextResource();
				String analysisResult = (selection.getContent()).toString();
				
				
				// Now we will create an XStream object and then put that into our StudyEntry objects
				// the StudyEntry objects are de-serialized versions of the studyEntry blocks.
				XStream xmlStream = new XStream();
				
				xmlStream.alias("analysisResult", AnalysisResult.class);
				
				AnalysisResult newResult = (AnalysisResult)xmlStream.fromXML(analysisResult);
				
				//*********************************************************************
				// FIXME:  This is another temporary piece of code which calls the temp method
				//			This will also be removed later
				//			bbenite1 - 4/12/2013
//				String displayName = setDisplayName(newResult.getAlgorithmUsed());
//				newResult.setAlgorithmUsed(displayName);
				//*********************************************************************
				
				// Add it to the return array
				tempList.add(newResult);
			}
		}
		catch (Exception ex) {
			System.out.println("StudyEntryUtility.getEntries():  AN EXCEPTION HAS BEEN CAUGHT!  IF A LIST IS RETURNED, IT WILL BE EMPTY!!!");
			ex.printStackTrace();
		}
		
		return tempList;
	}
	
	//FIXME:  This is a temporary solution only.  In the future, the correct Display name will be stored in the database in the ResultsAcquiredCallback class
	//			Currently, the database contains the web service name of the algorithm since no mechanism is currently in place to capture the name of the Display Name.
	//			That must be put in place when an Analysis is executed.  Once provided, this method will no longer be needed.
	//
	//			bbenite1 - 4/12/2013
	private String setDisplayName(String webServiceName) {		
		
		String displayName = "unsupported algorithm";
		
		AlgorithmServiceData[] theBigList = CannedAlgorithmList.getAlgorithmList();
		
		for(int i=0; i<theBigList.length; i++) {
			if(theBigList[i].sServiceMethod.equals(webServiceName)) {
				displayName = theBigList[i].sDisplayShortName;
				break;
			}
		}
		
		theBigList = null;
		return displayName;
	}

}
