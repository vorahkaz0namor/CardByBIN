package ru.cft.cardbybin.util

import android.util.Log
import okhttp3.OkHttpClient
import java.security.SecureRandom
import java.security.cert.X509Certificate
import javax.net.ssl.*

object UnsafeOkHttpClient {
    fun getUnsafeOkHttpClient(): OkHttpClient =
        OkHttpClient.Builder().apply {
            try {
                /** Trust manager, that doesn't validate certificate chains */
                val trustAllCerts: Array<TrustManager> = arrayOf(
                    object : X509TrustManager {
                        override fun checkClientTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
                        override fun checkServerTrusted(p0: Array<out X509Certificate>?, p1: String?) {}
                        override fun getAcceptedIssuers(): Array<X509Certificate> = emptyArray()
                    }
                )
                // Install the all-trusting manager
                val sslContext = SSLContext.getInstance("SSL")
                sslContext.init(
                    /* km = */ null,
                    /* tm = */ trustAllCerts,
                    /* random = */ SecureRandom()
                )
                /** SSL socket factory with all-trusting manager */
                val sslSocketFactory = sslContext.socketFactory
                sslSocketFactory(
                    sslSocketFactory = sslSocketFactory,
                    trustManager = trustAllCerts.first() as X509TrustManager
                )
                    .hostnameVerifier { _, _ -> true }
            } catch (e: Exception) {
                Log.d("UNSAFE EXCEPTION", e.toString())
            }
        }.build()
}