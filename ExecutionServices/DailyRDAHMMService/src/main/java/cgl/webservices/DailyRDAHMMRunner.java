package cgl.webservices;

import java.io.*;
import java.util.*;
import org.dom4j.*;
import org.dom4j.io.*;

public class DailyRDAHMMRunner {
	
	protected Vector<String[]> statoinList = new Vector<String[]>();
	protected int numOfStates = 7;
	protected TreeMap<String, Integer> stateChangeNums;
	
	public DailyRDAHMMRunner() {
		//get the station list
		Calendar yesterday = Calendar.getInstance();
		String xml;
		try {
			System.out.println("Parsing local RSS file...");
			BufferedReader br = new BufferedReader(new FileReader("stations-rss-new.xml"));
			StringBuffer sb = new StringBuffer();
			while (br.ready()) {
				sb.append(br.readLine());
			}
			xml = sb.toString();
			sb = null;
            
			SAXReader reader = new SAXReader();
			Document document = reader.read(new StringReader(xml));
            
			yesterday.setTimeInMillis(System.currentTimeMillis() - 86400000);
            
			List l = document.selectNodes("//station");
			Vector<String> vec = new Vector<String>();
			for (int i = 0; i < l.size(); i++) {
				Element e = (Element)l.get(i);
				if (e != null) {
					// System.out.println(e.selectSingleNode("id").getText());
					String[] listEle = new String[4];
					listEle[0] = e.selectSingleNode("id").getText().toLowerCase();
					listEle[1] = UtilSet.getDateString(yesterday);
					listEle[2] = e.selectSingleNode("latitude").getText();
					listEle[3] = e.selectSingleNode("longitude").getText();
					boolean found = false;
					for (int j=0; j<vec.size(); j++) {
						if (vec.elementAt(j).equals(listEle[0])) {
							found = true;
							break;
						}
					}
					
					if (!found) {
						statoinList.add(listEle);
						vec.add(listEle[0]);
						System.out.println("station added to station List in DailyRDAHMMRunner: " 
										+ listEle[0] + ":" + listEle[1] + "@" + listEle[2] + "," + listEle[3]);
					}
				}
            }
			stateChangeNums = new TreeMap<String, Integer>();
		} catch (Exception ex3) {
			ex3.printStackTrace();
        }    
	}
	
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		// TODO Auto-generated method stub
		// runner contains the station list 
		DailyRDAHMMRunner runner = new DailyRDAHMMRunner();
		
		//start the threads
		int thread_num = 8;
		
		try {
			if (args.length > 0 && args[0].equalsIgnoreCase("help")) {
				System.out.println("Usage: DailyRDAHMMRunner [num_of_thread [num_of_states [model_start_date " +
									"[model_end_date [eval_start_date] ] ] ] ]");
			}
			
			int n = Integer.valueOf(args[0]).intValue();
			if (n < 32)
				thread_num = n;			
			runner.numOfStates = Integer.valueOf(args[1]).intValue();
			
			if (args.length > 2)
				DailyRDAHMMThread.modelStartDate = args[2];
			
			if (args.length > 3)
				DailyRDAHMMThread.modelEndDate = args[3];
			
			if (args.length > 4)
				DailyRDAHMMThread.evalStartDate = args[4];
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}		
		
		for (int i=0; i<thread_num; i++){			
			DailyRDAHMMThread t = new DailyRDAHMMThread(runner);
			new Thread(t).start();
		}		
	}
}
