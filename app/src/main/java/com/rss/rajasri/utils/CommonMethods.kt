package com.rss.rajasri.utils

import android.annotation.SuppressLint
import android.app.ProgressDialog
import android.content.Context
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.Toast
import com.rss.rajasri.R

object CommonMethods {

    private var mProgressDialog: ProgressDialog? = null
    @SuppressLint("ResourceAsColor")
    fun showProgress(mContext: Context) {
        if (mProgressDialog == null) {

            mProgressDialog = ProgressDialog(mContext, R.style.Theme_RajaSri)
            if (mProgressDialog?.window != null) mProgressDialog?.window!!
                .setBackgroundDrawable(
                    ColorDrawable(Color.parseColor("#01000000"))
                )
            val layoutParams = WindowManager.LayoutParams()
            layoutParams.width = LinearLayout.LayoutParams.MATCH_PARENT
            layoutParams.height = LinearLayout.LayoutParams.MATCH_PARENT

            mProgressDialog?.window?.attributes = layoutParams
            mProgressDialog?.isIndeterminate = true
            mProgressDialog?.setCancelable(false)
            mProgressDialog?.setCanceledOnTouchOutside(false)
            mProgressDialog?.show()
            mProgressDialog?.setContentView(R.layout.layout_loader_progress_bar)

            mProgressDialog?.setOnDismissListener {
            }
        }

        if (!mProgressDialog!!.isShowing) {
            mProgressDialog?.show()
        }

    }
    fun hideProgress() {

        if (mProgressDialog != null && mProgressDialog!!.isShowing) {
            mProgressDialog?.dismiss()
        }
        mProgressDialog = null
    }



    fun Context.isInternetAvailable(): Boolean {
        val connectivityManager =
            getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val networkCapabilities = connectivityManager.activeNetwork ?: return false
        val actNw =
            connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
        val result = when {
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
            actNw.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
            else -> false
        }
        return result
    }


}

fun Context.showToastMsg(string: String?){
    Toast.makeText(this,string,Toast.LENGTH_SHORT).show()
}

fun View.hide(){
    this.visibility = View.GONE
}
fun View.show(){
    this.visibility = View.VISIBLE
}
fun View.invisible(){
    this.visibility = View.INVISIBLE
}