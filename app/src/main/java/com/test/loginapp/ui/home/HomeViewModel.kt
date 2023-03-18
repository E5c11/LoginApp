package com.test.loginapp.ui.home

import androidx.lifecycle.ViewModel
import com.test.loginapp.util.NetworkTracker
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val networkTracker: NetworkTracker
) : ViewModel() {

    val networkState = networkTracker.getNetworkState()
}