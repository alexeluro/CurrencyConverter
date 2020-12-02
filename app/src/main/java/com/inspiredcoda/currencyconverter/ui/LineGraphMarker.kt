package com.inspiredcoda.currencyconverter.ui

import android.content.Context
import com.github.mikephil.charting.components.MarkerView
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.highlight.Highlight
import com.github.mikephil.charting.utils.MPPointF
import com.inspiredcoda.currencyconverter.data.model.HistoryResponse.HistoryResponse
import kotlinx.android.synthetic.main.marker_layout.view.*
import java.math.RoundingMode
import java.text.DecimalFormat
import java.text.SimpleDateFormat
import java.util.*

class LineGraphMarker(
    private var historyResponse: MutableList<HistoryResponse>,
    con: Context,
    layoutId: Int
): MarkerView(con, layoutId){

    override fun getOffset(): MPPointF {
        return MPPointF(02f, -height.toFloat())
    }

    override fun refreshContent(e: Entry?, highlight: Highlight?) {
        super.refreshContent(e, highlight)

        if(e == null){
            return
        }

        val currentPosition = e.x.toInt()
        val currentRecord = historyResponse[currentPosition]


        marker_date.text = currentRecord.date?.substring(5)

        val decimalFormat = DecimalFormat("#.##")
        decimalFormat.roundingMode = RoundingMode.FLOOR
        val exchangeText = "1 EUR -> ${decimalFormat.format(currentRecord.rates?.nGN).toDouble()} NGN"
        marker_exchange_rate.text = exchangeText
    }
}