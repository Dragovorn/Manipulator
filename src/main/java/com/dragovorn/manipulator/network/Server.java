package com.dragovorn.manipulator.network;

import com.dragovorn.manipulator.Manipulator;

import java.io.IOException;
import java.net.ServerSocket;

public class Server {

    private int port;

    private ServerSocket socket;

    public Server(int port, int backlog) {
        this.port = port;
        try {
            this.socket = new ServerSocket(port, backlog);
        } catch (IOException exception) {
            Manipulator.getInstance().getLogger().severe("***************************** FAILED TO BIND PORT *****************************");
            Manipulator.getInstance().getLogger().severe("* Port: " + this.port + genEnding(8, String.valueOf(this.port).length()));
            Manipulator.getInstance().getLogger().severe("***************************** FAILED TO BIND ADDRESS *****************************");
        }
    }

    private String genEnding(int main, int strLen) {
        StringBuilder builder = new StringBuilder();

        for (;main < 79 - (strLen + 1); main++) {
            builder.append(' ');
        }

        return builder.append('*').toString();
    }

    public int getPort() {
        return this.port;
    }

    public void open() {
        new Thread("Connection Thread") {

            @Override
            public void run() {
                try {
                    while (socket != null && !socket.isClosed()) {
                        socket.accept();
                    }
                } catch (IOException e) { /* DO NOTHING */ }
            }
        }.start();
    }

    public void close() {
        if (this.socket == null) {
            return;
        }

        try {
            this.socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}