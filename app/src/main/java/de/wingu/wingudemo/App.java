package de.wingu.wingudemo;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import java.util.Collection;
import java.util.Collections;

import de.wingu.sdk.WinguSDKBuilder;
import de.wingu.sdk.data.api.model.Channel;
import de.wingu.sdk.notification.ChannelNotificationsConfig;
import de.wingu.sdk.notification.WinguNotification;

public class App extends Application {
  private static final String WINGU_API_KEY = null; // TODO

  @Override
  public void onCreate() {
    super.onCreate();

    final ChannelNotificationsConfig.ChannelNotificationCallback notificationCallback = collection -> {
      if (collection.isEmpty()) {
        return Collections.emptyList();
      }

      final PendingIntent pendingIntent = PendingIntent.getActivity(App.this, 0, new Intent(App.this, MainActivity.class), 0);
      final String title = collection.size() + " item(s) nearby";
      final WinguNotification winguNotification = WinguNotification.createGroupNotification(collection, title, pendingIntent);
      return Collections.singleton(winguNotification);
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
