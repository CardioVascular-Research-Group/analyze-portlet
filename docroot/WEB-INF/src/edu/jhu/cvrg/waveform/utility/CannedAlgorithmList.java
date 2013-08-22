package edu.jhu.cvrg.waveform.utility;

import edu.jhu.cvrg.waveform.model.Algorithm;


public class CannedAlgorithmList {
	
	/**
	 * Returns a canned list of algorithms that are currently supported by Waveform<BR>
	 * 
	 * Note:  This is a deprecated method.  In the next version it will be removed and replaced by a web service call<BR>
	 *  0:sqrsWrapperType2 - Detect QRS (sqrs)<BR>
		1:wqrsWrapperType2 - Detect QRS (wqrs)<BR>
		2:ann2rrWrapperType2 - Detect RR Intervals<BR>
		3:pnnlistWrapperType2 - Heart Rate Variability<BR>
		4:nguessWrapperType2 - Locate Missing Beats<BR>
		5:sigampWrapperType2 - QRS Peak Amplitude<BR>
		6:chesnokovWrapperType2 - QT Screening<BR>
		
		8:ssQRS_ScoreWrapperType2 - Strauss-Sylvester score
		default:ssQRS_ScoreWrapperType2 - Strauss-Sylvester score
		
	 * @return
	 */
	public static Algorithm[] getAlgorithmList() {
		
		Algorithm[] algorithmDetails = new Algorithm[7];
		
		int iParamCount = 1;
		
		for(int i=0;i<algorithmDetails.length;i++) {
			
			algorithmDetails[i] = new Algorithm();
			
			switch(i) {

			case 0:
				algorithmDetails[i].sServiceMethod = "sqrsWrapperType2";
				algorithmDetails[i].sDisplayShortName = "Detect QRS (sqrs)";
				algorithmDetails[i].sAnalysisServiceURL = "http://128.220.76.170:8080/axis2/services/physionetAnalysisService";

				algorithmDetails[i].sServiceName = "physionetAnalysisService";
				//algorithmDetails[i].sServiceMethod = "sqrsWrapperType2";
				algorithmDetails[i].sToolTipDescription = "Single-channel QRS detector.";
				algorithmDetails[i].sURLreference = "http://www.physionet.org/physiotools/wag/sqrs-1.htm";
				algorithmDetails[i].sLongDescription = "sqrs attempts to locate QRS complexes in an ECG signal in the specified record. The detector algorithm is based on example 10 in the WFDB Programmer�s Guide, which in turn is based on a Pascal program written by W.A.H. Engelse and C. Zeelenberg, ��A single scan algorithm for QRS-detection and feature extraction��, Computers in Cardiology 6:37-42 (1979). sqrs does not include the feature extraction capability of the Pascal program. The output of sqrs is an annotation file (with annotator name qrs) in which all detected beats are labelled normal; the annotation file may also contain �artifact� annotations at locations that sqrs believes are noise-corrupted.\n"
					+ " sqrs can process records containing any number of signals, but it uses only one signal for QRS detection (signal 0 by default; this can be changed using the -s option, see below). sqrs is optimized for use with adult human ECGs. For other ECGs, it may be necessary to experiment with the sampling frequency as recorded in the input record�s header file (see header(5) ) and the time constants indicated in the source file.\n"
					+ " sqrs uses the WFDB library�s setifreq function to resample the input signal at 250 Hz if a significantly different sampling frequency is indicated in the header file. sqrs125 is identical to sqrs except that its filter and time constants have been designed for 125 Hz input, so that its speed is roughly twice that of sqrs. If the input signal has been sampled at a frequency near 125 Hz, the quality of the outputs of sqrs and sqrs125 will be nearly identical. (Note that older versions of these programs did not resample their inputs; rather, they warned if the sampling frequency was significantly different than the ideal frequency, and suggested using xform(1) to resample the input.)\n"
					+ " This program is provided as an example only, and is not intended for any clinical application. At the time the algorithm was originally published, its performance was typical of state-of-the-art QRS detectors. Recent designs, particularly those that can analyze two or more input signals, may exhibit significantly better performance. ";
				algorithmDetails[i].sVersionIdWebService = "2.0";
				algorithmDetails[i].sDateWebService = "November 16, 2012";
				algorithmDetails[i].sLicence = "http://physionet.org/physiotools/wag/wag.htm";
	
				iParamCount = 5;
				algorithmDetails[i].aParameters =  new AdditionalParameters[iParamCount];
				  for(int p=0; p<iParamCount;p++){
					  algorithmDetails[i].aParameters[p] = new AdditionalParameters();
				  }
				  algorithmDetails[i].aParameters[0].sParameterFlag = "f";
				  algorithmDetails[i].aParameters[0].sDisplayShortName = "Begin time(seconds)";
				  algorithmDetails[i].aParameters[0].sToolTipDescription = "Begin at the specified time in record (default: the beginning of record).";
				  algorithmDetails[i].aParameters[0].sLongDescription = "Begin at the specified time in record (default: the beginning of record).";
				  algorithmDetails[i].aParameters[0].sParameterDefaultValue = "0";
				  algorithmDetails[i].aParameters[0].sType = "float";
				  algorithmDetails[i].aParameters[0].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[1].sParameterFlag = "t";
				  algorithmDetails[i].aParameters[1].sDisplayShortName = "End time(seconds)";
				  algorithmDetails[i].aParameters[1].sToolTipDescription = "Process until the specified time in record (default: the end of the record). ";
				  algorithmDetails[i].aParameters[1].sLongDescription = "Process until the specified time in record (default: the end of the record). ";
				  algorithmDetails[i].aParameters[1].sParameterDefaultValue = "-1";
				  algorithmDetails[i].aParameters[1].sType = "float";
				  algorithmDetails[i].aParameters[1].bOptional = "true"; 
				  //-------------
				  algorithmDetails[i].aParameters[2].sParameterFlag = "H";
				  algorithmDetails[i].aParameters[2].sDisplayShortName = "High Res";
				  algorithmDetails[i].aParameters[2].sToolTipDescription = "Read the signal files in high-resolution mode (default: standard mode).";
				  algorithmDetails[i].aParameters[2].sLongDescription = "Read the signal files in high-resolution mode (default: standard mode). These modes are identical for ordinary records. For multifrequency records, the standard decimation of oversampled signals to the frame rate is suppressed in high-resolution mode (rather, all other signals are resampled at the highest sampling frequency).";
				  algorithmDetails[i].aParameters[2].sParameterDefaultValue = "false";
				  algorithmDetails[i].aParameters[2].sType = "boolean";
				  algorithmDetails[i].aParameters[2].bOptional = "false";
				  //-------------
				  algorithmDetails[i].aParameters[3].sParameterFlag = "m";
				  algorithmDetails[i].aParameters[3].sDisplayShortName = "Threshold";
				  algorithmDetails[i].aParameters[3].sToolTipDescription = "Specify the detection threshold (default: 500 units).";
				  algorithmDetails[i].aParameters[3].sLongDescription = "Specify the detection threshold (default: 500 units); use higher values to reduce false detections, or lower values to reduce the number of missed beats.";
				  algorithmDetails[i].aParameters[3].sParameterDefaultValue = "500";
				  algorithmDetails[i].aParameters[3].sType = "integer";
				  algorithmDetails[i].aParameters[3].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[4].sParameterFlag = "s";
				  algorithmDetails[i].aParameters[4].sDisplayShortName = "Signal";
				  algorithmDetails[i].aParameters[4].sToolTipDescription = "Specify the signal (number or name) to be used for QRS detection (default: 0).";
				  algorithmDetails[i].aParameters[4].sLongDescription = "Specify the signal (number or name) to be used for QRS detection (default: 0).";
				  algorithmDetails[i].aParameters[4].sParameterDefaultValue = "0";
				  algorithmDetails[i].aParameters[4].sType = "integer";
				  algorithmDetails[i].aParameters[4].bOptional = "false";
				  //-------------

				algorithmDetails[i].afInFileTypes = new FileTypes[3];
				  for(int f=0; f<3;f++){
					  algorithmDetails[i].afInFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afInFileTypes[0].sName="WFDBheader";
				  algorithmDetails[i].afInFileTypes[0].sExtension="hea";
				  algorithmDetails[i].afInFileTypes[0].sDisplayShortName = "WFDB header";
				//-------------
				  algorithmDetails[i].afInFileTypes[1].sName="WFDBdata";
				  algorithmDetails[i].afInFileTypes[1].sExtension = "dat";
				  algorithmDetails[i].afInFileTypes[1].sDisplayShortName = "WFDB data";
				//-------------
				  algorithmDetails[i].afInFileTypes[2].sName="WFDBxyz";
				  algorithmDetails[i].afInFileTypes[2].sExtension = "xyz";
				  algorithmDetails[i].afInFileTypes[2].sDisplayShortName = "WFDB VCG xyz data";
				//-------------
				  
				algorithmDetails[i].afOutFileTypes = new FileTypes[2];
				  for(int f=0; f<2;f++){
					  algorithmDetails[i].afOutFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afOutFileTypes[0].sName="WFDBqrsAnnotation";
				  algorithmDetails[i].afOutFileTypes[0].sExtension="qrs";
				  algorithmDetails[i].afOutFileTypes[0].sDisplayShortName = "qrs Annotation";
				//-------------
				  algorithmDetails[i].afOutFileTypes[1].sName="WFDBAnnotationText";
				  algorithmDetails[i].afOutFileTypes[1].sExtension = "txt";
				  algorithmDetails[i].afOutFileTypes[1].sDisplayShortName = "WFDB Annotation Text";
				//-------------
					  
				algorithmDetails[i].apAlgorithmProgrammers = new People[3];
				  for(int p=0; p<3;p++){
					  algorithmDetails[i].apAlgorithmProgrammers[p] = new People();
				  }
				  algorithmDetails[i].apAlgorithmProgrammers[0].sEmail="george@mit.edu";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sFirstName="George";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sMiddleName="B.";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sLastName="Moody";  
				  algorithmDetails[i].apAlgorithmProgrammers[0].sOrganization="Physionet";
				//-------------------  
				  algorithmDetails[i].apAlgorithmProgrammers[1].sFirstName="WAH";
				  algorithmDetails[i].apAlgorithmProgrammers[1].sLastName="Engelse";  
				//-------------------  
				  algorithmDetails[i].apAlgorithmProgrammers[2].sFirstName="Cees";
				  algorithmDetails[i].apAlgorithmProgrammers[2].sLastName="Zeelenberg";  
				//-------------------  
				  
				algorithmDetails[i].apWebServiceProgrammers = new People[1];
				  for(int p=0; p<1;p++){
					  algorithmDetails[i].apWebServiceProgrammers[p] = new People();
				  }
				  algorithmDetails[i].apWebServiceProgrammers[0].sEmail="mshipwa1@jhu.edu";
				  algorithmDetails[i].apWebServiceProgrammers[0].sFirstName="Michael";
				  algorithmDetails[i].apWebServiceProgrammers[0].sMiddleName="P.";
				  algorithmDetails[i].apWebServiceProgrammers[0].sLastName="Shipway";  
				  algorithmDetails[i].apWebServiceProgrammers[0].sOrganization="Johns Hopkins University";
				//-------------------  
					  
				algorithmDetails[i].aoAffiliatedOrgs = new Organization[3];
				  for(int p=0; p<3;p++){
					  algorithmDetails[i].aoAffiliatedOrgs[p] = new Organization();
				  }
				  algorithmDetails[i].aoAffiliatedOrgs[0].sURL="www.jhu.edu";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sName="Johns Hopkins University";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactEmail = "mshipwa1@jhu.edu";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactPhone = "(410) 516-5294";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactFirstName = "Michael";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactMiddleName = "P.";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactLastName = "Shipway";
	
				  
				  algorithmDetails[i].aoAffiliatedOrgs[1].sURL="www.cvrgrid.org";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sName="CardioVascular Research Grid (CVRG)";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactEmail = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactPhone = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactFirstName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactMiddleName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactLastName = "";
				//-------------------  
				  algorithmDetails[i].aoAffiliatedOrgs[2].sURL="http://www.physionet.org";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sName="PhysioNet";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactEmail = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactPhone = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactFirstName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactMiddleName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactLastName = "";
				//-------------------  
	
				
				break;
			case 1:
				algorithmDetails[i].sServiceMethod = "wqrsWrapperType2";
				algorithmDetails[i].sDisplayShortName = "Detect QRS (wqrs)";
				algorithmDetails[i].sAnalysisServiceURL = "http://128.220.76.170:8080/axis2/services/physionetAnalysisService";
				
				//-------------------------------------------------
				//-----------  wqrs -----------
				//-------------------------------------------------
				//details.iWebServiceID = 0;

				algorithmDetails[i].sServiceName = "physionetAnalysisService";
				//algorithmDetails[i].sServiceMethod = "wqrsWrapperType2";
				algorithmDetails[i].sToolTipDescription = "single-channel QRS detector based on length transform.";
				algorithmDetails[i].sURLreference = "http://www.physionet.org/physiotools/wag/wqrs-1.htm";
				algorithmDetails[i].sLongDescription = "wqrs attempts to locate QRS complexes in an ECG signal in the specified record. The detector algorithm is based on the length transform. The output of wqrs is an annotation file (with annotator name wqrs) in which all detected beats are labelled normal; the annotation file will also contain optional J-point annotations if the -j option (see below) is used. \n"
					+ " wqrs can process records containing any number of signals, but it uses only one signal for QRS detection (signal 0 by default; this can be changed using the -s option, see below). wqrs is optimized for use with adult human ECGs. For other ECGs, it may be necessary to experiment with the sampling frequency as recorded in the input record�s header file (see header(5) ), the detector threshold (which can be set using the -m option), and the time constants indicated in the source file. \n"
					+ " wqrs optionally uses the WFDB library�s setifreq function to resample the input signal at 120 or 150 Hz (depending on the mains frequency, which can be specified using the -p option). wqrs performs well using input sampled at a range of rates up to 360 Hz and possibly higher rates, but it has been designed and tested to work best on signals sampled at 120 or 150 Hz. \n";
				algorithmDetails[i].sVersionIdWebService = "2.0";
				algorithmDetails[i].sDateWebService = "Dec. 3, 2012";
				algorithmDetails[i].sLicence = "http://physionet.org/physiotools/wag/wag.htm";
	

				algorithmDetails[i].aParameters =  new AdditionalParameters[11];
				  for(int p=0; p<11;p++){
					  algorithmDetails[i].aParameters[p] = new AdditionalParameters();
				  }
				  algorithmDetails[i].aParameters[0].sParameterFlag = "d";
				  algorithmDetails[i].aParameters[0].sDisplayShortName = "Dump the raw";
				  algorithmDetails[i].aParameters[0].sToolTipDescription = "Dump the raw and length-transformed input samples in text format on the standard output, but do not detect or annotate QRS complexes.";
				  algorithmDetails[i].aParameters[0].sLongDescription = "Dump the raw and length-transformed input samples in text format on the standard output, but do not detect or annotate QRS complexes.";
				  algorithmDetails[i].aParameters[0].sParameterDefaultValue = "false";
				  algorithmDetails[i].aParameters[0].sType = "boolean";
				  algorithmDetails[i].aParameters[0].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[1].sParameterFlag = "f";
				  algorithmDetails[i].aParameters[1].sDisplayShortName = "Begin time (sec)";
				  algorithmDetails[i].aParameters[1].sToolTipDescription = "Begin at the specified time in record (default: the beginning of record).";
				  algorithmDetails[i].aParameters[1].sLongDescription = "Begin at the specified time in record (default: the beginning of record).";
				  algorithmDetails[i].aParameters[1].sParameterDefaultValue = "0";
				  algorithmDetails[i].aParameters[1].sType = "float";
				  algorithmDetails[i].aParameters[1].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[2].sParameterFlag = "h";
				  algorithmDetails[i].aParameters[2].sDisplayShortName = "Print a brief usage (help) summary.";
				  algorithmDetails[i].aParameters[2].sToolTipDescription = "Print a brief usage (help) summary.";
				  algorithmDetails[i].aParameters[2].sLongDescription = "Print a brief usage (help) summary.";
				  algorithmDetails[i].aParameters[2].sParameterDefaultValue = "false";
				  algorithmDetails[i].aParameters[2].sType = "boolean";
				  algorithmDetails[i].aParameters[2].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[3].sParameterFlag = "H";
				  algorithmDetails[i].aParameters[3].sDisplayShortName = "High Res";
				  algorithmDetails[i].aParameters[3].sToolTipDescription = "Read the signal files in high-resolution mode (default: standard mode).";
				  algorithmDetails[i].aParameters[3].sLongDescription = "Read the signal files in high-resolution mode (default: standard mode). These modes are identical for ordinary records. For multifrequency records, the standard decimation of oversampled signals to the frame rate is suppressed in high-resolution mode (rather, all other signals are resampled at the highest sampling frequency).";
				  algorithmDetails[i].aParameters[3].sParameterDefaultValue = "false";
				  algorithmDetails[i].aParameters[3].sType = "boolean";
				  algorithmDetails[i].aParameters[3].bOptional = "false";
				  //-------------
				  algorithmDetails[i].aParameters[4].sParameterFlag = "j";
				  algorithmDetails[i].aParameters[4].sDisplayShortName = "Annotate J-points";
				  algorithmDetails[i].aParameters[4].sToolTipDescription = "Find and annotate J-points (QRS ends) as well as QRS onsets.";
				  algorithmDetails[i].aParameters[4].sLongDescription = "Find and annotate J-points (QRS ends) as well as QRS onsets.";
				  algorithmDetails[i].aParameters[4].sParameterDefaultValue = "false";
				  algorithmDetails[i].aParameters[4].sType = "boolean";
				  algorithmDetails[i].aParameters[4].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[5].sParameterFlag = "m";
				  algorithmDetails[i].aParameters[5].sDisplayShortName = "Threshold";
				  algorithmDetails[i].aParameters[5].sToolTipDescription = "Specify the detection threshold (default: 100 microvolts);";
				  algorithmDetails[i].aParameters[5].sLongDescription = "Specify the detection threshold (default: 100 microvolts); use higher values to reduce false detections, or lower values to reduce the number of missed beats.";
				  algorithmDetails[i].aParameters[5].sParameterDefaultValue = "100";
				  algorithmDetails[i].aParameters[5].sType = "integer";
				  algorithmDetails[i].aParameters[5].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[6].sParameterFlag = "p";
				  algorithmDetails[i].aParameters[6].sDisplayShortName = "Power line frequency.";
				  algorithmDetails[i].aParameters[6].sToolTipDescription = "Specify the power line (mains) frequency.";
				  algorithmDetails[i].aParameters[6].sLongDescription = "Specify the power line (mains) frequency used at the time of the recording, in Hz (default: 60). wqrs will apply a notch filter of the specified frequency to the input signal before length-transforming it.";
				  algorithmDetails[i].aParameters[6].sParameterDefaultValue = "60";
				  algorithmDetails[i].aParameters[6].sType = "integer";
				  algorithmDetails[i].aParameters[6].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[7].sParameterFlag = "R";
				  algorithmDetails[i].aParameters[7].sDisplayShortName = "Resample the input.";
				  algorithmDetails[i].aParameters[7].sToolTipDescription = "Resample the input at 120 Hz if the power line frequency is 60 Hz, or at 150 Hz otherwise (default: do not resample).";
				  algorithmDetails[i].aParameters[7].sLongDescription = "Resample the input at 120 Hz if the power line frequency is 60 Hz, or at 150 Hz otherwise (default: do not resample).";
				  algorithmDetails[i].aParameters[7].sParameterDefaultValue = "false";
				  algorithmDetails[i].aParameters[7].sType = "boolean";
				  algorithmDetails[i].aParameters[7].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[8].sParameterFlag = "s";
				  algorithmDetails[i].aParameters[8].sDisplayShortName = "Signal";
				  algorithmDetails[i].aParameters[8].sToolTipDescription = "Specify the signal (number or name) to be used for QRS detection (default: 0).";
				  algorithmDetails[i].aParameters[8].sLongDescription = "Specify the signal (number or name) to be used for QRS detection (default: 0).";
				  algorithmDetails[i].aParameters[8].sParameterDefaultValue = "0";
				  algorithmDetails[i].aParameters[8].sType = "integer";
				  algorithmDetails[i].aParameters[8].bOptional = "false";
				  //-------------
				  algorithmDetails[i].aParameters[9].sParameterFlag = "t";
				  algorithmDetails[i].aParameters[9].sDisplayShortName = "End time (sec)";
				  algorithmDetails[i].aParameters[9].sToolTipDescription = "Process until the specified time in record (default: the end of the record).";
				  algorithmDetails[i].aParameters[9].sLongDescription = "Process until the specified time in record (default: the end of the record).";
				  algorithmDetails[i].aParameters[9].sParameterDefaultValue = "-1";
				  algorithmDetails[i].aParameters[9].sType = "float";
				  algorithmDetails[i].aParameters[9].bOptional = "true"; 
				  //-------------
				  algorithmDetails[i].aParameters[10].sParameterFlag = "v";
				  algorithmDetails[i].aParameters[10].sDisplayShortName = "Verbose mode";
				  algorithmDetails[i].aParameters[10].sToolTipDescription = "Verbose mode: print information about the detector parameters.";
				  algorithmDetails[i].aParameters[10].sLongDescription = "Verbose mode: print information about the detector parameters.";
				  algorithmDetails[i].aParameters[10].sParameterDefaultValue = "false";
				  algorithmDetails[i].aParameters[10].sType = "boolean";
				  algorithmDetails[i].aParameters[10].bOptional = "true";
				  //-------------		  

				algorithmDetails[i].afInFileTypes = new FileTypes[4];
				  for(int f=0; f<4;f++){
					  algorithmDetails[i].afInFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afInFileTypes[0].sName="WFDBheader";
				  algorithmDetails[i].afInFileTypes[0].sExtension="hea";
				  algorithmDetails[i].afInFileTypes[0].sDisplayShortName = "WFDB header";
				//-------------
				  algorithmDetails[i].afInFileTypes[1].sName="WFDBdata";
				  algorithmDetails[i].afInFileTypes[1].sExtension = "dat";
				  algorithmDetails[i].afInFileTypes[1].sDisplayShortName = "WFDB data";
				//-------------
				  algorithmDetails[i].afInFileTypes[2].sName="WFDBxyz";
				  algorithmDetails[i].afInFileTypes[2].sExtension = "xyz";
				  algorithmDetails[i].afInFileTypes[2].sDisplayShortName = "WFDB VCG xyz data";
				//-------------
				  // this one is a dummy entry to test the CVRgrid Toolkit interface.
				  algorithmDetails[i].afInFileTypes[3].sName="TabDelimitedText";
				  algorithmDetails[i].afInFileTypes[3].sExtension = "txt";
				  algorithmDetails[i].afInFileTypes[3].sDisplayShortName = "GE_Magellan";
				//-------------
				  

				algorithmDetails[i].afOutFileTypes = new FileTypes[2];
				  for(int f=0; f<2;f++){
					  algorithmDetails[i].afOutFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afOutFileTypes[0].sName="WFDBqrsAnnotation";
				  algorithmDetails[i].afOutFileTypes[0].sExtension="qrs";
				  algorithmDetails[i].afOutFileTypes[0].sDisplayShortName = "qrs Annotation";
				//-------------
				  algorithmDetails[i].afOutFileTypes[1].sName="WFDBAnnotationText";
				  algorithmDetails[i].afOutFileTypes[1].sExtension = "txt";
				  algorithmDetails[i].afOutFileTypes[1].sDisplayShortName = "WFDB Annotation Text";
				//-------------
					  

				algorithmDetails[i].apAlgorithmProgrammers = new People[3];
				  for(int p=0; p<3;p++){
					  algorithmDetails[i].apAlgorithmProgrammers[p] = new People();
				  }
				  algorithmDetails[i].apAlgorithmProgrammers[0].sEmail="george@mit.edu";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sFirstName="George";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sMiddleName="B.";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sLastName="Moody";  
				  algorithmDetails[i].apAlgorithmProgrammers[0].sOrganization="Physionet";
				//-------------------  
				  algorithmDetails[i].apAlgorithmProgrammers[1].sFirstName="WAH";
				  algorithmDetails[i].apAlgorithmProgrammers[1].sLastName="Engelse";  
				//-------------------  
				  algorithmDetails[i].apAlgorithmProgrammers[2].sFirstName="Cees";
				  algorithmDetails[i].apAlgorithmProgrammers[2].sLastName="Zeelenberg";  
				//-------------------  
				  

				algorithmDetails[i].apWebServiceProgrammers = new People[1];
				  for(int p=0; p<1;p++){
					  algorithmDetails[i].apWebServiceProgrammers[p] = new People();
				  }
				  algorithmDetails[i].apWebServiceProgrammers[0].sEmail="mshipwa1@jhu.edu";
				  algorithmDetails[i].apWebServiceProgrammers[0].sFirstName="Michael";
				  algorithmDetails[i].apWebServiceProgrammers[0].sMiddleName="P.";
				  algorithmDetails[i].apWebServiceProgrammers[0].sLastName="Shipway";  
				  algorithmDetails[i].apWebServiceProgrammers[0].sOrganization="Johns Hopkins University";
				//-------------------  
					  

				algorithmDetails[i].aoAffiliatedOrgs = new Organization[3];
				  for(int p=0; p<3;p++){
					  algorithmDetails[i].aoAffiliatedOrgs[p] = new Organization();
				  }
				  algorithmDetails[i].aoAffiliatedOrgs[0].sURL="www.jhu.edu";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sName="Johns Hopkins University";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactEmail = "mshipwa1@jhu.edu";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactPhone = "(410) 516-5294";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactFirstName = "Michael";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactMiddleName = "P.";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactLastName = "Shipway";
	
				  
				  algorithmDetails[i].aoAffiliatedOrgs[1].sURL="www.cvrgrid.org";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sName="CardioVascular Research Grid (CVRG)";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactEmail = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactPhone = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactFirstName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactMiddleName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactLastName = "";
				//-------------------  
				  algorithmDetails[i].aoAffiliatedOrgs[2].sURL="http://www.physionet.org";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sName="PhysioNet";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactEmail = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactPhone = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactFirstName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactMiddleName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactLastName = "";
				//-------------------  
				
				break;
			case 2:
				algorithmDetails[i].sServiceMethod = "ann2rrWrapperType2";
				algorithmDetails[i].sDisplayShortName = "Detect RR Intervals";
				algorithmDetails[i].sAnalysisServiceURL = "http://128.220.76.170:8080/axis2/services/physionetAnalysisService";

				//-------------------------------------------------
				//-----------  ann2rr  -----------
				//-------------------------------------------------
				//details.iWebServiceID = 0;

				algorithmDetails[i].sServiceName = "physionetAnalysisService";
				//algorithmDetails[i].sServiceMethod = "ann2rrWrapperType2";
				algorithmDetails[i].sToolTipDescription = "Extracts a list of RR intervals, in text format, from an annotation file";
				algorithmDetails[i].sURLreference = "http://physionet.org/physiotools/wag/ann2rr-1.htm";
				algorithmDetails[i].sLongDescription = "Extracts a list of RR intervals, in text format, from an annotation file.  By default, the intervals are listed in units of sample intervals";
				algorithmDetails[i].sVersionIdWebService = "2.0";
				algorithmDetails[i].sDateWebService = "Dec 12, 2012";
				algorithmDetails[i].sLicence = "http://physionet.org/physiotools/wag/wag.htm";
	

				iParamCount = 11;
				algorithmDetails[i].aParameters =  new AdditionalParameters[iParamCount];
				  for(int p=0; p<iParamCount;p++){
					  algorithmDetails[i].aParameters[p] = new AdditionalParameters();
				  }
				  algorithmDetails[i].aParameters[0].sParameterFlag = "f";
				  algorithmDetails[i].aParameters[0].sDisplayShortName = "Starting Time(seconds)";
				  algorithmDetails[i].aParameters[0].sToolTipDescription = "Begin at specified time.";
				  algorithmDetails[i].aParameters[0].sLongDescription = "  Begin at the specified time. By default, ann2rr starts at the beginning of the record.";
				  algorithmDetails[i].aParameters[0].sParameterDefaultValue = "0";
				  algorithmDetails[i].aParameters[0].sType = "float";
				  algorithmDetails[i].aParameters[0].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[1].sParameterFlag = "t";
				  algorithmDetails[i].aParameters[1].sDisplayShortName = "Stop Time(seconds)";
				  algorithmDetails[i].aParameters[1].sToolTipDescription = "Stop at the specified time.";
				  algorithmDetails[i].aParameters[1].sLongDescription = "Stop at the specified time.";
				  algorithmDetails[i].aParameters[1].sParameterDefaultValue = "";
				  algorithmDetails[i].aParameters[1].sType = "float";
				  algorithmDetails[i].aParameters[1].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[2].sParameterFlag = "A";
				  algorithmDetails[i].aParameters[2].sDisplayShortName = "Print all intervals";
				  algorithmDetails[i].aParameters[2].sToolTipDescription = "Print all intervals between annotations.";
				  algorithmDetails[i].aParameters[2].sLongDescription = "    Print all intervals between annotations. By default, ann2rr prints only RR intervals (those between QRS (beat) annotations). This option overrides the -c and -p options.";
				  algorithmDetails[i].aParameters[2].sParameterDefaultValue = "";
				  algorithmDetails[i].aParameters[2].sType = "boolean";
				  algorithmDetails[i].aParameters[2].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[3].sParameterFlag = "c";
				  algorithmDetails[i].aParameters[3].sDisplayShortName = "Print consecutive valid intervals";
				  algorithmDetails[i].aParameters[3].sToolTipDescription = "Print intervals between consecutive valid annotations only.";
				  algorithmDetails[i].aParameters[3].sLongDescription = "    Print intervals between consecutive valid annotations only.";
				  algorithmDetails[i].aParameters[3].sParameterDefaultValue = "";
				  algorithmDetails[i].aParameters[3].sType = "boolean";
				  algorithmDetails[i].aParameters[3].bOptional = "true";
				  //-------------	
				  algorithmDetails[i].aParameters[4].sParameterFlag = "i";
				  algorithmDetails[i].aParameters[4].sDisplayShortName = "Specify Format";
				  algorithmDetails[i].aParameters[4].sToolTipDescription = "Print intervals in the specified format.";
				  algorithmDetails[i].aParameters[4].sLongDescription = "Print intervals in the specified format. By default, intervals are printed in units of sample intervals. Other formats include s (seconds), m (minutes), h (hours), and t (time interval in hh:mm:ss format). Formats s, m, and h may be followed by an integer between 0 and 15 inclusive, specifying the number of decimal places (default: 3). For example, use the option -is8 to obtain intervals in seconds with 8 decimal places.";
				  algorithmDetails[i].aParameters[4].sParameterDefaultValue = "";
				  algorithmDetails[i].aParameters[4].sType = "text";
				  algorithmDetails[i].aParameters[4].bOptional = "true";
				  //-------------		  
				  algorithmDetails[i].aParameters[5].sParameterFlag = "p";
				  algorithmDetails[i].aParameters[5].sDisplayShortName = "Ending Intervals for given type(s)";
				  algorithmDetails[i].aParameters[5].sToolTipDescription = "Print intervals ended by annotations of the specified types only.";
				  algorithmDetails[i].aParameters[5].sLongDescription = "Print intervals ended by annotations of the specified types only. The type arguments should be annotation mnemonics (e.g., N), as normally printed by rdann(1) in the third column. More than one -p option may be used in a single command, and each -p option may have more than one type argument following it. If type begins with ��-��, however, it must immediately follow -p (standard annotation mnemonics do not begin with ��-��, but modification labels in an annotation file may define such mnemonics).";
				  algorithmDetails[i].aParameters[5].sParameterDefaultValue = "";
				  algorithmDetails[i].aParameters[5].sType = "text";
				  algorithmDetails[i].aParameters[5].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[6].sParameterFlag = "P";
				  algorithmDetails[i].aParameters[6].sDisplayShortName = "Start Intervals for given type(s)";
				  algorithmDetails[i].aParameters[6].sToolTipDescription = "Print intervals begun by annotations of the specified types only.";
				  algorithmDetails[i].aParameters[6].sLongDescription = "Print intervals begun by annotations of the specified types only.";
				  algorithmDetails[i].aParameters[6].sParameterDefaultValue = "false";
				  algorithmDetails[i].aParameters[6].sType = "boolean";
				  algorithmDetails[i].aParameters[6].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[7].sParameterFlag = "v";
				  algorithmDetails[i].aParameters[7].sDisplayShortName = "Print Final Times";
				  algorithmDetails[i].aParameters[7].sToolTipDescription = "Print final times (the times of occurrence of the annotations that end each interval).";
				  algorithmDetails[i].aParameters[7].sLongDescription = "Print final times (the times of occurrence of the annotations that end each interval). This option accepts all of the formats defined for -i, as well as T (to print the date and time in [hh:mm:ss dd/mm/yyyy] if the starting time and date have been recorded in the header file for record). If this option is chosen, the times appear at the end of each line of output.";
				  algorithmDetails[i].aParameters[7].sParameterDefaultValue = "";
				  algorithmDetails[i].aParameters[7].sType = "text";
				  algorithmDetails[i].aParameters[7].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[8].sParameterFlag = "V";
				  algorithmDetails[i].aParameters[8].sDisplayShortName = "Print Starting times";
				  algorithmDetails[i].aParameters[8].sToolTipDescription = "Print initial times (the times of occurrence of the annotations that begin each interval).";
				  algorithmDetails[i].aParameters[8].sLongDescription = "Print initial times (the times of occurrence of the annotations that begin each interval). Any of the formats usable for the -v option may be used with -V. If this option is chosen, the times appear at the beginning of each line of output.";
				  algorithmDetails[i].aParameters[8].sParameterDefaultValue = "";
				  algorithmDetails[i].aParameters[8].sType = "text";
				  algorithmDetails[i].aParameters[8].bOptional = "true";
				  //------------
				  algorithmDetails[i].aParameters[9].sParameterFlag = "w";
				  algorithmDetails[i].aParameters[9].sDisplayShortName = "Final Annotations";
				  algorithmDetails[i].aParameters[9].sToolTipDescription = "Print final annotations (the types of the annotations that end each interval), immediately following the intervals in each line of output.";
				  algorithmDetails[i].aParameters[9].sLongDescription = "Print final annotations (the types (N, V, etc., as for -p above) of the annotations that end each interval), immediately following the intervals in each line of output.";
				  algorithmDetails[i].aParameters[9].sParameterDefaultValue = "false";
				  algorithmDetails[i].aParameters[9].sType = "boolean";
				  algorithmDetails[i].aParameters[9].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[10].sParameterFlag = "W";
				  algorithmDetails[i].aParameters[10].sDisplayShortName = "Starting Annotations";
				  algorithmDetails[i].aParameters[10].sToolTipDescription = "Print initial annotations (the types of the annotations that begin each interval), immediately before the interval in each line of output.";
				  algorithmDetails[i].aParameters[10].sLongDescription = "Stop at the specified time.";
				  algorithmDetails[i].aParameters[10].sParameterDefaultValue = "false";
				  algorithmDetails[i].aParameters[10].sType = "boolean";
				  algorithmDetails[i].aParameters[10].bOptional = "true";
				  //-------------
				  
 
				algorithmDetails[i].afInFileTypes = new FileTypes[1];
				  for(int f=0; f<1;f++){
					  algorithmDetails[i].afInFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afInFileTypes[0].sName="WFDBannotation";
				  algorithmDetails[i].afInFileTypes[0].sExtension="*";
				  algorithmDetails[i].afInFileTypes[0].sDisplayShortName = "WFDB annotation";
				

				algorithmDetails[i].afOutFileTypes = new FileTypes[1];
				  for(int f=0; f<1;f++){
					  algorithmDetails[i].afOutFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afOutFileTypes[0].sName="TabDelimitedText";
				  algorithmDetails[i].afOutFileTypes[0].sExtension = "txt";
				  algorithmDetails[i].afOutFileTypes[0].sDisplayShortName = "Tab Delimited Text";
				//-------------
	

				algorithmDetails[i].apAlgorithmProgrammers = new People[1];
				  for(int p=0; p<1;p++){
					  algorithmDetails[i].apAlgorithmProgrammers[p] = new People();
				  }
				  algorithmDetails[i].apAlgorithmProgrammers[0].sEmail="george@mit.edu";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sFirstName="George";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sMiddleName="B.";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sLastName="Moody";  
				  algorithmDetails[i].apAlgorithmProgrammers[0].sOrganization="Physionet";
				  
 
				algorithmDetails[i].apWebServiceProgrammers = new People[2];
				  for(int p=0; p<algorithmDetails[i].apWebServiceProgrammers.length;p++){
					  algorithmDetails[i].apWebServiceProgrammers[p] = new People();
				  }
				  algorithmDetails[i].apWebServiceProgrammers[0].sEmail="bbenite1@jhu.edu";
				  algorithmDetails[i].apWebServiceProgrammers[0].sFirstName="Brandon";
				  algorithmDetails[i].apWebServiceProgrammers[0].sMiddleName="M.";
				  algorithmDetails[i].apWebServiceProgrammers[0].sLastName="Benitez";  
				  algorithmDetails[i].apWebServiceProgrammers[0].sOrganization="Johns Hopkins University";
				//-------------------  
				  algorithmDetails[i].apWebServiceProgrammers[1].sEmail="mshipwa1@jhu.edu";
				  algorithmDetails[i].apWebServiceProgrammers[1].sFirstName="Michael";
				  algorithmDetails[i].apWebServiceProgrammers[1].sMiddleName="P.";
				  algorithmDetails[i].apWebServiceProgrammers[1].sLastName="Shipway";  
				  algorithmDetails[i].apWebServiceProgrammers[1].sOrganization="Johns Hopkins University";
				//=====================  
					  
				algorithmDetails[i].aoAffiliatedOrgs = new Organization[3];
				  for(int p=0; p<3;p++){
					  algorithmDetails[i].aoAffiliatedOrgs[p] = new Organization();
				  }
				  algorithmDetails[i].aoAffiliatedOrgs[0].sURL="www.jhu.edu";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sName="Johns Hopkins University";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactEmail = "bbenite1@jhu.edu";
				//-------------------  
				  algorithmDetails[i].aoAffiliatedOrgs[1].sURL="www.cvrgrid.org";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sName="CardioVascular Research Grid (CVRG)";
				//-------------------  
				  algorithmDetails[i].aoAffiliatedOrgs[2].sURL="http://www.physionet.org";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sName="PhysioNet";
				//-------------------  
	
				
				break;
			case 3:
				algorithmDetails[i].sServiceMethod = "pnnlistWrapperType2";
				algorithmDetails[i].sDisplayShortName = "Heart Rate Variability";
				algorithmDetails[i].sAnalysisServiceURL = "http://128.220.76.170:8080/axis2/services/physionetAnalysisService";
				
				//-------------------------------------------------
				//-----------  pNNx  -----------
				//-------------------------------------------------
				//algorithmDetails[i].iWebServiceID = 0;

				algorithmDetails[i].sServiceName = "physionetAnalysisService";
				//algorithmDetails[i].sServiceMethod = "pnnlistWrapperType2";
				algorithmDetails[i].sToolTipDescription = "Calculates time domain measures of heart rate variability";
				algorithmDetails[i].sURLreference = "http://physionet.org/physiotools/wag/pnnlis-1.htm";
				algorithmDetails[i].sLongDescription = "These programs derive pNNx, time domain measures of heart rate variability defined for any time interval x as the fraction of consecutive normal sinus (NN) intervals that differ by more than x." +
											"Conventionally, such measures have been applied to assess parasympathetic activity using x = 50 milliseconds (yielding the widely-cited pNN50 statistic). ";
				algorithmDetails[i].sVersionIdWebService = "2.0";
				algorithmDetails[i].sDateWebService = "Dec 12, 2012";
				algorithmDetails[i].sLicence = "http://physionet.org/physiotools/wag/wag.htm";
	
				algorithmDetails[i].aParameters =  new AdditionalParameters[5];
				  for(int p=0; p<5;p++){
					  algorithmDetails[i].aParameters[p] = new AdditionalParameters();
				  }
				  algorithmDetails[i].aParameters[0].sParameterFlag = "f";
				  algorithmDetails[i].aParameters[0].sDisplayShortName = "Start Time(seconds)";
				  algorithmDetails[i].aParameters[0].sToolTipDescription = "Begin at the specified time in record(seconds) (default: the beginning of record).  ";
				  algorithmDetails[i].aParameters[0].sLongDescription = "Begin at the specified time in record (default: the beginning of record).  ";
				  algorithmDetails[i].aParameters[0].sParameterDefaultValue = "0";
				  algorithmDetails[i].aParameters[0].sType = "float";
				  algorithmDetails[i].aParameters[0].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[1].sParameterFlag = "t";
				  algorithmDetails[i].aParameters[1].sDisplayShortName = "Stop Time(seconds)";
				  algorithmDetails[i].aParameters[1].sToolTipDescription = "Stop at the specified time.(seconds)";
				  algorithmDetails[i].aParameters[1].sLongDescription = "Stop at the specified time. ";
				  algorithmDetails[i].aParameters[1].sParameterDefaultValue = "-1";
				  algorithmDetails[i].aParameters[1].sType = "float";
				  algorithmDetails[i].aParameters[1].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[2].sParameterFlag = "i";
				  algorithmDetails[i].aParameters[2].sDisplayShortName = "Increment (milliseconds)";
				  algorithmDetails[i].aParameters[2].sToolTipDescription = "Compute and output pNNx for x = 0, inc, 2*inc, ... milliseconds.  ";
				  algorithmDetails[i].aParameters[2].sLongDescription = "Compute and output pNNx for x = 0, inc, 2*inc, ... milliseconds.  ";
				  algorithmDetails[i].aParameters[2].sParameterDefaultValue = "-1";
				  algorithmDetails[i].aParameters[2].sType = "integer";
				  algorithmDetails[i].aParameters[2].bOptional = "true";
				  //-------------		  
				  algorithmDetails[i].aParameters[3].sParameterFlag = "p";
				  algorithmDetails[i].aParameters[3].sDisplayShortName = "Increment (percentage)";
				  algorithmDetails[i].aParameters[3].sToolTipDescription = "Compute and output increments as percentage of initial intervals. ";
				  algorithmDetails[i].aParameters[3].sLongDescription = "Compute and output increments as percentage of initial intervals. ";
				  algorithmDetails[i].aParameters[3].sParameterDefaultValue = "false";
				  algorithmDetails[i].aParameters[3].sType = "boolean";
				  algorithmDetails[i].aParameters[3].bOptional = "true";
				  //-------------
				  algorithmDetails[i].aParameters[4].sParameterFlag = "s";
				  algorithmDetails[i].aParameters[4].sDisplayShortName = "Separate Distributions";
				  algorithmDetails[i].aParameters[4].sToolTipDescription = "Compute and output separate distributions of positive and negative intervals.  ";
				  algorithmDetails[i].aParameters[4].sLongDescription = "Compute and output separate distributions of positive and negative intervals.  ";
				  algorithmDetails[i].aParameters[4].sParameterDefaultValue = "false";
				  algorithmDetails[i].aParameters[4].sType = "boolean";
				  algorithmDetails[i].aParameters[4].bOptional = "true";
				  //-------------
				  

				algorithmDetails[i].afInFileTypes = new FileTypes[3];
				  for(int f=0; f<3;f++){
					  algorithmDetails[i].afInFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afInFileTypes[0].sName="WFDBheader";
				  algorithmDetails[i].afInFileTypes[0].sExtension="hea";
				  algorithmDetails[i].afInFileTypes[0].sDisplayShortName = "WFDB header";
				//-------------
				  algorithmDetails[i].afInFileTypes[1].sName="WFDBdata";
				  algorithmDetails[i].afInFileTypes[1].sExtension = "dat";
				  algorithmDetails[i].afInFileTypes[1].sDisplayShortName = "WFDB data";
				//-------------
				  algorithmDetails[i].afInFileTypes[2].sName="WFDBxyz";
				  algorithmDetails[i].afInFileTypes[2].sExtension = "xyz";
				  algorithmDetails[i].afInFileTypes[2].sDisplayShortName = "WFDB VCG xyz data";
				//-------------
				

				algorithmDetails[i].afOutFileTypes = new FileTypes[1];
				  for(int f=0; f<1;f++){
					  algorithmDetails[i].afOutFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afOutFileTypes[0].sName="TabDelimitedText";
				  algorithmDetails[i].afOutFileTypes[0].sExtension = "txt";
				  algorithmDetails[i].afOutFileTypes[0].sDisplayShortName = "Tab Delimited Text";
				//-------------
	

				algorithmDetails[i].apAlgorithmProgrammers = new People[1];
				  for(int p=0; p<1;p++){
					  algorithmDetails[i].apAlgorithmProgrammers[p] = new People();
				  }
				  algorithmDetails[i].apAlgorithmProgrammers[0].sEmail="george@mit.edu";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sFirstName="George";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sMiddleName="B.";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sLastName="Moody";  
				  algorithmDetails[i].apAlgorithmProgrammers[0].sOrganization="Physionet";
				  

				algorithmDetails[i].apWebServiceProgrammers = new People[1];
				  for(int p=0; p<1;p++){
					  algorithmDetails[i].apWebServiceProgrammers[p] = new People();
				  }
				  algorithmDetails[i].apWebServiceProgrammers[0].sEmail="bbenite1@jhu.edu";
				  algorithmDetails[i].apWebServiceProgrammers[0].sFirstName="Brandon";
				  algorithmDetails[i].apWebServiceProgrammers[0].sMiddleName="M.";
				  algorithmDetails[i].apWebServiceProgrammers[0].sLastName="Benitez";  
				  algorithmDetails[i].apWebServiceProgrammers[0].sOrganization="Johns Hopkins University";
				//-------------------  
					  

				algorithmDetails[i].aoAffiliatedOrgs = new Organization[3];
				  for(int p=0; p<3;p++){
					  algorithmDetails[i].aoAffiliatedOrgs[p] = new Organization();
				  }
				  algorithmDetails[i].aoAffiliatedOrgs[0].sURL="www.jhu.edu";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sName="Johns Hopkins University";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactEmail = "bbenite1@jhu.edu";
				//-------------------  
				  algorithmDetails[i].aoAffiliatedOrgs[1].sURL="www.cvrgrid.org";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sName="CardioVascular Research Grid (CVRG)";
				//-------------------  
				  algorithmDetails[i].aoAffiliatedOrgs[2].sURL="http://www.physionet.org";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sName="PhysioNet";
				//-------------------  
				
				break;
			case 4:
				algorithmDetails[i].sServiceMethod = "nguessWrapperType2";
				algorithmDetails[i].sDisplayShortName = "Locate Missing Beats";
				algorithmDetails[i].sAnalysisServiceURL = "http://128.220.76.170:8080/axis2/services/physionetAnalysisService";
				
				//-------------------------------------------------
				//-----------  nguess  -----------
				//-------------------------------------------------
				//details.iWebServiceID = 0;

				algorithmDetails[i].sServiceName = "physionetAnalysisService";
				//algorithmDetails[i].sServiceMethod = "nguessWrapperType2";
				algorithmDetails[i].sToolTipDescription = "guesses the times of missing normal beats in an annotation file.";
				algorithmDetails[i].sURLreference = "http://physionet.org/physiotools/wag/nguess-1.htm";
				algorithmDetails[i].sLongDescription = "This program copies its input (a WFDB annotation file containing beat annotations), removing annotations of events other than sinus beats, and interpolating additional Q (unknown beat) annotations at times when sinus beats are expected. " +
				"Intervals between sinus beats are predicted using a predictor array as described by Paul Schluter ('The design and evaluation of a bedside cardiac arrhythmia monitor'; Ph.D. thesis, MIT Dept. of Electrical Engineering, 1981)." +
				" When the predictions are inconsistent with the known sinus beats, as may occur in extreme noise or in highly irregular rhythms such as atrial fibrillation, no interpolations are made. ";
				algorithmDetails[i].sVersionIdWebService = "2.0";
				algorithmDetails[i].sDateWebService = "Dec 12, 2012";
				algorithmDetails[i].sLicence = "http://physionet.org/physiotools/wag/wag.htm";
	

				iParamCount = 4;
				algorithmDetails[i].aParameters =  new AdditionalParameters[iParamCount];
				for(int p=0; p<iParamCount;p++){
					algorithmDetails[i].aParameters[p] = new AdditionalParameters();
				}
				algorithmDetails[i].aParameters[0].sParameterFlag = "f";
				algorithmDetails[i].aParameters[0].sDisplayShortName = "Start Time(seconds)";
				algorithmDetails[i].aParameters[0].sToolTipDescription = "Begin at the specified time in record(seconds) (default: the beginning of record).  ";
				algorithmDetails[i].aParameters[0].sLongDescription = "Begin at the specified time in record (default: the beginning of record).  ";
				algorithmDetails[i].aParameters[0].sParameterDefaultValue = "0";
				algorithmDetails[i].aParameters[0].sType = "float";
				algorithmDetails[i].aParameters[0].bOptional = "true";
				//-------------		  
				algorithmDetails[i].aParameters[1].sParameterFlag = "t";
				algorithmDetails[i].aParameters[1].sDisplayShortName = "Stop Time(seconds)";
				algorithmDetails[i].aParameters[1].sToolTipDescription = "Stop at the specified time.(seconds) ";
				algorithmDetails[i].aParameters[1].sLongDescription = "Stop at the specified time. ";
				algorithmDetails[i].aParameters[1].sParameterDefaultValue = "";
				algorithmDetails[i].aParameters[1].sType = "float";
				algorithmDetails[i].aParameters[1].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[2].sParameterFlag = "m";
				algorithmDetails[i].aParameters[2].sDisplayShortName = "Predicted RR interval";
				algorithmDetails[i].aParameters[2].sToolTipDescription = "Insert Q annotations in the output at the inferred locations of sinus beats only when the input RR interval exceeds M times the predicted RR interval (default: M = 1.75). ";
				algorithmDetails[i].aParameters[2].sLongDescription = "Insert Q annotations in the output at the inferred locations of sinus beats only when the input RR interval exceeds M times the predicted RR interval (default: M = 1.75). M must be greater than 1; its useful range is roughly 1.5 to 2. ";
				algorithmDetails[i].aParameters[2].sParameterDefaultValue = "1.75";
				algorithmDetails[i].aParameters[2].sType = "float";
				algorithmDetails[i].aParameters[2].bOptional = "true";
				//-------------		  
				algorithmDetails[i].aParameters[3].sParameterFlag = "o";
				algorithmDetails[i].aParameters[3].sDisplayShortName = "Output Annotator Name";
				algorithmDetails[i].aParameters[3].sToolTipDescription = "Use this extension for the result file. ";
				algorithmDetails[i].aParameters[3].sLongDescription = "Write output to the annotation file with it's extention specified by output-annotator (default: nguess). ";
				algorithmDetails[i].aParameters[3].sParameterDefaultValue = "";
				algorithmDetails[i].aParameters[3].sType = "text";
				algorithmDetails[i].aParameters[3].bOptional = "true";
				//-------------
	

				algorithmDetails[i].afInFileTypes = new FileTypes[1];
				  for(int f=0; f<1;f++){
					  algorithmDetails[i].afInFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afInFileTypes[0].sName="WFDBannotation";
				  algorithmDetails[i].afInFileTypes[0].sExtension="*";
				  algorithmDetails[i].afInFileTypes[0].sDisplayShortName = "WFDB annotation";
				//-------------
	

				algorithmDetails[i].afOutFileTypes = new FileTypes[1];
				for(int f=0; f<1;f++){
					algorithmDetails[i].afOutFileTypes[f] = new FileTypes();
				}
				algorithmDetails[i].afOutFileTypes[0].sName="TabDelimitedText";
				algorithmDetails[i].afOutFileTypes[0].sExtension = "txt";
				algorithmDetails[i].afOutFileTypes[0].sDisplayShortName = "Tab Delimited Text";
				//-------------
	

				algorithmDetails[i].apAlgorithmProgrammers = new People[2];
				for(int p=0; p<2;p++){
					algorithmDetails[i].apAlgorithmProgrammers[p] = new People();
				}
				algorithmDetails[i].apAlgorithmProgrammers[0].sEmail="george@mit.edu";
				algorithmDetails[i].apAlgorithmProgrammers[0].sFirstName="George";
				algorithmDetails[i].apAlgorithmProgrammers[0].sMiddleName="B.";
				algorithmDetails[i].apAlgorithmProgrammers[0].sLastName="Moody";  
				algorithmDetails[i].apAlgorithmProgrammers[0].sOrganization="Physionet";
				//-----------------------------------------
				algorithmDetails[i].apAlgorithmProgrammers[1].sEmail="";
				algorithmDetails[i].apAlgorithmProgrammers[1].sFirstName="Paul";
				algorithmDetails[i].apAlgorithmProgrammers[1].sMiddleName="";
				algorithmDetails[i].apAlgorithmProgrammers[1].sLastName="Schluter ";  
				algorithmDetails[i].apAlgorithmProgrammers[1].sOrganization="MIT Dept. of Electrical Engineering, 1981";
	

				algorithmDetails[i].apWebServiceProgrammers = new People[2];
				for(int p=0; p<2;p++){
					algorithmDetails[i].apWebServiceProgrammers[p] = new People();
				}
				algorithmDetails[i].apWebServiceProgrammers[0].sEmail="bbenite1@jhu.edu";
				algorithmDetails[i].apWebServiceProgrammers[0].sFirstName="Brandon";
				algorithmDetails[i].apWebServiceProgrammers[0].sMiddleName="M.";
				algorithmDetails[i].apWebServiceProgrammers[0].sLastName="Benitez";  
				algorithmDetails[i].apWebServiceProgrammers[0].sOrganization="Johns Hopkins University";
				//-------------------  
				algorithmDetails[i].apWebServiceProgrammers[1].sEmail="mshipwa1@jhu.edu";
				algorithmDetails[i].apWebServiceProgrammers[1].sFirstName="Michael";
				algorithmDetails[i].apWebServiceProgrammers[1].sMiddleName="P.";
				algorithmDetails[i].apWebServiceProgrammers[1].sLastName="Shipway";  
				algorithmDetails[i].apWebServiceProgrammers[1].sOrganization="Johns Hopkins University";
				//=====================  
	

				algorithmDetails[i].aoAffiliatedOrgs = new Organization[3];
				for(int p=0; p<3;p++){
					algorithmDetails[i].aoAffiliatedOrgs[p] = new Organization();
				}
				algorithmDetails[i].aoAffiliatedOrgs[0].sURL="www.jhu.edu";
				algorithmDetails[i].aoAffiliatedOrgs[0].sName="Johns Hopkins University";
				algorithmDetails[i].aoAffiliatedOrgs[0].sContactEmail = "bbenite1@jhu.edu";
				//-------------------  
				algorithmDetails[i].aoAffiliatedOrgs[1].sURL="www.cvrgrid.org";
				algorithmDetails[i].aoAffiliatedOrgs[1].sName="CardioVascular Research Grid (CVRG)";
				//-------------------  
				algorithmDetails[i].aoAffiliatedOrgs[2].sURL="http://www.physionet.org";
				algorithmDetails[i].aoAffiliatedOrgs[2].sName="PhysioNet";
				//-------------------  
				break;

			case 5:
				algorithmDetails[i].sServiceMethod = "sigampWrapperType2";
				algorithmDetails[i].sDisplayShortName = "QRS Peak Amplitude";
				algorithmDetails[i].sAnalysisServiceURL = "http://128.220.76.170:8080/axis2/services/physionetAnalysisService";

				//-------------------------------------------------
				//-----------  sigamp  -----------
				//-------------------------------------------------
				//details.iWebServiceID = 0;

				algorithmDetails[i].sServiceName = "physionetAnalysisService";
				//algorithmDetails[i].sServiceMethod = "sigampWrapperType2";
				algorithmDetails[i].sToolTipDescription = "Measure signal amplitudes of a WFDB record.";
				algorithmDetails[i].sURLreference = "http://www.physionet.org/physiotools/wag/sigamp-1.htm";
				algorithmDetails[i].sLongDescription = "sigamp measures either baseline-corrected RMS amplitudes or (for suitably annotated ECG signals)  \n"
					+ " normal QRS peak-to-peak amplitudes for all signals of the specified record.  \n"
					+ " It makes up to 300 measurements (but see -n below) for each signal and calculates trimmed means  \n"
					+ " (by discarding the largest and smallest 5% of the measurements and taking the mean of the remaining 90%). \n";
				algorithmDetails[i].sVersionIdWebService = "2.0";
				algorithmDetails[i].sDateWebService = "Dec 12, 2012";
				algorithmDetails[i].sLicence = "http://physionet.org/physiotools/wag/wag.htm";
 
				algorithmDetails[i].aParameters =  new AdditionalParameters[17];
				for(int p=0; p<17;p++){
					algorithmDetails[i].aParameters[p] = new AdditionalParameters();
				}
				algorithmDetails[i].aParameters[0].sParameterFlag = "f";
				algorithmDetails[i].aParameters[0].sDisplayShortName = "Begin time (sec)";
				algorithmDetails[i].aParameters[0].sToolTipDescription = "Begin at the specified time in record (default: the beginning of record).";
				algorithmDetails[i].aParameters[0].sLongDescription = "Begin at the specified time in record (default: the beginning of record).";
				algorithmDetails[i].aParameters[0].sParameterDefaultValue = "0";
				algorithmDetails[i].aParameters[0].sType = "float";
				algorithmDetails[i].aParameters[0].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[1].sParameterFlag = "t";
				algorithmDetails[i].aParameters[1].sDisplayShortName = "End time (sec)";
				algorithmDetails[i].aParameters[1].sToolTipDescription = "Process until the specified time in record (default: the end of the record). ";
				algorithmDetails[i].aParameters[1].sLongDescription = "Process until the specified time in record (default: the end of the record). ";
				algorithmDetails[i].aParameters[1].sParameterDefaultValue = "-1";
				algorithmDetails[i].aParameters[1].sType = "float";
				algorithmDetails[i].aParameters[1].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[2].sParameterFlag = "H";
				algorithmDetails[i].aParameters[2].sDisplayShortName = "High Res";
				algorithmDetails[i].aParameters[2].sToolTipDescription = "Read the signal files in high-resolution mode (default: standard mode).";
				algorithmDetails[i].aParameters[2].sLongDescription = "Read the signal files in high-resolution mode (default: standard mode). These modes are identical for ordinary records. For multifrequency records, the standard decimation of oversampled signals to the frame rate is suppressed in high-resolution mode (rather, all other signals are resampled at the highest sampling frequency).";
				algorithmDetails[i].aParameters[2].sParameterDefaultValue = "false";
				algorithmDetails[i].aParameters[2].sType = "boolean";
				algorithmDetails[i].aParameters[2].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[3].sParameterFlag = "a";
				algorithmDetails[i].aParameters[3].sDisplayShortName = "annotator";
				algorithmDetails[i].aParameters[3].sToolTipDescription = "Measure QRS peak-to-peak amplitudes based on normal QRS annotations from the specified annotator. The suffix (extension), e.g. 'atr'.";
				algorithmDetails[i].aParameters[3].sLongDescription = "Measure QRS peak-to-peak amplitudes based on normal QRS annotations from the specified annotator. Where this appears, substitute an annotator name. Annotator names are not file names! The suffix (extension) of the name of an annotation file is the annotator name for that file; so, for example, the annotator name for `e0104.atr' is `atr'. The special annotator name `atr' is used to name the set of reference annotations supplied by the database developers. Other annotation sets have annotator names that may contain letters, digits, and underscores, as for record names. ";
				algorithmDetails[i].aParameters[3].sParameterDefaultValue = "";
				algorithmDetails[i].aParameters[3].sType = "text";
				algorithmDetails[i].aParameters[3].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[4].sParameterFlag = "dt1";
				algorithmDetails[i].aParameters[4].sDisplayShortName = "measurement window start delta";
				algorithmDetails[i].aParameters[4].sToolTipDescription = "Set the measurement window relative to QRS annotations.";
				algorithmDetails[i].aParameters[4].sLongDescription = "Set the measurement window start point relative to QRS annotations. Defaults: 0.05 (seconds before the annotation). ";
				algorithmDetails[i].aParameters[4].sParameterDefaultValue = "0.05";
				algorithmDetails[i].aParameters[4].sType = "float";
				algorithmDetails[i].aParameters[4].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[5].sParameterFlag = "dt2";
				algorithmDetails[i].aParameters[5].sDisplayShortName = "measurement window end delta";
				algorithmDetails[i].aParameters[5].sToolTipDescription = "Set the measurement window relative to QRS annotations.";
				algorithmDetails[i].aParameters[5].sLongDescription = "Set the measurement window end point relative to QRS annotations. Defaults: 0.05 (seconds after the annotation). ";
				algorithmDetails[i].aParameters[5].sParameterDefaultValue = "0.05";
				algorithmDetails[i].aParameters[5].sType = "float";
				algorithmDetails[i].aParameters[5].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[6].sParameterFlag = "w";
				algorithmDetails[i].aParameters[6].sDisplayShortName = "Set RMS amplitude measurement window.";
				algorithmDetails[i].aParameters[6].sToolTipDescription = "Set RMS amplitude measurement window.";
				algorithmDetails[i].aParameters[6].sLongDescription = "Set RMS amplitude measurement window. Default: dtw = 1 (second)";
				algorithmDetails[i].aParameters[6].sParameterDefaultValue = "1";
				algorithmDetails[i].aParameters[6].sType = "float";
				algorithmDetails[i].aParameters[6].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[7].sParameterFlag = "n";
				algorithmDetails[i].aParameters[7].sDisplayShortName = "Measurement count";
				algorithmDetails[i].aParameters[7].sToolTipDescription = "Make up to nmax measurements on each signal (default: 300).";
				algorithmDetails[i].aParameters[7].sLongDescription = "Make up to nmax measurements on each signal (default: 300). ";
				algorithmDetails[i].aParameters[7].sParameterDefaultValue = "300";
				algorithmDetails[i].aParameters[7].sType = "integer";
				algorithmDetails[i].aParameters[7].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[8].sParameterFlag = "p";
				algorithmDetails[i].aParameters[8].sDisplayShortName = "Print results in physical units.";
				algorithmDetails[i].aParameters[8].sToolTipDescription = "Print results in physical units (default: ADC units).";
				algorithmDetails[i].aParameters[8].sLongDescription = "Return physical units(default: ADC units) + elapsed time in seconds(same as -ps). (used with -q and -v when printing individual measurements);";
				algorithmDetails[i].aParameters[8].sParameterDefaultValue = "false";
				algorithmDetails[i].aParameters[8].sType = "boolean";
				algorithmDetails[i].aParameters[8].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[9].sParameterFlag = "pd";
				algorithmDetails[i].aParameters[9].sDisplayShortName = "time of day and date";
				algorithmDetails[i].aParameters[9].sToolTipDescription = "Process until the specified time in record (default: the end of the record). ";
				algorithmDetails[i].aParameters[9].sLongDescription = "Return physical units + time of day and date if known";
				algorithmDetails[i].aParameters[9].sParameterDefaultValue = "false";
				algorithmDetails[i].aParameters[9].sType = "boolean";
				algorithmDetails[i].aParameters[9].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[10].sParameterFlag = "pe";
				algorithmDetails[i].aParameters[10].sDisplayShortName = "Elapsed time.";
				algorithmDetails[i].aParameters[10].sToolTipDescription = "Elapsed time in hours, minutes, and seconds";
				algorithmDetails[i].aParameters[10].sLongDescription = "Return physical units + elapsed time as <hours>:<minutes>:<seconds>";
				algorithmDetails[i].aParameters[10].sParameterDefaultValue = "false";
				algorithmDetails[i].aParameters[10].sType = "boolean";
				algorithmDetails[i].aParameters[10].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[11].sParameterFlag = "ph";
				algorithmDetails[i].aParameters[11].sDisplayShortName = "Hours";
				algorithmDetails[i].aParameters[11].sToolTipDescription = "Elapsed time in hours.";
				algorithmDetails[i].aParameters[11].sLongDescription = "Return physical units + elapsed time in hours.";
				algorithmDetails[i].aParameters[11].sParameterDefaultValue = "false";
				algorithmDetails[i].aParameters[11].sType = "boolean";
				algorithmDetails[i].aParameters[11].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[12].sParameterFlag = "pm";
				algorithmDetails[i].aParameters[12].sDisplayShortName = "Minutes";
				algorithmDetails[i].aParameters[12].sToolTipDescription = "Elapsed time in minutes.";
				algorithmDetails[i].aParameters[12].sLongDescription = "Return physical units + elapsed time in minute";
				algorithmDetails[i].aParameters[12].sParameterDefaultValue = "false";
				algorithmDetails[i].aParameters[12].sType = "boolean";
				algorithmDetails[i].aParameters[12].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[13].sParameterFlag = "ps";
				algorithmDetails[i].aParameters[13].sDisplayShortName = "Seconds";
				algorithmDetails[i].aParameters[13].sToolTipDescription = "Elapsed time in seconds (default).";
				algorithmDetails[i].aParameters[13].sLongDescription = "Return physical units + elapsed time in seconds(default, same as -p)";
				algorithmDetails[i].aParameters[13].sParameterDefaultValue = "false";
				algorithmDetails[i].aParameters[13].sType = "boolean";
				algorithmDetails[i].aParameters[13].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[14].sParameterFlag = "pS";
				algorithmDetails[i].aParameters[14].sDisplayShortName = "Samples";
				algorithmDetails[i].aParameters[14].sToolTipDescription = "Elapsed time in sample intervals.";
				algorithmDetails[i].aParameters[14].sLongDescription = "Return physical units + elapsed time in sample intervals.";
				algorithmDetails[i].aParameters[14].sParameterDefaultValue = "false";
				algorithmDetails[i].aParameters[14].sType = "boolean";
				algorithmDetails[i].aParameters[14].bOptional = "true";
				//------------
				algorithmDetails[i].aParameters[15].sParameterFlag = "q";
				algorithmDetails[i].aParameters[15].sDisplayShortName = "Quick mode.";
				algorithmDetails[i].aParameters[15].sToolTipDescription = "Quick mode: print individual measurements only. ";
				algorithmDetails[i].aParameters[15].sLongDescription = "Quick mode: print individual measurements only, not trimmed means.";
				algorithmDetails[i].aParameters[15].sParameterDefaultValue = "false";
				algorithmDetails[i].aParameters[15].sType = "boolean";
				algorithmDetails[i].aParameters[15].bOptional = "true";
				//-------------
				algorithmDetails[i].aParameters[16].sParameterFlag = "v";
				algorithmDetails[i].aParameters[16].sDisplayShortName = "Verbose mode";
				algorithmDetails[i].aParameters[16].sToolTipDescription = "Verbose mode: print individual measurements as well as trimmed means. ";
				algorithmDetails[i].aParameters[16].sLongDescription = "Verbose mode: print individual measurements as well as trimmed means.";
				algorithmDetails[i].aParameters[16].sParameterDefaultValue = "false";
				algorithmDetails[i].aParameters[16].sType = "boolean";
				algorithmDetails[i].aParameters[16].bOptional = "true";
				//-------------
	

				algorithmDetails[i].afInFileTypes = new FileTypes[3];
				for(int f=0; f<3;f++){
					algorithmDetails[i].afInFileTypes[f] = new FileTypes();
				}
				algorithmDetails[i].afInFileTypes[0].sName="WFDBheader";
				algorithmDetails[i].afInFileTypes[0].sExtension="hea";
				algorithmDetails[i].afInFileTypes[0].sDisplayShortName = "WFDB header";
				//-------------
				algorithmDetails[i].afInFileTypes[1].sName="WFDBdata";
				algorithmDetails[i].afInFileTypes[1].sExtension = "dat";
				algorithmDetails[i].afInFileTypes[1].sDisplayShortName = "WFDB data";
				//-------------
				algorithmDetails[i].afInFileTypes[2].sName="WFDBxyz";
				algorithmDetails[i].afInFileTypes[2].sExtension = "xyz";
				algorithmDetails[i].afInFileTypes[2].sDisplayShortName = "WFDB VCG xyz data";
				//-------------

				algorithmDetails[i].afOutFileTypes = new FileTypes[1];
				for(int f=0; f<1;f++){
					algorithmDetails[i].afOutFileTypes[f] = new FileTypes();
				}
				algorithmDetails[i].afOutFileTypes[0].sName="TabDelimitedText";
				algorithmDetails[i].afOutFileTypes[0].sExtension = "txt";
				algorithmDetails[i].afOutFileTypes[0].sDisplayShortName = "Tab Delimited Text";
				//-------------
	

				algorithmDetails[i].apAlgorithmProgrammers = new People[1];
				for(int p=0; p<1;p++){
					algorithmDetails[i].apAlgorithmProgrammers[p] = new People();
				}
				algorithmDetails[i].apAlgorithmProgrammers[0].sEmail="george@mit.edu";
				algorithmDetails[i].apAlgorithmProgrammers[0].sFirstName="George";
				algorithmDetails[i].apAlgorithmProgrammers[0].sMiddleName="B.";
				algorithmDetails[i].apAlgorithmProgrammers[0].sLastName="Moody";  
				algorithmDetails[i].apAlgorithmProgrammers[0].sOrganization="Physionet";
	

				algorithmDetails[i].apWebServiceProgrammers = new People[1];
				for(int p=0; p<1;p++){
					algorithmDetails[i].apWebServiceProgrammers[p] = new People();
				}
				algorithmDetails[i].apWebServiceProgrammers[0].sEmail="mshipwa1@jhu.edu";
				algorithmDetails[i].apWebServiceProgrammers[0].sFirstName="Michael";
				algorithmDetails[i].apWebServiceProgrammers[0].sMiddleName="P.";
				algorithmDetails[i].apWebServiceProgrammers[0].sLastName="Shipway";  
				algorithmDetails[i].apWebServiceProgrammers[0].sOrganization="Johns Hopkins University";
				//-------------------  
	

				algorithmDetails[i].aoAffiliatedOrgs = new Organization[3];
				for(int p=0; p<3;p++){
					algorithmDetails[i].aoAffiliatedOrgs[p] = new Organization();
				}
				algorithmDetails[i].aoAffiliatedOrgs[0].sURL="www.jhu.edu";
				algorithmDetails[i].aoAffiliatedOrgs[0].sName="Johns Hopkins University";
				algorithmDetails[i].aoAffiliatedOrgs[0].sContactEmail = "mshipwa1@jhu.edu";
				//-------------------  
				algorithmDetails[i].aoAffiliatedOrgs[1].sURL="www.cvrgrid.org";
				algorithmDetails[i].aoAffiliatedOrgs[1].sName="CardioVascular Research Grid (CVRG)";
				//-------------------  
				algorithmDetails[i].aoAffiliatedOrgs[2].sURL="http://www.physionet.org";
				algorithmDetails[i].aoAffiliatedOrgs[2].sName="PhysioNet";
				//------------------- 
				
				break;
			case 6:
  
//				algorithmDetails[i].sServiceMethod = "chesnokovV1WrapperType1";
				algorithmDetails[i].sServiceMethod = "chesnokovWrapperType2";
				algorithmDetails[i].sDisplayShortName = "QT Screening";
//				algorithmDetails[i].sAnalysisServiceURL = "http://128.220.76.170:8080/axis2/services/physionetChallengeServices";
				algorithmDetails[i].sAnalysisServiceURL = "http://128.220.76.170:8080/axis2/services/physionetAnalysisService";

				//-------------------------------------------------
				//-----------  sigamp  -----------
				//-------------------------------------------------
				//details.iWebServiceID = 0;
				algorithmDetails[i].sServiceName = "physionetChallengeServices";
				//algorithmDetails[i].sServiceMethod = "sigampWrapperType2";
				algorithmDetails[i].sToolTipDescription = "Measure signal amplitudes of a WFDB record.";
				algorithmDetails[i].sURLreference = "http://www.physionet.org/physiotools/wag/sigamp-1.htm";
				algorithmDetails[i].sLongDescription = "sigamp measures either baseline-corrected RMS amplitudes or (for suitably annotated ECG signals)  \n"
					+ " normal QRS peak-to-peak amplitudes for all signals of the specified record.  \n"
					+ " It makes up to 300 measurements (but see -n below) for each signal and calculates trimmed means  \n"
					+ " (by discarding the largest and smallest 5% of the measurements and taking the mean of the remaining 90%). \n";
				algorithmDetails[i].sVersionIdWebService = "2.0";
				algorithmDetails[i].sDateWebService = "Dec 12, 2012";
				algorithmDetails[i].sLicence = "http://physionet.org/physiotools/wag/wag.htm";
	

				algorithmDetails[i].aParameters =  new AdditionalParameters[1];
				for(int p=0; p<algorithmDetails[i].aParameters.length;p++){
					algorithmDetails[i].aParameters[p] = new AdditionalParameters();
				}
				algorithmDetails[i].aParameters[0].sParameterFlag = "v";
				algorithmDetails[i].aParameters[0].sDisplayShortName = "Verbose mode";
				algorithmDetails[i].aParameters[0].sToolTipDescription = "Verbose mode: print individual measurements as well as trimmed means. ";
				algorithmDetails[i].aParameters[0].sLongDescription = "Verbose mode: print individual measurements as well as trimmed means.";
				algorithmDetails[i].aParameters[0].sParameterDefaultValue = "false";
				algorithmDetails[i].aParameters[0].sType = "boolean";
				algorithmDetails[i].aParameters[0].bOptional = "true";
				//-------------
	

				algorithmDetails[i].afInFileTypes = new FileTypes[3];
				for(int f=0; f<3;f++){
					algorithmDetails[i].afInFileTypes[f] = new FileTypes();
				}
				algorithmDetails[i].afInFileTypes[0].sName="WFDBheader";
				algorithmDetails[i].afInFileTypes[0].sExtension="hea";
				algorithmDetails[i].afInFileTypes[0].sDisplayShortName = "WFDB header";
				//-------------
				algorithmDetails[i].afInFileTypes[1].sName="WFDBdata";
				algorithmDetails[i].afInFileTypes[1].sExtension = "dat";
				algorithmDetails[i].afInFileTypes[1].sDisplayShortName = "WFDB data";
				//-------------
				algorithmDetails[i].afInFileTypes[2].sName="WFDBxyz";
				algorithmDetails[i].afInFileTypes[2].sExtension = "xyz";
				algorithmDetails[i].afInFileTypes[2].sDisplayShortName = "WFDB VCG xyz data";
				//-------------
	

				algorithmDetails[i].afOutFileTypes = new FileTypes[1];
				for(int f=0; f<1;f++){
					algorithmDetails[i].afOutFileTypes[f] = new FileTypes();
				}
				algorithmDetails[i].afOutFileTypes[0].sName="TabDelimitedText";
				algorithmDetails[i].afOutFileTypes[0].sExtension = "txt";
				algorithmDetails[i].afOutFileTypes[0].sDisplayShortName = "Tab Delimited Text";
				//-------------
	

				algorithmDetails[i].apAlgorithmProgrammers = new People[1];
				for(int p=0; p<1;p++){
					algorithmDetails[i].apAlgorithmProgrammers[p] = new People();
				}
				algorithmDetails[i].apAlgorithmProgrammers[0].sEmail="george@mit.edu";
				algorithmDetails[i].apAlgorithmProgrammers[0].sFirstName="George";
				algorithmDetails[i].apAlgorithmProgrammers[0].sMiddleName="B.";
				algorithmDetails[i].apAlgorithmProgrammers[0].sLastName="Moody";  
				algorithmDetails[i].apAlgorithmProgrammers[0].sOrganization="Physionet";
	

				algorithmDetails[i].apWebServiceProgrammers = new People[1];
				for(int p=0; p<1;p++){
					algorithmDetails[i].apWebServiceProgrammers[p] = new People();
				}
				algorithmDetails[i].apWebServiceProgrammers[0].sEmail="mshipwa1@jhu.edu";
				algorithmDetails[i].apWebServiceProgrammers[0].sFirstName="Michael";
				algorithmDetails[i].apWebServiceProgrammers[0].sMiddleName="P.";
				algorithmDetails[i].apWebServiceProgrammers[0].sLastName="Shipway";  
				algorithmDetails[i].apWebServiceProgrammers[0].sOrganization="Johns Hopkins University";
				//-------------------  
	

				algorithmDetails[i].aoAffiliatedOrgs = new Organization[3];
				for(int p=0; p<3;p++){
					algorithmDetails[i].aoAffiliatedOrgs[p] = new Organization();
				}
				algorithmDetails[i].aoAffiliatedOrgs[0].sURL="www.jhu.edu";
				algorithmDetails[i].aoAffiliatedOrgs[0].sName="Johns Hopkins University";
				algorithmDetails[i].aoAffiliatedOrgs[0].sContactEmail = "mshipwa1@jhu.edu";
				//-------------------  
				algorithmDetails[i].aoAffiliatedOrgs[1].sURL="www.cvrgrid.org";
				algorithmDetails[i].aoAffiliatedOrgs[1].sName="CardioVascular Research Grid (CVRG)";
				//-------------------  
				algorithmDetails[i].aoAffiliatedOrgs[2].sURL="http://www.physionet.org";
				algorithmDetails[i].aoAffiliatedOrgs[2].sName="PhysioNet";
				//------------------- 
				break;

			case 8:

				
				// most of these details were copied over from the physionet analysis service descriptions
				algorithmDetails[i].sServiceMethod = "ssQRS_ScoreWrapperType2";
				algorithmDetails[i].sDisplayShortName = "Strauss-Sylvester score";
				algorithmDetails[i].sAnalysisServiceURL = "http://128.220.76.170:8080/axis2/services/physionetAnalysisService";

				algorithmDetails[i].sServiceName = "physionetAnalysisService";
				algorithmDetails[i].sToolTipDescription = "single-channel QRS detector based on length transform.";
				algorithmDetails[i].sURLreference = "http://www.physionet.org/physiotools/wag/ssQRS_Score-1.htm";
				algorithmDetails[i].sLongDescription = "The Strauss-Sylvester score attempts to locate QRS complexes in an ECG signal in the specified record. The detector algorithm is based on the length transform. The output of ssQRS_Score is an annotation file (with annotator name ssQRS_Score) in which all detected beats are labelled normal; the annotation file will also contain optional J-point annotations if the -j option (see below) is used. \n"
					+ " ssQRS_Score can process records containing any number of signals, but it uses only one signal for QRS detection (signal 0 by default; this can be changed using the -s option, see below). ssQRS_Score is optimized for use with adult human ECGs. For other ECGs, it may be necessary to experiment with the sampling frequency as recorded in the input record�s header file (see header(5) ), the detector threshold (which can be set using the -m option), and the time constants indicated in the source file. \n"
					+ " ssQRS_Score optionally uses the WFDB library�s setifreq function to resample the input signal at 120 or 150 Hz (depending on the mains frequency, which can be specified using the -p option). ssQRS_Score performs well using input sampled at a range of rates up to 360 Hz and possibly higher rates, but it has been designed and tested to work best on signals sampled at 120 or 150 Hz. \n";
				algorithmDetails[i].sVersionIdWebService = "1.0";
				algorithmDetails[i].sDateWebService = "May 18, 2012";
				algorithmDetails[i].sLicence = "http://physionet.org/physiotools/wag/wag.htm";
				

				algorithmDetails[i].aParameters =  new AdditionalParameters[1];
				  for(int p=0; p<1;p++){
					  algorithmDetails[i].aParameters[p] = new AdditionalParameters();
				  }
				  //-------------
				  algorithmDetails[i].aParameters[0].sParameterFlag = "-v ";
				  algorithmDetails[i].aParameters[0].sDisplayShortName = "Verbose mode";
				  algorithmDetails[i].aParameters[0].sToolTipDescription = "Verbose mode: print information about the detector parameters.";
				  algorithmDetails[i].aParameters[0].sLongDescription = "Verbose mode: print information about the detector parameters.";
				  algorithmDetails[i].aParameters[0].sParameterDefaultValue = "false";
				  //-------------		  

				algorithmDetails[i].afInFileTypes = new FileTypes[1];
				  for(int f=0; f<1;f++){
					  algorithmDetails[i].afInFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afInFileTypes[0].sName="TabDelimitedText";
				  algorithmDetails[i].afInFileTypes[0].sExtension = "txt";
				  algorithmDetails[i].afInFileTypes[0].sDisplayShortName = "GE_Magellan";
				//-------------

				algorithmDetails[i].afOutFileTypes = new FileTypes[1];
				  for(int f=0; f<1;f++){
					  algorithmDetails[i].afOutFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afOutFileTypes[0].sName="QRS_Score";
				  algorithmDetails[i].afOutFileTypes[0].sExtension="csv";
				  algorithmDetails[i].afOutFileTypes[0].sDisplayShortName = "QRS Score";
				//-------------
					  

				algorithmDetails[i].apAlgorithmProgrammers = new People[3];
				  for(int p=0; p<3;p++){
					  algorithmDetails[i].apAlgorithmProgrammers[p] = new People();
				  }
				  algorithmDetails[i].apAlgorithmProgrammers[0].sEmail="george@mit.edu";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sFirstName="George";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sMiddleName="B.";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sLastName="Moody";  
				  algorithmDetails[i].apAlgorithmProgrammers[0].sOrganization="Physionet";
				//-------------------  
				  algorithmDetails[i].apAlgorithmProgrammers[1].sFirstName="WAH";
				  algorithmDetails[i].apAlgorithmProgrammers[1].sLastName="Engelse";  
				//-------------------  
				  algorithmDetails[i].apAlgorithmProgrammers[2].sFirstName="Cees";
				  algorithmDetails[i].apAlgorithmProgrammers[2].sLastName="Zeelenberg";  
				//-------------------  
				  

				algorithmDetails[i].apWebServiceProgrammers = new People[1];
				  for(int p=0; p<1;p++){
					  algorithmDetails[i].apWebServiceProgrammers[p] = new People();
				  }
				  algorithmDetails[i].apWebServiceProgrammers[0].sEmail="mshipwa1@jhu.edu";
				  algorithmDetails[i].apWebServiceProgrammers[0].sFirstName="Michael";
				  algorithmDetails[i].apWebServiceProgrammers[0].sMiddleName="P.";
				  algorithmDetails[i].apWebServiceProgrammers[0].sLastName="Shipway";  
				  algorithmDetails[i].apWebServiceProgrammers[0].sOrganization="Johns Hopkins University";
				//-------------------  
					  

				algorithmDetails[i].aoAffiliatedOrgs = new Organization[3];
				  for(int p=0; p<3;p++){
					  algorithmDetails[i].aoAffiliatedOrgs[p] = new Organization();
				  }
				  algorithmDetails[i].aoAffiliatedOrgs[0].sURL="www.jhu.edu";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sName="Johns Hopkins University";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactEmail = "mshipwa1@jhu.edu";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactPhone = "(410) 516-5294";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactFirstName = "Michael";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactMiddleName = "P.";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactLastName = "Shipway";
	
				  
				  algorithmDetails[i].aoAffiliatedOrgs[1].sURL="www.cvrgrid.org";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sName="CardioVascular Research Grid (CVRG)";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactEmail = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactPhone = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactFirstName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactMiddleName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactLastName = "";
				//-------------------  
				  algorithmDetails[i].aoAffiliatedOrgs[2].sURL="http://www.physionet.org";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sName="PhysioNet";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactEmail = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactPhone = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactFirstName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactMiddleName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactLastName = "";
				//-------------------  
				break;			
				
			
			default:
				algorithmDetails[i].sServiceMethod = "ssQRS_ScoreWrapperType2";
				algorithmDetails[i].sDisplayShortName = "Strauss-Sylvester score";
				algorithmDetails[i].sAnalysisServiceURL = "http://128.220.76.170:8080/axis2/services/physionetAnalysisService";
				
				// most of these details were copied over from the physionet analysis service descriptions
				//algorithmDetails[i].sServiceMethod = "ssQRS_ScoreWrapperType2";
				algorithmDetails[i].sServiceName = "physionetAnalysisService";
				algorithmDetails[i].sToolTipDescription = "single-channel QRS detector based on length transform.";
				algorithmDetails[i].sURLreference = "http://www.physionet.org/physiotools/wag/ssQRS_Score-1.htm";
				algorithmDetails[i].sLongDescription = "The Strauss-Sylvester score attempts to locate QRS complexes in an ECG signal in the specified record. The detector algorithm is based on the length transform. The output of ssQRS_Score is an annotation file (with annotator name ssQRS_Score) in which all detected beats are labelled normal; the annotation file will also contain optional J-point annotations if the -j option (see below) is used. \n"
					+ " ssQRS_Score can process records containing any number of signals, but it uses only one signal for QRS detection (signal 0 by default; this can be changed using the -s option, see below). ssQRS_Score is optimized for use with adult human ECGs. For other ECGs, it may be necessary to experiment with the sampling frequency as recorded in the input record�s header file (see header(5) ), the detector threshold (which can be set using the -m option), and the time constants indicated in the source file. \n"
					+ " ssQRS_Score optionally uses the WFDB library�s setifreq function to resample the input signal at 120 or 150 Hz (depending on the mains frequency, which can be specified using the -p option). ssQRS_Score performs well using input sampled at a range of rates up to 360 Hz and possibly higher rates, but it has been designed and tested to work best on signals sampled at 120 or 150 Hz. \n";
				algorithmDetails[i].sVersionIdWebService = "1.0";
				algorithmDetails[i].sDateWebService = "May 18, 2012";
				algorithmDetails[i].sLicence = "http://physionet.org/physiotools/wag/wag.htm";
				

				algorithmDetails[i].aParameters =  new AdditionalParameters[1];
				  for(int p=0; p<1;p++){
					  algorithmDetails[i].aParameters[p] = new AdditionalParameters();
				  }
				  //-------------
				  algorithmDetails[i].aParameters[0].sParameterFlag = "-v ";
				  algorithmDetails[i].aParameters[0].sDisplayShortName = "Verbose mode";
				  algorithmDetails[i].aParameters[0].sToolTipDescription = "Verbose mode: print information about the detector parameters.";
				  algorithmDetails[i].aParameters[0].sLongDescription = "Verbose mode: print information about the detector parameters.";
				  algorithmDetails[i].aParameters[0].sParameterDefaultValue = "false";
				  //-------------		  

				algorithmDetails[i].afInFileTypes = new FileTypes[1];
				  for(int f=0; f<1;f++){
					  algorithmDetails[i].afInFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afInFileTypes[0].sName="TabDelimitedText";
				  algorithmDetails[i].afInFileTypes[0].sExtension = "txt";
				  algorithmDetails[i].afInFileTypes[0].sDisplayShortName = "GE_Magellan";
				//-------------
				  

				algorithmDetails[i].afOutFileTypes = new FileTypes[1];
				  for(int f=0; f<1;f++){
					  algorithmDetails[i].afOutFileTypes[f] = new FileTypes();
				  }
				  algorithmDetails[i].afOutFileTypes[0].sName="QRS_Score";
				  algorithmDetails[i].afOutFileTypes[0].sExtension="csv";
				  algorithmDetails[i].afOutFileTypes[0].sDisplayShortName = "QRS Score";
				//-------------
					  
	

				algorithmDetails[i].apAlgorithmProgrammers = new People[3];
				  for(int p=0; p<3;p++){
					  algorithmDetails[i].apAlgorithmProgrammers[p] = new People();
				  }
				  algorithmDetails[i].apAlgorithmProgrammers[0].sEmail="george@mit.edu";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sFirstName="George";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sMiddleName="B.";
				  algorithmDetails[i].apAlgorithmProgrammers[0].sLastName="Moody";  
				  algorithmDetails[i].apAlgorithmProgrammers[0].sOrganization="Physionet";
				//-------------------  
				  algorithmDetails[i].apAlgorithmProgrammers[1].sFirstName="WAH";
				  algorithmDetails[i].apAlgorithmProgrammers[1].sLastName="Engelse";  
				//-------------------  
				  algorithmDetails[i].apAlgorithmProgrammers[2].sFirstName="Cees";
				  algorithmDetails[i].apAlgorithmProgrammers[2].sLastName="Zeelenberg";  
				//-------------------  
				  
 
				algorithmDetails[i].apWebServiceProgrammers = new People[1];
				  for(int p=0; p<1;p++){
					  algorithmDetails[i].apWebServiceProgrammers[p] = new People();
				  }
				  algorithmDetails[i].apWebServiceProgrammers[0].sEmail="mshipwa1@jhu.edu";
				  algorithmDetails[i].apWebServiceProgrammers[0].sFirstName="Michael";
				  algorithmDetails[i].apWebServiceProgrammers[0].sMiddleName="P.";
				  algorithmDetails[i].apWebServiceProgrammers[0].sLastName="Shipway";  
				  algorithmDetails[i].apWebServiceProgrammers[0].sOrganization="Johns Hopkins University";
				//-------------------  
					  

				algorithmDetails[i].aoAffiliatedOrgs = new Organization[3];
				  for(int p=0; p<3;p++){
					  algorithmDetails[i].aoAffiliatedOrgs[p] = new Organization();
				  }
				  algorithmDetails[i].aoAffiliatedOrgs[0].sURL="www.jhu.edu";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sName="Johns Hopkins University";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactEmail = "mshipwa1@jhu.edu";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactPhone = "(410) 516-5294";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactFirstName = "Michael";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactMiddleName = "P.";
				  algorithmDetails[i].aoAffiliatedOrgs[0].sContactLastName = "Shipway";
	
				  
				  algorithmDetails[i].aoAffiliatedOrgs[1].sURL="www.cvrgrid.org";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sName="CardioVascular Research Grid (CVRG)";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactEmail = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactPhone = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactFirstName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactMiddleName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[1].sContactLastName = "";
				//-------------------  
				  algorithmDetails[i].aoAffiliatedOrgs[2].sURL="http://www.physionet.org";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sName="PhysioNet";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactEmail = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactPhone = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactFirstName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactMiddleName = "";
				  algorithmDetails[i].aoAffiliatedOrgs[2].sContactLastName = "";
				//-------------------  
	
				
				break;
			}
		}
		return algorithmDetails;
	}
}
