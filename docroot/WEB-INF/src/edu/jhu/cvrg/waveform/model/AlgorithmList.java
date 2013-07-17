package edu.jhu.cvrg.waveform.model;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import edu.jhu.cvrg.waveform.utility.AlgorithmServiceData;
import edu.jhu.cvrg.waveform.main.AnalysisManager;

@ManagedBean(name = "algorithmList")
@ViewScoped
public class AlgorithmList implements Serializable {

	private static final long serialVersionUID = 8596023632774091195L;

	private List<String> selectedAlgorithms;
	private Map<String, String> availableAlgorithms;

	private AlgorithmServiceData[] algorithmDetailsList;

	AnalysisManager analysisManager = new AnalysisManager(true);

	public AlgorithmList() {

		availableAlgorithms = new LinkedHashMap<String, String>();

		try {
			algorithmDetailsList = analysisManager.fetchAlgorithmDetailClassArray();

			if (algorithmDetailsList != null) {
				for (int i = 0; i < algorithmDetailsList.length; i++) {
					availableAlgorithms.put(algorithmDetailsList[i].sDisplayShortName,	algorithmDetailsList[i].sServiceMethod);
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public List<String> getSelectedAlgorithms() {
		return selectedAlgorithms;
	}

	public void setSelectedAlgorithms(List<String> selectedAlgorithms) {
		this.selectedAlgorithms = selectedAlgorithms;
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
