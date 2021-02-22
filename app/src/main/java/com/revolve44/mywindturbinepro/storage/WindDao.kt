package com.revolve44.mywindturbinepro.storage

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.revolve44.mywindturbinepro.models.ForecastPer3hr

@Dao
interface WindDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun saveForecastPer3hr(forecastPer3hr: ForecastPer3hr)

    @Query("DELETE FROM forecastPer3hr")
    suspend fun deleteAllinForecast()

    @Query("SELECT * FROM forecastPer3hr")
    fun getALLForecastsPer5days(): LiveData<List<ForecastPer3hr>>


}