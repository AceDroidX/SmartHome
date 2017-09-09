package com.github.wangxuxin.smarthome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class LockActivity extends AppCompatActivity {
    String lockip;
    String lockpw;
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

        if ("1".equals(type)) {
            setContentView(R.layout.activity_set);
            Button keysetButton = (Button) findViewById(R.id.keysetButton);
            keysetButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {


                    final EditText keysetEdit = (EditText) findViewById(R.id.keysetEdit);
                    if ("".equals(keysetEdit.getText().toString())) {///////////////////////判断密码是否空白
                        Toast.makeText(getApplicationContext(), "密码不能空白",
                                Toast.LENGTH_LONG).show();
                        return;
                    }

                    /*
                    long password;
                    try {
                        password = Long.parseLong(keysetEdit.getText().toString());
                        long numbertemp=password;
                        for(int i=0;i<5;i++){
                            double ddtemp=numbertemp;
                            double dd=ddtemp/100;
                            double ddlong = Math.floor(dd);
                            double ddL = dd*100;
                            double ddlongL = ddlong*100;
                            double ddAfterDot = ddL-ddlongL;
                            int finish= (int) ddAfterDot;
                            numberlist[i]=finish;
                            long numbertemp1=numbertemp-finish;
                            numbertemp=numbertemp1/100;
                        }
                        for (int i=numberlist.length;i>0;i--) {
                            number+=numberlist[i-1];
                        }
                    } catch (Exception e) {
                        Toast.makeText(getApplicationContext(), "密码必须为整数",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    if (Long.parseLong(number) <= 1000000000L || Long.parseLong(number) >= 9999999999L) {
                        Toast.makeText(getApplicationContext(), "密码需为10位数字",
                                Toast.LENGTH_LONG).show();
                        return;
                    }
                    */


                    final TCPSocket keysetSocket = new TCPSocket();
                    keysetSocket.socket(lockip, 23333);
                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            if ("alreadysetkey".equals(keysetSocket.echo)) {
                                Toast.makeText(getApplicationContext(), "已经设置",
                                        Toast.LENGTH_LONG).show();
                            } else if ("setkeyerror".equals(keysetSocket.echo) || "thekeycantbe0".equals(keysetSocket.echo)) {
                                Toast.makeText(getApplicationContext(), "设置错误",
                                        Toast.LENGTH_LONG).show();
                            } else if ("setkeysuccess".equals(keysetSocket.echo)) {
                                Toast.makeText(getApplicationContext(), "设置成功",
                                        Toast.LENGTH_LONG).show();
                                //1、打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
                                SharedPreferences isFirstOpen = getSharedPreferences("lock", 0);
                                //2、让setting处于编辑状态
                                SharedPreferences.Editor editor = isFirstOpen.edit();
                                //3、存放数据
                                editor.putString("password", keysetEdit.getText().toString());
                                //4、完成提交
                                editor.apply();

                                setContentView(R.layout.activity_lock);
                            } else {
                                Toast.makeText(getApplicationContext(), "连接超时",
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    }, 1000);
                }
            });
        }

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
        lockSocket.socket(lockip, 23333);
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
        lockSocket.socket(lockip, 23333);
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
