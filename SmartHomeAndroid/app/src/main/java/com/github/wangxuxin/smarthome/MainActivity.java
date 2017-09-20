package com.github.wangxuxin.smarthome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.addlock_toast);

        //------------------------------------------------------------------
        //------------------------------------------------------------------
        //------------------------------------------------------------------
        SharedPreferences locklistSP = getSharedPreferences("lock", 0);
        final String lockip = locklistSP.getString("ip", null);
        String lockpw = locklistSP.getString("password", null);

        final EditText ipEdit = (EditText) findViewById(R.id.ipEdit);
        final EditText passwordEdit = (EditText) findViewById(R.id.passwordEdit);

        if (!(lockip == null || lockpw == null)) {
            ipEdit.setText(lockip);
            passwordEdit.setText(lockpw);
        }
        //------------------------------------------------------------------
        //------------------------------------------------------------------
        //------------------------------------------------------------------

        Button addButton = (Button) findViewById(R.id.addButton);
        Button aboutButton = (Button) findViewById(R.id.aboutButton);
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if ("".equals(ipEdit.getText().toString())) {
                    Toast.makeText(getApplicationContext(), "IP地址不能为空",
                            Toast.LENGTH_LONG).show();
                    return;
                }
                final TCPSocket islock = new TCPSocket();
                islock.connect(ipEdit.getText().toString(), 23333);
                islock.send("isSmartLock",1000);
                if ("SmartLock".equals(islock.recv(5000))) {
                    Toast.makeText(getApplicationContext(), "连接成功",
                            Toast.LENGTH_SHORT).show();
                    //1、打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
                    SharedPreferences isFirstOpen = getSharedPreferences("lock", 0);
                    //2、让setting处于编辑状态
                    SharedPreferences.Editor editor = isFirstOpen.edit();
                    //3、存放数据
                    editor.putString("ip", ipEdit.getText().toString());
                    editor.putString("password", passwordEdit.getText().toString());
                    //4、完成提交
                    editor.apply();

                    Intent intent = new Intent();
                    //intent.putExtra("type",type+"/"+l);
                    intent.setClass(MainActivity.this, LockActivity.class);
                    startActivity(intent);
                } else if ("unknown".equals(islock.echo)) {
                    Toast.makeText(getApplicationContext(), "连接超时",
                            Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(getApplicationContext(), "未知错误",
                            Toast.LENGTH_SHORT).show();
                }
                /*
                final TCPSocket islock = new TCPSocket();
                islock.socket(ipEdit.getText().toString(), 23333, "iskey " + passwordEdit.getText().toString());
                new Handler().postDelayed(new Runnable() {
                    public void run() {
                        if ("notInitialize".equals(islock.echo)) {
                            Toast.makeText(getApplicationContext(), "进入设置模式",
                                    Toast.LENGTH_LONG).show();
                            //1、打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
                            SharedPreferences isFirstOpen = getSharedPreferences("lock", 0);
                            //2、让setting处于编辑状态
                            SharedPreferences.Editor editor = isFirstOpen.edit();
                            //3、存放数据
                            editor.putString("ip", ipEdit.getText().toString());
                            //4、完成提交
                            editor.apply();

                            Intent intent = new Intent();
                            intent.putExtra("type", "1");
                            intent.setClass(MainActivity.this, LockActivity.class);
                            startActivity(intent);
                        } else if ("keywrong".equals(islock.echo)) {
                            Toast.makeText(getApplicationContext(), "密码错误",
                                    Toast.LENGTH_LONG).show();
                        } else if ("keycorrect".equals(islock.echo)) {
                            Toast.makeText(getApplicationContext(), "连接成功",
                                    Toast.LENGTH_SHORT).show();
                            //1、打开Preferences，名称为setting，如果存在则打开它，否则创建新的Preferences
                            SharedPreferences isFirstOpen = getSharedPreferences("lock", 0);
                            //2、让setting处于编辑状态
                            SharedPreferences.Editor editor = isFirstOpen.edit();
                            //3、存放数据
                            editor.putString("ip", ipEdit.getText().toString());
                            editor.putString("password", passwordEdit.getText().toString());
                            //4、完成提交
                            editor.apply();

                            Intent intent = new Intent();
                            //intent.putExtra("type",type+"/"+l);
                            intent.setClass(MainActivity.this, LockActivity.class);
                            startActivity(intent);
                        } else if ("unknown".equals(islock.echo)) {
                            Toast.makeText(getApplicationContext(), "连接超时",
                                    Toast.LENGTH_SHORT).show();
                        } else {
                            Toast.makeText(getApplicationContext(), "未知错误",
                                    Toast.LENGTH_SHORT).show();
                        }
                    }
                }, 1000);
                */
            }
        });

        //------------------------------------------------------------------
        //------------------------------------------------------------------
        //------------------------------------------------------------------
        aboutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                //intent.putExtra("type",type+"/"+l);
                intent.setClass(MainActivity.this, AboutActivity.class);
                startActivity(intent);
            }
        });
    }
}
