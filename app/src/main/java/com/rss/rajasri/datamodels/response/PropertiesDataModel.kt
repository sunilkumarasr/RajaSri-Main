package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName

data class PropertiesDataModel (

    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : PropertiesData?   =  null

)
data class PropertiesData (

    @SerializedName("all_properties" ) var allProperties : ArrayList<AllProperties> = arrayListOf(),
    @SerializedName("city_properties" ) var city_properties : ArrayList<CityProperty> = arrayListOf(),
    @SerializedName("pending_properties" ) var pendingProperties : ArrayList<PendingProperty> = arrayListOf()

)
data class AllProperties (

    @SerializedName("id"              ) var id            : String? = null,
    @SerializedName("is_deleted"      ) var isDeleted     : String? = null,
    @SerializedName("customer_id"     ) var customerId    : String? = null,
    @SerializedName("city_property_id"     ) var city_property_id    : String? = null,
    @SerializedName("role_id"         ) var roleId        : String? = null,
    @SerializedName("created_user_id" ) var createdUserId : String? = null,
    @SerializedName("name"            ) var name          : String? = null,
    @SerializedName("location"        ) var location      : String? = null,
    @SerializedName("area"            ) var area          : String? = null,
    @SerializedName("area_name"       ) var area_name          : String? = null,
    @SerializedName("price"           ) var price         : String? = null,
    @SerializedName("description"     ) var description   : String? = null,
    @SerializedName("reg_date"        ) var regDate       : String? = null,
    @SerializedName("created_at"      ) var createdAt     : String? = null,
    @SerializedName("updated_at"      ) var updatedAt     : String? = null,
    @SerializedName("status_id"      ) var status_id     : String? = null,
    @SerializedName("signature_img"      ) var signature_img     : String? = null,
    @SerializedName("agreement_file"      ) var agreement_file     : String? = null,
    @SerializedName("agreement_file_full_path"      ) var agreement_file_full_path     : String? = null,
    @SerializedName("image"           ) var image         : String? = null

)

data class PendingProperty (
    @SerializedName("id"              ) var id            : String? = null,
    @SerializedName("city_id"      ) var city_id     : String? = null,
    @SerializedName("role_id"         ) var roleId        : String? = null,
    @SerializedName("amenities" ) var amenities : String? = null,
    @SerializedName("name"            ) var name          : String? = null,
    @SerializedName("location"        ) var location      : String? = null,
    @SerializedName("terms_and_conditions"        ) var terms_and_conditions      : String? = null,
    @SerializedName("area"            ) var area          : String? = null,
    @SerializedName("area_name"            ) var area_name          : String? = null,
    @SerializedName("price"           ) var price         : String? = null,
    @SerializedName("description"     ) var description   : String? = null,
    @SerializedName("reg_date"        ) var regDate       : String? = null,
    @SerializedName("created_at"      ) var createdAt     : String? = null,
    @SerializedName("updated_at"      ) var updatedAt     : String? = null,
    @SerializedName("image"           ) var image        : String? = null
)

data class CityProperty (
    @SerializedName("id"                   ) var id                 : String? = null,
    @SerializedName("city_id"              ) var cityId             : String? = null,
    @SerializedName("name"                 ) var name               : String? = null,
    @SerializedName("location"             ) var location           : String? = null,
    @SerializedName("plot_number"          ) var plotNumber         : String? = null,
    @SerializedName("amenities"            ) var amenities          : String? = null,
    @SerializedName("description"          ) var description        : String? = null,
    @SerializedName("terms_and_conditions" ) var termsAndConditions : String? = null,
    @SerializedName("privacy_policy"       ) var privacyPolicy      : String? = null,
    @SerializedName("city_name"            ) var cityName           : String? = null,
    @SerializedName("brochure"            ) var brochure           : String? = null,
    @SerializedName("city_property_id"            ) var city_property_id           : String? = null,
    @SerializedName("brochure_full_path"            ) var brochure_full_path           : String? = null,
    @SerializedName("city_property_name"            ) var city_property_name           : String? = null,
    @SerializedName("image"            ) var image           : String? = null
/*
    @SerializedName("id"              ) var id            : String? = null,
    @SerializedName("city_id"      ) var city_id     : String? = null,
    @SerializedName("role_id"         ) var roleId        : String? = null,
    @SerializedName("amenities" ) var amenities : String? = null,
    @SerializedName("name"            ) var name          : String? = null,
    @SerializedName("location"        ) var location      : String? = null,
    @SerializedName("terms_and_conditions"        ) var terms_and_conditions      : String? = null,
    @SerializedName("area"            ) var area          : String? = null,
    @SerializedName("price"           ) var price         : String? = null,
    @SerializedName("description"     ) var description   : String? = null,
    @SerializedName("reg_date"        ) var regDate       : String? = null,
    @SerializedName("created_at"      ) var createdAt     : String? = null,
    @SerializedName("updated_at"      ) var updatedAt     : String? = null,
    @SerializedName("image"           ) var image        : String? = null*/

)