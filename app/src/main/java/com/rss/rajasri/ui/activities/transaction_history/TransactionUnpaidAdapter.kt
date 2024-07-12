package com.rss.rajasri.ui.activities.transaction_history

import android.app.Activity
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rss.rajasri.R
import com.rss.rajasri.databinding.TransactionsAdapterUnpaidListBinding
import com.rss.rajasri.datamodels.response.EmiDetails

class TransactionUnpaidAdapter(val activity: Activity): RecyclerView.Adapter<TransactionUnpaidAdapter.ViewHolder>() {


    var allProperties : ArrayList<EmiDetails> = arrayListOf()
    var clickListener : ((item: EmiDetails)->Unit)? = null
    fun setData(list : ArrayList<EmiDetails>){
        this.allProperties = list
        notifyDataSetChanged()
    }
    fun setOnClickListener(clickListener : ((item: EmiDetails)->Unit)?){
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup,viewType: Int,): TransactionUnpaidAdapter.ViewHolder {
        val binding = TransactionsAdapterUnpaidListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: TransactionUnpaidAdapter.ViewHolder, position: Int) {
        holder.binding.txtSNo.setText("${position+1}")
        holder.bindData(allProperties[position])
    }

    override fun getItemCount(): Int = allProperties.size

    inner class ViewHolder(val binding: TransactionsAdapterUnpaidListBinding): RecyclerView.ViewHolder(binding.root){
        fun bindData(item: EmiDetails){

            binding.statusTV.text = binding.statusTV.context.resources.getString(R.string.pay)
            binding.emiDateTV.text = item.emiDate
            binding.emiPriceTV.text = "₹ ${item.emi_amount}/-"
            binding.ventureNameTV.text = item.property_name?:"Greater Global"


            binding.relativeUnpaid.setOnClickListener {
                clickListener?.invoke(item)
            }


        }

    }

}