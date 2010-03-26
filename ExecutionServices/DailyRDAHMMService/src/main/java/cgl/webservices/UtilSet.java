package cgl.webservices;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.LineNumberReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.URL;
import java.net.URLConnection;
import java.util.Calendar;
import java.util.StringTokenizer;

import org.apache.tools.ant.Project;
import org.apache.tools.ant.taskdefs.ExecTask;


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
	public static Calendar getDateTimeFromString(String str) { 	
	  	Calendar ret = Calendar.getInstance();
	  	setDateTimeByString(ret, str);  	
	  	return ret;
	}
	
	/**
	 * Get the date part of a string like "2007-03-08T08:08:08"
	 * @param dateTimeStr
	 * @return
	 */
	public static String getDatePart(String dateTimeStr) {
		int idx = dateTimeStr.indexOf('T');
		if (idx < 0)
			return dateTimeStr;
		else
			return dateTimeStr.substring(0, idx);
	}
	
	/**
	 * Get the time part of a string like "2007-03-08T08:08:08"
	 * @param dateTimeStr
	 * @return
	 */
	public static String getTimePart(String dateTimeStr) {
		int idx = dateTimeStr.indexOf('T');
		if (idx < 0)
			return dateTimeStr;
		else
			return dateTimeStr.substring(idx+1);
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
				System.out.println("Failed to delete the old file " + newPath);
				return;
			}
		}

		// rename the srcFile
		if (!srcFile.renameTo(newPathFile)) {
			System.out.println("Failed to rename the source file to the destined file!");
		}
	}
	
	/**
	 * concatenate two files into a single file of path targetPath
	 */
	public static void catTwoFiles(String path1, String path2, String targetPath) throws Exception{
		InputStream in1 = new FileInputStream(path1);
		OutputStream out = new FileOutputStream(targetPath);
		byte[] buf = new byte[1024];
		int length;
		while ((length = in1.read(buf)) > 0) {
			out.write(buf, 0, length);
		}
		in1.close();
		
		InputStream in2 = new FileInputStream(path2);
		while ((length = in2.read(buf)) > 0) {
			out.write(buf, 0, length);
		}
		in2.close();
		out.flush();
		out.close();
	}
	
	/**
	 * paste colFilePath as a new column to srcFilePath, and save results in destFilePath
	 * @param srcFilePath
	 * @param colFilePath
	 * @param destFilePath
	 */
	public static void pasteFileAsColumn(String srcFilePath, String colFilePath, 
			String destFilePath, char delimiter) throws Exception {
		BufferedReader brSrc = new BufferedReader(new FileReader(srcFilePath));
		BufferedReader brCol = new BufferedReader(new FileReader(colFilePath));
		PrintWriter pwDest = new PrintWriter(new FileWriter(destFilePath));
		String lineSrc = brSrc.readLine();
		String lineCol = brCol.readLine();
		while(lineSrc != null || lineCol != null) {
			String lineDest = "";
			if (lineSrc != null)
				lineDest += lineSrc;
			lineDest += delimiter;
			if (lineCol != null)
				lineDest += lineCol;
			pwDest.println(lineDest);
			lineSrc = brSrc.readLine();
			lineCol = brCol.readLine();			
		}
		brSrc.close();
		brCol.close();
		pwDest.flush();
		pwDest.close();
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
     * Another famous method that I googled. This downloads contents
     * from the given URL to a local file.
     */ 
    public static void copyUrlToFile(URL inputFileUrl,String destFile) throws Exception {
		  URLConnection uconn=inputFileUrl.openConnection();
		  InputStream in=inputFileUrl.openStream();
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
     * get the last line of a file
     * @param path
     * @return
     */
    public static String getLastLineOfFile(String path) {
    	String res = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			String line = br.readLine();
			while (line != null) {
				res = line;
				line = br.readLine();
			}
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
    }
    
    /**
     * get the first line of a file
     * @param path
     * @return
     */
    public static String getFirstLineOfFile(String path) {
    	String res = null;
		try {
			BufferedReader br = new BufferedReader(new FileReader(path));
			res = br.readLine();
			br.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return res;
    }

    /**
     * extract certain columns from srcPath, and save the extracted columns to destPath.
     * column index starts from 0
     * @param srcPath
     * @param destPath
     * @param colStartIdx
     * @param colEndIdx
     * @return
     */
    public static boolean extractColumnsFromFile(String srcPath, String destPath, int colStartIdx, int colEndIdx) {
    	String tmpDestPath = destPath;
		if (srcPath.equals(destPath)) {
			tmpDestPath = destPath + ".tmp";
		}
    	
    	try {
    		BufferedReader br = new BufferedReader(new FileReader(srcPath));
			PrintWriter printer = new PrintWriter(new FileWriter(tmpDestPath));
			String line = br.readLine();
			while(line != null) {
				 StringTokenizer st = new StringTokenizer(line, " \t");
				 String newLine="";
				 int count = st.countTokens();
				 for (int j=0; j<count; j++) {
					  String temp = st.nextToken();
					  if(j >= colStartIdx && j <= colEndIdx){
							newLine += temp + ' ';
							if (j == colEndIdx) {
								break;
							}
					  }
				 }
				 printer.println(newLine.trim());
				 line = br.readLine();
			}
			br.close();
			printer.flush();
			printer.close();
    	} catch (Exception e) {
    		e.printStackTrace();
    		return false;
    	}
    	
    	// replace the file of srcPath with tmpDestPath if srcPath and destPath are the same
		if (srcPath.equals(destPath)) {
			File srcFile = new File(srcPath);
			if (!srcFile.delete()) {
				System.out.println("Failed to extract columns from " + srcPath + " and save them to "
									+ destPath + ": can't delete original file!");
				return false;
			}
			File tmpFile = new File(tmpDestPath);
			if (!tmpFile.renameTo(srcFile)) {
				System.out.println("Failed to extract columns from " + srcPath + " and save them to "
									+ destPath + ": can't rename tmp file to original file name!");
				return false;
			}
		}
    	
    	return true;
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
     * get the directory part of a path
     * @param path
     * @return
     */
    public static String getDirPart(String path) {
    	int idx = path.lastIndexOf(File.separatorChar);
    	if (idx >= 0) {
    		return path.substring(0, idx);
    	} else {
    		return "";
    	}
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
     * count the number of lines in a file
     * @param filePath
     * @return
     */
    public static int countLinesInFile(String filePath) {
		int count = 0;
		try {
			BufferedReader br = new LineNumberReader(new FileReader(filePath));
			String line = br.readLine();
			while (line != null) {
				count++;
				line = br.readLine();

			}
			br.close();
		} catch (Exception ex) {
			ex.printStackTrace();
			count = -1;
		}

		return count;
	}
    
    /**
	 * execute a command line program from ant
	 * 
	 * @param program
	 * @param args
	 * @param workDir
	 * @param inputPath
	 * @param outputPath
	 * @param errPath
	 */
    public static void antExecute(String program, String[] args, String workDir, String inputPath, String inputString, String outputPath, String errPath) {
    	File fWorkDir = null;
    	if (workDir != null && workDir.length() > 0)
    		fWorkDir = new File (workDir);
    	
    	Project antproject = new Project();
		antproject.init();
		if (fWorkDir != null)
			antproject.setBaseDir(fWorkDir);
		
		ExecTask execTask = new ExecTask();
		execTask.init();
		execTask.setProject(antproject);
    	if (fWorkDir != null)
    		execTask.setDir(new File (workDir));
		execTask.setExecutable(program);
		
		for (int i=0; i<args.length; i++) {
			execTask.createArg().setValue(args[i]);
		}
		
		if (inputPath != null && inputPath.length() > 0)
			execTask.setInput(new File(inputPath));
		
		if (inputString != null)
			execTask.setInputString(inputString);
		
		if (outputPath != null && outputPath.length() > 0)
			execTask.setOutput(new File(outputPath));
		
		if (errPath != null && errPath.length() > 0)
			execTask.setError(new File(errPath));
		
		execTask.execute();
    }
    
    /**
	 * get the program name from a command line such as "cp ./test.txt ./test2.txt"
	 * @param cmdLine
	 * @return
	 */
	public static String getProgFromCmdLine(String cmdLine) {
		int idx = cmdLine.indexOf(' ');
		if (idx < 0)
			return cmdLine;
		else
			return cmdLine.substring(0, idx);
	}
	
	/**
	 * get the arguments array from a command line such as "cp ./test.txt ./test2.txt"
	 * @param cmdLine
	 * @return
	 */
	public static String[] getArgsFromCmdLine(String cmdLine) {
		StringTokenizer st = new StringTokenizer(cmdLine, " ");
		int argNum = st.countTokens() - 1;
		if (argNum < 1) {
			String[] args = {};
			return args;
		}
		
		String[] args = new String[argNum];
		// the first token is the program name
		st.nextToken();
		int idx = 0;
		while (st.hasMoreTokens()) {
			args[idx++] = st.nextToken();
		}
		return args;
	}
	
	/**
	 * read the content of a file to a string and return the string
	 * @param file
	 * @return
	 */
	public static String readFileContentAsString (File file) {
		if (!file.exists() || file.isDirectory() || file.length() == 0)
			return "";
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuffer bRes = new StringBuffer();
			String line = br.readLine();
			while (line != null) {
				bRes.append(line).append('\n');
				line = br.readLine();
			}
			br.close();
			return bRes.toString();
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
}
