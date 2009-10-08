package cgl.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.util.Calendar;

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
	
	/**
	 * get the "2007.0215" alike string form of the date
	 * 
	 * @param date
	 * @return
	 */
	public static String getFloatDateString(Calendar date) {
		StringBuffer sb = new StringBuffer();
		
		int year = date.get(Calendar.YEAR);
		for (int i=0; i<4-String.valueOf(year).length(); i++)
			sb.append('0');
		sb.append(year).append('.');
		
		int month = date.get(Calendar.MONTH) + 1;
		if (month < 10)
			sb.append('0');
		sb.append(month);
		
		int day = date.get(Calendar.DAY_OF_MONTH);
		if (day < 10)
			sb.append('0');
		sb.append(day);
		
		return sb.toString();
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
	
	/** get date from a string like 2007-03-08 */
	public static Calendar getDateFromString(String str) { 	
	  	Calendar ret = Calendar.getInstance();
	  	setDateByString(ret, str);  	
	  	return ret;
	}
	
	/**
	 * set the calendar value of theDate according to a string like "2008-08-08"
	 * @param theDate
	 * @param str
	 */
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
	public static Calendar getTimeDateFromString(String str) { 	
	  	Calendar ret = Calendar.getInstance();
	  	setDateTimeByString(ret, str);  	
	  	return ret;
	}
	
	/**
	 * Get the date part of a string like "2007-03-08T08:08:08"
	 * @param str
	 * @return
	 */
	public static String getDatePart(String str) {
		int idx = str.indexOf('T');
		if (idx < 0)
			return str;
		else
			return str.substring(0, idx);
	}
	
	/**
	 * Get the time part of a string like "2007-03-08T08:08:08"
	 * @param str
	 * @return
	 */
	public static String getTimePart(String str) {
		int idx = str.indexOf('T');
		if (idx < 0)
			return str;
		else
			return str.substring(idx+1);
	}
	
	/**
	 * get the date objects representing a date that is "ndays" days "ago" from today 
	 * 
	 * @param today
	 * @param ndays
	 * @return
	 */
	public static Calendar ndaysBeforeToday(Calendar today, int ndays, Calendar daysBefore){
		daysBefore.setTimeInMillis(today.getTimeInMillis());
		daysBefore.set(Calendar.DATE, today.get(Calendar.DATE) - ndays);
		
		return daysBefore;
	}
	
	/**
	 * rename the file of srcPath to newPath
	 * @param srcPath
	 * @param newPath
	 */
	public static void renameFile(String srcPath, String newPath) {
		System.out.println("renameFile -- srcPath : " + srcPath);
		System.out.println("renameFile -- newPath : " + newPath);
		
		if (srcPath == null || newPath == null)
			return;
	
		File srcFile = new File(srcPath);
		if (!srcFile.exists())
			return;

		File newPathFile = new File(newPath);
		// if file of newPath exists, delete it
		if (newPathFile.exists()) {
			if (!newPathFile.delete()) {
				System.out.println("Failed to delete the old RDAHMM result file!");
				return;
			}
		}

		// rename the srcFile
		if (!srcFile.renameTo(newPathFile)) {
			System.out.println("Failed to rename the temporary file to the destined xml file!");
		}
	}
	
	/**
	 * concatenate two files into a single file of path targetPath
	 */
	public static void catTwoFiles(String path1, String path2, String targetPath) {
		String res = exec("cat " + path1 + " " +  path2 + " > " + targetPath);
		if (res.length() > 0)
			System.out.println("result of cat two files " + path1 + " " + path2 + ":" + res);
	}
	
	/**
     * This copies a file to a new place on the file system.
     */
    public static void copyFileToFile(File sourceFile,File destFile) 
		  throws Exception {
		  InputStream in=new FileInputStream(sourceFile);
		  OutputStream out=new FileOutputStream(destFile);
		  byte[] buf=new byte[1024];
		  int length;
		  while((length=in.read(buf))>0) {
				out.write(buf,0,length);
		  }
		  in.close();
		  out.close();
    }
    
    /**
     * get the file name part of a path
     * @param path
     * @return
     */
    public static String getFileNamePart(String path) {
    	int idx = path.lastIndexOf(File.separatorChar);
    	return path.substring(idx + 1);
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
		}
		return dir.delete();
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
}
