package com.sevenpeakssoftware.base.network

import okhttp3.Interceptor
import okhttp3.Response
import java.io.IOException

class UserInterceptor : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val userAgentRequest = chain.request()
            .newBuilder()
            .addHeader(CONTENT_TYPE, APPLICATION_JSON)
            .addHeader(ACCEPT_VERSION, "v1")
            //
            .build()
        return chain.proceed(userAgentRequest)
    }

    companion object{
        private const val CONTENT_TYPE = "Content-Type"
        private const val APPLICATION_JSON = "application/json"
        private const val ACCEPT_VERSION = "Accept-Version"
    }
}
