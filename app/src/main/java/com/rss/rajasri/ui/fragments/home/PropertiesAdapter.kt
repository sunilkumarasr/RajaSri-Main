package com.rss.rajasri.ui.fragments.home

import android.text.Html
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.rss.rajasri.R
import com.rss.rajasri.databinding.HomeVentureLayoutBinding
import com.rss.rajasri.datamodels.response.AllProperties

class PropertiesAdapter:RecyclerView.Adapter<PropertiesAdapter.ViewHolder>() {
    var allProperties : ArrayList<AllProperties> = arrayListOf()
    var clickListener : ((item:AllProperties)->Unit)? = null
    fun setData(allProperties : ArrayList<AllProperties>){
        this.allProperties = allProperties
        notifyDataSetChanged()
    }
    fun setOnClickListener(clickListener : ((item:AllProperties)->Unit)?){
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PropertiesAdapter.ViewHolder {
       val binding = HomeVentureLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PropertiesAdapter.ViewHolder, position: Int) {
        holder.bindData(allProperties[position])
    }

    override fun getItemCount(): Int = allProperties.size

    inner class ViewHolder(val binding:HomeVentureLayoutBinding):RecyclerView.ViewHolder(binding.root){
        fun bindData(item:AllProperties){
            Glide.with(binding.bannerIV)
                .load(item.image)
                .placeholder(R.drawable.venture_image_png)
                .into(binding.bannerIV)
            binding.titleTV.text = item.name
            binding.addressTV.text = item.location
            if (item.area_name!=null){
                binding.yardsTV.text ="${item.area} (${item.area_name}s)"
            }else{
                binding.yardsTV.text ="${item.area}"
            }
            binding.viewDetailsTV.setOnClickListener {
                clickListener?.invoke(item)
            }

        }
    }

}