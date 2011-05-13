package cgl.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.Random;
import java.util.TreeSet;
import java.util.Vector;

public class UtilSet {
	
	/**
	 * Execute a command
	 * 
	 * @param execStr
	 *            String command string
	 * @return String the information of execute result
	 */
	public static String exec(String execStr, File dir) {
		Runtime runtime = Runtime.getRuntime();// Get current runtime object
		String outInfo = ""; // execute error information
		try {
			String[] args = null;
			args = new String[] { "/bin/sh", "-c", execStr }; // execute

			Process proc = null;
			if (dir == null)
				proc = runtime.exec(args); // run another process to
			else
				proc = runtime.exec(args, null, dir);
			// execute command
			InputStream out = proc.getInputStream();
			BufferedReader br1 = new BufferedReader(new InputStreamReader(out));
			while (br1.readLine() != null) {
			}
			out.close();

			InputStream in = proc.getErrorStream(); // get error information
			BufferedReader br = new BufferedReader(new InputStreamReader(in));
			String line = "";
			while ((line = br.readLine()) != null) {
				outInfo = outInfo + line + "/n";
			}
			// System.out.println(outInfo);
			in.close();

			try { // check the result
				if (proc.waitFor() != 0) {
					System.err.println("exit value = " + proc.exitValue());
				}
			} catch (InterruptedException e) {
				System.err.print(e);
				e.printStackTrace();
				outInfo += " InterruptedException encountered when calling proc.waitFor()!";
			}

		} catch (IOException e) {
			System.out.println("exec error: " + e.getMessage());
			e.printStackTrace();
			outInfo += " IOException encountered!";
		}
		return outInfo;
	}
	
	public static String exec(String execStr) {
		return exec(execStr, null);
	}
	
	public static void log(int threadNum, String msg) {
		System.out.println("Thread " + threadNum + ": " + msg);
	}
	
	/**
	 * get the "2007-02-15" alike string form of the date
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateString(Calendar date) {
		StringBuffer sb = new StringBuffer();
		
		int year = date.get(Calendar.YEAR);
		for (int i=0; i<4-String.valueOf(year).length(); i++)
			sb.append('0');
		sb.append(year).append('-');
		
		int month = date.get(Calendar.MONTH) + 1;
		if (month < 10)
			sb.append('0');
		sb.append(month).append('-');
		
		int day = date.get(Calendar.DAY_OF_MONTH);
		if (day < 10)
			sb.append('0');
		sb.append(day);
		
		return sb.toString();
	}
	
	// get date from a string like 2007-03-08
	public static Calendar getDateFromString(String str) { 	
	  	Calendar ret = Calendar.getInstance();
	  	setDateByString(ret, str);  	
	  	return ret;
	}
	
	public static void setDateByString(Calendar theDate, String str) {
	  	str = str.trim();
		String year, month, day;
	  	int i1, i2;
	  	i1 = str.indexOf("-");
	  	i2 = str.indexOf("-", i1+1);
	  	year = str.substring(0, i1);
	  	month = str.substring(i1+1, i2);
	  	day = str.substring(i2+1);
	  	
	  	theDate.set(Calendar.YEAR, Integer.parseInt(year, 10));
	  	theDate.set(Calendar.MONTH, Integer.parseInt(month, 10)-1);
	  	theDate.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day, 10));
	  	theDate.set(Calendar.HOUR_OF_DAY, 12);
	  	theDate.set(Calendar.MINUTE, 0);
	  	theDate.set(Calendar.SECOND, 0);
	  	theDate.set(Calendar.MILLISECOND, 0);
	}
	
	/**
	 * Set the argument 'before' as the date n days before 'theDate'  
	 * */
	public static void nDaysBefore(Calendar before, int n, Calendar theDate) {
		before.setTimeInMillis(theDate.getTimeInMillis());
		before.set(Calendar.DATE, before.get(Calendar.DATE) - n);
	}
	
	/**
	 * find a line in file with substr as a substring 
	 * @param file
	 * @param substr
	 * @return
	 */
	public static String findLineInFile(File file, String substr) {
		if (!file.exists())
			return "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			String line = br.readLine();
			String result = "";
			while (line != null) {
				if (line.indexOf(substr) >= 0) {
					result = line;
					break;
				}
				line = br.readLine();
			}
			br.close();
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	/**
	 * call a web service through an http port
	 * @param url
	 * @return
	 */
	public static String callHttpService(String url) {
		try {
			URL serviceURL = new URL(url);
			URLConnection conn = serviceURL.openConnection();
			BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
			String htmlLine = in.readLine();
			StringBuffer sb = new StringBuffer();
			while (htmlLine != null) {
				sb.append(htmlLine).append(File.separator);
				htmlLine = in.readLine();
			}
			in.close();
			
            int idx = sb.indexOf("return>");
            if (idx < 0)
            	return "";
            int idx2 = sb.indexOf("</", idx);
            return sb.substring(idx + "return>".length(), idx2);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return "";
	}
	
	/**
     * delete a non-empty directory
     * @param success or not
     * @return
     */
    public static boolean deleteDirectory(File dir) {
    	if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					if (!deleteDirectory(files[i]))
						return false;
				} else {
					if (!files[i].delete())
						return false;
				}
			}
			return dir.delete();
		}
		return true;
    }
    
    /**
     * delete things under a directory
     * @param dir
     * @return
     */
    public static boolean deleteStuffInDir(File dir) {
    	if (dir.exists()) {
			File[] files = dir.listFiles();
			for (int i = 0; i < files.length; i++) {
				if (files[i].isDirectory()) {
					if (!deleteDirectory(files[i]))
						return false;
				} else {
					if (!files[i].delete())
						return false;
				}
			}
		}
    	return true;
    }
    
    /**
     * read the content of file located by "path" to "vec" 
     * @param path
     * @param vec
     */
    public static void readFileToVector(String path, Vector<String> vec) {
		if (path == null || path.length() == 0 || vec == null)
			return;
		try {
			vec.removeAllElements();
			String line;
			int count = 0;
			BufferedReader br = new BufferedReader(new FileReader(path));
			line = br.readLine();
			while (line != null) {
				if (line.length() > 0) {
					vec.add(line);
					count++;
				}
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
    
    /**
	 * set the calendar value of theDateTime according to a string like "2008-08-08T08:08:08"
	 * @param theDateTime
	 * @param str
	 */
	public static void setDateTimeByString(Calendar theDateTime, String str) {
	  	str = str.trim();
		String year, month, day;
	  	int i1, i2, iT;
	  	i1 = str.indexOf("-");
	  	i2 = str.indexOf("-", i1+1);
	  	iT = str.indexOf('T');
	  	year = str.substring(0, i1);
	  	month = str.substring(i1+1, i2);
	  	day = str.substring(i2+1, iT);
	  	
	  	theDateTime.set(Calendar.YEAR, Integer.parseInt(year, 10));
	  	theDateTime.set(Calendar.MONTH, Integer.parseInt(month, 10)-1);
	  	theDateTime.set(Calendar.DAY_OF_MONTH, Integer.parseInt(day, 10));
	  	
	  	i1 = str.indexOf(':', iT+1);
	  	i2 = str.indexOf(':', i1+1);
	  	
	  	theDateTime.set(Calendar.HOUR_OF_DAY, Integer.parseInt(str.substring(iT+1, i1)));
	  	theDateTime.set(Calendar.MINUTE, Integer.parseInt(str.substring(i1+1, i2)));
	  	theDateTime.set(Calendar.SECOND, Integer.parseInt(str.substring(i2+1)));
	  	theDateTime.set(Calendar.MILLISECOND, 0);
	}
	
	/** get date-time from a string like "2007-03-08T08:08:08" */
	public static Calendar getDateTimeFromString(String str) { 	
	  	Calendar ret = Calendar.getInstance();
	  	setDateTimeByString(ret, str);  	
	  	return ret;
	}
	
	/**
	 * get the "2007-02-15T13:23:22" alike string form of the date and time
	 * 
	 * @param date
	 * @return
	 */
	public static String getDateTimeString(Calendar date) {
		StringBuffer sb = new StringBuffer(getDateString(date));
		sb.append('T');
		
		if (date.get(Calendar.HOUR_OF_DAY) < 10)
			sb.append('0');
		sb.append(date.get(Calendar.HOUR_OF_DAY)).append(':');
		
		if (date.get(Calendar.MINUTE) < 10)
			sb.append('0');
		sb.append(date.get(Calendar.MINUTE)).append(':');
		
		if (date.get(Calendar.SECOND) < 10)
			sb.append('0');
		sb.append(date.get(Calendar.SECOND));
		
		return sb.toString();
	}
	
	/**
	 * This is for test anything handy
	 * @param args
	 */
	public static void main(String[] args) {
		/*
		Calendar cal = Calendar.getInstance();
		System.out.println(cal.toString());
		
		String s = "SOPAC_FILL_2005-06-09to2005-09-08.2010-10-03.kml";
		String pat = ".*";
		System.out.println(s.matches(pat)); */
		/*
		DailyRdahmmResultService service = new DailyRdahmmResultService();
		String res = service.calcStationColors(args[0], args[1]);
		System.out.println(res);
		*/
		
		Integer[] a = new Integer[20];
		TreeSet<Integer> ts = new TreeSet<Integer>();
		Random r = new Random();
		for (int i=0; i<a.length; i++) {
			ts.add(r.nextInt(100));
		}
		Integer[] res = ts.toArray(a);
		for (int i=0; i<res.length; i++) {
			System.out.print(res[i] + ", ");
		}
		
	}
}
