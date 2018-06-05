package de.wingu.wingudemo;

import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import de.wingu.sdk.data.api.model.Channel;
import de.wingu.sdk.ui.details.ChannelDetailsActivity;
import de.wingu.wingudemo.ChannelAdapter.ChannelViewHolder;

class ChannelAdapter extends RecyclerView.Adapter<ChannelViewHolder> {
    private List<Channel> channels;

    ChannelAdapter() {
        channels = new ArrayList<>();
    }

    @Override
    public ChannelViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final ViewGroup itemView = (ViewGroup) LayoutInflater.from(parent.getContext()).inflate(R.layout.channel_row, parent, false);
        return new ChannelViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ChannelViewHolder holder, int position) {
        final Channel channel = channels.get(position);
        holder.textView.setText(channel.getName());
        holder.itemView.setOnClickListener(v -> {
            Intent intent = ChannelDetailsActivity.newInstance(v.getContext(), channel, channel.getName());
            v.getContext().startActivity(intent);
        });
    }


    @Override
    public int getItemCount() {
        return channels.size();
    }

    void addAll(Collection<Channel> channelCollection) {
        final int a = channels.size();
        this.channels.addAll(channelCollection);
        notifyItemRangeInserted(a, channelCollection.size());
    }

    private void addOrUpdate(Channel channel) {
        final int idx = channels.indexOf(channel);
        if (idx >= 0) {
            channels.set(idx, channel);
            notifyItemChanged(idx);
        } else {
            channels.add(channel);
            notifyItemInserted(channels.size() - 1);
        }
    }

    void addOrUpdateAll(Collection<Channel> channelCollection) {
        for (Channel channel : channelCollection) {
            addOrUpdate(channel);
        }
    }

    void clearAll() {
        final int count = channels.size();
        channels.clear();
        notifyItemRangeRemoved(0, count);
    }

    private void remove(Channel channel) {
        final int idx = channels.indexOf(channel);
        if (idx >= 0) {
            channels.remove(idx);
            notifyItemRemoved(idx);
        }
    }

    void removeAll(Collection<Channel> channelCollection) {
        for (Channel channel : channelCollection) {
            remove(channel);
        }
    }

    static class ChannelViewHolder extends RecyclerView.ViewHolder {
        final TextView textView;

        ChannelViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}
