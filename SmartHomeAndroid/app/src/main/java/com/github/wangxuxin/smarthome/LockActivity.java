package com.github.wangxuxin.smarthome;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

public class LockActivity extends AppCompatActivity {
    private String lockip;
    private String lockpw;
    String number = "";
    TCPSocket socket;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");


        socket = new TCPSocket(this,1);


        SharedPreferences locklistSP = getSharedPreferences("lock", 0);
        lockip = locklistSP.getString("ip", null);

        lockpw = locklistSP.getString("password", null);
    }

    public void switchButton(View v) {
        socket.send("door_switch", 5000);
    }

    public void resetButton(View v) {
        socket.send("reset", 5000);
    }

    public void whiteLightButton(View v) {
        socket.send("whiteLight_switch", 5000);
    }
}
