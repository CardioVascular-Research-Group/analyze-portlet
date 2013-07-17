package edu.jhu.cvrg.waveform.model;

import java.io.Serializable;


/**
 * A container class for holding metadata for an analysis results record.
 * 
 * This class is similar to the StudyEntry class in this package.  Just as the StudyEntry object represents a raw data file, 
 * the AnalysisResult object represents the file that comes from running an analysis on that raw data file.
 * 
 * Like the StudyEntry object, this object will be translated to and from XML via XStream.
 * 
 * 20130411 sgranite - modified object to mimic structure of FileDetails, so that there would be consistency
 * 
 * 20130411 bbenite1 - removed filesize member as it currently has no use.  In addition, the analysis service does not provide it, so it
 * 						was easier to simply remove the filesize for now.  It was preferred over either reworking the analysis or opening the results
 * 						files to get the filesize.
 * 
 * @author bbenite1
 * @author sgranite
 * 
 *
 */
public class AnalysisResult implements Serializable{

	private String userID = "";
	private String subjectID = "";
	private String dateOfAnalysis = "";
	private String recordName = "";
	private String fileName = "";
	private String algorithmUsed = "";
	private String displayName = "";
	
	public AnalysisResult(String fileURI) {
		this.fileName = fileURI;
	}
	
	public AnalysisResult() {
		
	}
	
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userId) {
		this.userID = userId;
	}
	public String getSubjectID() {
		return subjectID;
	}
	public void setSubjectID(String subjectId) {
		this.subjectID = subjectId;
	}
	public String getDateOfAnalysis() {
		return dateOfAnalysis;
	}
	public void setDateOfAnalysis(String dateOfAnalysis) {
		this.dateOfAnalysis = dateOfAnalysis;
	}
	public String getRecordName() {
		return recordName;
	}
	public void setRecordName(String recordName) {
		this.recordName = recordName;
	}
	public String getFileName() {
		return fileName;
	}
	public void setFileName(String fileName) {
		this.fileName = fileName;
	}
	public String getAlgorithmUsed() {
		return algorithmUsed;
	}
	public void setAlgorithmUsed(String algorithm) {
		this.algorithmUsed = algorithm;
	}
	public String getDisplayName() {
		return displayName;
	}
	public void setDisplayName(String name) {
		this.displayName = name;
	}
	
	public String toString() {
		String printout = "Analysis Result object:  \n" +
							"userID = " + userID + "\n" +
							"subjectID = " + subjectID + "\n" +
							"date of analysis = " + dateOfAnalysis + "\n" +
							"record name = " + recordName + "\n" +
							"file name = " + fileName + "\n" +
							"algorithm used = " + algorithmUsed + "\n";
		
		return printout;
		
	}
}
