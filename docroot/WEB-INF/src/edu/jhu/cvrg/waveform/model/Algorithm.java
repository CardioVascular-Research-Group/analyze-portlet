package edu.jhu.cvrg.waveform.model;

import java.io.Serializable;
import java.util.ArrayList;

import edu.jhu.cvrg.waveform.utility.AdditionalParameters;
import edu.jhu.cvrg.waveform.utility.FileTypes;

/** Data in this class describes a single analysis algorithm service method.
 * It also will generate an OMElement containing all this data, so that the User Interface code 
 * can auto-generate an invocation interface which will support all the required and optional input parameters.
 * It also specifies the output files, so the result interface can be auto-generated. 
 * @author mshipwa1@jhu.edu
 *
 */
public class Algorithm implements Serializable{
	/**
	 * 
	 */
	public Algorithm(){}
	
	public Algorithm(String serviceName){
		this.sServiceName = serviceName;
	}
	
	private static final long serialVersionUID = 1L;
	//	public int iWebServiceID;
	// Descriptions and documentation
	private String sServiceName = "n/a"; // name to be used in the URL when calling the service
	private String sDisplayShortName = "n/a"; // Human friendly name to be used by the UI when listing services.
	private String sToolTipDescription = "n/a"; // Short summary description (under 150 characters) suitable for displaying is a tooltip.
	private String sLongDescription = "n/a"; // Complete description suitable for using in a manual/help file.
	private String sVersionIdAlgorithm = "n/a"; // Version ID of the algorithm (e.g. "2.5" or "3.0 Beta" ) 
	private String sDateAlgorithm= "n/a"; // Date of this algorithm version.
	private String sVersionIdWebService = "n/a"; // Version ID of the web service wrapping the algorithm(e.g. "1.0")
	private String sDateWebService = "n/a"; // Date of the last web service update.
	private String sURLreference = "n/a"; // URL of a web page about this algorithm.
	private String sLicence = "n/a"; // license of this algorithm, or URL of license e.g. "GPL".
	private String sDisplayLongDescription = "";
	
//	private People[] apAlgorithmProgrammers; // list of programmers and authors of the algorithm
//	private People[] apWebServiceProgrammers;// list of programmers and authors of the WebService
//	private Organization[] aoAffiliatedOrgs; // list of affiliated organizations.

	// input and output paramters and files
	private FileTypes[] afInFileTypes; // all possible input data files
	private FileTypes[] afOutFileTypes; // all possible output data files
	private ArrayList<AdditionalParameters> aParameters; // Additional parameters (beyond input file names), required or optional.
	private String sAnalysisServiceURL = "n/a"; // URL of the server containing the web services e.g. "http://128.220.76.170:8080/axis2/services" used together with sServiceName and sServiceMethod e.g. "http://128.220.76.170:8080/axis2/services/physionetAnalysisService/sqrsWrapperType2"
	private String sServiceMethod = "n/a"; // name of the method, with the webservice. e.g. "sqrsWrapperType2"

	public String getsServiceName() {
		return sServiceName;
	}

	public void setsServiceName(String sServiceName) {
		this.sServiceName = sServiceName;
	}

	public String getsDisplayShortName() {
		return sDisplayShortName;
	}

	public void setsDisplayShortName(String sDisplayShortName) {
		this.sDisplayShortName = sDisplayShortName;
	}

	public String getsToolTipDescription() {
		return sToolTipDescription;
	}

	public void setsToolTipDescription(String sToolTipDescription) {
		this.sToolTipDescription = sToolTipDescription;
	}

	public String getsLongDescription() {
		return sLongDescription;
	}

	public void setsLongDescription(String sLongDescription) {
		this.sLongDescription = sLongDescription;
	}

	public String getsVersionIdAlgorithm() {
		return sVersionIdAlgorithm;
	}

	public void setsVersionIdAlgorithm(String sVersionIdAlgorithm) {
		this.sVersionIdAlgorithm = sVersionIdAlgorithm;
	}

	public String getsDateAlgorithm() {
		return sDateAlgorithm;
	}

	public void setsDateAlgorithm(String sDateAlgorithm) {
		this.sDateAlgorithm = sDateAlgorithm;
	}

	public String getsVersionIdWebService() {
		return sVersionIdWebService;
	}

	public void setsVersionIdWebService(String sVersionIdWebService) {
		this.sVersionIdWebService = sVersionIdWebService;
	}

	public String getsDateWebService() {
		return sDateWebService;
	}

	public void setsDateWebService(String sDateWebService) {
		this.sDateWebService = sDateWebService;
	}

	public String getsURLreference() {
		return sURLreference;
	}

	public void setsURLreference(String sURLreference) {
		this.sURLreference = sURLreference;
	}

	public String getsLicence() {
		return sLicence;
	}

	public void setsLicence(String sLicence) {
		this.sLicence = sLicence;
	}

//	public People[] getApAlgorithmProgrammers() {
//		return apAlgorithmProgrammers;
//	}
//
//	public void setApAlgorithmProgrammers(People[] apAlgorithmProgrammers) {
//		this.apAlgorithmProgrammers = apAlgorithmProgrammers;
//	}
//
//	public People[] getApWebServiceProgrammers() {
//		return apWebServiceProgrammers;
//	}
//
//	public void setApWebServiceProgrammers(People[] apWebServiceProgrammers) {
//		this.apWebServiceProgrammers = apWebServiceProgrammers;
//	}
//
//	public Organization[] getAoAffiliatedOrgs() {
//		return aoAffiliatedOrgs;
//	}
//
//	public void setAoAffiliatedOrgs(Organization[] aoAffiliatedOrgs) {
//		this.aoAffiliatedOrgs = aoAffiliatedOrgs;
//	}

	public FileTypes[] getAfInFileTypes() {
		return afInFileTypes;
	}

	public void setAfInFileTypes(FileTypes[] afInFileTypes) {
		this.afInFileTypes = afInFileTypes;
	}

	public FileTypes[] getAfOutFileTypes() {
		return afOutFileTypes;
	}

	public void setAfOutFileTypes(FileTypes[] afOutFileTypes) {
		this.afOutFileTypes = afOutFileTypes;
	}

	public ArrayList<AdditionalParameters> getaParameters() {
		return aParameters;
	}

	public void setaParameters(ArrayList<AdditionalParameters> aParameters) {
		this.aParameters = aParameters;
	}

	public String getsAnalysisServiceURL() {
		return sAnalysisServiceURL;
	}

	public void setsAnalysisServiceURL(String sAnalysisServiceURL) {
		this.sAnalysisServiceURL = sAnalysisServiceURL;
	}

	public String getsServiceMethod() {
		return sServiceMethod;
	}

	public void setsServiceMethod(String sServiceMethod) {
		this.sServiceMethod = sServiceMethod;
	}

	public String getsDisplayLongDescription() {
		return sDisplayLongDescription;
	}
	
	public String getLongDescription(){
		return sDisplayLongDescription;
	}
	
	public String getShortName(){
		return sDisplayShortName;
	}

	public void setsDisplayLongDescription(String sDisplayLongDescription) {
		this.sDisplayLongDescription = sDisplayLongDescription;
	}

}


