package com.vender98.aviasearch.data.network

import okhttp3.OkHttpClient
import java.security.cert.X509Certificate
import java.util.concurrent.TimeUnit
import javax.inject.Inject
import javax.inject.Provider
import javax.net.ssl.SSLContext
import javax.net.ssl.SSLSocketFactory
import javax.net.ssl.X509TrustManager

class OkHttpClientProvider @Inject constructor() : Provider<OkHttpClient> {

    override fun get(): OkHttpClient {
        val trustManagers = getTrustManagers()
        val sslSocketFactory = getSslSocketFactory(trustManagers)

        return OkHttpClient.Builder()
            .readTimeout(READ_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .connectTimeout(CONNECT_TIMEOUT_SECONDS, TimeUnit.SECONDS)
            .sslSocketFactory(sslSocketFactory, trustManagers[0])
            .build()
    }

    private fun getTrustManagers(): Array<X509TrustManager> {
        val trustAnyCertificatesManager = object : X509TrustManager {
            override fun checkClientTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun checkServerTrusted(
                chain: Array<out X509Certificate>?,
                authType: String?
            ) {
            }

            override fun getAcceptedIssuers() = arrayOf<X509Certificate>()
        }
        return arrayOf(trustAnyCertificatesManager)
    }

    private fun getSslSocketFactory(trustManagers: Array<X509TrustManager>?): SSLSocketFactory =
        SSLContext.getInstance("TLS")
            .apply { init(null, trustManagers, null) }
            .socketFactory

    companion object {
        private const val READ_TIMEOUT_SECONDS = 30L
        private const val CONNECT_TIMEOUT_SECONDS = 30L
    }
}