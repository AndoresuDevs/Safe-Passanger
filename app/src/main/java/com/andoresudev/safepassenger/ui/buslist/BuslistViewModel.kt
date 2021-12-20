package com.andoresudev.safepassenger.ui.buslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class BuslistViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is buslist Fragment"
    }
    val text: LiveData<String> = _text
}