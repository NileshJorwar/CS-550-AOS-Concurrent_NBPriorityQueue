package com.server;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Properties;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Server_X_Client {

    static ArrayList<ServerThreadClients> serverConn = new ArrayList<ServerThreadClients>();
    static ConcurrentLinkedQueue<ServerThreadClients> concurrentThreads = new ConcurrentLinkedQueue<ServerThreadClients>();
    static ArrayList<Thread> threadArr = new ArrayList<Thread>();
    static ArrayList<Socket> socketArr = new ArrayList<Socket>();
    private ExecutorService executorService = Executors.newFixedThreadPool(10);

    public static void main(String args[]) throws FileNotFoundException, IOException {
        Properties prop = new Properties();
        InputStream inputP = null;

        int count = 0;
        ArrayList<Integer> priorityArray = new ArrayList<Integer>();
        ArrayList<String> peersArray = new ArrayList<String>();
        ExecutorService executorService = Executors.newFixedThreadPool(10);

//        MultiValueMap mul = new MultiValueMap();
//        ConcurrentLinkedQueue<MultiValueMap> clq = new ConcurrentLinkedQueue<MultiValueMap>();

        inputP = new FileInputStream("com/server/config.properties");
        prop.load(inputP);

        int noOfClients = Integer.parseInt((prop.getProperty("concurrentClients")));
        System.out.println("No of Concurrent Clients" + noOfClients);
        //int port = Integer.parseInt(args[0]);
        Socket socket = null;
        ServerSocket serverSocket = null;
        System.out.println("Server Listening......");
        try {

            serverSocket = new ServerSocket(Integer.parseInt(args[0])); // can also use static final PORT_NUM , when defined

        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Server error");

        }

        while (true) {
            try {
                socket = serverSocket.accept();
                System.out.println("connection Established....");
                ++count;
                ServerThreadClients st = new ServerThreadClients(socket, new Server_X_Client(), count, peersArray, priorityArray, threadArr, socketArr,noOfClients);
                //executorService.submit(st);
                st.start();

                serverConn.add(st);

            } catch (Exception e) {
                e.printStackTrace();
                System.out.println("Connection Error");
            }
        }

    }
}
