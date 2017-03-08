package com.konka.appupgrade.data;

/**
 *
 * 应用升级数据
 */

public class AppUpgradeInfo {

	private Long appId;//应用id


	private String appName;//应用名称


	private String  versionName;//应用版本名


	private long versionCode;//应用版本号


	private String appPackage;//应用包名


	private int appDownloadPriority;//下载优先级


	private int pushType;//推送方式


	private int upgradeMode;//升级方式


	private int selfUpgradeType;//自升级类型


	private boolean isBeforeUpgradeClear;//是否升级前清除数据


	private String cmdAfterInstall;//安装后执行命令


	private String appIcon;//图标url


	private String desc;//详情


	private long createTime;//创建时间


	private long updateTime;//更新时间


	private String downloadUrl;//下载链接


	private String sign;//apk签名信息


	private long apkSize;//pk文件大小


	private String md5;//文件md5

	public AppUpgradeInfo(Long appId, String appName, String versionName,
									long versionCode, String appPackage, int appDownloadPriority,
									int pushType, int upgradeMode, int selfUpgradeType,
									boolean isBeforeUpgradeClear, String cmdAfterInstall, String appIcon,
									String desc, long createTime, long updateTime, String downloadUrl,
									String sign, long apkSize, String md5) {
					this.appId = appId;
					this.appName = appName;
					this.versionName = versionName;
					this.versionCode = versionCode;
					this.appPackage = appPackage;
					this.appDownloadPriority = appDownloadPriority;
					this.pushType = pushType;
					this.upgradeMode = upgradeMode;
					this.selfUpgradeType = selfUpgradeType;
					this.isBeforeUpgradeClear = isBeforeUpgradeClear;
					this.cmdAfterInstall = cmdAfterInstall;
					this.appIcon = appIcon;
					this.desc = desc;
					this.createTime = createTime;
					this.updateTime = updateTime;
					this.downloadUrl = downloadUrl;
					this.sign = sign;
					this.apkSize = apkSize;
					this.md5 = md5;
	}


	public AppUpgradeInfo() {
	}

	public Long getAppId() {
					return this.appId;
	}

	public void setAppId(Long appId) {
					this.appId = appId;
	}

	public String getAppName() {
					return this.appName;
	}

	public void setAppName(String appName) {
					this.appName = appName;
	}

	public String getVersionName() {
					return this.versionName;
	}

	public void setVersionName(String versionName) {
					this.versionName = versionName;
	}

	public long getVersionCode() {
					return this.versionCode;
	}

	public void setVersionCode(long versionCode) {
					this.versionCode = versionCode;
	}

	public String getAppPackage() {
					return this.appPackage;
	}

	public void setAppPackage(String appPackage) {
					this.appPackage = appPackage;
	}

	public int getAppDownloadPriority() {
					return this.appDownloadPriority;
	}

	public void setAppDownloadPriority(int appDownloadPriority) {
					this.appDownloadPriority = appDownloadPriority;
	}

	public int getPushType() {
					return this.pushType;
	}

	public void setPushType(int pushType) {
					this.pushType = pushType;
	}

	public int getUpgradeMode() {
					return this.upgradeMode;
	}

	public void setUpgradeMode(int upgradeMode) {
					this.upgradeMode = upgradeMode;
	}

	public int getSelfUpgradeType() {
					return this.selfUpgradeType;
	}

	public void setSelfUpgradeType(int selfUpgradeType) {
					this.selfUpgradeType = selfUpgradeType;
	}

	public boolean getIsBeforeUpgradeClear() {
					return this.isBeforeUpgradeClear;
	}

	public void setIsBeforeUpgradeClear(boolean isBeforeUpgradeClear) {
					this.isBeforeUpgradeClear = isBeforeUpgradeClear;
	}

	public String getCmdAfterInstall() {
					return this.cmdAfterInstall;
	}

	public void setCmdAfterInstall(String cmdAfterInstall) {
					this.cmdAfterInstall = cmdAfterInstall;
	}

	public String getAppIcon() {
					return this.appIcon;
	}

	public void setAppIcon(String appIcon) {
					this.appIcon = appIcon;
	}

	public String getDesc() {
					return this.desc;
	}

	public void setDesc(String desc) {
					this.desc = desc;
	}

	public long getCreateTime() {
					return this.createTime;
	}

	public void setCreateTime(long createTime) {
					this.createTime = createTime;
	}

	public long getUpdateTime() {
					return this.updateTime;
	}

	public void setUpdateTime(long updateTime) {
					this.updateTime = updateTime;
	}

	public String getDownloadUrl() {
					return this.downloadUrl;
	}

	public void setDownloadUrl(String downloadUrl) {
					this.downloadUrl = downloadUrl;
	}

	public String getSign() {
					return this.sign;
	}

	public void setSign(String sign) {
					this.sign = sign;
	}

	public long getApkSize() {
					return this.apkSize;
	}

	public void setApkSize(long apkSize) {
					this.apkSize = apkSize;
	}

	public String getMd5() {
					return this.md5;
	}

	public void setMd5(String md5) {
					this.md5 = md5;
	}

	@Override
	public String toString() {
		return "AppUpgradeInfo{" +
				"appId=" + appId +
				", appName='" + appName + '\'' +
				", versionName='" + versionName + '\'' +
				", versionCode=" + versionCode +
				", appPackage='" + appPackage + '\'' +
				", appDownloadPriority=" + appDownloadPriority +
				", pushType=" + pushType +
				", upgradeMode=" + upgradeMode +
				", selfUpgradeType=" + selfUpgradeType +
				", isBeforeUpgradeClear=" + isBeforeUpgradeClear +
				", cmdAfterInstall='" + cmdAfterInstall + '\'' +
				", appIcon='" + appIcon + '\'' +
				", desc='" + desc + '\'' +
				", createTime=" + createTime +
				", updateTime=" + updateTime +
				", downloadUrl='" + downloadUrl + '\'' +
				", sign='" + sign + '\'' +
				", apkSize=" + apkSize +
				", md5='" + md5 + '\'' +
				'}';
	}


}
