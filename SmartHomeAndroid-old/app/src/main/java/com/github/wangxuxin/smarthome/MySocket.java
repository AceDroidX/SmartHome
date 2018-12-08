package com.github.wangxuxin.smarthome;

import android.app.Application;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class MySocket extends Application {
    Socket socket = null;
    DataOutputStream out = null;
    BufferedReader input = null;

    public Socket getSocket() {
        return socket;
    }

    public DataOutputStream getOut() {
        return out;
    }

    public BufferedReader getInput() {
        return input;
    }

    public void setSocket(Socket socket, DataOutputStream out, BufferedReader input) {
        this.socket = socket;
        this.out = out;
        this.input = input;
    }
}
