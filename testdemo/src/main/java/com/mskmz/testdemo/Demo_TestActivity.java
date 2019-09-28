package com.mskmz.testdemo;

import android.content.res.AssetManager;
import android.content.res.Resources;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

public class Demo_TestActivity extends AppCompatActivity {

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.plug_activity_main);
  }
  @Override
  public Resources getResources() {
    return getApplication().getResources();
  }

  @Override
  public AssetManager getAssets() {
    return getApplication().getAssets();
  }
}
