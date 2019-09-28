package com.mskmz.hookplugin.core;

import dalvik.system.DexClassLoader;

//修改Loader 方案一 合并package
public class PluginClassLoader extends DexClassLoader {

  public PluginClassLoader(String dexPath, String optimizedDirectory, String librarySearchPath, ClassLoader parent) {
    super(dexPath, optimizedDirectory, librarySearchPath, parent);
  }
}
