package com.inspiredcoda.currencyconverter.ui

import androidx.hilt.lifecycle.ViewModelInject
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.inspiredcoda.currencyconverter.data.MainRepository
import com.inspiredcoda.currencyconverter.data.entity.Rates
import com.inspiredcoda.currencyconverter.data.model.HistoryResponse.HistoryResponse
import com.inspiredcoda.currencyconverter.util.Constants.RateRecords.SINGLE_MONTH
import com.inspiredcoda.currencyconverter.util.ConverterCoroutines
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*


class MainActivityViewModel @ViewModelInject constructor(
    private val repository: MainRepository
): ViewModel() {

    private var mutableRates: MutableLiveData<Rates> = MutableLiveData()
    private var mutableRateRecords: MutableLiveData<List<HistoryResponse>> = MutableLiveData()
    private lateinit var calendar: Calendar

    val liveRate: LiveData<Rates>
    get() = mutableRates

    val liveRateRecordsForSomeDays: LiveData<List<HistoryResponse>>
    get() = mutableRateRecords

    fun viewModelPrep(progressStateListener: ProgressStateListener){
        ConverterCoroutines.io {
            mutableRates.postValue(repository.getStoredRates(progressStateListener))
            mutableRateRecords.postValue(repository.fetchSaveAndRetrieveRecordsForSomeDays(SINGLE_MONTH, progressStateListener))
        }
    }




    private fun getExchangeRate(
        countries: List<Double>,
        fromCurrencyPos: Int,
    ): Double{
        return 1/countries[fromCurrencyPos]
    }

    fun convertAmount(
        countries: List<Double>,
        fromCurrencyPos: Int,
        toCurrencyPos: Int,
        amount: Double
    ): Double{
        val exchangeRate = getExchangeRate(countries, fromCurrencyPos)
        val newExchangeRate = exchangeRate * countries[toCurrencyPos]
        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.FLOOR
        return decimalFormat.format(amount * newExchangeRate).toDouble()
    }



}