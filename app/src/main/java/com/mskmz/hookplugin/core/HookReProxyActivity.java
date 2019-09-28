package com.mskmz.hookplugin.core;

import android.content.Context;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageInfo;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

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
                  Intent intentSrc = intent.getParcelableExtra(Contance.PROXY_SRC_INTENT);
                  fIntentField.set(msg.obj, intentSrc);
                  if (DEBUG) {
                    Log.d(TAG, "handleMessage: " + intentSrc);
                    Log.d(TAG, "handleMessage: " + intentSrc.getComponent().getPackageName());
                    Log.d(TAG, "handleMessage: " + intentSrc.getComponent().getClassName());
                  }
                  //修改cActivityRecord 中的 activityInfo.packageInfo
                  Field fActivityRecord$activityInfo = cActivityRecord.getDeclaredField("activityInfo");
                  fActivityRecord$activityInfo.setAccessible(true);
                  ActivityInfo activityInfo = (ActivityInfo) fActivityRecord$activityInfo.get(msg.obj);
                  //在这里 我们需要替换PackageInfo
                  hookGetPackageInfo();

                  if (intentSrc.getPackage() == null) {
                    activityInfo.applicationInfo.packageName = intentSrc.getComponent().getPackageName();
                  } else {
                    activityInfo.applicationInfo.packageName = intentSrc.getPackage();
                  }


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

  // Hook 拦截此 getPackageInfo 做自己的逻辑
  private void hookGetPackageInfo() {
    try {
      // sPackageManager 替换  我们自己的动态代理
      Class mActivityThreadClass = Class.forName("android.app.ActivityThread");
      Field sCurrentActivityThreadField = mActivityThreadClass.getDeclaredField("sCurrentActivityThread");
      sCurrentActivityThreadField.setAccessible(true);

      Field sPackageManagerField = mActivityThreadClass.getDeclaredField("sPackageManager");
      sPackageManagerField.setAccessible(true);
      final Object packageManager = sPackageManagerField.get(null);

      /**
       * 动态代理
       */
      Class mIPackageManagerClass = Class.forName("android.content.pm.IPackageManager");

      Object mIPackageManagerProxy = Proxy.newProxyInstance(context.getClassLoader(),

          new Class[]{mIPackageManagerClass}, // 要监听的接口

          new InvocationHandler() {
            @Override
            public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
              if ("getPackageInfo".equals(method.getName())) {
                // 如何才能绕过 PMS, 欺骗系统

                // pi != null
                return new PackageInfo(); // 成功绕过 PMS检测
              }
              // 让系统正常继续执行下去
              return method.invoke(packageManager, args);
            }
          });


      // 替换  狸猫换太子   换成我们自己的 动态代理
      sPackageManagerField.set(null, mIPackageManagerProxy);

    } catch (Exception e) {
      e.printStackTrace();
    }
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
