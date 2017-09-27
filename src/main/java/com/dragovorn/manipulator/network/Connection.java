package com.dragovorn.manipulator.network;

import com.dragovorn.manipulator.Manipulator;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

public class Connection implements Runnable {

    private static List<Connection> connectionPool = new ArrayList<>();

    private Socket socket;

    private InetAddress address;

    private InputStream input;

    private OutputStream output;

    private Thread thread;

    Connection(Socket socket) {
        this.socket = socket;
        this.address = this.socket.getInetAddress();
        try {
            this.input = this.socket.getInputStream();
            this.output = this.socket.getOutputStream();
        } catch (IOException e) {
            e.printStackTrace();
        }
        Manipulator.getInstance().getLogger().info("Incoming connection @ " + this.socket.getInetAddress().getHostAddress());
        this.thread = new Thread(this, this.address.getHostAddress() + " Connection");
        this.thread.start();

        connectionPool.add(this);
    }

    public InetAddress getAddress() {
        return this.address;
    }

    void close() {
        Manipulator.getInstance().getLogger().info("Killing connection @ " + this.socket.getInetAddress().getHostAddress());

        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.thread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        connectionPool.remove(this);
    }

    @Override
    public void run() {
        try {
            int numByte = this.input.available();
            byte[] buf = new byte[numByte];

            this.input.read(buf, 2, 3); // FIXME throws java.lang.ArrayIndexOutOfBoundsException: length == 3 off == 2 buffer length == 0

            for (byte d : buf) {
                System.out.println((char)d+":" + d);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        close();
    }
}