package com.example.diningapp.data

import com.example.diningapp.data.network.response.CurrentDiningResponse
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.resocoder.forecastmvvm.data.network.ConnectivityInterceptor
import kotlinx.coroutines.Deferred
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query


const val CLIENT_ID = "1N5JJHPZXSRZE0GYXXMEYZ051VMHMU0KWVQ4CJKVMLHQMFG0"
const val SECRET_KEY = "ANG5OQH0YTWHM0JHWTVQW14V2O34WSICCIWYRMB4YOBK4RBN"
const val BASE_URL = "https://api.foursquare.com/v2/venues/"
const val V = "20130815"
const val CATEGORY_ID = "4d4b7105d754a06374d81259"

interface ApiService {

    @GET("search")
    fun getData(
        @Query("client_id") client_id: String,
        @Query("client_secret") client_secret: String,
        @Query("v") v: String,
        @Query("near") ll: String,
        @Query("categoryId") category: String,
        @Query("limit") limit: Int = 50
    ): Deferred<CurrentDiningResponse>

    companion object {
        operator fun invoke(
            connectivityInterceptor: ConnectivityInterceptor
        ): ApiService {
            val requestInterceptor = Interceptor { chain ->
                val url = chain.request()
                    .url()
                    .newBuilder()
                    .build()
                val request = chain.request()
                    .newBuilder()
                    .url(url)
                    .build()
                return@Interceptor chain.proceed(request)
            }
            val okHttp = OkHttpClient.Builder()
                .addInterceptor(requestInterceptor)
                .addInterceptor(connectivityInterceptor)
                .build()
            return Retrofit.Builder()
                .client(okHttp)
                .baseUrl(BASE_URL)
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addConverterFactory(GsonConverterFactory.create())
                .build()
                .create(ApiService::class.java)
        }
    }
}
