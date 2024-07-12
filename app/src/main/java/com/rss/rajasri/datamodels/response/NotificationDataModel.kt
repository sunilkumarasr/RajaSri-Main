package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName

data class NotificationDataModel(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : NotificationData?   =  null
)

data class NotificationData (
    @SerializedName("notification" ) var notification : ArrayList<ListofNotifications> = arrayListOf()
)

data class ListofNotifications(
    @SerializedName("id") val id: String?,
    @SerializedName("title") val title: String?,
    @SerializedName("body") val body: String?,
    @SerializedName("type_id") val type_id: String?,
    @SerializedName("route_type") val route_type: String?,
    @SerializedName("created_at") val created_at: String?
)