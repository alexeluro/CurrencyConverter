package com.inspiredcoda.currencyconverter.util

import kotlinx.coroutines.*

object ConverterCoroutines {

    fun main(work: suspend() -> Unit) =
        CoroutineScope(Dispatchers.Main).launch {
            work.invoke()
        }

    fun io(work: suspend() -> Unit) =
        CoroutineScope(Dispatchers.IO).launch {
            work.invoke()
        }

}
