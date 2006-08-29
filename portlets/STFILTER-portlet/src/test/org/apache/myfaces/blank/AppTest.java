package xportlets.jobsubmit;

import junit.framework.Test;
import junit.framework.TestCase;
import junit.framework.TestSuite;

/**
 * Unit test for simple App.
 */
public class AppTest 
    extends TestCase
{
    /**
     * Create the test case
     *
     * @param testName name of the test case
     */
    public AppTest( String testName )
    {
        super( testName );
    }

    /**
     * @return the suite of tests being tested
     */
    public static Test suite()
    {
        return new TestSuite( AppTest.class );
    }

    /**
     * Rigourous Test :-)
     */
    public void testApp()
    {
	String theurl="http://localhost:8080/";
	STFILTERBean rb=new STFILTERBean();
	rb.setCodeName("STFILTER");
	rb.setContextUrl(theurl);
        assertEquals(theurl,rb.getContextUrl);

	rb.setIsInitalized(true);
	assertEquals("create-new-project",rb.newProject);


    }
}
