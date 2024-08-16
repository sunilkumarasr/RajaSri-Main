package com.rss.rajasri.ui.fragments.home

import android.graphics.Paint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rss.rajasri.R
import com.rss.rajasri.databinding.SearchLayoutItemBinding
import com.rss.rajasri.datamodels.response.AllProperties
import com.rss.rajasri.datamodels.response.CityProperty
import com.rss.rajasri.datamodels.response.PendingProperty
import com.rss.rajasri.utils.Adapter_Details
import com.rss.rajasri.utils.Adapter_Loan


class PendingPropertyAdapter:RecyclerView.Adapter<PendingPropertyAdapter.ViewHolder>() {
    var allProperties : ArrayList<PendingProperty> = arrayListOf()
    var clickListener : ((item:PendingProperty,type:String)->Unit)? = null
    fun setData(allProperties : ArrayList<PendingProperty>){
        this.allProperties = allProperties
        notifyDataSetChanged()
    }
    fun setOnClickListener(clickListener : ((item:PendingProperty,type:String)->Unit)?){
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): PendingPropertyAdapter.ViewHolder {
       val binding = SearchLayoutItemBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PendingPropertyAdapter.ViewHolder, position: Int) {
        holder.bindData(allProperties[position])
    }

    override fun getItemCount(): Int = allProperties.size

    inner class ViewHolder(val binding:SearchLayoutItemBinding):RecyclerView.ViewHolder(binding.root){
        fun bindData(item:PendingProperty){
            Glide.with(binding.imageIV)
                .load(item.image)
                .placeholder(R.drawable.venture_image_png)
                .into(binding.imageIV)
            binding.titleTV.text = item.name
            binding.locationTV.text = item.location
            binding.areaTV.text ="${item.area} (${item.area_name})"
binding.txtPrice.setText("${binding.viewDetailsBt.context.resources.getString(R.string.price)} : ₹ ${item.price} ")
            binding.txtViewDoc.setPaintFlags(binding.txtViewDoc.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG);

            binding.viewDetailsBt.setOnClickListener {
                clickListener?.invoke(item, Adapter_Details)
            }
            binding.viewDetailsBt.setText(binding.viewDetailsBt.context.resources.getString(R.string.SignaturePending))
            binding.txtViewDoc.visibility= View.GONE
            binding.loanDetailsBt.visibility= View.GONE
            binding.loanDetailsBt.setOnClickListener {
                //clickListener?.invoke(item, Adapter_Loan)
            }

        }

    }

}