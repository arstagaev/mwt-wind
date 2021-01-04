package com.revolve44.mywindturbine.api

import com.revolve44.mywindturbine.models.ForecastPer3hr
import com.revolve44.mywindturbine.storage.PreferenceMaestro
import com.revolve44.mywindturbine.utils.Constants.Companion.API_KEY
import com.revolve44.postmakermassive.models.beta.FiveDaysForecastMain
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface WindAPI {

    @GET("data/2.5/forecast")
    suspend fun getWindRequestPer5days(
            @Query("lat")
        lat: Double = PreferenceMaestro.lat.toDouble(),
            @Query("lon")
        lon: Double = PreferenceMaestro.lon.toDouble(),
            @Query("cnt")
        cnt: Int = 40,
            @Query("appid")
        apiKey: String = API_KEY
    ) : Response<FiveDaysForecastMain>
}