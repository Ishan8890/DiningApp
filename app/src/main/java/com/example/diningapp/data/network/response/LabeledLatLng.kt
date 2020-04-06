package com.example.diningapp.data.network.response


import com.google.gson.annotations.SerializedName

data class LabeledLatLng(
    val label: String,
    val lat: Double,
    val lng: Double
)