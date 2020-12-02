package com.inspiredcoda.currencyconverter.data.model.HistoryResponse


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.inspiredcoda.currencyconverter.data.entity.Rates

@Entity(tableName = "history_response")
data class HistoryResponse (

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int? = null,

    @ColumnInfo(name = "base")
    @SerializedName("base")
    var base: String? = null,

    @ColumnInfo(name = "date")
    @SerializedName("date")
    var date: String? = null,

    @ColumnInfo(name = "rates")
    @SerializedName("rates")
    var rates: Rates? = null,

    @Ignore
    @SerializedName("success")
    val success: Boolean? = null,

)