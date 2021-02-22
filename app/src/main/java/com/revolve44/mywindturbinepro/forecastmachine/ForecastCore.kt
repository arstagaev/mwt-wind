package com.revolve44.mywindturbinepro.forecastmachine

import com.revolve44.mywindturbinepro.storage.PreferenceMaestro
import com.revolve44.mywindturbinepro.utils.TypeOfSky
import com.revolve44.mywindturbinepro.utils.roundTo2decimials
import com.revolve44.mywindturbinepro.utils.sqrtCustom
import timber.log.Timber
import kotlin.math.PI
import kotlin.math.pow

fun getForecast(windSpeed: Float,timeZone: Long,timestamp: Long): Int {
    var curpowx =
            (((1.23 * PI * (PreferenceMaestro.radius).pow(2) * ((windSpeed).pow(3)) * 1 ) / 2).toFloat())

    if(curpowx>PreferenceMaestro.chosenStationNOMINALPOWER){
        curpowx = PreferenceMaestro.chosenStationNOMINALPOWER.toFloat()
    }

    return curpowx.toInt()
}

fun getAverageNumOfArray(array: ArrayList<Float>) : Float {

    return  (array.sum()/array.size).toFloat()

}

fun ForecastToWindSpeed(forecast: Float) : Float{
    return roundTo2decimials(
            sqrtCustom((2f*forecast)/(1.23*PI*(PreferenceMaestro.radius).pow(2)*0.5).toFloat(),3))
}

fun defineTimeOfDay() : TypeOfSky{
    var typeOfSky: TypeOfSky = TypeOfSky.DAY

    var timezone = PreferenceMaestro.timezoneL
    var sunrise = PreferenceMaestro.sunriseL
    var sunset = PreferenceMaestro.sunsetL
    var currentTime = System.currentTimeMillis()/1000

    Timber.i("time of day current =${currentTime} sunrise=$sunrise and $sunset")

    if (currentTime <= sunrise-3600){
        typeOfSky = TypeOfSky.NIGHT

    }else if (currentTime <= sunrise){
        typeOfSky = TypeOfSky.MORNING

    }else if (currentTime > sunrise && currentTime < sunset-3600){
        typeOfSky = TypeOfSky.DAY

    }else if (currentTime >= sunset-3600 && currentTime < sunset ){
        typeOfSky = TypeOfSky.EVENING

    }else if (currentTime >= sunset){
        typeOfSky = TypeOfSky.NIGHT

    }

    print(" [ ${typeOfSky.name} ] ")


    return typeOfSky
}



//fun packer(arrayList: List<ListWithCoreofForecast>){
//    var cloudcoeff = 0.0
//    var timestamp : Long = 0
//    var sunrise : Long = 0
//    var sunset : Long = 0
//    var timeZone : Long = 0
//
//
//
//
//    for (i in arrayList){
//        timestamp = i.dt.toLong()
//        cloudcoeff = i.clouds.all.toDouble()
//
//        arrayX.add(timestamp+timeZone)
//
//    }
//
//}