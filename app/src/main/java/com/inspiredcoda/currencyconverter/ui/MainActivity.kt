package com.inspiredcoda.currencyconverter.ui

import android.animation.ValueAnimator
import android.app.ProgressDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.animation.AnimationUtils
import android.widget.AdapterView
import androidx.activity.viewModels
import androidx.core.content.ContextCompat
import com.github.mikephil.charting.animation.ChartAnimator
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.inspiredcoda.currencyconverter.R
import com.inspiredcoda.currencyconverter.data.model.ConverterRate
import com.inspiredcoda.currencyconverter.data.entity.Rates
import com.inspiredcoda.currencyconverter.data.model.HistoryResponse.HistoryResponse
import com.inspiredcoda.currencyconverter.util.Constants
import com.inspiredcoda.currencyconverter.util.toast
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.android.synthetic.main.activity_main.*

@AndroidEntryPoint
class MainActivity : AppCompatActivity(), View.OnClickListener, ProgressStateListener {

    private val TAG = "MainActivity"

    private val mainActivityViewModel by viewModels<MainActivityViewModel>()

//    private val mainActivityViewModel by lazy {
//        ViewModelProvider(this).get(MainActivityViewModel::class.java)
//    }
    private lateinit var countries: MutableList<ConverterRate>
//    private lateinit var conversionRates: MutableList<Rates>
    private var converterFromPos = 0
    private var converterToPos = 0

    private var progressBar: ProgressDialog? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        progressBar = ProgressDialog(this)
        mainActivityViewModel.viewModelPrep(this)

        setUpLineChart()

        mainActivityViewModel.liveRate.observe(this) { rates ->
            if (rates != null) {
                countries = mutableListOf()
                countries.add(ConverterRate(Constants.Nations.AUD, rates.aUD, R.drawable.aud_flag))
                countries.add(ConverterRate(Constants.Nations.CAD, rates.cAD, R.drawable.cad_flag))
                countries.add(ConverterRate(Constants.Nations.MXN, rates.mXN, R.drawable.mxn_flag))
                countries.add(ConverterRate(Constants.Nations.NGN, rates.nGN, R.drawable.ngn_flag))
                countries.add(ConverterRate(Constants.Nations.PLN, rates.pLN, R.drawable.pln_flag))
                countries.add(ConverterRate(Constants.Nations.USD, rates.uSD, R.drawable.usd_flag))
                populateSpinnerFrom(countries)
                populateSpinnerTo(countries)

            }
        }

        mainActivityViewModel.liveRateRecordsForSomeDays.observe(this){
            onLoading()
            if (it != null){
                Log.d(TAG, "onCreate: Size of records saved in the database ${it.size}")
                updateMessage("plotting data on the graph...")
                initLineChart(it)
            }
            onSuccess()
        }

        convert_to_edit_text.isEnabled = false

        convert_btn.setOnClickListener(this)
    }

    private fun setUpLineChart(){

        currency_chart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            setDrawLabels(true)
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        currency_chart.axisLeft.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }

        currency_chart.axisRight.apply {
            axisLineColor = Color.WHITE
            textColor = Color.WHITE
            setDrawGridLines(false)
        }


        currency_chart.apply {
            this.setDrawBorders(false)
            setDrawGridBackground(false)
            setNoDataTextColor(Color.WHITE)
            description.isEnabled = true
            description.textColor = Color.WHITE
            description.text = "Naira against Exchange Rates with Euros"

            legend.isEnabled = true
            legend.textColor = Color.WHITE
        }

    }

    private fun initLineChart(conversionRates: List<HistoryResponse>) {

        conversionRates.let { rates ->

            val lineDataSet =
                LineDataSet(
                    rates.indices.map { i ->
                        Entry(i.toFloat(), rates[i].rates?.nGN?.toFloat()!!) },
                    "Exchange Rates"
                ).apply {
                    valueTextColor = Color.WHITE
//                    fillColor = Color.BLACK
                    color = ContextCompat.getColor(this@MainActivity, R.color.colorBlueSky)
                    mode = LineDataSet.Mode.CUBIC_BEZIER
                    setDrawFilled(true)
                    fillAlpha = 255
                }
            currency_chart.data = LineData(lineDataSet).also { it.setDrawValues(false) }
            currency_chart.marker = LineGraphMarker(conversionRates.toMutableList(), this, R.layout.marker_layout)

            currency_chart.animation = AnimationUtils.loadAnimation(this, R.anim.fade_in)
            currency_chart.invalidate()


        }
    }




    private fun populateSpinnerFrom(countries: MutableList<ConverterRate>) {
        convert_from_spinner.adapter = SpinnerAdapter(this, countries)

        convert_from_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                rootView: View?,
                position: Int,
                p3: Long
            ) {
                convert_from_text.text = countries[position].name
                converterFromPos = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {
                TODO("Not yet implemented")
            }

        }

    }

    private fun populateSpinnerTo(countries: MutableList<ConverterRate>) {
        convert_to_spinner.adapter = SpinnerAdapter(this, countries)

        convert_to_spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                p0: AdapterView<*>?,
                rootView: View?,
                position: Int,
                p3: Long
            ) {
                convert_to_text.text = countries[position].name
                converterToPos = position
            }

            override fun onNothingSelected(p0: AdapterView<*>?) {

            }

        }
    }

    override fun onClick(item: View?) {
        if (item?.id == R.id.convert_btn) {
            if (convert_from_edit_text.text.toString().isEmpty()) {
                toast("Input an amount in your desired currency")
                return
            }
            val result = mainActivityViewModel.convertAmount(
                countries.map { it.rate },
                converterFromPos,
                converterToPos,
                convert_from_edit_text.text.toString().toDouble()
            )
            convert_to_edit_text.setText(result.toString())
        }
    }

    override fun onLoading() {
        progressBar?.setCancelable(false)
        progressBar?.setMessage("Loading exchange rate data...")
        progressBar?.show()
    }

    override fun updateMessage(message: String) {
        progressBar?.setMessage(message)
    }

    override fun onFailure(message: String) {
        progressBar?.dismiss()
        toast(message)
    }

    override fun onSuccess() {
        progressBar?.dismiss()
    }

}