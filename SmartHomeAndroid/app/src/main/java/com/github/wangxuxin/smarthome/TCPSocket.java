package com.github.wangxuxin.smarthome;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.SocketAddress;

import static java.lang.Thread.sleep;

/**
 * Created by a1274 on 2017/2/9.
 */
public class TCPSocket {
    boolean maxSEretry = false;
    boolean maxREretry = false;
    private Socket client = null;
    private DataOutputStream out = null;
    private BufferedReader input = null;
    private int retrycount = 0;
    Context context = null;
    Activity activity;
    TCPrecv tcprecv;

    TCPSocket(Activity a ,int get) {
        activity = a;
        context = a.getApplicationContext();
        this.client = ((MySocket)activity.getApplication()).getSocket();
        this.out = ((MySocket)activity.getApplication()).getOut();
        this.input = ((MySocket)activity.getApplication()).getInput();
    }

    TCPSocket(Activity a) {
        activity = a;

        context = a.getApplicationContext();
    }

    void send(String str, int MaxTimeOutms) {
        try {
            if (MaxTimeOutms == 0) {
                MaxTimeOutms = client.getSoTimeout();
            }
            client.setSoTimeout(MaxTimeOutms);
            out.writeBytes(str);
        } catch (IOException e) {
            e.printStackTrace();
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context.getApplicationContext(), "发送命令错误",
                            Toast.LENGTH_LONG).show();
                }
            });
        }
    }

    void recv() {
        if (context == null) {
            tcprecv = new TCPrecv(out, input, client, this);
            tcprecv.recv();
        } else {
            tcprecv = new TCPrecv(out, input, client, this, activity);
            tcprecv.recv();
        }
    }

    void keepAlive() {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    try {
                        send("keepAlive", client.getSoTimeout());
                        sleep(client.getSoTimeout() / 2);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        thread.start();
    }

    void cmd(String str) {
        send(str, 0);
    }

    void connect(final String name, final int port, final String str, final int MaxTimeOutms) {
        if(client.isConnected()){
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    Toast.makeText(context.getApplicationContext(), "socket已连接:socket",
                            Toast.LENGTH_LONG).show();
                }
            });
            return;
        }

        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //客户端请求与本机在20006端口建立TCP连接
                Log.i("wxxDeb", "connect " + name + ":" + port);
                try {
                    client = new Socket();
                    SocketAddress socketAddress = new InetSocketAddress(name, port);
                    client.connect(socketAddress, 1000);//连不上的0.5毫秒断掉连接
                    //Log.d("wxxDeb", "connect");
                    client.setSoTimeout(5000);
                    //Log.d("wxxDeb", "settimeout");
                    //获取Socket的输出流，用来发送数据到服务端
                    out = new DataOutputStream(client.getOutputStream());
                    Log.d("wxxDebug", "client.getOutputStream() - " + out);
                    //获取Socket的输入流，用来接收从服务端发送过来的数据
                    input = new BufferedReader(new InputStreamReader(client.getInputStream()));
                    Log.d("wxxDebug", "client.getInputStream() - " + input);
                    keepAlive();
                    send(str, MaxTimeOutms);
                    ((MySocket) activity.getApplication()).setSocket(client, out, input);
                    recv();
                    //client.close();
                } catch (Exception e) {
                    Log.e("socket", e.toString());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context.getApplicationContext(), "连接错误:socket",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        thread.start();
    }
}
