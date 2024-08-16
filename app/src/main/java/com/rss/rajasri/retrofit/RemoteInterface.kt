package com.rss.rajasri.retrofit

import com.rss.rajasri.datamodels.response.CityPropertyDetails
import com.rss.rajasri.datamodels.response.ContactDM
import com.rss.rajasri.datamodels.response.EMPDetailsResponse
import com.rss.rajasri.datamodels.response.EmiDetailsDM
import com.rss.rajasri.datamodels.response.EmiItemDataModel
import com.rss.rajasri.datamodels.response.EnquiryResponse
import com.rss.rajasri.datamodels.response.GetCustomerDetailsDM
import com.rss.rajasri.datamodels.response.LoanPropertyDetailsDataModel
import com.rss.rajasri.datamodels.response.LoginDataModel
import com.rss.rajasri.datamodels.response.NotificationDataModel
import com.rss.rajasri.datamodels.response.OTPDataModel
import com.rss.rajasri.datamodels.response.PendingInvoicesDataModel
import com.rss.rajasri.datamodels.response.PlotResponse
import com.rss.rajasri.datamodels.response.PropertiesDataModel
import com.rss.rajasri.datamodels.response.PropertyDetailsDataModel
import com.rss.rajasri.datamodels.response.RegisterDataModel
import com.rss.rajasri.datamodels.response.RequestSupport
import com.rss.rajasri.datamodels.response.SupportRes
import com.rss.rajasri.datamodels.response.UpdateProfileDM
import com.rss.rajasri.datamodels.response.UploadProfilePicDM
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface RemoteInterface {


    @POST("v2/customers/login")
    fun callLoginApi(
        @Body body: RequestBody,
    ): Call<LoginDataModel>

    @POST("v1/customers/otp/verify")
    fun callOTPApi(
        @Body body: RequestBody,
    ): Call<OTPDataModel>

    @POST("v1/customers/account/register")
    fun callRegisterApi(
        @Body body: RequestBody,
    ): Call<RegisterDataModel>


    @POST("v1/customers/account/profile/update")
    fun callUpdateApi(
        @Body body: RequestBody,
    ): Call<UpdateProfileDM>

    @POST("v1/customers/account/profile/updateprofilepic")
    @Multipart
    fun callUploadProfileApi(
        @Part body: MultipartBody.Part,
        @Part id:MultipartBody.Part
    ): Call<UploadProfilePicDM>

    @POST("v1/customers/account/profile")
    fun callCustomerDetailsApi(
        @Body body: RequestBody,
    ): Call<GetCustomerDetailsDM>

    @POST("v1/customers/properties")
    fun callCustomerPropertiesApi(
        @Body body: RequestBody,
    ): Call<PropertiesDataModel>

    @POST("v1/customers/property/details")
    fun callCustomerPropertyDetailsApi(
        @Body body: RequestBody,
    ): Call<PropertyDetailsDataModel>

    @POST("v1/customers/property/loan/details")
    fun callLoanPropertyDetailsApi(
        @Body body: RequestBody,
    ): Call<LoanPropertyDetailsDataModel>

    @POST("v1/customers/properties/emis/list")
    fun callEmiDetailsApi(
        @Body body: RequestBody,
    ): Call<EmiDetailsDM>

    @POST("v1/customers/properties/emis/list")
    fun callEmiCustomerApi(
        @Body body: RequestBody,
    ): Call<EmiDetailsDM>


    @POST("v1/customers/properties/emis/details")
    fun callEmiItemDetailsApi(
        @Body body: RequestBody,
    ): Call<EmiItemDataModel>


    @POST("v1/customers/properties/emis/list-by-customer")
    fun callBiilingPaidUnpaid(
        @Body body: RequestBody,
    ): Call<EmiDetailsDM>

    @POST("v1/customers/properties/emis/list-by-customer-yearmonth")
    fun callBiilingPaidUnpaidByYearMonth(
        @Body body: RequestBody,
    ): Call<EmiDetailsDM>

    @POST("v1/privacy.cms")
    fun callPrivacyApi(
    ): Call<SupportRes>

    @POST("v1/terms.cms")
    fun callTermsApi(
    ): Call<SupportRes>

    @POST("v1/about.cms")
    fun callAboutApi(
    ): Call<SupportRes>
   @POST("v1/contact.cms")
    fun callContactApi(
    ): Call<ContactDM>

    companion object{
        const val API_CONSTANT = "api"
    }


    @POST("v2/city-properties")
    fun callAvailableCustomerPropertiesApi(
        @Body body: RequestBody,
    ): Call<PropertiesDataModel>
 @POST("v1/support/request")
    fun submitSupport(
        @Body body: RequestBody,
    ): Call<RequestSupport>


    @POST("v2/customers/properties/list/pending")
    fun pendingProperties(
        @Body body: RequestBody,
    ): Call<PropertiesDataModel>

    @POST("v2/customers/properties/pending-invoices")
    fun pendingInvoices(
        @Body body: RequestBody,
    ): Call<PendingInvoicesDataModel>


    @POST("v2/customers/properties/signature/sign")
    fun submitAgreementSign(
        @Body body: RequestBody,
    ): Call<Any>

    @POST("v2/city-property/details")
    fun cityPropertyDetails(
        @Body body: RequestBody,
    ): Call<CityPropertyDetails>


    @POST("v1/customers/firebase/token/update")
    fun updateToken(
        @Body body: RequestBody,
    ): Call<Any>

    @POST("v2/customers/properties/invoice/gateway-status/update")
    fun invoicepaymentSuccess(
        @Body body: RequestBody,
    ): Call<Any>

    @POST("v1/customers/properties/emis/update-status")
    fun emipaymentSuccess(
        @Body body: RequestBody,
    ): Call<Any>

    @POST("v1/notification-list")
    fun notificationsApi(
        @Body body: RequestBody,
    ): Call<NotificationDataModel>

    @POST("v2/customers/properties/plots")
    fun getPlots(
        @Body body: RequestBody,
    ): Call<PlotResponse>


    @POST("v2/customers/properties/plots_enquiry")
    fun sendEnquiry(
        @Body body: RequestBody,
    ): Call<EnquiryResponse>


    @POST("v2/customers/properties/pay_advance_and_fee")
    fun payAdvance(
        @Body body: RequestBody,
    ): Call<Any>


    @POST("customers/ProfileController/get_employee")
    fun getEmpDetails(
        @Body body: RequestBody,
    ): Call<EMPDetailsResponse>
}


