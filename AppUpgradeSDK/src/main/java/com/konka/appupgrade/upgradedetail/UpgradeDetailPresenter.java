package com.konka.appupgrade.upgradedetail;

import android.content.Context;
import android.util.Log;

import com.konka.appupgrade.common.Constants;
import com.konka.appupgrade.common.MD5Util;
import com.konka.appupgrade.common.Utils;
import com.konka.appupgrade.data.AppUpgradeInfo;
import com.liulishuo.filedownloader.BaseDownloadTask;
import com.liulishuo.filedownloader.FileDownloadListener;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.File;


/**
 * Created by suqishuo on 2016/12/27.
 */

public class UpgradeDetailPresenter implements UpgradeDetailContract.Presenter {

    private static final String TAG = "UpdateSDK:Presenter";

    private UpgradeDetailContract.View mView;

    private AppUpgradeInfo appInfo;

    private int mDownloader;


    public UpgradeDetailPresenter(UpgradeDetailContract.View view, AppUpgradeInfo appInfo) {
        this.appInfo = appInfo;
        if (null != view) {
            mView = view;
            mView.setPresenter(this);
        }
    }

    @Override
    public void loadAppInfo(Context context) {

        if (appInfo.getSelfUpgradeType() == 0) {
            mView.showUICase(Constants.INIT_UI, false);
        } else if (appInfo.getSelfUpgradeType() == 1) {
            mView.showUICase(Constants.INIT_UI, true);
        }
        mView.showDetail(appInfo);
    }

    @Override
    public void downLoadApk(String filePath) {

        filePath = filePath + MD5Util.MD5(appInfo.getAppPackage()) + ".apk";

        Log.d(TAG, "downLoadURL: " + appInfo.getDownloadUrl());
        Log.d(TAG, "downLoadFilePath: " + filePath);

        if (!mView.checkEnvironmentInfo(appInfo.getApkSize())) {
            //存储空间或者网络环境不满足
            return;
        }

        mDownloader = FileDownloader.getImpl().create(appInfo.getDownloadUrl())
                .setPath(filePath)
                .setForceReDownload(true)// TODO: 2017-2-21 记得上线关掉
                .setListener(new FileDownloadListener() {
                    @Override
                    protected void pending(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        //mFileName.setText(task.getFilename());
                        mView.showUICase(Constants.DOWNLOADING_UI, false);
                        File dir = new File(task.getPath()).getParentFile();
                        Utils.chmodOfDir(dir);
                    }

                    @Override
                    protected void connected(BaseDownloadTask task, String etag, boolean isContinue, int soFarBytes, int totalBytes) {
                        mView.showUICase(Constants.DOWNLOADING_UI, false);
                    }

                    @Override
                    protected void progress(BaseDownloadTask task, int soFarBytes, int totalBytes) {

                        if (totalBytes == -1) {
                            //chunked transfer encoding data
                            //mProgressBar.setIndeterminate(true);
                        } else {
                            mView.updateProgress(soFarBytes, totalBytes, task.getSpeed());
                        }
                    }

                    @Override
                    protected void blockComplete(BaseDownloadTask task) {

                    }

                    @Override
                    protected void retry(final BaseDownloadTask task, final Throwable ex, final int retryingTimes, final int soFarBytes) {
                    }

                    @Override
                    protected void completed(BaseDownloadTask task) {
                        //框架在中100%的时候不会回调progress方法
                        mView.updateProgress(1, 1, 0);

                        Utils.chmodOfFile(task.getPath());

                        mView.toInstallActivity(appInfo, task.getPath());

                        // mView.killSelf();
                    }

                    @Override
                    protected void paused(BaseDownloadTask task, int soFarBytes, int totalBytes) {
                        Log.d(TAG, "download paused");
                    }

                    @Override
                    protected void error(BaseDownloadTask task, Throwable e) {
                        Log.d(TAG, "download error");
                        if (appInfo.getSelfUpgradeType() == 0) {
                            mView.showUICase(Constants.DOWNLOAD_ERROR_UI, false);
                        } else if (appInfo.getSelfUpgradeType() == 1) {
                            mView.showUICase(Constants.DOWNLOAD_ERROR_UI, true);
                        }
                    }

                    @Override
                    protected void warn(BaseDownloadTask task) {
                        Log.d(TAG, "download warn");
                    }
                }).start();
    }

    @Override
    public void pauseDownLoad() {
        FileDownloader.getImpl().pause(mDownloader);
    }

    @Override
    public boolean isForceUpgrade() {
        if (appInfo.getSelfUpgradeType() == 0) {
            return false;
        }
        return true;
    }

    @Override
    public void start() {
        loadAppInfo((Context) mView);
    }


}
