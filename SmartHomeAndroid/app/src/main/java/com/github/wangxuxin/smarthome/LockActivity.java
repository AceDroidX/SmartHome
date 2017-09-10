package com.github.wangxuxin.smarthome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Toast;

public class LockActivity extends AppCompatActivity {
    private String lockip;
    private String lockpw;
    String number = "";
    //static boolean canSendSocket=false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);
        //MutliThread m=new MutliThread();
        //Thread t1=new Thread(m,"thread1");
        //t1.start();

        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");

        SharedPreferences locklistSP = getSharedPreferences("lock", 0);
        lockip = locklistSP.getString("ip", null);

        lockpw = locklistSP.getString("password", null);
    }

    public void switchButton(View v) {
        /*
        if(!canSendSocket){
            Toast.makeText(getApplicationContext(), "操作过于频繁，请稍后再试",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        canSendSocket=false;
        */
        final TCPSocket lockSocket = new TCPSocket();
        lockSocket.connect(lockip, 23333);
        lockSocket.send("switch");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                if ("notInitialize".equals(lockSocket.echo)) {
                    Toast.makeText(getApplicationContext(), "设备未初始化",
                            Toast.LENGTH_LONG).show();
                } else if ("lock".equals(lockSocket.echo)) {
                    Toast.makeText(getApplicationContext(), "已锁住",
                            Toast.LENGTH_LONG).show();
                } else if ("unlock".equals(lockSocket.echo)) {
                    Toast.makeText(getApplicationContext(), "已开启",
                            Toast.LENGTH_LONG).show();
                } else if ("keywrong".equals(lockSocket.echo)) {
                    Toast.makeText(getApplicationContext(), "密码错误",
                            Toast.LENGTH_LONG).show();
                } else if ("unknown".equals(lockSocket.echo)) {
                    Toast.makeText(getApplicationContext(), "连接超时",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "未知错误",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }

    public void resetButton(View v) {
        /*
        if(!canSendSocket){
            Toast.makeText(getApplicationContext(), "操作过于频繁，请稍后再试",
                    Toast.LENGTH_SHORT).show();
            return;
        }
        canSendSocket=false;
        */
        final TCPSocket lockSocket = new TCPSocket();
        lockSocket.connect(lockip, 23333);
        lockSocket.send("reset");
        new Handler().postDelayed(new Runnable() {
            public void run() {
                //execute the task
                if ("notInitialize".equals(lockSocket.echo)) {
                    Toast.makeText(getApplicationContext(), "设备未初始化",
                            Toast.LENGTH_LONG).show();
                } else if ("resetsuccess".equals(lockSocket.echo)) {
                    Toast.makeText(getApplicationContext(), "重置密码成功",
                            Toast.LENGTH_LONG).show();
                } else if ("keywrong".equals(lockSocket.echo)) {
                    Toast.makeText(getApplicationContext(), "密码错误",
                            Toast.LENGTH_LONG).show();
                } else if ("unknown".equals(lockSocket.echo)) {
                    Toast.makeText(getApplicationContext(), "连接超时",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "未知错误",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }, 500);
    }
}
