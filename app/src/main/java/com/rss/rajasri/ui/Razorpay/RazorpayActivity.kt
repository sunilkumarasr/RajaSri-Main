package com.rss.rajasri.ui.Razorpay

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.razorpay.Checkout
import com.razorpay.PaymentResultListener
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivityRazorpayBinding
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.dashboard.HomeActivity
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RazorpayActivity : AppCompatActivity(), PaymentResultListener {

    val binding: ActivityRazorpayBinding by lazy {
        ActivityRazorpayBinding.inflate(layoutInflater)
    }

    lateinit var Amount:String
    lateinit var paymentType:String
    lateinit var PayId:String
    lateinit var customer_id:String
    lateinit var Id:String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        customer_id=RajaSriApp.appPreference.getCustomerID()
        Amount= intent.getStringExtra("Amount").toString()
        paymentType= intent.getStringExtra("paymentType").toString()
        PayId= intent.getStringExtra("PayId").toString()
        Id= intent.getStringExtra("Id").toString()

        startPayment()

        //success
        binding.cardHomeSuccess.setOnClickListener {
           /* val intent=Intent(this@RazorpayActivity, HomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)*/
            setResult(200)
            finish()
        }

        //failed
        binding.cardTryAgain.setOnClickListener {
            finish()
        }
        binding.cardHomeFailed.setOnClickListener {
            /*val intent=Intent(this@RazorpayActivity, HomeActivity::class.java)
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
            startActivity(intent)
            finish()*/
            setResult(200)
            finish()
        }

    }

    private fun startPayment() {

        //live
        //rzp_live_KnBdoDUl13wajc

        val checkout = Checkout()
        //checkout.setKeyID("rzp_test_I5qDYUDVJ0CSnZ")
        checkout.setKeyID("rzp_live_KnBdoDUl13wajc")

        try {
            val options = JSONObject()
            options.put("name", "Raja sree townships")
            options.put("description", "Test Payment")
            options.put("currency", "INR")
            //options.put("amount",  Amount+"00") // Amount in paise
            options.put("amount",  "${Amount}00")

            val prefill = JSONObject()
            prefill.put("email",  RajaSriApp.appPreference.getEMailID())
            prefill.put("contact", RajaSriApp.appPreference.getUserMobile())
            options.put("prefill", prefill)

            val theme = JSONObject()
            theme.put("color", "#F80C00")// Replace with your desired color code
            options.put("theme", theme)

            checkout.open(this, options)
        } catch (e: Exception) {
            //Toast.makeText(this, "Error in payment: ${e.message}", Toast.LENGTH_LONG).show()
            binding.cardViewPaymentFailed.visibility = View.VISIBLE
            binding.cardViewPaymentSuccess.visibility = View.GONE
            e.printStackTrace()
        }
    }

    override fun onPaymentSuccess(razorpayPaymentID: String?) {
        if(paymentType.equals("Signature"))
        {
            SignatureUpdate(razorpayPaymentID)
        }else
        if (paymentType.equals("EMI")){
            EMIpaymentSuccess(razorpayPaymentID)
        }else{
            InvoicepaymentSuccess(razorpayPaymentID)
        }
    }

    override fun onPaymentError(code: Int, response: String?) {
        Log.e("error_",response.toString())
        //Toast.makeText(this, response.toString(), Toast.LENGTH_LONG).show()
        binding.cardViewPaymentFailed.visibility = View.VISIBLE
        binding.cardViewPaymentSuccess.visibility = View.GONE
    }

    //unpaid EMI
    private fun EMIpaymentSuccess(razorpayPaymentID: String?) {
        if (!isInternetAvailable()) {
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this@RazorpayActivity)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        val emiID = Id.trim('"').toInt()
        jsonObject.put("property_id", PayId)
        jsonObject.put("emi_id", emiID.toString())
        jsonObject.put("Payment_ID", razorpayPaymentID.toString())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.emipaymentSuccess(body)
        apiCall.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                CommonMethods.hideProgress()
                binding.cardViewPaymentFailed.visibility = View.GONE
                binding.cardViewPaymentSuccess.visibility = View.VISIBLE
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                t.printStackTrace()
                CommonMethods.hideProgress()
                binding.cardViewPaymentFailed.visibility = View.VISIBLE
                binding.cardViewPaymentSuccess.visibility = View.GONE
            }
        })
    }
    private fun SignatureUpdate(razorpayPaymentID: String?) {
        if (!isInternetAvailable()) {
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this@RazorpayActivity)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()

        jsonObject.put("customer_id", customer_id)
        jsonObject.put("property_id", PayId)
        jsonObject.put("amount", Amount)
        jsonObject.put("Payment_ID", razorpayPaymentID.toString())

        Log.e("Request","Request ${jsonObject.toString()}")

        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.payAdvance(body)
        apiCall.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                CommonMethods.hideProgress()
                binding.cardViewPaymentFailed.visibility = View.GONE
                binding.cardViewPaymentSuccess.visibility = View.VISIBLE
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                t.printStackTrace()
                CommonMethods.hideProgress()
                binding.cardViewPaymentFailed.visibility = View.VISIBLE
                binding.cardViewPaymentSuccess.visibility = View.GONE
            }
        })
    }

    //unpaid InVoice
    private fun InvoicepaymentSuccess(razorpayPaymentID: String?) {
        if (!this@RazorpayActivity.isInternetAvailable()) {
            this@RazorpayActivity.showToastMsg(resources.getString(R.string.check_internet))
            finish()
            return
        }
        CommonMethods.showProgress(this@RazorpayActivity)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("invoice_id", PayId)
        jsonObject.put("Payment_ID", razorpayPaymentID.toString())

        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.invoicepaymentSuccess(body)
        apiCall.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                CommonMethods.hideProgress()
                binding.cardViewPaymentFailed.visibility = View.GONE
                binding.cardViewPaymentSuccess.visibility = View.VISIBLE
            }
            override fun onFailure(call: Call<Any>, t: Throwable) {
                t.printStackTrace()
                CommonMethods.hideProgress()
                binding.cardViewPaymentFailed.visibility = View.VISIBLE
                binding.cardViewPaymentSuccess.visibility = View.GONE
            }
        })
    }

    override fun onBackPressed() {
        super.onBackPressed()
        if(paymentType.equals("Signature")){

        }else {
            startActivity(Intent(this@RazorpayActivity, HomeActivity::class.java))
        }
        finish()
    }

}