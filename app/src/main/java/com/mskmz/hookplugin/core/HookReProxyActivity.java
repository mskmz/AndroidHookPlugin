package com.mskmz.hookplugin.core;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import java.lang.reflect.Field;
import java.lang.reflect.Method;

// 在创建ActivityThread.lunch
public class HookReProxyActivity {
  //>>>>>>>>>>>>>>>DEBUG配置>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
  private static final String TAG = "HookReProxyActivity>>>";
  private static final boolean DEBUG = true;
  //<<<<<<<<<<<<<<<DEBUG配置<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<
  public static final int LAUNCH_ACTIVITY = 100;

  Context context;

  public HookReProxyActivity(Context context) {
    this.context = context;
  }

  public void hook() throws Exception {
    HookReProxyActivityFor22();
  }

  private void HookReProxyActivityFor22() throws Exception {


    // 获取@3 --ActivityThread
    //Reference to singleton {@link ActivityThread} */
    //private static volatile ActivityThread sCurrentActivityThread;
    Class cActivityThreadCls = Class.forName("android.app.ActivityThread");
    // public static ActivityThread currentActivityThread() {
    //        return sCurrentActivityThread;
    //  }
    Method mActivityThread$currThread = cActivityThreadCls.getDeclaredMethod("currentActivityThread");
    Object oActivityThread = mActivityThread$currThread.invoke(null);

    //获取@1 -Handler final H mH = new H();
    Method mActivityThread$mH = cActivityThreadCls.getDeclaredMethod("getHandler");
    mActivityThread$mH.setAccessible(true);
    Object oActivityThread$mH = mActivityThread$mH.invoke(oActivityThread);

    //在Handle handleMessage 中的LAUNCH_ACTIVITY切换线程
    //由于是Handler 所以不需要hook 可以直接修改
    // public void dispatchMessage(Message msg) {
    //        if (msg.callback != null) {
    //            handleCallback(msg);
    //        } else {
    //            if (mCallback != null) {
    //                if (mCallback.handleMessage(msg)) {
    //                    return;
    //                }
    //            }
    //            handleMessage(msg);
    //        }
    //    }
    // 中的mCallback 就可以
    Class cHandlerCls = Class.forName("android.os.Handler");
    Field fHandler$Callback = cHandlerCls.getDeclaredField("mCallback");
    fHandler$Callback.setAccessible(true);
    //@1  @2
    fHandler$Callback.set(oActivityThread$mH, new Handler.Callback() {
      @Override
      public boolean handleMessage(Message msg) {
        switch (msg.what) {
          case LAUNCH_ACTIVITY:
            if (msg.obj != null) {
              //               ActivityClientRecord r = (ActivityClientRecord) msg.obj;
              try {
                Class cActivityRecord = Class.forName("android.app.ActivityThread$ActivityClientRecord");
                Field fIntentField = cActivityRecord.getDeclaredField("intent");
                fIntentField.setAccessible(true);
                Intent intent = (Intent) fIntentField.get(msg.obj);
                if (intent.hasExtra(Contance.PROXY_SRC_INTENT)) {
                  fIntentField.set(msg.obj, intent.getParcelableExtra(Contance.PROXY_SRC_INTENT));
                }
              } catch (Exception e) {
                e.printStackTrace();
              }
            }
            break;
        }
        return false;
      }
    });

  }

  private void HookReProxyActivityFor23() throws Exception {
    HookReProxyActivityFor22();
  }


  private void HookReProxyActivityFor24() throws Exception {
    HookReProxyActivityFor22();
  }

  private void HookReProxyActivityFor25() throws Exception {
    HookReProxyActivityFor22();
  }

  private void HookReProxyActivityFor26() throws Exception {

  }

  private void HookReProxyActivityFor27() throws Exception {
    HookReProxyActivityFor26();
  }

  private void HookReProxyActivityFor28() throws Exception {
    HookReProxyActivityFor26();
  }

  private void HookReProxyActivityFor29() throws Exception {
    HookReProxyActivityFor26();
  }

}
