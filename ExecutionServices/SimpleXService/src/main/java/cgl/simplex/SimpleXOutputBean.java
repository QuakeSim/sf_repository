package cgl.webservices.simplex;
/**
 * This bean holds the metadata of a simplex run, including urls to output.
 */
public class SimpleXOutputBean {

	// Member property fields
	String projectName;

	String jobUIDStamp;

	String inputUrl;

	String outputUrl;

	String logUrl;

	String faultUrl;

	boolean view;

	public void setView(boolean tmp_str) {
		this.view = tmp_str;
	}

	public boolean getView() {
		return view;
	}

	public void setJobUIDStamp(String jobUIDStamp) {
		this.jobUIDStamp = jobUIDStamp;
	}

	public String getJobUIDStamp() {
		return jobUIDStamp;
	}

	public void setProjectName(String projectName) {
		this.projectName = projectName;
	}

	public String getProjectName() {
		return projectName;
	}

	public String getInputUrl() {
		return inputUrl;
	}

	public void setInputUrl(String inputUrl) {
		this.inputUrl = inputUrl;
	}

	public String getFaultUrl() {
		return faultUrl;
	}

	public void setFaultUrl(String faultUrl) {
		this.faultUrl = faultUrl;
	}

	public void setOutputUrl(String outputUrl) {
		this.outputUrl = outputUrl;
	}

	public String getOutputUrl() {
		return outputUrl;
	}

	public void setLogUrl(String logUrl) {
		this.logUrl = logUrl;
	}

	public String getLogUrl() {
		return logUrl;
	}
}
