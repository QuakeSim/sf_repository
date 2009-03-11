package cgl.webservices;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.util.Calendar;
import java.util.Properties;

import javax.imageio.ImageIO;

public class DailyRDAHMMVideoThread extends Thread implements ImageObserver {
	
	protected Properties propConfig;
	
	public DailyRDAHMMVideoThread (Properties p) {
		propConfig = p;
	}
	
	/**
	 * draw pictures for the time from calStartDate to calEndDate for a make-video request
	 * @param req
	 * @param calStartDate
	 * @param calEndDate
	 * @param workDir
	 */
	public void drawPictures(MakeVideoRequest req, Calendar calStartDate, Calendar calEndDate, String workDir) {
		/*
		sbWorkDir.append(DailyRDAHMMVideoService.picDir).append(File.separatorChar).append(req.getDataSource());
		sbWorkDir.append(File.separatorChar).append(req.getPreProcessTreat()).append(File.separatorChar);
		*/
		int pos = req.getResultUrl().lastIndexOf('/');
		String xmlPath = workDir + File.separator + req.getResultUrl().substring(pos+1);
		
		DailyRdahmmResultsBean resultsBean = new DailyRdahmmResultsBean();
		resultsBean.setXmlResPath(xmlPath);
		resultsBean.setXmlResURL(req.getResultUrl());
		
		Calendar tmpDate = Calendar.getInstance();
		tmpDate.setTimeInMillis(calStartDate.getTimeInMillis());
		
		while (tmpDate.compareTo(calEndDate) <=0) {
			try {
				// check if the picture for tmpDate already exists
				StringBuffer sbDir = new StringBuffer(workDir);
				sbDir.append(File.separatorChar).append(tmpDate.get(Calendar.YEAR)).append(File.separatorChar);
				File picDir = new File(sbDir.toString());
				if (!picDir.exists() || !picDir.isDirectory())
					picDir.mkdirs();
				String path = sbDir.append(UtilSet.getDateString(tmpDate)).append(".jpg").toString();
				File picFile = new File(path);
				if (picFile.exists())
					picFile.delete();
				if (!picFile.exists()) {
					System.out.println("trying to draw picture for " + UtilSet.getDateString(tmpDate));				
					// draw picture for this tmpDate					
					for (int i=0; i<resultsBean.stationArray.length; i++) {
						//System.out.println("trying to draw station " + resultsBean.stationArray[i].stationID);
						char c = resultsBean.getColorForStation(tmpDate, resultsBean.stationArray[i]);
						// values of c -- 0:green, 1:red, 2:yellow, 3:grey, 4:blue
						BufferedImage smallImg = null;
						switch (c) {
						case '0':
							smallImg = DailyRDAHMMVideoService.normalIconImg;
							break;
						case '1':
							smallImg = DailyRDAHMMVideoService.changeIconImg;
							break;
						case '2':
							smallImg = DailyRDAHMMVideoService.changeHistIconImg;
							break;
						case '3':
							smallImg = DailyRDAHMMVideoService.noDataIconImg;
							break;
						case '4':
							smallImg = DailyRDAHMMVideoService.noDataCHIconImg;
							break;
						}
						StationPos p = (StationPos)DailyRDAHMMVideoService.posTable.get(resultsBean.stationArray[i].stationID);
						if (smallImg != null && p != null) {
							DailyRDAHMMVideoService.backgroundImg.getGraphics().drawImage(smallImg, p.x - smallImg.getWidth()/2, 
																	p.y - smallImg.getHeight(), this);
						} else {
							if (p == null) {
								System.out.println("can't find position for station " + resultsBean.stationArray[i].stationID);
							}
							if (smallImg == null) {
								System.out.println("can't get color for station " + resultsBean.stationArray[i].stationID);
							}
						}
					}
					Graphics g = DailyRDAHMMVideoService.backgroundImg.getGraphics();
					g.setColor(Color.BLACK);
					g.drawString(UtilSet.getDateString(tmpDate), 100, 15);
					ImageIO.write(DailyRDAHMMVideoService.backgroundImg, "JPEG", picFile);
					DailyRDAHMMVideoService.backgroundImg = ImageIO.read(new File(DailyRDAHMMVideoService.backPicPath));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// set tmpDate to the next day
			tmpDate.set(Calendar.DATE, tmpDate.get(Calendar.DATE)+1);
		}
	}
	
	/**
	 * make the list of pictures for time from calStartDate to calEndDate
	 * @param calStartDate
	 * @param calEndDate
	 * @param workDir
	 * @return
	 */
	public String makeList(Calendar calStartDate, Calendar calEndDate, String workDir) {
		Calendar tmpDate = Calendar.getInstance();
		tmpDate.setTimeInMillis(calStartDate.getTimeInMillis());
		StringBuffer sbPicListPath = new StringBuffer(workDir);
		sbPicListPath.append(File.separatorChar).append(UtilSet.getDateString(calStartDate)).append("to");
		sbPicListPath.append(UtilSet.getDateString(calEndDate)).append(".txt");
		
		try {
			FileWriter fwList = new FileWriter(sbPicListPath.toString(), false);
			while (tmpDate.compareTo(calEndDate) <=0) {
				StringBuffer sbLine = new StringBuffer(workDir);
				sbLine.append(File.separatorChar).append(tmpDate.get(Calendar.YEAR)).append(File.separatorChar);
				sbLine.append(UtilSet.getDateString(tmpDate)).append(".jpg\n");
				fwList.write(sbLine.toString());
				tmpDate.set(Calendar.DATE, tmpDate.get(Calendar.DATE)+1);
			}
			fwList.flush();
			fwList.close();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
		
		return sbPicListPath.toString();
	}
	
	public void run() {
		while (true) {
			MakeVideoRequest nextReq = null;
			synchronized (DailyRDAHMMVideoService.requestList) {
				if (!DailyRDAHMMVideoService.requestList.isEmpty()) {
					nextReq = DailyRDAHMMVideoService.requestList.removeFirst();
				} else {
					try {
						DailyRDAHMMVideoService.requestList.wait();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}
			if (nextReq != null) {
				try {
					processRequest(nextReq);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
	
	/**
	 * process a request for making a video
	 * @param req
	 */
	protected void processRequest(MakeVideoRequest req) throws Exception{
		UtilSet.log(1, "about to process reqest " + req);
		boolean needDeleteLastVideo = false;		
		String prefix = req.getDataSource() + '_' + req.getPreProcessTreat() + '_';
		
		// first, check if the last video has consistent information and wider start and end dates
		String lastVideoPath = propConfig.getProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_LVP);
		String lastStartDate = propConfig.getProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_LVSD);
		String lastEndDate = propConfig.getProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_LVED);
		boolean lvpValid = lastVideoPath != null && lastVideoPath.length() > 0;
		File flv = null;
		if (lvpValid) {
			flv = new File(lastVideoPath);
			lvpValid = flv.exists();
		}
		boolean lsdValid = lastStartDate != null && lastStartDate.length() > 0;
		boolean ledValid = lastEndDate != null && lastEndDate.length() > 0;		
		if (lvpValid != lsdValid || lvpValid != ledValid || lsdValid != ledValid) {
			throw new Exception("Inconsistent last video information");
		}
		
		UtilSet.log(1, "last video information consistent");
		
		Calendar calTmp1 = Calendar.getInstance();
		Calendar calTmp2 = Calendar.getInstance();
		// if the last video already covers the start and end date of the new request, return directly
		if (lvpValid) {
			UtilSet.setDateByString(calTmp1, lastStartDate);
			UtilSet.setDateByString(calTmp2, lastEndDate);
			if (calTmp1.compareTo(req.getCalStartDate()) <= 0
				&& calTmp2.compareTo(req.getCalEndDate()) >= 0) {
				return;
			} else {
				UtilSet.log(1, "need to delete last video");
				needDeleteLastVideo = true;
			}
		}
		
		// check consistency of historical video's information
		String histVideoPath = propConfig.getProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_HVP);
		String histStartDate = propConfig.getProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_HVSD);
		String histEndDate = propConfig.getProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_HVED);
		boolean hvpValid = histVideoPath != null && histVideoPath.length() > 0;
		File fhv = null;
		if (hvpValid) {
			fhv = new File(histVideoPath);
			hvpValid = fhv.exists();
		}
		boolean hsdValid = histStartDate != null && histStartDate.length() > 0;
		boolean hedValid = histEndDate != null && histEndDate.length() > 0;
		if (hvpValid != hsdValid || hvpValid != hedValid || hsdValid != hedValid) {
			throw new Exception("Inconsistent historical video information");
		}
		
		UtilSet.log(1, "historical video information consistent");
		
		// Now do the video-making job. First, if new historical video needs to be made, make one 
		StringBuffer sbWorkDir = new StringBuffer();
		sbWorkDir.append(DailyRDAHMMVideoService.picDir).append(File.separatorChar).append(req.getDataSource());
		sbWorkDir.append(File.separatorChar).append(req.getPreProcessTreat());
		String workDir = sbWorkDir.toString();
		File fWorkDir = new File(workDir);
		if (!fWorkDir.exists())
			fWorkDir.mkdirs();
		String histPathForMerge = histVideoPath;
		Calendar calNewPieceStartDate = Calendar.getInstance();
		setUpdatedHistDate(calTmp2, req.getCalEndDate());
		if (hedValid) {
			UtilSet.setDateByString(calTmp1, histEndDate);
			if (calTmp2.compareTo(calTmp1) > 0) {
				UtilSet.log(1, "need to update historical video");
				// out-dated historical video, update it
				UtilSet.ndaysBeforeToday(calTmp2, -1, calNewPieceStartDate);
				
				// make the piece of video needed to be appended to the old historical video
				UtilSet.ndaysBeforeToday(calTmp1, -1, calTmp1);
				drawPictures(req, calTmp1, calTmp2, workDir);
				String picListPath = makeList(calTmp1, calTmp2, workDir);
				StringBuffer sbNewPiecePath = new StringBuffer(workDir);
				sbNewPiecePath.append(File.separatorChar).append(req.getDataSource()).append('_').append(req.getPreProcessTreat());
				sbNewPiecePath.append('_').append(UtilSet.getDateString(calTmp1)).append("to").append(UtilSet.getDateString(calTmp2));
				sbNewPiecePath.append(".mp4");
				makeNewVideo(sbNewPiecePath.toString(), picListPath);
				
				StringBuffer sbNewHistPath = new StringBuffer(workDir);
				sbNewHistPath.append(File.separatorChar).append(req.getDataSource()).append('_').append(req.getPreProcessTreat());
				sbNewHistPath.append('_').append(histStartDate).append("to").append(UtilSet.getDateString(calTmp2));
				sbNewHistPath.append(".mp4");
				
				// merge the new piece with the old hitorical video
				mergeVideo(histVideoPath, sbNewPiecePath.toString(), sbNewHistPath.toString());
				deleteFile(picListPath);
				deleteFile(sbNewPiecePath.toString());
				deleteFile(histVideoPath);
				
				histPathForMerge = sbNewHistPath.toString();
				synchronized (propConfig) {
					propConfig.setProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_HVP, histPathForMerge);
					propConfig.setProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_HVSD, histStartDate);
					propConfig.setProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_HVED, UtilSet.getDateString(calTmp2));
					propConfig.store(new FileOutputStream(new File(DailyRDAHMMVideoService.propFileUri), false), "");
				}
			} else {
				UtilSet.ndaysBeforeToday(calTmp1, -1, calNewPieceStartDate);
			}
		} else {
			UtilSet.log(1, "need to build new historical video");
			// there is no historical video yet, build a new one from scratch
			UtilSet.ndaysBeforeToday(calTmp2, -1, calNewPieceStartDate);
			histStartDate = DailyRDAHMMVideoService.DEFAULT_HIST_VIDEO_START_DATE;
			UtilSet.setDateByString(calTmp1, histStartDate);
			drawPictures(req, calTmp1, calTmp2, workDir);
			String picListPath = makeList(calTmp1, calTmp2, workDir);
			StringBuffer sbNewHistPath = new StringBuffer(workDir);
			sbNewHistPath.append(File.separatorChar).append(req.getDataSource()).append('_').append(req.getPreProcessTreat());
			sbNewHistPath.append('_').append(histStartDate).append("to").append(UtilSet.getDateString(calTmp2));
			sbNewHistPath.append(".mp4");
			makeNewVideo(sbNewHistPath.toString(), picListPath);
			histPathForMerge = sbNewHistPath.toString();
			deleteFile(picListPath);
			synchronized (propConfig) {
				propConfig.setProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_HVP, histPathForMerge);
				propConfig.setProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_HVSD, histStartDate);
				propConfig.setProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_HVED, UtilSet.getDateString(calTmp2));
				propConfig.store(new FileOutputStream(new File(DailyRDAHMMVideoService.propFileUri), false), "");
			}
		}
		
		// draw new piece for the final video
		drawPictures(req, calNewPieceStartDate, req.getCalEndDate(), workDir);
		String picListPath = makeList(calNewPieceStartDate, req.getCalEndDate(), workDir);
		StringBuffer sbNewPiecePath = new StringBuffer(workDir);
		sbNewPiecePath.append(File.separatorChar).append(req.getDataSource()).append('_').append(req.getPreProcessTreat());
		sbNewPiecePath.append('_').append(UtilSet.getDateString(calNewPieceStartDate)).append("to");
		sbNewPiecePath.append(UtilSet.getDateString(req.getCalEndDate())).append(".mp4");
		makeNewVideo(sbNewPiecePath.toString(), picListPath);
		
		StringBuffer sbFinalVideoPath = new StringBuffer(DailyRDAHMMVideoService.destVideoDir);
		sbFinalVideoPath.append(File.separatorChar).append(req.getDataSource()).append('_').append(req.getPreProcessTreat());
		sbFinalVideoPath.append('_').append(histStartDate).append("to").append(UtilSet.getDateString(req.getCalEndDate()));
		sbFinalVideoPath.append(".mp4");
		
		// merge the new piece with the old hitorical video
		mergeVideo(histPathForMerge, sbNewPiecePath.toString(), sbFinalVideoPath.toString());
		deleteFile(sbNewPiecePath.toString());
		deleteFile(picListPath);
		
		synchronized (propConfig) {
			propConfig.setProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_LVP, sbFinalVideoPath.toString());
			propConfig.setProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_LVSD, histStartDate);
			propConfig.setProperty(prefix + DailyRDAHMMVideoService.PROP_NAME_SUFFIX_LVED, UtilSet.getDateString(req.getCalEndDate()));
			propConfig.store(new FileOutputStream(new File(DailyRDAHMMVideoService.propFileUri), false), "");
		}
		
		if (needDeleteLastVideo)
			deleteFile(lastVideoPath);
	}
	
	protected void deleteFile(String path) {
		File f = new File(path);
		f.delete();
	}
	
	/**
	 * set an end date for historical video that is close enough to a given video's end date
	 * the historical date is set to be the last day of the month that is earlier than the
	 * given video's end date by 2. E.g., return 2008-8-31 for 2008-10-13
	 * @param ret The calendar object which will contain the date set 
	 * @param videoDate The required video's end date
	 */
	protected void setUpdatedHistDate(Calendar ret, Calendar videoDate) {
		ret.setTimeInMillis(videoDate.getTimeInMillis());
		
		ret.set(Calendar.MONTH, videoDate.get(Calendar.MONTH) - 1);
		ret.set(Calendar.DAY_OF_MONTH, 0);
	}
	
	/**
	 * make a new video with makeVideo.sh
	 * @param newVideoPath
	 * @param picListPath
	 */
	public void makeNewVideo(String newVideoPath, String picListPath) {
		StringBuffer sb = new StringBuffer();
		sb.append(DailyRDAHMMVideoService.makeVideoShPath).append(' ').append(DailyRDAHMMVideoService.mencoderPath);
		sb.append(' ').append(picListPath).append(' ').append(newVideoPath).append(' ').append(DailyRDAHMMVideoService.mp4BoxPath);
		
		String err = UtilSet.exec(sb.toString());
		if (err.length() > 0)
			UtilSet.log(0, "result for making new video:" + err);
		else
			UtilSet.log(0, "new video made successfully!");
	}
	
	/**
	 * merge two videos with mergeVideo.sh
	 * @param videoPath1
	 * @param videoPath2
	 * @param finalVideoPath
	 */
	public void mergeVideo(String videoPath1, String videoPath2, String finalVideoPath) {
		StringBuffer sb = new StringBuffer();
		sb.append(DailyRDAHMMVideoService.mergeVideoShPath).append(' ').append(DailyRDAHMMVideoService.mp4BoxPath).append(' ');
		sb.append(videoPath1).append(' ').append(videoPath2).append(' ').append(finalVideoPath);
		
		String err = UtilSet.exec(sb.toString());
		if (err.length() > 0)
			UtilSet.log(0, "result for merging two videos:" + err);
		else
			UtilSet.log(0, "final video made successfully!");
	}
	
	public String getFinalVideoPath(MakeVideoRequest req) {
		StringBuffer sb = new StringBuffer();
		sb.append(DailyRDAHMMVideoService.destVideoDir).append(File.separatorChar).append(req.getDataSource());
		sb.append('_').append(req.getPreProcessTreat()).append('_').append(UtilSet.getDateString(req.getCalStartDate()));
		sb.append("to").append(UtilSet.getDateString(req.getCalEndDate())).append(".mp4");
		
		return sb.toString();
	}
	
	public boolean imageUpdate(Image img, int infoflags, int x, int y, int width, int height) {
		boolean res = (infoflags | ImageObserver.ABORT) != 0 || (infoflags | ImageObserver.ERROR) != 0; 
		if (res) {
			System.out.println("abort or error!");
		}
		
		if ((infoflags | ImageObserver.ALLBITS) != 0) {
			System.out.println("all bits completed!");
			res = false;
		}
		return  res;				
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		/*
		if (args.length < 1) {
			System.out.println("Usage: java VideoMaker <configuration file path>");
			return;
		} */

		DailyRDAHMMVideoService s = new DailyRDAHMMVideoService();
		String res = s.makeVideo("SOPAC", "NOFILL", 
			"http://gf13.ucs.indiana.edu:8080//rdahmmexec/station-status-change-rss.xml", "1994-01-01", "2009-02-09");
		System.out.println("result of make video: " + res);
	}
}
