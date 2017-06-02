package de.wingu.wingudemo;

import android.app.Application;
import de.wingu.sdk.WinguSDKBuilder;

public class App extends Application {

  @Override
  public void onCreate() {
    super.onCreate();
    WinguSDKBuilder.with(this).build();
  }
}
