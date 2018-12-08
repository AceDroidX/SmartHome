package com.github.wangxuxin.smarthome;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

public class InitActivity extends AppCompatActivity {
    private String lockip;

    @Override
    protected void onCreate(Bundle savedInstanceState) {//需要重写
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_init);

        SharedPreferences locklistSP = getSharedPreferences("lock", 0);
        lockip = locklistSP.getString("ip", null);
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

                final TCPSocket keysetSocket = new TCPSocket(InitActivity.this);
                keysetSocket.send("setkey " + keysetEdit.getText().toString(), 5000);
            }
        });
    }
}
