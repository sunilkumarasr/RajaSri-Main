package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName

data class EmiItemDataModel(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : EmiItemData?   = null

)


data class EmiItemData (

    @SerializedName("emi_details" ) var emiDetails : ArrayList<EmiItemDetails> = arrayListOf()

)

data class EmiItemDetails (

    @SerializedName("id"                        ) var id                     : String? = null,
    @SerializedName("customer_property_loan_id" ) var customerPropertyLoanId : String? = null,
    @SerializedName("status_id"                 ) var statusId               : String? = null,
    @SerializedName("transaction_id"            ) var transactionId          : String? = null,
    @SerializedName("emi_date"                  ) var emiDate                : String? = null,
    @SerializedName("payment_date"              ) var paymentDate            : String? = null,
    @SerializedName("created_at"                ) var createdAt              : String? = null,
    @SerializedName("updated_at"                ) var updatedAt              : String? = null,
    @SerializedName("status"                    ) var status                 : String? = null,
    @SerializedName("customer_property_id"      ) var customerPropertyId     : String? = null,
    @SerializedName("emi_amount"                ) var emiAmount              : String? = null,
    @SerializedName("emi_months"                ) var emiMonths              : String? = null,
    @SerializedName("loan_number"               ) var loanNumber             : String? = null,
    @SerializedName("is_deleted"                ) var isDeleted              : String? = null,
    @SerializedName("customer_id"               ) var customerId             : String? = null,
    @SerializedName("role_id"                   ) var roleId                 : String? = null,
    @SerializedName("created_user_id"           ) var createdUserId          : String? = null,
    @SerializedName("name"                      ) var name                   : String? = null,
    @SerializedName("location"                  ) var location               : String? = null,
    @SerializedName("area"                      ) var area                   : String? = null,
    @SerializedName("price"                     ) var price                  : String? = null,
    @SerializedName("description"               ) var description            : String? = null,
    @SerializedName("reg_date"                  ) var regDate                : String? = null,
    @SerializedName("full_name"                  ) var fullName                : String? = ""

)