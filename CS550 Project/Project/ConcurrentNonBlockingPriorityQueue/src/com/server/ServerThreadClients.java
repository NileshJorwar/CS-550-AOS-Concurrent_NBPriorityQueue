package com.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.Vector;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import static jdk.nashorn.tools.ShellFunctions.input;

public class ServerThreadClients extends Thread {

    public static volatile boolean continueRunning = true;
    //MultiValueMap mul = new MultiValueMap();
    //ConcurrentLinkedQueue<MultiValueMap> clq = new ConcurrentLinkedQueue<MultiValueMap>();
    ConcurrentLinkedQueue<ServerThreadClients> conc = new ConcurrentLinkedQueue<ServerThreadClients>();
    ConcurrentLinkedQueue<Thread> pThread = new ConcurrentLinkedQueue<Thread>();

    ArrayList<Thread> threadArr = new ArrayList<Thread>();
    ArrayList<Socket> socketArr = new ArrayList<Socket>();
    Long startTime = 0L;
    Long endTime = 0L;
    DataInputStream dataIn = null;
    DataOutputStream dataOut = null;
    Socket socket = null;
    BufferedReader inputReader;
    int priority = 0;
    ArrayList<Integer> priorityArray = new ArrayList<Integer>();
    ArrayList<String> peersArray = new ArrayList<String>();
    String serverInput = "", clientOutput = "";
    //MultiValueMap peersPriority = new MultiValueMap();
    String result = null;
    String prioPeers = null;
    Server_X_Client server;
    String clientMsg[] = new String[3];
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    int noOfClients=0;
    public ServerThreadClients(Socket s, Server_X_Client server, int count, ArrayList<String> peersAraay, ArrayList<Integer> priorityAraay,  ArrayList<Thread> threadArr, ArrayList<Socket> socketArr,int noOfClients) {
        this.socket = s;
        this.server = server;
        this.peersArray = peersAraay;
        this.priorityArray = priorityAraay;
        this.noOfClients=noOfClients;
        
        this.threadArr = threadArr;
        this.socketArr = socketArr;
        System.out.println("count---" + count);
    }

    public void run() {

        //ads the element to the tail of element
        try {
            dataIn = new DataInputStream(socket.getInputStream());
            dataOut = new DataOutputStream(socket.getOutputStream());
            inputReader = new BufferedReader(new InputStreamReader(System.in));

        } catch (IOException e) {
            System.out.println("IO error in server thread");
        }

        try {

            while (!serverInput.equals("Stop")) {
                clientOutput = dataIn.readUTF();
                clientMsg = clientOutput.split(",");
                System.out.println(clientMsg[0] + " says:" + clientMsg[1]);
                System.out.println("before add method");
                add(clientMsg[0], Integer.parseInt(clientMsg[2]));
                display();
            }

        } catch (SocketException e) {
            e.printStackTrace();
        } catch (InterruptedException ex) {
            Logger.getLogger(ServerThreadClients.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(ServerThreadClients.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public void add(String peerName, int priority) throws IOException, InterruptedException {
        System.out.println("Size +++" + server.serverConn.size());

        try {
            startTime = System.nanoTime();
            peersArray.add(peerName);
            priorityArray.add(priority);
            socketArr.add(socket);
            threadArr.add(this);
            System.out.println("Before conThreadStore method");
            //addClients(peerName, priority);
            conThreadStore(peersArray, priorityArray);
            System.out.println("After conThreadStore method");

        } catch (NullPointerException ex) {

            ex.printStackTrace();
        }
    }

    public void conThreadStore(ArrayList<String> client, ArrayList<Integer> prio) throws IOException, InterruptedException {
        System.out.println("No of Concurrent Clients-" + server.serverConn.size());
        String clientArr[] = new String[client.size()];
        int priArr[] = new int[prio.size()];
        Thread thrArr[] = new Thread[prio.size()];
        Socket socArr[] = new Socket[prio.size()];
        try {
            for (int i = 0; i < prio.size(); i++) {

                clientArr[i] = client.get(i);
                priArr[i] = prio.get(i);
                thrArr[i] = threadArr.get(i);
                socArr[i] = socketArr.get(i);
    //            mul.put(clientArr[i], priArr[i]);
                System.out.println("Client Name==" + clientArr[i] + " and its Priority==" + priArr[i]);

            }
            List list;
      //      Set entrySet = mul.entrySet();
        //    Iterator it = entrySet.iterator();
            int i = 0;
          //  System.out.println("Multivalue Map Size--" + mul.size());
            String array[] = clientArr;
            int arr[] = priArr;
            Thread thrArray[] = thrArr;
            int temp = 0;
            String tmp = null;
            Thread tmpThr = null;
            Socket tmpSocket = null;
            for (int i2 = 0; i2 < prio.size(); i2++) {
                for (int j = i2 + 1; j < prio.size(); j++) {
                    if (arr[i2] > arr[j]) {
                        temp = arr[i2];
                        tmp = array[i2];
                        tmpThr = thrArray[i2];
                        tmpSocket = socArr[i2];
                        arr[i2] = arr[j];
                        array[i2] = array[j];
                        thrArray[i2] = thrArray[j];
                        socArr[i2] = socArr[j];
                        arr[j] = temp;
                        array[j] = tmp;
                        thrArray[j] = tmpThr;
                        socArr[j] = tmpSocket;
                    }
                }

            }
            endTime = System.nanoTime();
            System.out.println("Average time for enqueue operation in nanoseconds--" + ((startTime - endTime)) + " for " + arr.length + " users");
            for (int i2 = 0; i2 < arr.length; i2++) {
                System.out.println("Array after Sorted--" + array[i2] + " " + arr[i2] + " " + thrArray[i2] + " " + socArr[i2]);
            }
            
            if (arr.length > noOfClients-1) {
                for (int i2 = arr.length - 1; i2 > 0; i2--) {
                    if (i2 == arr.length - 1) {
                        System.out.println("Before Responding to Higher Priority Client ");
                        Long startTimeDeq = System.nanoTime();
                        IntermediaryThread hpt = new IntermediaryThread(thrArray[i2], socArr[i2], array[i2]);
                        hpt.start();
                        Long endTimeDeq = System.nanoTime();
                        System.out.println("Average time for Dequeue operation in nanoseconds--" + ((startTimeDeq - endTimeDeq)) + " for " + arr.length + " users");
                        Thread.sleep(1000);
                        pThread.add(this);
                    }
                }
            }
        } catch (NullPointerException ex) {
            ex.printStackTrace();
        }

    }

    public void display() {
        System.out.println("No of Clients-" + server.serverConn.size());
        try {
            for (int i = 0; i < peersArray.size(); i++) {
                System.out.println("Client Name-" + peersArray.get(i) + " its Priority +" + priorityArray.get(i));
                System.out.println("Thread Name--" + threadArr.get(i));
            }
        } catch (NullPointerException ex) {

            ex.printStackTrace();
        }
    }
}
