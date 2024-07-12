package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName

data class OTPDataModel(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : OTPData?   = null

)
data class OTPData(
    @SerializedName("code" ) var code : Int? = null,
)