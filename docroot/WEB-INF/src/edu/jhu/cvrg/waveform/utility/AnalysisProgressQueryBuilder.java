package edu.jhu.cvrg.waveform.utility;

import java.io.Serializable;

import edu.jhu.cvrg.dbapi.EnumXMLInsertLocation;
import edu.jhu.cvrg.dbapi.XQueryBuilder;

public class AnalysisProgressQueryBuilder extends XQueryBuilder implements Serializable{

	private static final long serialVersionUID = 1L;

	/**
	 * The correct constructor to use, which has the URI and collection.
	 * 
	 * @param URI - The URI of the eXist database
	 * @param collection - the Database collection that this query builder will be using as the basis for queries.
	 */
	public AnalysisProgressQueryBuilder(String URI, String collection) {
		super(URI, collection);
		// TODO Auto-generated constructor stub
	}
	
	// This constructor has been made private so that it will remain inaccessible.  Without this, a builder could be created without a URI and collection, causing errors.
	private AnalysisProgressQueryBuilder() {
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
		this.forClause = "for $x in collection('" + this.dbCollection + "')/ecgProcessing/job";
		return this.forClause;
	}
	
	public String ecgFor() {
		this.forClause = "for $x in collection('" + this.dbCollection + "')/ecgProcessing";
		return this.forClause;
	}

	public String progressAccumFor() {
		this.forClause = "for $x in collection('" + this.dbCollection + "')/progress/accumulator";
		return this.forClause;
	}

	public String progressFor() {
		this.forClause = "for $x in collection('" + this.dbCollection + "')/progress";
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

	/**
	 * Default method for an order by statement
	 * 
	 * WARNING:  Currently not in use.  Returns null if invoked.  The method is derived from the parent class but has not been implemented in this case.
	 */
	@Override
	public String defaultOrderBy() {
		// TODO Auto-generated method stub
		return null;
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
	 * Default method a where statement
	 * 
	 * WARNING:  Currently not in use.  Returns null if invoked
	 */
	@Override
	public String defaultWhere() {
		// TODO Auto-generated method stub
		return null;
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
	
	public String create(String subjectID, String analyzeName, String newECGFile) {
		String creationString = "xmldb:store('" + this.dbCollection + "', '" + subjectID + "_" + analyzeName + ".xml', '" + newECGFile + "')";
		return creationString;
	}

}
