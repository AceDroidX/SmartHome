package io.github.acedroidx.smarthome

import android.os.Handler
import android.os.Message
import android.util.Log
import android.widget.Toast
import java.io.BufferedReader
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.Exception
import java.net.HttpURLConnection
import java.net.URL

class HttpClient:Thread{
    var baseUrl: String = ""
    var handler: Handler? = null

    constructor(baseUrl: String) {
        this.baseUrl = baseUrl    //构造方法传值
    }
    constructor(baseUrl: String,handler: Handler) {
        this.baseUrl = baseUrl    //构造方法传值
        this.handler = handler
    }

    override fun run() {
        super.run()
        var str:String=""
        try {
            var url = URL(baseUrl)
            Log.w("wxxDbg", url.toString())
            var httpConnect = url.openConnection() as HttpURLConnection

            httpConnect.connectTimeout = 5 * 1000  // 设置连接超时时间
            httpConnect.readTimeout = 5 * 1000  //设置从主机读取数据超时
            httpConnect.doOutput = false
            httpConnect.doInput = true
            httpConnect.useCaches = false
            httpConnect.requestMethod = "GET" // 设置为get请求
            httpConnect.connect() // 开始连接

            var inputStream = httpConnect.inputStream
            var reader = BufferedReader(InputStreamReader(inputStream))
//            var str: String = ""
//            while ((str = reader.readLine()) != null) {
//                println(str)
//            }
            var strBuilder = StringBuilder()
            reader.forEachLine {
                strBuilder.append(it)
                Log.w("wxxDbg", it)
            }
            str=strBuilder.toString()
            inputStream.close()
            reader.close()
        }catch (e:Exception){
            e.printStackTrace()
            str=e.localizedMessage
        }finally {
            if (handler==null){
                return
            }
            var message = Message.obtain()
            message.obj = str
            message.what = 1
            handler?.sendMessage(message)
        }
    }
}