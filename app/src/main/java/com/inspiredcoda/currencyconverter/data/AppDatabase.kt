package com.inspiredcoda.currencyconverter.data

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.inspiredcoda.currencyconverter.data.converter.RateConverter
import com.inspiredcoda.currencyconverter.data.dao.MultipleRatesDao
import com.inspiredcoda.currencyconverter.data.dao.SingleRateDao
import com.inspiredcoda.currencyconverter.data.entity.Rates
import com.inspiredcoda.currencyconverter.data.model.HistoryResponse.HistoryResponse

@Database(
    entities = [Rates::class, HistoryResponse::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(RateConverter::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun getSingleRateDao(): SingleRateDao
    abstract fun getMultipleRatesDao(): MultipleRatesDao

}