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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lock);

        Intent intent = getIntent();
        final String type = intent.getStringExtra("type");

        SharedPreferences locklistSP = getSharedPreferences("lock", 0);
        lockip = locklistSP.getString("ip", null);

        lockpw = locklistSP.getString("password", null);
    }

    public void switchButton(View v) {
        TCPSocket lockSwitchSocket = new TCPSocket(LockActivity.this);
        lockSwitchSocket.connect(lockip, 23333, "door_switch");
        lockSwitchSocket.cmd("door_switch", 1000);
    }

    public void resetButton(View v) {
        TCPSocket lockResetSocket = new TCPSocket(LockActivity.this);
        lockResetSocket.connect(lockip, 23333, "reset");
        lockResetSocket.cmd("reset", 1000);
    }

    public void whiteLightButton(View v) {
        TCPSocket lockWhiteLightSocket = new TCPSocket(LockActivity.this);
        lockWhiteLightSocket.connect(lockip, 23333, "whiteLight_switch");
        lockWhiteLightSocket.cmd("whiteLight_switch", 1000);
    }
}
