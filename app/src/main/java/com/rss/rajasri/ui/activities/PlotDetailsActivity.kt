package com.rss.rajasri.ui.activities

import android.app.Dialog
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Bundle
import android.text.Html
import android.util.Base64
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.google.gson.Gson
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivityPlotDetailsBinding
import com.rss.rajasri.databinding.LayoutAdvancePayAlertBinding
import com.rss.rajasri.databinding.LayoutAdvancePayAlertBindingImpl
import com.rss.rajasri.databinding.TransactionsViewDetailsBinding
import com.rss.rajasri.datamodels.response.AllProperties
import com.rss.rajasri.datamodels.response.PropertiesDataModel
import com.rss.rajasri.datamodels.response.PropertyDetailsDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.Razorpay.RazorpayActivity
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.IS_FROM_PENDING
import com.rss.rajasri.utils.PROPERTY_DETAILS
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONException
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import se.warting.signatureview.utils.SignaturePadBindingAdapter
import se.warting.signatureview.views.SignedListener
import java.io.ByteArrayOutputStream


class PlotDetailsActivity : AppCompatActivity() {
    val binding:ActivityPlotDetailsBinding by lazy {
        ActivityPlotDetailsBinding.inflate(layoutInflater)
    }
    var amount_to_pay="";
    var property_id="";
    var is_signed=false;
    var is_from_pending=false;
    var agreementPath=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RajaSriApp.setLangConfig(baseContext);
        setContentView(binding.root)

        binding.includedHeaderLL.backButtonIV.setOnClickListener {
            finish()
        }
        is_from_pending= intent?.getBooleanExtra(IS_FROM_PENDING,false) == true
        try {
            callPropertiesApi()
        }catch (e:Exception){
            e.printStackTrace()
            showToastMsg(e.message)
        }
        if(!is_from_pending) {
            binding.btnVewAgreement.visibility = View.VISIBLE
            binding.btnVewAgreement.setOnClickListener {
                if(agreementPath==null||agreementPath.isEmpty()||agreementPath=="null")
                {
                    showToastMsg(resources.getString(R.string.agreement_not_attached))
                    return@setOnClickListener
                }
                startActivity(Intent(Intent.ACTION_VIEW, Uri.parse(agreementPath.toString())))

            }
        }

        binding.txtTerms.setOnClickListener {
            startActivity(Intent(it.context, TermsAndConditionsActivity::class.java))
        }

        if(is_from_pending) {
            binding.cardviewSignature.visibility = View.VISIBLE
            binding.txtTermsCondition.visibility = View.VISIBLE
        }

        binding.signaturePad.setOnSignedListener(object : SignaturePadBindingAdapter.OnSignedListener,
            SignedListener {
            override fun onStartSigning() {
                //Event triggered when the pad is touched
               /* Toast.makeText(this@PlotDetailsActivity, "onStartSigning() triggered!", Toast.LENGTH_SHORT)
                    .show()*/
                binding.clearButton.isEnabled=true
                binding.saveButton.isEnabled=true
                is_signed=true
            }

            override fun onSigned() {
                //Event triggered when the pad is signed
                /*Toast.makeText(this@PlotDetailsActivity, "onStartSigning() triggered!", Toast.LENGTH_SHORT)
                    .show()*/
                is_signed=true;
            }

            override fun onSigning() {

            }

            override fun onClear() {
                //Event triggered when the pad is cleared
               /* Toast.makeText(this@PlotDetailsActivity, "onStartSigning() triggered!", Toast.LENGTH_SHORT)
                    .show()*/
                is_signed=false;
            }
        })

        binding.saveButton.setOnClickListener {
            if (!binding.termscheck.isChecked) {
                showToastMsg(resources.getString(R.string.accept_terms_conditions))
                return@setOnClickListener
            }
            if(!is_signed)
            {
                showToastMsg(resources.getString(R.string.please_sign_agreement))
                return@setOnClickListener
            }
            val bitmap=binding.signaturePad.getSignatureBitmap()
            val byteArrayOutputStream = ByteArrayOutputStream()
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream)
            val byteArray = byteArrayOutputStream.toByteArray()
            val encoded: String = Base64.encodeToString(byteArray, Base64.DEFAULT)
            submitAgreement(encoded)
        }
        binding.clearButton.setOnClickListener {
            binding.signaturePad.clear()
            Toast.makeText(this@PlotDetailsActivity, "onStartSigning() triggered!", Toast.LENGTH_SHORT)
                .show()
            is_signed=false;
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
        val dataModel = intent?.getStringExtra(PROPERTY_DETAILS)?:""
        val properties  = Gson().fromJson(dataModel,AllProperties::class.java)
        jsonObject.put("customer_property_id", properties.id)
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callCustomerPropertyDetailsApi(body)
        apiCall.enqueue(object : Callback<PropertyDetailsDataModel> {
            override fun onResponse(call: Call<PropertyDetailsDataModel>, response: Response<PropertyDetailsDataModel>) {
                CommonMethods.hideProgress()
                if(response.code()==500)
                {
                    var jsonObject: JSONObject? = null
                    try {
                        jsonObject = JSONObject(response.errorBody()!!.string())
                        val status = jsonObject.getString("status")
                        if(status=="200")
                        {
                            val propertyDetails=Gson().fromJson(jsonObject.toString(),
                                PropertyDetailsDataModel::class.java)
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

            }

            override fun onFailure(call: Call<PropertyDetailsDataModel>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }

    private fun setDataToList(body: PropertyDetailsDataModel?) {
        if(body?.status=="200"){
            body.data?.propertyDetails?.map { propertyDetails ->
                binding.apply {
                    titleTV.text = propertyDetails.name
                    descriptionTV.text = Html.fromHtml(propertyDetails.description)
                    ventureNameTV.text = propertyDetails.name
                    priceTV.text = "₹ ${propertyDetails.price}/-"
                    plotSizeTV.text = "${propertyDetails.area} (${propertyDetails.area_name})"
                    locationTV.text = "${propertyDetails.location}"
                    plotRegTV.text = "${propertyDetails.fullName}"
                    txtFacing.text = "${propertyDetails.facing}"
                    txtEmi.text = "₹ ${propertyDetails.emiAmount}"
                    if(propertyDetails.paid_loan_amount!=null&& propertyDetails.paid_loan_amount!!.isNotEmpty()) {
                        lnrPaid.visibility=View.VISIBLE
                        txtPaid.text = "₹ ${propertyDetails.paid_loan_amount}"
                    }else
                    {
                        lnrPaid.visibility=View.GONE
                    }
                    if(propertyDetails.agreement_file_full_path==null|| propertyDetails.agreement_file_full_path!!.isEmpty())
                    {
                        binding.btnVewAgreement.visibility=View.GONE
                    }else
                    {
                        binding.btnVewAgreement.visibility=View.VISIBLE
                        binding.btnVewAgreement.setOnClickListener {
                            Log.e("agreement_file_full_path","agreement_file_full_path ${propertyDetails.agreement_file_full_path}")

                            var intent=Intent(Intent.ACTION_VIEW)
                            intent.setDataAndType(Uri.parse(propertyDetails.agreement_file_full_path), "application/pdf");

                            startActivity(intent)
                            if(true)
                            {
                                return@setOnClickListener
                            }
                        }
                    }
                    Glide.with(bannerIV)
                        .load(propertyDetails.image)
                        .placeholder(R.drawable.venture_image_png)
                        .into(bannerIV)

                    amount_to_pay=propertyDetails.paid_loan_amount.toString()
                    property_id=propertyDetails.id.toString()

                }
                agreementPath= propertyDetails.agreement_file_full_path.toString()
            }

        }else{
            showToastMsg(body?.message)
        }

    }



    private fun submitAgreement( base64Img:String){

        if(!isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        var base64=base64Img
        if(base64.endsWith("\n")) {
            base64=base64.substring(0,base64.lastIndexOf("\n"))
        }

        CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        val dataModel = intent?.getStringExtra(PROPERTY_DETAILS)?:""
        val properties  = Gson().fromJson(dataModel,AllProperties::class.java)
        jsonObject.put("customer_property_id", properties.id)
        jsonObject.put("signature_img", base64)
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.submitAgreementSign(body)
        apiCall.enqueue(object : Callback<Any> {
            override fun onResponse(call: Call<Any>, response: Response<Any>) {
                //setDataToList(response.body())
                CommonMethods.hideProgress()
                showAdvancePayAlert()
               /* setResult(200)
                finish()*/
            }

            override fun onFailure(call: Call<Any>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
                setResult(404)
                finish()
            }

        })
    }

    fun showAdvancePayAlert()
    {
        val dialogBinding  = LayoutAdvancePayAlertBinding.inflate(layoutInflater,null,false)
        val dialog = Dialog(this@PlotDetailsActivity)
        dialog.setContentView(dialogBinding.root)
        dialog.show()
        val att = dialog.window?.attributes
        att?.width = WindowManager.LayoutParams.MATCH_PARENT
        att?.height = WindowManager.LayoutParams.WRAP_CONTENT
        dialog.window?.attributes = att
        dialog.window?.setBackgroundDrawable(ColorDrawable().apply {
            color = Color.TRANSPARENT
        })
        dialogBinding.txtAdvanceAmount.setText("₹ $amount_to_pay")
        dialogBinding.txtApplicationAmount.setText("₹ 1000")
        dialogBinding.btnPay.setOnClickListener {
            dialog.dismiss()
            startPaymentForSignature()
        }
    }
    fun startPaymentForSignature()
    {

        val payamount=amount_to_pay.toInt()+1000
        startActivityForResult(Intent(applicationContext, RazorpayActivity()::class.java).apply {
            putExtra("Amount",payamount.toString())
            putExtra("paymentType","Signature")
            putExtra("PayId",property_id)
            putExtra("Id","")
        },200)
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode==200&&requestCode==200)
        {
            setResult(200)
            finish()
        }
    }

}