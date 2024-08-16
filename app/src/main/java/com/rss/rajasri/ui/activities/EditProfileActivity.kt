package com.rss.rajasri.ui.activities

import android.content.Context
import android.content.Intent
import android.database.Cursor
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.provider.OpenableColumns
import android.util.Base64
import android.util.Log
import android.util.Patterns
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivityEditProfileBinding
import com.rss.rajasri.datamodels.response.GetCustomerDetailsDM
import com.rss.rajasri.datamodels.response.RegisterDataModel
import com.rss.rajasri.datamodels.response.UpdateProfileDM
import com.rss.rajasri.datamodels.response.UploadProfilePicDM
import com.rss.rajasri.preference.AppPreference
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.dashboard.HomeActivity
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.PROFILE_NAVIGATED_FROM_LOGIN
import com.rss.rajasri.utils.hide
import com.rss.rajasri.utils.show
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File

class EditProfileActivity : AppCompatActivity() {
   private val binding:ActivityEditProfileBinding by lazy {
        ActivityEditProfileBinding.inflate(layoutInflater)
    }
    private var selectedImageFile:File? = null
    var profile_image_file=""
    var profile_image_name=""
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        if(intent.getStringExtra(PROFILE_NAVIGATED_FROM_LOGIN).isNullOrEmpty()){
            binding.includedHeaderLL.backButtonIV.show()
            //binding.emailLL.show()
            callDetailsApi()
        }else{
            binding.includedHeaderLL.backButtonIV.hide()
            //binding.emailLL.hide()
            binding.phoneNumberET.append(RajaSriApp.appPreference.getUserMobile())
        }

        binding.includedHeaderLL.backButtonIV.setOnClickListener {
            finish()
        }
        binding.updateCv.setOnClickListener {
            validateUserInputs()
        }
        binding.profileIV.setOnClickListener {
            try {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }catch (e:Exception){
                e.printStackTrace()
                showToastMsg(e.message)
            }
        }
    }

    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            selectedImageFile = uriToFile(this@EditProfileActivity,uri)
           Glide.with(binding.profileIV)
               .load(selectedImageFile)
               .transform(CircleCrop())
               .placeholder(R.drawable.account_circle_24)
               .into(binding.profileIV)

           val bitmap= BitmapFactory.decodeFile(selectedImageFile.toString());
            //val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, uri)
            // initialize byte stream
            val stream = ByteArrayOutputStream()
            // compress Bitmap
            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, stream)
            // Initialize byte array
            val bytes = stream.toByteArray()
            // get base64 encoded string
            profile_image_file = Base64.encodeToString(bytes, Base64.DEFAULT)


            val returnCursor: Cursor = contentResolver.query(uri!!, null, null, null, null)!!
            val nameIndex = returnCursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            returnCursor!!.moveToFirst()
            profile_image_name = returnCursor!!.getString(nameIndex)
            returnCursor!!.close()


            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            showToastMsg(resources.getString(R.string.no_media_selected))
            Log.d("PhotoPicker", "No media selected")
        }
    }
    private fun uriToFile(context: Context, uri: Uri): File? {
        val filePathColumn = arrayOf(MediaStore.Images.Media.DATA)
        val cursor = context.contentResolver.query(uri, filePathColumn, null, null, null)
        cursor?.moveToFirst()
        val columnIndex = cursor?.getColumnIndex(filePathColumn[0])
        val filePath = cursor?.getString(columnIndex ?: -1)
        cursor?.close()

        return filePath?.let { File(it) }
    }

    private fun validateUserInputs(){
        if(!checkFullNameValidation()){
            showToastMsg("please, enter valid Full Name")
            return
        }
        if(!checkMobileNumberValidation()){
            showToastMsg(resources.getString(R.string.enter_valid_number))
            return
        }
        /*if(intent.getStringExtra(PROFILE_NAVIGATED_FROM_LOGIN).isNullOrEmpty()){
            if(!checkEmailValidation()){
                showToastMsg("please, enter valid email")
                return
            }
        }*/
        if(!checkAddressValidation()){
            showToastMsg("please, enter valid address")
            return
        }

        try {
            if(intent.getStringExtra(PROFILE_NAVIGATED_FROM_LOGIN).isNullOrEmpty()){
                callUpdateApi()
            }else{
                callUpdateApi()
            }
        }catch (e:Exception){
            showToastMsg(e.message)
            e.printStackTrace()
        }

    }

    private fun callRegisterApi() {
        if(!this.isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        val fullName = binding.fullNameET.editableText.trim().toString()
        val mobileNumber = binding.phoneNumberET.editableText.trim().toString()
        val address = binding.addressET.editableText.trim().toString()
        val email = binding.emailET.editableText.trim().toString()
        jsonObject.put("full_name", fullName)
        jsonObject.put("id", RajaSriApp.appPreference.getCustomerID())
        jsonObject.put("address",address)
        jsonObject.put("mobile_number",mobileNumber)
        jsonObject.put("profile_image_name",profile_image_name)
        jsonObject.put("profile_image_file",profile_image_file)
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callRegisterApi(body)
        apiCall.enqueue(object : Callback<RegisterDataModel> {
            override fun onResponse(call: Call<RegisterDataModel>, response: Response<RegisterDataModel>) {
                CommonMethods.hideProgress()
                val data = response.body()
                if(data?.status == "200"){
                        startActivity(Intent(this@EditProfileActivity, PendingPropertiesActivity::class.java))
                        finishAffinity()

                }
                showToastMsg("${data?.message}")
            }

            override fun onFailure(call: Call<RegisterDataModel>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })

    }
    private fun callUpdateApi() {
        if(!this.isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        val fullName = binding.fullNameET.editableText.trim().toString()
        val mobileNumber = binding.phoneNumberET.editableText.trim().toString()
        val address = binding.addressET.editableText.trim().toString()
        val email = binding.emailET.editableText.trim().toString()
        jsonObject.put("full_name", fullName)
        jsonObject.put("id", RajaSriApp.appPreference.getCustomerID())
        jsonObject.put("address",address)
        jsonObject.put("email",email)
        jsonObject.put("profile_image_name",profile_image_name)
        jsonObject.put("profile_image_file",profile_image_file)

        jsonObject.put("mobile_number",mobileNumber)
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callUpdateApi(body)
        apiCall.enqueue(object : Callback<UpdateProfileDM> {
            override fun onResponse(call: Call<UpdateProfileDM>, response: Response<UpdateProfileDM>) {
                CommonMethods.hideProgress()
                val data = response.body()
                if(data?.status == "200"){
                    if(intent.getStringExtra(PROFILE_NAVIGATED_FROM_LOGIN).isNullOrEmpty()) {
                        updatedLamda?.invoke("refresh")
                        finish()
                    }else
                    {
                        //if(RajaSriApp.appPreference.isLoggedIn()){
                            startActivity(Intent(this@EditProfileActivity, PendingPropertiesActivity::class.java))
                        //}
                    }

                }
                showToastMsg("${data?.message}")
            }

            override fun onFailure(call: Call<UpdateProfileDM>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })

    }



    private var profilePicUrlFromServer:String? = ""
    private fun callUploadProfileAPi() {
        if(!this.isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val mediaType = "application/octet-stream".toMediaTypeOrNull()
        val requestBody = (selectedImageFile?:return).asRequestBody(mediaType)
        val part = MultipartBody.Part.createFormData("profile_pic", filename = selectedImageFile?.name,requestBody)
        val apiCall = RetrofitClient.api.callUploadProfileApi(part,MultipartBody.Part.createFormData("id",RajaSriApp.appPreference.getCustomerID()))
        apiCall.enqueue(object : Callback<UploadProfilePicDM> {
            override fun onResponse(call: Call<UploadProfilePicDM>, response: Response<UploadProfilePicDM>) {
                CommonMethods.hideProgress()
                val data = response.body()
                if(data?.status == "200"){
                    profilePicUrlFromServer = data.data?.profilePic
                }
                showToastMsg("${data?.message}")

            }

            override fun onFailure(call: Call<UploadProfilePicDM>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })

    }

    private fun checkMobileNumberValidation():Boolean {
        val regex = "^[+]?[0-9]{10,13}$".toRegex()
        return binding.phoneNumberET.editableText.trim().toString().matches(regex)
    }

    private fun checkFullNameValidation():Boolean {
        return binding.fullNameET.editableText.trim().toString().isNotEmpty()
    }


    private fun checkAddressValidation():Boolean {
        return binding.addressET.editableText.trim().toString().isNotEmpty()
    }

    private fun checkEmailValidation():Boolean {
        return Patterns.EMAIL_ADDRESS.matcher(binding.emailET.editableText.trim().toString()).matches()
    }

    private fun callDetailsApi(){
        if(!isInternetAvailable()){
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        jsonObject.put("customer_id", RajaSriApp.appPreference.getCustomerID())
        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callCustomerDetailsApi(body)
        apiCall.enqueue(object : Callback<GetCustomerDetailsDM> {
            override fun onResponse(call: Call<GetCustomerDetailsDM>, response: Response<GetCustomerDetailsDM>) {
                setDataToUI(response.body())
                CommonMethods.hideProgress()
            }

            override fun onFailure(call: Call<GetCustomerDetailsDM>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }

        })
    }

    private fun setDataToUI(body: GetCustomerDetailsDM?) {
        body?.apply {
            data?.map {
                binding. fullNameET.append(it.fullName?:"")
                binding. phoneNumberET.append(it.mobileNumber?:"")
                binding. emailET.append(it.email?:"")
                binding. addressET.append(it.address?:"")
                it.email?.let { it1 -> RajaSriApp.appPreference.setUserEmailId(it1) }
                Glide.with(binding.profileIV)
                    .load(it.photo_full_path)
                    .transform(CircleCrop())
                    .placeholder(R.drawable.account_circle_24)
                    .into(binding.profileIV)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        updatedLamda = null
    }

    companion object{
        private var updatedLamda:((event:String) -> Unit)? = null
        fun newInstance(context: Context, updatedLamda:((event:String) -> Unit)? = null):Intent{
            this.updatedLamda = updatedLamda
            return  Intent(context,EditProfileActivity::class.java)
        }
    }
}