package com.konka.appupgrade.upgradedetail;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.gson.Gson;
import com.konka.appupgrade.R;
import com.konka.appupgrade.common.Constants;
import com.konka.appupgrade.common.Utils;
import com.konka.appupgrade.common.view.KKDialog;
import com.konka.appupgrade.common.view.KKToast;
import com.konka.appupgrade.data.AppUpgradeInfo;
import com.liulishuo.filedownloader.FileDownloader;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.DecimalFormat;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;

/**
 * 应用升级SDK界面，展示应用升级信息。
 */
public class UpgradeDetailActivity extends Activity implements UpgradeDetailContract.View {

    private static final String TAG = "UpdateSDK";

    private String downloadPath;

    private ImageView ivIcon;

    private TextView tvAppName;
    private TextView tvAppSize;
    private TextView tvPercent;

    private WebView wvDetail;

    private ProgressBar progressBar;

    private LinearLayout btnWrapLayout;
    private LinearLayout processWrapLayout;


    private Button btnUpdate;
    private Button btnSkip;

    private UpgradeDetailPresenter mPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        downloadPath = getFilesDir() + "/filedownloader/";
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_upgrade_detail);

        String jsonStr = getIntent().getStringExtra("appInfo");
        Gson gson = new Gson();
        AppUpgradeInfo appInfo = gson.fromJson(jsonStr, AppUpgradeInfo.class);

        assignViews();
        setListener();
        mPresenter = new UpgradeDetailPresenter(this, appInfo);

    }

    @Override
    protected void onStart() {
        super.onStart();
        mPresenter.start();
        btnUpdate.requestFocus();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mPresenter.pauseDownLoad();
    }

    private void assignViews() {
        ivIcon = (ImageView) findViewById(R.id.ivIcon_sdk);
        tvAppName = (TextView) findViewById(R.id.tv_apk_name_sdk);
        tvAppSize = (TextView) findViewById(R.id.tv_apk_size_sdk);
        tvPercent = (TextView) findViewById(R.id.tv_percent_sdk);
        wvDetail = (WebView) findViewById(R.id.wv_sdk);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_sdk);
        btnWrapLayout = (LinearLayout) findViewById(R.id.btn_wrapper_sdk);
        processWrapLayout = (LinearLayout) findViewById(R.id.progress_wrapper_sdk);
        btnUpdate = (Button) findViewById(R.id.btn_update_sdk);
        btnSkip = (Button) findViewById(R.id.btn_skip_sdk);
    }

    private void setListener() {

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mPresenter.downLoadApk(downloadPath);
            }
        });

        btnSkip.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }


    @Override
    public void showDetail(AppUpgradeInfo app) {

        loadAppIcon(app.getAppIcon());
        tvAppName.setText(app.getAppName());
        tvAppSize.setText(app.getVersionName() + " / " + Utils.getFileSizeStr(app.getApkSize()));

        //先设置背景色透明，在设置背景图
        wvDetail.setBackgroundColor(0);
        wvDetail.loadDataWithBaseURL("about:blank", Html.fromHtml(app.getDesc()).toString(), "text/html", "UTF-8", null);

        //滚动条不隐退
        wvDetail.setScrollbarFadingEnabled(false);
    }

    @Override
    public void showUICase(int caseType, boolean isForce) {
        switch (caseType) {
            case Constants.INIT_UI:
                btnWrapLayout.setVisibility(View.VISIBLE);
                if (isForce) {
                    btnSkip.setVisibility(View.GONE);
                }
                processWrapLayout.setVisibility(View.GONE);
                btnUpdate.requestFocus();
                break;

            case Constants.DOWNLOADING_UI:
                btnWrapLayout.setVisibility(View.GONE);
                processWrapLayout.setVisibility(View.VISIBLE);
                break;

            case Constants.DOWNLOAD_ERROR_UI:
                KKDialog.Builder builder = new KKDialog.Builder(this);
                builder.setTitle(getString(R.string.dialog_title));
                builder.setMessage(getString(R.string.dialog_message));
                builder.setPositiveButton(getString(R.string.dialog_retry),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                mPresenter.downLoadApk(downloadPath);
                            }
                        });
                builder.setNegativeButton(getString(R.string.dialog_next_time),
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                                if (mPresenter.isForceUpgrade()) {
                                    killSelf();
                                } else {
                                    finish();
                                }
                            }
                        });
                builder.show();
                processWrapLayout.setVisibility(View.GONE);
                btnWrapLayout.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void updateProgress(int soFarBytes, int totalBytes, int speed) {
        progressBar.setMax(totalBytes);
        progressBar.setProgress(soFarBytes);

        int percent = (int) (soFarBytes * 1.0f / totalBytes * 100);
        tvPercent.setText(getString(R.string.downloading) + " : " + percent + "%" + " (" + speed + "k/s)");
    }

    @Override
    public boolean checkEnvironmentInfo(long size) {

        ActivityManager am = (ActivityManager) getSystemService(Activity.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo memoryInfo = new ActivityManager.MemoryInfo();
        am.getMemoryInfo(memoryInfo);

        //检查存储空间是否足够
        if (memoryInfo.availMem < size) {
            KKToast.makeText(UpgradeDetailActivity.this, getString(R.string.space_insufficient)).show();
            return false;
        }

        //检查网络是否可用
        if (Utils.isNetworkAvailable(UpgradeDetailActivity.this)) {
            return true;
        } else {
            KKToast.makeText(UpgradeDetailActivity.this, getString(R.string.network_exception)).show();

            return false;
        }
    }

    @Override
    public void toInstallActivity(AppUpgradeInfo appInfo, String path) {

        if (Utils.getFileMD5(path).equalsIgnoreCase(appInfo.getMd5())) {
            Log.d(TAG, "MD5: " + Utils.getFileMD5(path));
            Intent installIntent = new Intent("com.konka.appupgrade.ACTION.INSTALL");
            installIntent.putExtra("app_id", appInfo.getAppId());
            installIntent.putExtra("apk_path", path);//增加安装文件路径参数
            installIntent.setFlags(FLAG_ACTIVITY_NEW_TASK);
            startActivity(installIntent);
            //killSelf();
        } else {
            KKToast.makeText(UpgradeDetailActivity.this, getString(R.string.fileMd5_exception)).show();
            if (appInfo.getSelfUpgradeType() == 0) {
                showUICase(Constants.INIT_UI, false);
                btnUpdate.requestFocus();
            } else {
                showUICase(Constants.INIT_UI, true);
            }
        }

    }


    @Override
    public void setPresenter(UpgradeDetailContract.Presenter presenter) {
        if (null != presenter) {
            mPresenter = (UpgradeDetailPresenter) presenter;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (keyCode == KeyEvent.KEYCODE_DPAD_UP) {
            wvDetail.pageUp(false);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_DPAD_DOWN) {
            wvDetail.pageDown(false);
            return true;
        }

        if (keyCode == KeyEvent.KEYCODE_BACK) {
            //android.os.Process.killProcess(Process.myPid());
            killSelf();
            return true;

        }

        return super.onKeyDown(keyCode, event);
    }


    @Override
    public void killSelf() {
        ActivityManager mActivityManager = (ActivityManager) getSystemService(Context.ACTIVITY_SERVICE);
        try {
            Method method = Class.forName("android.app.ActivityManager").getMethod("forceStopPackage", String.class);
            method.invoke(mActivityManager, this.getPackageName());
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void loadAppIcon(final String path) {
        if (path == null) {
            return;
        }
        new Thread() {
            @Override
            public void run() {
                try {
                    URL url = new URL(path);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.setRequestMethod("GET");
                    conn.setReadTimeout(10000);

                    if (conn.getResponseCode() == 200) {
                        InputStream fis = conn.getInputStream();
                        ByteArrayOutputStream bos = new ByteArrayOutputStream();
                        byte[] bytes = new byte[1024];
                        int length;
                        while ((length = fis.read(bytes)) != -1) {
                            bos.write(bytes, 0, length);
                        }

                        final Bitmap bitmap = Utils.createRoundCornerImage(
                                BitmapFactory.decodeByteArray(bos.toByteArray(), 0, bos.toByteArray().length));
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                ivIcon.setImageBitmap(bitmap);
                            }
                        });
                        bos.close();
                        fis.close();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }.start();

    }

    @Override
    protected void onStop() {
        super.onStop();
        FileDownloader.getImpl().unBindService();
    }

}
