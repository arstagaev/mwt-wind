package com.revolve44.postmakermassive.models.beta


import com.google.gson.annotations.SerializedName

data class FiveDaysForecastMain(
        @SerializedName("cod")
    val cod: String,
        @SerializedName("message")
    val message: Int,
        @SerializedName("cnt")
    val cnt: Int,
        @SerializedName("list")
    val list: List<ListWithCoreofForecast>,
        @SerializedName("city")
    val city: City
)