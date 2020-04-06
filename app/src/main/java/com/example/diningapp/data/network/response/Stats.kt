package com.example.diningapp.data.network.response


import com.google.gson.annotations.SerializedName

data class Stats(
    val checkinsCount: Int,
    val tipCount: Int,
    val usersCount: Int,
    val visitsCount: Int
)