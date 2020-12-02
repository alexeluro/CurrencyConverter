package com.inspiredcoda.currencyconverter.ui

import android.app.Activity
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.BaseAdapter
import android.widget.ImageView
import android.widget.TextView
import androidx.annotation.NonNull
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.TransformationUtils
import com.inspiredcoda.currencyconverter.R
import com.inspiredcoda.currencyconverter.data.model.ConverterRate
import de.hdodenhof.circleimageview.CircleImageView

class SpinnerAdapter(
    private val context: Activity,
    private val countriesAndRates: MutableList<ConverterRate>
) : ArrayAdapter<ConverterRate>(context, 0, countriesAndRates) {


    private val layoutInflater =
        context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater


    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null){
            val root = layoutInflater.inflate(R.layout.custom_spinner_layout, parent, false)
            return initRootView(position, root, parent)
        }
        return initRootView(position, convertView, parent)
    }

    override fun getDropDownView(position: Int, convertView: View?, parent: ViewGroup): View {
        if (convertView == null){
            val root = layoutInflater.inflate(R.layout.custom_spinner_layout, parent, false)
            return initRootView(position, root, parent)
        }
        return initRootView(position, convertView, parent)
    }

    private fun initRootView(position: Int, convertView: View?, parent: ViewGroup): View {
        val viewHolder =
            AdapterViewHolder(convertView ?: layoutInflater.inflate(R.layout.custom_spinner_layout, parent, false))
        val item = getItem(position)
        viewHolder.currencyCode.text = item?.name
        Glide.with(getContext())
            .load(item?.countryFlag)
            .circleCrop()
            .into(viewHolder.countryFlag)
        return convertView!!
    }


    class AdapterViewHolder(root: View) : RecyclerView.ViewHolder(root) {
        val currencyCode: TextView = root.findViewById(R.id.country_exchange_rate_text)
        val countryFlag: ImageView = root.findViewById(R.id.country_flags)
    }
}