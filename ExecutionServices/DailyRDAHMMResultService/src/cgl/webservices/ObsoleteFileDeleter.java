package cgl.webservices;

import java.io.File;
import java.util.Iterator;
import java.util.LinkedList;

public class ObsoleteFileDeleter extends Thread {
	
	public static long DAY_MILISEC_COUNT = 86400*1000;
	
	/** a list of directories to find obsolete files */
	LinkedList<String> destDirList;
	/** regular-expression name pattern of the files to be deleted */
	String namePattern;
	/** how frequent should this thread be run, a value <= 0 means only run once */
	long runPeriod;
	/** threshold tells files of how old should be deleted, must be longer than a day */
	long threshold;
	/** whether obsolete directories should be deleted */
	boolean deleteDir;
	/** signal for stopping the thread, used together with elegantStop() */
	boolean toContinue;
	
	public ObsoleteFileDeleter(String namePattern, long runPeriod, long threshold) throws Exception{
		if (namePattern == null || namePattern.length() == 0) {
			throw new IllegalArgumentException("Name Pattern cannot be null or empty!");
		}
		
		// runPeriod <= 0 means only run once, otherwise must be more than a day 
		if ((runPeriod > 0 && runPeriod < DAY_MILISEC_COUNT) || threshold < DAY_MILISEC_COUNT) {
			throw new IllegalArgumentException("Run Period or Obsolete Threshold cannot be less than a day!");
		}
		
		this.namePattern = namePattern;
		this.runPeriod = runPeriod;
		this.threshold = threshold;
		this.deleteDir = true;
		this.toContinue = true;
		this.destDirList = new LinkedList<String>();
	}
	
	/** add the path of a directory to the directory list */
	public void addDirPath(String path) {
		if (path != null && path.length() > 0) {
			destDirList.add(path);
		}
	}
	
	public void run() {
		int interruptCount = 0;
		while (toContinue) {
			Iterator<String> iter = destDirList.listIterator(0);
			while (iter.hasNext()) {
				String dirPath = iter.next();
				File dirFile = new File(dirPath);
				if (dirFile.exists() && dirFile.isDirectory()) {
					File[] files = dirFile.listFiles();
					for (int i=0; i<files.length; i++) {
						File f = files[i];
						if (f.getName().matches(namePattern) && 
							System.currentTimeMillis() - f.lastModified() > threshold) {
							if (f.isFile()) {
								f.delete();
							} else if (deleteDir) {
								UtilSet.deleteDirectory(f);
							}
						}
					}
				}
			}
			
			if (runPeriod <= 0) {
				toContinue = false;
			} else {
				try {
					Thread.sleep(runPeriod);
					interruptCount = 0;
				} catch (InterruptedException e) {
					// it's OK to get interrupted when trying to sleep; we'll try to sleep again.
					e.printStackTrace();
					if (++interruptCount > 15)
						break;
				}
			}
		}
	}
	
	/** stop the thread elegantly */
	public void elegantStop(){
		toContinue = false;
	}

	public String getNamePattern() {
		return namePattern;
	}

	public void setNamePattern(String namePattern) throws Exception{
		if (namePattern == null || namePattern.length() == 0) {
			throw new IllegalArgumentException("Name Pattern cannot be null or empty!");
		}
		this.namePattern = namePattern;
	}

	public long getRunPeriod() {
		return runPeriod;
	}

	public void setRunPeriod(long runPeriod) throws Exception{
		if (runPeriod < DAY_MILISEC_COUNT) {
			throw new IllegalArgumentException("Run Period cannot be less than a day!");
		}
		this.runPeriod = runPeriod;
	}

	public long getThreshold() {
		return threshold;
	}

	public void setThreshold(long threshold) throws Exception{
		if (threshold < DAY_MILISEC_COUNT) {
			throw new IllegalArgumentException("Obsolete Threshold cannot be less than a day!");
		}
		this.threshold = threshold;
	}

	public boolean isDeleteDir() {
		return deleteDir;
	}

	public void setDeleteDir(boolean deleteDir) {
		this.deleteDir = deleteDir;
	}
}
