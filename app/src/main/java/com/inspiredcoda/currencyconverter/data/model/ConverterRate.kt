package com.inspiredcoda.currencyconverter.data.model

data class ConverterRate(
    val name: String?,
    val rate: Double,
    val countryFlag: Int
) {
}