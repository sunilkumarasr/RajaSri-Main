package com.rss.rajasri.ui.fragments.home

import android.app.Activity
import android.app.DownloadManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Context.RECEIVER_NOT_EXPORTED
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Paint
import android.net.Uri
import android.os.Environment
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.rss.rajasri.R
import com.rss.rajasri.databinding.SearchLayoutItemBinding
import com.rss.rajasri.datamodels.response.AllProperties
import com.rss.rajasri.utils.Adapter_Details
import com.rss.rajasri.utils.Adapter_Loan
import com.rss.rajasri.utils.showToastMsg


class SearchPropertiesAdapter(val activity: Activity) :
    RecyclerView.Adapter<SearchPropertiesAdapter.ViewHolder>() {
    var allProperties: ArrayList<AllProperties> = arrayListOf()
    var clickListener: ((item: AllProperties, type: String) -> Unit)? = null
    fun setData(allProperties: ArrayList<AllProperties>) {
        this.allProperties = allProperties
        notifyDataSetChanged()
    }

    fun setOnClickListener(clickListener: ((item: AllProperties, type: String) -> Unit)?) {
        this.clickListener = clickListener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): SearchPropertiesAdapter.ViewHolder {
        val binding =
            SearchLayoutItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchPropertiesAdapter.ViewHolder, position: Int) {
        holder.bindData(allProperties[position])
    }

    override fun getItemCount(): Int = allProperties.size

    inner class ViewHolder(val binding: SearchLayoutItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bindData(item: AllProperties) {
            Glide.with(binding.imageIV)
                .load(item.image)
                .placeholder(R.drawable.venture_image_png)
                .into(binding.imageIV)
            Log.e("Property Name", "Property Name ${item.name}")
            binding.titleTV.text = item.name
            binding.locationTV.text = item.location
            binding.areaTV.text = "${item.area} (${item.area_name})"
            binding.txtPrice.setText("${binding.viewDetailsBt.context.resources.getString(R.string.price)} : ₹ ${item.price} ")
            binding.txtViewDoc.setPaintFlags(binding.txtViewDoc.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG);

            binding.viewDetailsBt.setOnClickListener {
                clickListener?.invoke(item, Adapter_Details)
            }
            binding.loanDetailsBt.setOnClickListener {
                clickListener?.invoke(item, Adapter_Loan)
            }
            binding.txtViewDoc.setOnClickListener {

                //item.agreement_file_full_path="https://www.cole13.com/wp-content/uploads/2023/06/The-Richest-Man-In-Babylon-George-S.-Clason-Z-Library.pdf"
                if (item.agreement_file_full_path == null || item.agreement_file_full_path!!.isEmpty() || item.agreement_file_full_path == "null") {
                    binding.txtViewDoc.context.showToastMsg(activity.resources.getString(R.string.agreement_not_attached))
                    return@setOnClickListener
                }

                val url = Uri.parse(item.agreement_file_full_path)
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
                    RECEIVER_NOT_EXPORTED
                )
                binding.txtViewDoc.context.startActivity(
                    Intent(Intent.ACTION_VIEW,Uri.parse(item.agreement_file_full_path.toString()))
                )


            }

        }

    }

}