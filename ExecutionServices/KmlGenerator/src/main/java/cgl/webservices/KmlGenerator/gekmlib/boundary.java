package cgl.webservices.KmlGenerator.gekmlib;
/**
 * AutoGenerated.
 *
 */

import java.util.List;
import java.util.ArrayList;
import java.util.Iterator;
public class boundary extends Node
{
    protected LinearRing linearRing;


    public boundary()
    {
        super();
    }

    public boundary(Node parent)
    {
        super(parent);
    }

    public LinearRing getLinearRing()
    {
        return this.linearRing;
    }

    public void addLinearRing(LinearRing value)
    {
        if(this.linearRing!=null)
        {
            markDeletedNode(this.linearRing);
        }
        this.linearRing = value;
        if(value!=null)
        {
            value.setParent(this);
            markCreatedNode(value);
        }
    }



    public String toKML()
    {
        return toKML(false);
    }
    
    public String getTagName() {
    	return "boundary";
    }

    
    	
    	
    public String toKML(boolean suppressEnclosingTags)
    {
        String kml="";
        if(!suppressEnclosingTags)
        {
        	
        // kml+="<boundary";
        	kml+="<"+this.getTagName();
        	kml+=">\n";
        }
        kml+=super.toKML(true);
        if(this.linearRing!=null)
        {
            kml+=this.linearRing.toKML();
        }
        if(!suppressEnclosingTags)
        {
            // kml+="</boundary>\n";
        	kml+="</"+this.getTagName()+">\n";
        }
        return kml;
    }
    public String toUpdateKML()
    {
        return toUpdateKML(false);
    }
    public String toUpdateKML(boolean suppressEnclosingTags)
    {
        if(!isDirty())
        {
            return "";
        }
        String change = "";
        boolean isPrimDirty = isPrimitiveDirty(); // need to track it after object is setNotDirty
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="<boundary";
        change+=">\n";
        }
        change+=super.toUpdateKML(true);
        if(this.linearRing!=null && this.linearRing.isDirty())
        {
            change+=this.linearRing.toUpdateKML();
        }
        if(isPrimDirty && !suppressEnclosingTags)
        {
        change+="</boundary>\n";
        }
        setNotDirty();
        return change;
    }
    public Object clone() throws CloneNotSupportedException
    {
        boundary result = (boundary)super.clone();
      if(result.linearRing!=null)
      {
        result.linearRing = (LinearRing)this.linearRing.clone();
        result.linearRing.setParent(result);
      }
        return result;
    }
    public void setRecursiveNotDirty()
    {
        super.setRecursiveNotDirty();
        if(this.linearRing!=null && this.linearRing.isDirty())
        {
            this.linearRing.setRecursiveNotDirty();
        }
    }
}
