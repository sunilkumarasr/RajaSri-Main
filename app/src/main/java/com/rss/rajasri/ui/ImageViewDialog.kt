package com.royalit.sudhakarpastor.fragment

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.ScaleGestureDetector
import android.view.View
import android.view.ViewGroup
import android.webkit.WebView
import android.widget.ImageView
import androidx.fragment.app.DialogFragment
import com.rss.rajasri.R
import kotlin.math.max
import kotlin.math.min

class ImageViewDialog (val img:String) : DialogFragment() {
    private var mScaleFactor = 1.0f
    private lateinit var scaleGestureDetector: ScaleGestureDetector

    private inner class ScaleListener(val mImageView: ImageView) : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(scaleGestureDetector: ScaleGestureDetector): Boolean {
            mScaleFactor *= scaleGestureDetector.scaleFactor
            mScaleFactor = max(0.1f, min(mScaleFactor, 10.0f))
            mImageView.scaleX = mScaleFactor
            mImageView.scaleY = mScaleFactor
            return true
        }
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        //setWidthPercent(85)
        dialog?.window?.setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)

    }
    /* fun DialogFragment.setWidthPercent(percentage: Int) {
         val percent = percentage.toFloat() / 100
         val dm = Resources.getSystem().displayMetrics
         val rect = dm.run { Rect(0, 0, widthPixels, heightPixels) }
         val percentWidth = rect.width() * percent
         dialog?.window?.setLayout(percentWidth.toInt(), ViewGroup.LayoutParams.WRAP_CONTENT)
     }*/
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val view= LayoutInflater.from(context).inflate(R.layout.zoom_dialog_item,container,true)

        val closeIV = view?.findViewById(R.id.closeIV) as ImageView
        val imageView = view?.findViewById(R.id.imageView) as ImageView
        scaleGestureDetector = ScaleGestureDetector(requireContext(), ScaleListener(imageView))

        val webview = view?.findViewById(R.id.webView1) as WebView


        webview.clearCache(true)
        webview.settings.javaScriptEnabled = true
        webview.settings.setUseWideViewPort(true);
        webview.settings.displayZoomControls=true
        webview.settings.builtInZoomControls=true
        var data =
            ""
        data = "<img width=\"600\" src=\"$img\" />"
        webview.loadData(data, "text/html", null)

        // webview.settings.setSupportZoom(true)
        //webview.loadUrl(img)
        /* imageView.setOnTouchListener(object:OnTouchListener{
             override fun onTouch(p0: View?, motionEvent: MotionEvent?): Boolean {
                 scaleGestureDetector.onTouchEvent(motionEvent!!)
                 return true
             }

         })*/

        Log.e("img","Image Path $img")
        /* Glide.with(requireActivity()).load(img)

             .error(R.drawable.img)
             .into(imageView)
         */

        closeIV.setOnClickListener {
            dialog?.dismiss()
        }
        return view
    }



}