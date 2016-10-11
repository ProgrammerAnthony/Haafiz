package support.ui.utilities;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

import java.util.Locale;

import starter.kit.util.Installation;

/**
 * Created by YuGang Yang on 04 13, 2016.
 * Copyright 2015-2016 qiji.tech. All rights reserved.
 */
public final class AppInfo {

  public String os;
  public String deviceName;
  public String deviceId;
  public String version;
  public int versionCode;
  public String channel;
  public int screenWidth;
  public int screenHeight;
  public String languageCode;

  private void initLanguageCode() {
    Locale locale = Locale.getDefault();
    String language = locale.getLanguage();
    if ("zh".equals(language)) {
      language = language + "-" + locale.getCountry();
    }
    this.languageCode = language;
  }

  private void initOs() {
    this.os = android.os.Build.MODEL + "," + android.os.Build.VERSION.SDK_INT + "," + android.os.Build.VERSION.RELEASE;
  }

  private void initMetrics() {
    this.screenWidth = ScreenUtils.getScreenWidth();
    this.screenHeight = ScreenUtils.getScreenHeight();
  }

  private void initDeviceId() {
    this.deviceId = Installation.genInstallationId();
  }

  private void initVersion(Context context) {
    PackageManager packageManager = context.getPackageManager();
    PackageInfo packInfo = null;
    try {
      packInfo = packageManager.getPackageInfo(context.getPackageName(), 0);
    } catch (PackageManager.NameNotFoundException e) {
      e.printStackTrace();
    }

    String version = "";
    int code = 0;
    if (packInfo != null) {
      version = packInfo.versionName;
      code = packInfo.versionCode;
    }
    this.version = version;
    this.versionCode = code;
  }

  private void initChannel(Context context) {
    this.channel = AndroidUtilities.getMetaData(context, "UMENG_CHANNEL");
  }

  private void initDeviceName() {
    this.deviceName = android.os.Build.DEVICE;
  }

  public AppInfo(Context context) {
    initLanguageCode();
    initDeviceId();
    initVersion(context);
    initChannel(context);
    initOs();
    initDeviceName();
    initMetrics();
  }
}
