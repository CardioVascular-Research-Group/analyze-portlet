package edu.jhu.cvrg.waveform.backing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.SessionScoped;
import javax.faces.bean.ViewScoped;
import javax.faces.model.ListDataModel;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;

import org.apache.axiom.om.OMElement;
import org.primefaces.model.SelectableDataModel;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import edu.jhu.cvrg.waveform.model.Algorithm;
import edu.jhu.cvrg.waveform.utility.AdditionalParameters;
import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

@ManagedBean(name = "algorithmList")
@ViewScoped
public class AlgorithmList implements Serializable{
	
	private static final long serialVersionUID = -4006126323152259063L;
	private List<Algorithm> availableAlgorithms = new ArrayList<Algorithm>();

	public AlgorithmList() {

		try {
			populateAlgorithms();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Algorithm getAlgorithmByName(String name){
		for(Algorithm algorithm : availableAlgorithms){
			if(algorithm.getsDisplayShortName().equals(name)){
				return algorithm;
			}
		}
		return null;
	}
	
	private void populateAlgorithms(){

		String xml="";
		try {

			String sServiceURL = ResourceUtility.getAnalysisServiceURL(); // e.g. "http://icmv058.icm.jhu.edu:8080/axis2/services"
			String sServiceName = ResourceUtility.getPhysionetAnalysisService(); // e.g. "/physionetAnalysisService"
			String sMethod = ResourceUtility.getAlgorithmDetailsMethod();
			LinkedHashMap<String, String> parameterMap = new LinkedHashMap<String, String>();
			parameterMap.put("verbose", String.valueOf(false));
			
			OMElement result = WebServiceUtility.callWebService(parameterMap, sMethod, sServiceName, 
					sServiceURL, null);

			StringWriter writer = new StringWriter();
			result.serialize(XMLOutputFactory.newInstance().createXMLStreamWriter(writer));
			writer.flush();
			xml = writer.toString();
			
			InputStream inStream = new ByteArrayInputStream(xml.getBytes("UTF-8"));
			DocumentBuilderFactory docBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder docBuilder = docBuilderFactory.newDocumentBuilder();
			Document document = docBuilder.parse(inStream);
			
			document.getDocumentElement().normalize();
			
			NodeList algorithmNodes = document.getElementsByTagName("AlgorithmServiceData");
			
			for(int i = 0; i < algorithmNodes.getLength(); i++){
				Algorithm algorithm = new Algorithm();
				
				Node node = algorithmNodes.item(i);
				
				for(int s = 0; s < node.getChildNodes().getLength(); s++){
					Node childNode = node.getChildNodes().item(s);

					if(childNode.getNodeName().equals("sDisplayShortName"))
						algorithm.setsDisplayShortName(getNodeValue(childNode, "sDisplayShortName").trim());
					if(childNode.getNodeName().equals("sServiceMethod"))
						algorithm.setsServiceMethod(getNodeValue(childNode, "sServiceMethod").trim());
					if(childNode.getNodeName().equals("sServiceName"))
						algorithm.setsServiceName(getNodeValue(childNode, "sServiceName").trim());
					if(childNode.getNodeName().equals("sAnalysisServiceURL"))
						algorithm.setsAnalysisServiceURL(getNodeValue(childNode, "sAnalysisServiceURL").trim());
					if(childNode.getNodeName().equals("sLongDescription"))
						algorithm.setsDisplayLongDescription(getNodeValue(childNode, "sLongDescription").trim());

					if(childNode.getNodeName().equals("aParameters")){
						ArrayList<AdditionalParameters> additionalParametersList = new ArrayList<AdditionalParameters>();
						for(int a = 0; a < childNode.getChildNodes().getLength(); a++){
							Node pNode = childNode.getChildNodes().item(a);
							if(pNode.getNodeName().equals("serviceDescriptionData.AdditionalParameters")){
								AdditionalParameters additionalParameters = new AdditionalParameters();
								for(int j = 0; j < pNode.getChildNodes().getLength(); j++){
									Node iNode = pNode.getChildNodes().item(j);
									additionalParameters.setsParameterFlag(getNodeValue(iNode, "sParameterFlag"));
									additionalParameters.setsParameterDefaultValue(getNodeValue(iNode, "sParameterDefaultValue"));
									additionalParameters.setsToolTipDescription(getNodeValue(iNode, "sToolTipDescription"));
								}
								additionalParametersList.add(additionalParameters);
							}
						}
						algorithm.setaParameters(additionalParametersList);
					}
				}
				availableAlgorithms.add(algorithm);
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}	
	}
	
	private String getNodeValue(Node node, String name){
		if(node.getNodeName().equals(name)){
			if(node.getFirstChild() != null){
				return node.getFirstChild().getNodeValue();
			}
			else{
				return "";
			}
		}
		return "";
	}

	public List<Algorithm> getAvailableAlgorithms() {
		return availableAlgorithms;
	}

	public void setAvailableAlgorithms(List<Algorithm> availableAlgorithms) {
		this.availableAlgorithms = availableAlgorithms;
	}


}
