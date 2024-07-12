package com.rss.rajasri.ui.fragments.Bullings.Childs

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.gson.Gson
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.FragmentInvoicePaidBinding
import com.rss.rajasri.datamodels.response.ListofInvoice
import com.rss.rajasri.datamodels.response.PendingInvoicesDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.fragments.home.InvoicePaidAdapter
import com.rss.rajasri.utils.Adapter_Details
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.Exception

class InvoicePaidFragment : Fragment() {

    private lateinit var binding: FragmentInvoicePaidBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentInvoicePaidBinding.inflate(inflater,container,false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        try {
            callPendingInvoiceApi()
        }catch (e: Exception){
            e.printStackTrace()
        }

    }

    private fun callPendingInvoiceApi() {
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("customer_id", RajaSriApp.appPreference.getCustomerID())
        Log.e("customer_id_", RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.pendingInvoices(body)
        apiCall.enqueue(object : Callback<PendingInvoicesDataModel> {
            override fun onResponse(
                call: Call<PendingInvoicesDataModel>,
                response: Response<PendingInvoicesDataModel>
            ) {
                Log.e("Response", "Response success ${response.code()}")
                if (response.code() == 500) {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        val status = jsonObject.getString("status")
                        if (status == "200") {
                            val propertyDetails =
                                Gson().fromJson(
                                    jsonObject.toString(),
                                    PendingInvoicesDataModel::class.java
                                )
                            setDataInvoicesList(propertyDetails)
                            Log.e("Response_pendingInvoices", "Response success ${jsonObject.toString()}")
                        }
                    } catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("Response", "Response success ${e.message}")
                    }
                    return
                }
                setDataInvoicesList(response.body())
            }

            override fun onFailure(call: Call<PendingInvoicesDataModel>, t: Throwable) {
                Log.e("Response", "Response error pending ")
            }

        })
    }

    private fun setDataInvoicesList(body: PendingInvoicesDataModel?) {
        if (body?.status == "200") {
            val propertiesAdapter = InvoicePaidAdapter(requireActivity())
            binding.invoiceRv.adapter = propertiesAdapter
            propertiesAdapter.setOnClickListener { it, type ->
                when (type) {
                    Adapter_Details -> {
//                        invoiceId = Gson().toJson(it.id)
//                        invoiceAmount = Gson().toJson(it.invoiceAmount)
                    }
                }
            }

//            Log.e("item_", body.data?.pending_invoices?.get(0)?.gatewayStatus.toString())
            val pendingInvoices: List<ListofInvoice>? = body?.data?.pending_invoices
            // Initialize newpending_invoices as an empty list
            val newpending_invoices = mutableListOf<ListofInvoice>()

            if (pendingInvoices != null && pendingInvoices.isNotEmpty()) {
                for (invoice in pendingInvoices) {
                    if (invoice.gatewayStatus == "1") {
                        newpending_invoices.add(invoice)
                    }
                }
                body.data?.pending_invoices?.let { propertiesAdapter.setData(newpending_invoices) }
            }

            if (propertiesAdapter.itemCount <= 0) {
                binding.linearEmi.visibility = View.GONE
                binding.noDataFoundTV.visibility = View.VISIBLE
            }
        } else {
            binding.linearEmi.visibility = View.GONE
            binding.noDataFoundTV.visibility = View.VISIBLE
        }
    }

}