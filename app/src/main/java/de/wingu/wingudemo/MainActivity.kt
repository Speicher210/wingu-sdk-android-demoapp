package de.wingu.wingudemo

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import de.wingu.sdk.WinguSDK
import de.wingu.sdk.data.api.model.ChannelEventType
import de.wingu.sdk.data.api.model.ChannelEvents
import de.wingu.sdk.utils.prerequisite.PrerequisitesChecker
import rx.Subscription

class MainActivity : AppCompatActivity() {

    private var channelsSubscription: Subscription? = null

    private val channelAdapter = ChannelAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.recycler_view)
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        recyclerView.adapter = channelAdapter

        displayCurrentChannels()
    }

    override fun onStart() {
        super.onStart()
        if (PrerequisitesChecker.checkWithDefaultDialogs(this, WINGU_SDK_PREREQUISITES_REQUEST)) {
            listenForNearbyChannels()
        }
    }

    override fun onStop() {
        stopListeningForNearbyChannels()
        super.onStop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == WINGU_SDK_PREREQUISITES_REQUEST) {
            val results = PrerequisitesChecker.getResolveResults(data)
            if (results != null && results.failed.size == 0) {
                listenForNearbyChannels()
            } else {
                Log.e(TAG, "Could not get ResolveResults or some failed")
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        menuInflater.inflate(R.menu.main_activity, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.rescan_menu_item -> {
                rescan()
                return true
            }
            else -> return super.onOptionsItemSelected(item)
        }
    }

    private fun listenForNearbyChannels() {
        if (channelsSubscription != null) {
            channelsSubscription!!.unsubscribe()
        }

        channelsSubscription = WinguSDK.getInstance().getNearbyChannelObserver()
                .getChannelEvents()
                .subscribe({ this.onChannelEvents(it) },
                        { throwable -> Log.e(TAG, "Could not get nearby channels", throwable) }
                )
    }

    private fun onChannelEvents(channelEvents: ChannelEvents) {
        for (channel in channelEvents.channels) {
            Log.d(TAG, "Channel event " + channelEvents.eventType + " " + channel)
        }

        when (channelEvents.eventType) {
            ChannelEventType.DISCOVERED, ChannelEventType.UPDATED -> channelAdapter.addOrUpdateAll(channelEvents.channels)
            ChannelEventType.LOST -> channelAdapter.removeAll(channelEvents.channels)
        }
    }

    private fun stopListeningForNearbyChannels() {
        if (channelsSubscription != null) {
            channelsSubscription!!.unsubscribe()
            channelsSubscription = null
        }
    }

    private fun displayCurrentChannels() {
        val currentChannels = WinguSDK.getInstance().getNearbyChannelObserver()
                .getCurrentNearbyChannels()
        channelAdapter.clearAll()
        channelAdapter.addAll(currentChannels)
    }

    private fun rescan() {
        displayCurrentChannels()
        WinguSDK.getInstance().getNearbyChannelObserver().rescan()
    }

    companion object {

        private val TAG = "MainActivity"
        private val WINGU_SDK_PREREQUISITES_REQUEST = 535
    }
}
