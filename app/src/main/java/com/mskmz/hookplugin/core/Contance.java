package com.mskmz.hookplugin.core;

import android.content.Context;

import com.mskmz.hookplugin.HookApplication;

import java.io.File;

public class Contance {
  public static final String PROXY_SRC_INTENT = "proxySrcIntent";

  public static String getPluginPath() {
    File dir = new File("/sdcard/Download/");
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File file = new File(dir.getAbsolutePath() + File.separator + "p.apk");
    return file.getAbsolutePath();
  }

  public static String getOptimizedDirectory() {
    File fileDir = HookApplication.INSTANCE.getDir("pulginPathDir", Context.MODE_PRIVATE);
    return fileDir.getAbsolutePath();
  }
}
