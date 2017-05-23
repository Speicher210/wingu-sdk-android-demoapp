package de.wingu.wingudemo;

import android.app.Application;
import de.wingu.sdk.WinguSDK;
import de.wingu.sdk.WinguSDKBuilder;

public class App extends Application {
  private static WinguSDK winguSDK;

  public static WinguSDK getWinguSDK() {
    return winguSDK;
  }

  @Override
  public void onCreate() {
    super.onCreate();
    winguSDK = WinguSDKBuilder.with(this).build();
  }
}
