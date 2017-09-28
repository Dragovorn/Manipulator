package com.dragovorn.manipulator.network;

import com.dragovorn.manipulator.Manipulator;
import io.netty.buffer.ByteBuf;
import io.netty.buffer.ByteBufAllocator;
import io.netty.buffer.EmptyByteBuf;

import java.io.DataInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.Socket;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;

public class Connection implements Runnable {

    private static List<Connection> connectionPool = new ArrayList<>();

    private Socket socket;

    private InetAddress address;

    private DataInputStream input;

    private OutputStream output;

    private Thread thread;

    Connection(Socket socket) {
        this.socket = socket;
        this.address = this.socket.getInetAddress();
        try {
            this.input = new DataInputStream(this.socket.getInputStream());
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
        ByteBuf buffer = new EmptyByteBuf(ByteBufAllocator.DEFAULT);
        buffer.writeBoolean(false);
        buffer.writeInt(0);
        buffer.writeCharSequence("THIS IS FROM A MANIPULATOR SERVER", Charset.forName("UTF-8"));

        try {
            this.output.write(buffer.array());
        } catch (IOException e) {
            e.printStackTrace();
        }

        Manipulator.getInstance().getLogger().info("Sent rejection packet");

        close();
    }
}