package com.mskmz.hookplugin;

import android.app.Application;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.mskmz.hookplugin.core.HookEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Method;

public class HookApplication extends Application {
  //>>>>>>>>>>>>>>>DEBUG配置>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
  private static final String TAG = "HookApplication>>>";
  private static final boolean DEBUG = true;

  //<<<<<<<<<<<<<<<DEBUG配置<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
  @Override
  public void onCreate() {
    super.onCreate();
    HookEngine.Hook(this);
    try {
      loadRes();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private String getPluginPath() {
    File dir = new File("/sdcard/Download/");
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File file = new File(dir.getAbsolutePath() + File.separator + "p.apk");
    return file.getAbsolutePath();
  }

  private void loadRes() throws Exception {
    assetManager = AssetManager.class.newInstance();

    // 把插件的路径 给 AssetManager
    File file = new File(getPluginPath());
    if (!file.exists()) {
      throw new FileNotFoundException("没有找到插件包!!");
    }

    // 执行此 public final int addAssetPath(String path) 方法，才能把插件的路径添加进去
    Method method = assetManager.getClass().getDeclaredMethod("addAssetPath", String.class); // 类类型
    method.setAccessible(true);
    method.invoke(assetManager, file.getAbsolutePath());

    Resources r = getResources(); // 拿到的是宿主的 配置信息

    // 实例化此方法 final StringBlock[] ensureStringBlocks()
    Method ensureStringBlocksMethod = assetManager.getClass().getDeclaredMethod("ensureStringBlocks");
    ensureStringBlocksMethod.setAccessible(true);
    ensureStringBlocksMethod.invoke(assetManager); // 执行了ensureStringBlocks  string.xml  color.xml   anim.xml 被初始化

    // 特殊：专门加载插件资源
    resources = new Resources(assetManager, r.getDisplayMetrics(), r.getConfiguration());
    if (DEBUG) Log.d(TAG, "loadRes: 资源加载成功" + resources);
  }

  private Resources resources;
  private AssetManager assetManager;

  @Override
  public Resources getResources() {
    return resources == null ? super.getResources() : resources;
  }

  @Override
  public AssetManager getAssets() {
    return assetManager == null ? super.getAssets() : assetManager;
  }
}
