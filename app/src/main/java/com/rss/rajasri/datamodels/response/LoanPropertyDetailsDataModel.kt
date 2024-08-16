package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName

data class LoanPropertyDetailsDataModel (

    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : LoanPropertyDetailsData?   =  null

)
data class LoanPropertyDetailsData (

    @SerializedName("property_loan_details" ) var propertyDetails : ArrayList<LoanPropertyDetails> = arrayListOf()

)
data class LoanPropertyDetails (

@SerializedName("id"                   ) var id                 : String? = null,
@SerializedName("customer_property_id" ) var customerPropertyId : String? = null,
@SerializedName("emi_amount"           ) var emiAmount          : String? = null,
@SerializedName("emi_date"             ) var emiDate            : String? = null,
@SerializedName("emi_months"           ) var emiMonths          : String? = null,
@SerializedName("loan_number"          ) var loanNumber         : String? = null,
@SerializedName("created_at"           ) var createdAt          : String? = null,
@SerializedName("updated_at"           ) var updatedAt          : String? = null,
@SerializedName("is_deleted"           ) var isDeleted          : String? = null,
@SerializedName("customer_id"          ) var customerId         : String? = null,
@SerializedName("role_id"              ) var roleId             : String? = null,
@SerializedName("created_user_id"      ) var createdUserId      : String? = null,
@SerializedName("name"                 ) var name               : String? = null,
@SerializedName("location"             ) var location           : String? = null,
@SerializedName("area"                 ) var area               : String? = null,
@SerializedName("area_name"                 ) var area_name               : String? = null,
@SerializedName("price"                ) var price              : String? = null,
@SerializedName("description"          ) var description        : String? = null,
@SerializedName("reg_date"             ) var regDate            : String? = null,
@SerializedName("property_loan_id"     ) var propertyLoanId     : String? = null,
@SerializedName("full_name"            ) var fullName     : String ="",
@SerializedName("image"            ) var image     : String? = null
)