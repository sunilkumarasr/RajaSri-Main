package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName

data class Plot(
    @SerializedName("id"                   ) var id                 : String? = null,
    @SerializedName("property_id"          ) var propertyId         : String? = null,
    @SerializedName("land_face"            ) var landFace           : String? = null,
    @SerializedName("plot_num"             ) var plotNum            : String? = null,
    @SerializedName("bhk_no"               ) var bhkNo              : String? = null,
    @SerializedName("measurements"         ) var measurements       : String? = null,
    @SerializedName("plot_area"            ) var plotArea           : String? = null,
    @SerializedName("status"               ) var status             : String? = null,
    @SerializedName("total_amount"         ) var totalAmount        : String? = null,
    @SerializedName("approval_status"      ) var approvalStatus     : String? = null,
    @SerializedName("created_date"         ) var createdDate        : String? = null,
    @SerializedName("undivided_share_area" ) var undividedShareArea : String? = null
)
data class PlotResponse(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : ArrayList<Plot>?   = ArrayList<Plot>()
)


data class PlotRes(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : ArrayList<Plot>?   = ArrayList<Plot>()
)


data class EnquiryResponse(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,

)