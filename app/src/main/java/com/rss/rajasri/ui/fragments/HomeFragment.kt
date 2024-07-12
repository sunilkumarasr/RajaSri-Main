package com.rss.rajasri.ui.fragments

import android.content.Intent
import android.graphics.Paint
import android.net.Uri
import android.opengl.Visibility
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.denzcoskun.imageslider.constants.ScaleTypes
import com.denzcoskun.imageslider.interfaces.ItemChangeListener
import com.denzcoskun.imageslider.interfaces.ItemClickListener
import com.denzcoskun.imageslider.models.SlideModel
import com.google.gson.Gson
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.FragmentHomeBinding
import com.rss.rajasri.datamodels.response.EmiDetails
import com.rss.rajasri.datamodels.response.EmiDetailsDM
import com.rss.rajasri.datamodels.response.GetCustomerDetailsDM
import com.rss.rajasri.datamodels.response.PropertiesDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.activities.AvailablePropertyDetails
import com.rss.rajasri.ui.activities.PlotDetailsActivity
import com.rss.rajasri.ui.activities.transaction_history.TransactionAdapter
import com.rss.rajasri.ui.activities.transaction_history.TransactionsHistoryActivity
import com.rss.rajasri.ui.dashboard.HomeActivity
import com.rss.rajasri.ui.fragments.home.CityPropertyAdapter
import com.rss.rajasri.ui.fragments.home.PropertiesAdapter
import com.rss.rajasri.utils.Adapter_Details
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.IS_FROM_PENDING
import com.rss.rajasri.utils.PROPERTY_DETAILS
import com.rss.rajasri.utils.PROPERTY_ID
import com.rss.rajasri.utils.hide
import com.rss.rajasri.utils.show
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class HomeFragment : Fragment() {
    private lateinit var binding: FragmentHomeBinding
   // private var sliderDataList: List<PropertiesDataModel> = emptyList()
   val imageList = ArrayList<SlideModel>()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentHomeBinding.inflate(inflater,container,false)
        binding.viewAllTV.setOnClickListener {
            (activity as HomeActivity).binding.bottomNav.selectedItemId = R.id.myPropertiesFragment
        }

        binding.txtContact.setPaintFlags(binding.txtContact.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG)

        binding.txtContact.setOnClickListener {
            val phone = "6262666999"
            val intent = Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null))
            startActivity(intent)
        }
        binding.viewallText.setOnClickListener {
            startActivity(Intent(it.context,TransactionsHistoryActivity::class.java))
        }
        binding.homeVenture1.viewDetailsTV.setOnClickListener {
            startActivity(Intent(it.context,PlotDetailsActivity::class.java))
        }

        binding.homeVenture2.viewDetailsTV.setOnClickListener {
            startActivity(Intent(it.context,PlotDetailsActivity::class.java))
        }

        binding.homeVenture3.viewDetailsTV.setOnClickListener {
            startActivity(Intent(it.context,PlotDetailsActivity::class.java))
        }
        binding.homeVenture4.viewDetailsTV.setOnClickListener {
            startActivity(Intent(it.context,PlotDetailsActivity::class.java))
        }

        binding.txtViewAvailable.setPaintFlags(binding.txtViewAvailable.getPaintFlags() or Paint.UNDERLINE_TEXT_FLAG);

        binding.searchCV.setOnClickListener {
/*            val navController = Navigation.findNavController(
                requireActivity(), R.id.nav_host_fragment_activity_home
            )
            navController.navigate(R.id.searchFragment)*/
            (activity as HomeActivity).binding.bottomNav.selectedItemId = R.id.myPropertiesFragment
        }
        binding.searchET.setOnClickListener {
            binding.searchCV.performClick()
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callGetCustomerDetailsApi()
        callPropertiesApi()
        callAvailablePropertiesApi()
        try {
            callEmiDetailsApi()
        }catch (e: Exception){
            e.printStackTrace()
        }

        try{
           // ( requireActivity() as HomeActivity).setProfilePic()
        }catch (e:Exception)
        {
            e.printStackTrace()
        }
    }

    private fun callEmiDetailsApi(){
        if(!requireContext().isInternetAvailable()){
            requireContext().showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(requireContext())
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        /*        val dataModel = intent?.getStringExtra(PROPERTY_DETAILS)?:""
                val properties  = Gson().fromJson(dataModel, AllProperties::class.java)*/
        jsonObject.put("customer_id", RajaSriApp.appPreference.getCustomerID())
        jsonObject.put("payment_status", "unpaid")
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callBiilingPaidUnpaidByYearMonth(body)
        apiCall.enqueue(object : Callback<EmiDetailsDM> {
            override fun onResponse(call: Call<EmiDetailsDM>, response: Response<EmiDetailsDM>) {
                if(response.code()==500)
                {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        val status = jsonObject.getString("status")
                        if(status=="200")
                        {
                            val propertyDetails=Gson().fromJson(jsonObject.toString(),EmiDetailsDM::class.java)
                            setDataToEmiRV(propertyDetails)
                            Log.e("Response","Response success my ${jsonObject.toString()}")
                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("Response","Response success ${e.message}")

                    }
                    return
                }
                setDataToEmiRV(response.body())
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<EmiDetailsDM>, t: Throwable) {
                context?.showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }

    private fun setDataToEmiRV(body: EmiDetailsDM?) {
        Log.d("Context","Homefragment ${body?.status}")
        Log.d("Context","Homefragment ${body?.message}")
        if(body?.status=="200"){
            val paidTransactionList = body.data?.emiDetails?.filter { it.name?.equals("unpaid",ignoreCase = true) ==true}?.toMutableList() as ArrayList
            if(paidTransactionList.isNotEmpty()){
                binding.linearInvoice.show()
                binding.unPaidRVNoData.hide()
                val adapter = TransactionAdapter(requireActivity())
                binding.unPaidRV.adapter = adapter
                adapter.setData(paidTransactionList.take(1).toMutableList() as ArrayList<EmiDetails>)
            }else{
                binding.linearInvoice.hide()
                binding.unPaidRVNoData.show()
            }
        }else{
            binding.linearInvoice.hide()
            binding.unPaidRVNoData.show()
        }
    }
    private fun callPropertiesApi(){
        if(!requireContext().isInternetAvailable()){
            requireContext().showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(requireContext())
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("customer_id",RajaSriApp.appPreference.getCustomerID())
        Log.e("customer_id_",RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callCustomerPropertiesApi(body)
        apiCall.enqueue(object : Callback<PropertiesDataModel> {
            override fun onResponse(call: Call<PropertiesDataModel>, response: Response<PropertiesDataModel>) {
                if(response.code()==500)
                {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        val status = jsonObject.getString("status")
                        if(status=="200")
                        {
                            val propertyDetails=Gson().fromJson(jsonObject.toString(),PropertiesDataModel::class.java)
                            setDataToList(propertyDetails)
                            Log.e("Response","Response success my ${jsonObject.toString()}")
                        }


                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("Response","Response success ${e.message}")

                    }
                    return
                }
                setDataToList(response.body())
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<PropertiesDataModel>, t: Throwable) {
                requireContext().showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }

    private fun setDataToList(body: PropertiesDataModel?) {
        if(body?.status=="200"){
            val propertiesAdapter = PropertiesAdapter()
            binding.propertiesRV.adapter = propertiesAdapter
            propertiesAdapter.setOnClickListener {
                startActivity(Intent(requireContext(),PlotDetailsActivity::class.java).apply {
                    putExtra(PROPERTY_DETAILS,Gson().toJson(it))
                })
            }
            propertiesAdapter.setData(body.data?.allProperties?: arrayListOf())

            if(body.data?.allProperties.isNullOrEmpty()){
                binding.propertiesRV.visibility = View.GONE
                binding.propertiesRVNoData.visibility = View.VISIBLE
            }

        }else{
            binding.propertiesRV.visibility = View.GONE
            binding.propertiesRVNoData.visibility = View.VISIBLE
        }

    }

    private fun callGetCustomerDetailsApi(){
        if(!requireContext().isInternetAvailable()){
            requireContext().showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(requireContext())
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("customer_id",RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callCustomerDetailsApi(body)
        apiCall.enqueue(object : Callback<GetCustomerDetailsDM> {
            override fun onResponse(call: Call<GetCustomerDetailsDM>, response: Response<GetCustomerDetailsDM>) {
                setDataToUI(response.body())
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<GetCustomerDetailsDM>, t: Throwable) {
                requireContext().showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }

    private fun setDataToUI(body: GetCustomerDetailsDM?) {
        body?.apply {
            data?.map {
               binding. userNameTV.text =  it.fullName

            }
        }

    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {

            }
    }

    private fun callAvailablePropertiesApi(){
        if(!requireContext().isInternetAvailable()){
            requireContext().showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(requireActivity())
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("customer_id", RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callAvailableCustomerPropertiesApi(body)
        apiCall.enqueue(object : Callback<PropertiesDataModel> {
            override fun onResponse(call: Call<PropertiesDataModel>, response: Response<PropertiesDataModel>) {
                setAvailableDataToList(response.body())
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<PropertiesDataModel>, t: Throwable) {
                requireContext(). showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }
    private fun setAvailableDataToList(body: PropertiesDataModel?) {
        if(body?.status=="200"){
            val cityPropertyAdapter = CityPropertyAdapter()
            binding.recyclerAvailableProperties.adapter = cityPropertyAdapter
            cityPropertyAdapter.setOnClickListener {it,type->
                when(type){
                    Adapter_Details ->{
                        startActivity(Intent(requireActivity(),PlotDetailsActivity::class.java).apply {
                            putExtra(PROPERTY_DETAILS, Gson().toJson(it))
                            putExtra(IS_FROM_PENDING,false)
                        })
                    }
                }
            }
            imageList.clear()
            body.data?.city_properties?.forEach {
                var images=it.image
                if(images!=null)
                imageList.add(SlideModel(images, ""))
                else
                imageList.add(SlideModel(R.drawable.venture_image_png, ""))
            }
            binding!!.imageSlider.setImageList(imageList, scaleType = ScaleTypes.CENTER_CROP)

            try {
                if(body.data?.city_properties?.size!!>0)
                binding.txtProperty.setText(body.data?.city_properties?.get(0)?.name)
            }catch (e:Exception){

            }
            binding.imageSlider.setItemChangeListener(object :ItemChangeListener{
                override fun onItemChanged(position: Int) {
                    try {
                        binding.txtProperty.setText(body.data?.city_properties?.get(position)?.name)
                    }catch (e:Exception){

                    }
                }

            })
            binding!!.imageSlider.setItemClickListener(object :ItemClickListener{
                override fun doubleClick(position: Int) {

                }
                override fun onItemSelected(position: Int) {
                    val intent=Intent(requireActivity(),AvailablePropertyDetails::class.java)
                    intent.putExtra(PROPERTY_ID,body.data?.city_properties?.get(position)?.id)
                    /*intent.apply {
                        putExtra(PROPERTY_ID,body.data?.city_properties?.get(position)?.id)
                    }*/
                    startActivity(intent)
                }

            })
            cityPropertyAdapter.setData(body.data?.city_properties?: arrayListOf())

        }else{
            requireContext().showToastMsg(body?.message)
        }

    }
}