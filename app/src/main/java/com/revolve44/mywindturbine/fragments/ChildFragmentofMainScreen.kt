package com.revolve44.mywindturbine.fragments

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import android.view.View
import android.widget.SeekBar
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.components.Description
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.revolve44.mywindturbine.R
import com.revolve44.mywindturbine.activity.MainActivity
import com.revolve44.mywindturbine.forecastmachine.ForecastToWindSpeed
import com.revolve44.mywindturbine.forecastmachine.getAverageNumOfArray
import com.revolve44.mywindturbine.models.FirstChartDataTransitor
import com.revolve44.mywindturbine.storage.PreferenceMaestro
import com.revolve44.mywindturbine.utils.*
import com.revolve44.mywindturbine.utils.Constants.Companion.DUAL_INDICATOR_SIZEOFFACTORS
import com.revolve44.mywindturbine.viewmodels.MainScreenViewModel
//import com.revolve44.solarpanelx.activity.MainActivity
//import com.revolve44.solarpanelx.R
//import com.revolve44.mywindturbine.utils.chartDatasort
//import com.revolve44.mywindturbine.utils.chartDatasortforFirstChart
//import com.revolve44.solarpanelx.core.getAverageNumOfArray
//import com.revolve44.mywindturbine.utils.scaleOfkWh
//import com.revolve44.solarpanelx.models.FirstChartDataTransitor
//import com.revolve44.solarpanelx.storage.PreferenceMaestro
//import com.revolve44.solarpanelx.viewmodels.MainViewModel
//import com.revolve44.solarpanelx.ui.charts.MyXAxisValuesFormatter
//import com.revolve44.solarpanelx.utils.Constants.Companion.DUAL_INDICATOR_SIZEOFFACTORS
import timber.log.Timber
import kotlin.collections.ArrayList
import kotlin.math.roundToInt


class ChildFragmentofMainScreen : Fragment(R.layout.fragment_child_of_main_screen_fragment) {

    private val chart: LineChart? = null
    private val seekBarX: SeekBar? = null
    private val tvX: TextView? = null
    val values = arrayListOf<String>("0h", "3h", "6h", "9h", "12h", "15h", "18h", "21h")

    private lateinit var viewmodel : MainScreenViewModel

    val mainScreenFragment : MainScreenFragment = MainScreenFragment()

    //Forecast for 24 hours: (Σ= 1.4kW)
    lateinit var topChartDescription : TextView
    lateinit var leftChartDescription : TextView
    lateinit var rightChartDescription : TextView

//    lateinit var sunshineIndicatorBlockSUNRISE : TextView
//    lateinit var sunshineIndicatorBlockDURATION : TextView
//    lateinit var sunshineIndicatorBlockSUNSET : TextView

    lateinit var maxWindin24hr : TextView
    lateinit var maxWindForTomorrow: TextView
    lateinit var maxWindForAfterTomorrow: TextView

    private var maxWindSpeedto24hr : Float = 0.0f
    private var maxWindSpeedforTomorrow : Float = 0.0f
    private var maxWindSpeedforAfterTomorrow : Float = 0.0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as Context
        viewmodel =(activity as MainActivity).mainScreenViewModel

        //init charts descriptions
        topChartDescription = view.findViewById(R.id.topChartDescription)
        leftChartDescription = view.findViewById(R.id.leftChartDescription)
        rightChartDescription = view.findViewById(R.id.rightChartDescription)

        //init sunshine indicator block
//        sunshineIndicatorBlockSUNRISE = view.findViewById(R.id.sunshineIndicatorBlockSUNRISE)
//        sunshineIndicatorBlockDURATION = view.findViewById(R.id.sunshineIndicatorBlockDURATION)
//        sunshineIndicatorBlockSUNSET = view.findViewById(R.id.sunshineIndicatorBlockSUNSET)

        val lineChart2 = view.findViewById<LineChart>(R.id.lineChart2)
        var description: Description = lineChart2.getDescription()
        val legend = lineChart2.legend
        //FirstLineChartSetup(lineChart, legend)

        val lineChart3 = view.findViewById<LineChart>(R.id.lineChart3)
        //ThirdLineChartSetup(lineChart3, legend)

        val lineChart1 = view.findViewById<LineChart>(R.id.lineChart1)
        //FirstLineChartSetup(lineChart1, legend)
        var forecastArrayForDualIndicator :ArrayList<Float> = ArrayList()
        var forecastArray :ArrayList<Float> = ArrayList()
        var forecastDateArray :ArrayList<Long> = ArrayList()

        viewmodel.getAllForecastForChart().observe(viewLifecycleOwner, Observer {data ->
            //data.get(1).HumanTime


            val timer = object: CountDownTimer(500, 250) {
                override fun onTick(millisUntilFinished: Long) {

                }

                override fun onFinish() {
                    try {
                        for (i in 0..DUAL_INDICATOR_SIZEOFFACTORS){
                            val forecastForThreeHr = data.get(i).forecast.toFloat()
                            if (forecastForThreeHr != 0F){
                                forecastArrayForDualIndicator.add(forecastForThreeHr)
                            }
                        }

                        PreferenceMaestro.averageForecastperThreeHours = getAverageNumOfArray(forecastArrayForDualIndicator)


                        forecastArray.clear()
                        forecastDateArray.clear()
                        for (i in 0 until 39) {
                            forecastArray.add(data.get(i).forecast.toFloat())
                            forecastDateArray.add(data.get(i).unixTime)
                        }

                        //refreshChartData(legend, lineChart, lineChart2, lineChart3, forecastArray)
                    }catch (e: java.lang.Exception) {
                        Timber.e("vvv "+forecastArray.size+" error - "+ e.message)
                    }
                    try {
                        Timber.i("yyy $forecastArray")

                        refreshChartData(legend, lineChart1, lineChart2, lineChart3, forecastArray,forecastDateArray)
                        refreshSunshineIndicatorBlock() // coming soon i will add
                        refreshMaxWindSpeedPerPeriod(view)
                        (activity as MainActivity).refreshNavigationHeader()
                    }catch (e : java.lang.Exception){
                        Timber.e("vvv2 "+forecastArray.size+" error - "+ e.message)
                    }
                }
            }
            timer.start()
        })
    }

    private fun refreshMaxWindSpeedPerPeriod(view: View){
        maxWindin24hr = view.findViewById(R.id.maxwindin24hr)
        maxWindForTomorrow = view.findViewById(R.id.maxwindfortomorrow)
        maxWindForAfterTomorrow = view.findViewById(R.id.maxwindforaftertomorrow)

        maxWindin24hr.setText("${ForecastToWindSpeed(maxWindSpeedto24hr)} m/s")
        maxWindForTomorrow.setText("${ForecastToWindSpeed(maxWindSpeedforTomorrow)} m/s")
        maxWindForAfterTomorrow.setText("${ForecastToWindSpeed(maxWindSpeedforAfterTomorrow)} m/s")
    }

    private fun refreshChartData(legend: Legend,
                                 lineChart1: LineChart,
                                 lineChart2: LineChart,
                                 lineChart3: LineChart,
                                 forecastArray: ArrayList<Float>, arrayDate: ArrayList<Long> ) {
        try {
            Timber.i("vvv3 =  ${forecastArray.toString()}")

            //FirstLineChartSetup(lineChart1,legend, chartDataHandler(forecastArray,0,1))
            /** get first element of array, for current forecast per hour (for dual indicator)*/
            PreferenceMaestro.forecastForNow = forecastArray[0]
            (parentFragment as MainScreenFragment).runTwoIndicators(1)
            //viewmodel.forecastPowerPerHour.value = forecastArray.get(0)
            /**init 1st or another name - top chart  */
            topLineChartSetup(lineChart1,legend, chartDatasortforFirstChart(arrayDate,forecastArray))
            /**init 2nd or left chart  */
            leftLineChartSetup(lineChart2,legend, chartDatasort(arrayDate,forecastArray,0))
            /**init 3rd or right chart */
            rightLineChartSetup(lineChart3,legend, chartDatasort(arrayDate,forecastArray,1))

            // Timber.i("www ${secondChartDataHandler(arrayDate)}")
        }catch (e : Exception){
            Timber.e("vvv3   ${e.message}")
        }
    }

    private fun refreshSunshineIndicatorBlock(){
//        sunshineIndicatorBlockSUNRISE.text = PreferenceMaestro.sunrise
//        sunshineIndicatorBlockDURATION.text = "${PreferenceMaestro.solarDayDuration}hr"
//        sunshineIndicatorBlockSUNSET.text = PreferenceMaestro.sunset
    }

    private fun topLineChartSetup(lineChart: LineChart, legend: Legend, frcstData: FirstChartDataTransitor){
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)
        lineChart.getDescription().setEnabled(false)
        lineChart.legend.isEnabled = false // description of define line

        legend.position = Legend.LegendPosition.BELOW_CHART_CENTER

        //Disable right axis
        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false

        val leftAxis = lineChart.axisLeft
        leftAxis.removeAllLimitLines()

        leftAxis.enableGridDashedLine(30F, 30F, 0F)
        leftAxis.setDrawLimitLinesBehindData(true)

        // Transit
        //forecast values
        val arrayData : ArrayList<Float> = frcstData.forecasts
        maxWindSpeedto24hr = arrayData.maxOrNull()!!
        // timeline
        val firstChartSpecialValues : ArrayList<String> = frcstData.dates

        val yValues = ArrayList<Entry>()
        //for (i in )
        try {

            for (i in 0..arrayData.size-1){
                yValues.add(Entry(i.toFloat(), arrayData.get(i) as Float))
            }
        }catch (e: Exception){
            Timber.i("vvv4 ${e.message}")
        }

        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false);
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        set1.setColor(Color.BLUE)
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val data = LineData(dataSet)

        lineChart.data = data

        //val values = arrayOf("jan", "feb", "mar", "nov", "oct", "apr")

        //   X
        val xAxis = lineChart.xAxis
        xAxis.setValueFormatter(MyXAxisValuesFormatter(firstChartSpecialValues))
        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //set1.isDrawFilledEnabled

//        set1.setCircleColor(Color.WHITE);
//        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        set1.setColor(Color.WHITE);
        set1.setDrawFilled(true);
        // setup drawable to filled gaps between xaxis and chart line
//        val drawable = ContextCompat.getDrawable(this, R.drawable.fade_blue)
//        set1.fillDrawable = drawable
        //пунктир
//        set1.enableDashedLine(10f, 5f, 0f);
//        set1.enableDashedHighlightLine(10f, 5f, 0f);

        set1.fillColor = Color.BLUE;
        set1.fillAlpha = 100;

        set1.setCubicIntensity(0.2f);

        /** Refresh all chart, i use this when i again setup new dataset*/
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()
        topChartDescription.text = "Forecast for 24 hours: (Σ= ${scaleOfkWh(arrayData.sum().toInt())})"
    }

    private fun leftLineChartSetup(lineChart: LineChart, legend: Legend, arrayData : ArrayList<Float>){
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)
        lineChart.getDescription().setEnabled(false)
        lineChart.legend.isEnabled = false // description of define line




        legend.position= Legend.LegendPosition.BELOW_CHART_CENTER

        val leftAxis = lineChart.axisLeft
        leftAxis.removeAllLimitLines()

        leftAxis.enableGridDashedLine(30F, 30F, 0F)
        leftAxis.setDrawLimitLinesBehindData(true)

        //Disable right axis
        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false


        val yValues = ArrayList<Entry>()
        Timber.i("vvv4 $arrayData")
        maxWindSpeedforTomorrow = arrayData.maxOrNull()!!
        try {

            for (i in 0..arrayData.size-1){
                yValues.add(Entry(i.toFloat(), arrayData.get(i) as Float))
            }
        }catch (e: Exception){
            Timber.i("vvv4 ${e.message}")
        }


//        yValues.add(Entry(1F, 80F))
//        yValues.add(Entry(2F, 59F))
//        yValues.add(Entry(3F, 14F))
//        yValues.add(Entry(4F, 56F))
//        yValues.add(Entry(5F, 34F))
//        yValues.add(Entry(6F, 14F))
//        yValues.add(Entry(7F, 56F))
//        yValues.add(Entry(8F, 34F))

        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false);
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val data = LineData(dataSet)

        lineChart.data = data



        //   X
        val xAxis = lineChart.xAxis
        try {
            xAxis.setValueFormatter(MyXAxisValuesFormatter(values))
        }catch (e: Exception){}

        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //set1.isDrawFilledEnabled

//        set1.setCircleColor(Color.WHITE);
//        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        set1.setColor(Color.WHITE);
        set1.setDrawFilled(true);
        set1.fillColor = Color.parseColor("#118cf7");
        set1.fillAlpha = 200;

        set1.setCubicIntensity(0.2f);

        /** Refresh all chart, i use this when i again setup a new dataset*/
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()

        leftChartDescription.text = "for tomorrow \n(Σ= ${scaleOfkWh(arrayData.sum().toInt())}): ${PreferenceMaestro.leftChartMonthandDay}"


    }

    private fun rightLineChartSetup(lineChart: LineChart, legend: Legend, arrayData : ArrayList<Float>){
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)
        lineChart.getDescription().setEnabled(false)
        lineChart.legend.isEnabled = false // description of define line

        legend.position = Legend.LegendPosition.BELOW_CHART_CENTER

//        val upper_limit = LimitLine(65F, "danger")
//        upper_limit.lineWidth = 4F
//        upper_limit.enableDashedLine(10F,10F, 0F)
//        upper_limit.labelPosition = LimitLine.LimitLabelPosition.RIGHT_TOP
//        upper_limit.textSize = 15F
//
//        val lower_limit = LimitLine(35F, "too low")
//        lower_limit.lineWidth = 4F
//        lower_limit.enableDashedLine(10F,10F, 0F)
//        lower_limit.labelPosition = LimitLine.LimitLabelPosition.RIGHT_BOTTOM
//        lower_limit.textSize = 15F
        //Disable right axis
        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false

        val leftAxis = lineChart.axisLeft
        leftAxis.removeAllLimitLines()
//        leftAxis.addLimitLine(upper_limit)
//        leftAxis.addLimitLine(lower_limit)
        //YAxis.YAxisLabelPosition.OUTSIDE_CHART
//        leftAxis.axisMaximum = 100F
//        leftAxis.axisMinimum = 25F

        leftAxis.enableGridDashedLine(30F, 30F, 0F)
        leftAxis.setDrawLimitLinesBehindData(true)

        maxWindSpeedforAfterTomorrow = arrayData.maxOrNull()!!
        val yValues = ArrayList<Entry>()
        try {

            for (i in 0..arrayData.size-1){
                yValues.add(Entry(i.toFloat(), arrayData.get(i) as Float))
            }
        }catch (e: Exception){
            Timber.i("vvv4 ${e.message}")
        }

        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false);
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val data = LineData(dataSet)

        lineChart.data = data

       //val values = arrayOf("jan", "feb", "mar", "nov", "oct", "apr")

        //   X
        val xAxis = lineChart.xAxis
        xAxis.setValueFormatter(MyXAxisValuesFormatter(values))
        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        set1.setMode(LineDataSet.Mode.CUBIC_BEZIER);
        //set1.isDrawFilledEnabled

//        set1.setCircleColor(Color.WHITE);
//        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        set1.setColor(Color.WHITE);
        set1.setDrawFilled(true);
        set1.fillColor = Color.parseColor("#118cf7");
        set1.fillAlpha = 200;

        set1.setCubicIntensity(0.2f);

        /** Refresh all chart, i use this when i again setup new dataset*/
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()

        rightChartDescription.text = "for after tomorrow \n(Σ= ${scaleOfkWh(arrayData.sum().roundToInt())}): ${PreferenceMaestro.rightChartMonthandDay}"

    }
}