/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.server;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.net.UnknownHostException;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;
import java.util.ArrayList;

public class HigherPriorityThread extends JFrame implements ActionListener {

    ArrayList<Thread> threadArr = new ArrayList<Thread>();
    Thread priorityThread = new Thread();
    DataInputStream dataIn = null;
    DataOutputStream dataOut = null;
    
    BufferedReader inputReader;
    int priority = 0;
    ArrayList<Integer> priorityArray = new ArrayList<Integer>();
    ArrayList<String> peersArray = new ArrayList<String>();
    String serverInput = "", clientOutput = "";
    String result = null;
    String prioPeers = null;

    String clientMsg[] = new String[3];
    BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

    static Socket socket;
    JPanel panel;
    JTextField NewMsg;
    JTextArea ChatHistory;
    JButton Send;
    String clientName = "";
    
    public HigherPriorityThread(Thread priorityThread, Socket socket,String clientName) throws UnknownHostException, IOException {
        this.priorityThread = priorityThread;
        System.out.println("hi higher");
        this.socket = socket;
        panel = new JPanel();
        NewMsg = new JTextField();
        ChatHistory = new JTextArea();
        Send = new JButton("Send");
        this.setSize(500, 500);
        this.setVisible(true);
        setDefaultCloseOperation(EXIT_ON_CLOSE);
        panel.setLayout(null);
        this.add(panel);
        ChatHistory.setBounds(20, 20, 450, 360);
        panel.add(ChatHistory);
        NewMsg.setBounds(20, 400, 340, 30);
        panel.add(NewMsg);
        Send.setBounds(375, 400, 95, 30);
        panel.add(Send);
        Send.addActionListener(this);
        this.clientName=clientName;
        /*
         * for remote pc use ip address of server with function
         * InetAddress.getByName("Provide ip here")
         * ChatHistory.setText("Connected to Server");
         */
        ChatHistory.setText("Welcome !!!!");
        System.out.println("");
        this.setTitle("Server output for Client "+clientName);
        while (true) {
            try {
                DataInputStream dis = new DataInputStream(socket.getInputStream());
                String response = dis.readUTF();
                ChatHistory.setText(ChatHistory.getText() + '\n'
                        + response.split(",")[1] + response.split(",")[0]);
            } catch (Exception e1) {
                ChatHistory.setText(ChatHistory.getText() + '\n'
                        + "Message sending fail:Network Error");
                try {
                    Thread.sleep(3000);
                    System.exit(0);
                } catch (InterruptedException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        // TODO Auto-generated method stub

        if ((e.getSource() == Send) && (NewMsg.getText() != "")) {

            ChatHistory.setText(ChatHistory.getText() + '\n' + "Admin says" + ": "
                    + NewMsg.getText());
            try {
                DataOutputStream dos = new DataOutputStream(
                        socket.getOutputStream());
                dos.writeUTF(NewMsg.getText()+ "," + "Admin Says "  + "," + priority);
            } catch (Exception e1) {
                ChatHistory.setText(ChatHistory.getText() + '\n'
                        + "Message sending fail:Network Error");
                try {
                    Thread.sleep(3000);
                    System.exit(0);
                } catch (InterruptedException e2) {
                    // TODO Auto-generated catch block
                    e2.printStackTrace();
                }
            }
            NewMsg.setText("");
        }
    }

    

}
