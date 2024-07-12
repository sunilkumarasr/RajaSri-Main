package com.rss.rajasri.preference

import android.content.Context
import android.content.SharedPreferences
import com.rss.rajasri.BuildConfig


class AppPreference(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences(BuildConfig.APPLICATION_ID, Context.MODE_PRIVATE)

    fun isLoggedIn(): Boolean {
        return sharedPreferences.getBoolean(IS_LOGGED_IN, false)
    }

    fun setLoggedIn(isLoggedIn: Boolean) {
        sharedPreferences.edit().putBoolean(IS_LOGGED_IN, isLoggedIn).apply()
    }
    fun getUserMobile(): String {
        return sharedPreferences.getString(USER_MOBILE_NUMBER,"").toString()
    }

    fun setUserMobileNumber(mobileNumber: String) {
        sharedPreferences.edit().putString(USER_MOBILE_NUMBER, mobileNumber).apply()
    }

    fun setUserEmailId(email_id: String) {
        sharedPreferences.edit().putString(USER_EMAIL_ID, email_id).apply()
    }



    fun setProfilePath(authToken:String) {
        return sharedPreferences.edit().putString(PROFILE_PIC_PATH, authToken).apply()
    }

    fun getProfilePath(): String {
        return sharedPreferences.getString(PROFILE_PIC_PATH, "").toString()
    }

    fun setAuthToken(authToken:String) {
        return sharedPreferences.edit().putString(AUTH_TOKEN, authToken).apply()
    }

    fun getAuthToken(): String {
        return sharedPreferences.getString(AUTH_TOKEN, "").toString()
    }


    fun setCustomerID(authToken:String) {
        return sharedPreferences.edit().putString(CUSTOMER_ID, authToken).apply()
    }

    fun getCustomerID(): String {
        return sharedPreferences.getString(CUSTOMER_ID, "").toString()
    }

    fun getEMailID(): String {
        return sharedPreferences.getString(USER_EMAIL_ID, "").toString()
    }


    fun setLanguage(lan:String) {
        return sharedPreferences.edit().putString(LANGUAGE, lan).apply()
    }

    fun getLanguage(): String {
        return sharedPreferences.getString(LANGUAGE, "en").toString()
    }

    fun clear(): String {
        return sharedPreferences.edit().clear().apply().toString()
    }

    companion object{
        const val IS_LOGGED_IN = "isLoggedIn"
        const val AUTH_TOKEN = "authToken"
        const val PROFILE_PIC_PATH = "profile_path"
        const val CUSTOMER_ID = "customer_id"
        const val LANGUAGE = "language"
        const val USER_MOBILE_NUMBER = "user_mobile_number"
        const val USER_EMAIL_ID = "user_email_id"
    }

}