package cgl.webservices;

import java.util.Calendar;

public class MakeVideoRequest {
	private String dataSource;
	private String preProcessTreat;
	private String resultUrl;
	private Calendar calStartDate;
	private Calendar calEndDate;
	
	public MakeVideoRequest(String dataSource, String preProcTreat, String resultUrl, 
								Calendar calStartDate, Calendar calEndDate) {
		this.dataSource = dataSource;
		this.preProcessTreat = preProcTreat;
		this.resultUrl = resultUrl;
		this.calStartDate = calStartDate;
		this.calEndDate = calEndDate;
	}

	public String getDataSource() {
		return dataSource;
	}

	public String getPreProcessTreat() {
		return preProcessTreat;
	}

	public String getResultUrl() {
		return resultUrl;
	}

	public Calendar getCalStartDate() {
		return calStartDate;
	}

	public Calendar getCalEndDate() {
		return calEndDate;
	}
	
	public String toString() {
		StringBuffer sb = new StringBuffer();
		sb.append("MakeVideoRequest--").append("data source:").append(dataSource).append(";\n");
		sb.append("  pre-processing treatment:").append(preProcessTreat).append(";\n");
		sb.append("  start date:").append(UtilSet.getDateString(calStartDate)).append(";\n");
		sb.append("  end date:").append(UtilSet.getDateString(calEndDate)).append(";\n");
		sb.append("  URL:").append(resultUrl).append('\n');
		
		return sb.toString();
	}
}
