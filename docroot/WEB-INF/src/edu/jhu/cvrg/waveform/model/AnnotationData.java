package edu.jhu.cvrg.waveform.model;

import java.io.Serializable;

public class AnnotationData implements Serializable , Comparable<AnnotationData> {

	private static final long serialVersionUID = 36376190556447571L;

	String[] colNames = {"I","II","III","aVR","aVL","aVF","V1","V2","V3","V4","V5","V6"};
	
	private boolean isSinglePoint=true;
	private boolean isComment=false;
	private String creator;
	private String datasetName;
	private String uniqueID;
	private String userID;
	private String subjectID;
	private String studyID = "Mesa";
	private int leadIndex;
	private String leadName;	
	private double milliSecondStart; // start time of interval in milliSeconds from beginning of the file.
	private double uVoltStart;	// Y Coordinate in Schema
	private double milliSecondEnd; // end time of interval in milliSeconds from beginning of the file.
	private double uVoltEnd;	// Y Coordinate in Schema
	private String conceptLabel;
	private String onsetLabel;
	private String offsetLabel;	
	private String conceptID;
	private String onsetID;
	private String offsetID;	
	private String annotation;
	private String measurementUnit;
	private String onsetDescription;
	private String offsetDescription;
	private String conceptRestURL;
	private String onsetRestURL;
	private String offsetRestURL;

	public void setIsSinglePoint(boolean isSinglePoint) {
		this.isSinglePoint = isSinglePoint;
	}
	public boolean getIsSinglePoint() {
		return isSinglePoint;
	}
	public void setIsComment(boolean isComment) {
		this.isComment = isComment;
	}
	public boolean getIsComment() {
		return isComment;
	}
	public void setCreator(String creator) {
		this.creator = creator;
	}
	public String getCreator() {
		return creator;
	}
	public String getDatasetName() {
		return datasetName;
	}
	public void setDatasetName(String datasetName) {
		this.datasetName = datasetName;
	}
	public String getUniqueID() {
		return uniqueID;
	}
	public void setUniqueID(String id) {
		this.uniqueID = id;
	}
	public String getUserID() {
		return userID;
	}
	public void setUserID(String userID) {
		this.userID = userID;
	}
	public String getSubjectID() {
		return subjectID;
	}
	public void setSubjectID(String subjectID) {
		this.subjectID = subjectID;
	}
	public String getStudyID() {
		return studyID;
	}
	public void setStudyID(String studyID) {
		this.studyID = studyID;
	}	
	/** Lead #, is zero based.
	 * e.g lead 0 is "I", 1 is "II"
	 * @return
	 */
	public int getLeadIndex() {
		return leadIndex;
	}
	
	/**
	 * Get the lead Number based on the Bioportal lead name
	 * 
	 * @param leadName - Bioportal lead name (I, II, III, aVL, etc.)
	 * @return - lead number the name maps to (1, 2, 3, 4, etc.)
	 */
	public int getLeadIndex(String leadName) {
		/*String leadIndexString = leadMap.get(leadName);*/
		
		int leadIndex = 0;
		
		for(int i=0;i<colNames.length;i++) {
			if(colNames[i].equals(leadName)) {
				this.setLeadIndex(i);
				leadIndex = i + 1;
				break;
			}
		}
		
		return leadIndex;
	}
	

	public static int getLeadIndexStatic(String leadName) {
		String[] colNames = {"I","II","III","aVR","aVL","aVF","V1","V2","V3","V4","V5","V6"};
		
		int leadIndex = -1;
		
		for(int i=0;i<colNames.length;i++) {
			if(colNames[i].equals(leadName)) {
				leadIndex = i + 1;
				break;
			}
		}
		
		return leadIndex;
	}
	
	public void setLeadIndex(int lead) {
		this.leadIndex = lead;
	}

	/** Gets the name of this lead, based on the value of this.leadIndex.
	 * 
	 * @return
	 */
	public String getLeadName() {
		leadName=getLeadName(this.leadIndex);
		

		return leadName;
	}
	
	/** Gets the name of this lead.
	 * 
	 * @param leadIndex - One based lead index to match what comes back from the web service.
	 * @return
	 */
	public String getLeadName(int _leadIndex) {
		// This variable is now a data member of the class.
		//String[] colNames = {"I","II","III","aVR","aVL","aVF","V1","V2","V3","V4","V5","V6"};
		
		leadName=colNames[_leadIndex];

		return leadName;
	}
	
	
	/**
	 * @param leadName the leadName to set
	 */
	public void setLeadName(String leadName) {
		this.leadName = leadName;
	}
	
	/** Gets the Start time from the beginning of the file in milliseconds **/
	// * e.g. "3.040" equals sample # 3,040 at a 1000hz sample rate **/
	public double getMilliSecondStart() {
		return milliSecondStart;
	}
	/** Sets the Start time from the beginning of the file in milliseconds **/ 
	public void setMilliSecondStart(double timePoint) {
		this.milliSecondStart = timePoint;
	}	

	/** Gets the Start time from the beginning of the file in  milliseconds **/ 
	// * e.g. "3.040" equals sample # 3,040 at a 1000hz sample rate **/
	public double getMilliSecondEnd() {
		return milliSecondEnd;
	}
	/** Sets the Start time from the beginning of the file in milliseconds **/ 
	public void setMilliSecondEnd(double timePoint) {
		this.milliSecondEnd = timePoint;
	}	

	/** ECG Start value in microvolts **/
//	public double getValue() {
//		return uVoltStart;
//	}
	
	/** ECG Start value in microvolts **/
	public double getMicroVoltStart(){
		return uVoltStart;
	}
	/** ECG Start value in microvolts **/
	public void setMicroVoltStart(double uVolt) {
		this.uVoltStart = uVolt;
	}

	/** ECG End value in microvolts **/
	public double getMicroVoltEnd() {
		return uVoltEnd;
	}
	/** ECG End value in microvolts **/
	public void setMicroVoltEnd(double uVolt) {
		this.uVoltEnd = uVolt;
	}

	
	/** Full annotation text  
	 *  e.g. "An initial slurring (delta wave) of the QRS complex due to the presence of an accessory pathway. 
	 * This characteristic EKG pattern is typically seen in Wolff-Parkinson-White syndrome."**/
	public String getAnnotation() {
		return annotation;
	}
	/** Full annotation text.
	 * e.g. "An initial slurring (delta wave) of the QRS complex due to the presence of an accessory pathway. 
	 * This characteristic EKG pattern is typically seen in Wolff-Parkinson-White syndrome."
	 * **/
	public void setAnnotation(String annotation) {
		this.annotation = annotation;
	}
	public String getUnit() {
		return measurementUnit;
	}
	public void setUnit(String unit) {
		this.measurementUnit = unit;
	}
	public String getOnsetDescription() {
		return onsetDescription;
	}
	public void setOnsetDescription(String onsetDescription) {
		this.onsetDescription = onsetDescription;
	}
	public String getOffsetDescription() {
		return offsetDescription;
	}
	public void setOffsetDescription(String offsetDescription) {
		this.offsetDescription = offsetDescription;
	}
	/** Short name (Label) of the Concept.
	 * e.g. "ECG_Delta_Wave_Complex"
	 * @return
	 */
	public String getConceptLabel() {
		return conceptLabel;
	}
	/** Short name (Label) of the Concept.
	 * e.g. "ECG_Delta_Wave_Complex"
	 * @param ontologyLabel
	 */
	public void setConceptLabel(String ontologyLabel) {
		this.conceptLabel = ontologyLabel;
	}
	
	public String getOnsetLabel() {
		return onsetLabel;
	}
	public void setOnsetLabel(String onsetLabel) {
		this.onsetLabel = onsetLabel;
	}
	public String getOffsetLabel() {
		return offsetLabel;
	}
	public void setOffsetLabel(String offsetLabel) {
		this.offsetLabel = offsetLabel;
	}
	public String getOnsetID() {
		return onsetID;
	}
	public void setOnsetID(String onsetID) {
		this.onsetID = onsetID;
	}
	public String getOffsetID() {
		return offsetID;
	}
	public void setOffsetID(String offsetID) {
		this.offsetID = offsetID;
	}
	/** Concept ID e.g. "ECGOntology:ECG_000000243".
	 * 
	 * @return
	 */
	public String getConceptID() {
		return conceptID;
	}
	/** Concept ID e.g. "ECGOntology:ECG_000000243".
	 * @param ontologyID
	 */
	public void setConceptID(String ontologyID) {
		this.conceptID = ontologyID;
	}
	
	public String getConceptRestURL() {
		return conceptRestURL;
	}
	public void setConceptRestURL(String conceptRestURL) {
		this.conceptRestURL = conceptRestURL;
	}

	public String getOnsetRestURL() {
		return onsetRestURL;
	}
	public void setOnsetRestURL(String onsetRestURL) {
		this.onsetRestURL = onsetRestURL;
	}
	
	public String getOffsetRestURL() {
		return offsetRestURL;
	}
	public void setOffsetRestURL(String offsetRestURL) {
		this.offsetRestURL = offsetRestURL;
	}

	/**
	 * Compares the t-Coordinates for each annotations object (assumes it is a lead annotation for now, otherwise it will return an equals)
	 * 
	 */
	public int compareTo(AnnotationData o) {
		// Check to make sure that the annotation passed in is a valid one		
		if(o != null) {
			if(this.milliSecondStart == o.getMilliSecondStart()) {
				return 0;
			}
			else if(this.milliSecondStart < o.getMilliSecondStart()) {
				return -1;
			}
			else if(this.milliSecondStart > o.getMilliSecondStart()) {
				return 1;
			}
		}
		
		return 0;
	}

}