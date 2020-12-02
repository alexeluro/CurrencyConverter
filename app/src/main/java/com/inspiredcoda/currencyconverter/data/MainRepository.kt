package com.inspiredcoda.currencyconverter.data

import android.util.Log
import com.inspiredcoda.currencyconverter.data.dao.MultipleRatesDao
import com.inspiredcoda.currencyconverter.data.dao.SingleRateDao
import com.inspiredcoda.currencyconverter.data.model.ConverterResponse
import com.inspiredcoda.currencyconverter.data.entity.Rates
import com.inspiredcoda.currencyconverter.data.model.HistoryResponse.HistoryResponse
import com.inspiredcoda.currencyconverter.ui.ProgressStateListener
import com.inspiredcoda.currencyconverter.util.ApiException
import com.inspiredcoda.currencyconverter.util.ConverterCoroutines
import com.inspiredcoda.currencyconverter.util.NoInternetException
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*
import javax.inject.Inject

class MainRepository @Inject constructor(
    private val api: Api,
    private val singleRateDao: SingleRateDao,
    private val multipleRatesDao: MultipleRatesDao
) : SafeApiRequest() {

    private val TAG = "MainRepository"
    private lateinit var calendar: Calendar

    private suspend fun getLatestRates(): ConverterResponse {
        return apiRequest { api.getLatestExchangeRates() }
    }

    suspend fun getStoredRates(progressStateListener: ProgressStateListener
    ): Rates {
        ConverterCoroutines.io {
            try {
                withContext(Dispatchers.Main){
                    progressStateListener.onLoading()
                }

                singleRateDao.clearRecords()

                val rates = getLatestRates()

                withContext(Dispatchers.Main){
                    progressStateListener.updateMessage("Saving into the database...")
                }

                Log.d(TAG, "getStoredRates: ${rates.rates.nGN}")
                singleRateDao.insertRates(rates.rates)

                withContext(Dispatchers.Main){
                    progressStateListener.onSuccess()
                }

            }catch (e: NoInternetException){
                withContext(Dispatchers.Main){
                    progressStateListener.onFailure(e.message!!)
                }
            }catch (e: ApiException){
                withContext(Dispatchers.Main){
                    progressStateListener.onFailure(e.message!!)
                }
            }catch (e: IOException){
                withContext(Dispatchers.Main){
                    progressStateListener.onFailure(e.message!!)
                }
            }
        }
        return singleRateDao.getTodaysRates()
    }


    private suspend fun getRecordsForSomeDaysFromApi(date: String): ConverterResponse {
        return apiRequest { api.getRecordsByDate(date) }
    }

    suspend fun fetchSaveAndRetrieveRecordsForSomeDays(
        days: Int,
        progressStateListener: ProgressStateListener
    ): List<HistoryResponse>{
        withContext(Dispatchers.Main){
            progressStateListener.onLoading()
        }
        multipleRatesDao.clearRecords()
        try {
            for (x in days downTo 1) {
                withContext(Dispatchers.Main) {
                    progressStateListener.updateMessage("Saving record for day $x")
                }
                val date = getDateFromNDaysAgo(x)

//            val record = getRecordsForSomeDaysFromApi(date).rates
                multipleRatesDao.insertRates(
                    HistoryResponse(
                        date = date,
                        rates = getRecordsForSomeDaysFromApi(date).rates
                    )
                )
            }

            withContext(Dispatchers.Main) {
                progressStateListener.onSuccess()
            }
        }catch (e: NoInternetException){
            withContext(Dispatchers.Main){
                progressStateListener.onFailure(e.message!!)
            }
        }catch (e: ApiException){
            withContext(Dispatchers.Main){
                progressStateListener.onFailure(e.message!!)
            }
        }catch (e: IOException){
            withContext(Dispatchers.Main){
                progressStateListener.onFailure(e.message!!)
            }
        }
        return multipleRatesDao.getRecordsFrom30Days()
    }

    private fun getDateFromNDaysAgo(daysAgo: Int): String {
        calendar = Calendar.getInstance()
        calendar.add(Calendar.DATE, -daysAgo)
        val simpleDateFormat = SimpleDateFormat("YYYY-MM-dd", Locale.getDefault())

//        return calendar.time
        return simpleDateFormat.format(calendar.time)
    }



}