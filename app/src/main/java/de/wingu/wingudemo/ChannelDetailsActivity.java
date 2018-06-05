package de.wingu.wingudemo;

import android.content.Context;
import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import de.wingu.sdk.data.api.model.Channel;
import de.wingu.sdk.data.api.model.ChannelKey;
import de.wingu.sdk.ui.details.ChannelDetailsFragment;

public class ChannelDetailsActivity extends AppCompatActivity {
  private static final String CHANNEL_EXTRA = "CHANNEL_EXTRA";
  private static final String TITLE_EXTRA = "TITLE_EXTRA";

  private static final String CHANNEL_FRAGMENT_TAG = "CHANNEL_FRAGMENT_TAG";

  public static Intent newIntent(Context context, Channel channel, String title) {
    Intent intent = new Intent(context, ChannelDetailsActivity.class);
    intent.putExtra(CHANNEL_EXTRA, ChannelKey.from(channel));
    intent.putExtra(TITLE_EXTRA, title);
    return intent;
  }

  @Override
  protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_channel_details);

    FragmentManager fm = getSupportFragmentManager();
    if (fm.findFragmentByTag(CHANNEL_FRAGMENT_TAG) == null) {
      ChannelKey channelKey = getIntent().getParcelableExtra(CHANNEL_EXTRA);
      Fragment fragment = ChannelDetailsFragment.newInstance(channelKey);
      fm.beginTransaction()
          .replace(R.id.frame, fragment)
          .commit();
    }

    String title = getIntent().getStringExtra(TITLE_EXTRA);
    setTitle(title);
  }
}
