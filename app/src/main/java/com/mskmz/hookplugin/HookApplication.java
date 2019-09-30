package com.mskmz.hookplugin;

import android.app.Application;
import android.content.Context;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;

import com.mskmz.hookplugin.core.Contance;
import com.mskmz.hookplugin.core.HookEngine;

import java.io.File;
import java.io.FileNotFoundException;
import java.lang.reflect.Field;
import java.lang.reflect.Method;

public class HookApplication extends Application {
  //>>>>>>>>>>>>>>>DEBUG配置>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
  private static final String TAG = "HookApplication>>>";
  private static final boolean DEBUG = true;
  //<<<<<<<<<<<<<<<DEBUG配置<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
  public static HookApplication INSTANCE = null;

  private AssetManager mAssetManager;
  private Resources mResources;

  @Override
  public void onCreate() {
    super.onCreate();
    INSTANCE = this;
    HookEngine.Hook(this);
    try {
      loadRes();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void loadRes() throws Exception {
    Resources srcRes = getResources();
    mAssetManager = AssetManager.class.newInstance();
    Method mAssetManager$addAssetPath = AssetManager.class.getDeclaredMethod("addAssetPath", String.class);
    mAssetManager$addAssetPath.invoke(mAssetManager, Contance.getPluginPath());

    Method mAssetManager$ensure = AssetManager.class.getDeclaredMethod("ensureStringBlocks");
    mAssetManager$ensure.invoke(mAssetManager, null);
    //public Resources(AssetManager assets, DisplayMetrics metrics, Configuration config) {
    mResources = new Resources(
        mAssetManager,
        srcRes.getDisplayMetrics(),
        srcRes.getConfiguration()
    );
  }

  @Override
  public Resources getResources() {
    return mResources == null ? super.getResources() : mResources;
  }

  @Override
  public AssetManager getAssets() {
    return mAssetManager == null ? super.getAssets() : mAssetManager;
  }
}
