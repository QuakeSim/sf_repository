package cgl.webservices;

import java.io.File;
import java.util.Calendar;

public class DailyRDAHMMVideoThread extends Thread {
	String configPath = null;
	VideoMaker vm = null;
	String videoPathToDelete = null;
	public DailyRDAHMMVideoThread(String configPath) {
		this(configPath, null, null);
	}
	
	public DailyRDAHMMVideoThread(String configPath, Calendar startDate, Calendar endDate){
		super();
		this.configPath = configPath;
		vm = new VideoMaker(configPath);
		if (startDate != null)
			vm.startDate = startDate;
		if (endDate != null)
			vm.endDate = endDate;
	}
	
	public void run() {	
		vm.drawPictures();
		vm.makeList();
		vm.makeNewVideo();
		
		if (videoPathToDelete != null) {
			System.out.print("deleting " + videoPathToDelete);
			File vf = new File(videoPathToDelete);
			if (vf.exists()) {
				System.out.println("... result:" + vf.delete());
			}
		} else
			System.out.println("videoPathToDelete is null!");
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
