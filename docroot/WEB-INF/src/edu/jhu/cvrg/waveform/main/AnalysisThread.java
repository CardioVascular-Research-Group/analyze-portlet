package edu.jhu.cvrg.waveform.main;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;

import org.apache.axiom.om.OMElement;
import org.apache.log4j.Logger;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.exception.SystemException;
import com.liferay.portal.kernel.repository.model.FileEntry;
import com.liferay.portal.model.User;
import com.liferay.portal.security.auth.PrincipalThreadLocal;
import com.liferay.portal.security.permission.PermissionChecker;
import com.liferay.portal.security.permission.PermissionCheckerFactoryUtil;
import com.liferay.portal.security.permission.PermissionThreadLocal;
import com.liferay.portal.service.UserLocalServiceUtil;
import com.liferay.portlet.documentlibrary.service.DLAppLocalServiceUtil;

import edu.jhu.cvrg.dbapi.dto.AnnotationDTO;
import edu.jhu.cvrg.dbapi.factory.Connection;
import edu.jhu.cvrg.waveform.utility.ServerUtility;
import edu.jhu.cvrg.waveform.utility.WebServiceUtility;

public class AnalysisThread extends Thread{

	private Map<String, String> map;
	private Connection dbUtility;
	private long documentRecordId;
	private boolean hasWfdbAnnotationOutput;
	private ArrayList<FileEntry> originFiles;
	private long userId;
	
	private String headerFileName;
	private String annotation;
	private ServerUtility util = new ServerUtility(false);
	private Logger log = Logger.getLogger(AnalysisThread.class);
	
	public AnalysisThread(Map<String, String> params, long documentRecordId, boolean hasWfdbAnnotationOutput, ArrayList<FileEntry> originFiles, long userId, Connection dbUtility) {
		super(params.get("jobID"));
		this.dbUtility = dbUtility;
		this.map = params;
		this.documentRecordId = documentRecordId;
		this.hasWfdbAnnotationOutput = hasWfdbAnnotationOutput;
		this.originFiles = originFiles;
		this.userId = userId;
	}
	
	public AnalysisThread(Map<String, String> params, long documentRecordId, boolean hasWfdbAnnotationOutput, ArrayList<FileEntry> originFiles, long userId, Connection dbUtility, ThreadGroup threadGroup) {
		super(threadGroup, params.get("jobID"));
		this.dbUtility = dbUtility;
		this.map = params;
		this.documentRecordId = documentRecordId;
		this.hasWfdbAnnotationOutput = hasWfdbAnnotationOutput;
		this.originFiles = originFiles;
		this.userId = userId;
	}
	
	@Override
	public void run() {
		
		
		//TODO [VILARDO] ADD THE RESULTS ACQUIRED CALLBACK HERE
		OMElement jobResult = WebServiceUtility.callWebService(map,map.get("method"),map.get("serviceName"), map.get("URL"), null, null);
		
		Map<String, OMElement> params = WebServiceUtility.extractParams(jobResult);
		
		if(params != null){
			int fileCount = Integer.valueOf(params.get("filecount").getText());
			OMElement fileList = params.get("fileList");
			
			long[] filesId = new long[fileCount];
			
			int i = 0;
			for (Iterator<OMElement> iterator = fileList.getChildElements(); iterator.hasNext();) {
				OMElement file = iterator.next();
				
				String fileIdStr = AnalysisThread.getElementByName(file, "FileId").getText();
				
				Long fileId = null; 
				if(fileIdStr != null && fileIdStr.length() > 0){
					fileId = Long.valueOf(fileIdStr);
					filesId[i] = fileId;
					i++;
				}
			}
			
			Long jobId = Long.valueOf(map.get("jobID").replaceAll("\\D", ""));
			
			recordAnalysisResults(documentRecordId, jobId, filesId);
		}
	
	}
	
	public static OMElement getElementByName(OMElement parent, String tagName){
		return parent.getFirstChildWithName(new QName(parent.getNamespace().getNamespaceURI(), tagName, parent.getNamespace().getPrefix()));
	}
	
	private void recordAnalysisResults(Long documentRecordId, Long jobId, long[] filesId) {
		
		if(filesId != null){
			dbUtility.storeFilesInfo(documentRecordId, filesId, jobId);
		}
		
		if(hasWfdbAnnotationOutput){
			
			try {
				
				this.initializeLiferayPermissionChecker(userId);
				
				this.createTempFiles(jobId, originFiles, filesId);
				List<String[]> result = execute_rdann(headerFileName, annotation);
				result = this.changePhysioBankToOntology(result);
				this.storeAnnotationList(result, documentRecordId);
				
			} catch (PortalException e) {
				e.printStackTrace();
			} catch (SystemException e) {
				e.printStackTrace();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	
	private void initializeLiferayPermissionChecker(long userId) throws Exception {
		PrincipalThreadLocal.setName(userId);
		
        User user = UserLocalServiceUtil.getUserById(userId);

        PermissionChecker permissionChecker = PermissionCheckerFactoryUtil.create(user);

        PermissionThreadLocal.setPermissionChecker(permissionChecker);
		
	}

	/** Reads a WFDB annotation file and returns an ArrayList of String arrays containing the data from the WFDB annotation file.<BR>
	 * <BR>
	 * Below is an example of  first 2 1/2 seconds from wqrs run on twa01.hea,twa01.dat<BR>
	 * Command that makes the annotation file:  wqrs -r twa01 -v -j<BR>
	 * Command that parses the annotation file: rdann -x -v -r twa01 -a wqrs -t 2.5<BR>
	 array[0]     [1]        [2]    [3]  [4]  [5]  [6]      [7]		 <BR>
	  Seconds   Minutes     Hours  Type  Sub Chan  Num      Aux<BR>
	    0.194   0.00323 0.0000539     N    0    0    0<BR>
	    0.260   0.00433 0.0000722     )    0    0    0<BR>
	    0.752   0.01253 0.0002089     N    0    0    0<BR>
	    0.818   0.01363 0.0002272     )    0    0    0<BR>
	    1.288   0.02147 0.0003578     N    0    0    0<BR>
	    1.354   0.02257 0.0003761     )    0    0    0<BR>
	    1.820   0.03033 0.0005056     N    0    0    0<BR>
	    1.888   0.03147 0.0005244     )    0    0    0<BR>
	    2.362   0.03937 0.0006561     N    0    0    0<BR>
	    2.428   0.04047 0.0006744     )    0    0    0<BR>
	    @see  http://www.physionet.org/physiobank/annotations.shtml
	    @return In each String array the elements contain the following data:<BR>
	    @param headerFileName Header file name.
	    @param annotation is the suffix (extension) of the bare name of the annotation file
	 * [0] - Seconds, total time from the beginning of the file in decimal seconds, to 3 decimal places.<BR>
	 * [1] - Minutes, same value as Seconds above, but in units of Minutes, to 5 decimal places (e.g. Seconds/60.0)<BR>
	 * [2] - Hours, same value as Seconds above, but in units of Hours, to 7 decimal places (e.g. Seconds/3600.0)<BR>
	 * [3] - Type, PhysioBank Annotation Code, <BR>
	 * [4] - Sub, Subtype - context-dependent attribute (see the documentation for each Physionet database for details), it's always been zero on our sample data.<BR>
	 * [5] - Chan, Channel(lead) number, zero base integer. (e.g. 0 = leadI, 5 = leadV1)<BR>
	 * [6] - Num, context-dependent attribute (see the documentation for each Physionet database for details), it's always been zero on our sample data.<BR>
	 * [7] - Aux, a free text string, it's always been blank on our sample data.<BR>
	**/
	private List<String[]> execute_rdann(String headerFileName, String annotation){
		log.info("execute_rdann(), only works on Linux with the WFBD library installed.");
		List<String[]> alistAnnotation = new ArrayList<String[]>();
		
		String[] asEnvVar = new String[0];   
		int iIndexPeriod = headerFileName.lastIndexOf(".");
		String sRecord = headerFileName.substring(0, iIndexPeriod);
		 
		try { 
			
			// rdann -v -x -r twa01 -a wqrs
			String sCommand = "rdann -x -r " + sRecord + " -a " + annotation;
			util.executeCommand(sCommand, asEnvVar, "/");
			
			String line;
			int lineNum = 0;
		    String[] columns;
		    
		    //iterate thru the returned text of the command, one line per annotation.");
		    while ((line = util.stdInputBuffer.readLine()) != null) {
		    	// columns: Seconds   Minutes     Hours  Type  Sub Chan  Num      Aux
				columns = line.split("\\s+");
				alistAnnotation.add(columns);
				lineNum++;
			}
			log.debug("--- execute_rdann() found " + lineNum + " annotations");
		} catch (IOException e) {
			e.printStackTrace();
		}finally{
			File jobFolder = new File(headerFileName).getParentFile();
			File[] files = jobFolder.listFiles();
			
			for (File file : files) {
				file.delete();
			}
			
			jobFolder.delete();
		}
		return alistAnnotation;
	}
	
	private void createTempFiles(long jobId, List<FileEntry> wfdbFiles, long[] filesId ) throws PortalException, SystemException {
		
		String tempPath = System.getProperty("java.io.tmpdir") + File.separator + "waveform/a" + File.separator + jobId + File.separator;
		
		
		for (long fileId : filesId) {
			FileEntry file = DLAppLocalServiceUtil.getFileEntry(fileId);
			
			if(AnalysisThread.isAnotationFile(file.getTitle())){
				wfdbFiles.add(file);
				annotation = file.getExtension();
				break;
			}
		}
		
		for (FileEntry liferayFile : wfdbFiles) {
			
			File targetDirectory = new File(tempPath);
			
			File targetFile = new File(tempPath + liferayFile.getTitle());
			
			try {
				targetDirectory.mkdirs();
				
				InputStream fileToSave = liferayFile.getContentStream();
				
				OutputStream fOutStream = new FileOutputStream(targetFile);

				int read = 0;
				byte[] bytes = new byte[1024];

				while ((read = fileToSave.read(bytes)) != -1) {
					fOutStream.write(bytes, 0, read);
				}

				fileToSave.close();
				fOutStream.flush();
				fOutStream.close();
				
			} catch (IOException e) {
				e.printStackTrace();
			}finally{
				log.info("File created? " + targetFile.exists());
			}
			
			if(AnalysisThread.isHeaderFile(liferayFile.getTitle())){
				headerFileName = targetFile.getAbsolutePath();
			}
		}
		
	}
	
	/** Change all of the values from PhysioBank Annotation Codes to the corresponding Bioportal ECG ontology IDs.
	 * STUB METHOD
	 * @param annotationMap - the original annotations with Key =  time stamp, and Value = PhysioBank Annotation Codes
	 * @return - same as the passed in LinkedHashMap but with the Values as Bioportal ECG ontology IDs.
	 */
	private List<String[]> changePhysioBankToOntology(List<String[]>  alistAnnotation){
		// TODO STUB METHOD-Needs to be fully implemented.
		System.out.println("- changePhysioBankToOntology() alistAnnotation.size():" + alistAnnotation.size());
		String sPhysioBankCode="";
		for(String[] saAnnot : alistAnnotation){
			sPhysioBankCode = saAnnot[4];
			if(sPhysioBankCode.equals("N")){
				saAnnot[4] = "ECGTermsv1:ECG_000000023";
			}else if(sPhysioBankCode.equals(")")){
				saAnnot[4] = "ECGTermsv1:ECG_000000236";
			}							
		}
		return alistAnnotation;
	}
	
	/** Stores the annotations from the ArrayList of String arrays generated by execute_rdann().<BR>
	 * Data are stored in database using Connection.storeLeadAnnotationNode().
	 * 
	 * @param alistAnnotation - output of execute_rdann() or changePhysioBankToOntology()
	 * @return true if all stored successfully
	 */	 
	private void storeAnnotationList(List<String[]> alistAnnotation, long recordId){
		//	 * Required values that need to be filled in are:
		//	 * 
		//	 * created by (x) - the source of this annotation (whether it came from an algorithm or was entered manually)
		//	 * concept label - the type of annotation as defined in the annotation's bioportal reference term
		//	 * annotation ID - a unique ID used for easy retrieval of the annotation in the database
		//	 * onset label - the bioportal reference term for the onset position.  This indicates the start point of an interval
		//	 * 					or the location of a single point
		//	 * onset y-coordinate - the y coordinate for that point on the ECG wave
		//	 * onset t-coordinate - the t coordinate for that point on the ECG wave.
		//	 * an "isInterval" boolean - for determining whether this is an interval (and thus needs an offset tag)
		//	 * Full text description - This is the "value" so to speak, and contains the full definition of the annotation type being used
		//	 * 
		//	 * Note:  If this is an interval, then an offset label, y-coordinate, and t-coordinate are required for that as well.
		
		Set<AnnotationDTO> toPersist = new HashSet<AnnotationDTO>();
		for(String[] saAnnot : alistAnnotation){

			if( (saAnnot != null) & (saAnnot.length>=6)){
				try {
					double dMilliSec = Double.parseDouble(saAnnot[1]);
					String sOntologyID = saAnnot[4];
					int iLeadIndex = Integer.parseInt(saAnnot[6]);
					double dMicroVolt = lookupVoltage(dMilliSec,iLeadIndex);
					
					String[] saOntDetails = WebServiceUtility.lookupOntologyDefinition(sOntologyID); // ECGTermsv1:ECG_000000103
					String sTermName = saOntDetails[0];
					String sFullAnnotation=saOntDetails[1];

					AnnotationDTO annotationToInsert = new AnnotationDTO(0L/*userid*/, 0L/*groupID*/, 0L/*companyID*/, recordId, null/*createdBy*/, "ANNOTATION", sTermName, sOntologyID,
																		 null/*bioportalRef*/, iLeadIndex, null/*unitMeasurement*/, null/*description*/,sFullAnnotation, Calendar.getInstance(), dMilliSec, dMicroVolt,
																		 0.0, 0.0, null/*newStudyID*/, null/*newRecordName*/, null/*newSubjectID*/);
	
					// Inserting save to XML database
					toPersist.add(annotationToInsert);
					
				} catch (NumberFormatException e) {
					e.printStackTrace();
				} catch (Exception e){
					e.printStackTrace();
				}
			}else{
				System.out.println("-- ERROR bad annotatation");
			}
		}
		
		Long insertedQtd = dbUtility.storeAnnotations(toPersist);
		
		if(toPersist.size() != insertedQtd) {
			log.error("Annotation did not save properly");
		}
		
	}
	
	/** Looks up the voltage value for the specified timestamp on the specified lead index.
	 * 
	 * @param dMilliSec
	 * @param iLeadIndex
	 * @return
	 */
	private double lookupVoltage(double dMilliSec,int iLeadIndex){
		//TODO implement this, for now zero will work because the dygraph annotation display code ignores the y axis value.
		return 0.0;		
	}
	
	private static boolean isAnotationFile(String fileName){
		String tmp = fileName.replaceAll("\\.(w?qrs|atr)\\d*$", "");
		return !tmp.equals(fileName);
	}
	
	private static boolean isHeaderFile(String fileName){
		String tmp = fileName.replaceAll("\\.hea$", "");
		return !tmp.equals(fileName);
	}
}