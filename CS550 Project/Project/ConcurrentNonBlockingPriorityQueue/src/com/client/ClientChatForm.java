/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.client;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.print.PrinterException;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.InetAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.sql.Time;
import javax.swing.JButton;
import javax.swing.JFrame;
import static javax.swing.JFrame.EXIT_ON_CLOSE;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;

public class ClientChatForm extends JFrame implements ActionListener {

    static Socket conn;
    JPanel panel;
    JTextField NewMsg;
    JTextArea ChatHistory;
    JButton Send;
    String clientName="";
    int priority =0;
    public ClientChatForm(String client, String port, String prio) throws UnknownHostException, IOException {
         priority = Integer.parseInt(prio);
        clientName = client;
        int portNo= Integer.parseInt(port);
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
        conn = new Socket(InetAddress.getLocalHost(), portNo);

        /*
         * for remote pc use ip address of server with function
         * InetAddress.getByName("Provide ip here")
         * ChatHistory.setText("Connected to Server");
         */
        ChatHistory.setText(clientName+" is connected to Server:"+InetAddress.getLocalHost());
        System.out.println("");
        this.setTitle(clientName);
        while (true) {
            try {
                DataInputStream dis = new DataInputStream(conn.getInputStream());
                String response = dis.readUTF();
                ChatHistory.setText(ChatHistory.getText() + '\n'
                        + response.split(",")[1] + ":"+ response.split(",")[0]);
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

            ChatHistory.setText(ChatHistory.getText() + '\n' + clientName+": "
                    + NewMsg.getText());
            try {
                DataOutputStream dos = new DataOutputStream(
                        conn.getOutputStream());
                dos.writeUTF(clientName + "," + NewMsg.getText() + "," + priority);
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

    public static void main(String[] args) throws UnknownHostException,
            IOException {
        ClientChatForm chatForm = new ClientChatForm(args[0], args[1], args[2]);
    }
}
