package com.revolve44.mywindturbine.forecastmachine

import com.revolve44.mywindturbine.storage.PreferenceMaestro
import com.revolve44.mywindturbine.utils.roundTo2decimials
import com.revolve44.mywindturbine.utils.sqrtCustom
import com.revolve44.mywindturbine.viewmodels.MainScreenViewModel
import com.revolve44.postmakermassive.models.beta.ListWithCoreofForecast
import kotlin.math.PI
import kotlin.math.pow

fun getForecast(windSpeed: Float,timeZone: Long,timestamp: Long): Int {
    var curpowx =
            (((1.23 * PI * (PreferenceMaestro.radius).pow(2) * ((windSpeed).pow(3)) * PreferenceMaestro.powerefficiency ) / 2).toFloat())

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