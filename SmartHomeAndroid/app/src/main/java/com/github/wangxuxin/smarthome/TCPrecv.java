package com.github.wangxuxin.smarthome;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.net.Socket;

public class TCPrecv {
    private Socket client = null;
    private DataOutputStream out = null;
    private BufferedReader input = null;
    String TCPtype = "unknown";
    String echo = "unknown";
    Context context = null;
    View view = null;

    TCPrecv(DataOutputStream o, BufferedReader i, Socket c, String t) {
        out = o;
        input = i;
        client = c;
        TCPtype = t;
    }

    TCPrecv(DataOutputStream o, BufferedReader i, Socket c, String t, Context con, View v) {
        out = o;
        input = i;
        client = c;
        TCPtype = t;
        context = con;
        view = v;
    }

    void settype() {//TCPtype设置

    }

    void recv() {//注意：context没有做空指针检测    view可能出现错误
        try {
            while (true) {
                echo = input.readLine();
                if ("isSmartLock".equals(TCPtype)) {
                    if (context == null) {
                        Log.e("wxxDebug", "verify时context不能为null");
                        continue;
                    }
                    if ("SmartLock".equals(echo)) {
                        final EditText ipEdit = (EditText) view.findViewById(R.id.ipEdit);
                        final EditText passwordEdit = (EditText) view.findViewById(R.id.passwordEdit);
                        Toast.makeText(context, "连接成功",
                                Toast.LENGTH_SHORT).show();
                        //1、打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
                        SharedPreferences isFirstOpen = context.getSharedPreferences("lock", 0);
                        //2、让setting处于编辑状态
                        SharedPreferences.Editor editor = isFirstOpen.edit();
                        //3、存放数据
                        editor.putString("ip", ipEdit.getText().toString());
                        editor.putString("password", passwordEdit.getText().toString());
                        //4、完成提交
                        editor.apply();

                    /*Intent intent = new Intent();
                    //intent.putExtra("type",type+"/"+l);
                    intent.setClass(MainActivity.this, LockActivity.class);
                    startActivity(intent);*/
                    } else if ("unknown".equals(echo)) {
                        Toast.makeText(context.getApplicationContext(), "连接超时",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context.getApplicationContext(), "未知错误",
                                Toast.LENGTH_SHORT).show();
                    }
                }
                if ("verify".equals(TCPtype)) {
                    final EditText ipEdit = (EditText) view.findViewById(R.id.ipEdit);
                    final EditText passwordEdit = (EditText) view.findViewById(R.id.passwordEdit);
                    if ("notInitialize".equals(echo)) {
                        Toast.makeText(context.getApplicationContext(), "进入设置模式",
                                Toast.LENGTH_LONG).show();
                        //1、打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
                        SharedPreferences isFirstOpen = context.getSharedPreferences("lock", 0);
                        //2、让setting处于编辑状态
                        SharedPreferences.Editor editor = isFirstOpen.edit();
                        //3、存放数据
                        editor.putString("ip", ipEdit.getText().toString());
                        //4、完成提交
                        editor.apply();

                        Intent intent = new Intent();
                        intent.putExtra("type", "1");
                        intent.setClass(context, LockActivity.class);
                        context.startActivity(intent);
                    } else if ("keywrong".equals(echo)) {
                        Toast.makeText(context.getApplicationContext(), "密码错误",
                                Toast.LENGTH_LONG).show();
                    } else if ("keycorrect".equals(echo)) {
                        Toast.makeText(context.getApplicationContext(), "连接成功",
                                Toast.LENGTH_SHORT).show();
                        //1、打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
                        SharedPreferences isFirstOpen = context.getSharedPreferences("lock", 0);
                        //2、让setting处于编辑状态
                        SharedPreferences.Editor editor = isFirstOpen.edit();
                        //3、存放数据
                        editor.putString("ip", ipEdit.getText().toString());
                        editor.putString("password", passwordEdit.getText().toString());
                        //4、完成提交
                        editor.apply();

                        Intent intent = new Intent();
                        //intent.putExtra("type",type+"/"+l);
                        intent.setClass(context, LockActivity.class);
                        context.startActivity(intent);
                    } else if ("unknown".equals(echo)) {
                        Toast.makeText(context.getApplicationContext(), "连接超时",
                                Toast.LENGTH_SHORT).show();
                    } else {
                        Toast.makeText(context.getApplicationContext(), "未知错误",
                                Toast.LENGTH_SHORT).show();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
