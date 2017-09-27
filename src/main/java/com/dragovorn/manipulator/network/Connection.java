package com.dragovorn.manipulator.network;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;

public class Connection implements Runnable {

    private Socket socket;

    private InetAddress address;

    private BufferedReader input;

    private OutputStream output;

    private Thread thread;

    Connection(Socket socket) {
        this.socket = socket;
        this.address = this.socket.getInetAddress();
        try {
            this.input = new BufferedReader(new InputStreamReader(this.socket.getInputStream()));
            this.output = this.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        this.thread = new Thread(this, this.address.getHostName() + " Connection");
        this.thread.start();
    }

    public InetAddress getAddress() {
        return this.address;
    }

    void close() {
        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void run() {
        String line;

        try {
            while ((line = this.input.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}