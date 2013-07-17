package edu.jhu.cvrg.waveform.utility;

import edu.jhu.cvrg.dbapi.EnumXMLInsertLocation;
import edu.jhu.cvrg.dbapi.XQueryBuilder;

public class AnnotationQueryBuilder extends XQueryBuilder {

	// The default constructor is made private so no one can create a query builder without having a URI and collection
	// This is the desired behavior and it can only be done in the derived classes, not the base class.
	private AnnotationQueryBuilder() {
		super();
	}

	/**
	 * The correct constructor to use, which has the URI and collection.
	 * 
	 * @param URI - The URI of the eXist database
	 * @param collection - the Database collection that this query builder will be using as the basis for queries.
	 */
	public AnnotationQueryBuilder(String URI, String collection) {
		super(URI, collection);
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * Default method for a for statement.  Tells query to search entire records.
	 */
	@Override
	public String defaultFor() {
		this.forClause = "for $x in collection('" + this.dbCollection + "')//record \n";
		
		return this.forClause;
	}

	/**
	 * Default method for a let statement.  The default defines a t-coordinate which is used to sort lead annotations
	 * 
	 * 
	 */
	@Override
	public String defaultLet() {
		// TODO Auto-generated method stub
		String query = " let $onset := //record/lead/onset/t-coordinate \n";
		return query;
	}
	
	public String commentLet() {
		String query = " let $id := //record/annotation/ID \n";
		return query;
	}

	/**
	 * Default method a where statement
	 * 
	 * WARNING:  Currently not in use.  Returns null if invoked
	 */
	@Override
	public String defaultWhere() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Default method for an order by statement
	 */
	@Override
	public String defaultOrderBy() {
		// The default order by statement will be used for retrieving lead annotations
		// It will sort by the t-coordinate on the onset tag
		// In the future, another version will be needed for non-lead annotations
		String query = " order by $x/lead/annotation/onset/position/tCoordinate ascending ";
		
		return query;
	}
	
	/**
	 * used for comments currently, will order annotation ID (timestamp for comments)
	 * 
	 * @return
	 */
	public String orderByID() {
		String query = " order by $id ascending";
		
		return query;
	}

	/**
	 * Default method for a return statement.
	 * 
	 * WARNING:  Currently not in use.  Returns null if invoked.  The method is derived from the parent class but has not been implemented in this case.
	 */
	@Override
	public String defaultReturn() {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Default statement for conditional statements inside of brackets.  May be overloaded or superceded by a custom version
	 * by a derived class
	 * 
	 * WARNING:  Currently not in use.  Returns null if invoked.  The method is derived from the parent class but has not been implemented in this case.
	 */
	@Override
	public String defaultBracket() {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * A more customized version that allows for dynamic conditional statements.  It is better to use this if you have many different kinds
	 * of conditional statements to make throughout your code
	 * 
	 * @param node
	 * @param term
	 * @return
	 */
	public String customBracket(String node, String term) {
		String bracket = "[" + node + "='" + term + "']";
		
		return bracket;
	}

	/**
	 * Default insert statement.  Includes node to insert, whether to insert before, after, or into another node, and the node to be inserted
	 * into (or before or after)
	 * 
	 * WARNING:  Currently not in use.  Returns null if invoked.  The method is derived from the parent class but has not been implemented in this case.
	 */
	@Override
	public String insert(String newNode, EnumXMLInsertLocation locale,
			String anchorNode) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Default method for replacing one XML node with another.  Includes the XML node to change, the XML node to replace it with, and the
	 * condition to find the specific node in the right record (so it updates only one node).
	 * 
	 * WARNING:  Currently not in use.  Returns null if invoked.  The method is derived from the parent class but has not been implemented in this case.
	 */
	@Override
	public String update(String oldNode, String newNode, String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 * Default method for changing a value of an XML node.  Includes the XML node to change, the value to use, and the search condition to find
	 * the correct node
	 * 
	 * WARNING:  Currently not in use.  Returns null if invoked.  The method is derived from the parent class but has not been implemented in this case.
	 */
	@Override
	public String modify(String nodeToModify, String newValue, String condition) {
		// TODO Auto-generated method stub
		return null;
	}

	/**
	 *  Default method for writing a delete statement in XQuery.  Includes XML node to delete and the search condition for to ensure
	 *   the right one gets deleted
	 *   
	 *   WARNING:  Currently not in use.  Returns null if invoked.  The method is derived from the parent class but has not been implemented in this case.
	 */
	@Override
	public String delete(String nodeToDelete, String condition) {
		// TODO Auto-generated method stub
		return null;
	}
	
	/**
	 * Creates an insert statement which will insert a new XML node into an existing lead node
	 * 
	 * @param leadName - Name of the lead node to store an annotation in
	 * @param theXML - contents to be inserted
	 * @return
	 */
	
	//TODO:  Refactor this and put it into the actual insert statement this builder uses
	public String insertLeadStatement(String leadName, String theXML) {
		String query = "	return update insert \n " + theXML + " into \n	$x/lead[bioportalReference/term='" + leadName + "']\n";;
		
		return query;
	}
	
	/**
	 * Creates an insert statement which will insert a new XML comment node into the record
	 * 
	 * @param theXML - contents to be inserted
	 * @return
	 */
	public String insertCommentStatement(String theXML) {
		String query = "  return update insert \n " + theXML + "into \n $x";
		
		return query;
	}
	
	/**
	 * Sets up a where statement which searches by studyID, subjectID, and the name of the record
	 * 
	 * @param sStudyID
	 * @param sSubjectID
	 * @param sRecordName
	 * @return
	 */
	public String recordSearch(String sStudyID, String sSubjectID, String sRecordName) {
		String query = "   where studyEntry/studyID='" + sStudyID + "' and studyEntry/subjectID='" + sSubjectID + "' and studyEntry/recordName='" + sRecordName + "'";
		
		return query;
	}
	
	/**
	 * Sets up a where statement which searches by studyID, subjectID, and the name of the record
	 * 
	 * @param sStudyID
	 * @param sSubjectID
	 * @param sRecordName
	 * @return
	 */
	public String userRecordSearch(String sStudyID, String sSubjectID, String sRecordName, String sUserID) {
		String query = "   where studyEntry/studyID='" + sStudyID + "' and studyEntry/subjectID='" + sSubjectID + "' and studyEntry/recordName='" + sRecordName + "' and studyEntry/submitterID='" + sUserID + "'";
		
		return query;
	}
	
	
	/**
	 * A return statement specifically for retrieving all annotations on a single lead
	 * 
	 * @param leadName
	 * @return
	 */
	public String returnLeadAnnotation(String leadName) {
		String query = "	return $x/lead[bioportalReference/term='" + leadName + "']/annotation\n";;
		
		return query;
	}
	
	/**
	 * A return statement specifically for retrieving all comment annotations for a record
	 * 
	 * 
	 * @return
	 */
	public String returnCommentAnnotation() {
		String query = "	return $x/annotation[bioportalReference/term='comment']\n";;
		
		return query;
	}

	/** A return statement for retrieving a count of manual and automated annotations for each lead.<BR>
	 * Result will be a comma separated list with the following columns<BR>
	 * Lead number(term), Total annotation count, Manual annotations count, Automated annotation count.
	 * @return
	 */
	public String returnLeadAnnotationCount() {
		String query = "\n	return(\n" +
						"		for $lead in $x/lead\n" +
						"			let $term := $lead/bioportalReference/term\n" +
						"			let $countManual := count($lead/annotation[createdBy='manual'])\n" +
						"			let $countAuto := count($lead/annotation[createdBy!='manual'])\n" +
						"			let $result := fn:concat(fn:data($term),',',($countManual+$countAuto),',',$countManual,',',$countAuto)\n" +
						"		order by fn:number($lead/bioportalReference/term)\n" +
						"		return\n" +
						"			$result\n" +
						"	)\n";
		
		return query;
	}
	
	public String returnCommentBlock() {
		String query = "\n return	\n" +
				"			for $annotation in $x/annotation\n" +
				"				let $comment := $annotation/value\n" +
				"				let $author := $annotation/createdBy\n" +
				"				let $date := $annotation/ID\n" +
				"				let $nl := \"&#10;\"\n" +
				"				let $finalBlock := fn:concat($author, \" on \", $date, $nl, $comment, $nl)\n" +
				"			return fn:data($finalBlock)\n";
		
		return query;
	}
	
}
