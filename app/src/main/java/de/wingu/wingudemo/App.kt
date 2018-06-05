package de.wingu.wingudemo

import android.app.Application
import android.content.Context
import android.content.Intent
import de.wingu.sdk.WinguSDKBuilder
import de.wingu.sdk.data.api.model.Channel
import de.wingu.sdk.notification.ChannelNotificationsConfig
import de.wingu.sdk.notification.WinguNotification
import java.util.concurrent.Callable

class App : Application() {

    override fun onCreate() {
        super.onCreate()

        WinguSDKBuilder.with(this, WINGU_API_KEY)
                .ibeaconBackgroundScan(true)
                .channelNotificationsConfig(Callable { this.getChannelNotificationsConfig() })
                //                .registerComponent(LocationWinguComponent.specMapView()) // TODO optional; needs google API key added to your AndroidManifest.xml
                //                .registerComponent(VideoWinguComponent.spec(your_google_api_key)) // TODO optional
                .build()
    }

    private fun getChannelNotificationsConfig(): ChannelNotificationsConfig {


        return ChannelNotificationsConfig.Builder(MyChannelNotificationCallback(applicationContext))
                .setSmallIcon(R.drawable.ic_notification_small)
                .setLargeIcon(R.drawable.ic_notification_large)
                .build()
    }

    companion object {
        private val WINGU_API_KEY: String? = null // TODO
    }

    class MyChannelNotificationCallback(private val appContext: Context) : ChannelNotificationsConfig.ChannelNotificationCallback {
        override fun winguNotifications(channels: Collection<Channel>): Collection<WinguNotification> {
            if (channels.isEmpty()) {
                return emptyList()
            }
            return channels.map { channel ->
                val intent = WinguNotification
                        .createDefaultChannelPendingIntent(channel, Intent(appContext, MainActivity::class.java))
                WinguNotification.createSingleChannelNotification(channel, channel.name, "Your description", intent)
            }
        }

    }
}
