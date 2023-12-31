package com.example.app1.Backend;
import java.io.*;
import java.net.*;
import java.util.Scanner;

public class Server { //Server is Master
    private ServerSocket serverSocket;
    private int workeramount;

    public Server(ServerSocket serverSocket,int workeramount) throws IOException {
        this.serverSocket = serverSocket;
        this.workeramount = workeramount;
    }

    public void startServer() throws IOException {
        //System.out.println("Activity Tracker App has started");
        //System.out.println("Waiting for workers to connect ... ");
        int count = 0;

        try {
            while (!serverSocket.isClosed()) {
                Socket socket1 = serverSocket.accept(); //A new worker has connected
                WorkerHandler workerHandler = new WorkerHandler(socket1);
                Thread thread = new Thread(workerHandler);
                thread.start();
                count++;
                if (count > workeramount - 1) {
                    break;
                }
            }
        } catch (IOException e) {
            closeServerSocket();
        }

        try {
            while (!serverSocket.isClosed()) {
                Socket socket1 = serverSocket.accept(); //A new client has connected
                ClientHandler clientHandler = new ClientHandler(socket1);

                Thread thread = new Thread(clientHandler);
                thread.start();
            }
        } catch (Exception e) {
            e.printStackTrace();
            System.out.println("There was an exception in the SERVER");
            closeServerSocket();

        }
    }

    public void closeServerSocket() {
        try {
            if (serverSocket != null) {
                serverSocket.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void main(String[] args) throws IOException {
        try (Scanner scanner = new Scanner(System.in)) {
            System.out.println("How many workers do you need: ");
            int workeramount = Integer.parseInt(scanner.nextLine());
            ServerSocket serverSocket = new ServerSocket(1234);
            Server server = new Server(serverSocket,workeramount);
            server.startServer();
        } catch (NumberFormatException e) {
            e.printStackTrace();
        }

    }
}