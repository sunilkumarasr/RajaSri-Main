package com.rss.rajasri.ui.plots

import android.content.Intent
import android.content.res.Configuration
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.AdapterView.OnItemSelectedListener
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivityAvailablePlotListScreenBinding
import com.rss.rajasri.datamodels.response.Plot
import com.rss.rajasri.datamodels.response.PlotResponse
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.Locale


class AvailablePlotListScreen : AppCompatActivity() {
    lateinit var binding:ActivityAvailablePlotListScreenBinding
    lateinit var adapter: AvailablePlotsAdapter
    var property_id=""
    var land_face=""
    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        RajaSriApp.setLangConfig(baseContext);

        binding=ActivityAvailablePlotListScreenBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val faingArray=resources.getStringArray(R.array.array_plot_types)
        val faingArray_=resources.getStringArray(R.array.array_plot_types_)
        property_id= intent.getStringExtra("property_id").toString()
        adapter= AvailablePlotsAdapter(this@AvailablePlotListScreen)
        binding.recyclerPlots.adapter=adapter
        binding.backButtonIV.setOnClickListener {
            finish()
        }

        binding.spinnerPlotTypes.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>,
                view: View,
                position: Int,
                id: Long
            ) {
            land_face=faingArray_[position];
                land_face=  land_face.replace(" Corner","C")
                getPlots()

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {

                // sometimes you need nothing here
            }
        };
    }

    fun showOpenDetails(plot: Plot)
    {
        val builder= AlertDialog.Builder(this@AvailablePlotListScreen);
        val  language=RajaSriApp.appPreference.getLanguage()

        val locale = Locale(language)
        Locale.setDefault(locale)
        var config= Configuration()
        config.locale=locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)

        val view=LayoutInflater.from(applicationContext).inflate(R.layout.layout_plot_status_available_alert,null)
        val lnr_enquiry=view.findViewById<View>(R.id.lnr_enquiry)
        val txt_plot_no=view.findViewById<TextView>(R.id.txt_plot_no)
        val txt_plot_facing=view.findViewById<TextView>(R.id.txt_plot_facing)
        val txt_uds=view.findViewById<TextView>(R.id.txt_uds)
        val txt_plot_flat_area=view.findViewById<TextView>(R.id.txt_plot_flat_area)
        val txt_plot_status=view.findViewById<TextView>(R.id.txt_plot_status)
        val txt_enquiry=view.findViewById<TextView>(R.id.txt_enquiry)

        val txt_p_status=view.findViewById<TextView>(R.id.txt_p_status)
        val txt_p_area=view.findViewById<TextView>(R.id.txt_p_area)
        val txt_p_uds=view.findViewById<TextView>(R.id.txt_p_uds)
        val txt_p_facing=view.findViewById<TextView>(R.id.txt_p_facing)
        val plot_no=view.findViewById<TextView>(R.id.txt_p_no)

        txt_p_status.setText("${resources.getString(R.string.status)}")
        txt_p_area.setText("${resources.getString(R.string.plot_flat_area)}")
        txt_p_uds.setText("${resources.getString(R.string.plot_flat_uds)}")
        txt_p_facing.setText("${resources.getString(R.string.plot_facing)}")
        plot_no.setText("${resources.getString(R.string.plot_no)}")

        txt_plot_no.setText("${plot.plotNum}")
        txt_plot_facing.setText("${plot.landFace}")
        txt_uds.setText("${plot.undividedShareArea}")
        txt_plot_flat_area.setText("${plot.plotArea}")
        txt_enquiry.setText("${resources.getString(R.string.enquiery_now)}")
        if(plot.status=="1")
        txt_plot_status.setText(resources.getString(R.string.availbelable))
        else
        txt_plot_status.setText(resources.getString(R.string.booked))
        lnr_enquiry.setOnClickListener {
            dialog.dismiss()

            startActivity(Intent(applicationContext,EnquiryFromScreen::class.java))
        }
        builder.setView(view)
        dialog=builder.create()
        dialog.show()



    }

    lateinit var dialog:AlertDialog;
    fun showBookedDetails(plot: Plot)
    {
        val builder= AlertDialog.Builder(this@AvailablePlotListScreen);
        val  language=RajaSriApp.appPreference.getLanguage()

        val locale = Locale(language)
        Locale.setDefault(locale)
        var config= Configuration()
        config.locale=locale
        baseContext.resources.updateConfiguration(config,baseContext.resources.displayMetrics)

        val view=LayoutInflater.from(applicationContext).inflate(R.layout.layout_plot_status_alert,null)
        val txt_booking_id=view.findViewById<TextView>(R.id.txt_booking_id)
        val txt_customer_name=view.findViewById<TextView>(R.id.txt_customer_name)
        val txt_customer_mobile=view.findViewById<TextView>(R.id.txt_customer_mobile)
        val txt_plot_no=view.findViewById<TextView>(R.id.txt_plot_no)
        val txt_plot_facing=view.findViewById<TextView>(R.id.txt_plot_facing)
        val txt_uds=view.findViewById<TextView>(R.id.txt_uds)
        val txt_plot_flat_area=view.findViewById<TextView>(R.id.txt_plot_flat_area)
        val txt_plot_status=view.findViewById<TextView>(R.id.txt_plot_status)


        val txt_p_status=view.findViewById<TextView>(R.id.txt_p_status)
        val txt_p_area=view.findViewById<TextView>(R.id.txt_p_area)
        val txt_p_uds=view.findViewById<TextView>(R.id.txt_p_uds)
        val txt_p_facing=view.findViewById<TextView>(R.id.txt_p_facing)
        val plot_no=view.findViewById<TextView>(R.id.txt_p_no)

        txt_p_status.setText("${resources.getString(R.string.status)}")
        txt_p_area.setText("${resources.getString(R.string.plot_flat_area)}")
        txt_p_uds.setText("${resources.getString(R.string.plot_flat_uds)}")
        txt_p_facing.setText("${resources.getString(R.string.plot_facing)}")
        plot_no.setText("${resources.getString(R.string.plot_no)}")


        txt_plot_no.setText("${plot.plotNum}")
        txt_plot_facing.setText("${plot.landFace}")
        txt_uds.setText("${plot.undividedShareArea}")
        txt_plot_flat_area.setText("${plot.plotArea}")
        if(plot.status=="1")
            txt_plot_status.setText("Available")
        else
            txt_plot_status.setText("Booked")
        builder.setView(view)
        builder.show()


    }

    private fun getPlots() {
        if(!this@AvailablePlotListScreen.isInternetAvailable()){
            this@AvailablePlotListScreen.showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        checkUI()
        CommonMethods.showProgress(this@AvailablePlotListScreen)

        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("user_id", RajaSriApp.appPreference.getCustomerID())
        jsonObject.put("property_id", property_id)
        jsonObject.put("land_face", land_face)
        Log.e("user_id_", RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.getPlots(body)
        apiCall.enqueue(object : Callback<PlotResponse> {
            override fun onResponse(call: Call<PlotResponse>, response: Response<PlotResponse>) {
                CommonMethods.hideProgress()

                Log.e("Response", "Response success ${response.code()}")
                response.body()!!.data?.let { adapter.setPlotList(it) }

                checkUI()
                //response.code()

            }

            override fun onFailure(call: Call<PlotResponse>, t: Throwable) {
                Log.e("Response", "Response error pending ${t.message}")
                CommonMethods.hideProgress()
                checkUI()
            }

        })
    }

    private fun checkUI() {
        if(adapter.list.size>0)
            binding.txtNoData.visibility=View.GONE
        else
            binding.txtNoData.visibility=View.VISIBLE
    }
}