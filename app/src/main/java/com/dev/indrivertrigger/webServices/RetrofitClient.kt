package com.dev.novisshop.network

import com.dev.indrivertrigger.Myapp.Companion.appContext
import com.dev.indrivertrigger.utils.ConstantValues
import com.dev.indrivertrigger.utils.PreferenceHelper
import com.dev.indrivertrigger.webServices.ApiConstant.BASE_URL
import com.dev.indrivertrigger.webServices.ApiInterface
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

class RetrofitClient(private val isHeader: Boolean) {

    init {
        val logging = HttpLoggingInterceptor()
        logging.setLevel(HttpLoggingInterceptor.Level.BODY)
        val httpClient: OkHttpClient.Builder = OkHttpClient.Builder()
            .callTimeout(200, TimeUnit.MINUTES)
            .connectTimeout(200, TimeUnit.SECONDS)
            .readTimeout(200, TimeUnit.SECONDS)
            .writeTimeout(200, TimeUnit.SECONDS)
            .addInterceptor(logging)
            .addInterceptor(ResponseInterceptor())

        httpClient.addInterceptor(Interceptor { chain ->
            val request: Request
            val token = PreferenceHelper.defaultPrefs(appContext).getString(ConstantValues.PREF_TOKEN, "") ?: ""
            println("access token $token")
            if (isHeader) {
                request = if (token.isNotEmpty()) {
                    chain.request().newBuilder()
                        .addHeader("Accept", "application/json")
                        .addHeader("Content-Type", "text/plain")
                        .addHeader("shop-access-token", token)
                        .build()
                } else {
                    chain.request().newBuilder()
                        .build()
                }
            } else {
                request = chain.request().newBuilder()
                    .addHeader("Accept", "application/json")
                    .addHeader("Content-Type", "text/plain")
                    .build()
            }
            chain.proceed(request)
        })

        retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .client(httpClient.build())
            .addConverterFactory(GsonConverterFactory.create())
            .build()

    }


    fun getApi(): ApiInterface {
        return retrofit?.create(ApiInterface::class.java)!!

    }

    class ResponseInterceptor : Interceptor {
        override fun intercept(chain: Interceptor.Chain): Response {
            val response = chain.proceed(chain.request())
            return response.newBuilder()
                .addHeader("Content-Type", "application/json; charset=utf-8")
                .build()
        }
    }

    companion object {
        private var retrofit: Retrofit? = null
    }
}