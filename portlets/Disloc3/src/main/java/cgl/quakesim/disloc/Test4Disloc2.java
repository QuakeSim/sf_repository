package cgl.quakesim.disloc;

import java.io.IOException;

import javax.faces.component.html.HtmlDataTable;
import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;

import junit.framework.Test;
import junit.framework.TestSuite;


import org.apache.cactus.ServletTestCase;
import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

import com.gargoylesoftware.htmlunit.BrowserVersion;



public class Test4Disloc2 extends ServletTestCase
{
	
   private JSFSession jsfsession;
   private JSFClientSession jsfclient;
   private JSFServerSession jsfserver;


   public static Test suite()
   {
	   return new TestSuite(Test4Disloc2.class);
   }
   
   private DislocBean getDislocBean(JSFServerSession jsfserver)
   {
	   return (DislocBean) jsfserver.getManagedBeanValue("#{DislocBean2}");	   
   }
   
   private void setDislocBean(JSFServerSession jsfserver, DislocBean sb)
   {	   
	   FacesContext f = jsfserver.getFacesContext();
	   ValueBinding b = f.getApplication().createValueBinding("#{DislocBean2}");	   
	   b.setValue(f, sb);
   }
   
   
   // the main page of portlet Disloc2   

   public void testLoadProject_NewProject() throws IOException
   {   
	   // emptyFetchResults();
	   
	   WebClientSpec wcs = new WebClientSpec("/LoadProject.faces", BrowserVersion.FIREFOX_3);
	   this.jsfsession = new JSFSession(wcs);
	   this.jsfclient = this.jsfsession.getJSFClientSession();
	   this.jsfserver = this.jsfsession.getJSFServerSession();
	   
	   
	   System.out.println("\ntestLoadProject_NewProject() started... for " + this.jsfserver.getCurrentViewID() + "\n");
	   assertEquals("/LoadProject.jsp", jsfserver.getCurrentViewID());
	   
	   DislocBean dsb = getDislocBean(jsfserver);
	   assertEquals("Project Archive", jsfserver.getComponentValue("lptv1"));
	   
	   System.out.println(dsb.getKmlProjectFile());
	   System.out.println(dsb.getMyProjectNameList().size());
	   
	   if(dsb.getMyProjectNameList().isEmpty())
	   {		   
		   assertTrue(jsfserver.findComponent("lptv11").isRendered());
		   assertEquals("You don't have any archived projects.", jsfserver.getComponentValue("lptv11"));
		   assertFalse(jsfserver.findComponent("lptv12").isRendered());
		   
		   
		   assertEquals("Project Name:", jsfserver.getComponentValue("lpj_projectname"));
		   
		   
		   jsfclient.setValue("projectName", "dummyproject1");
		   
		   jsfclient.click("lpj_makeselection"); // a button 'Make Selection' running 'NewProjectThenEditProject'
		   
		   
		   // Now, we are in the EditProject.jsp		   
		   
		   assertEquals("Project Input", jsfserver.getComponentValue("epjf1"));
		   assertEquals("", jsfserver.getComponentValue("message"));
		   assertEquals("<p>Create your geometry out of observation points and faults.<br/>The project origin will be the starting lat/lon of the first fault.</p>", jsfserver.getComponentValue("epjf1_text1"));
		   
		   
		   // testing for the DashboardPanel.jsp
		   
		   assertEquals("<b>Project Name: " + dsb.getProjectName() + " </b><br>", jsfserver.getComponentValue("stuff1"));
		   assertEquals("<b>Project Origin (lat/lon):</b> (" + dsb.getCurrentParams().getOriginLat() + ", " + dsb.getCurrentParams().getOriginLon() + ") <br>", jsfserver.getComponentValue("stuffe3io4"));
		   assertEquals("<b>Observation Style: " + dsb.getCurrentParams().getObservationPointStyle() + " </b><br>", jsfserver.getComponentValue("stufw3f1"));
		   
		   

		   // Let's click to set an observation style.
		   
		   jsfclient.click("item0w3"); // Set Observation Style: Choose between grid and scatter points.
		   jsfclient.click("button1"); // Make Selection
		   
		   
		   // Now, we can see the ObsvStyle.jsp
		   
		   assertEquals("<b>Select Sites:</b>Click to choose scatter point.", jsfserver.getComponentValue("clrr33asz3"));
		   
		   
		   // choose a value for an observation style.		   
		   		   
		   jsfclient.click("item02121"); // Rectangular grid of observation points
		   
		   /*
		    * usesGridPoints=true;
		    * currentParams.setObservationPointStyle(1);
		    * 
		    * */
		   
		   
		   // jsfclient.click("item0332"); // Scattered observation points.
		   
		   /*
		    * usesGridPoints=false;
		    * currentParams.setObservationPointStyle(0);
		    * 
		    * */
		   
		   
		   jsfclient.click("chooseAStyle"); // a button 'Choose Style' running toggleSetObsvStyle
		   
		   
		   
		   dsb = getDislocBean(jsfserver);
		   
		   assertTrue(dsb.getUsesGridPoints());
		   assertEquals(dsb.getCurrentParams().getObservationPointStyle(), 1);
		   
		   
		   
		   
		   
		   // dsb = getDislocBean(jsfserver);
		   
		   // assertFalse(dsb.getUsesGridPoints());
		   // assertEquals(dsb.getCurrentParams().getObservationPointStyle(), 0);
		   		   
		   
		   
		   
		   jsfclient.click("item4"); // Add Fault from DB: Click to select a fault segment from the database.
		   jsfclient.click("button1"); // Make Selection
		   
		   // Now, we can see a FaultSearchOptionPanel.jsp
		   
		   System.out.println("\nwe are in " + this.jsfserver.getCurrentViewID() + "\n");
		   
		   
		   
		   assertTrue(jsfserver.findComponent("faultselection").isRendered());
		   // assertEquals("<b>Select Sites:</b>Click to choose scatter point.", jsfserver.getComponentValue("faultselection"));
		   assertEquals("<b>Fault Database Selection</b><br><br>", jsfserver.getComponentValue("stuff42"));
		   assertEquals("You may select faults from the Fault Database using author search, latitude/longitude bounding box, or by viewing the master list (long).<br><br>", jsfserver.getComponentValue("stuff43"));
		   assertEquals("Please choose a radio button and click <b>Select</b>.<br><br>", jsfserver.getComponentValue("pg211"));
		   
		   
		   // To open a FaultDisplaySearchResultsPanel.jsp		   
		   jsfclient.click("item04"); // View all faults
		   jsfclient.click("button122"); // a button 'Make Selection"
		   
		   
		   
		   // Simulate clicking 3rd fault.
		   dsb.getCurrentFault().setFaultName("Antelope Valley@N/A%50");
		   
		   jsfclient.click("SelectFaultDBEntry"); // a button 'SelectFaultDBEntry' to open 'FaultParamPanel.jsp'
		   
		   jsfclient.click("domath"); // a button 'Do Math'
		   jsfclient.click("addfault"); // a button 'Set Values' running 'toggleAddFaultForProject()'
		   
		   // Now, we should be able to see 'ProjectComponentPanel.jsp'
		   
		   assertTrue(jsfserver.findComponent("stuff77").isRendered());
		   assertEquals("Current Project Components", jsfserver.getComponentValue("stuff78"));
		   
		   // Also, 'DislocSubmitPanel.jsp'
		   
		   jsfclient.click("rundisloc");
		   
		   
		   
		   
		   // check if the project is inserted into a projectlist
		   assertEquals("dummyproject1", ((SelectItem) dsb.getMyProjectNameList().get(0)).getValue());
		   
		   System.out.println("\n\nfinished\n");
	   }
	   
	   else
		   System.out.println("This test is only for an empty project list");
		   
   }
   
   public void testLoadProject_SelectProject() throws IOException
   {
	   WebClientSpec wcs = new WebClientSpec("/LoadProject.faces", BrowserVersion.FIREFOX_3);
	   this.jsfsession = new JSFSession(wcs);
	   this.jsfclient = this.jsfsession.getJSFClientSession();
	   this.jsfserver = this.jsfsession.getJSFServerSession();
	   
	   System.out.println("\ntestLoadProject_SelectProject() started... for " + this.jsfserver.getCurrentViewID() + "\n");
	   assertEquals("/LoadProject.jsp", jsfserver.getCurrentViewID());
	   
	   DislocBean dsb = getDislocBean(jsfserver);
	   
	   assertEquals("Project Archive", jsfserver.getComponentValue("lptv1"));
	   
	   if(!dsb.getMyProjectNameList().isEmpty())
	   {
		   
		   assertTrue(jsfserver.findComponent("lptv12").isRendered());
		   assertEquals("<b>Select Project</b><br><br>", jsfserver.getComponentValue("lptv21"));
		   assertEquals("Please select from one of the previous projects.", jsfserver.getComponentValue("lptv22"));
		   
		   		   
		   // Check its projectnamelist
		   System.out.println("((SelectItem) dsb.getMyProjectNameList().get(0)).getValue : " + ((SelectItem) dsb.getMyProjectNameList().get(0)).getValue());
		   assertEquals("dummyproject1", ((SelectItem) dsb.getMyProjectNameList().get(0)).getValue());
		   
		   
		   // Currently, it's so hard to simulate 'selectManyCheckbox' and 'selectItems' so we just set a backing bean directly.
		   
		   dsb.setSelectProjectsArray(new String[]{"dummyproject1"});		   
		   setDislocBean(jsfserver, dsb);
		   dsb = getDislocBean(jsfserver);
		   
		   // Check if the checked value is updated.
		   String[] dsbprojectlist = (String[]) jsfserver.getManagedBeanValue("#{DislocBean2.selectProjectsArray}");		   
		   assertEquals("dummyproject1", dsbprojectlist[0]);		   
		   System.out.println("(String[]) jsfserver.getManagedBeanValue(#{DislocBean2.selectProjectsArray}) " + ((String[])jsfserver.getManagedBeanValue("#{DislocBean2.selectProjectsArray}"))[0]);
		  
		   
		   // Let's select it
		   
		   // jsfclient.click("lkdjf8"); // a button 'Select'
		   
		   
		   try {
			dsb.toggleSelectProject();
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		   setDislocBean(jsfserver, dsb);
		   
		   // Now, we should be in a 'EditProject.jsp'		   
		   // assertEquals("/EditProject.jsp", jsfserver.getCurrentViewID());
	   }
	   
	   else
		   System.out.println("This test is only for a non-empty project list");
   }
   
  // fetchresults   
   
   public void testFetchResults() throws IOException
   {   
	   WebClientSpec wcs = new WebClientSpec("/FetchResults.faces", BrowserVersion.FIREFOX_3);
	   this.jsfsession = new JSFSession(wcs);
	   this.jsfclient = this.jsfsession.getJSFClientSession();
	   this.jsfserver = this.jsfsession.getJSFServerSession();
	   System.out.println("\ntestFetchResults() started... for " + this.jsfserver.getCurrentViewID() + "\n");	   
	   assertEquals("/FetchResults.jsp", jsfserver.getCurrentViewID());
	   
	   DislocBean dsb = getDislocBean(this.jsfserver);
	   
	   if (dsb.getMyArchivedDislocResultsList().isEmpty())
		   emptyFetchResults();
	   
	   else
	   {
		   System.out.println("size: " + dsb.getMyArchivedDislocResultsList().size());
		   
		   System.out.println("aaa " + ((DislocProjectSummaryBean)dsb.getMyArchivedDislocResultsList().get(0)).projectName);
		   
		   
		   assertEquals("Refresh Page", jsfserver.getComponentValue("blaheoru129"));	   
		   assertEquals("<h2>Archived Results</h2>", jsfserver.getComponentValue("header"));
		   assertEquals("You have the following archived disloc runs. Click the link to download the desired file to your desk top. To save directly to your desktop, click your mouse's right button over the link and select<br>", jsfserver.getComponentValue("dosomework"));
		   
		   
		   // assertTrue(jsfserver.findComponent("DislocOutputPanel").isRendered());
		   
		   System.out.println("\nwhere I am " + jsfserver.getCurrentViewID() + "\n");
		   		   
		   System.out.println("blaheoru0's id : " + jsfserver.findComponent("blaheoru0").getId());
		   assertEquals("Project Name", jsfserver.getComponentValue("blaheoru0"));
		   assertEquals("Creation Date", jsfserver.getComponentValue("blaheoru3"));
		   assertEquals("Job UID Stamp", jsfserver.getComponentValue("blaheoru4"));
		   assertEquals("Input File", jsfserver.getComponentValue("blaheoru6"));
		   assertEquals("Input", jsfserver.getComponentValue("blaheoru8"));
		   assertEquals("Output File", jsfserver.getComponentValue("blaheoru9"));
		   assertEquals("Output", jsfserver.getComponentValue("blaheoru11"));
		   assertEquals("<b>Kml file</b>", jsfserver.getComponentValue("blaheoru15"));
		   assertEquals("[<b>Download</b>]", jsfserver.getComponentValue("kdjfjk"));
		   assertEquals("<b> </b>", jsfserver.getComponentValue("blaheoru16"));
		   assertEquals("<b>Delete</b>", jsfserver.getComponentValue("blaheoru18"));
		   assertEquals("Stdout", jsfserver.getComponentValue("blaheoru14"));
		   
		   
		   // Check its projectnamelist
		   System.out.println("((SelectItem) dsb.getMyProjectNameList().get(0)).getValue : " + ((SelectItem) dsb.getMyProjectNameList().get(0)).getValue());
		   
		   
		   HtmlDataTable table = (HtmlDataTable) jsfserver.getComponentValue("DislocOutputPanel");
		   
		   for (int nA = 0 ; nA < table.getRowCount() ; nA++)
		   {
			   table.setRowIndex(nA);
			   DislocProjectSummaryBean sob = (DislocProjectSummaryBean)table.getRowData();
			   
			   assertEquals(sob.getProjectName(), ((DislocProjectSummaryBean)(dsb.getMyArchivedDislocResultsList().get(nA))).getProjectName());
			   assertEquals(sob.getCreationDate(), ((DislocProjectSummaryBean)(dsb.getMyArchivedDislocResultsList().get(nA))).getCreationDate());
			   assertEquals(sob.getJobUIDStamp(), ((DislocProjectSummaryBean)(dsb.getMyArchivedDislocResultsList().get(nA))).getJobUIDStamp());
			   assertEquals(sob.getResultsBean().getInputFileUrl(), ((DislocProjectSummaryBean)(dsb.getMyArchivedDislocResultsList().get(nA))).getResultsBean().getInputFileUrl());
			   assertEquals(sob.getResultsBean().getOutputFileUrl(), ((DislocProjectSummaryBean)(dsb.getMyArchivedDislocResultsList().get(nA))).getResultsBean().getOutputFileUrl());
			   assertEquals(sob.getKmlurl(), ((DislocProjectSummaryBean)(dsb.getMyArchivedDislocResultsList().get(nA))).getKmlurl());
			   assertEquals(sob.getResultsBean().getStdoutUrl(), ((DislocProjectSummaryBean)(dsb.getMyArchivedDislocResultsList().get(nA))).getResultsBean().getStdoutUrl());
			   
			   String nTemp = sob.getProjectName();
			   
			   // Delete the entry
			   this.jsfclient.click("deletepanel"); // a button 'Delete' to run a 'toggleDeleteProjectSummary'.
			   
			   
			   // Check if the entry is removed well.
			   
			   
			   // In the table
			   for (int nB = 0 ; nB < table.getRowCount() ; nB++)
			   {
				   table.setRowIndex(nB);
				   assertNotSame(nTemp, ((DislocProjectSummaryBean)(dsb.getMyArchivedDislocResultsList().get(nA))).getProjectName());
			   }
			   
			   table.setRowIndex(nA);
			   
			   // In a entrylist			   
			   for (int nB = 0 ; nB < dsb.getMyArchivedDislocResultsList().size() ; nB++)
				   assertNotSame(nTemp, ((DislocProjectSummaryBean)(dsb.getMyArchivedDislocResultsList().get(nA))).getProjectName());
			   
			   // In a namelist
			   for (int nB = 0 ; nB < dsb.getMyProjectNameList().size() ; nB++)
				   assertNotSame(nTemp, ((SelectItem) dsb.getMyProjectNameList().get(nB)).getValue());
			   
		   }
	   }
   }
   
   
   
   
   
   
   public void testLoadProject_DeleteProject() throws IOException
   {
	   WebClientSpec wcs = new WebClientSpec("/LoadProject.faces", BrowserVersion.FIREFOX_3);
	   this.jsfsession = new JSFSession(wcs);
	   this.jsfclient = this.jsfsession.getJSFClientSession();
	   this.jsfserver = this.jsfsession.getJSFServerSession();
	   
	   System.out.println("\ntestLoadProject_DeleteProject() started... for " + this.jsfserver.getCurrentViewID() + "\n");	   
	   assertEquals("/LoadProject.jsp", jsfserver.getCurrentViewID());
	   
	   DislocBean dsb = getDislocBean(jsfserver);
	   
	   
	   assertEquals("Project Archive", jsfserver.getComponentValue("lptv1"));
	   
	   
	   if(!dsb.getMyProjectNameList().isEmpty())
	   {		   
		   assertTrue(jsfserver.findComponent("lptv12").isRendered());
		   assertEquals("<b>Select Project</b><br><br>", jsfserver.getComponentValue("lptv21"));
		   assertEquals("Please select from one of the previous projects.", jsfserver.getComponentValue("lptv22"));
		   
		   		   
		   // Check its projectnamelist
		   System.out.println("((SelectItem) dsb.getMyProjectNameList().get(0)).getValue : " + ((SelectItem) dsb.getMyProjectNameList().get(0)).getValue());
		   assertEquals("dummyproject1", ((SelectItem) dsb.getMyProjectNameList().get(0)).getValue());
		   
		   
		   // Currently, it's so hard to simulate 'selectManyCheckbox' and 'selectItems' so we just set a backing bean directly.
		   
		   dsb.setSelectProjectsArray(new String[]{"dummyproject1"});		   
		   setDislocBean(jsfserver, dsb);
		   dsb = getDislocBean(jsfserver);
		   
		   // Check what if the checked value is updated.
		   String[] dsbprojectlist = (String[]) jsfserver.getManagedBeanValue("#{DislocBean2.selectProjectsArray}");
		   
		   assertEquals("dummyproject1", dsbprojectlist[0]);
		   
		   		   
		   // Let's delect it
		   dsb.toggleDeleteProject();
		   // jsfclient.click("lkdjf12"); // a button 'Delete'
		   
		   
		   
		   // Now, the 'dummyproject1' should be removed.
		   dsb = getDislocBean(jsfserver);
		   for (int nA = 0 ; nA < dsb.getMyProjectNameList().size() ; nA++)
			   assertNotSame(((SelectItem)dsb.getMyProjectNameList().get(nA)).getValue(), "dummyproject1");
		   
		   
	   }
	   
	   else
		   System.out.println("This test is only for a non-empty project list");
   }

   

 
   // This methods is only for an empty archived data and called only when needed, but not by cactus.
   
   public void emptyFetchResults() throws IOException
   {   
	   WebClientSpec wcs = new WebClientSpec("/FetchResults.faces", BrowserVersion.FIREFOX_3);
	   this.jsfsession = new JSFSession(wcs);
	   this.jsfclient = this.jsfsession.getJSFClientSession();
	   this.jsfserver = this.jsfsession.getJSFServerSession();
	   System.out.println("\nemptyFetchResults() started... for " + this.jsfserver.getCurrentViewID() + "\n");	   
	   assertEquals("/FetchResults.jsp", jsfserver.getCurrentViewID());
	   
   }
   
   
   // Pick-location-portlet
   
   public void testLocateProject() throws IOException
   {
	   WebClientSpec wcs = new WebClientSpec("/LocateProject.faces", BrowserVersion.FIREFOX_3);	   
	   this.jsfsession = new JSFSession(wcs);
	   this.jsfclient = this.jsfsession.getJSFClientSession();
	   this.jsfserver = this.jsfsession.getJSFServerSession();
	   System.out.println("\ntestLocateProject() started... for " + this.jsfserver.getCurrentViewID() + "\n");
	   assertEquals("/LocateProject.jsp", jsfserver.getCurrentViewID());
	   
   }
}