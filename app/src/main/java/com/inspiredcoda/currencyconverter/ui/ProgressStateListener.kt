package com.inspiredcoda.currencyconverter.ui

interface ProgressStateListener {

    fun onLoading()

    fun updateMessage(message: String)

    fun onFailure(message: String)

    fun onSuccess()

}