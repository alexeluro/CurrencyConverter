package com.inspiredcoda.currencyconverter.data.entity

import androidx.room.ColumnInfo
import androidx.room.Entity

@Entity
data class PastToPresentRates(

    @ColumnInfo(name = "")
    val id: Int,

    @ColumnInfo(name = "date")
    val date: String,

    @ColumnInfo(name = "rates")
    val rates: String
)