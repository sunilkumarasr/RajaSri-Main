package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName

data class GetCustomerDetailsDM(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : ArrayList<CustomerData>?   = null

)

data class CustomerData (

    @SerializedName("id"            ) var id           : String? = null,
    @SerializedName("full_name"     ) var fullName     : String? = null,
    @SerializedName("mobile_number" ) var mobileNumber : String? = null,
    @SerializedName("email" ) var email : String? = null,
    @SerializedName("address" ) var address : String? = "",
    @SerializedName("profile_pic" ) var profilePic : String? = "",
    @SerializedName("photo_full_path" ) var photo_full_path : String? = "",
    @SerializedName("photo" ) var photo : String? = ""

)