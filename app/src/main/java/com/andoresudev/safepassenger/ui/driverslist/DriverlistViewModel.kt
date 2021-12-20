package com.andoresudev.safepassenger.ui.driverslist

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class DriverlistViewModel: ViewModel() {
    private val _text = MutableLiveData<String>().apply {
        value = "This is bus Fragment"
    }
    val text: LiveData<String> = _text
}