package com.mihaipuiu.youtubelite.ui.channels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ChannelsViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is channels Fragment"
    }
    val text: LiveData<String> = _text
}