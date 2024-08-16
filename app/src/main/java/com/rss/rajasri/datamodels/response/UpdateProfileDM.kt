package com.rss.rajasri.datamodels.response

import com.google.gson.annotations.SerializedName


data class UpdateProfileDM (

    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : UpdateProfileData?   = null

)
data class UpdateProfileData (

    @SerializedName("full_name"  ) var fullName  : String? = null,
    @SerializedName("email"      ) var email     : String? = null,
    @SerializedName("address"    ) var address   : String? = null,
    @SerializedName("updated_at" ) var updatedAt : String? = null

)
data class UploadProfileData (
    @SerializedName("profile_pic"  ) var profilePic  : String? = null,

)

data class UploadProfilePicDM(
    @SerializedName("status"  ) var status  : String? = null,
    @SerializedName("message" ) var message : String? = null,
    @SerializedName("data"    ) var data    : UploadProfileData?   = null
)

data class EMPDetailsResponse(
    @SerializedName("status"    ) var status   : Boolean?          = null,
    @SerializedName("message"   ) var message  : String?           = null,
    @SerializedName("data"      ) var data     : EMPDetails?             = null

)
data class EMPDetails(@SerializedName("id"            ) var id           : String? = null,
                      @SerializedName("employee_id"   ) var employeeId   : String? = null,
                      @SerializedName("status_id"     ) var statusId     : String? = null,
                      @SerializedName("shift_id"      ) var shiftId      : String? = null,
                      @SerializedName("is_deleted"    ) var isDeleted    : String? = null,
                      @SerializedName("login_otp"     ) var loginOtp     : String? = null,
                      @SerializedName("full_name"     ) var fullName     : String? = null,
                      @SerializedName("Relationship"  ) var Relationship : String? = null,
                      @SerializedName("company_name"  ) var companyName  : String? = null,
                      @SerializedName("email"         ) var email        : String? = null,
                      @SerializedName("mobile"        ) var mobile       : String? = null,
                      @SerializedName("gender"        ) var gender       : String? = null,
                      @SerializedName("dob"           ) var dob          : String? = null,
                      @SerializedName("address"       ) var address      : String? = null,
                      @SerializedName("location"      ) var location     : String? = null,
                      @SerializedName("designation"   ) var designation  : String? = null,
                      @SerializedName("aadhar_number" ) var aadharNumber : String? = null,
                      @SerializedName("username"      ) var username     : String? = null,
                      @SerializedName("password"      ) var password     : String? = null,
                      @SerializedName("created_at"    ) var createdAt    : String? = null,
                      @SerializedName("updated_at"    ) var updatedAt    : String? = null,
                      @SerializedName("profile_image" ) var profileImage : String? = null,
                      @SerializedName("id_card"       ) var idCard       : String? = null,
                      @SerializedName("joining_date"  ) var joiningDate  : String? = null,
                      @SerializedName("created_by"    ) var createdBy    : String? = null,
                      @SerializedName("token"         ) var token        : String? = null)