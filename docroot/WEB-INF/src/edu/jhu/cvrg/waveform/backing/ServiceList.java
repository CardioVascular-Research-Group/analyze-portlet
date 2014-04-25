package edu.jhu.cvrg.waveform.backing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.Iterator;
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
import edu.jhu.cvrg.dbapi.dto.Service;
import edu.jhu.cvrg.dbapi.factory.hibernate.AWS_Algorithm;
import edu.jhu.cvrg.dbapi.factory.Connection;
import edu.jhu.cvrg.dbapi.factory.ConnectionFactory;
import edu.jhu.cvrg.dbapi.factory.HibernateConnection;

//import edu.jhu.cvrg.waveform.model.Algorithm;
//import edu.jhu.cvrg.waveform.model.PhysionetMethods;
//import edu.jhu.cvrg.waveform.utility.AdditionalParameters;

import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

public class ServiceList implements Serializable{
	
	private static final long serialVersionUID = -4006126323152259063L;
	
	private List<Service> availableServiceList = new ArrayList<Service>();

	public ServiceList() {

		try {
			populateServiceListFromDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/** Returns a single algorithm based on it's Primary Key.
	 * 
	 * @param id - the primary key, as found in the algorithm database table.
	 * @return - the selected algorithm.
	 */
	public Service getServiceByID(int id){
		for(Service service : availableServiceList){
			if(service.getId()==id){
				return service;
			}
		}
		return null;
	}
	

	/** populates the availableService List from the waveform3 database.
	 * Instead of from the web service's AlgorithmDetails method.
	 */
	public void populateServiceListFromDB(){
		try {
			Connection dbUtility = ConnectionFactory.createConnection();
			List<Service> algList = dbUtility.getAvailableServiceList(-1);
			availableServiceList = algList;
		} catch(Exception ex) {
			ex.printStackTrace();
		}	
	}

	public List<Service> getAvailableServiceList() {
		return availableServiceList;
	}

	public void setAvailableServiceList(List<Service> availableServiceList) {
		this.availableServiceList = availableServiceList;
	}
}
