package com.revolve44.mywindturbinepro.utils

import com.revolve44.mywindturbinepro.ext.plusString
import com.revolve44.mywindturbinepro.storage.PreferenceMaestro
//import com.revolve44.solarpanelx.core.extensions.plusString
//import com.revolve44.solarpanelx.storage.PreferenceMaestro
import timber.log.Timber
import java.math.RoundingMode
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList
import kotlin.math.pow

var timestampList : ArrayList<Long> = ArrayList()
var cloudinessList : ArrayList<Double> = ArrayList()

fun firstStage(timestamp : Long, cloudiness : Double){
    timestampList.add(timestamp)
    cloudinessList.add(cloudiness)
    Timber.i("xxx"+ timestampList.toString())
}
fun unixtoHumanTime(timestamp: Long): String {

    val sdf = java.text.SimpleDateFormat("MM-dd HH:mm ZZZZ")
    sdf.timeZone = TimeZone.getTimeZone(PreferenceMaestro.chosenTimeZone)
    var str : String = sdf.format(java.util.Date(timestamp * 1000))

    return str
}

//fun scaleOfkWh(watts : Int) : String{
//    var kiloWatts = 0F
//    if (watts>999){
//        kiloWatts = roundTo2decimials(watts.toFloat()/1000)
//        return kiloWatts.plusString("kWh")
//
//    }else{
//        return watts.plusString("Wh")
//    }
//
//}

fun scaleOfkWh(watts : Int, roundedNumbers : Boolean) : String{
    if (!roundedNumbers){
        var newFormattedWatts : Int = 0

        if (watts<1000){
            return watts.plusString("Wh")

        }else if (watts in 1000..999999){
            newFormattedWatts = watts/1000
            return newFormattedWatts.plusString("kWh")

        }else{
            newFormattedWatts = watts/1000000
            return newFormattedWatts.plusString("MWh")
        }



//        if (watts>999){
//            newFormattedWatts = watts/1000
//            return newFormattedWatts.plusString("kWh")
//
//        }else if (watts>1000000){
//            newFormattedWatts = watts/1000000
//            return newFormattedWatts.plusString("MWh")
//
//        }else{
////        newFormattedWatts = roundTo2decimials(watts.toFloat()/1000)
//            return watts.plusString("Wh")
//
//        }

    }else{

        var newFormattedWatts = 0F
        if (watts in 1000..999999){
            newFormattedWatts = roundTo2decimials(watts.toFloat()/1000)
            return newFormattedWatts.plusString("kWh")

        }else if (watts>1000000){
            newFormattedWatts = roundTo2decimials(watts.toFloat()/1000000)
            return newFormattedWatts.plusString("MWh")

        }else{
//        newFormattedWatts = roundTo2decimials(watts.toFloat()/1000)
            return watts.plusString("Wh")
        }

//        var newFormattedWatts = 0F
//        if (watts>999){
//            newFormattedWatts = roundTo2decimials(watts.toFloat()/1000)
//            return newFormattedWatts.plusString("kWh")
//
//        }else if (watts>1000000){
//            newFormattedWatts = roundTo2decimials(watts.toFloat()/1000000)
//            return newFormattedWatts.plusString("MWh")
//        }else{
////        newFormattedWatts = roundTo2decimials(watts.toFloat()/1000)
//            return watts.plusString("Wh")
//        }
    }


}

fun lastUpdateDate(month : Int, day : Int, hourOfday : Int, minute :Int) : String{
    var HumanUnderstandingDate: String = "--:--"

//    var monthEND : String
//    var dayEND : String
    var hourOfdayEND : String = hourOfday.toString()
    var minuteEND : String = minute.toString()


    if (hourOfday<10){
        hourOfdayEND = "0$hourOfday"
    }
    if (minute<10){
        minuteEND = "0$minute"
    }
    HumanUnderstandingDate = "$month-$day $hourOfdayEND:$minuteEND"

    return HumanUnderstandingDate
}

fun roundTo2decimials(num : Float) : Float{
    val df = DecimalFormat("#.##")
    df.roundingMode = RoundingMode.CEILING

    return (df.format(num)).replace(",",".").toFloat()

}

fun roundTo1decimials(num : Float) : Float{
    val df = DecimalFormat("#.#")
    df.roundingMode = RoundingMode.CEILING

    return (df.format(num)).replace(",",".").toFloat()

}

fun getTimeZoneGMTstyle(timestamp: Long) : String{
    if (timestamp>=0){
        var hoursInSec = timestamp
        var hours = hoursInSec / 3600
        var minutesInSec =hoursInSec - (hours*3600)
        var minutes = minutesInSec / 60

        if (minutes<10 && minutes!=0L){
            return ("GMT+${hours}:0${minutes}")

        }else if(minutes==0L){
            return ("GMT+${hours}:${minutes}0")

        } else{
            return ("GMT+${hours}:${minutes}")
        }


    }else{
        // for North and Yourth America
        var hoursInSec = timestamp*(-1)
        var hours = hoursInSec / 3600
        var minutesInSec =hoursInSec - (hours*3600)
        var minutes = minutesInSec / 60

        if (minutes<10 && minutes!=0L){
            return ("GMT-${hours}:0${minutes}")

        }else if(minutes==0L){
            return ("GMT-${hours}:${minutes}0")

        } else{
            return ("GMT-${hours}:${minutes}")
        }
    }

}
fun sqrtCustom(a: Float, rate: Int):Float {
    return a.toFloat().pow(1/rate.toFloat())
}


