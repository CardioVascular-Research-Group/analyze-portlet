package edu.jhu.cvrg.waveform.backing;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import edu.jhu.cvrg.dbapi.dto.Service;
import edu.jhu.cvrg.dbapi.factory.Connection;
import edu.jhu.cvrg.dbapi.factory.ConnectionFactory;

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
