package cgl.webservices;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.ImageObserver;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.net.URL;
import java.util.Calendar;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class VideoMaker implements ImageObserver{
	
	public class StationPos {
		String stationName;
		int x;
		int y;
		
		public StationPos(String name, int x, int y) {
			stationName = name;
			this.x = x;
			this.y = y;
		}
	}
	
	String xmlResPath;	// path to the daily rdahmm result xml file
	String xmlResUrl;	// url to the daily rdahmm result xml file
	Calendar startDate;	// starting date of the video
	Calendar endDate;	// end date of the video
	Calendar histStartDate;	// starting date of the historical video
	String picStorageDir;	// directory to store the picture for everyday
	String backgroundPath;	// path to the background picture of the video
	String changeIconUrl;	// url to the icon corresponding to state change
	String changeHistIconUrl;	// url to the icon corresponding to historical state change
	String normalIconUrl;	// url to the icon corresponding to no state change
	String noDataIconUrl;	// url to the icon corresponding to no data
	String noDataCHIconUrl;	// url to the icon corresponding to no data but with historical state change
	String posFilePath;		// path to the file that defines the relative position of every station in the background picture
	String mencoderPath;	// path to mencoder for making new video
	String makeVideoShPath; // path of shell scripts for making a video
	String mergeVideoShPath;// path of shell scripts for merging two videos
	String histVideoPath;	// path to the historical video that is to be merged with a newly created one 
	String picListFilePath;	// path to the list of pictures for making a new video
	String newVideoPath;	// path of the newly created video
	String finalVideoPath;	// path of the merged final video
	String finalVideoDir;	// directory for saving the final video
	BufferedImage backgroundImg;	// background picture of the video
	BufferedImage changeIconImg;	// the icon corresponding to state change
	BufferedImage changeHistIconImg;// the icon corresponding to historical state change
	BufferedImage normalIconImg;	// the icon corresponding to no state change
	BufferedImage noDataIconImg;	// the icon corresponding to no data
	BufferedImage noDataCHIconImg;	// the icon corresponding to no data but with historical state change
	DailyRdahmmResultsBean resultsBean;	// object to calculate stations' colors
	Hashtable posTable;		// table to save stations' positions in the background picture
	
	public VideoMaker (String cgfPath) {
		initialize(cgfPath);
	}
	
	protected void initialize (String propPath) {
		Properties p = new Properties();
		try {
			// load the necessary properties
			p.load(new FileInputStream(propPath));
			xmlResPath = p.getProperty("xmlResPath");
			xmlResUrl = p.getProperty("xmlResUrl");
			picStorageDir = p.getProperty("picDir");
			backgroundPath = p.getProperty("backPicPath");
			changeIconUrl = p.getProperty("changeIconUrl");
			changeHistIconUrl = p.getProperty("changeHistIconUrl");
			normalIconUrl = p.getProperty("normalIconUrl");
			noDataIconUrl = p.getProperty("noDataIconUrl");
			noDataCHIconUrl = p.getProperty("noDataCHIconUrl");
			posFilePath = p.getProperty("posFilePath");
			startDate = Calendar.getInstance();
			UtilSet.setDateByString(startDate, p.getProperty("startDate"));
			endDate = Calendar.getInstance();
			UtilSet.setDateByString(endDate, p.getProperty("endDate"));
			histStartDate = Calendar.getInstance();
			UtilSet.setDateByString(histStartDate, p.getProperty("histVideoStartDate"));
			mencoderPath = p.getProperty("mencoderPath");
			makeVideoShPath = p.getProperty("makeVideoShPath");
			mergeVideoShPath = p.getProperty("mergeVideoShPath");
			histVideoPath = p.getProperty("histVideoPath");
			finalVideoDir = p.getProperty("finalVideoDir");
			
			// create images
			backgroundImg = ImageIO.read(new File(backgroundPath));
			normalIconImg = ImageIO.read(new URL(normalIconUrl));
			changeIconImg = ImageIO.read(new URL(changeIconUrl));
			changeHistIconImg = ImageIO.read(new URL(changeHistIconUrl));
			noDataIconImg = ImageIO.read(new URL(noDataIconUrl));
			noDataCHIconImg = ImageIO.read(new URL(noDataCHIconUrl));
			
			// create DailyRdahmmResultsBean to calculate stations' colors
			resultsBean = new DailyRdahmmResultsBean();
			resultsBean.setXmlResPath(xmlResPath);
			resultsBean.setXmlResURL(xmlResUrl);
			
			// get stations' positions in the picture
			if (!readPositions(posFilePath)) {
				System.out.println("failed to read positions!");
				System.exit(-1);
			}
		} catch (Exception e) {
			System.out.println("Error in initlaizing!!");
			e.printStackTrace();
			System.exit(-1);			
		}
	}
	
	protected boolean readPositions (String posFilePath) {
		try {
			posTable = new Hashtable();
			BufferedReader br = new BufferedReader(new FileReader(posFilePath));
			String line = br.readLine();
			int lineNum = 1;
			while (line != null && line.trim().length() > 0) {
				if (lineNum > 2) {
					StringTokenizer st = new StringTokenizer(line, "\t ");
					String name = st.nextToken();
					st.nextToken();
					String posStr = st.nextToken();
					int idx = posStr.indexOf(',');
					int x = Integer.parseInt(posStr.substring(0, idx));
					int y = Integer.parseInt(posStr.substring(idx+1));
					posTable.put(name, new StationPos(name, x, y));
				}
				line = br.readLine();
				lineNum++;					
			}
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}
	
	public void drawPictures() {
		Calendar tmpDate = Calendar.getInstance();
		tmpDate.setTimeInMillis(startDate.getTimeInMillis());
		
		while (tmpDate.compareTo(endDate) <=0) {
			try {
				// check if the picture for tmpDate already exists
				String dir = picStorageDir + File.separator + tmpDate.get(Calendar.YEAR) + File.separator;
				File picDir = new File(dir);
				if (!picDir.exists() || !picDir.isDirectory())
					picDir.mkdirs();
				String path = dir + DailyRdahmmResultsBean.getDateString(tmpDate) + ".jpg";
				File picFile = new File(path);
				if (picFile.exists())
					picFile.delete();
				if (!picFile.exists()) {
					System.out.println("trying to draw picture for " + DailyRdahmmResultsBean.getDateString(tmpDate));				
					// draw picture for this tmpDate					
					for (int i=0; i<resultsBean.stationArray.length; i++) {
						//System.out.println("trying to draw station " + resultsBean.stationArray[i].stationID);
						char c = resultsBean.getColorForStation(tmpDate, resultsBean.stationArray[i]);
						// values of c -- 0:green, 1:red, 2:yellow, 3:grey, 4:blue
						BufferedImage smallImg = null;
						switch (c) {
						case '0':
							smallImg = normalIconImg;
							break;
						case '1':
							smallImg = changeIconImg;
							break;
						case '2':
							smallImg = changeHistIconImg;
							break;
						case '3':
							smallImg = noDataIconImg;
							break;
						case '4':
							smallImg = noDataCHIconImg;
							break;
						}
						StationPos p = (StationPos)posTable.get(resultsBean.stationArray[i].stationID);
						if (smallImg != null && p != null) {
							backgroundImg.getGraphics().drawImage(smallImg, p.x - smallImg.getWidth()/2, 
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
					Graphics g = backgroundImg.getGraphics();
					g.setColor(Color.BLACK);
					g.drawString(DailyRdahmmResultsBean.getDateString(tmpDate), 100, 15);
					ImageIO.write(backgroundImg, "JPEG", picFile);
					backgroundImg = ImageIO.read(new File(backgroundPath));
				}
			} catch (Exception e) {
				e.printStackTrace();
			}
			// set tmpDate to the next day
			tmpDate.set(Calendar.DATE, tmpDate.get(Calendar.DATE)+1);
		}
	}
	
	public void makeList() {
		Calendar tmpDate = Calendar.getInstance();
		tmpDate.setTimeInMillis(startDate.getTimeInMillis());
		
		try {
			picListFilePath = picStorageDir + File.separator + DailyRdahmmResultsBean.getDateString(startDate) 
								+ "to" + DailyRdahmmResultsBean.getDateString(endDate) + ".txt";
			FileWriter fwList = new FileWriter(picListFilePath, false);
			while (tmpDate.compareTo(endDate) <=0) {
				String line = picStorageDir + File.separator + tmpDate.get(Calendar.YEAR) 
								+ File.separator + DailyRdahmmResultsBean.getDateString(tmpDate) + ".jpg\n";
				fwList.write(line);
				tmpDate.set(Calendar.DATE, tmpDate.get(Calendar.DATE)+1);
			}
			fwList.close();
		} catch (Exception e) {
			e.printStackTrace();
		}	
	}
	
	public void makeNewVideo() {
		newVideoPath = picStorageDir + File.separator + UtilSet.getDateString(startDate) 
								+ "to" + UtilSet.getDateString(endDate) + ".mpeg";
		String err = UtilSet.exec(makeVideoShPath + " " + mencoderPath + " " + picListFilePath + " " + newVideoPath);
		if (err.length() > 0)
			UtilSet.log(0, "result for making new video:" + err);
		else
			UtilSet.log(0, "new video made successfully!");
		
		finalVideoPath = getFinalVideoPath();
		err = UtilSet.exec(mergeVideoShPath + " " + mencoderPath + " " + histVideoPath + " " + newVideoPath 
							+ " " + finalVideoPath);
		if (err.length() > 0)
			UtilSet.log(0, "result for merging two videos:" + err);
		else
			UtilSet.log(0, "final video made successfully!");
		
		File fileList = new File(picListFilePath);
		fileList.delete();
		File fileNewVideo = new File(newVideoPath);
		fileNewVideo.delete();
	}
	
	public String getFinalVideoPath() {
		return finalVideoDir + File.separator + UtilSet.getDateString(histStartDate) 
				+ "to" + UtilSet.getDateString(endDate) + ".mpeg";
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
		if (args.length < 1) {
			System.out.println("Usage: java VideoMaker <configuration file path>");
			return;
		}
		VideoMaker vm = new VideoMaker(args[0]);
		vm.drawPictures();
		vm.makeList();
		vm.makeNewVideo();
	}

}
