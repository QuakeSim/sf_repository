package cgl.quakesim.simplex;

import java.util.ArrayList;
import java.util.List;
import java.util.StringTokenizer;
import TestClient.Select.*;
import java.net.URL;

import javax.faces.model.SelectItem;

public class SelectBean {
	String DB_RESPONSE_HEADER = "results of the query:";

	// --------------------------------------------------
	// Set up the DB
	// --------------------------------------------------
	String selectdbURL = "http://danube.ucs.indiana.edu:9090/axis/services/"
			+ "/Select";

	String getAuthorListSQL = "select Author1 from LAYER LEFT JOIN LREFERENCE on LAYER.InterpId=LREFERENCE.InterpId;";

	String getLayerListSQL = "select LayerName from LAYER LEFT JOIN LREFERENCE on LAYER.InterpId=LREFERENCE.InterpId;";

	List layerNameList = new ArrayList();

	List layerOwnerList = new ArrayList();

	/**
	 * default empty constructor
	 */
	public SelectBean() {
		System.out.println("Select Bean Created");
	}
    public void setSelectdbURL(String surl) {
    	this.selectdbURL=surl;
        }
    public String getSelectdbURL() {
    	return this.selectdbURL;
        }

	public List getLayerNameList() {
		try {

			SelectService ss = new SelectServiceLocator();
			Select select = ss.getSelect(new URL(selectdbURL));

			// --------------------------------------------------
			// Make queries.
			// --------------------------------------------------
			String layers = select.select(getLayerListSQL);
			layers = layers.substring(DB_RESPONSE_HEADER.length());
			StringTokenizer st1 = new StringTokenizer(layers, "\n");
			// They begin with blank lines ?!
			 st1.nextToken();
			 st1.nextToken();
			layerNameList.clear();
			while (st1.hasMoreTokens()) {
				String tmp1=st1.nextToken();
				System.out.println(tmp1);
				layerNameList.add(new SelectItem(tmp1, tmp1));
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return layerNameList;
	}

	public List getLayerOwnerList() {
		try {

			SelectService ss = new SelectServiceLocator();
			Select select = ss.getSelect(new URL(selectdbURL));

			// --------------------------------------------------
			// Make queries.
			// --------------------------------------------------

			String authors = select.select(getAuthorListSQL);
			authors = authors.substring(DB_RESPONSE_HEADER.length());
			StringTokenizer st3 = new StringTokenizer(authors, "\n");

			// They begin with blank lines ?!
			layerOwnerList.clear();
			 st3.nextToken();
			 st3.nextToken();
			while (st3.hasMoreTokens()) {
				layerOwnerList.add(st3.nextToken());
			}
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return layerOwnerList;
	}

	public static void main(String[] args) {
		SelectBean sb = new SelectBean();
		List ss=sb.getLayerOwnerList();
		System.out.println(ss.size());
		for (int i =0; i<ss.size();i++){
			System.out.println(ss.get(i));
		
		}

	}

}
