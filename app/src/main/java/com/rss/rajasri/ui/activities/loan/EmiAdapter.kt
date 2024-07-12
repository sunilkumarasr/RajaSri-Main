package com.rss.rajasri.ui.activities.loan

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rss.rajasri.R
import com.rss.rajasri.databinding.EmiItemLayoutBinding
import com.rss.rajasri.databinding.HomeVentureLayoutBinding
import com.rss.rajasri.datamodels.response.AllProperties
import com.rss.rajasri.datamodels.response.EmiDetails
import com.rss.rajasri.ui.fragments.home.PropertiesAdapter

class EmiAdapter: RecyclerView.Adapter<EmiAdapter.ViewHolder>() {
    var allProperties : ArrayList<EmiDetails> = arrayListOf()
    var clickListener : ((item: EmiDetails)->Unit)? = null
    fun setData(list : ArrayList<EmiDetails>){
        this.allProperties = list
        notifyDataSetChanged()
    }
    fun setOnClickListener(clickListener : ((item: EmiDetails)->Unit)?){
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): EmiAdapter.ViewHolder {
        val binding = EmiItemLayoutBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: EmiAdapter.ViewHolder, position: Int) {
        holder.bindData(allProperties[position])
    }

    override fun getItemCount(): Int = allProperties.size

    inner class ViewHolder(val binding: EmiItemLayoutBinding): RecyclerView.ViewHolder(binding.root){
        fun bindData(item: EmiDetails){
            binding.snoTV.text = adapterPosition.plus(1).toString()
            binding.dateTV.text = item.emiDate
            binding.priceTV.text = item.emi_amount
            if(item.name?.equals("paid",ignoreCase = true)==true){
                binding.statusTV.text = binding.statusTV.context.resources.getString(R.string.paid)
                binding.statusTV.setTextColor(Color.parseColor("#6C63FF"))
            }else{
                binding.statusTV.text = binding.statusTV.context.resources.getString(R.string.unpaid)
                binding.statusTV.setTextColor(Color.parseColor("#FB4E45"))
            }

            binding.viewDetailsCV.setOnClickListener {
                clickListener?.invoke(item)
            }

        }

    }

}