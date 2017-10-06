package com.github.wangxuxin.smarthome;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class TCPrecv {
    private Socket client = null;
    private DataOutputStream out = null;
    private BufferedReader input = null;
    String TCPtype = "unknown";
    String echo = "unknown";

    TCPrecv(DataOutputStream o, BufferedReader i, Socket c, String t) {
        out = o;
        input = i;
        client = c;
        TCPtype = t;
    }

    void settype() {//TCPtype设置

    }

    void recv() {
        try {
            while (true) {
                echo = input.readLine();
                if ("verify".equals(TCPtype)) {

                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
