package com.example.app1.Activity;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.ArrayList;

public class Client {

    private Socket socket;
    private BufferedWriter bufferedWriter;
    private ObjectInputStream in;
    private String gpxfile;


    public Client(Socket socket, String gpxfile) {
        try {
            this.socket = socket;
            this.gpxfile = gpxfile;
            in = new ObjectInputStream(socket.getInputStream());
            this.bufferedWriter = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            closeEverything(socket, in, bufferedWriter);
        }
    }

    public void sendMessage() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //writes the gpxfile to the Server (ClientHandler)
                    bufferedWriter.write(gpxfile);
                    bufferedWriter.newLine();
                    bufferedWriter.flush();

                } catch (IOException e) {
                    e.printStackTrace();
                    System.out.println("There was an error in the Client sendMessage");

                }

            }
        }).start();
    }

    public synchronized void listenForMessage(ResultCallback callback) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                ArrayList<Double> results;
                try {
                    results = (ArrayList<Double>) in.readObject();
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                if (results.size() == 8) {
                    callback.onResultsReceived(results);
                }
            }
        }).start();
    }

    public void closeEverything(Socket socket, ObjectInputStream in, BufferedWriter bufferedWriter) {
        try {
            System.out.println("Shutting down everything in client");
            if (in != null) {
                in.close();
            }
            if (bufferedWriter != null) {
                bufferedWriter.close();

            }
            if (socket != null) {
                socket.close();
            }

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}