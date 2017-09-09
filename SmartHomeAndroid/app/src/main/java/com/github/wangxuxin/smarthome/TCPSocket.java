package com.github.wangxuxin.smarthome;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;

/**
 * Created by a1274 on 2017/2/9.
 */
public class TCPSocket {
    Socket client = null;
    PrintStream out;
    BufferedReader input;
    String echo = "unknown";
    int retrycount = 0;

    void socket(final String name, final int port) {
        echo = "unknown";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //客户端请求与本机在20006端口建立TCP连接
                Log.i("wxxDeb", "connect " + name + ":" + port);
                try {
                    client = new Socket(name, port);
                    client.setSoTimeout(10000);
                    //获取Socket的输出流，用来发送数据到服务端
                    out = new PrintStream(client.getOutputStream());
                    //获取Socket的输入流，用来接收从服务端发送过来的数据
                    input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    while (true) {
                        String temp = input.readLine();
                        if (!"Alive".equals(temp)) {
                            echo = temp;
                        }
                    }
                    //client.close();
                } catch (IOException e) {
                    Log.e("socket", e.toString());
                    System.out.println("Time out, No response");
                }
            }
        });
        thread.start();
        Thread keepAliveThread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        send("isAlive");
                        Thread.sleep(5000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        if (!(client == null)) {
            keepAliveThread.start();
        }
    }

    void send(String str) {
        try {
            out.println(str);
        } catch (Exception err) {
            Log.e("wxxDebug", err.toString());
        }
    }

    String recv(final int MaxRetrycount) {
        final Thread t1 = new Thread() {
            public void run() {
                int i = 0;
                while (i < 1) {
                    try {
                        Thread.sleep(100);
                        i++;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        };
        try {
            retrycount = 0;
            while ("unknown".equals(echo) && retrycount < MaxRetrycount)
            {
                retrycount++;
                if (retrycount >= MaxRetrycount) {
                    return echo;
                }
                t1.start();
                t1.join();
            }
            return echo;
        }catch (Exception err){
            Log.e("wxxDebug", err.toString());
            return err.toString();
        }
    }
}
