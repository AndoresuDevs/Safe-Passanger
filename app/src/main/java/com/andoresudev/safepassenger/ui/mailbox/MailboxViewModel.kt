package com.andoresudev.safepassenger.ui.mailbox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class MailboxViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is logout Fragment"
    }
    val text: LiveData<String> = _text
}