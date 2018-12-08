package io.github.acedroidx.smarthome

import android.content.Intent
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.Toast

class LoginActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        //------------------------------------------------------------------
        //------------------------------------------------------------------
        //------------------------------------------------------------------
        val locklistSP = getSharedPreferences("lock", 0)
        val lockip = locklistSP.getString("ip", null)
        val lockpw = locklistSP.getString("password", null)

        val ipEdit = findViewById<View>(R.id.ipEdit) as EditText
        val passwordEdit = findViewById<View>(R.id.passwordEdit) as EditText

        if (!(lockip == null || lockpw == null)) {
            ipEdit.setText(lockip)
            passwordEdit.setText(lockpw)
        }
        //------------------------------------------------------------------
        //------------------------------------------------------------------
        //------------------------------------------------------------------

        val addButton = findViewById<View>(R.id.addButton) as Button
        val aboutButton = findViewById<View>(R.id.aboutButton) as Button
        addButton.setOnClickListener {
            if ("" == ipEdit.text.toString()) {
                Toast.makeText(applicationContext, "IP地址不能为空", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            var handler = object : Handler(Looper.getMainLooper()) {
                override fun handleMessage(inputMessage: Message) {
                    Log.d("wxxDbg", inputMessage.obj.toString())
                    if (inputMessage.obj == "SmartHome") {
                        locklistSP.edit().putString("ip", ipEdit.text.toString())
                            .putString("password", passwordEdit.text.toString())
                            .apply()
                        val intent = Intent()
                        intent.putExtra("ip",ipEdit.text.toString());
                        intent.setClass(this@LoginActivity, MainActivity::class.java)
                        startActivity(intent)
                    } else {
                        Toast.makeText(applicationContext, "error:" + inputMessage.obj.toString(), Toast.LENGTH_LONG)
                            .show()
                    }
                }
            }
            HttpClient("http://" + ipEdit.text + ":23333/isSmartHome", handler).start()
        }

        //------------------------------------------------------------------
        //------------------------------------------------------------------
        //------------------------------------------------------------------
        aboutButton.setOnClickListener {
            val intent = Intent()
            //intent.putExtra("type",type+"/"+l);
            intent.setClass(this@LoginActivity, AboutActivity::class.java)
            startActivity(intent)
        }
    }
}
