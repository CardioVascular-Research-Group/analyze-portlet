package edu.jhu.cvrg.waveform.main;

import java.util.Map;

import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

public class AnalysisThread extends Thread{

	private Map<String, String> map;
	
	public AnalysisThread(Map<String, String> params) {
		super(params.get("jobID"));
		map = params;
	}
	
	public AnalysisThread(Map<String, String> params, ThreadGroup threadGroup) {
		super(threadGroup, params.get("jobID"));
		this.map = params;
	}
	
	@Override
	public void run() {
		WebServiceUtility.callWebService(map,map.get("method"),map.get("serviceName"), map.get("URL"), null, null);
	}
}
