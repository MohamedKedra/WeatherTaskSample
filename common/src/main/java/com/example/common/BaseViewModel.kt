package com.example.common

import androidx.lifecycle.ViewModel

abstract class BaseViewModel : ViewModel() {

    private var requestInProgress = false

    fun <T> publishLoading(liveData: LiveDataState<T>) {
        requestInProgress = true
        liveData.postLoading()
    }

    fun <T> publishNoInternet(liveData: LiveDataState<T>) {
        requestInProgress = false
        liveData.postNoInternet()
    }

    fun <T> publishError(liveData: LiveDataState<T>, t: String) {
        requestInProgress = false
        liveData.postError(Throwable(t))
    }

    fun <T> publishResult(liveData: LiveDataState<T>, data: T?) {
        requestInProgress = false
        liveData.postSuccess(data)
    }
}