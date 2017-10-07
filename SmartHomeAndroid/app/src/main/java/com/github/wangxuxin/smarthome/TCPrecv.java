package com.github.wangxuxin.smarthome;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPrecv {
    private Socket client = null;
    private DataOutputStream out = null;
    private BufferedReader input = null;
    String TCPtype = "unknown";
    String echo = "unknown";
    Context context = null;
    View view = null;
    Activity activity;

    TCPrecv(DataOutputStream o, BufferedReader i, Socket c, String t) {
        out = o;
        input = i;
        client = c;
        TCPtype = t;
    }

    TCPrecv(DataOutputStream o, BufferedReader i, Socket c, String t, Activity a) {
        out = o;
        input = i;
        client = c;
        TCPtype = t;
        activity = a;

        context = a.getApplicationContext();
        view = a.getCurrentFocus();
    }

    void settype() {//TCPtype设置

    }

    private void makeToastOnUI(final String str, final int i) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                Toast.makeText(context, str, i).show();
            }
        });
    }

    void close() {
        try {
            client.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    void recv() {//注意：context没有做空指针检测
        try {
            while (client.isConnected()) {
                echo = input.readLine();
                if ("unknown".equals(echo)) {
                    makeToastOnUI("服务端未知错误", Toast.LENGTH_LONG);
                    continue;
                } else if ("keywrong".equals(echo)) {
                    makeToastOnUI("密码错误", Toast.LENGTH_LONG);
                    continue;
                }else if("verifyerror".equals(echo)){
                    makeToastOnUI("验证出现未知错误 请重新验证", Toast.LENGTH_LONG);
                    continue;
                }

                if ("verify".equals(TCPtype)) {
                    verifyType();
                } else if ("init".equals(TCPtype)) {
                    initType();
                } else if ("door_switch".equals(TCPtype)) {
                    doorSwitchType();
                } else if ("reset".equals(TCPtype)) {
                    resetType();
                } else if ("whiteLight_switch".equals(TCPtype)) {
                    whiteLightSwitchType();
                } else {
                    Log.d("wxxDebug", "未知数据类型" + echo);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
            makeToastOnUI("连接错误", Toast.LENGTH_LONG);
        }
    }

    void verifyType(){
        if (context == null) {
            Log.e("wxxDebug", "verify时context不能为null");
            return;
        }
        final EditText ipEdit = (EditText) view.findViewById(R.id.ipEdit);
        final EditText passwordEdit = (EditText) view.findViewById(R.id.passwordEdit);
        if ("notInitialize".equals(echo)) {
            makeToastOnUI("进入设置模式", Toast.LENGTH_LONG);
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
        } else if ("keycorrect".equals(echo)) {
            makeToastOnUI("连接成功", Toast.LENGTH_SHORT);
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
        } else {
            makeToastOnUI("未知错误", Toast.LENGTH_SHORT);
        }
    }

    void initType(){
        final EditText keysetEdit = (EditText) view.findViewById(R.id.keysetEdit);
        if ("alreadysetkey".equals(echo)) {
            makeToastOnUI("已经设置", Toast.LENGTH_LONG);
        } else if ("setkeyerror".equals(echo) || "thekeycantbe0".equals(echo)) {
            makeToastOnUI("设置错误", Toast.LENGTH_LONG);
        } else if ("setkeysuccess".equals(echo)) {
            makeToastOnUI("设置成功", Toast.LENGTH_LONG);
            //1、打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
            SharedPreferences isFirstOpen = context.getSharedPreferences("lock", 0);
            //2、让setting处于编辑状态
            SharedPreferences.Editor editor = isFirstOpen.edit();
            //3、存放数据
            editor.putString("password", keysetEdit.getText().toString());
            //4、完成提交
            editor.apply();

            activity.setContentView(R.layout.activity_lock);
        } else {
            makeToastOnUI("连接超时", Toast.LENGTH_LONG);
        }
    }

    void doorSwitchType(){
        if ("notInitialize".equals(echo)) {
            makeToastOnUI("设备未初始化", Toast.LENGTH_LONG);
        } else if ("lock".equals(echo)) {
            makeToastOnUI("已锁住", Toast.LENGTH_LONG);
        } else if ("unlock".equals(echo)) {
            makeToastOnUI("已开启", Toast.LENGTH_LONG);
        } else {
            makeToastOnUI("未知错误", Toast.LENGTH_SHORT);
        }
    }

    void resetType(){
        if ("notInitialize".equals(echo)) {
            makeToastOnUI("设备未初始化", Toast.LENGTH_LONG);
        } else if ("resetsuccess".equals(echo)) {
            makeToastOnUI("重置密码成功", Toast.LENGTH_LONG);
        } else {
            makeToastOnUI("未知错误", Toast.LENGTH_SHORT);
        }
    }

    void whiteLightSwitchType(){
        if ("notInitialize".equals(echo)) {
            makeToastOnUI("设备未初始化", Toast.LENGTH_LONG);
        } else if ("off".equals(echo)) {
            makeToastOnUI("已关闭", Toast.LENGTH_LONG);
        } else if ("on".equals(echo)) {
            makeToastOnUI("已开启", Toast.LENGTH_LONG);
        } else {
            makeToastOnUI("未知错误", Toast.LENGTH_SHORT);
        }
    }
}
