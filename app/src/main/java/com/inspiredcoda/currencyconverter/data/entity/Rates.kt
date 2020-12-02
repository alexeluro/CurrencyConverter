package com.inspiredcoda.currencyconverter.data.entity


import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity(tableName = "rates")
data class Rates(

    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id")
    var id: Int,

    @ColumnInfo(name = "AUD")
    @SerializedName("AUD")
    val aUD: Double,

    @ColumnInfo(name = "CAD")
    @SerializedName("CAD")
    val cAD: Double,

    @ColumnInfo(name = "MXN")
    @SerializedName("MXN")
    val mXN: Double,

    @ColumnInfo(name = "NGN")
    @SerializedName("NGN")
    val nGN: Double,

    @ColumnInfo(name = "PLN")
    @SerializedName("PLN")
    val pLN: Double,

    @ColumnInfo(name = "USD")
    @SerializedName("USD")
    val uSD: Double
)