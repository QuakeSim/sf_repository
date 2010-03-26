package cgl.webservices;

import java.io.*;
import java.util.*;

import org.dom4j.*;
import org.dom4j.io.*;

public class DailyRDAHMMRunner {
	
	protected LinkedList<DailyRDAHMMStation> statoinList = null;
	protected HashMap<String, Integer> stateChangeNums = null;
	protected HashMap<String, String> modelDates = null;
	Document stationXmlDoc = null;
	boolean isStationXmlChanged = false;
	
	public DailyRDAHMMRunner() {
		try {
			statoinList = new LinkedList<DailyRDAHMMStation>();
			String xml;
			System.out.println("Parsing local RSS file...");
			BufferedReader br = new BufferedReader(new FileReader(DailyRDAHMMStation.stationXmlPath));
			StringBuffer sb = new StringBuffer();
			while (br.ready()) {
				sb.append(br.readLine());
			}
			xml = sb.toString();
			sb = null;
			br.close();
            
			SAXReader reader = new SAXReader();
			stationXmlDoc = reader.read(new StringReader(xml));
			isStationXmlChanged = false;
			Calendar yesterday = Calendar.getInstance();
			UtilSet.ndaysBeforeToday(yesterday, 1, yesterday);
			
			List l = stationXmlDoc.selectNodes("//station");
			int count = 0;
			Vector<String> vec = new Vector<String>(l.size());
			for (int i = 0; i < l.size(); i++) {
				Element e = (Element)l.get(i);
				if (e != null) {
					String stationId = e.selectSingleNode("id").getText().toLowerCase();
					float lat = Float.valueOf(e.selectSingleNode("latitude").getText());
					float lon = Float.valueOf(e.selectSingleNode("longitude").getText());
					DailyRDAHMMStation station = new DailyRDAHMMStation(stationId, lat, lon, this);
					Calendar lastEvalDate = Calendar.getInstance();
					lastEvalDate.setTimeInMillis(yesterday.getTimeInMillis());
					station.setLastEvalDateTime(lastEvalDate);
					Node modelStartDateNode = e.selectSingleNode("modelStartDate");
					if (modelStartDateNode == null) {
						isStationXmlChanged = true;
						modelStartDateNode = e.addElement("modelStartDate");
						modelStartDateNode.setText("");
					}
					String startDateStr = modelStartDateNode.getText();
					if (startDateStr.length() > 0) {
						station.setModelStartDate(UtilSet.getDateFromString(startDateStr));
					} else {
						startDateStr = getModelStartDateForStation(stationId);
						isStationXmlChanged = true;
						modelStartDateNode.setText(startDateStr);
						if (startDateStr.length() > 0) {
							station.setModelStartDate(UtilSet.getDateFromString(startDateStr));
						}
					}
					
					Node modelEndDateNode = e.selectSingleNode("modelEndDate");
					if (modelEndDateNode == null) {
						isStationXmlChanged = true;
						modelEndDateNode = e.addElement("modelEndDate");
						modelEndDateNode.setText("");
					}
					String endDateStr = modelEndDateNode.getText();
					// if endDateStr is empty, it will be found out later when the threads are running
					if (endDateStr.length() > 0) {
						station.setModelEndDate(UtilSet.getDateFromString(endDateStr));
					}
					
					boolean found = false;
					for (int j=0; j<vec.size(); j++) {
						if (vec.elementAt(j).equals(stationId)) {
							found = true;
							break;
						}
					}
					
					if (!found) {
						statoinList.add(station);
						vec.add(stationId);
						count++;
						//System.out.println("station added to station List in DailyRDAHMMRunner: " 
							//			+ listEle[0] + ":" + listEle[1] + "@" + listEle[2] + "," + listEle[3]);
					}
				}
            }
			System.out.println(count + " distinct statations are added in total");
			stateChangeNums = new HashMap<String, Integer>();		
		} catch (Exception ex3) {
			ex3.printStackTrace();
        }    
	}
	
	/**
	 * get the model start date for a station from the model start dates list file.
	 * the file is a list of lines like "widc 1997-10-01"
	 * @param stationId
	 * @return
	 */
	String getModelStartDateForStation(String stationId) {
		try {
			if (modelDates == null) {
				modelDates = new HashMap<String, String>();
				BufferedReader br = new BufferedReader(new FileReader(DailyRDAHMMStation.modelDatesFilePath));
				String line = br.readLine();
				while (line != null) {
					line = line.trim();
					int idx = line.indexOf(' ');
					String id = "";
					String date = "";
					if (idx >= 0) {
						id = line.substring(0, idx);
						date = line.substring(idx+1);
					} else {
						id = line;
					}
					modelDates.put(id, date);
					line = br.readLine();
				}
				br.close();
			}
			
			String result = modelDates.get(stationId);
			if (result == null) {
				result = "";
			}
			return result;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}
	
	public void setStationXmlModelStartDate(String stationId, String modelStartDate) {
		if (stationXmlDoc == null)
			return;
		List l = stationXmlDoc.selectNodes("//station");
		for (int i = 0; i < l.size(); i++) {
			Element e = (Element)l.get(i);
			if (e != null && e.selectSingleNode("id").getText().equals(stationId)) {
				e.selectSingleNode("modelStartDate").setText(modelStartDate);
				isStationXmlChanged = true;
				break;
			}
		}
		
	}
	
	public void setStationXmlModelEndDate(String stationId, String modelEndDate) {
		if (stationXmlDoc == null)
			return;
		List l = stationXmlDoc.selectNodes("//station");
		for (int i = 0; i < l.size(); i++) {
			Element e = (Element)l.get(i);
			if (e != null && e.selectSingleNode("id").getText().equals(stationId)) {
				e.selectSingleNode("modelEndDate").setText(modelEndDate);
				isStationXmlChanged = true;
				break;
			}
		}
		
	}
	
	public void saveStationXml() {
		if (stationXmlDoc == null || !isStationXmlChanged) {
			return;
		}
		
		try {
			OutputFormat format = OutputFormat.createPrettyPrint();
			FileWriter fw = new FileWriter(DailyRDAHMMStation.stationXmlPath);
			XMLWriter writer = new XMLWriter(fw, format);
			writer.write(stationXmlDoc);
			writer.flush();
			writer.close();
			fw.close();
			isStationXmlChanged = false;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		try {
			if (args.length < 1 || args[0].equalsIgnoreCase("--help")) {
				System.out.println("Usage: DailyRDAHMMRunner <daily RDAHMM property file name> [num_of_thread]");
			}
			// set up basic properties
			String propFileName = args[0];
			ClassLoader loader = ClassLoader.getSystemClassLoader();
			Properties prop = new Properties();
			prop.load(loader.getResourceAsStream(propFileName));
			DailyRDAHMMStation.initRdahmmProperties(prop);
			
			// create a runner, which contains the station list 
			DailyRDAHMMRunner runner = new DailyRDAHMMRunner();
			
			//start the threads
			int thread_num = 8;
			if (args.length > 1) {
				int n = Integer.valueOf(args[1]).intValue();
				if (n < 32)
					thread_num = n;
			}
			for (int i=0; i<thread_num; i++){			
				DailyRDAHMMThread t = new DailyRDAHMMThread(runner);
				new Thread(t).start();
			}	
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}				
	}

	public boolean isStationXmlChanged() {
		return isStationXmlChanged;
	}

	public void setStationXmlChanged(boolean isStationXmlChanged) {
		this.isStationXmlChanged = isStationXmlChanged;
	}
}
