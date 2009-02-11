package cgl.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
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
}
