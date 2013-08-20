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
* @author Chris Jurado, Mike Shipway
* 
*/
import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import edu.jhu.cvrg.waveform.utility.AlgorithmServiceData;
import edu.jhu.cvrg.waveform.utility.CannedAlgorithmList;

@ManagedBean(name = "algorithmList")
@ViewScoped
public class AlgorithmList implements Serializable {

	private static final long serialVersionUID = 8596023632774091195L;

	private Map<String, String> availableAlgorithms;
	private AlgorithmServiceData[] algorithmDetailsList;

	public AlgorithmList() {

		availableAlgorithms = new LinkedHashMap<String, String>();

		try {
			algorithmDetailsList = fetchAlgorithmDetailClassArray();

			if (algorithmDetailsList != null) {
				for (AlgorithmServiceData item : algorithmDetailsList) {
					availableAlgorithms.put(item.sDisplayShortName,	item.sServiceMethod);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	

	public AlgorithmServiceData[] fetchAlgorithmDetailClassArray() throws Exception {

//		String urlLocation = propsUtil.getAnalysisServiceURL();

		//util.debugPrintln("BrokerServiceImpl.fetchAlgorithmDetail() passed brokerURL: "  + urlLocation);


		AlgorithmServiceData[] algorithmDetails;
		String xml="";
		try {
			
			algorithmDetails = CannedAlgorithmList.getAlgorithmList();

/*			// set up web service call
			String sServiceURL = propsUtil.getAnalysisServiceURL(); // e.g. "http://icmv058.icm.jhu.edu:8080/axis2/services"
			String sServiceName = propsUtil.getPhysionetService(); // e.g. "/physionetAnalysisService"
			String sMethod = propsUtil.getAlgorithmDetailsMethod();

			EndpointReference targetEPR = new EndpointReference(sServiceURL + "/" + sServiceName + "/" + sMethod);


			// set up the call to the webservice
			// the OMElement creation happens here.  These OMElements will be passed in to the 
			// service as parameters.
			OMFactory fac = OMAbstractFactory.getOMFactory();

			OMNamespace omNs = fac.createOMNamespace(sServiceURL + "/" + sServiceName , sMethod);
			OMElement fetchDetails = fac.createOMElement(sMethod, omNs);


//*******************************************************list of algorithms***************************************************
//****************************************************************************************************************************			
			util.addOMEChild("verbose", String.valueOf(false), fetchDetails, fac, omNs);

			ServiceClient sender = util.getSender(targetEPR, sServiceURL + "/" + sServiceName);
			// execute up web service call, capturing the result.
			OMElement result = sender.sendReceive(fetchDetails);
			// extract the XStream generated XML from the result.
			StringWriter writer = new StringWriter();
			result.serialize(XMLOutputFactory.newInstance().createXMLStreamWriter(writer));
			writer.flush();
			xml = writer.toString();
			System.out.println("xml length: " + xml.length());

			// convert the returned XML result into an array of AlgorithmServiceData objects
			XStream xstream = new XStream();

			//						algorithmDetails = new AlgorithmServiceData[2];
			//						algorithmDetails[0] = new org.cvrgrid.ecgrid.shared.AlgorithmServiceData();
			//						algorithmDetails[1] = new org.cvrgrid.ecgrid.shared.AlgorithmServiceData();
			//						xstream.alias("People", People.class);
			//						xstream.alias("Organization", Organization.class);
			//						xstream.alias("FileTypes", FileTypes.class);
			//						xstream.alias("AdditionalParameters", AdditionalParameters.class);
			//						xstream.setClassLoader(AlgorithmServiceData.class.getClassLoader());
			//						xstream.setClassLoader(People.class.getClassLoader());
			//						xstream.setClassLoader(Organization.class.getClassLoader());
			//						xstream.setClassLoader(FileTypes.class.getClassLoader());
			//						xstream.setClassLoader(AdditionalParameters.class.getClassLoader());
			//						xstream.alias("AlgorithmServiceData", AlgorithmServiceDataECGrid.class);
			//						ret = (AlgorithmServiceDataECGrid[])xstream.fromXML(xml);
			//						util.debugPrintln("algorithmDetails length: " + ret.length);

			xstream.alias("AlgorithmServiceData", AlgorithmServiceData.class);
			algorithmDetails = (AlgorithmServiceData[]) xstream.fromXML(xml);
			util.debugPrintln("algorithmDetails length: " + algorithmDetails.length);*/


		} /*catch (AxisFault axisFault) {
			axisFault.printStackTrace();
			throw axisFault;
		}*/ catch(Exception ex) {
			ex.printStackTrace();
			throw ex;
		}	

		return algorithmDetails;
	}

	public Map<String, String> getAvailableAlgorithms() {
		return availableAlgorithms;
	}

	public AlgorithmServiceData[] getAlgorithmDetailsList() {
		return algorithmDetailsList;
	}

	public void setAlgorithmDetailsList(AlgorithmServiceData[] algorithmDetailsList) {
		this.algorithmDetailsList = algorithmDetailsList;
	}
}
