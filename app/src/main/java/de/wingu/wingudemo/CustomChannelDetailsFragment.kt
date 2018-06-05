package de.wingu.wingudemo


import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import de.wingu.sdk.data.api.model.ChannelKey
import de.wingu.sdk.ui.details.ChannelProxyFragment

/** You can  display custom content (based on a payload associated with a channel)
 * by creating your own subclass of [ChannelProxyFragment].
 * Once the payload is available, your subclass will
 * receive a call to [.onCustomPayload].
 */
class CustomChannelDetailsFragment : ChannelProxyFragment() {
    override fun onError(throwable: Throwable) {
        Toast.makeText(activity!!, throwable.localizedMessage, Toast.LENGTH_LONG).show()
    }

    override fun onCustomPayload(payload: String?) {
        textView!!.text = payload
    }

    private var textView: TextView? = null

    // ChannelProxyFragment

    override val channelKey: ChannelKey by lazy { arguments!!.getParcelable<ChannelKey>(CHANNEL_KEY_ARG) }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_custom_channel_details, container, false)
    }

    override fun onViewCreated(view: View, bundle: Bundle?) {
        super.onViewCreated(view, bundle)
        textView = view.findViewById<View>(R.id.text_view) as TextView
    }


    companion object {

        private val CHANNEL_KEY_ARG = "CHANNEL_KEY_ARG"

        fun newInstance(channelKey: ChannelKey): Fragment {
            val fragment = CustomChannelDetailsFragment()
            val args = Bundle()
            args.putParcelable(CHANNEL_KEY_ARG, channelKey)
            fragment.arguments = args
            return fragment
        }
    }
}
