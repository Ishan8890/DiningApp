package com.example.diningapp.data.network.error


import com.google.gson.annotations.SerializedName

data class failure(
    @SerializedName("meta")
    val meta: Meta,
    @SerializedName("response")
    val response: Response
)