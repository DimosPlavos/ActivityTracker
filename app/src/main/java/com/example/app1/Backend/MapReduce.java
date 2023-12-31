package com.example.app1.Backend;
import java.util.*;
import java.time.Instant;
import java.time.temporal.ChronoUnit;

public class MapReduce {

    public static ArrayList<ArrayList<Waypoint>> Mapping(String pathname, ArrayList<Waypoint> waypoints) {

        int partitionSize = 10;

        if (waypoints.size() == 1) { //not enough waypooints 
            partitionSize += 1;

        }

        // create a list to hold the partitions
        ArrayList<ArrayList<Waypoint>> partitions = new ArrayList<>();

        // divide the waypoints list into partitions of size partitionSize
        for (int i = 0; i < waypoints.size(); i += partitionSize) {

            int endIndex = Math.min(i + partitionSize, waypoints.size());

            if (i + partitionSize < waypoints.size() - 1) {
                endIndex += 1;
            }

            ArrayList<Waypoint> partition = new ArrayList<>(waypoints.subList(i, endIndex));

            partitions.add(partition);
        }

        return partitions;
    }

    // Reduce function for the final results of the whole route
    public static ArrayList<Double> Reduce(String out_key, ArrayList<ArrayList<Double>> inter) {

        Double total_elevation = 0.0;
        Double total_distance = 0.0;
        Double total_time = 0.0;
        Double total_average_speed = 0.0;

        ArrayList<Double> finallized = new ArrayList<>();

        for (int i = 0; i < inter.size(); i++) {
            total_elevation += inter.get(i).get(0);
            total_distance += inter.get(i).get(1);
            total_time += inter.get(i).get(2);
        }

        total_average_speed = total_distance / (total_time/60);

        finallized.add(total_elevation);
        finallized.add(total_distance);
        finallized.add(total_time);
        finallized.add(total_average_speed);

        return finallized;
    }

    //Process function for the results of each chunk separately
    public static ArrayList<Double> Process(ArrayList<Waypoint> Waypoints) {

        ArrayList<Double> results_here = new ArrayList<Double>();

        Double climb = 0.0;
        Double distance = 0.0;
        Double time = 0.0;
        Double speed = 0.0;

        if (Waypoints.size() == 1) {
            return null;
        }

        for (int i = 1; i < Waypoints.size(); i++) {

            Waypoint current_Waypoint = Waypoints.get(i);
            Waypoint previous_Waypoint = Waypoints.get(i - 1);

            // climb
            Double cur_ele = current_Waypoint.getEle();
            Double prev_ele = previous_Waypoint.getEle();

            Double ele_dif = cur_ele - prev_ele;

            if (ele_dif > 0) {
                climb += ele_dif;
            }

            // distance
            double cur_lat = current_Waypoint.getLattitude();
            double cur_lon = current_Waypoint.getLongitude();

            double prev_lat = previous_Waypoint.getLattitude();
            double prev_lon = previous_Waypoint.getLongitude();

            distance += Distance.distance(cur_lat, cur_lon, prev_lat, prev_lon);

            // time
            time += time_dif(current_Waypoint.getTime(), previous_Waypoint.getTime());
        }

        speed = distance / (time/60); // km/hour

        results_here.add(climb);
        results_here.add(distance);
        results_here.add(time);
        results_here.add(speed);

        return results_here;

    }

    public static Double time_dif(String cur_time_str, String prev_time_str) {

        Instant cur_time = Instant.parse(cur_time_str);
        Instant prev_time = Instant.parse(prev_time_str);

        long time_diff_seconds = ChronoUnit.SECONDS.between(prev_time, cur_time);
        double time_diff_seconds_double = (double) time_diff_seconds;

        return time_diff_seconds_double/60; //returns minutes

    }

}
