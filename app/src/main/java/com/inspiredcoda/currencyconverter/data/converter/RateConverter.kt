package com.inspiredcoda.currencyconverter.data.converter

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import com.google.gson.reflect.TypeToken
import com.inspiredcoda.currencyconverter.data.entity.Rates

class RateConverter {

    @TypeConverter
    fun convertRatesToJson(rates: Rates?): String{
        return GsonBuilder().create().toJson(rates)
    }

    @TypeConverter
    fun convertJsonToRates(rateString: String?): Rates?{
        val type = object : TypeToken<Rates>() {}.type
        return Gson().fromJson<Rates>(rateString, type)
    }


}