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

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.TreeNode;

import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.model.User;

import edu.jhu.cvrg.waveform.main.AnalysisManager;
import edu.jhu.cvrg.waveform.model.Algorithm;
import edu.jhu.cvrg.waveform.model.LocalFileTree;
import edu.jhu.cvrg.waveform.model.StudyEntry;
import edu.jhu.cvrg.waveform.utility.AnalysisUtility;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;

@ManagedBean(name = "analyzeBacking")
@ViewScoped
public class AnalyzeBacking implements Serializable {

	private static final long serialVersionUID = -4006126553152259063L;

	private StudyEntry[] selectedStudyEntries;
	private Algorithm[] selectedAlgorithms;
	private ArrayList<FileEntry> tableList;

	private AnalysisManager analysisManager = new AnalysisManager(true);
	private LocalFileTree fileTree;
	private User userModel;
	protected static org.apache.log4j.Logger logger = Logger.getLogger(AnalyzeBacking.class);
	
	@ManagedProperty("#{algorithmList}")
	private AlgorithmList algorithmList;

	public void init() {
		userModel = ResourceUtility.getCurrentUser();
		if(fileTree == null){
			fileTree = new LocalFileTree(userModel.getUserId(), "hea");
		}
		if(algorithmList == null){
			algorithmList = new AlgorithmList();
		}
	}

	public void startAnalysis() {

		if(tableList.isEmpty()){
			logger.info("No files selected.  List is empty.");
			return;
		}
		
		if(selectedAlgorithms == null){
			logger.info("Algorithms selected is null.");
			return;
		}
		
		
		analysisManager.performAnalysis(tableList,  userModel.getScreenName(), selectedAlgorithms);
		
		
//		for (Algorithm algorithm : selectedAlgorithms) {
//			for (FileEntry studyEntry : tableList) {
//				// [VILARDO] Working with only one file
//				analysisManager.performAnalysis(studyEntry, userModel.getScreenName(), algorithm);
//			}
//		}
	}

	public void displaySelectedMultiple(ActionEvent event) {
		this.setTableList(fileTree.getSelectedFileNodes());
	}

	private String[] extractFilenames(String[] filepaths) {
		String[] results = new String[filepaths.length];
		for (int i = 0; i < filepaths.length; i++) {
			results[i] = AnalysisUtility.extractName(filepaths[i]);
		}

		return results;
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

	public ArrayList<FileEntry> getTableList() {
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

	public void setTableList(ArrayList<FileEntry> tableList) {
		this.tableList = tableList;
	}

	public Algorithm[] getSelectedAlgorithms() {
		return selectedAlgorithms;
	}

	public void setSelectedAlgorithms(Algorithm[] selectedAlgorithms) {
		this.selectedAlgorithms = selectedAlgorithms;
	}

	public StudyEntry[] getSelectedStudyEntries() {
		return selectedStudyEntries;
	}

	public void setSelectedStudyEntries(StudyEntry[] selectedStudyEntries) {
		this.selectedStudyEntries = selectedStudyEntries;
	}

	public AlgorithmList getAlgorithmList() {
		return algorithmList;
	}

	public void setAlgorithmList(AlgorithmList algorithmList) {
		this.algorithmList = algorithmList;
	}
}