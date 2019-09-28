package com.mskmz.hookplugin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.File;

import dalvik.system.DexClassLoader;

public class MainActivity extends AppCompatActivity {
  //>>>>>>>>>>>>>>>DEBUG配置>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
  private static final String TAG = "MainActivity>>>";
  private static final boolean DEBUG = true;
  //<<<<<<<<<<<<<<<DEBUG配置<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    findViewById(R.id.btn_jump).setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        jump();
      }
    });
  }

  public void jump() {
//    test();
    Intent intent = new Intent();
    intent.setComponent(new ComponentName("com.mskmz.testdemo", "com.mskmz.testdemo.Demo_MainActivity"));
//    intent.setComponent(new ComponentName("com.mskmz.hookplugin", "com.mskmz.hookplugin.Demo_TestActivity"));
    startActivity(intent);
  }

  private void test() {
    File cache = getDir("pDir", Context.MODE_PRIVATE);
    if (!cache.exists()) {
      cache.mkdirs();
    }
    File dir = new File("/sdcard/Download/");
    if (!dir.exists()) {
      dir.mkdirs();
    }
    File file = new File(dir.getAbsolutePath() + File.separator + "p.apk");
    if (!file.exists()) {
      if (DEBUG) Log.d(TAG, "test: 类丢失");
    }
    // 加载Activity
    ClassLoader c = new DexClassLoader(file.getAbsolutePath(), cache.getAbsolutePath(), null, getClassLoader());
    try {
      if (DEBUG) Log.d(TAG, "test: " + c);
      Class a = c.loadClass("com.mskmz.testdemo.Demo_MainActivity");
      if (DEBUG) Log.d(TAG, "test: " + a);
    } catch (ClassNotFoundException e) {
      e.printStackTrace();
    }
  }

}
