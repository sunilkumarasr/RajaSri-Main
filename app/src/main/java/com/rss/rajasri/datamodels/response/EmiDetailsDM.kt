package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName

data class EmiDetailsDM(
    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: EmiDetailsData? = null

)

data class EmiDetailsData(
    @SerializedName("emi_details") var emiDetails: ArrayList<EmiDetails> = arrayListOf()

)

data class EmiDetails(

    @SerializedName("id") var id: String? = null,
    @SerializedName("customer_property_loan_id") var customerPropertyLoanId: String? = null,
    @SerializedName("status_id") var statusId: String? = null,
    @SerializedName("transaction_id") var transactionId: String? = null,
    @SerializedName("emi_date") var emiDate: String? = null,
    @SerializedName("payment_date") var paymentDate: String? = null,
    @SerializedName("created_at") var createdAt: String? = null,
    @SerializedName("updated_at") var updatedAt: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("emi_price") var emiPrice: String? = null,
    @SerializedName("venture_name") var ventureName: String? = null,
    @SerializedName("property_name") var property_name: String? = null,
    @SerializedName("file") var file: String? = null,
    @SerializedName("emi_amount") var emi_amount: String? = null

)

data class DefaultDM(
    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: ArrayList<DefaultListResponse> = arrayListOf()

)

data class ContactDM(
    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data:ContactSData?=null

)

data class ContactSData(
    @SerializedName("contact") var contact: ArrayList<ContacttListResponse> = arrayListOf()

)

data class ContacttListResponse(

    @SerializedName("email") var email: String? = null,
    @SerializedName("mobile") var mobile: String? = null,
    @SerializedName("created_at") var created_at: String? = null,
    @SerializedName("updated_at") var updated_at: String? = null,
    @SerializedName("address") var address: String? = null


)

data class DefaultListResponse(

    @SerializedName("content") var content: String? = null


)

data class SupportRes(
    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,
    @SerializedName("data") var data: AboutUSRes?=null

)

data class AboutUSRes(
    @SerializedName("about") var about: ArrayList<ContentSupport>? = arrayListOf(),
    @SerializedName("privacy") var privacy: ArrayList<ContentSupport>? = arrayListOf(),
    @SerializedName("terms") var terms: ArrayList<ContentSupport>? = arrayListOf()

)

data class ContentSupport(
    @SerializedName("content") var content: String?=null

)

data class RequestSupport(
    @SerializedName("status") var status: String? = null,
    @SerializedName("message") var message: String? = null,

)