package com.example.diningapp.data.network.response


import com.google.gson.annotations.SerializedName

data class IconX(
    val name: String,
    val prefix: String,
    val sizes: List<Int>
)