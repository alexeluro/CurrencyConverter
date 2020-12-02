package com.inspiredcoda.currencyconverter.data.dao

import androidx.room.*
import com.inspiredcoda.currencyconverter.data.entity.Rates
import com.inspiredcoda.currencyconverter.data.model.HistoryResponse.HistoryResponse

@Dao
interface MultipleRatesDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRates(rates: HistoryResponse)

    @Query("SELECT * FROM history_response ORDER BY id DESC")
    suspend fun getRecordsFrom30Days(): List<HistoryResponse>

    @Query("DELETE FROM history_response")
    suspend fun clearRecords()

}