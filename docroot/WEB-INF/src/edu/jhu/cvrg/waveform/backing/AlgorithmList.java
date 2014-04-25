package edu.jhu.cvrg.waveform.backing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;

import org.apache.axiom.om.OMElement;
import org.hsqldb.error.Error;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;

import edu.jhu.cvrg.dbapi.dto.AdditionalParameters;
import edu.jhu.cvrg.dbapi.dto.Algorithm;
import edu.jhu.cvrg.dbapi.dto.PhysionetMethods;
import edu.jhu.cvrg.dbapi.factory.hibernate.AWS_Algorithm;
import edu.jhu.cvrg.dbapi.factory.Connection;
import edu.jhu.cvrg.dbapi.factory.ConnectionFactory;
import edu.jhu.cvrg.dbapi.factory.HibernateConnection;

//import edu.jhu.cvrg.waveform.model.Algorithm;
//import edu.jhu.cvrg.waveform.model.PhysionetMethods;
//import edu.jhu.cvrg.waveform.utility.AdditionalParameters;

import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

public class AlgorithmList implements Serializable{
	
	private static final long serialVersionUID = -4006126323152259063L;
	
	private List<Algorithm> availableAlgorithms = new ArrayList<Algorithm>();

	public AlgorithmList() {

		try {
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
	
	/** Copies the current algorithm List to the database.
	 * Used once for initializing the database only, should not be run in normal situations.
	 */
	public void updateAlgorithmToDB(Algorithm alg){
		
		System.out.println("Updating algorithm ID:" + alg.getId());
		System.out.println("Display Name:" + alg.getDisplayShortName());
		System.out.println("Service Name:" + alg.getServiceName());
		System.out.println("Method:" + alg.getServiceMethod());

//		Connection dbUtility = ConnectionFactory.createConnection();

		
//			int algorithmID = dbUtility.storeAlgorithm(a.getDisplayShortName(), 1, a.getServiceMethod(),
//					a.getToolTipDescription(), a.getDisplayLongDescription());
//			persistAlgorithmParametersToDB(a, algorithmID);
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
	

	/** populates the availableAlgorithm List from the waveform3 database.
	 * Instead of from the web service's AlgorithmDetails method.
	 */
	public void populateAlgorithmsFromDB(){
		try {
			Connection dbUtility = ConnectionFactory.createConnection();
			List<Algorithm> algList = dbUtility.getAvailableAlgorithmList(-1);
			availableAlgorithms = algList;

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
