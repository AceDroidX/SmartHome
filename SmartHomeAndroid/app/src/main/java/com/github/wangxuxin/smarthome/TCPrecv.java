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
    TCPSocket tcpsocket;
    String[] cmd = {"unknown", "unknown"};
    Context context = null;
    View view = null;
    Activity activity;

    TCPrecv(DataOutputStream o, BufferedReader i, Socket c, TCPSocket t) {
        out = o;
        input = i;
        client = c;
        tcpsocket = t;
    }

    TCPrecv(DataOutputStream o, BufferedReader i, Socket c, TCPSocket t, Activity a) {
        out = o;
        input = i;
        client = c;
        tcpsocket = t;
        activity = a;

        context = a.getApplicationContext();
        view = a.getWindow().getDecorView();
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
                String tmp = input.readLine();
                if(!"keepAlive".equals(tmp)) Log.d("wxxDebug", "echo:" + tmp);
                cmd = tmp.split(" ");
                if ("keepAlive".equals(cmd[0])) {
                    continue;
                } else if ("unknown".equals(cmd[0])) {
                    makeToastOnUI("服务端未知错误", Toast.LENGTH_LONG);
                    continue;
                } else if ("keywrong".equals(cmd[0])) {
                    makeToastOnUI("密码错误", Toast.LENGTH_LONG);
                    continue;
                } else if ("verifyerror".equals(cmd[0])) {
                    makeToastOnUI("验证出现未知错误 请重新验证", Toast.LENGTH_LONG);
                    continue;
                } else if ("notInitialize".equals(cmd[0])) {
                    makeToastOnUI("没有初始化", Toast.LENGTH_LONG);
                    continue;
                }

                if ("verify".equals(cmd[0])) {
                    verifyType();
                } else if ("init".equals(cmd[0])) {
                    initType();
                } else if ("door_switch".equals(cmd[0])) {
                    doorSwitchType();
                } else if ("reset".equals(cmd[0])) {
                    resetType();
                } else if ("whiteLight_switch".equals(cmd[0])) {
                    whiteLightSwitchType();
                } else {
                    Log.d("wxxDebug", "未知数据类型" + cmd[0]);
                }

                cmd = new String[]{"unknown", "unknown"};
            }
        } catch (Exception e) {
            e.printStackTrace();
            makeToastOnUI("连接已断开:recv", Toast.LENGTH_LONG);
        }
    }

    void verifyType() {
        if (context == null) {
            Log.e("wxxDebug", "verify时context不能为null");
            return;
        }
        final EditText ipEdit = (EditText) view.findViewById(R.id.ipEdit);
        final EditText passwordEdit = (EditText) view.findViewById(R.id.passwordEdit);
        if ("notInitialize".equals(cmd[1])) {
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
            activity.startActivity(intent);
        } else if ("keycorrect".equals(cmd[1])) {
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
            activity.startActivity(intent);
        } else {
            makeToastOnUI("未知错误", Toast.LENGTH_LONG);
        }
    }

    void initType() {
        final EditText keysetEdit = (EditText) view.findViewById(R.id.keysetEdit);
        if ("alreadysetkey".equals(cmd[1])) {
            makeToastOnUI("已经设置", Toast.LENGTH_LONG);
        } else if ("setkeyerror".equals(cmd[1]) || "thekeycantbe0".equals(cmd[1])) {
            makeToastOnUI("设置错误", Toast.LENGTH_LONG);
        } else if ("setkeysuccess".equals(cmd[1])) {
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

    void doorSwitchType() {
        if ("notInitialize".equals(cmd[1])) {
            makeToastOnUI("设备未初始化", Toast.LENGTH_LONG);
        } else if ("lock".equals(cmd[1])) {
            makeToastOnUI("已锁住", Toast.LENGTH_SHORT);
        } else if ("unlock".equals(cmd[1])) {
            makeToastOnUI("已开启", Toast.LENGTH_SHORT);
        } else {
            makeToastOnUI("未知错误", Toast.LENGTH_LONG);
        }
    }

    void resetType() {
        if ("notInitialize".equals(cmd[1])) {
            makeToastOnUI("设备未初始化", Toast.LENGTH_LONG);
        } else if ("resetsuccess".equals(cmd[1])) {
            makeToastOnUI("重置密码成功", Toast.LENGTH_LONG);
        } else {
            makeToastOnUI("未知错误", Toast.LENGTH_LONG);
        }
    }

    void whiteLightSwitchType() {
        if ("notInitialize".equals(cmd[1])) {
            makeToastOnUI("设备未初始化", Toast.LENGTH_LONG);
        } else if ("off".equals(cmd[1])) {
            makeToastOnUI("已关闭", Toast.LENGTH_SHORT);
        } else if ("on".equals(cmd[1])) {
            makeToastOnUI("已开启", Toast.LENGTH_SHORT);
        } else {
            makeToastOnUI("未知错误", Toast.LENGTH_LONG);
        }
    }
}

