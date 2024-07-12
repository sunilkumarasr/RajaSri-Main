package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName

data class RegisterDataModel(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : ArrayList<RegisterData>?   = null

)
class RegisterData