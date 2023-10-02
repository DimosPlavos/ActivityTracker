package com.example.app1.Backend;
import java.util.ArrayList;
import java.io.File;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

public class GPXparser {

    // defaut constuctor
    GPXparser() {
    }

    // function parseGpx which analyzes the gpx file
    public ArrayList<Waypoint> parseGpx(File file) {

        ArrayList<Waypoint> waypoints = new ArrayList<>();

        try {
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
            Document doc = builder.parse(file);
            NodeList wptList = doc.getElementsByTagName("wpt");
            for (int i = 0; i < wptList.getLength(); i++) {
                Element wptElement = (Element) wptList.item(i);
                double lat = Double.parseDouble(wptElement.getAttribute("lat"));
                double lon = Double.parseDouble(wptElement.getAttribute("lon"));
                String eleString = wptElement.getElementsByTagName("ele").item(0).getTextContent();
                double ele = Double.parseDouble(eleString);
                String timeString = wptElement.getElementsByTagName("time").item(0).getTextContent();
                waypoints.add(new Waypoint(lat, lon, timeString, ele));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return waypoints;
    }
}

