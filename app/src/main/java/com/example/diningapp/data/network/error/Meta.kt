package com.example.diningapp.data.network.error


import com.google.gson.annotations.SerializedName

data class Meta(
    @SerializedName("code")
    val code: Int,
    @SerializedName("errorDetail")
    val errorDetail: String,
    @SerializedName("errorType")
    val errorType: String,
    @SerializedName("requestId")
    val requestId: String
)