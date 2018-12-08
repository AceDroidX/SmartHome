package io.github.acedroidx.smarthome

import android.app.PendingIntent.getActivity
import android.content.DialogInterface
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.content.Intent
import android.widget.Toast
import android.widget.EditText
import android.content.SharedPreferences
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.View
import android.widget.Button
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.dialog_biglight.*
import android.view.LayoutInflater
import kotlinx.android.synthetic.main.dialog_biglight.view.*


class MainActivity : AppCompatActivity() {
    lateinit var ip: String
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        if (intent.extras != null) ip = intent.extras.getString("ip")
        else ip = "error"
        var handler = object : Handler(Looper.getMainLooper()) {
            override fun handleMessage(inputMessage: Message) {
                Log.w("wxxDbg", inputMessage.obj.toString())
                if (inputMessage.obj == "on" || inputMessage.obj == "off" || inputMessage.obj == "ok") {
                    Toast.makeText(applicationContext, inputMessage.obj.toString(), Toast.LENGTH_SHORT)
                        .show()
                } else {
                    Toast.makeText(applicationContext, "error1:" + inputMessage.obj.toString(), Toast.LENGTH_LONG)
                        .show()
                }
            }
        }
        var builder = AlertDialog.Builder(this)
        val inflater = this.layoutInflater
        var inflate = inflater.inflate(R.layout.dialog_biglight, null)
        // Inflate and set the layout for the dialog
        // Pass null as the parent view because its going in the dialog layout
        builder.setView(inflate)
        builder.setTitle("输入亮度等级,最高32")
        builder.setPositiveButton("确定") { dialog, id ->
            if(inflate.editText.text.toString().toInt() in 1..32){
                HttpClient("http://" + ip + ":23333/bigLight-set/1000/"+inflate.editText.text, handler).start()
                dialog.dismiss()
            }else{
                Toast.makeText(applicationContext, "输入亮度等级,最高32", Toast.LENGTH_LONG)
                    .show()
            }
        }
        builder.setNegativeButton("取消") { dialog, id -> dialog.cancel()}
        var dialog = builder.create()
        door.setOnClickListener { HttpClient("http://" + ip + ":23333/door-switch", handler).start() }
        bigLight.setOnClickListener { HttpClient("http://" + ip + ":23333/bigLight-switch", handler).start() }
        bigLS.setOnClickListener {dialog.show()}
        whiteLight.setOnClickListener { HttpClient("http://" + ip + ":23333/whiteLight-switch", handler).start() }
        fan.setOnClickListener { HttpClient("http://" + ip + ":23333/fan-switch", handler).start() }
    }
}
