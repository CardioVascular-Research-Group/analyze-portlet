package edu.jhu.cvrg.waveform.backing;

//import java.io.ByteArrayInputStream;
//import java.io.InputStream;
//import java.io.StringWriter;
//import java.util.LinkedHashMap;
//import javax.xml.parsers.DocumentBuilder;
//import javax.xml.parsers.DocumentBuilderFactory;
//import javax.xml.stream.XMLOutputFactory;
//import org.apache.axiom.om.OMElement;
//import org.hsqldb.error.Error;
//import org.w3c.dom.Document;
//import org.w3c.dom.NodeList;
//import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;
//import edu.jhu.cvrg.dbapi.dto.PhysionetMethods;
//import edu.jhu.cvrg.dbapi.factory.hibernate.Algorithm_AWS;
//import edu.jhu.cvrg.dbapi.dto.PhysionetMethods;
//import edu.jhu.cvrg.dbapi.factory.hibernate.Algorithm_AWS;
//import edu.jhu.cvrg.dbapi.factory.HibernateConnection;
//import edu.jhu.cvrg.waveform.model.Algorithm;
//import edu.jhu.cvrg.waveform.model.PhysionetMethods;
//import edu.jhu.cvrg.waveform.utility.AdditionalParameters;
//import edu.jhu.cvrg.waveform.utility.ResourceUtility;
//import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.apache.log4j.Logger;
import org.w3c.dom.Node;
import edu.jhu.cvrg.dbapi.dto.AdditionalParameters;
import edu.jhu.cvrg.dbapi.dto.Algorithm;
import edu.jhu.cvrg.dbapi.factory.Connection;
import edu.jhu.cvrg.dbapi.factory.ConnectionFactory;

public class AlgorithmList implements Serializable{
	
	private static final long serialVersionUID = -4006126323152259063L;
	Logger log = Logger.getLogger(this.getClass());
	
	private List<Algorithm> availableAlgorithms = new ArrayList<Algorithm>();

	public AlgorithmList() {

		try {
			availableAlgorithms.clear();
			populateAlgorithmsFromDB();
//			persistAlgorithmsToDB();
//			persistAlgorithmParametersToDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Algorithm getAlgorithmByName(String name){
		for(Algorithm algorithm : availableAlgorithms){
			if(algorithm.getDisplayShortName().equals(name)){
				return algorithm;
			}
		}
		return null;
	}
	
	/** Returns a single algorithm based on it's Primary Key.
	 * 
	 * @param id - the primary key, as found in the algorithm database table.
	 * @return - the selected algorithm.
	 */
	public Algorithm getAlgorithmByID(int id){
		for(Algorithm algorithm : availableAlgorithms){
			if(algorithm.getId()==id){
				return algorithm;
			}
		}
		return null;
	}
	
	/** Old method for populating algorithm list.<BR>
	 * Partially uses the getAlgorithmDetails method, and partly uses hard coded values.
	 * Is replaced by populateAlgorithmsFromDB() which uses the postgres database instead.
	 */

	
	/** Copies the current algorithm List to the database.
	 * Used once for initializing the database only, should not be run in normal situations.
	 */
	private void persistAlgorithmsToDB(){
		Connection dbUtility = ConnectionFactory.createConnection();
		
		for(Algorithm a : availableAlgorithms){
			int algorithmID = dbUtility.storeAlgorithm(a.getDisplayShortName(), 1, a.getServiceMethod(),
					a.getToolTipDescription(), a.getDisplayLongDescription());
			persistAlgorithmParametersToDB(a, algorithmID);
		}
	}
	
	
	/** Adds a new algorithm entry to the database with blatantly unreal values, so that it can be edited by the user.
	 * This is the first step of creating a new algorithm entry.
	 * 
	 * @param serviceID - the primary key of an existing service entry in this database.  Can be edited later.
	 * @return - the primary key of the new algorithm entry.
	 * @author Michael Shipway
	 */
	public int addNewAlgorithmToDB(int serviceID){
		Connection dbUtility = ConnectionFactory.createConnection();
		String displayShortName = "REPLACE";
		String serviceMethod = "REPLACE";
		String toolTipDescription = "REPLACE";
		String displayLongDescription = "REPLACE";
		int algorithmID = dbUtility.storeAlgorithm(displayShortName, serviceID, serviceMethod,
				toolTipDescription, displayLongDescription);
		return algorithmID;
	}
	
	/** Copies the current algorithm List to the database.
	 * Used once for initializing the database only, should not be run in normal situations.
	 */
	public void updateAlgorithmToDB(Algorithm alg){
		
		log.info("updateAlgorithmToDB() algorithm ID:" + alg.getId());
		log.info("updateAlgorithmToDB() Display Name:" + alg.getDisplayShortName());
		log.info("updateAlgorithmToDB() Service Name:" + alg.getServiceName());
		log.info("updateAlgorithmToDB() Method:" + alg.getServiceMethod());
		log.info("updateAlgorithmToDB() URL Ref:" + alg.getURLreference());
		
		Connection dbUtility = ConnectionFactory.createConnection();

		
			int algorithmID = dbUtility.updateAlgorithm(alg.getId(), alg.getDisplayShortName(), alg.getServiceID(), 
					alg.getServiceMethod(), alg.getToolTipDescription(), alg.getDisplayLongDescription());
//			persistAlgorithmParametersToDB(alg, algorithmID);
	}
	
	/** Copies the current algorithm List to the database.
	 * Used once for initializing the database only, should not be run in normal situations.
	 */
	private void persistAllAlgorithmParametersToDB(){
		Connection dbUtility = ConnectionFactory.createConnection();
		
		int algID = 1;

		for(Algorithm a : availableAlgorithms){
			persistAlgorithmParametersToDB(a, algID);
			algID++;
		}
	}
	
	/** Copies a single algorithm to the database.
	 * Used once for initializing the database only, should not be run in normal situations.
	 */
	private void persistAlgorithmParametersToDB(Algorithm a, int algID){
		Connection dbUtility = ConnectionFactory.createConnection();
		
		if (a.getParameters() !=null){
			if (a.getParameters().size() != 0){
				for(AdditionalParameters param: a.getParameters()){
					int parameterID = dbUtility.storeAlgorithmParameter(param, algID);
				}
			}
		}
	}
	
	/** Adds a single new algorithm parameter to the database. 
	 * 
	 * @param param - an initialized AdditionalParameters object.
	 * @param algID - Primary key of the algorithm this parameter pertains to.
	 * @return - The primary key of the new entry.
	 * @author Michael Shipway
	 */
	public int addNewAlgorithmParameterToDB(AdditionalParameters param, int algID){
		Connection dbUtility = ConnectionFactory.createConnection();
		
		return dbUtility.storeAlgorithmParameter(param, algID);
	}
		
	/** Updates a single new algorithm parameter to the database. 
	 * 
	 * @param param - an initialized AdditionalParameters object.
	 * @param algID - Primary key of the algorithm this parameter pertains to.
	 * @return - The primary key of the new entry.
	 * @author Michael Shipway
	 */
	public int updateAlgorithmParameterToDB(AdditionalParameters param, int algID){
		Connection dbUtility = ConnectionFactory.createConnection();
		
		return dbUtility.updateAlgorithmParameter(param, algID);
	}
		
	


	/** populates the availableAlgorithm List from the waveform3 database.
	 * Instead of from the web service's AlgorithmDetails method.
	 */
	public void populateAlgorithmsFromDB(){
		try {
			Connection dbUtilityConn = ConnectionFactory.createConnection();
			log.info("Connnection to database:" + dbUtilityConn.getType().toString());
//			List<Algorithm> algList = dbUtilityConn.getAvailableAlgorithmList(-1);
			availableAlgorithms.clear();
			availableAlgorithms = dbUtilityConn.getAvailableAlgorithmList(-1);;
			log.info("Number of algorithms in list:" + availableAlgorithms.size());
		} catch(Exception ex) {
			ex.printStackTrace();
		}	
	}

	private String getNodeValue(Node node, String name){
		if(node.getNodeName().equals(name)){
			if(node.getFirstChild() != null){
				return node.getFirstChild().getNodeValue();
			}
			else{
				return "";
			}
		}
		return "";
	}

	public List<Algorithm> getAvailableAlgorithms() {
		return availableAlgorithms;
	}

	public void setAvailableAlgorithms(List<Algorithm> availableAlgorithms) {
		this.availableAlgorithms = availableAlgorithms;
	}
}
