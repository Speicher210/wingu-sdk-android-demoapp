package de.wingu.wingudemo;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;
import de.wingu.sdk.data.api.model.ChannelKey;
import de.wingu.sdk.ui.details.ChannelProxyFragment;

/** You can  display custom content (based on a payload associated with a channel)
 * by creating your own subclass of {@link ChannelProxyFragment}.
 * Once the payload is available, your subclass will
 * receive a call to {@link #onCustomPayload(String)}.
 */
public class CustomChannelDetailsFragment extends ChannelProxyFragment {

  private static final String CHANNEL_KEY_ARG = "CHANNEL_KEY_ARG";

  private TextView textView;

  public CustomChannelDetailsFragment() {
  }

  public static Fragment newInstance(ChannelKey channelKey) {
    final CustomChannelDetailsFragment fragment = new CustomChannelDetailsFragment();
    final Bundle args = new Bundle();
    args.putParcelable(CHANNEL_KEY_ARG, channelKey);
    fragment.setArguments(args);
    return fragment;
  }

  @Override
  public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
    return inflater.inflate(R.layout.fragment_custom_channel_details, container, false);
  }

  @Override
  public void onViewCreated(View view, @Nullable Bundle bundle) {
    super.onViewCreated(view, bundle);
    textView = (TextView) view.findViewById(R.id.text_view);
  }

  // ChannelProxyFragment

  @Override
  protected ChannelKey getChannelKey() {
    return getArguments().getParcelable(CHANNEL_KEY_ARG);
  }

  @Override
  protected void onCustomPayload(String s) {
    textView.setText(s);
  }

  @Override
  public void onError(Throwable throwable) {
    Toast.makeText(getActivity(), throwable.getLocalizedMessage(), Toast.LENGTH_LONG).show();
  }
}
