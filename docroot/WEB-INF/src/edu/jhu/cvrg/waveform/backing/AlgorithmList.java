package edu.jhu.cvrg.waveform.backing;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.Serializable;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.stream.XMLOutputFactory;

import org.apache.axiom.om.OMElement;
import org.hsqldb.error.Error;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import com.sun.org.apache.xerces.internal.dom.DeferredElementImpl;

import edu.jhu.cvrg.dbapi.dto.AdditionalParameters;
import edu.jhu.cvrg.dbapi.dto.Algorithm;
import edu.jhu.cvrg.dbapi.dto.PhysionetMethods;
import edu.jhu.cvrg.dbapi.factory.hibernate.AWS_Algorithm;
import edu.jhu.cvrg.dbapi.factory.Connection;
import edu.jhu.cvrg.dbapi.factory.ConnectionFactory;
import edu.jhu.cvrg.dbapi.factory.HibernateConnection;

//import edu.jhu.cvrg.waveform.model.Algorithm;
//import edu.jhu.cvrg.waveform.model.PhysionetMethods;
//import edu.jhu.cvrg.waveform.utility.AdditionalParameters;

import edu.jhu.cvrg.waveform.utility.ResourceUtility;
import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

public class AlgorithmList implements Serializable{
	
	private static final long serialVersionUID = -4006126323152259063L;
	
	private List<Algorithm> availableAlgorithms = new ArrayList<Algorithm>();

	public AlgorithmList() {

		try {
//			populateAlgorithms();
			populateAlgorithmsFromDB();
//			persistAlgorithmsToDB();
//			persistAlgorithmParametersToDB();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public Algorithm getAlgorithmByName(String name){
		for(Algorithm algorithm : availableAlgorithms){
			if(algorithm.getDisplayShortName().equals(name)){
				return algorithm;
			}
		}
		return null;
	}
	
	/** Returns a single algorithm based on it's Primary Key.
	 * 
	 * @param id - the primary key, as found in the algorithm database table.
	 * @return - the selected algorithm.
	 */
	public Algorithm getAlgorithmByID(int id){
		for(Algorithm algorithm : availableAlgorithms){
			if(algorithm.getId()==id){
				return algorithm;
			}
		}
		return null;
	}
	
	/** Old method for populating algorithm list.<BR>
	 * Partially uses the getAlgorithmDetails method, and partly uses hard coded values.
	 * Is replaced by populateAlgorithmsFromDB() which uses the postgres database instead.
	 */
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
			
			NodeList algorithmNodes = document.getElementsByTagName("edu.jhu.cvrg.services.physionetAnalysisService.serviceDescriptionData.AlgorithmServiceData");
			
			for(int i = 0; i < algorithmNodes.getLength(); i++){
				Algorithm algorithm = new Algorithm();
				boolean noShow = false;
				Node node = algorithmNodes.item(i);
				
				boolean hasWFDBAnnotationOutput = false;
				
				for(int s = 0; s < node.getChildNodes().getLength(); s++){
					Node childNode = node.getChildNodes().item(s);

					if(childNode.getNodeName().equals("sDisplayShortName")){
						algorithm.setDisplayShortName(getNodeValue(childNode, "sDisplayShortName").trim());
						algorithm.setType(PhysionetMethods.getMethodByName(algorithm.getDisplayShortName()));
						
						switch (algorithm.getType()) {
							case ANN2RR:
							case NGUESS:
							case PNNLIST:
							case TACH:
							case WRSAMP:
								noShow = true;
								break;
							default: break;
						}
						
						if(noShow){
							break;
						}
					}
					if(childNode.getNodeName().equals("sServiceMethod"))
						algorithm.setServiceMethod(getNodeValue(childNode, "sServiceMethod").trim());
					if(childNode.getNodeName().equals("sServiceName"))
						algorithm.setServiceName(getNodeValue(childNode, "sServiceName").trim());
					if(childNode.getNodeName().equals("sAnalysisServiceURL"))
						algorithm.setAnalysisServiceURL(getNodeValue(childNode, "sAnalysisServiceURL").trim());
					if(childNode.getNodeName().equals("sToolTipDescription"))
						algorithm.setToolTipDescription(getNodeValue(childNode, "sToolTipDescription").trim());
					if(childNode.getNodeName().equals("sLongDescription"))
						algorithm.setDisplayLongDescription(getNodeValue(childNode, "sLongDescription").trim());

					if(childNode.getNodeName().equals("aParameters")){
						ArrayList<AdditionalParameters> additionalParametersList = new ArrayList<AdditionalParameters>();
						for(int a = 0; a < childNode.getChildNodes().getLength(); a++){
							Node pNode = childNode.getChildNodes().item(a);
							if(pNode.getNodeName().endsWith("serviceDescriptionData.AdditionalParameters")){
								AdditionalParameters additionalParameters = new AdditionalParameters();
								for(int j = 0; j < pNode.getChildNodes().getLength(); j++){
									Node iNode = pNode.getChildNodes().item(j);
									String nName = iNode.getNodeName();
									if(nName.equals("sParameterFlag")) { additionalParameters.setParameterFlag(getNodeValue(iNode, "sParameterFlag")); }
									if(nName.equals("sParameterDefaultValue")) { additionalParameters.setParameterDefaultValue(getNodeValue(iNode, "sParameterDefaultValue")); }
									if(nName.equals("sDisplayShortName")) { additionalParameters.setDisplayShortName(getNodeValue(iNode, "sDisplayShortName")); }
									if(nName.equals("sToolTipDescription")) { additionalParameters.setToolTipDescription(getNodeValue(iNode, "sToolTipDescription")); }
									if(nName.equals("sLongDescription")) { additionalParameters.setLongDescription(getNodeValue(iNode, "sLongDescription")); }
									if(nName.equals("sType")) { additionalParameters.setType(getNodeValue(iNode, "sType")); }
									if(nName.equals("bOptional")) { additionalParameters.setOptional(Boolean.parseBoolean(getNodeValue(iNode, "bOptional")) ); }
								}
								additionalParametersList.add(additionalParameters);
							}
						}
						algorithm.setParameters(additionalParametersList);
					}
					
					if (childNode.getNodeName().equals("afOutFileTypes")) {
						NodeList fileTypeNodes = childNode.getChildNodes();
						
						for (int f = 0; f < fileTypeNodes.getLength(); f++) {
							Node fileTypeNode = fileTypeNodes.item(f);
							if(fileTypeNode instanceof DeferredElementImpl){
								
								for (int g = 0; g < fileTypeNode.getChildNodes().getLength(); g++) {
									Node n = fileTypeNode.getChildNodes().item(g);
									if(n instanceof DeferredElementImpl){
										if("sName".equals(n.getNodeName())){
											String nameValue = this.getNodeValue(n, "sName");
											hasWFDBAnnotationOutput = "WFDBqrsAnnotation".equals(nameValue);
											break;	
										}
									}
								}
								if(hasWFDBAnnotationOutput){
									break;
								}
							}
						}
					}
					
					algorithm.setWfdbAnnotationOutput(hasWFDBAnnotationOutput);
				}
				
				if(noShow){
					continue;
				}
				availableAlgorithms.add(algorithm);
			}

		} catch(Exception ex) {
			ex.printStackTrace();
		}	
	}
	
	/** Copies the current algorithm List to the database.
	 * Used once for initializing the database only, should not be run in normal situations.
	 */
	private void persistAlgorithmsToDB(){
		Connection dbUtility = ConnectionFactory.createConnection();
		
		for(Algorithm a : availableAlgorithms){
			int algorithmID = dbUtility.storeAlgorithm(a.getDisplayShortName(), 1, a.getServiceMethod(),
					a.getToolTipDescription(), a.getDisplayLongDescription());
			persistAlgorithmParametersToDB(a, algorithmID);
		}
	}
	
	/** Copies the current algorithm List to the database.
	 * Used once for initializing the database only, should not be run in normal situations.
	 */
	public void updateAlgorithmToDB(Algorithm alg){
		
		System.out.println("Updating algorithm ID:" + alg.getId());
		System.out.println("Display Name:" + alg.getDisplayShortName());
		System.out.println("Service Name:" + alg.getServiceName());
		System.out.println("Method:" + alg.getServiceMethod());

//		Connection dbUtility = ConnectionFactory.createConnection();

		
//			int algorithmID = dbUtility.storeAlgorithm(a.getDisplayShortName(), 1, a.getServiceMethod(),
//					a.getToolTipDescription(), a.getDisplayLongDescription());
//			persistAlgorithmParametersToDB(a, algorithmID);
	}
	
	/** Copies the current algorithm List to the database.
	 * Used once for initializing the database only, should not be run in normal situations.
	 */
	private void persistAllAlgorithmParametersToDB(){
		Connection dbUtility = ConnectionFactory.createConnection();
		
		int algID = 1;

		for(Algorithm a : availableAlgorithms){
			persistAlgorithmParametersToDB(a, algID);
			algID++;
		}
	}
	
	/** Copies a single algorithm to the database.
	 * Used once for initializing the database only, should not be run in normal situations.
	 */
	private void persistAlgorithmParametersToDB(Algorithm a, int algID){
		Connection dbUtility = ConnectionFactory.createConnection();
		
		if (a.getParameters() !=null){
			if (a.getParameters().size() != 0){
				for(AdditionalParameters param: a.getParameters()){
					int parameterID = dbUtility.storeAlgorithmParameter(param, algID);
				}
			}
		}
	}
	

	/** populates the availableAlgorithm List from the waveform3 database.
	 * Instead of from the web service's AlgorithmDetails method.
	 */
	public void populateAlgorithmsFromDB(){
		try {
			Connection dbUtility = ConnectionFactory.createConnection();
			List<Algorithm> algList = dbUtility.getAvailableAlgorithmList(-1);
			availableAlgorithms = algList;

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
