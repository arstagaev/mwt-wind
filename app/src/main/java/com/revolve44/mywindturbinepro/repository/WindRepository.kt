package com.revolve44.mywindturbinepro.repository

import android.app.Application
import com.revolve44.mywindturbinepro.api.RetrofitInstance
import com.revolve44.mywindturbinepro.models.ForecastPer3hr
import com.revolve44.mywindturbinepro.storage.WindDatabase

class WindRepository (app : Application) {
    val db : WindDatabase = WindDatabase.getInstance(app)
    val dao = db.windDao

    /////////////////////////////API/////////////////////////////

    suspend fun getRequestFor5days() =
        RetrofitInstance.apiWindTurbine.getWindRequestPer5days()

    ////////////////////////////Database/////////////////////////

    fun getAllForecastFrom5days() = db.windDao.getALLForecastsPer5days()

    suspend fun saveForecastPer5days(forecastPer3hr: ForecastPer3hr) =
        db.windDao.saveForecastPer3hr(forecastPer3hr)

    suspend fun deleteAllElementsFromTable() = db.windDao.deleteAllinForecast()


}