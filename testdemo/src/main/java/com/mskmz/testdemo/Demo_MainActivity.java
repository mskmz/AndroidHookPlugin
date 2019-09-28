package com.mskmz.testdemo;

import android.os.Bundle;
import android.util.Log;

public class Demo_MainActivity extends BaseActivity {
  //>>>>>>>>>>>>>>>DEBUG配置>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>>
  private static final String TAG = "Demo_MainActivity>>>";
  private static final boolean DEBUG = true;
  //<<<<<<<<<<<<<<<DEBUG配置<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<<

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    if (DEBUG) Log.d(TAG, "onCreate: 代理成功");
    if (DEBUG) Log.d(TAG, "onCreate: " + R.layout.plug_activity_main);
    if (DEBUG) Log.d(TAG, "onCreate: " + R.id.btn_plug_jump);
    setContentView(R.layout.plug_activity_main);
//    findViewById(R.id.btn_plug_jump).setOnClickListener(new View.OnClickListener() {
//      @Override
//      public void onClick(View v) {
//        startActivity(new Intent(Demo_MainActivity.this, Demo_TestActivity.class));
//      }
//    });
  }
}
