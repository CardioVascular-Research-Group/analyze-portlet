package edu.jhu.cvrg.waveform.utility;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import com.liferay.portal.model.User;

@ManagedBean(name="progressBean")
@ViewScoped
public class ProgressBean implements Serializable {

	private AnalysisUtility analysisUtility = new AnalysisUtility();

	private User userModel;
	private static final long serialVersionUID = -8505968495234737448L;
	private Integer progress;
	private DateFormat displayFormat = new SimpleDateFormat("MM/dd/yyyy");
	private Calendar theCalendar = Calendar.getInstance();
	private Date currentTime = theCalendar.getTime();
	private String analysisDate = displayFormat.format(currentTime);

	public Integer getProgress() {
		if(progress == null)
			progress = 0;
		else {
			
			int[] progressDetails = analysisUtility.getProgressAccumulation(userModel.getScreenName(), analysisDate);
						
			progress = progress + progressDetails[2];
			if (progressDetails[0] > 0) progress = progress / progressDetails[0];
									
			if (progress > 100)
				progress = 100;
		}
		
		return progress;
	}

	public void setProgress(Integer progress) {
		this.progress = progress;
	}
	
	public void onComplete() {
		this.cancel();
	}
	
	public void cancel() {
		progress = null;
	}
}
					