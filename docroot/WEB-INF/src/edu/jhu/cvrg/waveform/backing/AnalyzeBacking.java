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
import javax.faces.bean.SessionScoped;
import javax.faces.component.UISelectItem;
import javax.faces.component.html.HtmlOutputLabel;
import javax.faces.component.html.HtmlPanelGrid;
import javax.faces.component.html.HtmlPanelGroup;
import javax.faces.context.FacesContext;
import javax.faces.event.ActionEvent;

import org.primefaces.component.inputtext.InputText;
import org.primefaces.component.selectbooleancheckbox.SelectBooleanCheckbox;
import org.primefaces.component.selectoneradio.SelectOneRadio;
import org.primefaces.context.RequestContext;
import org.primefaces.event.NodeSelectEvent;
import org.primefaces.event.NodeUnselectEvent;
import org.primefaces.event.SelectEvent;
import org.primefaces.event.UnselectEvent;
import org.primefaces.model.TreeNode;

import com.liferay.portal.model.User;

import edu.jhu.cvrg.data.dto.AdditionalParametersDTO;
import edu.jhu.cvrg.data.dto.AlgorithmDTO;
import edu.jhu.cvrg.data.factory.Connection;
import edu.jhu.cvrg.data.factory.ConnectionFactory;
import edu.jhu.cvrg.data.util.DataStorageException;
import edu.jhu.cvrg.waveform.main.AnalysisManager;
import edu.jhu.cvrg.waveform.model.DocumentDragVO;
import edu.jhu.cvrg.waveform.model.FileTreeNode;
import edu.jhu.cvrg.waveform.model.LocalFileTree;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.ServerUtility;

@ManagedBean(name = "analyzeBacking")
@SessionScoped
public class AnalyzeBacking extends BackingBean implements Serializable {

	private static final long serialVersionUID = -4006126553152259063L;

	private HtmlPanelGroup panelParameterSet;

	private AlgorithmDTO[] selectedAlgorithms;
	private ArrayList<DocumentDragVO> tableList;

	private LocalFileTree fileTree;
	private User userModel;
	
	private AlgorithmList algorithmList;
	private int algorithmToEditID = -1;
	private boolean setparameterdisplayed=false;
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
			this.getLog().info("Number of algorithms in list:" + algorithmList.getAvailableAlgorithms().size());
			messages = new ArrayList<FacesMessage>();
		}
		// TODO: **** testing creating front end controls purely from Java, for parameter editing.
	}

	private void loadParameterSetPanel(){
        panelParameterSet = new HtmlPanelGroup();
        panelParameterSet.setId("panelParameterSet");

        HtmlPanelGrid grid = new HtmlPanelGrid();
	    grid.setId("gridOne");
	    grid.setBorder(2);
	    grid.setColumns(2);
			grid.getChildren().add(makeLabel("#{algorithm.displayShortName}", "Title"));
			grid.getChildren().add(makeLabel("Col Two", "Title"));
			
			grid.getChildren().add(makeLabel("Input Text", "Title"));
//			grid.getChildren().add(showInputText("foo bar","input Text Label"));
			
			grid.getChildren().add(makeLabel("Checkbox", "Title"));
//			grid.getChildren().add(showCheckbox());
			
			grid.getChildren().add(makeLabel("Radio Options", "Title"));
			//grid.getChildren().add(showStronglyQuestion());

	    panelParameterSet.getChildren().add(grid);
	}

	private void loadParameterSetPanel(int selectedAlgID){
		if (selectedAlgID != (-1)){
	        panelParameterSet = new HtmlPanelGroup();
	        panelParameterSet.setId("panelParameterSet");
	        int algIndex = -1;
			if(algorithmList.getAvailableAlgorithms().size()>1){
				for(int i=0;i<algorithmList.getAvailableAlgorithms().size();i++){
					if(algorithmList.getAvailableAlgorithms().get(i).getId() == selectedAlgID){
						algIndex = i;
					}
				}			
			}
			ArrayList<AdditionalParametersDTO> paramList = algorithmList.getAvailableAlgorithms().get(algIndex).getParameters();
			
			HtmlPanelGrid grid = new HtmlPanelGrid();
			grid.setId("gridOne");
			grid.setBorder(4);
			grid.setColumns(3);
	
			for(AdditionalParametersDTO p:paramList){
				grid.getChildren().add(makeLabel("(" + p.getParameterFlag() + ") " + p.getDisplayShortName(), p.getToolTipDescription()));
				
				String type = p.getType(); /** MUST BE text, integer, float, boolean, select, data_column, or drill_down  BUT NOT genomebuild, hidden, baseurl, file, data. **/
				if(type.equals("text")){
					grid.getChildren().add(showInputText(String.valueOf(p.getId()),p.getParameterUserSpecifiedValue(),p.getLongDescription()));
				}
				if(type.equals("integer")){
					grid.getChildren().add(showInputText(String.valueOf(p.getId()),p.getParameterUserSpecifiedValue(),p.getLongDescription()));
				}
				if(type.equals("float")){
					grid.getChildren().add(showInputText(String.valueOf(p.getId()),p.getParameterUserSpecifiedValue(),p.getLongDescription()));
				}
				if(type.equals("boolean")){
					grid.getChildren().add(showCheckbox(String.valueOf(p.getId()), p.getParameterUserSpecifiedValue(),p.getLongDescription()));
				}
				if(type.equals("select")){
	//				grid.getChildren().add(showInputText(String.valueOf(p.getId()),p.getParameterUserSpecifiedValue(),p.getLongDescription()));
				}
				if(type.equals("data_column")){
	//				grid.getChildren().add(showInputText(String.valueOf(p.getId()),p.getParameterUserSpecifiedValue(),p.getLongDescription()));
				}
				if(type.equals("drill_down")){
	//				grid.getChildren().add(showInputText(String.valueOf(p.getId()),p.getParameterUserSpecifiedValue(),p.getLongDescription()));
				}
				grid.getChildren().add(makeLabel(p.getLongDescription(), p.getLongDescription()));
	
			}
		    panelParameterSet.getChildren().add(grid);
		}
	}

	private HtmlOutputLabel makeLabel(String value, String title){
        HtmlOutputLabel label = new HtmlOutputLabel();
        label.setValue(value);
        label.setTitle(title);
        return label;
	}
	private InputText showInputText(String id, String value, String label){
		InputText it = new InputText();
		try {
			it.setId("text" + id);
			it.setValue(value);
			it.setLabel(label);
		} catch (IllegalArgumentException  e) {
			e.printStackTrace();
			this.getLog().fatal("ArgumentException. " + e.getMessage());
		}catch (Exception e){
			this.getLog().fatal("Fatal error. " + e.getMessage());
			ServerUtility.logStackTrace(e, this.getLog());
		}
		
		return it;
	}
	
	private SelectBooleanCheckbox showCheckbox(String id, String value, String label){
		SelectBooleanCheckbox cb = new SelectBooleanCheckbox();
		try{
			cb.setId("boolean" + id);
			cb.setValue(value);
			cb.setLabel(label);
		} catch (IllegalArgumentException  e) {
			e.printStackTrace();
			this.getLog().fatal("ArgumentException. " + e.getMessage());
		}catch (Exception e){
			this.getLog().fatal("Fatal error. " + e.getMessage());
			ServerUtility.logStackTrace(e, this.getLog());
		}
		
		return cb;
	}
	
//	private SelectBooleanCheckbox showCheckbox(){
//		SelectBooleanCheckbox cb = new SelectBooleanCheckbox();
//		cb.setId("cb1");
//		cb.setValue("Checkbox1");
//		cb.setLabel("Checkbox1 Label");
////		cb.setRendered(true);
//		
//		return cb;
//	}

	private SelectOneRadio showStronglyQuestion(){
		SelectOneRadio rb = new SelectOneRadio();
		rb.setRendered(true);
		
		UISelectItem itemOne = new UISelectItem();
		itemOne.setValue("1");
		itemOne.setItemLabel("Strongly Agree");
		rb.getChildren().add(itemOne);
		
		UISelectItem itemTwo = new UISelectItem();
		itemTwo.setItemValue("2");
		itemTwo.setItemLabel("Agree");
		rb.getChildren().add(itemTwo);
		
		UISelectItem itemThree = new UISelectItem();
		itemThree.setItemValue("3");
		itemThree.setItemLabel("Neither Agree nor Disagree");
		rb.getChildren().add(itemThree);
		
		UISelectItem itemFour = new UISelectItem();
		itemFour.setItemValue("4");
		itemFour.setItemLabel("Disagree");
		rb.getChildren().add(itemFour);
		
		UISelectItem itemFive = new UISelectItem();
		itemFive.setItemValue("5");
		itemFive.setItemLabel("Strongly Disagree");
		rb.getChildren().add(itemFive);
		
		return rb;
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

	public void generateParameterSetting(int algorithmID){
//int algorithmID = 999;
		this.getLog().info("algorithmID:" + algorithmID);
		algorithmToEditID = algorithmID;
		loadParameterSetPanel(algorithmID);

	}
	
	//action listener event
	public void attrListener(ActionEvent event){
 
		int algorithmid = (Integer) event.getComponent().getAttributes().get("algorithmid");
		this.getLog().info("attrListener() algorithmid:" + algorithmid);
	}

	
    public void updateParameter(int id){
    	this.getLog().info(" updateParameter(" + id + ")");
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

	public ArrayList<DocumentDragVO> getTableList() {
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

	public void setTableList(ArrayList<DocumentDragVO> tableList) {
		this.tableList = tableList;
	}

	public AlgorithmDTO[] getSelectedAlgorithms() {
		return selectedAlgorithms;
	}

	public void setSelectedAlgorithms(AlgorithmDTO[] selectedAlgorithms) {
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

	public HtmlPanelGroup getPanelParameterSet() {
//		this.getLog().info("getPanelParameterSet() algorithmID:" + algorithmToEditID);
		if(algorithmToEditID !=-1)	{
			this.getLog().info("getPanelParameterSet() algorithmID:" + algorithmToEditID);
			loadParameterSetPanel(algorithmToEditID);
		}else{
			loadParameterSetPanel(47);
		}

    	return panelParameterSet;
    }

    public void setPanelParameterSet(HtmlPanelGroup panelParameterSet) {
		this.panelParameterSet = panelParameterSet;
	}
	
	
	public int getAlgorithmToEditID() {
		return algorithmToEditID;
	}

	public void setAlgorithmToEditID(int algorithmToEditID) {
		this.algorithmToEditID = algorithmToEditID;
	}

	public boolean isSetparameterdisplayed() {
		return setparameterdisplayed;
	}

	public void setSetparameterdisplayed(boolean setparameterdisplayed) {
		this.setparameterdisplayed = setparameterdisplayed;
	}

	public void updateProgressBar() {  
    	int progress = 0;
    	if(analysisManager != null){
	        if(analysisManager.getTotal() > 0){
	        	int done = analysisManager.getDone();
	        	if(done>0){
	        		progress = (100 * done)/analysisManager.getTotal();
	        	}
	        }
	        
	        if(progress > 100){
	        	progress = 100;
	        }
	        if(progress>0){
		        RequestContext context = RequestContext.getCurrentInstance();  
		        context.execute("PF(\'pbClient\').setValue("+progress+");");
	        }
    	}
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
        	try {
				Connection con = ConnectionFactory.createConnection();
				
				if(tableList == null){
					tableList = new ArrayList<DocumentDragVO>();
				}
				
				DocumentDragVO vo = null;
				
				if("leaf".equals(type)){
					FileTreeNode node = fileTree.getNodeByReference(property);
					if(node != null){
						vo = new DocumentDragVO(node, con.getDocumentRecordById(node.getDocumentRecordId()));
						if(!tableList.contains(vo)){
							tableList.add(vo);	
						}
					}	
				}else if("parent".equals(type)){
					List<FileTreeNode> nodes = fileTree.getNodesByReference(property);
					if(nodes!=null){
						for (FileTreeNode node : nodes) {
							
							vo = new DocumentDragVO(node, con.getDocumentRecordById(node.getDocumentRecordId()));
							if(!tableList.contains(vo)){
				        		tableList.add(vo);	
				        	}			
						}
					}
				}
			} catch (DataStorageException e) {
				this.getLog().error("Error on node2dto conversion. " + e.getMessage());
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
//	public void initAlgorithmList(int selectedAlgID){
//		System.out.println("passed param:" + selectedAlgID);
//	}
}
