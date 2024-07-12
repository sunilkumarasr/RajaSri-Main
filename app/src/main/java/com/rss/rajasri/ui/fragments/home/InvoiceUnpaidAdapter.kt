package com.rss.rajasri.ui.fragments.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.rss.rajasri.R
import com.rss.rajasri.databinding.InvoiceunpaidItemsListBinding
import com.rss.rajasri.datamodels.response.ListofInvoice
import com.rss.rajasri.utils.Adapter_Details

class InvoiceUnpaidAdapter: RecyclerView.Adapter<InvoiceUnpaidAdapter.ViewHolder>() {

    var allProperties : ArrayList<ListofInvoice> = arrayListOf()
    var clickListener : ((item: ListofInvoice, type:String)->Unit)? = null
    fun setData(allProperties: MutableList<ListofInvoice>){
        this.allProperties = allProperties as ArrayList<ListofInvoice>
        notifyDataSetChanged()
    }
    fun setOnClickListener(clickListener : ((item: ListofInvoice, type:String)->Unit)?){
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): InvoiceUnpaidAdapter.ViewHolder {
        val binding = InvoiceunpaidItemsListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ViewHolder(binding)
    }


    override fun onBindViewHolder(holder: InvoiceUnpaidAdapter.ViewHolder, position: Int) {

        if (allProperties[position].gatewayStatus.equals("0")){
            holder.binding.txtSNo.setText("${position+1}")
            holder.bindData(allProperties[position])
        }

    }


    override fun getItemCount(): Int = allProperties.size

    inner class ViewHolder(val binding:InvoiceunpaidItemsListBinding):RecyclerView.ViewHolder(binding.root){
        fun bindData(item:ListofInvoice){

            binding.statusTV.text = binding.statusTV.context.resources.getString(R.string.pay)
            binding.emiDateTV.text = item.invoiceDate
            binding.emiPriceTV.text = "₹ ${item.invoiceAmount}/-"
            binding.ventureNameTV.text = item.propertyName


            binding.relativeUnpaid.setOnClickListener {
                clickListener?.invoke(item, Adapter_Details)
            }


        }

    }

}