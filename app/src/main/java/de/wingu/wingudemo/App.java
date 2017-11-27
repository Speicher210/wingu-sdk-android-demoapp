package de.wingu.wingudemo;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.Nullable;

import java.util.Collection;

import de.wingu.sdk.WinguSDKBuilder;
import de.wingu.sdk.data.api.model.Channel;
import de.wingu.sdk.notification.ChannelNotificationsConfig;

public class App extends Application {
  private static final String WINGU_API_KEY = null; // TODO

  @Override
  public void onCreate() {
    super.onCreate();

    final ChannelNotificationsConfig.ChannelNotificationCallback notificationCallback = new ChannelNotificationsConfig.ChannelNotificationCallback() {
      @Override
      public String contentText(Collection<Channel> collection) {
        return collection.size() + " item(s) nearby";
      }

      @Override
      public String contentTitle(Collection<Channel> collection) {
        return "Nearby";
      }

      @Nullable
      @Override
      public PendingIntent pendingIntent(Collection<Channel> collection) {
        if (collection.isEmpty()) {
          return null;
        }
        return PendingIntent.getActivity(App.this, 0, new Intent(App.this, MainActivity.class), 0);
      }
    };

    WinguSDKBuilder.with(this, WINGU_API_KEY)
        .iBeaconBackgroundScan(true)
        .channelNotificationsConfig(() ->
            new ChannelNotificationsConfig.Builder(notificationCallback)
                .setSmallIcon(R.drawable.ic_notification_small)
                .setLargeIcon(R.drawable.ic_notification_large)
                .build())
        .build();
  }
}
