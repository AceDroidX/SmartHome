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

                final TCPSocket password = new TCPSocket(MainActivity.this);
                password.connect(ipEdit.getText().toString(), 23333, "verify " + passwordEdit.getText().toString(), 5000);
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
