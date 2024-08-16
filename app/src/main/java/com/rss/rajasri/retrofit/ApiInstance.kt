package com.rss.rajasri.retrofit

import android.content.Intent
import android.util.Log
import androidx.core.content.ContextCompat.startActivity
import okhttp3.Interceptor
import okhttp3.Interceptor.*
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


object RetrofitClient {
    private val retrofit by lazy {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val builder = OkHttpClient.Builder().apply {
            readTimeout(180, TimeUnit.SECONDS)
            connectTimeout(180, TimeUnit.SECONDS)
            writeTimeout(180, TimeUnit.SECONDS)
        }.addInterceptor { chain ->
            chain.proceed(chain.request().newBuilder().apply {
                addHeader("api_key","Wr23u8O8Ud0bx55OYYHvDlCRrvGFcsbP7EibX1ndZV9v6VLXZT9Lb9ijpkTa02k7")
            }.url(chain.request().url).build())
        }.addInterceptor(logging)
            .addInterceptor(Interceptor { chain ->
                val request: Request = chain.request()
                val response = chain.proceed(request)
                // todo deal with the issues the way you need to
                if (response.code == 500) {
                    response.code==200
                    Log.e("response error","respinse error ${response.body.toString()} ")
                    return@Interceptor response
                }
                response
            })
        Retrofit.Builder()
            //.baseUrl("https://ritps.com/raja-township/api/v1/")//-->BASEURL
            .baseUrl("https://rajasreetownship.in/api/")//-->BASEURL
            .addConverterFactory(GsonConverterFactory.create())
            .client(builder.build())
            .build()
    }
    val api by lazy {
        retrofit.create(RemoteInterface::class.java)

    }

    var okHttpClient: OkHttpClient = OkHttpClient.Builder()
        .addInterceptor(Interceptor { chain ->
            val request: Request = chain.request()
            val response = chain.proceed(request)

            // todo deal with the issues the way you need to
            if (response.code == 500) {

                return@Interceptor response
            }
            response
        })
        .build()

}