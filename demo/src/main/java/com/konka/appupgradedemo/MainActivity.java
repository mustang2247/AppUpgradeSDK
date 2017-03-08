package com.konka.appupgradedemo;

import android.app.Activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;


import com.google.gson.Gson;
import com.konka.appupgrade.data.AppUpgradeInfo;
import com.konka.appupgrade.upgradedetail.UpgradeDetailActivity;


/**
 * Created by suqishuo on 2016/12/27.
 */

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //setContentView(R.layout.test_layout);
        //ButterKnife.bind(this);
    }


    public void onClickDetail(View view){

        // TODO: 2017-2-22 测试使用

        AppUpgradeInfo appInfo = new AppUpgradeInfo();
        appInfo.setAppId((long)999);
        appInfo.setAppName("凤凰视频");
        appInfo.setSelfUpgradeType(0);
        appInfo.setApkSize(1023);
        appInfo.setVersionName("V5.6.0");
        appInfo.setDownloadUrl("http://117.169.71.164/imtt.dd.qq.com/16891/59851A71428C6522A271C876F2B8F6B4.apk");
        appInfo.setDesc("&lt;p style=&quot;font-size: 20px; color: rgb(240, 240, 240);line-height:0.6;&quot;&gt;\n" +
                "    新特性\n" +
                "&lt;/p&gt;\n" +
                "&lt;p style=&quot;font-size: 20px;color: rgb(200, 200, 200);line-height:1.2;&quot;&gt;\n" +
                "    截屏界面升级，新增马赛克和标签工具截屏界面升级，新增马赛克和标签工具截屏界面升级，新增马赛克和标签工具&lt;br/&gt;群增加本地群文件查看窗口，文件支持直接发送上传&lt;br/&gt;两人音视频通话界面升级，优化屏幕分享功能&lt;br/&gt;解决其他问题，提升程序稳定性\n" +
                "&lt;/p&gt;\n" +
                "&lt;p style=&quot;font-size: 20px; color: rgb(240, 240, 240);&quot;&gt;\n" +
                "    功能优化\n" +
                "&lt;/p&gt;\n" +
                "&lt;p style=&quot;font-size: 20px;color: rgb(200, 200, 200);line-height:1.2;&quot;&gt;\n" +
                "    新增讨论组的语音通话功能，和好友们语音群聊更痛快\n" +
                "&lt;/p&gt;\n" +
                "&lt;p style=&quot;font-size: 20px; color: rgb(240, 240, 240);&quot;&gt;\n" +
                "    问题优化\n" +
                "&lt;/p&gt;");
        Gson gson = new Gson();
        String json = gson.toJson(appInfo);
        Intent intent = new Intent(getApplicationContext(), UpgradeDetailActivity.class);
        intent.putExtra("appInfo", json);
        startActivity(intent);
    }
}
