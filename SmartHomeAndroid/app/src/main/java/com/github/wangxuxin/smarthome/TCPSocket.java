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
    String echo="unknown";

    void socket(final String name, final int port, final String str){
        echo="unknown";
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                //客户端请求与本机在20006端口建立TCP连接
                Log.i("wxxDeb","connect "+name+":"+23333+" "+str);
                Socket client = null;
                try{
                    client = new Socket(name, 23333);
                    client.setSoTimeout(500);
                    //获取Socket的输出流，用来发送数据到服务端
                    PrintStream out = new PrintStream(client.getOutputStream());
                    //获取Socket的输入流，用来接收从服务端发送过来的数据
                    BufferedReader buf =  new BufferedReader(new InputStreamReader(client.getInputStream()));
                    //发送数据到服务端
                    out.println(str);
                    while(true){
                        if(!"".equals(buf.readLine())){
                            Log.i("wxxDebug1",echo);
                            client.close();
                            break;
                        }
                    }
                }catch(IOException e) {
                    Log.e("socket",e.toString());
                    System.out.println("Time out, No response");
                }
            }
        });
        thread.start();
    }
}
