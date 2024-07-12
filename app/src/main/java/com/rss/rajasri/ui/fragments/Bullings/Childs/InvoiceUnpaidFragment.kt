package com.rss.rajasri.ui.fragments.Bullings.Childs

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.google.gson.Gson
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.FragmentInvoiceUnpaidBinding
import com.rss.rajasri.datamodels.response.ListofInvoice
import com.rss.rajasri.datamodels.response.PendingInvoicesDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.Razorpay.RazorpayActivity
import com.rss.rajasri.ui.dashboard.HomeActivity
import com.rss.rajasri.ui.fragments.home.InvoiceUnpaidAdapter
import com.rss.rajasri.utils.Adapter_Details
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.showToastMsg
import com.weipl.checkout.WLCheckoutActivity
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class InvoiceUnpaidFragment : Fragment(), WLCheckoutActivity.PaymentResponseListener  {

    private lateinit var binding: FragmentInvoiceUnpaidBinding
    private lateinit var mListener: WLCheckoutActivity.PaymentResponseListener

    val propertiesAdapter = InvoiceUnpaidAdapter()
    var property_id: String = "";
    var invoiceId: String = "";
    var invoiceAmount: String = "";

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?,): View {
        // Inflate the layout for this fragment
        binding = FragmentInvoiceUnpaidBinding.inflate(inflater, container, false)

        mListener = this
        WLCheckoutActivity.setPaymentResponseListener(mListener)
        WLCheckoutActivity.preloadData(requireContext())

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callPendingInvoiceApi()
    }

    //invoice amount
    private fun callPendingInvoiceApi() {
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("customer_id", RajaSriApp.appPreference.getCustomerID())
        Log.e("customer_id_", RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.pendingInvoices(body)
        apiCall.enqueue(object : Callback<PendingInvoicesDataModel> {
            override fun onResponse(call: Call<PendingInvoicesDataModel>, response: Response<PendingInvoicesDataModel>) {
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
                            setDataInvoiceList(propertyDetails)
                            Log.e("Response", "Response success ${jsonObject.toString()}")
                        }
                    }catch (e: JSONException) {
                        e.printStackTrace()
                        Log.e("Response", "Response success ${e.message}")
                    }
                    return
                }
                setDataInvoiceList(response.body())
            }
            override fun onFailure(call: Call<PendingInvoicesDataModel>, t: Throwable) {
                Log.e("Response", "Response error pending ")
            }
        })
    }

    private fun setDataInvoiceList(body: PendingInvoicesDataModel?) {
        if (body?.status == "200") {

            binding.invoiceRv.adapter = propertiesAdapter
            propertiesAdapter.setOnClickListener { it, type ->
                when (type) {
                    Adapter_Details -> {
                        invoiceId = Gson().toJson(it.id)
                        invoiceAmount = Gson().toJson(it.invoiceAmount)
//                        paynimoApiRun()
                        //InvoicepaymentSuccess(invoiceId)
                        startActivityForResult(Intent(activity, RazorpayActivity()::class.java).apply {
                            putExtra("Amount",invoiceAmount)
                            putExtra("paymentType","Invoice")
                            putExtra("PayId",invoiceId)
                            putExtra("Id","")
                        },200)
                    }
                }
            }
            //body.data?.pending_invoices?.let { propertiesAdapter.setData(it) }

            val pendingInvoices: List<ListofInvoice>? = body?.data?.pending_invoices
            val newpending_invoices = mutableListOf<ListofInvoice>()
            if (pendingInvoices != null && pendingInvoices.isNotEmpty()) {
                for (invoice in pendingInvoices) {
                    if (invoice.gatewayStatus == "0") {
                        newpending_invoices.add(invoice)
                    }
                }
                body.data?.pending_invoices?.let { propertiesAdapter.setData(newpending_invoices) }
            }

            if (propertiesAdapter.itemCount <= 0) {
                binding.linearinvoice.visibility = View.GONE
                binding.noDataFoundTV.visibility = View.VISIBLE
            }
        } else {
            Log.e("body?.message", "body?.message ${body?.message}")
            if (propertiesAdapter.itemCount <= 0) {
                binding.linearinvoice.visibility = View.GONE
                binding.noDataFoundTV.visibility = View.VISIBLE
            }
        }

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==200&&requestCode==200)
        {
            callPendingInvoiceApi()
        }
    }

    private fun paynimoApiRun() {
        val reqJson = JSONObject()

        val jsonFeatures = JSONObject()
        jsonFeatures.put("enableExpressPay", true)
        jsonFeatures.put("enableInstrumentDeRegistration", true)
        jsonFeatures.put("enableAbortResponse", true)
        jsonFeatures.put("enableMerTxnDetails", true)
        reqJson.put("features", jsonFeatures)

        val jsonConsumerData = JSONObject()
        jsonConsumerData.put("deviceId","AndroidSH2") // Possible values "AndroidSH1" or "AndroidSH2"
        jsonConsumerData.put("token","e04be9ed85f134a8ca30f609dca6c1f36e742762590daf6ed6edda06275f378a2147f6244ca2295d134beba1e98c6e67140577893b99e6bd34c09d3f2350519c")
        jsonConsumerData.put("paymentMode", "all")
        jsonConsumerData.put("merchantLogoUrl","https://www.paynimo.com/CompanyDocs/company-logo-vertical.png")
        jsonConsumerData.put("merchantId", "L3348")
        jsonConsumerData.put("currency", "INR")
        jsonConsumerData.put("consumerId", "c964634")
        jsonConsumerData.put("txnId", "1708068696283")

        val jArrayItems = JSONArray()
        val jsonItem1 = JSONObject()
        jsonItem1.put("itemId", "first")
        jsonItem1.put("amount", "1") //invoiceAmount
        jsonItem1.put("comAmt", "0")
        jArrayItems.put(jsonItem1)
        jsonConsumerData.put("items", jArrayItems)

        val jsonCustomStyle = JSONObject()
        jsonCustomStyle.put("PRIMARY_COLOR_CODE", "#45beaa")
        jsonCustomStyle.put("SECONDARY_COLOR_CODE", "#ffffff")
        jsonCustomStyle.put("BUTTON_COLOR_CODE_1", "#2d8c8c")
        jsonCustomStyle.put("BUTTON_COLOR_CODE_2", "#ffffff")
        jsonConsumerData.put("customStyle", jsonCustomStyle)

        reqJson.put("consumerData", jsonConsumerData)

        WLCheckoutActivity.open(requireContext(), reqJson)
    }

    override fun wlCheckoutPaymentResponse(response: JSONObject) {
        Log.e("response_success", response.toString())

        val jsonObject = JSONObject(response.toString())
        val successValue = jsonObject.getString("msg").split("|")[1]

        if (successValue.equals("success")){
            //InvoicepaymentSuccess()
        }else{
            Toast.makeText(requireContext(), "payment failed", Toast.LENGTH_LONG).show()
        }

    }

    override fun wlCheckoutPaymentError(response: JSONObject) {
        Toast.makeText(requireContext(), "Payment failed", Toast.LENGTH_LONG).show()
        Log.e("response_error", response.toString())
    }


    private fun InvoicepaymentSuccess(invoiceId: String) {
        if (!requireContext().isInternetAvailable()) {
            requireContext().showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(requireContext())
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("invoice_id", invoiceId)
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.invoicepaymentSuccess(body)
        apiCall.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {

                startActivity(Intent(requireContext(), HomeActivity::class.java))

//                var jsonObject: JSONObject? = null
//                jsonObject = JSONObject(response.errorBody()!!.string())
//                val message = jsonObject.getString("message")
////                showToastMsg(message)
//                Toast.makeText(requireContext(), message, Toast.LENGTH_LONG).show()

            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }

}