package edu.jhu.cvrg.waveform.utility;

import java.io.Serializable;

import edu.jhu.cvrg.waveform.utility.AdditionalParameters;

public class AnalysisInProgress implements Serializable {

	private static final long serialVersionUID = -2120168072188806013L;
	private String jobID;
	private String subjectId;
	private String sDatasetName;
	private String userId;
	private String webServiceMethod;
	private String analysisType;
	private String sServiceName;
	private String sDisplayText;
	private String[] asDataFileList;
	private String[] asDataHandleList;
	private String[] asResultHandleList;
	private String[] asResultFileList;
	private String analysisServiceURL;
	private AdditionalParameters[] aParameterList;
	
	/**
	 * @return the jobID
	 */
	public String getJobID() {
		return jobID;
	}
	/**
	 * @param jobID the jobID to set
	 */
	public void setJobID(String jobID) {
		this.jobID = jobID;
	}
	public String getSubjectId() {
		return subjectId;
	}
	public void setSubjectId(String subjectId) {
		this.subjectId = subjectId;
	}

	public String getDatasetName() {
		return sDatasetName;
	}
	public void setDatasetName(String sDatasetName) {
		this.sDatasetName = sDatasetName;
	}

	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	/** Get the method name for this analysis. e.g. "sqrsWrapperType2" **/
	public String getWebServiceMethod() {
		return webServiceMethod;
	}
	/** Set the method name for this analysis. e.g. "sqrsWrapperType2" **/
	public void setWebServiceMethod(String webServiceName) {
		this.webServiceMethod = webServiceName;
	}
	
	/** Get the "pretty" description text suitable for display, e.g. "Detect QRS (sqrs)" **/
	public String getsDisplayText() {
		return sDisplayText;
	}
	/** Set the "pretty" description text suitable for display, e.g. "Detect QRS (sqrs)" **/
	public void setsDisplayText(String sDisplayText) {
		this.sDisplayText = sDisplayText;
	}
	public String getAnalysisType() {
		return analysisType;
	}
	public void setAnalysisType(String analysisType) {
		this.analysisType = analysisType;
	}
	public String[] getDataFileList() {
		return asDataFileList;
	}
	public void setDataFileList(String[] filenameList) {
		this.asDataFileList = filenameList;
	}
	public void setDataHandleList(String[] asDataHandleList) {
		this.asDataHandleList = asDataHandleList;
	}
	public String[] getDataHandleList() {
		return asDataHandleList;
	}
	public void setResultHandleList(String[] ResultHandleList) {
		this.asResultHandleList = ResultHandleList;
	}
	public String[] getResultHandleList() {
		return asResultHandleList;
	}
	public String[] getResultFileList() {
		return asResultFileList;
	}
	public void setResultFileList(String[] asResultFileList) {
		this.asResultFileList = asResultFileList;
	}

	/** Set the web service's name for this analysis. e.g. "physionetAnalysisService" **/
	public void setServiceName(String ServiceName) {
		this.sServiceName = ServiceName;
	}
	/** Get the web service's name for this analysis. e.g. "physionetAnalysisService" **/
	public String getServiceName() {
		return sServiceName;
	}
	/** Set the full url of the Web Service without the method.
	 * e.g. "http://128.220.76.170:8080/axis2/services/physionetAnalysisService/" **/ 
	public void setAnalysisServiceURL(String analysisBrokerURL) {
		this.analysisServiceURL = analysisBrokerURL;
	}
	/** get the full url of the Web Service without the method.
	 * e.g. "http://128.220.76.170:8080/axis2/services/physionetAnalysisService/" **/ 
	public String getAnalysisServiceURL() {
		return analysisServiceURL;
	}
	public void setaParameterList(AdditionalParameters[] aParameterList) {
		this.aParameterList = aParameterList;
	}
	public AdditionalParameters[] getaParameterList() {
		return aParameterList;
	}
	/** get the full url of the file copying Web Service without the method.
	 * e.g. "http://128.220.76.170:8080/axis2/services/dataTransferService/" **/ 
	public String getFileCopyServiceURL() {
		String sURL="";
		int iIndexLastSlash = analysisServiceURL.lastIndexOf("/");		
		sURL = analysisServiceURL.substring(0,iIndexLastSlash+1) + "dataTransferService";

		return sURL;
	}
	
	public String getDataFilelistAsString(){
		String fileNameString = "";
		for(int f=0;f<asDataFileList.length;f++){
			fileNameString += asDataFileList[f] + "^";
		}
		return fileNameString;
	}

	public String getResultFilelistAsString(){
		String fileNameString = "";
		for(int f=0;f<asResultHandleList.length;f++){
			fileNameString += asResultHandleList[f] + "^";
		}
		return fileNameString;
	}
	
	/** Gets the directory path by removing the filename.ext from the passed in parameter.
	 * 
	 * @param sPath_Name - relative file path/filename.extension
	 * @return
	 */
	public String getRelativePath(String sPath_Name){
		String sFilePath="";
		int iIndexLastSlash = sPath_Name.lastIndexOf("/");
		
		sFilePath = sPath_Name.substring(0,iIndexLastSlash+1);
		
		return sFilePath;
	}
	
}