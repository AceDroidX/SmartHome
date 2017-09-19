package com.github.wangxuxin.smarthome;

import android.util.Log;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.net.Socket;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by a1274 on 2017/2/9.
 */
public class TCPSocket {
    boolean maxSEretry = false;
    boolean maxREretry = false;
    private Socket client = null;
    private PrintStream out;
    private BufferedReader input;
    String echo = "unknown";
    private int retrycount = 0;

    void connect(final String name, final int port) {
        echo = "unknown";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //客户端请求与本机在20006端口建立TCP连接
                Log.i("wxxDeb", "connect " + name + ":" + port);
                try {
                    client = new Socket(name, port);
                    Log.d("wxxDeb", "connect");
                    client.setSoTimeout(2000);
                    Log.d("wxxDeb", "settimeout");
                    //获取Socket的输出流，用来发送数据到服务端
                    out = new PrintStream(client.getOutputStream());
                    Log.d("wxxDebug", "client.getOutputStream() - " + out);
                    //获取Socket的输入流，用来接收从服务端发送过来的数据
                    input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    Log.d("wxxDebug", "client.getInputStream() - " + input);
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
                        send("isAlive",1000);
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

    void send(String str,int MaxRetryms) {
        Timer timer = new Timer();// 实例化Timer类
        timer.schedule(new TimerTask() {
            public void run() {
                maxSEretry = true;
            }
        }, MaxRetryms);// 这里百毫秒
        Log.d("wxxDebugSE", String.valueOf(MaxRetryms));
        try {
            while(!maxSEretry){
                if(out==null){
                    continue;
                }
                out.println(str);
                Log.d("wxxDebugSE","send "+str);
                maxSEretry = false;
                return;
            }
            Log.d("wxxDebugSE","time out "+str);
        } catch (Exception err) {
            Log.e("wxxDebug", err.toString());
        }
    }

    String recv(int MaxRetryms) {
        String tmpecho;
        Timer timer = new Timer();// 实例化Timer类
        timer.schedule(new TimerTask() {
            public void run() {
                maxREretry = true;
            }
        }, MaxRetryms);// 这里百毫秒
        Log.d("wxxDebugRE", String.valueOf(MaxRetryms));
        while(!maxREretry){
            if("unknown".equals(echo)){
                continue;
            }
            tmpecho=echo;
            echo="unknown";
            maxREretry = false;
            Log.d("wxxDebugRE", "recv"+tmpecho);
            return tmpecho;
        }
        echo="unknown";
        maxREretry = false;
        Log.d("wxxDebugRE", "recv "+echo);
        return echo;
    }
}
