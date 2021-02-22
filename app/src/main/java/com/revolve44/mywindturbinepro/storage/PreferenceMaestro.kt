package com.revolve44.mywindturbinepro.storage

import android.content.Context
import android.content.SharedPreferences

object PreferenceMaestro {
    private const val NAME = "WindTurbinePRO"
    private const val MODE = Context.MODE_PRIVATE
    private lateinit var preferences: SharedPreferences

    // list of app specific preferences
    private val IS_FIRST_RUN_PREF = Pair("is_first_run", false)

    fun init(context: Context) {
        preferences = context.getSharedPreferences(NAME, MODE)
    }

    /**
     * SharedPreferences extension function, so we won't need to call edit() and apply()
     * ourselves on every SharedPreferences operation.
     */
    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = edit()
        operation(editor)
        editor.apply()
    }

    var isNightNode: Boolean
        get() = preferences.getBoolean("isNightNode", true)
        set(value) = preferences.edit {
            it.putBoolean("isNightNode", value)
        }

    //<<<<<<< HEAD
    var timeOfLastDataUpdate: String?
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString("lastupd", "offline")
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString("lastupd", value)
        }


    var chosenWindTurbineStartupWindSpeed: Int // in years
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getInt("solarPanelInstallationDate", 5)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putInt("solarPanelInstallationDate", value)
        }

    var chosenWindTurbineRotorDiameter: Int // in %
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getInt("solarPanelEfficiency", 100)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putInt("solarPanelEfficiency", value)
        }



    // max = 100, min = 0
    var calibrationCoeff: Int // 100 is 1.0 coeff
        get() = preferences.getInt("calibration", 100)
        set(value) = preferences.edit {
            it.putInt("calibration", value)
        }

    var lat: Float
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getFloat("lat", 0.0f)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putFloat("lat", value)
        }

    var lon: Float
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getFloat("lon", 0.0f)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putFloat("lon", value)
        }

    ////////////////////////////////////////////////////////////////////////for descriptions

    var leftChartMonthandDay: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString("leftchartd", "1").toString()
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString("leftchartd", value)
        }

    var rightChartMonthandDay: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString("rightchartd", "1").toString()
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString("rightchartd", value)
        }

    var fourChartMonthandDay: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString("thirdchartd", "1").toString()
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString("thirdchartd", value)
        }

    var fiveChartMonthandDay: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString("fourthchartd", "1").toString()
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString("fourthchartd", value)
        }
    ////////////////////////////////////////////////////////////////////////////////


    var averageForecastperThreeHours: Float
        get() = preferences.getFloat("avr", 1.0f)

        set(value) = preferences.edit {
            it.putFloat("avr", value)
        }

    var lastUpdateDate: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString("lstupd", "1").toString()
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString("lstupd", value)
//>>>>>>> experimental
        }

    var temp: Float
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getFloat("temp", 1f)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putFloat("temp", value)
        }

    var chosenStationNOMINALPOWER: Int
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getInt("nompower", 0)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putInt("nompower", value)
        }
    var chosenStationNAME: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString("name", "my PV station 1").toString()
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString("name", value)
        }
    var chosenStationLAT: Float
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getFloat("lat", 0.0f)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putFloat("lat", value)
        }

    var chosenStationLON: Float
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getFloat("lon", 0.0f)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putFloat("lon", value)
        }

    var chosenStationCITY: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString("city", "City not defined").toString()
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString("city", value)
        }

    var solarDayDuration: Int
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getInt("solarDay", 1)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putInt("solarDay", value)
        }

    var pickedColorofToolbarTitle: Int
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getInt("pickedColor", 0)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putInt("pickedColor", value)
        }

    var pickedColorofMainScreen: Int
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getInt("pickedColorofMainScreen", 0)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putInt("pickedColorofMainScreen", value)
        }


    var sunrise: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString("sunrise", "--:--").toString()
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString("sunrise", value)
        }



    var sunset: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString("sunset", "--:--").toString()
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString("sunset", value)
        }

    var firstStart: Boolean
        get() = preferences.getBoolean("firstStart", true)

        set(value) = preferences.edit {
            it.putBoolean("firstStart", value)
        }

    var forecastForNow: Float
        get() = preferences.getFloat("forecastForNow", 0.0f)
        set(value) = preferences.edit {
            it.putFloat("forecastForNow", value)
        }

    var chosenTimeZone: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString("timezone", "--:--").toString()
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString("timezone", value)
        }
    var chosenCurrency: String
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getString("currency", "$").toString()
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putString("currency", value)
        }

    //in US 13.19 cents per kilowatt hour (kWh)
    var pricePerkWh: Float
        // custom getter to get a preference of a desired type, with a predefined default value
        get() = preferences.getFloat("ppkWh", 1f)
        // custom setter to save a preference back to preferences file
        set(value) = preferences.edit {
            it.putFloat("ppkWh", value)
        }

    /////////// WindTurbine custom

    var radius : Float
        get() = preferences.getFloat("radius",0.5f )
        set(value) = preferences.edit {
            it.putFloat("radius", value)
        }

//    var wind: Float
//        // custom getter to get a preference of a desired type, with a predefined default value
//        get() = preferences.getFloat("wind", 1f)
//        // custom setter to save a preference back to preferences file
//        set(value) = preferences.edit {
//            it.putFloat("wind", value)
//        }

    var powerefficiency : Float // Betz Limit
        get() = preferences.getFloat("powerefficiency",0.5f )
        set(value) = preferences.edit {
            it.putFloat("powerefficiency", value)
        }

//    var maxWind: Float
//        // custom getter to get a preference of a desired type, with a predefined default value
//        get() = preferences.getFloat("ppkWh", 1f)
//        // custom setter to save a preference back to preferences file
//        set(value) = preferences.edit {
//            it.putFloat("ppkWh", value)
//        }

    ////////////////////////////////////////////////////////////////////////
    var timezoneL: Long
        get() = preferences.getLong("timezoneL", 0L)
        set(value) = preferences.edit {
            it.putLong("timezoneL", value)
        }

    var sunriseL: Long
        get() = preferences.getLong("sunriseL", 0L)
        set(value) = preferences.edit {
            it.putLong("sunriseL", value)
        }

    var sunsetL: Long
        get() = preferences.getLong("sunsetL", 0L)
        set(value) = preferences.edit {
            it.putLong("sunsetL", value)
        }














}