package cgl.webservices.geofest;

//Java base imports
import java.net.*;

//Ant imports
import org.apache.tools.ant.Task;
import org.apache.tools.ant.BuildException;

public class QueueTask extends Task {

	 String queueName;
	 String message;
	 String queueServiceUrl;
	 QueueService queueService;

	 public QueueTask() {
	 }

	 public void execute() {
		  if(queueName==null || message==null) {
				throw new BuildException("Queue or message not set");
		  }
		  
		  System.out.println("Params:"+queueName+" "+message);

		  try {
				queueService=new QueueServiceServiceLocator().getQueueExec(new URL(queueServiceUrl));
				queueService.createQueue(queueName);
				queueService.writeQueueMessage(queueName,message);
		  }
		  catch (Exception ex) {
				ex.printStackTrace();
		  }
	 }

	 public void setQueueServiceUrl(String queueServiceUrl) {
		  this.queueServiceUrl=queueServiceUrl;
	 }

	 public void setQueueName(String queueName) {
		  this.queueName=queueName;
	 }
	 
	 public void setMessage(String message) {
		  this.message=message;
	 }	 
}