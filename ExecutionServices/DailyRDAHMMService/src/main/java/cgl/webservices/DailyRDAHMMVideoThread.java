package cgl.webservices;

import java.io.File;
import java.util.Calendar;

public class DailyRDAHMMVideoThread extends Thread {
	String configPath = null;
	Calendar startDate = null;
	Calendar endDate = null;
	VideoMaker vm = null;
	String videoPathToDelete = null;
	public DailyRDAHMMVideoThread(String configPath) {
		this(configPath, null, null);
	}
	
	public DailyRDAHMMVideoThread(String configPath, Calendar startDate, Calendar endDate){
		super();
		this.configPath = configPath;
		this.startDate = startDate;
		this.endDate = endDate;
		vm = new VideoMaker(configPath);
	}
	
	public void run() {
		if (startDate != null)
			vm.startDate = startDate;
		if (endDate != null)
			vm.endDate = endDate;
		vm.drawPictures();
		vm.makeList();
		vm.makeNewVideo();
		
		if (videoPathToDelete != null) {
			File vf = new File(videoPathToDelete);
			if (vf.exists())
				vf.delete();
		}
	}
	
	public String getFinalVideoPath(){
		if (vm != null)
			return vm.getFinalVideoPath();
		return null;
	}

	public String getVideoPathToDelete() {
		return videoPathToDelete;
	}

	public void setVideoPathToDelete(String videoPathToDelete) {
		this.videoPathToDelete = videoPathToDelete;
	}
	
	public void setFinalVideoDir(String finalVideoDir) {
		vm.finalVideoDir = finalVideoDir;
	}

}
