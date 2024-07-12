package com.rss.rajasri.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.rss.rajasri.R
import com.rss.rajasri.application.RajaSriApp
import com.rss.rajasri.databinding.FragmentAccountBinding
import com.rss.rajasri.datamodels.response.GetCustomerDetailsDM
import com.rss.rajasri.retrofit.RetrofitClient
import com.rss.rajasri.ui.LanguageSelectionScreen
import com.rss.rajasri.ui.activities.AboutUsActivity
import com.rss.rajasri.ui.activities.ContactUsActivity
import com.rss.rajasri.ui.activities.EditProfileActivity
import com.rss.rajasri.ui.activities.PrivacyPolicyActivity
import com.rss.rajasri.ui.activities.SupportActivity
import com.rss.rajasri.ui.activities.TermsAndConditionsActivity
import com.rss.rajasri.ui.activities.transaction_history.TransactionsHistoryActivity
import com.rss.rajasri.ui.authentication.LoginScreen
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


class AccountFragment : Fragment() {

    private lateinit var binding: FragmentAccountBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        binding = FragmentAccountBinding.inflate(inflater,container,false)


        binding.editIV.setOnClickListener {
            val intent = EditProfileActivity.newInstance(it.context){
                callGetCustomerDetailsApi()
            }
            startActivity(intent)
        }
        binding.logoutLL.setOnClickListener {
            showLogoutAlert()
        }
        binding.aboutLL.setOnClickListener {
            startActivity(Intent(it.context,AboutUsActivity::class.java))
        }
        binding.supportLL.setOnClickListener {
            startActivity(Intent(it.context,SupportActivity::class.java))
        }
        binding.contactLL.setOnClickListener {
            startActivity(Intent(it.context,ContactUsActivity::class.java))
        }

        binding.transactionsLL.setOnClickListener {
            startActivity(Intent(it.context, TransactionsHistoryActivity::class.java))
        }

        binding.privacyPolicyLL.setOnClickListener {
            startActivity(Intent(it.context, PrivacyPolicyActivity::class.java))
        }

        binding.termsConditionsLL.setOnClickListener {
            startActivity(Intent(it.context, TermsAndConditionsActivity::class.java))
        }

        binding.lnrLanguage.setOnClickListener {
            startActivity(Intent(it.context, LanguageSelectionScreen::class.java))
        }



        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        callGetCustomerDetailsApi()
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
                binding. userMailTV.text =  it.mobileNumber
                it.photo_full_path?.let { it1 -> RajaSriApp.appPreference.setProfilePath(it1) }
                Glide.with(binding.profileIV)
                    .load(it.photo_full_path)
                    .transform(CircleCrop())
                    .placeholder(R.drawable.account_circle_24)
                    .into(binding.profileIV)
                try{
                    ( requireActivity() as HomeActivity).setProfilePic()
                }catch (e:Exception)
                {
                    e.printStackTrace()
                }
            }
        }

    }

    private fun showLogoutAlert() {
        MaterialAlertDialogBuilder(binding.root.context)
            .setTitle(activity?.resources?.getString(R.string.logout))
            .setIcon(R.drawable.ic_profile_logout_png)
            .setMessage(activity?.resources?.getString(R.string.are_you_sure_logout))
            .setNegativeButton(activity?.resources?.getString(R.string.cancel)) { dialog, which ->
                // Respond to negative button press
            }
            .setPositiveButton(activity?.resources?.getString(R.string.yes)) { dialog, which ->
                RajaSriApp.appPreference.clear()
                startActivity(Intent(binding.root.context,LoginScreen::class.java))
                activity?.finishAffinity()
            }
            .show()
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) = AccountFragment().apply {}
    }
}