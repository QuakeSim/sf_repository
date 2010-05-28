package cgl.quakesim.simplex;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import java.net.URL;

public class UnavcoVelParser {

	/**
	 * @param args
	 * @throws IOException 
	 */
	
	String vel;
	
	/*
	public static void main(String[] args) throws IOException {
		// TODO Auto-generated method stub
		
		
		String snf01 = "ftp://data-out.unavco.org/pub/products/velocity/pbo.final_snf01.vel";
		String igs05 = "ftp://data-out.unavco.org/pub/products/velocity/pbo.final_igs05.vel";
		
		UnavcoVelParser t = new UnavcoVelParser();
		
		t.getFile(snf01);
		System.out.println(t.getStationVelocity("P100"));
		System.out.println("[Finished]");	
	}
	*/
	
	
	public String getFile(String url) throws IOException {
		
		vel = "";
		String snf01 = "ftp://data-out.unavco.org/pub/products/velocity/pbo.final_snf01.vel";
		String igs05 = "ftp://data-out.unavco.org/pub/products/velocity/pbo.final_igs05.vel";
			
		URL url_vel = new URL(url);
		
		BufferedReader in = new BufferedReader(new InputStreamReader(url_vel.openStream()));
		String line;
		while ((line = in.readLine()) != null) {
			
			
			while (line.contains("  ")) {
			line = line.replaceAll("  ", " ");			
			// System.out.println(line);			
			}
			
			line = line.trim();
			vel += (line + "\n");			
		}
		return vel;
	}
	
	private String getStationline(String dotnumber){
		// System.out.println(vel);
		String[] line = vel.split("\n");
		String result = null;
		
		for (int nA = 0 ; nA < line.length ; nA++) {
			if (line[nA].contains("Release") && (result == null))
				result = (line[nA]+"\n");
			
			String[] elements = line[nA].split(" ");
			
			for (int nB = 0 ; nB < elements.length ; nB++) {
			
				if (elements[nB].compareToIgnoreCase(dotnumber) == 0) {
					
					if (result == null)
						result = (line[nA]+"\n");
					result += (line[nA]+"\n");
				}
			}
			
		}	
		
		return result;
	}
	
	private String getNeu(String line) {
		
		String neu =null;
		String date = "0000-00-00T00:00:00";
		
		if (line == null)
			return null;
		
		// System.out.println(line);
		String[] twoline = line.split("\n");
		if (twoline.length == 2) {
			
			twoline[0] = twoline[0].trim();
			String[] s = twoline[0].split(" ");
			
			System.out.println("[getNew] " + s[s.length-1]);
			
			date = null;
			date = s[s.length-1].substring(0, 4) + "-";
			date += s[s.length-1].substring(4, 6) + "-";
			date += s[s.length-1].substring(6, 8) + "T";
			date += s[s.length-1].substring(8, 10) + ":";
			date += s[s.length-1].substring(10, 12) + ":";
			date += s[s.length-1].substring(12, 14);
			
			
			line = twoline[1];
		}
						
		String[] elements = line.split(" ");
		
		int n = 20;
		int e = 21;
		int u = 22;
		int nn = 23;
		int ee = 24;
		int uu = 25;
		
		neu = (elements[0].toLowerCase() + " ");
		neu += date + " ";
		neu += (elements[n] + " ");
		neu += (elements[e] + " ");
		neu += (elements[u] + " ");
		neu += (elements[nn] + " ");
		neu += (elements[ee] + " ");
		neu += (elements[uu] + " ");
		
		neu = neu.trim();
		
		return neu;
	}
	
	public String getStationVelocity(String dotnumber) {
		
		return getNeu(getStationline(dotnumber));
	}
}





