package com.dragovorn.manipulator.network;

import com.dragovorn.manipulator.Manipulator;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private int port;

    private ServerSocket socket;

    private Thread serverThread;

    public Server(int port, int backlog) {
        this.port = port;
        try {
            this.socket = new ServerSocket(port, backlog);
        } catch (IOException exception) {
            Manipulator.getInstance().getLogger().severe("***************************** FAILED TO BIND PORT *****************************");
            Manipulator.getInstance().getLogger().severe("* Port: " + this.port + genEnding(String.valueOf(this.port).length()));
            Manipulator.getInstance().getLogger().severe("***************************** FAILED TO BIND PORT *****************************");
        }
    }

    private String genEnding(int strLen) {
        StringBuilder builder = new StringBuilder();

        for (int x = 0;x < 79 - (strLen + 1); x++) {
            builder.append(' ');
        }

        return builder.append('*').toString();
    }

    public int getPort() {
        return this.port;
    }

    public void open() {
        this.serverThread = new Thread("Connection Thread") {

            @Override
            public void run() {
                try {
                    while (socket != null && !socket.isClosed()) {
                        new Connection(socket.accept());
                    }
                } catch (IOException e) { /* DO NOTHING */ }
            }
        };
        this.serverThread.start();
    }

    public void close() {
        if (this.socket == null) {
            return;
        }

        Connection.connectionPool.forEach(Connection::close);
        Connection.connectionPool.clear();

        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

        try {
            this.serverThread.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}