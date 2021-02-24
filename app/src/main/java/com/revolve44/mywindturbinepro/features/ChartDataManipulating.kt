package com.revolve44.mywindturbinepro.features

import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import com.revolve44.mywindturbinepro.models.FirstChartDataTransitor
import com.revolve44.mywindturbinepro.storage.PreferenceMaestro
//import com.revolve44.solarpanelx.models.FirstChartDataTransitor
//import com.revolve44.solarpanelx.storage.PreferenceMaestro
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList

/**
 * ->input:
 * timestamps[],  forecasts[], num (num is number of chart which we fill our array on output)
 *
 * <-output:
 * right sorted part of array forecasts[] for define chart
 */
fun chartDataSort(timestamps: ArrayList<Long>, forecasts: ArrayList<Float>, jumpAboveArray : Int): ArrayList<Float> {

    Timber.i("chartDatasort inputput jump array is "+jumpAboveArray + "forecast size "+forecasts.size)
    var arrayListOUTPUT: ArrayList<Float> = ArrayList()


    var countdown = (8-(unxtoHr(timestamps.get(0)) / 3)) + 8*jumpAboveArray

    // here has been error, size and and demand index did don`t match
    for (i in countdown..countdown+7){


        Timber.i("chartDatasort: "+ unxtoDate(timestamps.get(i)))
        //for (z in countdown..timestamps.)
        arrayListOUTPUT.add(forecasts.get(i))

    }
    // for description of charts
    if (jumpAboveArray==0){
        // countdown+1 is coz i borrow second day of day-period to ensure filling
        PreferenceMaestro.leftChartMonthandDay = unxtoDayandMonth(timestamps.get(countdown+1))
    }else if (jumpAboveArray==1){
        PreferenceMaestro.rightChartMonthandDay = unxtoDayandMonth(timestamps.get(countdown+1))
    }else if (jumpAboveArray==2){
        PreferenceMaestro.fourChartMonthandDay = unxtoDayandMonth(timestamps.get(countdown+1))
    }else if (jumpAboveArray==3){
        PreferenceMaestro.fiveChartMonthandDay = unxtoDayandMonth(timestamps.get(countdown+1))
    }

    Timber.i("chartDatasort output: "+arrayListOUTPUT.toString())
    return arrayListOUTPUT
}


/**
 * ->input:
 * timestamps[],  forecasts[]
 *
 * <-output:
 * right sorted part of array forecasts[] for first chart in special container (FirstChartDataTransitor)
 */
fun chartDatasortforFirstChart( timestamps: ArrayList<Long>, forecasts: ArrayList<Float>) : FirstChartDataTransitor {

    var dateOUTPUT: ArrayList<String> = ArrayList()
    var forecastOUTPUT: ArrayList<Float> = ArrayList()

    for (i in 0..7){

        Timber.i("qqq "+ unxtoDate(timestamps.get(i)))

        var hour = unxtoHr(timestamps.get(i)).toInt()

        Timber.i("unx1 ${hour}")
        if (hour == 0 ){
            dateOUTPUT.add(unxtoDate(timestamps.get(i)))
        }else{
            dateOUTPUT.add("$hour"+"hr")
        }

        forecastOUTPUT.add(forecasts.get(i))


    }
    var magicContainer = FirstChartDataTransitor(dateOUTPUT,forecastOUTPUT)
    return magicContainer
}

/** Unix timestamp to hour INTEGER format */

fun unxtoHr(timestamp : Long): Int {


    val sdf = java.text.SimpleDateFormat("HH", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(PreferenceMaestro.chosenTimeZone)
    return sdf.format(java.util.Date(timestamp * 1000)).toInt()
}

/** Unix timestamp to date STRING format */

fun unxtoDate(timestamp: Long): String {
//    val sdf = java.text.SimpleDateFormat("HH dd-MMM ")
//    return (sdf.format(java.util.Date(timestamp * 1000)))

    val sdf = java.text.SimpleDateFormat("MM-dd HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(PreferenceMaestro.chosenTimeZone)
    return sdf.format(java.util.Date(timestamp * 1000))
}

/** Unix timestamp to day-month STRING format */

fun unxtoDayandMonth(timestamp: Long): String {
//    val sdf = java.text.SimpleDateFormat("dd MMM ")
//    return (sdf.format(java.util.Date(timestamp * 1000)))
    val sdf = java.text.SimpleDateFormat("MM-dd", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(PreferenceMaestro.chosenTimeZone)
    return sdf.format(java.util.Date(timestamp * 1000))
}

class MyXAxisValuesFormatter : IAxisValueFormatter {
    private var values : ArrayList<String>
    constructor(values: ArrayList<String>){
        this.values = values
    }
    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        return values[value.toInt()]
    }
}


/** for debugging purposes */

fun unxtoDateDEBUG(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("dd-MMM-yyyy / HH:mm:ss")
    return (sdf.format(java.util.Date(timestamp * 1000)))
}

//fun chartDataHandler(arrayList: ArrayList<Float>, rangeX0 : Int, rangeX1 : Int): ArrayList<Float> {
//    var arrayListOUTPUT : ArrayList<Float> = ArrayList()
//    for (i in (arrayList.size*rangeX0)/5+1..(arrayList.size*rangeX1)/5){
//        arrayListOUTPUT.add(arrayList.get(i))
//
//    }
//
//    Timber.i("vvv* $arrayListOUTPUT")
//
//    return arrayListOUTPUT
//}

fun unxtoHoursAndMinutes(timestamp: Long): String {
    val sdf = java.text.SimpleDateFormat("HH:mm", Locale.getDefault())
    sdf.timeZone = TimeZone.getTimeZone(PreferenceMaestro.chosenTimeZone)
    return sdf.format(java.util.Date(timestamp * 1000))

//    var days = timestamp / 86400
//    var hoursInSec = timestamp - (days * 86400)
//    var hours = hoursInSec / 3600
//    var minutesInSec =hoursInSec - (hours*3600)
//    var minutes = minutesInSec / 60

    //return "${hours}:${minutes}"
}
