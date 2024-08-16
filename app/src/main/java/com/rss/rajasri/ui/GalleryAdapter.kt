package com.rss.rajasri.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.royalit.sudhakarpastor.fragment.ImageViewDialog
import com.rss.rajasri.R
import com.rss.rajasri.datamodels.response.Images

class GalleryAdapter(val childFragmentManager:FragmentManager): RecyclerView.Adapter<GalleryAdapter.GalleryViewHolder>() {

    lateinit var listImages:ArrayList<Images>
    init {

        listImages= ArrayList()
    }
    class GalleryViewHolder (view: View):RecyclerView.ViewHolder(view){
        val img_property=view.findViewById<ImageView>(R.id.img_property)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GalleryViewHolder {

        return GalleryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.layout_gallery_item,parent,false))
    }

    override fun getItemCount(): Int {
        return listImages.size
    }

    override fun onBindViewHolder(holder: GalleryViewHolder, position: Int) {
        Glide.with(holder.img_property)
            .load(listImages.get(position).imgFullPath)
            .placeholder(R.drawable.venture_image_png)
            .into(holder.img_property)

        holder.img_property.setOnClickListener {



            val path=listImages.get(position).imgFullPath;
            if (path != null && path.isNotEmpty()) {

                val imageViewDialog=  ImageViewDialog(path)
                imageViewDialog.show(childFragmentManager,"fragmentContext")

            }
        }
    }
}