package com.revolve44.mywindturbine.models

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity( tableName = "forecastPer3hr")
class ForecastPer3hr (
        @PrimaryKey(autoGenerate = false)
        val unixTime: Long,
        val day: Int,
        val humanTime: String,
        val forecast: Int,
        val windSpeed: Float,
        val windDirection: Int
    )