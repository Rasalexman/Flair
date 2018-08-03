package com.mincor.flair.utils

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.provider.Settings
import com.google.gson.GsonBuilder
import com.ihsanbal.logging.Level
import com.ihsanbal.logging.LoggingInterceptor
import com.mincor.flair.BuildConfig
import okhttp3.OkHttpClient
import okhttp3.internal.platform.Platform
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


/**
 * Created by a.minkin on 25.10.2017.
 */

object NetModule {
    fun isNetworkAvailable(cm: ConnectivityManager?): Boolean {
        cm?.let {
            val activeNetwork: NetworkInfo? = cm.activeNetworkInfo
            return (activeNetwork?.let {
                it.isConnected && it.isConnectedOrConnecting && it.isAvailable
            } ?: false)
        }
        return false
    }
}

@SuppressLint("HardwareIds")
fun takeDeviceID(contentResolver: ContentResolver): String = Settings.Secure.getString(contentResolver, Settings.Secure.ANDROID_ID)

fun createOkHttpClient(cm: ConnectivityManager?, deviceId: String): OkHttpClient {
    val httpClient = OkHttpClient.Builder()
    val logging = LoggingInterceptor.Builder()
            .loggable(BuildConfig.DEBUG)
            .setLevel(Level.HEADERS)
            .log(Platform.INFO)
            .request("Request")
            .response("Response")
            .addHeader("version", BuildConfig.VERSION_NAME).build()

    // add logging as last interceptor
    httpClient.addInterceptor(logging)  // <-- this is the important line!

    httpClient.addInterceptor { chain ->
        var request = chain.request()
        request = if (NetModule.isNetworkAvailable(cm)) {
            // if there is connectivity, we tell the request it can reuse the data for sixty seconds.
            request.newBuilder()
                    .removeHeader("Pragma")
                    .addHeader("Cache-Control", "public, max-age=10000")
                    .addHeader("Device-Id", deviceId)               //
                    .addHeader("Platform", "Android")         //
                    .addHeader("Connection", "close")
                    .build()

        } else {
            // If there’s no connectivity, we ask to be given only (only-if-cached) ‘stale’ data upto 7 days ago
            request.newBuilder().removeHeader("Pragma").addHeader("Cache-Control", "public, only-if-cached, max-stale=10000").build()
        }
        chain.proceed(request)
    }

    httpClient.addNetworkInterceptor { chain ->
        val originalResponse = chain.proceed(chain.request())
        // если получили неизмененный ответ прост овыходим и не запариваемся
        if(originalResponse.code() == 304) return@addNetworkInterceptor originalResponse

        val cacheControl = originalResponse.header("Cache-Control")

        return@addNetworkInterceptor if (cacheControl == null || cacheControl.contains("no-store") || cacheControl.contains("no-cache") ||
                cacheControl.contains("must-revalidate") || cacheControl.contains("max-age=0")) {
            originalResponse.newBuilder()
                    .removeHeader("Pragma")
                    .addHeader("Cache-Control", "public, max-age=600000")
                    .addHeader("Device-Id", deviceId)
                    .addHeader("Platform", "Android")
                    .addHeader("Connection", "close")
                    .build()
        } else {
            originalResponse
        }
    }

    return httpClient.build()
}

inline fun <reified F> createWebServiceApi(okHttpClient: OkHttpClient, url: String): F {
    val gsonInstance = GsonBuilder().create()
    val retrofit = Retrofit.Builder()
            .baseUrl(url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create(gsonInstance))
            .build()
    return retrofit.create(F::class.java)
}
