package com.mskmz.hookplugin.core;

import android.content.Context;
import android.util.Log;

public class HookEngine {
  //>>>>>>>>>>>>>>>DEBUG配置>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
  private static final String TAG = "HookEngine>>>";
  private static final boolean DEBUG = true;

  //<<<<<<<<<<<<<<<DEBUG配置<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
  public static void Hook(Context context) {
    try {
      new HookCheckActivity(context).hook();
      if (DEBUG) Log.d(TAG, "HookCheckActivity:完成 ");
      new HookReProxyActivity(context).hook();
      if (DEBUG) Log.d(TAG, "HookReProxyActivity:完成 ");
//      new HookMergeDex(context).hook();
      new HookLoader(context).hook();
      if (DEBUG) Log.d(TAG, "HookLoader:完成 ");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
