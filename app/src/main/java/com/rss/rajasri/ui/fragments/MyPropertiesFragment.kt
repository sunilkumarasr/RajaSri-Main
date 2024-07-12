package com.rss.rajasri.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.google.gson.Gson
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.FragmentMyPropertiesBinding
import com.rss.rajasri.databinding.FragmentsearchScreenBinding
import com.rss.rajasri.datamodels.response.AllProperties
import com.rss.rajasri.datamodels.response.PropertiesDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.activities.LoanDetailsActivity
import com.rss.rajasri.ui.activities.PlotDetailsActivity
import com.rss.rajasri.ui.fragments.home.SearchPropertiesAdapter
import com.rss.rajasri.utils.Adapter_Details
import com.rss.rajasri.utils.Adapter_Loan
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.PROPERTY_DETAILS
import com.rss.rajasri.utils.hide
import com.rss.rajasri.utils.show
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MyPropertiesFragment : Fragment() {
    private lateinit var binding: FragmentMyPropertiesBinding
    private var temporaryList:ArrayList<AllProperties> = arrayListOf()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentMyPropertiesBinding.inflate(inflater,container,false)

        binding.viewdetailsBt.setOnClickListener {
            startActivity(Intent(it.context, PlotDetailsActivity::class.java))
        }
        binding.loandetailsBt.setOnClickListener {
            startActivity(Intent(it.context, LoanDetailsActivity::class.java))
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callPropertiesApi()
        binding.searchET.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if(binding.searchET.editableText.trim().isNullOrEmpty()){
                    if(binding.searchedRV.adapter!=null){
                        val adapter = binding.searchedRV.adapter as SearchPropertiesAdapter
                        adapter.setData(temporaryList)
                    }

                    binding.searchedRV.show()
                    binding.noDataFoundTV.hide()
                }else{
                    val searchList = temporaryList
                        .filter { it.name?.contains(p0.toString(),ignoreCase = true)==true }
                        .toMutableList() as ArrayList
                    if(searchList.isNullOrEmpty()){
                        binding.searchedRV.hide()
                        binding.noDataFoundTV.show()
                    }else{
                        binding.searchedRV.show()
                        binding.noDataFoundTV.hide()
                    }
                    val adapter = binding.searchedRV.adapter as SearchPropertiesAdapter
                    adapter.setData(searchList)
                }
            }

            override fun afterTextChanged(p0: Editable?) {}

        })
    }

    private fun callPropertiesApi(){
        if(!requireContext().isInternetAvailable()){
            requireContext().showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(requireContext())
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        Log.e("RajaSriApp.appPreference.getCustomerID()","RajaSriApp.appPreference.getCustomerID() ${RajaSriApp.appPreference.getCustomerID()}")
        jsonObject.put("customer_id", RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callCustomerPropertiesApi(body)
        apiCall.enqueue(object : Callback<PropertiesDataModel> {

            override fun onResponse(call: Call<PropertiesDataModel>, response: Response<PropertiesDataModel>) {
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
            val propertiesAdapter = SearchPropertiesAdapter(requireActivity())
            binding.searchedRV.adapter = propertiesAdapter
            propertiesAdapter.setOnClickListener {it,type->
                when(type){
                    Adapter_Details ->{
                        startActivity(Intent(requireContext(), PlotDetailsActivity::class.java).apply {
                            putExtra(PROPERTY_DETAILS, Gson().toJson(it))
                        })
                    }
                    Adapter_Loan ->{
                        startActivity(Intent(requireContext(), LoanDetailsActivity::class.java).apply {
                            putExtra(PROPERTY_DETAILS, Gson().toJson(it))
                        })
                    }
                }

            }
            temporaryList = body.data?.allProperties?: arrayListOf()
            if(temporaryList.isNullOrEmpty()){
                binding.searchedRV.hide()
                binding.noDataFoundTV.show()
            }else{
                binding.searchedRV.show()
                binding.noDataFoundTV.hide()
            }
            propertiesAdapter.setData(temporaryList)

        }else{
           // requireContext().showToastMsg(body?.message)
        }

    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment MyPropertiesFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            MyPropertiesFragment().apply {

            }
    }
}