package com.mskmz.hookplugin.core;

import android.content.Context;
import android.content.Intent;
import android.util.Log;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

//干掉送检的过程
public class HookCheckActivity {
  //---------------DEBUG配置---------------------------------------------------------------------------
  private static final String TAG = "HookCheckActivity>>>";
  private static final boolean DEBUG = false;

  Context context;

  public HookCheckActivity(Context context) {
    this.context = context;
  }

  public void hook() throws Exception {
    hookCheckActivityFor22();
  }

  private void hookCheckActivityFor22() throws Exception {
    if (DEBUG) Log.d(TAG, "hookCheckActivityFor22: ");
    //如何获取gDefault - 在ActivityManagerNative中
    // @src = gDefult.get
    Class rActivityManagerNativeClass = Class.forName("android.app.ActivityManagerNative");
    Field gDefaultField = rActivityManagerNativeClass.getDeclaredField("gDefault");
    //@AMN = ActivityManagerNative -- 是静态的 不需要对象就存在;
    gDefaultField.setAccessible(true); // 授权

    // private static final Singleton<IActivityManager> gDefault；
    Object gDefault = gDefaultField.get(null);
    //static public IActivityManager getDefault() {
    //        return gDefault.get();
    //}
    final Object srcIActivityManager = rActivityManagerNativeClass
        .getDeclaredMethod("getDefault")
        .invoke(null);


    //先写结论最终修改修改
    //IActivityManager
    //          startActivity(whoThread, who.getBasePackageName(), intent,
    //                  intent.resolveTypeIfNeeded(who.getContentResolver()),
    //                  token, target != null ? target.mEmbeddedID : null,
    //                  requestCode, 0, null, options);
    Class rIActivityManagerClass = Class.forName("android.app.IActivityManager");
    Object rIActivityManagerProxy = Proxy.newProxyInstance(
        context.getClassLoader(),
        new Class[]{rIActivityManagerClass},
        new InvocationHandler() {
          @Override
          public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
            if (DEBUG) Log.d(TAG, "invoke: " + method + "被调用");
            if (method.getName().equals("startActivity")) {
              if (DEBUG) Log.d(TAG, "invoke: startActivity被调用");
              Intent intent = new Intent(context, ProxyActivity.class);
              intent.putExtra(Contance.PROXY_SRC_INTENT, ((Intent) args[2]));
              args[2] = intent;
            }
            //@Src 原对象
            return method.invoke(srcIActivityManager, args);
          }
        });
    //public abstract class Singleton<T> {
    //    private T mInstance = @1;
    //
    //    protected abstract T create();
    //
    //    public final T get() {
    //        synchronized (this) {
    //            if (mInstance == null) {
    //                mInstance = create();
    //            }
    //            return mInstance;
    //        }
    //    }
    //}
    //@Src 原对象
    Class singletonClass = Class.forName("android.util.Singleton");
    Field rSingletonField = singletonClass.getDeclaredField("mInstance");
    rSingletonField.setAccessible(true); // 授权
    rSingletonField.set(gDefault, rIActivityManagerProxy);
    if (DEBUG) Log.d(TAG, "hookCheckActivityFor22: >>>hook成功");
  }

  private void hookCheckActivityFor23() throws Exception {
    hookCheckActivityFor22();
  }

  private void hookCheckActivityFor24() throws Exception {
    hookCheckActivityFor22();
  }

  private void hookCheckActivityFor25() throws Exception {
    hookCheckActivityFor22();
  }

  private void hookCheckActivityFor26() throws Exception {

  }

  private void hookCheckActivityFor27() throws Exception {
    hookCheckActivityFor26();
  }

  private void hookCheckActivityFor28() throws Exception {
    hookCheckActivityFor26();
  }

  private void hookCheckActivityFor29() throws Exception {
    hookCheckActivityFor26();
  }

}
