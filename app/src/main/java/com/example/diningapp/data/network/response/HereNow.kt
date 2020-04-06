package com.example.diningapp.data.network.response


import com.google.gson.annotations.SerializedName

data class HereNow(
    val count: Int,
    val groups: List<Any>,
    val summary: String
)