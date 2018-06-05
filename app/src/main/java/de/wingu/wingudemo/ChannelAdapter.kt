package de.wingu.wingudemo

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import de.wingu.sdk.data.api.model.Channel
import de.wingu.sdk.ui.details.ChannelDetailsActivity
import de.wingu.wingudemo.ChannelAdapter.ChannelViewHolder

internal class ChannelAdapter : RecyclerView.Adapter<ChannelViewHolder>() {
    private val channels: MutableList<Channel> = mutableListOf()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.channel_row, parent, false) as ViewGroup
        return ChannelViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        val channel = channels[position]
        holder.textView.text = channel.name
        holder.itemView.setOnClickListener { v ->
            val intent = ChannelDetailsActivity.newInstance(v.context, channel, channel.name)
            v.context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return channels.size
    }

    fun addAll(channelCollection: Collection<Channel>) {
        val a = channels.size
        this.channels.addAll(channelCollection)
        notifyItemRangeInserted(a, channelCollection.size)
    }

    private fun addOrUpdate(channel: Channel) {
        val idx = channels.indexOf(channel)
        if (idx >= 0) {
            channels[idx] = channel
            notifyItemChanged(idx)
        } else {
            channels.add(channel)
            notifyItemInserted(channels.size - 1)
        }
    }

    fun addOrUpdateAll(channelCollection: Collection<Channel>) {
        for (channel in channelCollection) {
            addOrUpdate(channel)
        }
    }

    fun clearAll() {
        val count = channels.size
        channels.clear()
        notifyItemRangeRemoved(0, count)
    }

    private fun remove(channel: Channel) {
        val idx = channels.indexOf(channel)
        if (idx >= 0) {
            channels.removeAt(idx)
            notifyItemRemoved(idx)
        }
    }

    fun removeAll(channelCollection: Collection<Channel>) {
        for (channel in channelCollection) {
            remove(channel)
        }
    }

    internal class ChannelViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById<View>(R.id.text_view) as TextView
    }
}
