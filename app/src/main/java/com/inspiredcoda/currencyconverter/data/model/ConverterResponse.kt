package com.inspiredcoda.currencyconverter.data.model


import com.google.gson.annotations.SerializedName
import com.inspiredcoda.currencyconverter.data.entity.Rates

data class ConverterResponse(
    @SerializedName("base")
    val base: String,
    @SerializedName("date")
    val date: String,
    @SerializedName("rates")
    val rates: Rates,
    @SerializedName("success")
    val success: Boolean,
    @SerializedName("timestamp")
    val timestamp: Int
)