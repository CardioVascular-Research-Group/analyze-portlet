package edu.jhu.cvrg.waveform.model;
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
* @author Chris Jurado
* 
*/
import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

public class FileNode extends DefaultTreeNode {

	private static final long serialVersionUID = 3282562534862428877L;
	private boolean folder;
	private String folderName;
	private StudyEntry studyEntry;

	public FileNode(Object data, TreeNode parent) {
		super(data, parent);
		// TODO Auto-generated constructor stub
	}
	
	public FileNode(Object data, TreeNode parent, boolean folder, StudyEntry studyEntry) {
		super(data, parent);
		this.folder = folder;
		if(data.getClass().equals("java.lang.String")){
			folderName = data.toString();
		}
		this.studyEntry = studyEntry;
	}

	public FileNode(String type, Object data, TreeNode parent) {
		super(type, data, parent);
		// TODO Auto-generated constructor stub
	}

	public boolean isFolder() {
		return folder;
	}

	public void setFolder(boolean folder) {
		this.folder = folder;
	}

	public String getFolderName() {
		return folderName;
	}

	public void setFolderName(String folderName) {
		this.folderName = folderName;
	}

	public StudyEntry getStudyEntry() {
		return studyEntry;
	}

	public void setStudyEntry(StudyEntry studyEntry) {
		this.studyEntry = studyEntry;
	}

}
