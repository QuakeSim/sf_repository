package cgl.sensorgrid.sopac.gps;

import java.io.*;
import java.util.*;
import cgl.sensorgrid.common.*;
import org.apache.commons.httpclient.*;
import org.apache.commons.httpclient.methods.*;
import org.dom4j.*;
import org.dom4j.io.*;

public class GetStationsRSS {

    private java.util.Properties properties = new java.util.Properties();
    private Document document = null;

    public GetStationsRSS() {
        loadProperties();
        String xml = "";
        String rssLocalPath = properties.getProperty("stations.rss.local.path");
        File f = new File(rssLocalPath);
        boolean getfile = false;
        if (!f.exists()) {
            try {
                f.createNewFile();
            } catch (IOException ex1) {
                ex1.printStackTrace();
            }
            getfile = true;
        } else {
            if (f.length() < 5) {
                getfile = true;
            }
        }

        long currentTime = System.currentTimeMillis();
        //if file is too old
        System.out.println("RSS file was last updated " +
                           ((currentTime - f.lastModified()) / 1000 / 60) +
                           " minutes ago.");
//        if (((currentTime - f.lastModified()) > (60 * 60 * 1000)) || getfile) {
//            System.out.println("Updating RSS file from web");
//            xml = HTTPGetFile(rssUrl);
//
//            try {
//                FileWriter fw = new FileWriter(rssLocalPath);
//                fw.write(xml);
//                fw.flush();
//                fw.close();
//            } catch (IOException ex2) {
//                ex2.printStackTrace();
//            }
//        } else {
            try {
                System.out.println("Parsing local RSS file...");
                BufferedReader br = new BufferedReader(new FileReader(
                        rssLocalPath));
                StringBuffer sb = new StringBuffer();
                while (br.ready()) {
                    sb.append(br.readLine());
                }
                xml = sb.toString();
                sb = null;
            } catch (Exception ex3) {
                ex3.printStackTrace();
            }
//        }

        SAXReader reader = new SAXReader();
        try {
            document = reader.read(new StringReader(xml));
        } catch (DocumentException ex) {
            ex.printStackTrace();
        }
    }

    private java.util.Properties loadProperties() {
        // Read properties file.
        try {
            properties = PropertyFile.loadProperties(
                    "sensorgrid.properties");
        } catch (Exception e) {
            e.printStackTrace();
        }
        return properties;
    }

    public String HTTPGetFile(String url) {
        //create a singular HttpClient object
        HttpClient client = new HttpClient();

        //establish a connection within 5 seconds
        client.getHttpConnectionManager().
                getParams().setConnectionTimeout(5000);

        HttpMethod method = null;

        //create a method object
        method = new GetMethod(url);
        method.setFollowRedirects(true);

        //execute the method
        String responseBody = null;
        try {
            client.executeMethod(method);
            responseBody = method.getResponseBodyAsString();
            //System.out.println("" + responseBody);
            //this.findNode(responseBody);
        } catch (HttpException he) {
            System.err.println("Http error connecting to '" + url + "'");
            System.err.println(he.getMessage());
            System.exit( -4);
        } catch (IOException ioe) {
            System.err.println("Unable to connect to '" + url + "'");
            System.exit( -3);
        }

        return responseBody;
    }

//    public void findNode(String xmlInstanceString) {
//        try {
//            String xpath = "//xml/station/network/name";
//            SAXReader reader = new SAXReader();
//            Document document = reader.read(new StringReader(xmlInstanceString));
//            Node node = document.selectSingleNode(xpath);
//            if (node != null) {
//                System.out.println("node name " + node.getName());
//                System.out.println("node.getText() " + node.getText());
//            }
//        } catch (Exception ex) {
//            ex.printStackTrace();
//        }
//    }

    public void treeWalk(Document document) {
        treeWalk(document.getRootElement());
    }

    /**
     *
     * @param element Element
     * @param vec Vector
     * @param elements Vector
     * @param fw FileWriter
     */

    private Vector v = new Vector();
    String line = "";

    public void treeWalk(Element element) {
        try {
            for (int i = 0, size = element.nodeCount(); i < size; i++) {
                Node node = element.node(i);
                if (node.getName() != null &&
                    node.getName().equals("station")) {
                    //System.out.println("");
                    if (!line.trim().equals("")) {
                        if (!line.startsWith("\" \"")) {
                            v.add(line);
                        }
                    }
                    line = new String();
                }
                if (node instanceof Element) {
                    Element a = (Element) node;

                    if (!node.getName().equals("") &&
                        node.getName().equalsIgnoreCase("id")) {
                        //System.out.print(node.getText() + "\t");
                    } else if (!node.getName().equals("") &&
                               node.getName().equalsIgnoreCase("longitude")) {
                        //System.out.print(node.getText()+ "\t");
                    } else if (!node.getName().equals("") &&
                               node.getName().equalsIgnoreCase("latitude")) {
                        //System.out.print(node.getText()+ "\n");
                    }

                    if (a.nodeCount() <= 1) {
                        if (!node.getName().equals("") &&
                            node.getName().equalsIgnoreCase("name")) {
                            //                            System.out.println("node.getName()=" + node.getName() +
                            //                                               " : " + node.getText());
                            if (!node.getText().equals("")) {
                                if (!networkNames.contains(node.getText())) {
                                    networkNames.add(node.getText());
                                }
                            }
                        }
                        if (!node.getText().equals("")) {
                            line += node.getText() + "\t";
                        } else {
                            line += "\" \"" + "\t";
                        }
                    }
                    // treeWalkChildren((Element) node);
                    treeWalk((Element) node);
                }
            }
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    /**
     *
     */
    public void print() {
        Collections.sort(v);
        for (int i = 0; i < v.size(); i++) {
            System.out.println(v.get(i).toString());
        }
    }

    private Vector networkNames = new Vector();
    /**
     *
     * @param rssUrl String
     * @return Vector
     */
    public String getNetworkNames() {
        Vector nameVec = new Vector();
        String ret = "";
        treeWalk(document);
//        Collections.sort(networkNames);
        for (int i = 0; i < networkNames.size(); i++) {
            String line = networkNames.get(i).toString();
            StringTokenizer st = new StringTokenizer(line, "\t");
            int cnt = 0;
            while (st.hasMoreTokens()) {
                String token = st.nextToken();
                if (cnt == 0) {
                    if (!nameVec.contains(token)) {
                        ret += token + ",";
                        nameVec.add(token);
                    }
                }
                cnt++;
            }
        }
        if (ret.endsWith(",")) {
            ret = ret.substring(0, ret.length() - 1);
        }
        networkNames = new Vector();
        v = new Vector();
        return ret;
    }
    /**
     *
     * @return Vector
     */
    public Vector networkNames() {
        List l = document.selectNodes("//station/network/name");
        Vector v = new Vector();
        for (int i = 0; i < l.size(); i++) {
            Node no = (Node) l.get(i);
            if (!v.contains(no.getText()) && !no.getText().equals("")) {
                v.add(no.getText());
            }
        }
        return v;
    }

    public Hashtable networkInfo(String networkName){
        Hashtable h = new Hashtable();
        Node n = document.selectSingleNode("//station/network[name='" + networkName +"']");
        h.put("ip",n.selectSingleNode("ip").getText());
        h.put("port",n.selectSingleNode("port").getText());
        h.put("stations",getStations(networkName));
        return h;
    }

    public String getStations(String networkName) {
        String s = "";
        List l = document.selectNodes("//station/network[name='" + networkName +
                                      "']");
        for (int i = 0; i < l.size(); i++) {
            Node no = (Node) l.get(i);
            Element e = no.getParent().element("id");
            s += e.getText() +",";
        }
        if (s.endsWith(",")) {
            s = s.substring(0, s.length() - 1);
        }
        return s;
    }

    public Vector getStationsVec(String networkName) {
        Vector v = new Vector();
        List l = document.selectNodes("//station/network[name='" + networkName +
                                      "']");
        for (int i = 0; i < l.size(); i++) {
            Node no = (Node) l.get(i);
            Element e = no.getParent().element("id");
            v.add(e.getText().toUpperCase());
        }
        return v;
    }

    public String[] getStationInfo(String stationName) {
        String[] latlon = new String[2];
        List l = document.selectNodes("//station[id='" + stationName.toLowerCase() + "']");
        for (int i = 0; i < l.size(); i++) {
            Element e = (Element)l.get(i);
            latlon [0] = e.selectSingleNode("latitude").getText();
            latlon [1] = e.selectSingleNode("longitude").getText();
        }
        return latlon;
    }


    public String[] getNetworkCenter(String networkName){
        String[] latlon = new String[2];
        List l = document.selectNodes("//station/network[name='" + networkName +
                                      "']");
        double minx = 0.0;
        double miny = 0.0;
        double maxx = 0.0;
        double maxy = 0.0;
        boolean start = true;

        for (int i = 0; i < l.size(); i++) {
            Node no = (Node) l.get(i);
            Element e = no.getParent();
//            System.out.println("id=" + e.selectSingleNode("id").getText());;
            String lat = e.selectSingleNode("latitude").getText();
            String lon = e.selectSingleNode("longitude").getText();

            double x = Double.parseDouble(lat);
            double y = Double.parseDouble(lon);

            if(start){
                minx = maxx = x;
                miny = maxy = y;
                start = false;
            }

            if(x<minx)
                minx = x;
            if(x>maxx)
                maxx=x;
            if(y<miny)
                miny=y;
            if(y>maxy)
                maxy=y;
        }
//        System.out.println("minx = " + minx);
//        System.out.println("miny = " + miny);
//        System.out.println("maxx = " + maxx);
//        System.out.println("maxy = " + maxy);

        double center_x = minx + (maxx - minx) / 2;
        double center_y = miny + (maxy - miny) / 2;

//        System.out.println("center_x=" + center_x);
//        System.out.println("center_y=" + center_y);

        latlon[0] =Double.toString(center_x);
        latlon[1] =Double.toString(center_y);
//        System.out.println("NETWORK CENTER = " + latlon[0] + " - " + latlon[1]);
        return latlon;
    }

    /**
     *
     * @return String[]
     */
    public String[] getMapCenter(){
        String[] latlon = new String[2];
        List l = document.selectNodes("//station");
        double minx = 0.0;
        double miny = 0.0;
        double maxx = 0.0;
        double maxy = 0.0;
        boolean start = true;

        for (int i = 0; i < l.size(); i++) {
            Element e = (Element)l.get(i);
            String lat = e.selectSingleNode("latitude").getText();
            String lon = e.selectSingleNode("longitude").getText();

            double x = Double.parseDouble(lat);
            double y = Double.parseDouble(lon);

            if(start){
                minx = maxx = x;
                miny = maxy = y;
                start = false;
            }

            if(x<minx)
                minx = x;
            if(x>maxx)
                maxx=x;
            if(y<miny)
                miny=y;
            if(y>maxy)
                maxy=y;
        }
//        System.out.println("minx = " + minx);
//        System.out.println("miny = " + miny);
//        System.out.println("maxx = " + maxx);
//        System.out.println("maxy = " + maxy);

        double center_x = minx + (maxx - minx) / 2;
        double center_y = miny + (maxy - miny) / 2;

//        System.out.println("center_x=" + center_x);
//        System.out.println("center_y=" + center_y);

        latlon[0] =Double.toString(center_x);
        latlon[1] =Double.toString(center_y);
        return latlon;
    }

    public Vector getAllStationsVec() {
          Vector v = new Vector();
          List l = document.selectNodes("//station");
          for (int i = 0; i < l.size(); i++) {
            Element e = (Element)l.get(i);
              if(e!=null){
                  //System.out.println(e.selectSingleNode("id").getText());
                  v.add(e.selectSingleNode("id").getText().toUpperCase());
              }
          }
          return v;
    }

    /**
     *
     * @param args String[]
     */
    public static void main(String[] args) {
        try {
            GetStationsRSS g = new GetStationsRSS();
            System.out.println("g.getNetworkCenter(crtn_01)=" + g.getNetworkCenter("crtn_01")[0] + " " + g.getNetworkCenter("crtn_01")[1]);
//            Vector v  = g.getAllStationsVec();
//
//            Vector v = g.getStationsVec("cvsrn");
//            Collections.sort(v);
//            for (int i = 0; i < v.size(); i++) {
//                System.out.println( i + " = " + (String)v.get(i));
//                System.out.println("lat=" + g.getStationInfo((String)v.get(i))[0]);
//                System.out.println("lon=" + g.getStationInfo((String)v.get(i))[1]);
//            }


        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}

