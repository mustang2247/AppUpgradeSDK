package com.konka.appupgrade;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;

import com.google.gson.Gson;
import com.konka.appupgrade.data.AppUpgradeInfo;
import com.konka.appupgrade.upgradedetail.UpgradeDetailActivity;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;

import java.net.Proxy;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * Created by yangsheng on 2017-1-20.
 */

public class AppUpgradeSDK {

    public static void init(final Context context){

        FileDownloader.init(context.getApplicationContext(), new DownloadMgrInitialParams.InitCustomMaker()
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                        .proxy(Proxy.NO_PROXY) // set proxy
                )));


        FileDownloader.init(context.getApplicationContext());

        new Thread(){
            @Override
            public void run() {
                checkUpdate(context);
            }
        }.start();

    }

    private static void checkUpdate(Context context) {
        String packageName = context.getPackageName();
//        PackageManager manager = context.getPackageManager();
//        PackageInfo info = null;
//        try {
//            info = manager.getPackageInfo(packageName, 0);
//        } catch (PackageManager.NameNotFoundException e) {
//            e.printStackTrace();
//        }
//        String versionCode = String.valueOf(info.versionCode);


        Uri upgradeUri = Uri.parse("content://com.konka.appupgrade.sdkprovider/APP_UPGRADE_INFO");

        Cursor cursor = context.getContentResolver().query(upgradeUri, null, "APP_PACKAGE = ?",
                new String[]{packageName}, null);

        AppUpgradeInfo appInfo = null;

        if (cursor != null) {
            while (cursor.moveToNext()) {
                appInfo = new AppUpgradeInfo();
                appInfo.setAppId(cursor.getLong(cursor.getColumnIndex("_id")));
                appInfo.setAppName(cursor.getString(cursor.getColumnIndex("APP_NAME")));
                appInfo.setVersionName(cursor.getString(cursor.getColumnIndex("VERSION_NAME")));
                appInfo.setVersionCode(cursor.getLong(cursor.getColumnIndex("VERSION_CODE")));
                appInfo.setAppPackage(cursor.getString(cursor.getColumnIndex("APP_PACKAGE")));
                appInfo.setAppDownloadPriority(cursor.getInt(cursor.getColumnIndex("APP_DOWNLOAD_PRIORITY")));
                appInfo.setPushType(cursor.getInt(cursor.getColumnIndex("PUSH_TYPE")));
                appInfo.setUpgradeMode(cursor.getInt(cursor.getColumnIndex("UPGRADE_MODE")));
                appInfo.setSelfUpgradeType(cursor.getInt(cursor.getColumnIndex("SELF_UPGRADE_TYPE")));
                appInfo.setIsBeforeUpgradeClear(cursor.getInt(cursor.getColumnIndex("IS_BEFORE_UPGRADE_CLEAR")) > 0);
                appInfo.setCmdAfterInstall(cursor.getString(cursor.getColumnIndex("CMD_AFTER_INSTALL")));
                appInfo.setAppIcon(cursor.getString(cursor.getColumnIndex("APP_ICON")));
                appInfo.setDesc(cursor.getString(cursor.getColumnIndex("DESC")));
                appInfo.setCreateTime(cursor.getLong(cursor.getColumnIndex("CREATE_TIME")));
                appInfo.setUpdateTime(cursor.getLong(cursor.getColumnIndex("UPDATE_TIME")));
                appInfo.setDownloadUrl(cursor.getString(cursor.getColumnIndex("DOWNLOAD_URL")));
                appInfo.setSign(cursor.getString(cursor.getColumnIndex("SIGN")));
                appInfo.setApkSize(cursor.getLong(cursor.getColumnIndex("APK_SIZE")));
                appInfo.setMd5(cursor.getString(cursor.getColumnIndex("MD5")));

            }
            cursor.close();
        }

        if (appInfo != null) {

            Gson gson = new Gson();
            String jsonStr = gson.toJson(appInfo);
            Intent intent = new Intent(context, UpgradeDetailActivity.class);
            intent.putExtra("appInfo", jsonStr);
            intent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);

        }


    }
}
