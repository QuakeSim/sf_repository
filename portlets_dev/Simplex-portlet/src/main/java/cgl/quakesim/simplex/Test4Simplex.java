package cgl.quakesim.simplex;

import java.io.IOException;


import javax.faces.context.FacesContext;
import javax.faces.el.ValueBinding;
import javax.faces.model.SelectItem;

import junit.framework.Test;
import junit.framework.TestSuite;


import org.apache.cactus.ServletTestCase;
import org.apache.myfaces.component.html.ext.HtmlDataTable;

import org.jboss.jsfunit.framework.WebClientSpec;
import org.jboss.jsfunit.jsfsession.JSFClientSession;
import org.jboss.jsfunit.jsfsession.JSFServerSession;
import org.jboss.jsfunit.jsfsession.JSFSession;

import com.gargoylesoftware.htmlunit.BrowserVersion;
import com.gargoylesoftware.htmlunit.html.HtmlForm;
import com.gargoylesoftware.htmlunit.html.HtmlSpan;;



public class Test4Simplex extends ServletTestCase
{
	
   private JSFSession jsfsession;
   private JSFClientSession jsfclient;
   private JSFServerSession jsfserver;


   public static Test suite()
   {
      return new TestSuite(Test4Simplex.class);
   }
   
   private SimplexBean getSimplexBean(JSFServerSession jsfserver)
   {
	   return (SimplexBean) jsfserver.getManagedBeanValue("#{SimplexBean}");
   }
   
   private void setSimplexBean(JSFServerSession jsfserver, SimplexBean sb)
   {	   
	   FacesContext f = jsfserver.getFacesContext();
	   ValueBinding b = f.getApplication().createValueBinding("#{SimplexBean}");	   
	   b.setValue(f, sb);	   
   }
   
   /*

   // the main page of portlet Simplex-portlet

   public void testLoadProject_NewProject() throws IOException
   {   
	   emptyArchivedData();
	   
	   WebClientSpec wcs = new WebClientSpec("/LoadProject.faces", BrowserVersion.FIREFOX_3);
	   this.jsfsession = new JSFSession(wcs);
	   this.jsfclient = this.jsfsession.getJSFClientSession();
	   this.jsfserver = this.jsfsession.getJSFServerSession();
	   
	   System.out.println("\ntestLoadProject_NewProject() started... for " + this.jsfserver.getCurrentViewID() + "\n");
	   assertEquals("/LoadProject.jsp", jsfserver.getCurrentViewID());
	   
	   SimplexBean sb = getSimplexBean(jsfserver);
	   assertEquals("Project Archive", jsfserver.getComponentValue("lkdjf2"));
	   
	   if(sb.getMyProjectNameList().isEmpty())
	   {		   
		   assertTrue(jsfserver.findComponent("lkdjf3").isRendered());		   
		   assertEquals("You don't have any archived projects.", jsfserver.getComponentValue("lkdjf3"));
		   assertFalse(jsfserver.findComponent("lkdjf4").isRendered());
		   
		   
		   assertEquals("Provide a Project Name:", jsfserver.getComponentValue("lkdjf15"));
		   assertEquals("Problem starting temperature:", jsfserver.getComponentValue("lkdjf17"));
		   assertEquals("Specify the maximum number of iterations:", jsfserver.getComponentValue("lkdjf19"));
		   
		   
		   jsfclient.setValue("projectName", "dummyproject1");
		   jsfclient.setValue("startTemp", "100");
		   jsfclient.setValue("maxIters", "200");

		   
		   jsfclient.click("lkdjf113");
		   
		   
		   // Now, we are in the EditProject.jsp		   
		   
		   assertEquals("Project Component Manager", jsfserver.getComponentValue("lkdrq1"));
		   assertEquals("<br>You must provide at least one fault and one observation point before you can run Simplex", jsfserver.getComponentValue("lkdrq2"));
		   
		   
		   // testing for the DashboardPanel.jsp
		   
		   assertEquals("<b>Project Name:</b> " + sb.getProjectName() + " <br>", jsfserver.getComponentValue("lkdrq4"));
		   assertEquals("<b>Starting Temperature:</b> " + sb.getCurrentProjectEntry().getStartTemp() + " <br>", jsfserver.getComponentValue("lkdrq5"));
		   assertEquals("<b>Maximum Iterations:</b> " + sb.getCurrentProjectEntry().getMaxIters() + " <br>", jsfserver.getComponentValue("lkdrq6"));
		   assertEquals("<b>Project Lat/Lon Origin:</b> (" + sb.getCurrentProjectEntry().getOrigin_lat() + ", " + sb.getCurrentProjectEntry().getOrigin_lon() + ")", jsfserver.getComponentValue("lkj3034f"));		   
		   assertEquals("<br>You must add at least one observation point and one fault.  The preferred method is to use the GPS station map option.", jsfserver.getComponentValue("instructionezzze"));
		   assertEquals(sb.getCurrentEditProjectForm().getProjectSelectionCode(), jsfserver.getComponentValue("subscriptions"));
		   

		   // Let's click to add a GPS observation point.
		   
		   jsfclient.click("item021"); // Add GPS Observation Point (Preferred): Use map to choose GPS station input.
		   jsfclient.click("button1"); // Make Selection
		   
		   
		   // Now, we can see the GPSMapPanel.jsp (Add GPS Observation Point (Preferred): Use map to choose GPS station input.)
		   
		   assertEquals("<b>Select Stations from Map:</b> Select the stations that you want to use as observation points.", jsfserver.getComponentValue("clrlc093"));
		   
		   assertEquals("Station:", jsfserver.getComponentValue("dkl34rtjf"));
		   assertEquals("Latitude:", jsfserver.getComponentValue("dkljr3rf"));
		   assertEquals("Longitude:", jsfserver.getComponentValue("dkljfer4"));
		   assertEquals("Ref Station?:", jsfserver.getComponentValue("dkljr3dssrf"));
		   
		   
		   // Give values for an observation point.
		   
		   
		   // jsfclient.setValue("stationName", "dummystation1");
		   // jsfclient.setValue("stationLat", "33.036");
		   // jsfclient.setValue("stationLon", "-117.24");
		   
		   
		   jsfclient.setValue("stationName", "scip");
		   jsfclient.setValue("stationLat", "32.91443");
		   jsfclient.setValue("stationLon", "-118.48794");

		   
		   // Add the point.
		   
		   jsfclient.click("addGPSObsv");
		   
		   
		   // Check if the point is made and updated.		   
		   
		   assertEquals("<b>Project Lat/Lon Origin:</b> (32.91443, -118.48794)", jsfserver.getComponentValue("lkj3034f"));
		   		   
		   
		   // Check a backing bean to verify if this project is updated.
		   
		   SimplexBean sb2 = getSimplexBean(jsfserver);
		   System.out.println("((SelectItem) sb2.getMyProjectNameList().get(0)).getValue : " + ((SelectItem) sb2.getMyProjectNameList().get(0)).getValue());
		   assertEquals("dummyproject1", ((SelectItem) sb2.getMyProjectNameList().get(0)).getValue());
		   
		   
		   
		   // Now, we click a 'Add Fault from Map'
		   
		   jsfclient.click("item33221"); // Add Fault from Map (Preferred): Use map to choose input fault.
		   jsfclient.click("button1"); // Make Selection
		   
		   
		   // a FaultMapPanelFram.jsp (Add Fault from Map) is open
		   
		   jsfclient.setValue("faultName", "Antelope Valley@N/A%50"); // 3rd fault 'Antelope Valley' in the list.
		   jsfclient.click("queryDBFromMap"); // a button 'Get Fault Params'
		   
		    
		   // FaultParamsPanel.jsp is open.
		   
		   jsfclient.click("domath"); // a button 'Do Math';
		   jsfclient.click("addfault"); // a button 'Set Values';
		   
		   // Now, We can see and click a button 'Run Simplex'

		   jsfclient.click("runSimplex2"); // a button 'Run Simplex'
		   
		   // Now, We are supposed to be back to 'LoadProject.jsp'
		   
		   System.out.println("After 'runSimplex2' : " + jsfserver.getCurrentViewID());
		   assertEquals("/LoadProject.jsp", jsfserver.getCurrentViewID());
		   
		   sb = getSimplexBean(this.jsfserver);
		   
		   
		   // Check if the result has a correct project.		   
		   assertEquals("dummyproject1", sb.getProjectSimpleXOutput().getProjectName());
		   
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
	   
	   
	   SimplexBean sb = getSimplexBean(jsfserver);
	   
	   assertEquals("Project Archive", jsfserver.getComponentValue("lkdjf2"));
	   
	   if(!sb.getMyProjectNameList().isEmpty())
	   {
		   assertFalse(jsfserver.findComponent("lkdjf3").isRendered());
		   assertTrue(jsfserver.findComponent("lkdjf4").isRendered());
		   assertEquals("<b>Select Projects</b><br><br>", jsfserver.getComponentValue("lkdjf6"));
		   assertEquals("Please select from one of the previous projects.", jsfserver.getComponentValue("lkdjf7"));		   
		   
		   		   
		   // Check its projectnamelist
		   System.out.println("((SelectItem) sb.getMyProjectNameList().get(0)).getValue : " + ((SelectItem) sb.getMyProjectNameList().get(0)).getValue());
		   assertEquals("dummyproject1", ((SelectItem) sb.getMyProjectNameList().get(0)).getValue());
		   
		   
		   // Currently, it's so hard to simulate 'selectManyCheckbox' and 'selectItems' so we just set a backing bean directly.
		   
		   sb.setSelectProjectsList(new String[]{"dummyproject1"});		   
		   setSimplexBean(jsfserver, sb);		   
		   sb = getSimplexBean(jsfserver);
		   
		   // Check if the checked value is updated.
		   String[] sbprojectlist = (String[]) jsfserver.getManagedBeanValue("#{SimplexBean.selectProjectsList}");
		   
		   assertEquals("dummyproject1", sbprojectlist[0]);
		  
		   
		   // Let's select it
		   
		   // jsfclient.click("lkdjf8"); // a button 'Select'
		   
		   
		   sb.toggleSelectProject();
		   setSimplexBean(jsfserver, sb);
		   
		   
		   // Now, we should be in a 'EditProject.jsp'		   
		   // assertEquals("/EditProject.jsp", jsfserver.getCurrentViewID());
		   
	   }
	   
	   else
		   System.out.println("This test is only for a non-empty project list");
   }
   
   */
   
   
   
   
   // the main page of portlet Simplex-plot-portlet
   
   public void testPlotGrid() throws IOException
   {
	   WebClientSpec wcs = new WebClientSpec("/PlotGrid.faces", BrowserVersion.FIREFOX_3);	   
	   this.jsfsession = new JSFSession(wcs);
	   this.jsfclient = this.jsfsession.getJSFClientSession();
	   this.jsfserver = this.jsfsession.getJSFServerSession();
	   System.out.println("\ntestPlotGrid() started... for " + this.jsfserver.getCurrentViewID() + "\n");
	   assertEquals("/PlotGrid.jsp", jsfserver.getCurrentViewID());
	   
	   
	   assertEquals("Project Name", jsfserver.getComponentValue("blaher0er"));
	   System.out.println("jsfserver.getComponentValue(blaher0er) : " + jsfserver.getComponentValue("blaher0er"));
	   assertEquals("<b>Creation Date</b>", jsfserver.getComponentValue("kjb132623d"));
	   assertEquals("<b>Select</b>", jsfserver.getComponentValue("blah1er8er"));	   
	   
	   
   }
   
   
   
   
   
   
   // the main page of portlet Simplex-archive-portlet   
   
   public void testArchivedData() throws IOException
   {   
	   WebClientSpec wcs = new WebClientSpec("/ArchivedData.faces", BrowserVersion.FIREFOX_3);
	   this.jsfsession = new JSFSession(wcs);
	   this.jsfclient = this.jsfsession.getJSFClientSession();
	   this.jsfserver = this.jsfsession.getJSFServerSession();
	   System.out.println("\ntestArchivedData() started... for " + this.jsfserver.getCurrentViewID() + "\n");	   
	   assertEquals("/ArchivedData.jsp", jsfserver.getCurrentViewID());
	   
	   SimplexBean sb = getSimplexBean(this.jsfserver);
	   
	   if (sb.getMyarchivedFileEntryList().isEmpty())
	   {
		   System.out.println("The myarchivedfileentrylist is empty.");
		   emptyArchivedData();
	   }
	   
	   else
	   {
		   
		   System.out.println("The myarchivedfileentrylist isn't empty.");
		   assertTrue(jsfserver.findComponent("message1").isRendered());
		   // assertTrue(jsfserver.findComponent("SimplexOutputPanel").isRendered());
		   	   
		   assertFalse(jsfserver.findComponent("message2").isRendered());	   
		   assertEquals("You have the following archived data files. Download the input and output files for more information on the data.<br>", jsfserver.getComponentValue("message1"));
		   		
		   
		   // assertEquals("<b>Project Name</b>", jsfserver.getComponentValue("kjb16"));
		   // assertEquals("<b>Creation Date</b>", jsfserver.getComponentValue("kjb1623"));
		   // assertEquals("<b>	Archived Data File</b>", jsfserver.getComponentValue("kjb3"));
		   // assertEquals("[<b>Download</b>]", jsfserver.getComponentValue("kjb6"));
		   // assertEquals("[<font size=1px>View In Google map</font>]", jsfserver.getComponentValue("kjb1"));
		   // assertEquals("<b>Delete</b>", jsfserver.getComponentValue("blah18"));
		   
		   
		   // HtmlDataTable table = (HtmlDataTable) jsfserver.getComponentValue("SimplexOutputPanel");
		   // System.out.println("SimplexOutputpanel 's id : " + jsfserver.findComponent("SimplexOutputPanel").getId());
		   

		   // HtmlForm myForm = (HtmlForm)jsfclient.getElement("theFormid");
		   // HtmlSpan mySpan = (HtmlSpan)myForm.getFirstByXPath(".//span[@id='theFormid:SimplexOutputPanel:kjb16']");
		   
		   /*
		   
		   assertEquals("Project Name", mySpan.getNodeValue());
		   
		   mySpan = (HtmlSpan)myForm.getFirstByXPath(".//span[@id='theFormid:SimplexOutputPanel:kjb1623']");		   
		   assertEquals("<b>Creation Date</b>", mySpan.getNodeValue());
		   
		   mySpan = (HtmlSpan)myForm.getFirstByXPath(".//span[@id='theFormid:SimplexOutputPanel:kjb3']");
		   assertEquals("<b>	Archived Data File</b>", mySpan.getNodeValue());
		   
		   mySpan = (HtmlSpan)myForm.getFirstByXPath(".//span[@id='theFormid:SimplexOutputPanel:kjb6']");
		   assertEquals("[<b>Download</b>]", mySpan.getNodeValue());
		   
		   mySpan = (HtmlSpan)myForm.getFirstByXPath(".//span[@id='theFormid:SimplexOutputPanel:kjb1']");
		   assertEquals("[<font size=1px>View In Google map</font>]", mySpan.getNodeValue());
		   
		   mySpan = (HtmlSpan)myForm.getFirstByXPath(".//span[@id='theFormid:SimplexOutputPanel:blah18']");
		   assertEquals("<b>Delete</b>", mySpan.getNodeValue());
		   
		   */
		   
		   System.out.println("jsfserver.findComponent(SimplexOutputPanel).getId() : " + jsfserver.findComponent("SimplexOutputPanel").getId());
		   System.out.println("jsfserver.findComponent(SimplexOutputPanel).getParent().getId() : " + jsfserver.findComponent("SimplexOutputPanel").getParent().getId());
		   
		   HtmlDataTable table = (HtmlDataTable) jsfserver.getComponentValue("SimplexOutputPanel");
		   // HtmlDataTable table = (HtmlDataTable) myForm.getFirstByXPath(".//table[@id='theFormid:pgidsimplex']");
		   
		   
		   

		   for (int nA = 0 ; nA < table.getRowCount() ; nA++)
		   {
			   table.setRowIndex(nA);
			   SimpleXOutputBean sob = (SimpleXOutputBean)table.getRowData();
			   
			   assertEquals(sob.getProjectName(), ((SimpleXOutputBean)(sb.getMyarchivedFileEntryList().get(nA))).getProjectName());
			   assertEquals(sob.getCreationDate(), ((SimpleXOutputBean)(sb.getMyarchivedFileEntryList().get(nA))).getCreationDate());
			   assertEquals(sob.getInputUrl(), ((SimpleXOutputBean)(sb.getMyarchivedFileEntryList().get(nA))).getInputUrl());
			   assertEquals(sob.getLogUrl(), ((SimpleXOutputBean)(sb.getMyarchivedFileEntryList().get(nA))).getLogUrl());
			   assertEquals(sob.getOutputUrl(), ((SimpleXOutputBean)(sb.getMyarchivedFileEntryList().get(nA))).getOutputUrl());
			   assertEquals(sob.getFaultUrl(), ((SimpleXOutputBean)(sb.getMyarchivedFileEntryList().get(nA))).getFaultUrl());
			   assertEquals(sob.getKmlUrls()[0], ((SimpleXOutputBean)(sb.getMyarchivedFileEntryList().get(nA))).getKmlUrls());
			   
			   String nTemp = sob.getProjectName();
			   
			   
			   // Delete the entry
			   this.jsfclient.click("deletepanel"); // a button 'Delete' to run a 'toggleDeleteProjectSummary'.
			   
			   
			   // Check if the entry is removed well.
			   
			   
			   // In the table
			   for (int nB = 0 ; nB < table.getRowCount() ; nB++)
			   {
				   table.setRowIndex(nB);
				   assertNotSame(nTemp, ((SimpleXOutputBean)(sb.getMyarchivedFileEntryList().get(nA))).getProjectName());
			   }
			   
			   table.setRowIndex(nA);
			   
			   // In a entrylist			   
			   for (int nB = 0 ; nB < sb.getMyarchivedFileEntryList().size() ; nB++)
				   assertNotSame(nTemp, ((SimpleXOutputBean)(sb.getMyarchivedFileEntryList().get(nA))).getProjectName());
			   
			   // In a namelist
			   for (int nB = 0 ; nB < sb.getMyProjectNameList().size() ; nB++)
				   assertNotSame(nTemp, ((SelectItem) sb.getMyProjectNameList().get(nB)).getValue());
			   
			   System.out.println ("god");
		   }
	   }
   }
   
   /*
   public void testLoadProject_DeleteProject() throws IOException
   {
	   WebClientSpec wcs = new WebClientSpec("/LoadProject.faces", BrowserVersion.FIREFOX_3);
	   this.jsfsession = new JSFSession(wcs);
	   this.jsfclient = this.jsfsession.getJSFClientSession();
	   this.jsfserver = this.jsfsession.getJSFServerSession();
	   
	   System.out.println("\ntestLoadProject_DeleteProject() started... for " + this.jsfserver.getCurrentViewID() + "\n");	   
	   assertEquals("/LoadProject.jsp", jsfserver.getCurrentViewID());
	   
	   SimplexBean sb = getSimplexBean(jsfserver);
	   
	   
	   assertEquals("Project Archive", jsfserver.getComponentValue("lkdjf2"));
	   
	   
	   if(!sb.getMyProjectNameList().isEmpty())
	   {		   
		   assertFalse(jsfserver.findComponent("lkdjf3").isRendered());
		   assertTrue(jsfserver.findComponent("lkdjf4").isRendered());
		   assertEquals("<b>Select Projects</b><br><br>", jsfserver.getComponentValue("lkdjf6"));
		   assertEquals("Please select from one of the previous projects.", jsfserver.getComponentValue("lkdjf7"));		   
		   
		   		   
		   // Check its projectnamelist
		   System.out.println("((SelectItem) sb.getMyProjectNameList().get(0)).getValue : " + ((SelectItem) sb.getMyProjectNameList().get(0)).getValue());
		   assertEquals("dummyproject1", ((SelectItem) sb.getMyProjectNameList().get(0)).getValue());
		   
		   
		   // Currently, it's so hard to simulate 'selectManyCheckbox' and 'selectItems' so we just set a backing bean directly.
		   
		   sb.setDeleteProjectsList(new String[]{"dummyproject1"});
		   setSimplexBean(this.jsfserver, sb);
		   
		   sb = getSimplexBean(jsfserver);
		   
		   // Check what if the checked value is updated.
		   String[] sbprojectlist = (String[]) jsfserver.getManagedBeanValue("#{SimplexBean.deleteProjectsList}");
		   assertEquals("dummyproject1", sbprojectlist[0]);
		   
		   		   
		   // Let's delect it
		   sb.toggleDeleteProject();
		   // jsfclient.click("lkdjf12"); // a button 'Delete'
		   
		   
		   
		   // Now, the 'dummyproject1' should be removed.
		   sb = getSimplexBean(jsfserver);
		   for (int nA = 0 ; nA < sb.getMyProjectNameList().size() ; nA++)
			   assertNotSame(((SelectItem)sb.getMyProjectNameList().get(nA)).getValue(), "dummyproject1");
		   
		   System.out.println("testLoadProject_DeleteProject() is finished.");
		   
	   }
	   
	   else
		   System.out.println("This test is only for a non-empty project list");
   }
   */

   
   // This methods is only for an empty archived data and called only when needed, but not by cactus.
   
   public void emptyArchivedData() throws IOException
   {   
	   WebClientSpec wcs = new WebClientSpec("/ArchivedData.faces", BrowserVersion.FIREFOX_3);
	   this.jsfsession = new JSFSession(wcs);
	   this.jsfclient = this.jsfsession.getJSFClientSession();
	   this.jsfserver = this.jsfsession.getJSFServerSession();
	   System.out.println("\nemptyArchivedData() started... for " + this.jsfserver.getCurrentViewID() + "\n");	   
	   assertEquals("/ArchivedData.jsp", jsfserver.getCurrentViewID());
	   
	   
	   assertFalse(jsfserver.findComponent("message1").isRendered());
	   assertFalse(jsfserver.findComponent("SimplexOutputPanel").isRendered());
	   	   
	   assertTrue(jsfserver.findComponent("message2").isRendered());	   
	   assertEquals("You don't have any archived results.", jsfserver.getComponentValue("message2"));
	   
   }
   
   

   
}
	   