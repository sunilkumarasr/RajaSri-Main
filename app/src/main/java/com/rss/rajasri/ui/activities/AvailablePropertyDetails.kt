package com.rss.rajasri.ui.activities

import android.R.attr
import android.app.DownloadManager
import android.app.DownloadManager.Request
import android.content.Intent
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.text.Html
import android.util.DisplayMetrics
import android.util.Log
import android.view.View
import android.webkit.WebSettings
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import com.bumptech.glide.Glide
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivityAvailablePropertyDetailsBinding
import com.rss.rajasri.datamodels.response.CityPropertyDetails
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.GalleryAdapter
import com.rss.rajasri.ui.plots.AvailablePlotListScreen
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.PROPERTY_ID
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AvailablePropertyDetails : AppCompatActivity() {
    lateinit var binding: ActivityAvailablePropertyDetailsBinding
    var brochure_path=""
    lateinit var galleryAdapter: GalleryAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RajaSriApp.setLangConfig(baseContext);
        binding= ActivityAvailablePropertyDetailsBinding.inflate(layoutInflater)

        //setContentView(R.layout.activity_available_property_details)
        setContentView(binding.root)
        callPropertiesApi()
        binding.backButtonIV.setOnClickListener {
            finish()
        }
        galleryAdapter= GalleryAdapter(supportFragmentManager)
        binding.recyclerGallery.layoutManager=GridLayoutManager(applicationContext,3)
        binding.recyclerGallery.adapter=galleryAdapter
        val property_id = intent?.getStringExtra(PROPERTY_ID)?:""
        binding.btnBroucher.setOnClickListener {
            if(brochure_path!=null&&brochure_path.isNotEmpty())
            {

                var intent=Intent(Intent.ACTION_VIEW)
                intent.setDataAndType(Uri.parse(brochure_path), "application/pdf");

                startActivity(intent)
                if(true)
                {
                    return@setOnClickListener
                }

                val downloadManager=getSystemService(DOWNLOAD_SERVICE) as DownloadManager
                val request=Request(Uri.parse(brochure_path))
                    .setAllowedNetworkTypes(DownloadManager.Request.NETWORK_WIFI or DownloadManager.Request.NETWORK_MOBILE)
                .setAllowedOverRoaming(false)
                .setTitle("Downloading")
                Log.e("brochure_path","brochure_path $brochure_path")

                val enque=downloadManager.enqueue(request)
                Log.e("brochure_path","brochure_path download status $enque")

                return@setOnClickListener
            }
            showToastMsg(resources.getString(R.string.brocher_not_attached))
        }
        binding.btnAvailability.setOnClickListener {
            val intent=Intent(applicationContext,AvailablePlotListScreen::class.java)
            intent.putExtra("property_id",property_id)
            startActivity(intent)
        }

    }
    private fun callPropertiesApi(){
        if(!isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        val id = intent?.getStringExtra(PROPERTY_ID)?:""

        jsonObject.put("city_property_id", id)
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.cityPropertyDetails(body)
        apiCall.enqueue(object : Callback<CityPropertyDetails> {
            override fun onResponse(call: Call<CityPropertyDetails>, response: Response<CityPropertyDetails>) {
               Log.e("data","data ${response.body()}")
                if(response.body()?.data?.cityProperties?.size!! >0) {
                    val cityPropertyDetails = response.body()?.data?.cityProperties?.get(0)

                    binding.txtLocation.setText("${cityPropertyDetails?.location}")

                    brochure_path=cityPropertyDetails?.brochure_full_path!!
                    if(cityPropertyDetails?.images?.size!! >0)
                    {
                        Glide.with(binding.imgProperty)
                            .load(cityPropertyDetails?.images?.get(0)?.imgFullPath)
                            .placeholder(R.drawable.venture_image_png)
                            .error(R.drawable.venture_image_png)
                            .into(binding.imgProperty)

                        galleryAdapter.listImages= cityPropertyDetails?.images!!
                        galleryAdapter.notifyDataSetChanged()
                    }
                    if(cityPropertyDetails.g_map_embed_url==null&&cityPropertyDetails.g_map_embed_url!!.isEmpty())
                    {
                        binding.lnrLocation.visibility=View.GONE

                    }else
                    {
                        binding.lnrLocation.visibility=View.VISIBLE
                        binding.webviewLocation.getSettings().setAllowFileAccess(true)
                        binding.webviewLocation.getSettings().setPluginState(WebSettings.PluginState.ON)
                        binding.webviewLocation.getSettings().setPluginState(WebSettings.PluginState.ON_DEMAND)
                        binding.webviewLocation.setWebViewClient(WebViewClient())
                        binding.webviewLocation.getSettings().setJavaScriptEnabled(true)
                        binding.webviewLocation.getSettings().setLoadWithOverviewMode(true)
                        binding.webviewLocation.getSettings().setUseWideViewPort(true)
                        val displaymetrics = DisplayMetrics()
                        windowManager.defaultDisplay.getMetrics(displaymetrics)
                        val height = displaymetrics.heightPixels
                        val width = displaymetrics.widthPixels

                       /* val data_html =
                            ("<!DOCTYPE html><html> <head> <meta charset=\"UTF-8\"><meta name=\"viewport\" content=\"target-densitydpi=high-dpi\" /> <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\"> <link rel=\"stylesheet\" media=\"screen and (-webkit-device-pixel-ratio:1.5)\" href=\"hdpi.css\" /></head>
                            <body style=\"background:black;margin:0 0 0 0; padding:0 0 0 0;\">
                            <iframe style=\"background:black;\" width=' " + width + "' height='" + height + "' src=\"" + cityPropertyDetails.g_map_embed_url!!).toString() + "\" frameborder=\"0\"></iframe> </body> </html> "

                        binding.webviewLocation.loadData(data_html, "text/html",
                            "utf-8" )*/
                        binding.webviewLocation.loadData("<iframe height=600 width= $width src=\"${cityPropertyDetails.g_map_embed_url!!}\"></iframe>", "text/html",
                            "utf-8" )
                    }

                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                        binding.txtAmenities.setText(Html.fromHtml("${cityPropertyDetails.amenities}", Html.FROM_HTML_MODE_COMPACT));
                        binding.txtFeatures.setText(Html.fromHtml("${cityPropertyDetails.description}", Html.FROM_HTML_MODE_COMPACT));
                        binding.txtTerms.setText(Html.fromHtml("${cityPropertyDetails.termsAndConditions}", Html.FROM_HTML_MODE_COMPACT));
                    } else {
                        binding.txtAmenities.setText(Html.fromHtml("${cityPropertyDetails.amenities}"));
                        binding.txtFeatures.setText(Html.fromHtml("${cityPropertyDetails.description}"));
                        binding.txtTerms.setText(Html.fromHtml("${cityPropertyDetails.termsAndConditions}"));
                    }

                    if(cityPropertyDetails?.videos?.size!! >0)
                    {
                        binding.lnrVideo.visibility= View.VISIBLE
                        Glide.with(binding.imgVideo)
                            .load(cityPropertyDetails?.videos?.get(0)?.video)
                            .thumbnail(0.1f)
                            .into(binding.imgVideo)
                        binding.imgVideo.setOnClickListener {
                            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(cityPropertyDetails?.videos?.get(0)?.video))
                            startActivity(intent)
                        }

                    }else
                    {
                        binding.cardVideo.visibility= View.GONE
                    }


                }else
                {
                    showToastMsg("${response.body()?.message}")

                    finish()
                }
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<CityPropertyDetails>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                finish()
                CommonMethods.hideProgress()
            }

        })
    }
}