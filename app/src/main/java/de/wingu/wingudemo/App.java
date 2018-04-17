package de.wingu.wingudemo;

import android.app.Application;
import android.app.PendingIntent;
import android.content.Intent;

import java.util.Collections;

import de.wingu.sdk.WinguSDKBuilder;
import de.wingu.sdk.notification.ChannelNotificationsConfig;
import de.wingu.sdk.notification.WinguNotification;

public class App extends Application {
    private static final String WINGU_API_KEY = null; // TODO

    @Override
    public void onCreate() {
        super.onCreate();

        WinguSDKBuilder.with(this, WINGU_API_KEY)
                .iBeaconBackgroundScan(true)
                .channelNotificationsConfig(this::getChannelNotificationsConfig)
//                .registerComponent(LocationWinguComponent.specMapView()) // TODO optional; needs google API key added to your AndroidManifest.xml
//                .registerComponent(VideoWinguComponent.spec(your_google_api_key)) // TODO optional
                .build();
    }

    private ChannelNotificationsConfig getChannelNotificationsConfig() {
        final ChannelNotificationsConfig.ChannelNotificationCallback notificationCallback = collection -> {
            if (collection.isEmpty()) {
                return Collections.emptyList();
            }

            final PendingIntent pendingIntent = PendingIntent.getActivity(App.this, 0, new Intent(App.this, MainActivity.class), 0);
            final String title = collection.size() + " item(s) nearby";
            final WinguNotification winguNotification = WinguNotification.createGroupNotification(collection, title, pendingIntent);
            return Collections.singleton(winguNotification);
        };

        return new ChannelNotificationsConfig.Builder(notificationCallback)
                .setSmallIcon(R.drawable.ic_notification_small)
                .setLargeIcon(R.drawable.ic_notification_large)
                .build();
    }
}
