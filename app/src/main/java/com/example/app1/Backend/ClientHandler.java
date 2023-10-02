package com.example.app1.Backend;

import java.util.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.net.*;
import java.io.*;

public class ClientHandler implements Runnable {

    public static HashMap<String, ClientHandler> clientHandlers = new HashMap<>();
    private Socket socket;
    public static int w = 0;
    private BufferedReader bufferedReaderC;
    public ObjectOutputStream out;
    public static int partition_size;
    public static HashMap<String, ArrayList<ArrayList<Waypoint>>> Userlog = new HashMap();
    public static HashMap<String, ArrayList<ArrayList<Double>>> for_reduce;
    public static ArrayList<Double> totals = new ArrayList<>();
    private String clientUsername;

    public ClientHandler(Socket socket) {
        try {

            this.for_reduce = new HashMap<String, ArrayList<ArrayList<Double>>>();
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            this.bufferedReaderC = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            if(totals.isEmpty()){
                totals.add(0.0);
                totals.add(0.0);
                totals.add(0.0);
                totals.add(0.0);
            }
            w++;
            clientUsername = "user" + w;
            clientHandlers.put(clientUsername, this);
            

        } catch (IOException e) {
            closeEverything(socket, bufferedReaderC, out);
        }
    }

    @Override
    public void run() {
        String messageFromClient;
            try {
                messageFromClient = bufferedReaderC.readLine(); //kanei read to onoma toy gpxfile apo ton client

                //parse to gpx file
                GPXparser gp = new GPXparser();
                ArrayList<Waypoint> wpts = new ArrayList<>();
                wpts = gp.parseGpx(new File(messageFromClient));

                //dhmiourgei ta chunks
                ArrayList<ArrayList<Waypoint>> partitions = MapReduce.Mapping(messageFromClient, wpts);
                partition_size = partitions.size();
                Userlog.put(clientUsername, partitions);

                broadcastToWorker(clientUsername, partitions); //broadcast ta chunks stous workers

            } catch (Exception e) {
                System.out.println("There was an exception in the ClientHandler");

                e.printStackTrace();
                closeEverything(socket, bufferedReaderC, out);
            }
        

    }

    public void broadcastToWorker(String clientUsername, ArrayList<ArrayList<Waypoint>> waypoints) { // steilto se olous tous workers
        int size = WorkerHandler.workerHandlers.size();

        for (int i = 0; i < waypoints.size(); i++) {
            try {
                int to_worker = i % size;

                ArrayList<Waypoint> to_proccess = waypoints.get(i);
                HashMap<String, ArrayList<Waypoint>> sendingHash = new HashMap<>();

                sendingHash.put(clientUsername, to_proccess);

                WorkerHandler.workerHandlers.get(to_worker).outW.writeObject(sendingHash);
                WorkerHandler.workerHandlers.get(to_worker).outW.flush();

            } catch (IOException e) {
                closeEverything(socket, bufferedReaderC, out);
                System.out.println("Had an issue while distributing in the Client Hanlder");
                e.printStackTrace();
                break;
            } catch (Exception e) {
                System.out.println("Had an issue while distributing in the Client Hanlder");
                e.printStackTrace();
                closeEverything(socket, bufferedReaderC, out);
            }

        }
    }

    public synchronized static void broadcastToClient(ArrayList<Double> results, String clientName) {
        if (for_reduce.containsKey(clientName)) {
            // add to the existing list
            for_reduce.get(clientName).add(results);
        } else {
            // if the key doesn't exist, create a new entry with the results list
            ArrayList<ArrayList<Double>> first_input = new ArrayList<>();
            first_input.add(results);
            for_reduce.put(clientName, first_input);
        }

        if (for_reduce.get(clientName).size() == Userlog.get(clientName).size()) {
            try {
                ArrayList<Double> tosend = MapReduce.Reduce(clientName, for_reduce.get(clientName));

                totals.set(0, tosend.get(0)+totals.get(0)); //sto totals(0)=climb 
                totals.set(1, tosend.get(1)+totals.get(1)); //sto totals(1)=distance 
                totals.set(2, tosend.get(2)+totals.get(2)); //sto totals(2)=time
                totals.set(3,totals.get(3)+1.0); //sto totals(4)=arithmos apo clients

                tosend.addAll(totals);
                ClientHandler.clientHandlers.get(clientName).out.writeObject(tosend);
                ClientHandler.clientHandlers.get(clientName).out.flush();

                ClientHandler.clientHandlers.remove(clientName);

            } catch (Exception e) {

                System.out.println("There was an error in the BroadcasttoClinet in the ClientHandler");
                e.printStackTrace();

            }
        }

    }

    public void closeEverything(Socket socket, BufferedReader bufferedReader, ObjectOutputStream out) {
        // removeClientHandler();
        try {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
            if (out != null) {
                out.close();

            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}