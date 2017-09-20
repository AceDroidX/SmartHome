package com.github.wangxuxin.smarthome;

import android.util.Log;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by a1274 on 2017/2/9.
 */
public class TCPSocket {
    boolean maxSEretry = false;
    boolean maxREretry = false;
    private Socket client = null;
    private DataOutputStream out = null;
    private BufferedReader input = null;
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
                    client = new Socket();
                    SocketAddress socketAddress = new InetSocketAddress(name, port);
                    client.connect(socketAddress, 500);//连不上的0.5毫秒断掉连接
                    //Log.d("wxxDeb", "connect");
                    client.setSoTimeout(10000);
                    //Log.d("wxxDeb", "settimeout");
                    //获取Socket的输出流，用来发送数据到服务端
                    out = new DataOutputStream(client.getOutputStream());
                    Log.d("wxxDebug", "client.getOutputStream() - " + out);
                    //获取Socket的输入流，用来接收从服务端发送过来的数据
                    input = new BufferedReader (new InputStreamReader(client.getInputStream()));
                    Log.d("wxxDebug", "client.getInputStream() - " + input);
                    //client.close();
                } catch (IOException e) {
                    Log.e("socket", e.toString());
                    System.out.println("Time out, No response");
                }
            }
        });
        thread.start();
    }

    void send(final String str, final int MaxRetryms) {
        maxSEretry=false;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (client == null || out == null) {
                    }
                    client.setSoTimeout(MaxRetryms);
                    out.writeBytes(str);
                    maxSEretry=true;
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        });
        thread.start();
        Timer timer = new Timer();// 实例化Timer类
        timer.schedule(new TimerTask() {
            public void run() {
                maxSEretry=true;
            }
        }, MaxRetryms);// 这里百毫秒
        while (!maxSEretry){
        }
        maxSEretry=false;
    }

    String recv(final int MaxRetryms) {
        echo="unknown";
        maxREretry=false;
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    while (client == null || input == null) {
                    }
                    client.setSoTimeout(MaxRetryms);
                    echo=input.readLine();
                    maxREretry=true;
                } catch (IOException e) {
                    e.printStackTrace();
                    echo="unknown";
                }
            }
        });
        thread.start();
        Timer timer = new Timer();// 实例化Timer类
        timer.schedule(new TimerTask() {
            public void run() {
                maxREretry=true;
            }
        }, MaxRetryms);// 这里百毫秒
        while (!maxREretry){
        }
        maxREretry=false;
        Log.d("wxxDebugRE",echo);
        return echo;
    }
}
