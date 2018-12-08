package io.github.acedroidx.smarthome

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView

class AboutActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_about)

        val versionText = findViewById<TextView>(R.id.textView_version)
        try {
            versionText.text = "V$versionName"
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val copyrightText = findViewById<TextView>(R.id.textView_copyright)
        if (copyrightText.text != "By AceDroidX") {
            copyrightText.text = "By AceDroidX"
            Log.d("wxxDebugAbout", "原程序已被修改！！！！By wxx")
        }

        val websiteText = findViewById<TextView>(R.id.textView_website)
        val appInfo = this.packageManager
            .getApplicationInfo(
                this.packageName,
                PackageManager.GET_META_DATA
            )
        val url = appInfo.metaData.getString("Website")
        websiteText.text = url
        websiteText.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW)
            websiteText.text = url
            intent.data = Uri.parse(url)
            startActivity(intent)
        }
    }

    // 获取packagemanager的实例
    // getPackageName()是你当前类的包名，0代表是获取版本信息
    val versionName: String
        get() {
            val packageManager = packageManager
            val packInfo = packageManager?.getPackageInfo(packageName, 0)
            Log.d("wxxDebugAbout", packInfo!!.versionName)
            return packInfo.versionName
        }
}
