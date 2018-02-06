/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author admin
 */
public class IntermediaryThread extends Thread{
    ArrayList<Thread> threadArr = new ArrayList<Thread>();
    Thread priorityThread = new Thread();
    DataInputStream dataIn = null;
    DataOutputStream dataOut = null;
    Socket socket = null;
    BufferedReader inputReader;
    int priority = 0;
    ArrayList<Integer> priorityArray = new ArrayList<Integer>();
    ArrayList<String> peersArray = new ArrayList<String>();
    String serverInput = "", clientOutput = "";
    String result = null;
    String prioPeers = null;
    Server_X_Client server;
    String clientMsg[] = new String[3];
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));
    String clientName="";
    public IntermediaryThread(Thread priorityThread,Socket socket,String clientName) {
        this.priorityThread=priorityThread;
        this.socket=socket;
        this.clientName=clientName;
    }
    synchronized public void run(){
        try {
            System.out.println("Hi,,,,"+clientName+"in Intermediary");
            HigherPriorityThread hpt = new HigherPriorityThread(priorityThread, socket,clientName);
        } catch (IOException ex) {
            Logger.getLogger(IntermediaryThread.class.getName()).log(Level.SEVERE, null, ex);
        }
                    
    }
}
