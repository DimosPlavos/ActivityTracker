package com.example.app1.Backend;
import java.util.ArrayList;

public class Distance {

    Distance() {
    }

    public static double calculateDistance(ArrayList<double[]> locations) {
        double distance = 0.0;
        double prevLat = 0.0, prevLon = 0.0;
        for (double[] location : locations) {
            double lat = location[0];
            double lon = location[1];
            if (prevLat != 0.0 && prevLon != 0.0) {
                distance += distance(prevLat, prevLon, lat, lon);
            }
            prevLat = lat;
            prevLon = lon;
        }
        return distance;
    }

    public static double distance(double lat1, double lon1, double lat2, double lon2) {

        double R = 6371; // Earth radius in km
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        double distance = R * c;
        return distance;
    }
}
