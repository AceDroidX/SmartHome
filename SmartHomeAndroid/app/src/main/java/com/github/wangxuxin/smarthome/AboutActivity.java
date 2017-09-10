package com.github.wangxuxin.smarthome;

import android.content.Intent;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * Created by a1274 on 2016/12/9.
 */
public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);
        //ActionBar actionBar = getActionBar();
        //actionBar.setDisplayHomeAsUpEnabled(true);

        TextView versionText=(TextView)findViewById(R.id.textView_version);
        try {
            versionText.setText("V"+getVersionName());
        } catch (Exception e) {
            e.printStackTrace();
        }

        TextView copyrightText = (TextView) findViewById(R.id.textView_copyright);
        if(!copyrightText.getText().equals("By wxx")){
            copyrightText.setText("By wxx");
            Log.d("wxxDebugAbout","原程序已被修改！！！！By wxx");
        }

        TextView websiteText = (TextView) findViewById(R.id.textView_website);
        websiteText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://wangxuxin.github.io"; // web address
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(url));
                startActivity(intent);
            }
        });
    }

    /*
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */

    public String getVersionName() throws Exception {
        // 获取packagemanager的实例
        PackageManager packageManager = getPackageManager();
        // getPackageName()是你当前类的包名，0代表是获取版本信息
        PackageInfo packInfo = packageManager.getPackageInfo(getPackageName(),0);
        Log.d("wxxDebugAbout",packInfo.versionName);
        return packInfo.versionName;
    }
}
