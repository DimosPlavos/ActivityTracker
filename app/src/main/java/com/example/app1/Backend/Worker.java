package com.example.app1.Backend;
import java.io.*;
import java.net.*;
import java.util.*;

public class Worker {
    public static final String IP_ADDRESS = "192.146.1.8";

    private Socket socket;
    private ObjectInputStream in;
    private ObjectOutputStream out;

    HashMap<String, ArrayList<String>> mymap = new HashMap<>();

    public Worker(Socket socket) {
        try {
            this.socket = socket;
            this.out = new ObjectOutputStream(socket.getOutputStream());
            this.in = new ObjectInputStream(socket.getInputStream());
        } catch (IOException e) {
            closeEverything(socket, in, out);
        }
    }

    // sendMessage function: Worker sends an object to the Server (WorkerHandler)
    public void sendMessage(HashMap<String, ArrayList<Double>> toSend) {
        try {
            out.writeObject(toSend);
            out.flush();

        } catch (IOException e) {
            System.out.println("Had an issue sending message in the worker");
            closeEverything(socket, in, out);
        }

    }

    public void listenForMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                HashMap<String, ArrayList<Waypoint>> listpart = new HashMap<>();

                while (true) {
                    try {

                        //Worker is waiting for an object from the Server
                        listpart = (HashMap<String, ArrayList<Waypoint>>) in.readObject();

                        String clientUsername = (String) listpart.keySet().toArray()[0];
                        ArrayList<Double> worker_results = MapReduce.Process(listpart.get(clientUsername)); //ta intermediate results

                        HashMap<String, ArrayList<Double>> all_results = new HashMap();

                        all_results.put(clientUsername, worker_results); //pernaw ta midresults se hashmap me prwto orisma to onoma tou User
                        sendMessage(all_results);

                    } catch (IOException e) {

                        closeEverything(socket, in, out);
                        break;

                    } catch (ClassNotFoundException e) {
                        System.out.println("There was an error ClassNotFound in worker");
                        e.printStackTrace();
                        break;
                    }
                }
            }
        }).start();

    }

    public void closeEverything(Socket socket, ObjectInputStream in, ObjectOutputStream out) {
        try {
            if (in != null) {
                in.close();
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

    public static void main(String[] args) throws UnknownHostException, IOException {
        Socket socket = new Socket(IP_ADDRESS, 1234);
        Worker worker = new Worker(socket);
        worker.listenForMessage();
    }
}