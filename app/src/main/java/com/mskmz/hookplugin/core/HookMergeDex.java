package com.mskmz.hookplugin.core;

import android.content.Context;
import android.util.Log;

import java.io.File;
import java.lang.reflect.Array;
import java.lang.reflect.Field;

import dalvik.system.BaseDexClassLoader;
import dalvik.system.DexClassLoader;

//修改Loader 方案一 合并package
public class HookMergeDex {
  //---------------DEBUG配置---------------------------------------------------------------------------
  private static final String TAG = "hookLoaderActivity>>>";
  private static final boolean DEBUG = false;

  Context context;

  public HookMergeDex(Context context) {
    this.context = context;
  }

  public void hook() throws Exception {
    hookLoaderActivityFor22();
  }

  //要做什么
  //实现FindClass合并dex 想办法加载插件的
  //dex是通过什么
  //DexPathList中的Element登记
  private void hookLoaderActivityFor22() throws Exception {

    //拿到字段 private final DexPathList pathList;
    Class cDexClassLoader = BaseDexClassLoader.class;
    Field fClassLoader$DexPathList = cDexClassLoader.getDeclaredField("pathList");
    fClassLoader$DexPathList.setAccessible(true);

    //需要oDexPathListApp
    Object oAppClassLoader = context.getClassLoader();
    Object oAppDexPathList = fClassLoader$DexPathList.get(oAppClassLoader);

    //DexClassLoader
    //使用ClassLoader加载资源包
    Object oPluginClassLoader = getPluginClassLoader(Contance.getPluginPath());
    Object oPluginDexPathList = fClassLoader$DexPathList.get(oPluginClassLoader);

    //DexPathList — dexElements
    Class cDexPathList = Class.forName("dalvik.system.DexPathList");
    //private Element[] dexElements;
    Field fDexPathList$DexElements = cDexPathList.getDeclaredField("dexElements");
    fDexPathList$DexElements.setAccessible(true);
    //AppElement
    Object oAppDexPathList$DexElements = fDexPathList$DexElements.get(oAppDexPathList);
    //pluginElement
    Object oPluginDexPathList$DexElements = fDexPathList$DexElements.get(oPluginDexPathList);
    //合并
    //Element
    Class cDexPathList$Element = Class.forName("dalvik.system.DexPathList$Element");
    int appElementLength = Array.getLength(oAppDexPathList$DexElements);
    int plugElementLength = Array.getLength(oPluginDexPathList$DexElements);
    int elementCount = appElementLength + plugElementLength;

    Object newElement = Array.newInstance(cDexPathList$Element, elementCount);
    for (int i = 0; i < elementCount; i++) {
      if (i < appElementLength) {
        Array.set(newElement, i, Array.get(oAppDexPathList$DexElements, i));
      } else {
        Array.set(newElement, i, Array.get(oPluginDexPathList$DexElements, i - appElementLength));
      }
    }
    fDexPathList$DexElements.set(oAppDexPathList, newElement);
  }


  private ClassLoader getPluginClassLoader(String path) {
    String dexPath = path;
    // dexClassLoader需要一个缓存目录   /data/data/当前应用的包名/pDir
    File fileDir = context.getDir("plug_" + getApkName(path), Context.MODE_PRIVATE);
    fileDir.delete();
    if (!fileDir.exists()) {
      fileDir.mkdirs();
    }
    String optionDirectory = fileDir.getAbsolutePath();
    return new DexClassLoader(
        dexPath,
        optionDirectory,
        null,
        context.getClassLoader()
    );
  }

  private void hookLoaderActivityFor23() throws Exception {
    hookLoaderActivityFor22();
  }

  private void hookLoaderActivityFor24() throws Exception {
    hookLoaderActivityFor22();
  }

  private void hookLoaderActivityFor25() throws Exception {
    hookLoaderActivityFor22();
  }

  private void hookLoaderActivityFor26() throws Exception {

  }

  private void hookLoaderActivityFor27() throws Exception {
    hookLoaderActivityFor26();
  }

  private void hookLoaderActivityFor28() throws Exception {
    hookLoaderActivityFor26();
  }

  private void hookLoaderActivityFor29() throws Exception {
    hookLoaderActivityFor26();
  }

  private String getApkName(String path) {
    return getApkName(new File(path));
  }

  private String getApkName(File apkFile) {
    String name = apkFile.getName();
    if (name.contains(".")) {
      name = name.substring(0, name.indexOf("."));
    }
    return name;
  }

//  private String getPluginPath() {
//    File dir = new File("/sdcard/Download/");
//    if (!dir.exists()) {
//      dir.mkdirs();
//    }
//    File file = new File(dir.getAbsolutePath() + File.separator + "p.apk");
//    return file.getAbsolutePath();
//  }

}
