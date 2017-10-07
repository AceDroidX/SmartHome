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

    TCPSocket(String s) {//为了防止空参数
    }

    TCPSocket(Activity a) {
        activity = a;

        context = a.getApplicationContext();
    }

    void connect(final String name, final int port, final String type) {
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
                    if (context == null) {
                        TCPrecv tcprecv = new TCPrecv(out, input, client, type);
                        tcprecv.recv();
                    } else {
                        TCPrecv tcprecv = new TCPrecv(out, input, client, type, activity);
                        tcprecv.recv();
                    }
                    //client.close();
                } catch (Exception e) {
                    Log.e("socket", e.toString());
                    activity.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(context.getApplicationContext(), "连接错误",
                                    Toast.LENGTH_LONG).show();
                        }
                    });
                }
            }
        });
        thread.start();
    }

    void cmd(String str, int MaxTimeOutms) {
        try {
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
}
