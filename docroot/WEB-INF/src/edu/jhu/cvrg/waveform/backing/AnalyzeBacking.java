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

import javax.annotation.PostConstruct;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.TreeNode;

import com.liferay.portal.model.User;

import edu.jhu.cvrg.waveform.main.AnalysisManager;
import edu.jhu.cvrg.waveform.model.Algorithm;
import edu.jhu.cvrg.waveform.model.FileTreeNode;
import edu.jhu.cvrg.waveform.model.LocalFileTree;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;

@ManagedBean(name = "analyzeBacking")
@ViewScoped
public class AnalyzeBacking implements Serializable {

	private static final long serialVersionUID = -4006126553152259063L;

	private Algorithm[] selectedAlgorithms;
	private ArrayList<FileTreeNode> tableList;

	private LocalFileTree fileTree;
	private User userModel;
	protected static Logger logger = Logger.getLogger(AnalyzeBacking.class);
	
	private AlgorithmList algorithmList;
	
	private List<FacesMessage> messages;

	@PostConstruct
	public void init() {
		userModel = ResourceUtility.getCurrentUser();
		if(fileTree == null){
			fileTree = new LocalFileTree(userModel.getUserId(), "hea");
		}
		if(algorithmList == null){
			algorithmList = new AlgorithmList();
		}
		messages = new ArrayList<FacesMessage>();
	}

	public void startAnalysis() {
		messages.clear();

		if(tableList == null || tableList.isEmpty()){
			logger.info("No files selected.  List is empty.");
			messages.add(new FacesMessage(FacesMessage.SEVERITY_WARN, "Analysis Error" , "No file selected."));
		}
		
		if(selectedAlgorithms == null || selectedAlgorithms.length == 0){
			logger.info("Algorithms selected is null.");
			messages.add(new FacesMessage(FacesMessage.SEVERITY_WARN, "Analysis Error" , "No algorithm(s) selected."));
		}
		
		if(messages == null || messages.size() == 0){
			AnalysisManager analysisManager = new AnalysisManager();
			analysisManager.performAnalysis(tableList,  userModel.getUserId(), selectedAlgorithms);
			
			tableList.clear();
			selectedAlgorithms = null;
			ResourceUtility.showMessages("Analysis Completed", messages);
		}else{
			ResourceUtility.showMessages("WARNING", messages);
		}
		
	}

	public void displaySelectedMultiple(ActionEvent event) {
		this.setTableList(fileTree.getSelectedFileNodes());
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

	public ArrayList<FileTreeNode> getTableList() {
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

	public void setTableList(ArrayList<FileTreeNode> tableList) {
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
}