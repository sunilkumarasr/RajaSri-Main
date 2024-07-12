package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName

data class PropertyDetailsDataModel (

    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : PropertyDetailsData?   =  null,

)
data class PropertyDetailsData (

    @SerializedName("property_details" ) var propertyDetails : ArrayList<PropertyDetails> = arrayListOf()

)
data class PropertyDetails (

    @SerializedName("id"              ) var id            : String? = null,
    @SerializedName("is_deleted"      ) var isDeleted     : String? = null,
    @SerializedName("customer_id"     ) var customerId    : String? = null,
    @SerializedName("role_id"         ) var roleId        : String? = null,
    @SerializedName("city_property_id" ) var city_property_id : String? = null,
    @SerializedName("created_user_id" ) var createdUserId : String? = null,
    @SerializedName("name"            ) var name          : String? = null,
    @SerializedName("location"        ) var location      : String? = null,
    @SerializedName("area"            ) var area          : String? = null,
    @SerializedName("area_name"            ) var area_name          : String? = null,
    @SerializedName("price"           ) var price         : String? = null,
    @SerializedName("description"     ) var description   : String? = null,
    @SerializedName("reg_date"        ) var regDate       : String? = null,
    @SerializedName("created_at"      ) var createdAt     : String? = null,
    @SerializedName("updated_at"      ) var updatedAt     : String? = null,
    @SerializedName("full_name"       ) var fullName      : String? = null,
    @SerializedName("emi_amount"      ) var emiAmount     : String? = null,
    @SerializedName("loan_id"         ) var loanId        : String? = null,
    @SerializedName("emi_date"        ) var emiDate       : String? = null,
    @SerializedName("emi_months"      ) var emiMonths     : String? = null,
    @SerializedName("loan_number"     ) var loanNumber    : String? = null,
    @SerializedName("status_id"     ) var status_id    : String? = null,
    @SerializedName("facing"     ) var facing    : String? = null,
    @SerializedName("paid_loan_amount"     ) var paid_loan_amount    : String? = null,
    @SerializedName("signature_img"     ) var signature_img    : String? = null,
    @SerializedName("agreement_file_full_path"     ) var agreement_file_full_path    : String? = null,
    @SerializedName("image"           ) var image         : String? = null

)
data class CityProperties (

    @SerializedName("id"                   ) var id                 : String?           = null,
    @SerializedName("city_id"              ) var cityId             : String?           = null,
    @SerializedName("name"                 ) var name               : String?           = null,
    @SerializedName("location"             ) var location           : String?           = null,
    @SerializedName("plot_number"          ) var plotNumber         : String?           = null,
    @SerializedName("amenities"            ) var amenities          : String?           = null,
    @SerializedName("description"          ) var description        : String?           = null,
    @SerializedName("terms_and_conditions" ) var termsAndConditions : String?           = null,
    @SerializedName("privacy_policy"       ) var privacyPolicy      : String?           = null,
    @SerializedName("brochure_full_path" ) var brochure_full_path           : String? = null,
    @SerializedName("brochure"            ) var brochure           : String? = null,
    @SerializedName("g_map_embed_url") var g_map_embed_url           : String? = null,

    @SerializedName("images"               ) var images             : ArrayList<Images> = arrayListOf(),
    @SerializedName("videos"               ) var videos             : ArrayList<video> = arrayListOf()

)
data class Images (

    @SerializedName("img_full_path"    ) var imgFullPath    : String? = null,
    @SerializedName("id"               ) var id             : String? = null,
    @SerializedName("city_property_id" ) var cityPropertyId : String? = null,
    @SerializedName("image"            ) var image          : String? = null,
    @SerializedName("created_at"       ) var createdAt      : String? = null,
    @SerializedName("updated_at"       ) var updatedAt      : String? = null

)

data class CityPropertyDetails (

    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : Data?   =  null,

)
data class Data (

    @SerializedName("city_properties" ) var cityProperties : ArrayList<CityProperties> = arrayListOf()

)
data class video (

    @SerializedName("video" ) var video : String?

)