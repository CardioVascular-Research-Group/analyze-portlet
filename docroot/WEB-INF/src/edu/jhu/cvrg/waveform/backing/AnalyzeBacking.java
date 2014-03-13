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
 * @author Chris Jurado, Scott Alger
 * 
 */
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.TreeNode;

import com.liferay.portal.model.User;

import edu.jhu.cvrg.dbapi.factory.Connection;
import edu.jhu.cvrg.dbapi.factory.ConnectionFactory;
import edu.jhu.cvrg.waveform.main.AnalysisManager;
import edu.jhu.cvrg.waveform.model.Algorithm;
import edu.jhu.cvrg.waveform.model.AnalysisObjectVO;
import edu.jhu.cvrg.waveform.model.FileTreeNode;
import edu.jhu.cvrg.waveform.model.LocalFileTree;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;

@ManagedBean(name = "analyzeBacking")
@ViewScoped
public class AnalyzeBacking extends BackingBean implements Serializable {

	private static final long serialVersionUID = -4006126553152259063L;

	private Algorithm[] selectedAlgorithms;
	private ArrayList<AnalysisObjectVO> tableList;

	private LocalFileTree fileTree;
	private User userModel;
	
	private AlgorithmList algorithmList;
	private AnalysisManager analysisManager;	
	private List<FacesMessage> messages;
	
	@PostConstruct
	public void init() {
		userModel = ResourceUtility.getCurrentUser();
		if(userModel != null){
			if(fileTree == null){
				fileTree = new LocalFileTree(userModel.getUserId(), "hea");
			}
			if(algorithmList == null){
				algorithmList = new AlgorithmList();
			}
			messages = new ArrayList<FacesMessage>();
		}
	}

	public void startAnalysis() {
		messages.clear();

		if(tableList == null || tableList.isEmpty()){
			this.getLog().info("No files selected.  List is empty.");
			messages.add(new FacesMessage(FacesMessage.SEVERITY_WARN, "Analysis Error" , "No file selected."));
		}
		
		if(selectedAlgorithms == null || selectedAlgorithms.length == 0){
			this.getLog().info("Algorithms selected is null.");
			messages.add(new FacesMessage(FacesMessage.SEVERITY_WARN, "Analysis Error" , "No algorithm(s) selected."));
		}
		
		if(messages == null || messages.size() == 0){
			analysisManager = new AnalysisManager();
			
			analysisManager.performAnalysis(tableList,  userModel.getUserId(), selectedAlgorithms);
			
		}else{
			ResourceUtility.showMessages("Warning", messages);
		}
		
	}

	public void folderSelect(NodeSelectEvent event){
		TreeNode node = event.getTreeNode();
		if(!node.getType().equals("document")){
			fileTree.selectAllChildNodes(node);
		}
	}
	
	public void folderUnSelect(NodeUnselectEvent event){
		TreeNode node = event.getTreeNode();
		node.setSelected(false);
		if(!node.getType().equals("document")){
			fileTree.unSelectAllChildNodes(node);
		}
	}
	
    public void onRowSelect(SelectEvent event) {  
    	//Do not delete this method.  The listener is present to force a form submit on select.
    	return;
    }  
  
    public void onRowUnselect(UnselectEvent event) {  
    	//Do not delete this method.  The listener is present to force a form submit on un-select.
    	return;
    }  

	public ArrayList<AnalysisObjectVO> getTableList() {
		return tableList;
	}

	public void refreshStudieList(ActionEvent actionEvent) {
		tableList.removeAll(tableList);
	}

	public LocalFileTree getFileTree() {
		return fileTree;
	}

	public void setFileTree(LocalFileTree fileTree) {
		this.fileTree = fileTree;
	}

	public void setTableList(ArrayList<AnalysisObjectVO> tableList) {
		this.tableList = tableList;
	}

	public Algorithm[] getSelectedAlgorithms() {
		return selectedAlgorithms;
	}

	public void setSelectedAlgorithms(Algorithm[] selectedAlgorithms) {
		this.selectedAlgorithms = selectedAlgorithms;
	}

	public AlgorithmList getAlgorithmList() {
		return algorithmList;
	}

	public void setAlgorithmList(AlgorithmList algorithmList) {
		this.algorithmList = algorithmList;
	}
	
	public User getUser(){
		return userModel;
	}
	
	
	public void updateProgressBar() {  
    	int progress = 0;
        if(analysisManager.getTotal() > 0){
        	progress = (100 * analysisManager.getDone())/analysisManager.getTotal();
        }  
        
        if(progress > 100){
        	progress = 100;
        }
        RequestContext context = RequestContext.getCurrentInstance();  
        context.execute("PF(\'pbClient\').setValue("+progress+");");
    }  
  
    public void onComplete() {
    	
    	int failed = 0;
    	List<String> messages = analysisManager.getMessages();
    	if(messages != null){
    		for (String m : messages) {
				this.messages.add(new FacesMessage(FacesMessage.SEVERITY_ERROR, "Analysis Error", m));
			}
    		failed = messages.size();
    	}
    	
    	ResourceUtility.showMessages("Analysis Completed ["+analysisManager.getTotal()+" Analysis - "+failed+" fail(s)]", this.messages);
		tableList.clear();
		selectedAlgorithms = null;
    	this.messages.clear();
    }
    
    public void treeToTable() {
        Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String property = params.get("property");
        String type = params.get("type");
        
        if(property!=null && !property.isEmpty()){
        	Connection con = ConnectionFactory.createConnection();
        	
        	if(tableList == null){
        		tableList = new ArrayList<AnalysisObjectVO>();
        	}
        	
        	AnalysisObjectVO vo = null;
        	
        	if("leaf".equals(type)){
        		FileTreeNode node = fileTree.getNodeByReference(property);
            	if(node != null){
            		vo = new AnalysisObjectVO(node, con.getDocumentRecordById(node.getDocumentRecordId()));
            		if(!tableList.contains(vo)){
            			tableList.add(vo);	
            		}
            	}	
        	}else if("parent".equals(type)){
        		List<FileTreeNode> nodes = fileTree.getNodesByReference(property);
            	if(nodes!=null){
            		for (FileTreeNode node : nodes) {
            			
            			vo = new AnalysisObjectVO(node, con.getDocumentRecordById(node.getDocumentRecordId()));
            			if(!tableList.contains(vo)){
                    		tableList.add(vo);	
                    	}			
					}
            	}
            }
        }else{
        	System.err.println("DRAGDROP = ERROR");
        }
    }
    
    public void removeTableItem(){
    	Map<String,String> params = FacesContext.getCurrentInstance().getExternalContext().getRequestParameterMap();
        String index = params.get("index");
        
    	if(index != null ){
    		int indexTableToRemove = Integer.parseInt(index);
    		
    		if(indexTableToRemove >= 0 && (tableList != null && tableList.size() > indexTableToRemove)){
    			tableList.remove(indexTableToRemove);
    		}
    	}
    }
    
}
