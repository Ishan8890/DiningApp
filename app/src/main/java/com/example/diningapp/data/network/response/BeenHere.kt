package com.example.diningapp.data.network.response


import com.google.gson.annotations.SerializedName

data class BeenHere(
    val count: Int,
    val lastCheckinExpiredAt: Int,
    val marked: Boolean,
    val unconfirmedCount: Int
)