package edu.jhu.cvrg.waveform.utility;


import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import edu.jhu.cvrg.waveform.model.AnalysisResult;
/** The methods in this class are called when an analysis job reaches various stages of the analysisPipeline.
 * 
 * @author mshipwa1
 * @author sgranite
 *
 */
@ManagedBean(name = "progressNotification")
@ViewScoped
public final class ProgressNotification {
	
	private static AnalysisUtility analysisUtility = new AnalysisUtility();
	private static ResultsStorageDBUtility resultsStorageDBUtility = new ResultsStorageDBUtility();
	
	/** The initialization of an analysis Job. At this point an entry in the tracking database has been made, but no other work has been done.
	 * 
	 * @param sUserId - The id of the user running the jobs. 
	 * @param sDate - The date that the analysis was run. 
	 */
	public static void step0_jobSubmitted(String sUserId, String sDate){
		ArrayList<AnalysisResult> userResultFiles = resultsStorageDBUtility.getAnalysisResults(sUserId);
		boolean isTheFirstJobOfTheDay = true;
		DateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
		Calendar theCalendar = Calendar.getInstance();
		Date currentTime = theCalendar.getTime();
		String analysisDate = displayFormat.format(currentTime);

		for(AnalysisResult analysisResult : userResultFiles){
			if (analysisResult.getDateOfAnalysis().equalsIgnoreCase(analysisDate)) {
				isTheFirstJobOfTheDay = false;
			}
		}
		
		if (isTheFirstJobOfTheDay) {
			analysisUtility.createProgressAccumulator(sUserId, sDate);
		}
		
		analysisUtility.incrementProgressPercentAccumulation(sUserId, sDate, 10);
		analysisUtility.incrementProgressJobTotal(sUserId, sDate);
		System.out.println("progressNotification.step0_jobSubmitted added 1 job and incremented total percentage by 10%");
	}
	
	/** Tells analysis server to ftp the data files from the ftpHost. 
	 * 
	 * @param sUserId - The id of the user running the jobs. 
	 * @param sDate - The date that the analysis was run. 
	 */
	public static void step1A_TransferDataFilesToAnalysisCB(String sUserId, String sDate){
		analysisUtility.incrementProgressPercentAccumulation(sUserId, sDate, 10);
		System.out.println("progressNotification.step1A_TransferDataFilesToAnalysisCB incremented total percentage by 5%");
	}
	
	/** Analysis server has reported that it has the data files.
	 * 
	 * @param sUserId - The id of the user running the jobs. 
	 * @param sDate - The date that the analysis was run. 
	 */
	public static void step1B_FilesAcquiredCallback(String sUserId, String sDate){
		analysisUtility.incrementProgressPercentAccumulation(sUserId, sDate, 20);
		System.out.println("progressNotification.step1B_FilesAcquiredCallback incremented total percentage by 20%");
	}
	
	/** Starting the analysis web service.
	 * 
	 * @param sUserId - The id of the user running the jobs. 
	 * @param sDate - The date that the analysis was run. 
	 */
	public static void step2A_OrderAnalysisWS(String sUserId, String sDate){
		analysisUtility.incrementProgressPercentAccumulation(sUserId, sDate, 10);
		System.out.println("progressNotification.step2A_OrderAnalysisWS incremented total percentage by 5%");
	}
	
	/** Analysis server has reported that it has finished the analysis.
	 * 
	 * @param sUserId - The id of the user running the jobs. 
	 * @param sDate - The date that the analysis was run. 
	 */
	public static void step2B_AnalysisDoneCallback(String sUserId, String sDate){
		analysisUtility.incrementProgressPercentAccumulation(sUserId, sDate, 20);
		System.out.println("progressNotification.step2B_AnalysisDoneCallback incremented total percentage by 20%");
	}
	
	/** Telling the analysis server to ftp the result files to the ECGrid's file repository.
	 * 
	 * @param sUserId - The id of the user running the jobs. 
	 * @param sDate - The date that the analysis was run. 
	 */
	public static void step3A_CopyResultFilesToECGridToolkit(String sUserId, String sDate){
		analysisUtility.incrementProgressPercentAccumulation(sUserId, sDate, 10);
		System.out.println("progressNotification.step3A_CopyResultFilesToECGridToolkit incremented total percentage by 20%");
	}
	
	/** Analysis server has reported that it has finished sending us the result file.
	 * 
	 * @param sUserId - The id of the user running the jobs. 
	 * @param sDate - The date that the analysis was run. 
	 */
	public static void step3B_ResultsAcquiredCallback(String sUserId, String sDate){
		analysisUtility.incrementProgressPercentAccumulation(sUserId, sDate, 20);
		analysisUtility.incrementProgressJobComplete(sUserId, sDate);
		System.out.println("progressNotification.step3B_ResultsAcquiredCallback incremented total percentage by 20%");
	}
	
	
	/** Saves the result files into the Big XML database as annotations.
	 * 
	 * @param sUserId - The id of the user running the jobs. 
	 * @param sDate - The date that the analysis was run. 
	 */
	public static void step4_RecordAnalysisResults(String sUserId, String sDate){
		//analysisUtility.incrementProgressPercentAccumulation(sUserId, sDate, 5);
		System.out.println("progressNotification.step4_RecordAnalysisResults incremented total percentage by 20%");
	}
	
	
	/** Telling the analysis server that it can safely delete it's copies of the data and result files.
	 * 
	 * @param sUserId - The id of the user running the jobs. 
	 * @param sDate - The date that the analysis was run. 
	 */
	public static void step5_CleanupAnalysis(String sUserId, String sDate){
		//analysisUtility.incrementProgressPercentAccumulation(sUserId, sDate, 5);
		System.out.println("progressNotification.step5_CleanupAnalysis incremented total percentage by 20%");
	}
	
}
