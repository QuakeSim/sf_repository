package cgl.webservices;

import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileReader;
import java.net.URI;
import java.net.URL;
import java.util.Calendar;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Properties;
import java.util.StringTokenizer;

import javax.imageio.ImageIO;

import com.sun.org.apache.xalan.internal.xsltc.runtime.Hashtable;

public class DailyRDAHMMVideoService {
	
	protected static LinkedList<MakeVideoRequest> requestList = new LinkedList<MakeVideoRequest>();
	protected Properties propConfig;
	
	protected static final String PROP_FILE_NAME = "rdahmmVideo.properties";
	protected static final String DEFAULT_HIST_VIDEO_START_DATE = "1994-01-01";
	
	// for error processing
	protected String[] errMsgs = {"Invalid data source.", 
									"Invalid pre-processing treatment method.",
									"Video start date earlier than 1994-01-01.",
									"Video end date earlier than start date or later than today."};
	protected static final int ERR_DATA_SOURCE = 0;
	protected static final int ERR_TREATMENT = 1;
	protected static final int ERR_START_DATE = 2;
	protected static final int ERR_END_DATE = 3;
	protected static final int ERR_NO_ERROR = -1;
	
	// supported data sources and pre-processing treatment methods, where "FILL" means fill
	// missing data time sections with duplicated fake data, and "NOFILL" means no such treatments
	protected LinkedList<String> dataSources;
	protected LinkedList<String> preProcTreats;
	protected DailyRDAHMMVideoThread videoThread;
	
	// constant properties
	protected static String picDir;
	protected static String videoUrlPattern;
	protected static String destVideoDir;
	protected static String serviceUrlPattern;
	protected static String backPicPath;
	protected static String changeIconUrl;
	protected static String changeHistIconUrl;
	protected static String normalIconUrl;
	protected static String noDataIconUrl;
	protected static String noDataCHIconUrl;
	protected static String posFilePath;
	protected static String mencoderPath;
	protected static String mp4BoxPath;
	protected static String makeVideoShPath;
	protected static String mergeVideoShPath;
	protected static BufferedImage backgroundImg;
	protected static BufferedImage normalIconImg;
	protected static BufferedImage changeIconImg;
	protected static BufferedImage changeHistIconImg;
	protected static BufferedImage noDataIconImg;
	protected static BufferedImage noDataCHIconImg;
	protected static Hashtable posTable;		// table to save stations' positions in the background picture
	protected static URI propFileUri;			// URI for the properties file
	
	// name suffix of properties whose value vary among different data sets and treatments
	protected static final String PROP_NAME_SUFFIX_PD = "picDir";
	protected static final String PROP_NAME_SUFFIX_HVP = "histVideoPath";
	protected static final String PROP_NAME_SUFFIX_HVSD = "histVideoStartDate";
	protected static final String PROP_NAME_SUFFIX_HVED = "histVideoEndDate";
	protected static final String PROP_NAME_SUFFIX_LVSD = "lastVideoStartDate";
	protected static final String PROP_NAME_SUFFIX_LVED = "lastVideoEndDate";
	protected static final String PROP_NAME_SUFFIX_LVP = "lastVideoPath";
	
	public DailyRDAHMMVideoService() {
		propConfig = new Properties();
		try {
			propFileUri = getClass().getClassLoader().getResource(PROP_FILE_NAME).toURI();
			propConfig.load(new FileInputStream(new File(propFileUri)));
			
			dataSources = new LinkedList<String>();
			String tmp = propConfig.getProperty("dataSources");
			StringTokenizer st = new StringTokenizer(tmp, ",");
			while (st.hasMoreTokens()) {
				dataSources.add(st.nextToken());
			}
			
			preProcTreats = new LinkedList<String>();
			tmp = propConfig.getProperty("preProcTreats");
			st = new StringTokenizer(tmp, ",");
			while (st.hasMoreTokens()) {
				preProcTreats.add(st.nextToken());
			}
			
			picDir = propConfig.getProperty("picDir");
			videoUrlPattern = propConfig.getProperty("videoUrlPattern");
			destVideoDir = propConfig.getProperty("destVideoDir");
			serviceUrlPattern = propConfig.getProperty("serviceUrlPattern");
			backPicPath = propConfig.getProperty("backPicPath");
			changeIconUrl = propConfig.getProperty("changeIconUrl");
			changeHistIconUrl = propConfig.getProperty("changeHistIconUrl");
			normalIconUrl = propConfig.getProperty("normalIconUrl");
			noDataIconUrl = propConfig.getProperty("noDataIconUrl");
			noDataCHIconUrl = propConfig.getProperty("noDataCHIconUrl");
			posFilePath = propConfig.getProperty("posFilePath");
			mencoderPath = propConfig.getProperty("mencoderPath");
			mp4BoxPath = propConfig.getProperty("mp4BoxPath");
			makeVideoShPath = propConfig.getProperty("makeVideoShPath");
			mergeVideoShPath = propConfig.getProperty("mergeVideoShPath");
			
			// create images
			backgroundImg = ImageIO.read(new File(backPicPath));
			normalIconImg = ImageIO.read(new URL(normalIconUrl));
			changeIconImg = ImageIO.read(new URL(changeIconUrl));
			changeHistIconImg = ImageIO.read(new URL(changeHistIconUrl));
			noDataIconImg = ImageIO.read(new URL(noDataIconUrl));
			noDataCHIconImg = ImageIO.read(new URL(noDataCHIconUrl));
			
			posTable = readPositions(posFilePath);
			if (posTable == null) {
				UtilSet.log(1, "fail to read positions");
				System.exit(1);
			}
			
			videoThread = new DailyRDAHMMVideoThread(propConfig);
			videoThread.start();
		} catch (Exception e) {
			e.printStackTrace();
		}		
	}
	
	protected Hashtable readPositions (String posFilePath) {
		try {
			Hashtable table = new Hashtable();
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
					table.put(name, new StationPos(name, x, y));
				}
				line = br.readLine();
				lineNum++;					
			}
			return table;
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * The makeVideo() service method
	 * @param dataSource
	 * @param preProcTreat
	 * @param resultUrl
	 * @param startDate
	 * @param endDate
	 * @return
	 */
	public String makeVideo(String dataSource, String preProcTreat, String resultUrl, 
							String startDate, String endDate) {
		dataSource = dataSource.toUpperCase();
		preProcTreat = preProcTreat.toUpperCase();
		Calendar calStartDate = UtilSet.getDateFromString(startDate);
		Calendar calEndDate = UtilSet.getDateFromString(endDate);
		int err = validateParameter(dataSource, preProcTreat, calStartDate, calEndDate);
		if (err == ERR_NO_ERROR) {
			String prefix = dataSource + '_' + preProcTreat + '_';
			// the start date of historical video is always 1994-01-01
			// if there is a historical video with a start date, set start date of the requested
			// video to this historical start date; other wise set it to default 1994-01-01
			String histStartDate;
			synchronized (propConfig) {
				histStartDate = propConfig.getProperty(prefix + PROP_NAME_SUFFIX_HVSD);
			}
			if (histStartDate == null || histStartDate.length() == 0)
				histStartDate = DEFAULT_HIST_VIDEO_START_DATE;
			Calendar calHistStartDate = UtilSet.getDateFromString(histStartDate);
			if (calStartDate.compareTo(calHistStartDate) > 0)
				calStartDate.setTimeInMillis(calHistStartDate.getTimeInMillis());
			
			// if a video with a later end date has been made, return the existed longer video
			String retEndDate = endDate;
			String lastEndDate;
			synchronized (propConfig) {
				lastEndDate = propConfig.getProperty(prefix + PROP_NAME_SUFFIX_LVED);
			}
			if (lastEndDate != null && lastEndDate.length() > 0) {
				Calendar calLastEndDate = UtilSet.getDateFromString(lastEndDate);
				if (calLastEndDate.compareTo(calEndDate) >= 0){
					retEndDate = lastEndDate;
					calEndDate.setTimeInMillis(calLastEndDate.getTimeInMillis());
				}
			}
			MakeVideoRequest req = new MakeVideoRequest(dataSource, preProcTreat, resultUrl, calStartDate, calEndDate);
			synchronized (requestList) {
				requestList.add(req);
				requestList.notifyAll();
			}
			
			return videoUrlPattern.replaceFirst("<FileName>", prefix + histStartDate + "to" + retEndDate + ".mp4");
		} else
			return serviceUrlPattern.replaceFirst("<FunctionAndPara>", "getErrorMsg?errNum=" + err);
	}
	
	protected int validateParameter(String dataSource, String preProcTreat, 
									Calendar calStartDate, Calendar calEndDate) {
		boolean found = false;
		Iterator<String> i = dataSources.listIterator();
		while (i.hasNext()) {
			if (i.next().equals(dataSource)) {
				found = true;
				break;
			}
		}
		if (!found) 
			return ERR_DATA_SOURCE;
		
		found = false;
		i = preProcTreats.listIterator();
		while (i.hasNext()) {
			if (i.next().equals(preProcTreat)) {
				found = true;
				break;
			}
		}
		if (!found)
			return ERR_TREATMENT;
		
		String prefix = dataSource + '_' + preProcTreat + '_';
		String histVideoStartDate;
		synchronized (propConfig) {
			histVideoStartDate = propConfig.getProperty(prefix + "histVideoStartDate");
		}
		if (histVideoStartDate == null || histVideoStartDate.length() == 0)
			histVideoStartDate = DEFAULT_HIST_VIDEO_START_DATE;
		Calendar calTmp = UtilSet.getDateFromString(histVideoStartDate);
		if (calStartDate.compareTo(calTmp) < 0)
			return ERR_START_DATE;
		
		calTmp.setTimeInMillis(System.currentTimeMillis());
		calTmp.set(Calendar.HOUR_OF_DAY, 23);
		calTmp.set(Calendar.MINUTE, 59);
		calTmp.set(Calendar.SECOND, 59);
		calTmp.set(Calendar.MILLISECOND, 999);
		if (calEndDate.compareTo(calStartDate) < 0 || calEndDate.compareTo(calTmp) > 0) {
			return ERR_END_DATE;
		}
		
		return ERR_NO_ERROR;
	}
	
	/**
	 * The getErrorMsg() service method
	 * @param errNum
	 * @return
	 */
	public String getErrorMsg(int errNum) {
		if (errNum >= 0 && errNum < errMsgs.length)
			return errMsgs[errNum];
		else
			return "Can't get error message: invalid error number!";
	}

}
