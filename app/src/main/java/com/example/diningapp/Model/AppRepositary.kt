package com.example.diningapp.Model

import android.content.Context
import com.example.diningapp.data.ApiService
import com.example.diningapp.data.CATEGORY_ID
import com.example.diningapp.data.CLIENT_ID
import com.example.diningapp.data.network.response.CurrentDiningResponse
import com.example.diningapp.data.SECRET_KEY
import com.resocoder.forecastmvvm.data.network.ConnectivityInterceptorImpl

class AppRepositary {
    suspend fun getData(v: String, ll: String,context: Context): CurrentDiningResponse {
        var isConnect = ConnectivityInterceptorImpl(context)
        val apiService = ApiService(isConnect).getData(CLIENT_ID, SECRET_KEY, v, ll,CATEGORY_ID).await()
        return apiService;
    }

}