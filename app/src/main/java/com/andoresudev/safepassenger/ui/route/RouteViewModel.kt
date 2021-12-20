package com.andoresudev.safepassenger.ui.route

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class RouteViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is logout Fragment"
    }
    val text: LiveData<String> = _text
}