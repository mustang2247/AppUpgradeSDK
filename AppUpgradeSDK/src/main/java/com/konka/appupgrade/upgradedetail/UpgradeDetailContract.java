package com.konka.appupgrade.upgradedetail;

import android.content.Context;

import com.konka.appupgrade.BasePresenter;
import com.konka.appupgrade.BaseView;
import com.konka.appupgrade.data.AppUpgradeInfo;


public interface UpgradeDetailContract {
    interface View extends BaseView<Presenter> {

        void showDetail(AppUpgradeInfo app);

        void showUICase(int caseType, boolean isForce);

        void updateProgress(int soFarBytes, int totalBytes, int speed);

        boolean checkEnvironmentInfo(long size);

        void toInstallActivity(AppUpgradeInfo appInfo, String path);

        void killSelf();

        void loadAppIcon(String path);
    }

    interface Presenter extends BasePresenter {
        void loadAppInfo(Context context);

        void downLoadApk(String filePath);

        void pauseDownLoad();

        boolean isForceUpgrade();

    }
}
