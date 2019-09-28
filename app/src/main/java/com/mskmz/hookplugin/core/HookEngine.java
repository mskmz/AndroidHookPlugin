package com.mskmz.hookplugin.core;

import android.content.Context;

public class HookEngine {
  public static void Hook(Context context) {
    try {
      new HookCheckActivity(context).hook();
      new HookReProxyActivity(context).hook();
      new HookLoaderFullActivity(context).hook();
    } catch (Exception e) {
      e.printStackTrace();
    }
  }
}
