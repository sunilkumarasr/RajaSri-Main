package com.rss.rajasri.ui.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rss.rajasri.databinding.InvoiceunpaidItemsListBinding
import com.rss.rajasri.databinding.NotificationsItemsListBinding
import com.rss.rajasri.datamodels.response.ListofNotifications
import com.rss.rajasri.utils.Adapter_Details

class NotificationsAdapter: RecyclerView.Adapter<NotificationsAdapter.ViewHolder>() {

    var allNotifications : ArrayList<ListofNotifications> = arrayListOf()
    var clickListener : ((item: ListofNotifications)->Unit)? = null

    //activity to adapter
    fun setData(allNotifications : ArrayList<ListofNotifications>){
        this.allNotifications = allNotifications
        notifyDataSetChanged()
    }

    //onclick
    fun setOnClickListener(clickListener : ((item: ListofNotifications)->Unit)?){
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder( parent: ViewGroup, viewType: Int,): NotificationsAdapter.ViewHolder {
        val binding = NotificationsItemsListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: NotificationsAdapter.ViewHolder, position: Int) {
        holder.bindData(allNotifications[position])
    }


    override fun getItemCount(): Int = allNotifications.size

    inner class ViewHolder(val binding:NotificationsItemsListBinding):RecyclerView.ViewHolder(binding.root){
        fun bindData(item:ListofNotifications){

            binding.txtTitle.text = item.title
            binding.txtbody.text = item.body
            binding.txtdate.text = item.created_at


            binding.linear.setOnClickListener {
                clickListener?.invoke(item)
            }

        }

    }

}