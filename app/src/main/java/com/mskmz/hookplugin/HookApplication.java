package com.mskmz.hookplugin;

import android.app.Application;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.mskmz.hookplugin.core.HookEngine;

public class HookApplication extends Application {
  @Override
  public void onCreate() {
    super.onCreate();
    HookEngine.Hook(this);
  }

}
