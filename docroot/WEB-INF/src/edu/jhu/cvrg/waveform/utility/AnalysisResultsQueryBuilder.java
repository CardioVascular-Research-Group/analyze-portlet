package edu.jhu.cvrg.waveform.utility;

import edu.jhu.cvrg.dbapi.EnumXMLInsertLocation;
import edu.jhu.cvrg.dbapi.XQueryBuilder;

public class AnalysisResultsQueryBuilder extends XQueryBuilder {

	/**
	 * The correct constructor to use, which has the URI and collection.
	 * 
	 * @param URI - The URI of the eXist database
	 * @param collection - the Database collection that this query builder will be using as the basis for queries.
	 */
	public AnalysisResultsQueryBuilder(String URI, String collection) {
		super(URI, collection);
		// TODO Auto-generated constructor stub
	}
	
	// The default constructor is made private so no one can create a query builder without having a URI and collection
	// This is the desired behavior and it can only be done in the derived classes, not the base class.
	private AnalysisResultsQueryBuilder() {
		// TODO Auto-generated constructor stub
		super();
	}

	@Override
	public String defaultBracket() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	// By default, this for statement checks the job node for its queries.
	public String defaultFor() {
		this.forClause = "for $x in collection('" + this.dbCollection + "')//analysisFile";
		return this.forClause;
	}

	/**
	 * Default method for a let statement.
	 * 
	 * WARNING:  Currently not in use.  Returns null if invoked.  The method is derived from the parent class but has not been implemented in this case.
	 */
	@Override
	public String defaultLet() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String defaultOrderBy() {
		this.orderByClause = " order by $x/analysisResults/subjectID "; 

		return this.orderByClause;
	}

	@Override
	public String defaultReturn() {
		this.returnClause = " return $x/analysisResults";

		return this.returnClause;
	}

	@Override
	public String defaultWhere() {
		// Do not use this yet!!!
		
		System.out.println("The defaultWhere(void) method in AnalysisResultsQueryBuilder has been invoked.  This version should not be used and will return null!");
		return null;
	}
	
	public String defaultWhere(String userID) {
		this.whereClause = " where $x/analysisResults/userID='" + userID + "' ";
		
		return this.whereClause;
	}

	@Override
	public String delete(String nodeToDelete, String condition) {
		// set a default node to delete
		if(nodeToDelete.equals("")) {
			nodeToDelete = "$x";
		}
		
		// it is assumend that the bracket statement used in the condition has been defined and used in a different
		// method in this builder
		String deleteClause = "return update delete " + nodeToDelete + condition;
		return deleteClause;
	}

	@Override
	public String insert(String arg0, EnumXMLInsertLocation arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String modify(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String update(String arg0, String arg1, String arg2) {
		// TODO Auto-generated method stub
		return null;
	}
	
	public String create(String userID, String recordName, String algorithmName, String date, String newContent) {
		String creationString = "xmldb:store('" + this.dbCollection + "', '" + userID + "_" + recordName + "_" + algorithmName + "_" + date + ".xml', '" + newContent + "')";
		return creationString;
	}

	public String defaultAndWhere(String xmlTerm, String xmlValue) {
		String returnString = " and $x/analysisResults/" + xmlTerm + "='" + xmlValue + "' ";

		return returnString;
	}

}
