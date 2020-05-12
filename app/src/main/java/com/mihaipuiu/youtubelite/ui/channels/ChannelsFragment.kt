package com.mihaipuiu.youtubelite.ui.channels

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.mihaipuiu.youtubelite.R

class ChannelsFragment : Fragment() {

    private lateinit var channelsViewModel: ChannelsViewModel

    override fun onCreateView(
            inflater: LayoutInflater,
            container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View? {
        channelsViewModel =
                ViewModelProviders.of(this).get(ChannelsViewModel::class.java)
        val root = inflater.inflate(R.layout.fragment_channels, container, false)
        val textView: TextView = root.findViewById(R.id.text_channels)
        channelsViewModel.text.observe(viewLifecycleOwner, Observer {
            textView.text = it
        })
        return root
    }
}
