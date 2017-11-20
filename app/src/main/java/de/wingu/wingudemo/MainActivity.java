package de.wingu.wingudemo;

import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import de.wingu.sdk.WinguSDK;
import de.wingu.sdk.data.api.model.Channel;
import de.wingu.sdk.data.api.model.ChannelEvents;
import de.wingu.sdk.utils.prerequisite.PrerequisitesChecker;
import de.wingu.sdk.utils.prerequisite.ResolveResults;
import java.util.Collection;
import rx.Subscription;

public class MainActivity extends AppCompatActivity {

  private static final String TAG = "MainActivity";
  private static final int WINGU_SDK_PREREQUISITES_REQUEST = 535;

  @Nullable
  private Subscription channelsSubscription;

  private final ChannelAdapter channelAdapter = new ChannelAdapter();

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    RecyclerView recyclerView = findViewById(R.id.recycler_view);
    recyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    recyclerView.setAdapter(channelAdapter);

    displayCurrentChannels();
  }

  @Override
  protected void onStart() {
    super.onStart();
    if (PrerequisitesChecker.checkWithDefaultDialogs(this, WINGU_SDK_PREREQUISITES_REQUEST)) {
      listenForNearbyChannels();
    }
  }

  @Override
  protected void onStop() {
    stopListeningForNearbyChannels();
    super.onStop();
  }

  @Override
  protected void onActivityResult(int requestCode, int resultCode, Intent data) {
    super.onActivityResult(requestCode, resultCode, data);
    if (requestCode == WINGU_SDK_PREREQUISITES_REQUEST) {
      final ResolveResults results = PrerequisitesChecker.getResolveResults(data);
      if (results != null && results.getFailed().size() == 0) {
        listenForNearbyChannels();
      } else {
        Log.e(TAG, "Could not get ResolveResults or some failed");
      }
    }
  }

  @Override
  public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main_activity, menu);
    return true;
  }

  @Override
  public boolean onOptionsItemSelected(MenuItem item) {
    switch (item.getItemId()) {
      case R.id.rescan_menu_item:
        rescan();
        return true;
      default:
        return super.onOptionsItemSelected(item);
    }
  }

  private void listenForNearbyChannels() {
    if (channelsSubscription != null) {
      channelsSubscription.unsubscribe();
    }

    channelsSubscription = WinguSDK.getInstance().getNearbyChannelObserver()
        .getChannelEvents()
        .subscribe(this::onChannelEvents, throwable -> Log.e(TAG, "Could not get nearby channels", throwable));
  }

  private void onChannelEvents(ChannelEvents channelEvents) {
    for (Channel channel : channelEvents.getChannels()) {
      Log.d(TAG, "Channel event " + channelEvents.getEventType() + " " + channel);
    }

    switch (channelEvents.getEventType()) {
      case DISCOVERED:
      case UPDATED:
        channelAdapter.addOrUpdateAll(channelEvents.getChannels());
        break;
      case LOST:
        channelAdapter.removeAll(channelEvents.getChannels());
        break;
    }
  }

  private void stopListeningForNearbyChannels() {
    if (channelsSubscription != null) {
      channelsSubscription.unsubscribe();
      channelsSubscription = null;
    }
  }

  private void displayCurrentChannels() {
    final Collection<Channel> currentChannels = WinguSDK.getInstance().getNearbyChannelObserver()
        .getCurrentNearbyChannels();
    channelAdapter.clearAll();
    channelAdapter.addAll(currentChannels);
  }

  private void rescan() {
    displayCurrentChannels();
    WinguSDK.getInstance().getNearbyChannelObserver().rescan();
  }
}
