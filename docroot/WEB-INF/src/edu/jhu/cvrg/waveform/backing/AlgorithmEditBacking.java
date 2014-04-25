package edu.jhu.cvrg.waveform.backing;
/*
 Copyright 2013 Johns Hopkins University Institute for Computational Medicine

 Licensed under the Apache License, Version 2.0 (the "License");
 you may not use this file except in compliance with the License.
 You may obtain a copy of the License at

 http://www.apache.org/licenses/LICENSE-2.0

 Unless required by applicable law or agreed to in writing, software
 distributed under the License is distributed on an "AS IS" BASIS,
 WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 See the License for the specific language governing permissions and
 limitations under the License.
 */
/**
 * @author Michael Shipway
 * 
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import org.hibernate.cfg.AvailableSettings;
import org.hibernate.sql.Select;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import com.liferay.portal.model.User;

import edu.jhu.cvrg.dbapi.dto.AdditionalParameters;
import edu.jhu.cvrg.dbapi.dto.Algorithm;
import edu.jhu.cvrg.dbapi.dto.Service;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;

@ManagedBean(name = "algorithmEditBacking")
@ViewScoped
public class AlgorithmEditBacking extends BackingBean implements Serializable {
	private static final long serialVersionUID = 1183266658930656309L;
	
	private int selectedAlgorithmID=-1;
	private Algorithm selectedAlgorithm;
	private Service selectedService;

	private User userModel;
	
	private AlgorithmList algorithmList;
	private ServiceList serviceList;
	private List<FacesMessage> messages;
	
	@PostConstruct
	public void init() {
		// System.out.println("AlgorithmEditBacking.init()");
		userModel = ResourceUtility.getCurrentUser();
		if(userModel != null){
			if(serviceList == null){
				serviceList = new ServiceList();
			}
			if(algorithmList == null){
				algorithmList = new AlgorithmList();
//				if(algorithmList.getAvailableAlgorithms().size()>1){
//					setSelectedAlgorithm(algorithmList.getAvailableAlgorithms().get(1));
//					selectedAlgorithmID = getSelectedAlgorithm().getId();
//				}
			}
			messages = new ArrayList<FacesMessage>();
		}
		
	}

    public void onComplete() {
    	
    	int failed = 0;
//    	List<String> messages = analysisManager.getMessages();
//    	if(messages != null){
//    		for (String m : messages) {
//				this.messages.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Analysis Error", m));
//			}
//    		failed = messages.size();
//    	}
//    	
//    	ResourceUtility.showMessages("Analysis Completed ["+analysisManager.getTotal()+" Analysis - "+failed+" fail(s)]", this.messages);
//		tableList.clear();
//		selectedAlgorithms = null;
    	// System.out.println("AlgorithmEditBacking.onComplete algorithm:" + selectedAlgorithm.getDisplayShortName());
    	this.messages.clear();
    }


    public void onRowSelect(SelectEvent event) {  
    	//Do not delete this method.  The listener is present to force a form submit on select.
    	// System.out.println("AlgorithmEditBacking.onRowSelect() algorithm:" + ((Algorithm) event.getObject()).getDisplayShortName());
//    	setSelectedAlgorithm((Algorithm) event.getObject());

    	return;
    }  
  
    public void onRowUnselect(UnselectEvent event) {  
    	//Do not delete this method.  The listener is present to force a form submit on un-select.
    	return;
    }  
    
    public String saveAlgorithm(){
    	if(selectedAlgorithmID>0){
    		// edited existing algorithm
    		algorithmList.updateAlgorithmToDB(selectedAlgorithm);
    	}else{
    		// new algorithm
    	}
    	return "success";
    }
    
	public int getSelectedAlgorithmID() {
		return selectedAlgorithmID;
	}

	public void setSelectedAlgorithmID(int algorithmID) {
		this.selectedAlgorithmID = algorithmID;
	}

	public Algorithm getSelectedAlgorithm() {
		return selectedAlgorithm;
	}

	public void setSelectedAlgorithm(Algorithm selectedAlgorithm) {
		this.selectedAlgorithm = selectedAlgorithm;
		this.selectedAlgorithmID = this.selectedAlgorithm.id;
		int serviceID = this.selectedAlgorithm.getServiceID();
		for (Service s:serviceList.getAvailableServiceList()){
			if(serviceID ==s.id) {
				this.selectedService = s;
			}
		}
		// System.out.println("AlgorithmEditBacking.setSelectedAlgorithm() algorithm:" + selectedAlgorithm.getDisplayShortName());
	}
	
	public int getParameterCount(){
		if(selectedAlgorithmID == -1) return 0;
		return this.selectedAlgorithm.getParameters().size();
	}
	
	public ArrayList<AdditionalParameters> getParameterList(){
		return this.selectedAlgorithm.getParameters();
	}

	public Service getSelectedService() {
		return selectedService;
	}

	public void setSelectedService(Service selectedService) {
		this.selectedService = selectedService;
	}

	public AlgorithmList getAlgorithmList() {
		return algorithmList;
	}

	public void setAlgorithmList(AlgorithmList algorithmList) {
		this.algorithmList = algorithmList;
	}
	
	public ServiceList getServiceList() {
		return serviceList;
	}

	public void setServiceList(ServiceList serviceList) {
		this.serviceList = serviceList;
	}

	public User getUser(){
		return userModel;
	}
}
