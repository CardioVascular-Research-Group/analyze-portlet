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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.liferay.portal.model.User;

import edu.jhu.cvrg.waveform.main.AnalysisManager;
import edu.jhu.cvrg.waveform.model.Algorithm;
import edu.jhu.cvrg.waveform.model.AlgorithmList;
import edu.jhu.cvrg.waveform.model.FileTree;
import edu.jhu.cvrg.waveform.model.StudyEntry;
import edu.jhu.cvrg.waveform.utility.AnalysisUtility;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;

@ManagedBean(name = "analyzeBacking")
@ViewScoped
public class AnalyzeBacking implements Serializable {

	private static final long serialVersionUID = -4006126553152259063L;

	private StudyEntry[] selectedStudyEntries;
	private List<String> selectedAlgorithms;
	private ArrayList<StudyEntry> tableList;
	private AnalysisManager analysisManager = new AnalysisManager(true);
	private FileTree fileTree;
	private User userModel;
	protected static org.apache.log4j.Logger logger = Logger.getLogger(AnalyzeBacking.class);
	private AlgorithmList algorithmList;

	@ManagedProperty("#{algorithmMap}")
	private AlgorithmMap algorithmMap;

	@PostConstruct
	public void init() {
		userModel = ResourceUtility.getCurrentUser();
		fileTree = new FileTree(userModel.getScreenName());
	}

	public void startAnalysis() {

		if(tableList.isEmpty()){
			logger.info("No items selected.  List is empty.");
			return;
		}
		
		algorithmList = new AlgorithmList();
		ArrayList<Algorithm> selectedAlgorithmList = new ArrayList<Algorithm>();
		
		for (String algorithmName : selectedAlgorithms) {
			Algorithm algorithm = algorithmList.getAlgorithmByName(algorithmName);
			if(algorithm != null){
				selectedAlgorithmList.add(algorithm);
			}
		}

		for (Algorithm algorithm : selectedAlgorithmList) {
			for (StudyEntry studyEntry : tableList) {

				String[] asFileNameList = extractFilenames(studyEntry.getAllFilenames());
				
				analysisManager.performAnalysis(studyEntry.getSubjectID(), userModel.getScreenName(), 
						algorithm, studyEntry.getRecordName(), asFileNameList.length, asFileNameList, 
						AnalysisUtility.extractPath(studyEntry.getAllFilenames()[0]));
			}
		}
	}

	public void displaySelectedMultiple(ActionEvent event) {
		setTableList(fileTree.getSelectedFileNodes());
	}

	private String[] extractFilenames(String[] filepaths) {
		String[] results = new String[filepaths.length];
		for (int i = 0; i < filepaths.length; i++) {
			results[i] = AnalysisUtility.extractName(filepaths[i]);
		}

		return results;
	}
	
	public void updateAlgorithmSelection(ActionEvent event){
		System.out.println("Changed Selection.");
		for(String entry : selectedAlgorithms){
			System.out.println(entry);
		}
	}

	public ArrayList<StudyEntry> getTableList() {
		return tableList;
	}

	public void refreshStudieList(ActionEvent actionEvent) {
		tableList.removeAll(tableList);
	}

	public FileTree getFileTree() {
		return fileTree;
	}

	public void setFileTree(FileTree fileTree) {
		this.fileTree = fileTree;
	}

	public void setTableList(ArrayList<StudyEntry> tableList) {
		this.tableList = tableList;
	}

	public List<String> getSelectedAlgorithms() {
		return selectedAlgorithms;
	}

	public void setSelectedAlgorithms(List<String> selectedAlgorithms) {
		this.selectedAlgorithms = selectedAlgorithms;
	}

	public StudyEntry[] getSelectedStudyEntries() {
		return selectedStudyEntries;
	}

	public void setSelectedStudyEntries(StudyEntry[] selectedStudyEntries) {
		this.selectedStudyEntries = selectedStudyEntries;
	}

	public AlgorithmMap getAlgorithmMap() {
		return algorithmMap;
	}

	public void setAlgorithmMap(AlgorithmMap algorithmMap) {
		this.algorithmMap = algorithmMap;
	}
}