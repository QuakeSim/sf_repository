package org.apache.myfaces.blank;

/**
 * A typical simple backing bean, that is backed to <code>helloworld.jsp</code>
 * 
 * @author <a href="mailto:matzew@apache.org">Matthias Weßendorf</a> 
 */
public class RDAHMMBean {

    
    //properties
    private String name;
    
    /**
     * default empty constructor
     */
    public RDAHMMBean(){   
    }
    
    /**
     * Method that is backed to a submit button of a form.
     */
    public String newProject(){
        //Do real logic
	System.out.println("Creating new project");
        return ("create-new-project");
    }

    public String loadProject() {
	System.out.println("Creating new project");
        return ("list-old-projects");

    }
}
