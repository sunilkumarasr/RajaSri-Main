package com.rss.rajasri.ui.fragments.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rss.rajasri.R
import com.rss.rajasri.databinding.CityVentureLayoutBinding
import com.rss.rajasri.databinding.HomeVentureLayoutBinding
import com.rss.rajasri.databinding.SearchLayoutItemBinding
import com.rss.rajasri.datamodels.response.AllProperties
import com.rss.rajasri.datamodels.response.CityProperty
import com.rss.rajasri.utils.Adapter_Details
import com.rss.rajasri.utils.Adapter_Loan


class CityPropertyAdapter:RecyclerView.Adapter<CityPropertyAdapter.ViewHolder>() {
    var allProperties : ArrayList<CityProperty> = arrayListOf()
    var clickListener : ((item:CityProperty,type:String)->Unit)? = null
    fun setData(allProperties : ArrayList<CityProperty>){
        this.allProperties = allProperties
        notifyDataSetChanged()
    }
    fun setOnClickListener(clickListener : ((item:CityProperty,type:String)->Unit)?){
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CityPropertyAdapter.ViewHolder {
       val binding = CityVentureLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CityPropertyAdapter.ViewHolder, position: Int) {
        holder.bindData(allProperties[position])
    }

    override fun getItemCount(): Int = allProperties.size

    inner class ViewHolder(val binding:CityVentureLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bindData(item:CityProperty){
            Glide.with(binding.bannerIV)
                .load("")
                .placeholder(R.drawable.venture_image_png)
                .into(binding.bannerIV)
            binding.titleTV.text = item.name
            binding.addressTV.text = item.location
            binding.yardsTV.text ="${binding.titleTV.context.resources.getString(R.string.plot_no)}. ${item.plotNumber}"


            binding.viewDetailsTV.setOnClickListener {
                clickListener?.invoke(item, Adapter_Details)
            }


        }

    }

}