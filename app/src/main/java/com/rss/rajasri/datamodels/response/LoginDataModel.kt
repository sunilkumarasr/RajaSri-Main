package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName

data class LoginDataModel(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : LoginData?   = null

)
data class LoginData (
    @SerializedName("customer_id" ) var customerId : String? = null,
    @SerializedName("otp_number" ) var otp_number : Int? = null
)