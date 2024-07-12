package com.rss.rajasri.ui.fragments.home

import android.app.Activity
import android.app.AlertDialog
import android.app.Dialog
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.drawable.ColorDrawable
import android.graphics.pdf.PdfDocument
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.rss.rajasri.R
import com.rss.rajasri.databinding.InvoicepaidItemsListBinding
import com.rss.rajasri.databinding.TransactionsViewDetailsBinding
import com.rss.rajasri.datamodels.response.ListofInvoice
import com.rss.rajasri.utils.Adapter_Details
import com.rss.rajasri.utils.showToastMsg
import java.io.File
import java.io.FileOutputStream
import java.io.IOException
import java.io.InputStream

class InvoicePaidAdapter(val activity: Activity): RecyclerView.Adapter<InvoicePaidAdapter.ViewHolder>() {

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
    ): InvoicePaidAdapter.ViewHolder {
        val binding = InvoicepaidItemsListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return  ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: InvoicePaidAdapter.ViewHolder, position: Int) {

        if (allProperties[position].gatewayStatus.equals("1")){
            holder.binding.txtSNo.setText("${position+1}")
            holder.bindData(allProperties[position])
        }

    }


    override fun getItemCount(): Int = allProperties.size

    inner class ViewHolder(val binding:InvoicepaidItemsListBinding):RecyclerView.ViewHolder(binding.root){
        fun bindData(item:ListofInvoice){

            binding.statusTV.text = binding.statusTV.context.resources.getString(R.string.recipt)
            binding.emiDateTV.text = item.invoiceDate
            binding.emiPriceTV.text = "₹ ${item.invoiceAmount}/-"
            binding.ventureNameTV.text = item.propertyName


//            binding.statusTV.setOnClickListener {
//                clickListener?.invoke(item, Adapter_Details)
//            }

            binding.statusTV.setOnClickListener {
                val layoutInflater=LayoutInflater.from(binding.root.context)
                val dialogBinding  = TransactionsViewDetailsBinding.inflate(layoutInflater,null,false)

                val dialog = Dialog(activity)
                dialog.setContentView(dialogBinding.root)
                dialog.show()
                val att = dialog.window?.attributes
                att?.width = WindowManager.LayoutParams.MATCH_PARENT
                att?.height = WindowManager.LayoutParams.WRAP_CONTENT
                dialog.window?.attributes = att
                dialog.window?.setBackgroundDrawable(ColorDrawable().apply {
                    color = Color.TRANSPARENT
                })

                dialogBinding.imgLogo.visibility= View.VISIBLE
                dialogBinding.txtHeader.text  = dialogBinding.txtHeader.context.resources.getString(R.string.transaction_details)
                dialogBinding.ventureNameTV.text  = item.propertyName?:"Greater Global"
                dialogBinding.fullNameTV.visibility= View.GONE
                dialogBinding.emiPriceTV.text  = "${item.invoiceAmount} /-"
                dialogBinding.emiPaidTV.text  = "${item.invoiceDate}"

                dialogBinding.downloadCV.setOnClickListener {
                    dialog.cancel()

                    if (item.file == null || item.file!!.isEmpty() || item.file == "null") {
                        dialogBinding.downloadCV.context.showToastMsg(activity.resources.getString(R.string.receipt_not_attached))
                        return@setOnClickListener
                    }

                    val url = Uri.parse(item.file)
                    val request = DownloadManager.Request(url)

                    request.setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                    request.setAllowedOverRoaming(false)
                    request.setTitle("PDF File Download")
                    request.setDescription("Downloading " + "pdf" + " file using Download Manager.")
                    request.setVisibleInDownloadsUi(true)
                    request.setDestinationInExternalPublicDir(
                        Environment.DIRECTORY_DOWNLOADS,
                        "pdf_name.pdf"
                    )
                    request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_COMPLETED)
                    val manager =
                        activity.getSystemService(Context.DOWNLOAD_SERVICE) as DownloadManager
                    val refid = manager.enqueue(request)
                    Toast.makeText(activity, "PDF Download Started", Toast.LENGTH_SHORT).show()
                    val onComplete: BroadcastReceiver = object : BroadcastReceiver() {
                        override fun onReceive(ctxt: Context, intent: Intent) {
                            // Do something on download complete
                            Toast.makeText(activity, "Download Completed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    activity.registerReceiver(
                        onComplete,
                        IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE),
                        Context.RECEIVER_NOT_EXPORTED
                    )
                    dialogBinding.downloadCV.context.startActivity(
                        Intent(Intent.ACTION_VIEW, Uri.parse(item.file.toString()))
                    )
                }


                if(item.cityPropertyId==null|| item.cityPropertyId!!.isEmpty())
                    dialogBinding.transactionTV.text  = "No Transaction"
                else
                    dialogBinding.transactionTV.text  = "${item.cityPropertyId}"

            }

        }

    }



}