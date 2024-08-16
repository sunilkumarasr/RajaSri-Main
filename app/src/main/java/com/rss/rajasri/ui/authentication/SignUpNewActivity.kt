package com.rss.rajasri.ui.authentication

import android.app.DatePickerDialog
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
import android.text.Editable
import android.text.TextWatcher
import android.util.Base64
import android.util.Log
import android.util.Patterns
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.Toast
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.ActivitySignUpNewBinding
import com.rss.rajasri.databinding.ActivitySignUpScreenBinding
import com.rss.rajasri.datamodels.response.EMPDetailsResponse
import com.rss.rajasri.datamodels.response.RegisterDataModel
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.activities.PrivacyPolicyActivity
import com.rss.rajasri.ui.activities.TermsAndConditionsActivity
import com.rss.rajasri.utils.CommonMethods
import com.rss.rajasri.utils.CommonMethods.isInternetAvailable
import com.rss.rajasri.utils.showToastMsg
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.ByteArrayOutputStream
import java.io.File
import java.util.Calendar

class SignUpNewActivity : AppCompatActivity() {

    val binding: ActivitySignUpNewBinding by lazy {
        ActivitySignUpNewBinding.inflate(layoutInflater)
    }

    //upload image
    private var selectedImageFile:File? = null
    var profile_image_file=""
    var profile_image_name=""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        RajaSriApp.setLangConfig(baseContext)

        setContentView(binding.root)

        inits()

    }

    private fun inits() {

        val languages = resources.getStringArray(R.array.RelationList)
        // access the spinner
        val spinner = findViewById<Spinner>(R.id.spinner)
        if (spinner != null) {
            val adapter = ArrayAdapter(this,
                android.R.layout.simple_spinner_item, languages)
            spinner.adapter = adapter

            spinner.onItemSelectedListener = object :
                AdapterView.OnItemSelectedListener {
                override fun onItemSelected(parent: AdapterView<*>,
                                            view: View, position: Int, id: Long) {

                }

                override fun onNothingSelected(parent: AdapterView<*>) {
                    // write code to perform some action
                }
            }
        }

        binding.tvDOB.setOnClickListener {
            val c: Calendar = Calendar.getInstance()
            val mYear = c.get(Calendar.YEAR)
            val mMonth = c.get(Calendar.MONTH)
            val mDay = c.get(Calendar.DAY_OF_MONTH)
            val datePickerDialog = DatePickerDialog(
                this,
                { _, year, monthOfYear, dayOfMonth ->
                    val selectedDate = "$dayOfMonth-${monthOfYear + 1}-$year"
                    binding.tvDOB.text = selectedDate
                },
                mYear,
                mMonth,
                mDay
            )
            // Calculate the date 18 years ago from today
            c.add(Calendar.YEAR, -18)
            datePickerDialog.datePicker.maxDate = c.timeInMillis
            datePickerDialog.show()
        }

        binding.loginTV.setOnClickListener {
            startActivity(Intent(this@SignUpNewActivity, LoginScreen::class.java))
            finish()
        }
        binding.txtPrivacy.setOnClickListener {
            startActivity(Intent(it.context, PrivacyPolicyActivity::class.java))
        }
        binding.txtTerms.setOnClickListener {
            startActivity(Intent(it.context, TermsAndConditionsActivity::class.java))
        }
        binding.registerCV.setOnClickListener {
            validateUserInputs()
        }

        binding.empId.addTextChangedListener(object:TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {

            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                val empid=s.toString();
                if(empid.trim().length>3)
                {
                    callGetEmpDetails()
                }else{
                    binding.lnrEmpDetails.visibility=View.GONE
                }
            }

            override fun afterTextChanged(s: Editable?) {

            }

        })
        binding.uploadImage.setOnClickListener {
            try {
                pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))
            }catch (e:Exception){
                e.printStackTrace()
                showToastMsg(e.message)
            }
        }

    }


    private fun validateUserInputs() {
        if (!checkFullNameValidation()) {
            showToastMsg(resources.getString(R.string.enter_valid_name))
            return
        }
        if (!checkFatherNameValidation()) {
            showToastMsg(resources.getString(R.string.enter_valid_fathername))
            return
        }
        if (!checkDOBValidation()) {
            showToastMsg(resources.getString(R.string.enter_dob))
            return
        }
        if (!checkAgeValidation()) {
            showToastMsg(resources.getString(R.string.enter_age))
            return
        }
        if (!checkAadhaarValidation()) {
            showToastMsg(resources.getString(R.string.enter_valid_aadhar))
            return
        }
        if (binding.edEmail.editableText.trim().toString()?.isEmpty() == true) {
            showToastMsg(resources.getString(R.string.enter_email))
            return
        }
        if (!checkEmailValidation()) {
            showToastMsg(resources.getString(R.string.enter_email))
            return
        }
//        if (!checkCountryValidation()) {
//            showToastMsg(resources.getString(R.string.enter_country_name))
//            return
//        }
        if (!checkCityValidation()) {
            showToastMsg(resources.getString(R.string.enter_city_name))
            return
        }
        if (!checkPinCodeValidation()) {
            showToastMsg(resources.getString(R.string.enter_pincode))
            return
        }
//        if (!checkPassBookNumberValidation()) {
//            showToastMsg("please enter PassBook Number")
//            return
//        }
        if (!checkGroupNameValidation()) {
            showToastMsg(resources.getString(R.string.enter_group_name))
            return
        }
        if (!checkMobileNumberValidation()) {
            showToastMsg(resources.getString(R.string.enter_valid_number))
            return
        }
        /*if (!checkAlterNateMobileNumberValidation()) {
            showToastMsg(resources.getString(R.string.please_fill_all_details))
            return
        }*/
        if (!checkLocationValidation()) {
            showToastMsg(resources.getString(R.string.enter_location))
            return
        }
        if (!checkPasswordValidation()) {
            showToastMsg(resources.getString(R.string.enter_password))
            return
        }
        if (!checkNomineeValidation()) {
            showToastMsg(resources.getString(R.string.enter_nominee_name))
            return
        }
//        if (!checkNomineeRelationValidation()) {
//            showToastMsg(resources.getString(R.string.enter_relation))
//            return
//        }
        if (!checkOccuptionValidation()) {
            showToastMsg(resources.getString(R.string.enter_occupation))
            return
        }
        if (!checkPrimaryAddressValidation()) {
            showToastMsg(resources.getString(R.string.enter_primary_address))
            return
        }
        if (!checkSecondaryAddressValidation()) {
            showToastMsg(resources.getString(R.string.enter_secondary_address))
            return
        }
        if (!binding.termscheck.isChecked) {
            showToastMsg(resources.getString(R.string.please_accecpt_terms))
            return
        }
        try {
            callRegisterApi()
        } catch (e: Exception) {
            showToastMsg(e.message)
            e.printStackTrace()
        }

    }


    //validations
    private fun checkFullNameValidation(): Boolean {
        return binding.fullNameET.editableText.toString().isNotEmpty()
    }
    private fun checkFatherNameValidation(): Boolean {
        return binding.fatherNameET.editableText.toString().isNotEmpty()
    }
    private fun checkDOBValidation(): Boolean {
        return binding.tvDOB.text.toString().isNotEmpty()
    }
    private fun checkAgeValidation(): Boolean {
        return binding.ageNameET.editableText.trim().toString().isNotEmpty()
    }
    private fun checkAadhaarValidation(): Boolean {
        return binding.aadhaarNameET.editableText.trim().toString().isNotEmpty()
    }
    private fun checkEmailValidation(): Boolean {
        val regex = Regex("^\\w+([.-]?\\w+)*@\\w+([.-]?\\w+)*(\\.\\w{2,})+\$")
        return regex.matches(binding.edEmail.editableText.trim().toString())
    }
    private fun checkCountryValidation(): Boolean {
        return binding.countryNameET.editableText.toString().isNotEmpty()
    }
    private fun checkCityValidation(): Boolean {
        return binding.cityNameET.editableText.toString().isNotEmpty()
    }
    private fun checkPinCodeValidation(): Boolean {
        return binding.pinCodeET.editableText.trim().toString().isNotEmpty()
    }
//    private fun checkPassBookNumberValidation(): Boolean {
//        return binding.passBookNumberET.editableText.trim().toString().isNotEmpty()
//    }
    private fun checkGroupNameValidation(): Boolean {
        return binding.groupNameET.editableText.toString().isNotEmpty()
    }
    private fun checkMobileNumberValidation(): Boolean {
        val mobilePattern = "^[6-9][0-9]{9}\$"

        // You can also use Patterns class for more comprehensive validation
        return Patterns.PHONE.matcher(binding.phoneNumberET.editableText.trim().toString())
            .matches() && binding.phoneNumberET.editableText.trim().toString()
            .matches(Regex(mobilePattern))
    }
    private fun checkAlterNateMobileNumberValidation(): Boolean {
        val mobilePattern = "^[6-9][0-9]{9}\$"

        // You can also use Patterns class for more comprehensive validation
        return Patterns.PHONE.matcher(binding.alternatephoneNumberET.editableText.trim().toString())
            .matches() && binding.alternatephoneNumberET.editableText.trim().toString()
            .matches(Regex(mobilePattern))
    }
    private fun checkLocationValidation(): Boolean {
        return binding.locationEd.editableText.toString().isNotEmpty()
    }
    private fun checkPasswordValidation(): Boolean {
        return binding.edPassword.editableText.trim().toString().isNotEmpty()
    }
    private fun checkNomineeValidation(): Boolean {
        return binding.nomineeNameET.editableText.toString().isNotEmpty()
    }
    private fun checkNomineeRelationValidation(): Boolean {
        return binding.nomineeRelationship.editableText.toString().isNotEmpty()
    }
    private fun checkOccuptionValidation(): Boolean {
        return binding.occuptionET.editableText.toString().isNotEmpty()
    }
    private fun checkPrimaryAddressValidation(): Boolean {
        return binding.primaryAddressET.editableText.toString().isNotEmpty()
    }
    private fun checkSecondaryAddressValidation(): Boolean {
        return binding.secondryAddressET.editableText.toString().isNotEmpty()
    }


    //upload image
    // Registers a photo picker activity launcher in single-select mode.
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->
        // Callback is invoked after the user selects a media item or closes the
        // photo picker.
        if (uri != null) {
            selectedImageFile = uriToFile(this@SignUpNewActivity,uri)

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

            //set name
            binding.txtfileName.setText(profile_image_name)

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

    //api run
    private fun callRegisterApi() {
        if (!this.isInternetAvailable()) {
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()
        val fullName = binding.fullNameET.editableText.toString()
        val mobileNumber = binding.phoneNumberET.editableText.trim().toString()
        val alternatemobileNumber = binding.alternatephoneNumberET.editableText.trim().toString()
        jsonObject.put("full_name", fullName)
        jsonObject.put("father_name", binding.fatherNameET.editableText.toString())
        jsonObject.put("age", binding.ageNameET.editableText.toString())
        jsonObject.put("aadhaar", binding.aadhaarNameET.editableText.trim().toString())
        jsonObject.put("email", binding.edEmail.editableText.trim().toString())
        //jsonObject.put("country", binding.countryNameET.editableText.toString())
        jsonObject.put("city", binding.cityNameET.editableText.toString())
        jsonObject.put("pincode", binding.pinCodeET.editableText.toString())
//        jsonObject.put("passbook_number", binding.passBookNumberET.editableText.toString())
       // jsonObject.put("passbook_number", "123456")
        jsonObject.put("group_name", binding.groupNameET.editableText.toString())
        jsonObject.put("mobile_number", mobileNumber)
        //jsonObject.put("alternate_mobile_number", alternatemobileNumber)
        jsonObject.put("location", binding.locationEd.editableText.toString())
        jsonObject.put("password", binding.edPassword.editableText.trim().toString())
        jsonObject.put("nominee_name", binding.nomineeNameET.editableText.toString())
        jsonObject.put("occupation", binding.occuptionET.editableText.toString())
        jsonObject.put("primary_address", binding.primaryAddressET.editableText.toString())
        jsonObject.put("secondary_address", binding.primaryAddressET.editableText.toString())
        jsonObject.put("secondary_address", binding.primaryAddressET.editableText.toString())
        jsonObject.put("image_name",profile_image_name)
        jsonObject.put("image_file",profile_image_file)
        jsonObject.put("dob",binding.tvDOB.text.toString())
        if (binding.empId.text.toString().equals("") || binding.empId.text.toString()==null){
            jsonObject.put("employee_id",0)
            Log.e("employee_id_","0")
        }else{
            jsonObject.put("employee_id",binding.empId.text.toString())
            Log.e("employee_id_","1")
        }

       // jsonObject.put("Relationship",binding.nomineeRelationship.text.toString())

        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.callRegisterApi(body)
        apiCall.enqueue(object : Callback<RegisterDataModel> {
            override fun onResponse(
                call: Call<RegisterDataModel>,
                response: Response<RegisterDataModel>
            ) {
                setDataToUI(response.body())
                CommonMethods.hideProgress()
            }
            override fun onFailure(call: Call<RegisterDataModel>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
            }
        })
    }

    private fun callGetEmpDetails() {
        if (!this.isInternetAvailable()) {
            showToastMsg(resources.getString(R.string.check_internet))
            return
        }
        CommonMethods.showProgress(this)
        val mediaType = "application/json".toMediaTypeOrNull()
        val jsonObject = JSONObject()

        jsonObject.put("employee_id",binding.empId.text.toString())

        val body = jsonObject.toString().toRequestBody(mediaType)
        val apiCall = RetrofitClient.api.getEmpDetails(body)
        apiCall.enqueue(object : Callback<EMPDetailsResponse> {
            override fun onResponse(
                call: Call<EMPDetailsResponse>,
                response: Response<EMPDetailsResponse>
            ) {
                if(response.body()?.status==true)
                {
                    if(response.body()?.data!=null) {
                        binding.lnrEmpDetails.visibility = View.VISIBLE
                        response.body()?.data.let {
                            binding.txtEmpid.text=it?.id
                            binding.txtName.text=it?.fullName
                            binding.txtDesignation.text=it?.designation
                        }

                    }
                }

                CommonMethods.hideProgress()
            }
            override fun onFailure(call: Call<EMPDetailsResponse>, t: Throwable) {
                showToastMsg("${t.message}")
                t.printStackTrace()
                CommonMethods.hideProgress()
                binding.lnrEmpDetails.visibility=View.VISIBLE
            }
        })
    }

    private fun setDataToUI(data: RegisterDataModel?) {
        if (data?.status == "200") {
            navigateToOTPScreen()
        }
        showToastMsg("${data?.message}")
    }
    private fun navigateToOTPScreen() {
        startActivity(Intent(this@SignUpNewActivity, LoginScreen::class.java))
        finishAffinity()
    }


    override fun onBackPressed() {
        startActivity(Intent(this@SignUpNewActivity, LoginScreen::class.java))
        finish()
        super.onBackPressed()
    }

}