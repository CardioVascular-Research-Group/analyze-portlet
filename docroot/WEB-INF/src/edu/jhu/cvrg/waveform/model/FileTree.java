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
import java.io.Serializable;
import java.util.ArrayList;

import javax.faces.event.ActionEvent;

import org.primefaces.model.DefaultTreeNode;
import org.primefaces.model.TreeNode;

import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.StudyEntryUtility;

public class FileTree implements Serializable{

	private static final long serialVersionUID = 1L;
	private TreeNode treeRoot;
	private TreeNode selectedNode;
	private TreeNode[] selectedNodes;
	private String newFolderName = "";
	private ArrayList<StudyEntry> studyEntryList;
	private String username;
	StudyEntryUtility theDB;

	public FileTree (String username){
		initialize(username);
	}
	
	public void initialize(String username) {
		System.out.println("Initializing tree..");
		this.username = username;
		
		theDB = new StudyEntryUtility(ResourceUtility.getDbUser(),
				ResourceUtility.getDbPassword(), 
				ResourceUtility.getDbURI(),	
				ResourceUtility.getDbDriver(), 
				ResourceUtility.getDbMainDatabase());

		
		if (treeRoot == null) {
			buildTree();
		}
	}

	private void buildTree() {

		studyEntryList = theDB.getEntries(this.username);

		treeRoot = new DefaultTreeNode("root", null);

		if (studyEntryList.isEmpty()) {
			TreeNode newNode = new DefaultTreeNode("document", null, treeRoot);
			return;
		}

		for (StudyEntry studyEntry : studyEntryList) {

			String[] path = studyEntry.getVirtualPath().split("\\|");
			TreeNode workNode = treeRoot;

			for (String step : path) {

				TreeNode newNode = getNodeByName(workNode, step);

				if (newNode == null) {
					StudyEntry entryFolder = new StudyEntry();
					entryFolder.setSubjectID(step);
					newNode = new DefaultTreeNode("default", entryFolder, workNode);
				}
				newNode.setExpanded(true);
				
				workNode = newNode;
			}

			@SuppressWarnings("unused")
			TreeNode recordNode = new DefaultTreeNode("document", studyEntry, workNode);
		}
	}

	public String getSelectedNodePath() {

		TreeNode node = this.selectedNode;
		String path = (String) node.getData();

		while (!node.getParent().getData().toString().equals("Root")) {
			node = node.getParent();
			path = node.getData().toString() + "|" + path;
		}

		return path;

	}

	private TreeNode getNodeByName(TreeNode searchNode, String name) {

		for (TreeNode node : searchNode.getChildren()) {
			if (((StudyEntry)node.getData()).getSubjectID().equals(name)) {
				return node;
			}
		}
		return null;
	}

	public void addFolder(ActionEvent event) {
		
		if (selectedNode == null) {
			selectedNode = (DefaultTreeNode) treeRoot;
		}

		if (!newFolderName.equals("")) {
			StudyEntry entryFolder = new StudyEntry();
			entryFolder.setSubjectID(newFolderName);
			TreeNode newNode = new DefaultTreeNode(newFolderName, entryFolder, selectedNode);
			selectedNode.setExpanded(true);
			selectedNode = (DefaultTreeNode) newNode;
		}
	}

	public ArrayList<StudyEntry> getSelectedFileNodes() {

		if(selectedNodes == null){
			System.out.println("No selected Nodes");
			return null;
		}
		
		ArrayList<StudyEntry> fileEntries = new ArrayList<StudyEntry>();

		for (TreeNode selectedNode : selectedNodes) {
			if (selectedNode.isLeaf()) {
				fileEntries.add((StudyEntry)selectedNode.getData());
			}
		}
		return fileEntries;
	}
	
	public void selectAllChildNodes(TreeNode startingNode){

		for(TreeNode node : startingNode.getChildren()){
			node.setSelected(true);
			if(!node.getType().equals("document")){
				selectAllChildNodes(node);
			}
		}
	}
	
	public void unSelectAllChildNodes(TreeNode startingNode){
		for(TreeNode node : startingNode.getChildren()){
			node.setSelected(false);
			if(!node.getType().equals("document")){
				unSelectAllChildNodes(node);
			}
		}
	}

	public TreeNode getTreeRoot() {
		return treeRoot;
	}

	public TreeNode getSelectedNode() {
		return selectedNode;
	}

	public void setSelectedNode(TreeNode selectedNode) {
		this.selectedNode = selectedNode;
	}

	public String getNewFolderName() {
		return newFolderName;
	}

	public void setNewFolderName(String newFolderName) {
		this.newFolderName = newFolderName;
	}

	public TreeNode[] getSelectedNodes() {
		return selectedNodes;
	}

	public void setSelectedNodes(TreeNode[] selectedNodes) {
		this.selectedNodes = selectedNodes;
	}
}
