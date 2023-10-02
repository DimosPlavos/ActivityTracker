package com.example.app1.Backend;
import java.util.*;
import java.io.IOException;
import java.net.*;
import java.io.*;

public class WorkerHandler implements Runnable {

    public static ArrayList<WorkerHandler> workerHandlers = new ArrayList<>();
    private Socket socket;
    public static int f = 0;
    public ArrayList<ArrayList<Double>> for_reduce = new ArrayList<>();
    public static int counting = 0;

    public ObjectOutputStream outW;
    public ObjectInputStream inW;
    private String workerUsername;

    public WorkerHandler(Socket socket) {
        try {
            this.socket = socket;
            this.outW = new ObjectOutputStream(socket.getOutputStream());
            this.inW = new ObjectInputStream(socket.getInputStream());

            f++;
            workerUsername = "worker" + f;
            workerHandlers.add(this);
        } catch (IOException e) {
            closeEverything(socket, inW, outW);
        }
    }

    @Override
    public void run() {
        HashMap<String, ArrayList<Double>> midresults;

        while (true) {
            try {

                //Workerhandler reads this Object from Worker
                midresults = (HashMap<String, ArrayList<Double>>) inW.readObject();

                String clientUsername = (String) midresults.keySet().toArray()[0];
                ArrayList<Double> worker_results = midresults.get(clientUsername);

                ClientHandler.broadcastToClient(worker_results, clientUsername); 

            } catch (Exception e) {

                System.out.println("Had an exception in the workerhandler");
                e.printStackTrace();

            }
        }

    }

    public void closeEverything(Socket socket, ObjectInputStream inW, ObjectOutputStream outW) {
        // removeworkerHandler();
        try {
            if (inW != null) {
                inW.close();
            }
            if (outW != null) {
                outW.close();

            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
