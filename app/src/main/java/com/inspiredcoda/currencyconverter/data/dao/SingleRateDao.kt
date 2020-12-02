package com.inspiredcoda.currencyconverter.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.inspiredcoda.currencyconverter.data.entity.Rates

@Dao
interface SingleRateDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: Rates)

    @Query("SELECT * FROM rates")
    suspend fun getTodaysRates(): Rates

    @Query("DELETE FROM rates")
    suspend fun clearRecords()


}