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
import javax.faces.bean.ManagedBean;
import javax.faces.bean.ManagedProperty;
import javax.faces.bean.ViewScoped;
import javax.faces.event.ActionEvent;

import org.apache.log4j.Logger;

import com.liferay.portal.model.User;

import edu.jhu.cvrg.waveform.main.AnalysisManager;
import edu.jhu.cvrg.waveform.model.AlgorithmList;
import edu.jhu.cvrg.waveform.model.FileTree;
import edu.jhu.cvrg.waveform.model.StudyEntry;
import edu.jhu.cvrg.waveform.utility.AlgorithmServiceData;
import edu.jhu.cvrg.waveform.utility.AnalysisUtility;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;

@ManagedBean(name = "analyzeBacking")
@ViewScoped
public class AnalyzeBacking implements Serializable {

	private static final long serialVersionUID = -4006126553152259063L;

	private StudyEntry[] studyEntryListArray;
	private StudyEntry[] selectedStudyEntries;
	private List<String> selectedAlgorithms;
	private List<StudyEntry> droppedStudy;
	private ArrayList<StudyEntry> tableList;
	private AnalysisManager analysisManager;
	private FileTree fileTree;
	private User userModel;
	protected static org.apache.log4j.Logger logger = Logger.getLogger(AnalyzeBacking.class);

	@ManagedProperty("#{algorithmList}")
	private AlgorithmList algorithmList;

	@PostConstruct
	public void init() {
		userModel = ResourceUtility.getCurrentUser();
		droppedStudy = new ArrayList<StudyEntry>();
		fileTree = new FileTree();
		fileTree.initialize(userModel.getScreenName());
	}

	public void startAnalysis() {

		AlgorithmServiceData[] algorithmDetailsList = algorithmList.getAlgorithmDetailsList();
				
		//Will hold the full selected algorithms
		ArrayList<AlgorithmServiceData> finalAlgs = new ArrayList<AlgorithmServiceData>();

		if(selectedAlgorithms == null){
			logger.info("No items selected.  List is null.");
			return;
		}
		
		for (String algorithm : selectedAlgorithms) {
			for (AlgorithmServiceData datum : algorithmDetailsList) {
				if (datum.sServiceMethod.equals(algorithm)) {
					finalAlgs.add(datum);
				}
			} 
		}

		for (AlgorithmServiceData algToProcess : finalAlgs) {

			for (StudyEntry studyEntry : selectedStudyEntries) {

				String userID = userModel.getScreenName();
				String subjectID = studyEntry.getSubjectID();
				String datasetName = studyEntry.getRecordName();
				String[] asFileNameList = extractFilenames(studyEntry.getAllFilenames());
				int iFileCount = asFileNameList.length;

				String ftpRelativePath = AnalysisUtility.extractPath(studyEntry.getAllFilenames()[0]);
				
				analysisManager = new AnalysisManager(true);
				System.out.println("Got AnalyzizManager");
				if (analysisManager.performAnalysis(subjectID, userID, algToProcess, datasetName, iFileCount, asFileNameList, ftpRelativePath))
					System.out
							.println("analysisManager.performAnalysis call successful for: "
									+ algToProcess.sDisplayShortName
									+ " on "
									+ subjectID);
			}

		}
	}

	public void displaySelectedMultiple(ActionEvent event) {

		setTableList(fileTree.getSelectedFileNodes());

	}

	/**
	 * Given an array of filepaths/filenames, returns an array containing only
	 * the filename portion of each string.<BR>
	 * Calls AnalysisUtility.extractName(), which assumes that the file
	 * separator is "/" (e.g. Unix file system, not Windows)
	 * 
	 * @param filepaths
	 * @return - array of bare filenames.
	 */
	private String[] extractFilenames(String[] filepaths) {
		String[] results = new String[filepaths.length];
		for (int i = 0; i < filepaths.length; i++) {
			results[i] = AnalysisUtility.extractName(filepaths[i]);
		}

		return results;
	}

	public List<StudyEntry> getDroppedStudy() {
		return droppedStudy;
	}

	public ArrayList<StudyEntry> getTableList() {
		return tableList;
	}

	public void refreshStudieList(ActionEvent actionEvent) {
		tableList.removeAll(tableList);
	}

	public AlgorithmList getAlgorithmList() {
		return algorithmList;
	}

	public void setAlgorithmList(AlgorithmList algorithmList) {
		this.algorithmList = algorithmList;
	}

	public FileTree getFileTree() {
		return fileTree;
	}

	public void setFileTree(FileTree fileTree) {
		this.fileTree = fileTree;
	}

	public StudyEntry[] getStudyEntryListArray() {
		return studyEntryListArray;
	}

	public void setStudyEntryListArray(StudyEntry[] studyEntryListArray) {
		this.studyEntryListArray = studyEntryListArray;
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

}