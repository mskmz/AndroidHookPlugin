package com.mskmz.hookplugin.core;

import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageManager;
import android.util.Log;

import com.mskmz.hookplugin.HookApplication;

import java.io.File;
import java.lang.ref.WeakReference;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Map;

//修改Loader 方案一 合并package
public class HookLoader {
  //>>>>>>>>>>>>>>>DEBUG配置>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
  private static final String TAG = "hookLoader>>>";
  private static final boolean DEBUG = true;
  //<<<<<<<<<<<<<<<DEBUG配置<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
  Context context;

  public HookLoader(Context context) {
    this.context = context;
  }

  public void hook() throws Exception {
    hookLoaderFor22();
  }

  private void hookLoaderFor22() throws Exception {

    Class cActivityThread = Class.forName("android.app.ActivityThread");
    Method mActivityThread = cActivityThread.getDeclaredMethod("currentActivityThread");
    Object oActivityThread = mActivityThread.invoke(null);


    Class cPackageParse = Class.forName("android.content.pm.PackageParser");
    Class cPackageUserState = Class.forName("android.content.pm.PackageUserState");
    Object oPackageParse = cPackageParse.newInstance();
    Object oPackageUserState = cPackageUserState.newInstance();
    //public Package parsePackage(File packageFile, int flags) throws PackageParserException {
    Method mPackageParse$parsePackage = cPackageParse.getDeclaredMethod(
        "parsePackage",
        File.class,
        int.class
    );
    mPackageParse$parsePackage.setAccessible(true);
    Object oPackage = mPackageParse$parsePackage.invoke(
        oPackageParse,
        new File(Contance.getPluginPath()),
        PackageManager.GET_ACTIVITIES);
    //public static ApplicationInfo generateApplicationInfo(Package p, int flags,
    //            PackageUserState state) {

    Method method = cPackageParse.getDeclaredMethod(
        "generateApplicationInfo",
        oPackage.getClass(),
        int.class,
        cPackageUserState);

    ApplicationInfo oApplicationInfo = (ApplicationInfo) method.invoke(
        oPackageParse,
        oPackage,
        0,
        oPackageUserState);
    oApplicationInfo.publicSourceDir = Contance.getPluginPath();
    oApplicationInfo.sourceDir = Contance.getPluginPath();

    //public final LoadedApk getPackageInfoNoCheck(
    //        ApplicationInfo ai,
    //        CompatibilityInfo compatInfo
    //        ) {
    Class cCompatibilityInfo = Class.forName("android.content.res.CompatibilityInfo");
    Field fCompatibilityInfo$DEFAULT_COMPATIBILITY_INFO
        = cCompatibilityInfo.getDeclaredField("DEFAULT_COMPATIBILITY_INFO");
    Object oCompatibilityInfo = fCompatibilityInfo$DEFAULT_COMPATIBILITY_INFO.get(null);
    Method mActivityThread$getPackageInfoNoCheck = cActivityThread.getDeclaredMethod(
        "getPackageInfoNoCheck",
        ApplicationInfo.class,
        cCompatibilityInfo
    );
    Object oLoadApk = mActivityThread$getPackageInfoNoCheck.invoke(
        oActivityThread,
        oApplicationInfo,
        oCompatibilityInfo
    );
    //替换LoadedApk
    //public PluginClassLoader(
    //        String dexPath,
    //        String optimizedDirectory,
    //        String librarySearchPath,
    //        ClassLoader parent)
    PluginClassLoader pluginClassLoader = new PluginClassLoader(
        Contance.getPluginPath(),
        Contance.getOptimizedDirectory(),
        null,
        context.getClassLoader()
    );
    if (DEBUG) Log.d(TAG, "hookLoaderFor22: " + pluginClassLoader);
    if (DEBUG) Log.d(TAG, "hookLoaderFor22: " + oLoadApk.getClass());
    Field fLoadApk$ClassLoader = oLoadApk.getClass().getDeclaredField("mClassLoader");
    fLoadApk$ClassLoader.setAccessible(true);
    fLoadApk$ClassLoader.set(oLoadApk, pluginClassLoader);
    //final ArrayMap<String, WeakReference<LoadedApk>> mPackages
    //            = new ArrayMap<String, WeakReference<LoadedApk>>();
    // 将自己LoadApk加入
    Field fActivityThread$mPackages = cActivityThread.getDeclaredField("mPackages");
    fActivityThread$mPackages.setAccessible(true);
    Map omPackage = (Map) fActivityThread$mPackages.get(oActivityThread);
    omPackage.put(oApplicationInfo.packageName, new WeakReference(oLoadApk));
    if (DEBUG) Log.d(TAG, "hookLoaderFor22: " + oApplicationInfo.packageName);
    if (DEBUG) Log.d(TAG, "hookLoaderFor22: " + oLoadApk);

  }

  private void hookLoaderFor23() throws Exception {
    hookLoaderFor22();
  }

  private void hookLoaderFor24() throws Exception {
    hookLoaderFor22();
  }

  private void hookLoaderFor25() throws Exception {
    hookLoaderFor22();
  }

  private void hookLoaderFor26() throws Exception {

  }

  private void hookLoaderFor27() throws Exception {
    hookLoaderFor26();
  }

  private void hookLoaderFor28() throws Exception {
    hookLoaderFor26();
  }

  private void hookLoaderFor29() throws Exception {
    hookLoaderFor26();
  }
}
