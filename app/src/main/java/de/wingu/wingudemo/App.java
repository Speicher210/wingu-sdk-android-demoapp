package de.wingu.wingudemo;

import android.app.Application;
import de.wingu.sdk.WinguSDKBuilder;

public class App extends Application {
  private static final String WINGU_API_KEY = null; // TODO

  @Override
  public void onCreate() {
    super.onCreate();
    WinguSDKBuilder.with(this, WINGU_API_KEY).build();
  }
}
