package com.revolve44.mywindturbinepro.fragments

import android.app.AlertDialog
import android.content.Context
import android.content.DialogInterface
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.CountDownTimer
import android.util.TypedValue
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.content.ContextCompat
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
import com.revolve44.mywindturbinepro.R
import com.revolve44.mywindturbinepro.activity.MainActivity
import com.revolve44.mywindturbinepro.features.*
import com.revolve44.mywindturbinepro.models.FirstChartDataTransitor
import com.revolve44.mywindturbinepro.storage.PreferenceMaestro
import com.revolve44.mywindturbinepro.utils.Constants.Companion.DUAL_INDICATOR_SIZEOFFACTORS
import com.revolve44.mywindturbinepro.utils.Constants.Companion.IS_PRO_VERSION
import com.revolve44.mywindturbinepro.viewmodels.MainScreenViewModel
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

/**
 * Fragment include 5 charts + module of max wind speed
 */
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
    lateinit var fourthChartDescription : TextView
    lateinit var fiveChartDescription : TextView


    // i will add
//    lateinit var sunshineIndicatorBlockSUNRISE : TextView
//    lateinit var sunshineIndicatorBlockDURATION : TextView
//    lateinit var sunshineIndicatorBlockSUNSET : TextView

    lateinit var maxWindin24hr : TextView
    lateinit var maxWindForTomorrow: TextView
    lateinit var maxWindForAfterTomorrow: TextView
    lateinit var maxWindFor4day: TextView
    lateinit var maxWindFor5day: TextView

    private var maxWindSpeedto24hr : Float = 0.0f
    private var maxWindSpeedforTomorrow : Float = 0.0f
    private var maxWindSpeedforAfterTomorrow : Float = 0.0f
    private var maxWindSpeedfor4day : Float = 0.0f
    private var maxWindSpeedfor5day : Float = 0.0f

    private lateinit var helper : ImageView

    private lateinit var signWithCharts : CardView
    private lateinit var signSecond : CardView
    private lateinit var signThird : CardView

    lateinit var setNightMode : ImageView

    lateinit var getProVersion : TextView
    lateinit var chartsForProVersion : LinearLayout
    lateinit var signCardViewchartsForProVersion : CardView

    private lateinit var maxwindfor4day : TextView
    private lateinit var maxwindfor5day : TextView
    private lateinit var maxwindfor4dayDescription : TextView
    private lateinit var maxwindfor5dayDescription : TextView

    var sumOfFirstChart = 0
    var sumOfSecondChart = 0
    var sumOfThirdChart = 0
    var sumOfFourthChart = 0
    var sumOfFifthhart = 0

    private fun switchNightMode(){

        if (PreferenceMaestro.isNightNode){
            signWithCharts.setCardBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black_spoke))
//            signSecond.setCardBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black_spoke))
//            signThird.setCardBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black_spoke))
            signCardViewchartsForProVersion.setCardBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.black_spoke))

            topChartDescription.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            leftChartDescription.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            rightChartDescription.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

            fourthChartDescription.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            fiveChartDescription.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))

//            sunshineIndicatorBlockSUNRISE.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
//            sunshineIndicatorBlockDURATION.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
//            sunshineIndicatorBlockSUNSET.setTextColor(ContextCompat.getColor(requireActivity(), R.color.white))
            //nowNightMode = false

        }else{
            signWithCharts.setCardBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
//            signSecond.setCardBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))
//            signThird.setCardBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))

            signCardViewchartsForProVersion.setCardBackgroundColor(ContextCompat.getColor(requireActivity(), R.color.white))

            topChartDescription.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black_spoke))
            leftChartDescription.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            rightChartDescription.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

            fourthChartDescription.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
            fiveChartDescription.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

//            sunshineIndicatorBlockSUNRISE.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
//            sunshineIndicatorBlockDURATION.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))
//            sunshineIndicatorBlockSUNSET.setTextColor(ContextCompat.getColor(requireActivity(), R.color.black))

            //nowNightMode = true
        }
        //signWithCharts.setBackgroundColor(Color.BLACK)

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val activity = activity as Context
        viewmodel =(activity as MainActivity).viewModel

        signWithCharts = view.findViewById(R.id.main_sign_with_three_charts)
        setNightMode = view.findViewById(R.id.switch_to_night_mode)

        //init charts descriptions
        topChartDescription = view.findViewById(R.id.topChartDescription)
        leftChartDescription = view.findViewById(R.id.leftChartDescription)
        rightChartDescription = view.findViewById(R.id.rightChartDescription)
        fourthChartDescription = view.findViewById(R.id.fourthChartDescription)
        fiveChartDescription = view.findViewById(R.id.fiveChartDescription)

        // max winds view pro/standart
        maxwindfor4day= view.findViewById(R.id.maxwindfor4day)
        maxwindfor5day= view.findViewById(R.id.maxwindfor5day)
        maxwindfor4dayDescription= view.findViewById(R.id.maxwindfor4day_description)
        maxwindfor5dayDescription = view.findViewById(R.id.maxwindfor5day_description)

        getProVersion = view.findViewById(R.id.get_pro_version)
        chartsForProVersion = view.findViewById<LinearLayout>(R.id.charts_for_pro_version)
        signCardViewchartsForProVersion = view.findViewById<CardView>(R.id.main_sign_with_two_charts)

        observeChangingData(view)

        signCardViewchartsForProVersion.setOnClickListener {
            if (IS_PRO_VERSION == false){
                goToUrl("https://play.google.com/store/apps/details?id=com.revolve44.mywindturbinepro")
            }
        }
        if (IS_PRO_VERSION == true){
            switchToProVersion()
        }else{
            switchToStandartVersion()
        }

        setNightMode.setOnClickListener {
            PreferenceMaestro.isNightNode = !PreferenceMaestro.isNightNode

            switchNightMode()
            //nowNightMode = true
        }



    }

    private fun observeChangingData(view: View) {
        val lineChart1 = view.findViewById<LineChart>(R.id.lineChart1)
        val lineChart2 = view.findViewById<LineChart>(R.id.lineChart2)
        val lineChart3 = view.findViewById<LineChart>(R.id.lineChart3)

        var description: Description = lineChart2.getDescription()
        val legend = lineChart2.legend
        //FirstLineChartSetup(lineChart, legend)

//        val lineChart3 = view.findViewById<LineChart>(R.id.lineChart3)
//        //ThirdLineChartSetup(lineChart3, legend)
//        val lineChart4 = view.findViewById<LineChart>(R.id.lineChart4)
//        val lineChart5 = view.findViewById<LineChart>(R.id.lineChart5)
//
//        val lineChart1 = view.findViewById<LineChart>(R.id.lineChart1)

        helper = view.findViewById(R.id.helpInMainScreen)
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
                        /**
                         * 8*5 =40 but, is 0:00 may be errors
                         */
//                        for (i in 0 until data.size) {
//                            forecastArray.add(data.get(i).forecast.toFloat())
//                            forecastDateArray.add(data.get(i).unixTime)
//                        }
                        viewmodel.forecastNow.value = data.get(0).forecast.toFloat()
                        //* (PreferenceMaestro.calibrationCoeff / 100f)


                        for (i in 0 until data.size) {

                            // need for charts
                            forecastArray.add(data.get(i).forecast.toFloat() * (PreferenceMaestro.calibrationCoeff / 100f))
                            forecastDateArray.add(data.get(i).unixTime)

                        }
                        Timber.e("vvv "+forecastArray.size+" vvv - ")

                        //refreshChartData(legend, lineChart, lineChart2, lineChart3, forecastArray)
                    }catch (e: java.lang.Exception) {
                        Timber.e("vvv "+forecastArray.size+" error - "+ e.message)
                    }
                    try {
                        Timber.i("yyy $forecastArray")

                        if (IS_PRO_VERSION == true) {
                            val lineChart4 = view.findViewById<LineChart>(R.id.lineChart4)
                            val lineChart5 = view.findViewById<LineChart>(R.id.lineChart5)
                            refreshChartDataProVersion(
                                    legend,
                                    lineChart1,
                                    lineChart2,
                                    lineChart3,
                                    lineChart4,
                                    lineChart5,
                                    forecastArray,
                                    forecastDateArray
                            )

                        } else {

                            refreshChartDataStandartVersion(
                                    legend,
                                    lineChart1,
                                    lineChart2,
                                    lineChart3,
                                    forecastArray,
                                    forecastDateArray
                            )
                        }                        //refreshSunshineIndicatorBlock() // coming soon i will add
                        refreshMaxWindSpeedPerPeriod(view)
                        (activity as MainActivity).refreshNavigationHeader()
                    }catch (e : java.lang.Exception){
                        Timber.e("vvv2 "+forecastArray.size+" error - "+ e.message)
                    }
                }
            }
            timer.start()
        })

        switchNightMode()

        initHelper()

    }

    private fun switchToStandartVersion() {
//        var getProVersion = view.findViewById(R.id.get_pro_version) as TextView
//        var chartsForProVersion = view.findViewById<LinearLayout>(R.id.charts_for_pro_version)
//        var cardViewchartsForProVersion = view.findViewById<CardView>(R.id.main_sign_with_two_charts)
        val dimensionInPixel = 40F
        val dimensionInDp =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dimensionInPixel,
                        resources.displayMetrics
                ).toInt()
        maxwindfor4day.visibility= View.GONE
        maxwindfor5day.visibility= View.GONE
        maxwindfor4dayDescription.visibility= View.GONE
        maxwindfor5dayDescription.visibility = View.GONE

        signCardViewchartsForProVersion.layoutParams.height = dimensionInDp
        getProVersion.visibility = View.VISIBLE
        chartsForProVersion.visibility = View.INVISIBLE

    }

    private fun switchToProVersion() {
//        var getProVersion = view.findViewById(R.id.get_pro_version) as TextView
//        var chartsForProVersion = view.findViewById<LinearLayout>(R.id.charts_for_pro_version)
//        var cardViewchartsForProVersion = view.findViewById<CardView>(R.id.main_sign_with_two_charts)

        val dimensionInPixel = 160F
        val dimensionInDp =
                TypedValue.applyDimension(
                        TypedValue.COMPLEX_UNIT_DIP,
                        dimensionInPixel,
                        resources.displayMetrics
                ).toInt()

        maxwindfor4day.visibility= View.VISIBLE
        maxwindfor5day.visibility= View.VISIBLE
        maxwindfor4dayDescription.visibility= View.VISIBLE
        maxwindfor5dayDescription.visibility = View.VISIBLE

        signCardViewchartsForProVersion.layoutParams.height = dimensionInDp
        getProVersion.visibility = View.INVISIBLE
        chartsForProVersion.visibility = View.VISIBLE

    }

    private fun refreshChartDataStandartVersion(
            legend: Legend,
            lineChart1: LineChart,
            lineChart2: LineChart,
            lineChart3: LineChart,
            forecastArray: ArrayList<Float>, arrayDate: ArrayList<Long>
    ) {
        try {
            Timber.i("vvv3 =  $forecastArray")

            //FirstLineChartSetup(lineChart1,legend, chartDataHandler(forecastArray,0,1))
            /** get first element of array, for current forecast per hour (for dual indicator)*/
            //getSumOfPowerPerWeek()
            //showIndicatorNowYouMayUse((forecastArray[0]).toInt())




            viewmodel.forecastPowerPerWeek.value = forecastArray.sum() + forecastArray.takeLast(8).sum()*2
            Timber.i("viewmodel.forecastPowerPerWeek.value" + viewmodel.forecastPowerPerWeek.value)

            (parentFragment as MainScreenFragment).runTwoIndicators(1, true)
            //showIndicatorNowYouMayUse()
            //viewmodel.forecastPowerPerHour.value = forecastArray.get(0)
            /**init 1st or another name - top chart  */
            topLineChartSetup(
                    lineChart1, legend, chartDatasortforFirstChart(
                    arrayDate,
                    forecastArray
            )
            )
            /**init 2nd or left chart  */
            leftLineChartSetup(lineChart2, legend, chartDataSort(arrayDate, forecastArray, 0))
            /**init 3rd or right chart */
            rightLineChartSetup(lineChart3, legend, chartDataSort(arrayDate, forecastArray, 1))

            getSumOfPowerPerWeek()
        }catch (e: Exception){
            Timber.e("vvv3   ${e.message}")
        }
    }

    private fun refreshChartDataProVersion(
            legend: Legend,
            lineChart1: LineChart,
            lineChart2: LineChart,
            lineChart3: LineChart,
            lineChart4: LineChart,
            lineChart5: LineChart,
            forecastArray: ArrayList<Float>, arrayDate: ArrayList<Long>
    ) {
        try {
            Timber.i("vvv3 =  $forecastArray")

            //FirstLineChartSetup(lineChart1,legend, chartDataHandler(forecastArray,0,1))
            /** get first element of array, for current forecast per hour (for dual indicator)*/

            //showIndicatorNowYouMayUse((forecastArray[0]).toInt())




            viewmodel.forecastPowerPerWeek.value = forecastArray.sum() + forecastArray.takeLast(8).sum()*2
            Timber.i("viewmodel.forecastPowerPerWeek.value" + viewmodel.forecastPowerPerWeek.value)

            (parentFragment as MainScreenFragment).runTwoIndicators(1, true)
            //showIndicatorNowYouMayUse()
            //viewmodel.forecastPowerPerHour.value = forecastArray.get(0)
            /**init 1st or another name - top chart  */
            topLineChartSetup(
                    lineChart1, legend, chartDatasortforFirstChart(
                    arrayDate,
                    forecastArray
            )
            )
            /**init 2nd or left chart  */
            leftLineChartSetup(lineChart2, legend, chartDataSort(arrayDate, forecastArray, 0))
            /**init 3rd or right chart */
            rightLineChartSetup(lineChart3, legend, chartDataSort(arrayDate, forecastArray, 1))
            /**
             * PRO version below
             */
            /**init chart 4  */
            fourthLineChartSetup(lineChart4, legend, chartDataSort(arrayDate, forecastArray, 2))
            /**init chart 5 */
            fiveLineChartSetup(lineChart5, legend, chartDataSort(arrayDate, forecastArray, 3))

            getSumOfPowerPerWeek()

            // Timber.i("www ${secondChartDataHandler(arrayDate)}")
        }catch (e: Exception){
            Timber.e("vvv3   ${e.message}")
        }
    }

    private fun topLineChartSetup(
            lineChart: LineChart,
            legend: Legend,
            frcstData: FirstChartDataTransitor
    ){
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false // description of define line


        //lineChart.legend.textColor = Color.GREEN

        //lineChart.axisLeft.axisLineColor = Color.CYAN

        legend.position = Legend.LegendPosition.BELOW_CHART_CENTER

        //Disable right axis
        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false

        val leftAxis = lineChart.axisLeft
        leftAxis.removeAllLimitLines()

        //leftAxis.enableGridDashedLine(30F, 30F, 0F)
        leftAxis.setDrawLimitLinesBehindData(true)
        leftAxis.disableGridDashedLine()

        // Transit
        val arrayData : ArrayList<Float> = frcstData.forecasts
        val firstChartSpecialValues : ArrayList<String> = frcstData.dates

        val yValues = ArrayList<Entry>()

        //for (i in )
        try {

            for (i in 0..arrayData.size-1){
                yValues.add(Entry(i.toFloat(), arrayData.get(i)))
            }
        }catch (e: Exception){
            Timber.i("vvv4 ${e.message}")
        }

        var set1 = LineDataSet(yValues, "")


        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false)
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false

        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val data = LineData(dataSet)
        lineChart.data = data

        //   X
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = MyXAxisValuesFormatter(firstChartSpecialValues)
        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM



        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        set1.setDrawFilled(true)
        set1.fillAlpha = 100
        set1.cubicIntensity = 0.2f

        lineChart.axisLeft.textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        xAxis.textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
        set1.setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
        set1.fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)


        /** Refresh all chart, i use this when i again setup new dataset*/
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()

        maxWindSpeedto24hr = arrayData.maxOrNull()!!
        sumOfFirstChart = arrayData.sum().toInt()
        topChartDescription.text = getString(R.string.mainscreen_chart_title_forecast20hr)+" (Σ= ${
            scaleOfkWh(
                arrayData.sum().toInt(),
                true
        )
        })"

    }

    private fun leftLineChartSetup(
            lineChart: LineChart,
            legend: Legend,
            arrayData: ArrayList<Float>
    ){
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)
        lineChart.description.isEnabled = false
        lineChart.legend.isEnabled = false // description of define line


        legend.position= Legend.LegendPosition.BELOW_CHART_CENTER

        val leftAxis = lineChart.axisLeft
        leftAxis.removeAllLimitLines()

        //leftAxis.enableGridDashedLine(30F, 30F, 0F)

        leftAxis.setDrawLimitLinesBehindData(true)

        //Disable right axis
        val yAxisRight = lineChart.axisRight
        yAxisRight.isEnabled = false


        val yValues = ArrayList<Entry>()
        Timber.i("vvv4 $arrayData")
        try {

            for (i in 0..arrayData.size-1){
                yValues.add(Entry(i.toFloat(), arrayData.get(i)))
            }
        }catch (e: Exception){
            Timber.i("vvv4 ${e.message}")
        }

        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false)
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val data = LineData(dataSet)

        lineChart.data = data



        //   X
        val xAxis = lineChart.xAxis
        try {
            xAxis.valueFormatter = MyXAxisValuesFormatter(values)
        }catch (e: Exception){}

        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        set1.mode = LineDataSet.Mode.CUBIC_BEZIER

        set1.setDrawFilled(true)
//        set1.color = ContextCompat.getColor(requireActivity(),R.color.chart_stroke)
//        set1.fillColor = ContextCompat.getColor(requireActivity(),R.color.chart_fill_otherchart)
        set1.fillAlpha = 100

        set1.cubicIntensity = 0.2f

        lineChart.axisLeft.textColor = ContextCompat.getColor(
                requireActivity(),
                R.color.hint_white2
        )
        xAxis.textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
        set1.setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
        set1.fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)

        /** Refresh all chart, i use this when i again setup a new dataset*/
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()

        maxWindSpeedforTomorrow = arrayData.maxOrNull()!!
        sumOfSecondChart = arrayData.sum().toInt()
        leftChartDescription.text = getString(R.string.mainscreen_chart_title_forecasttomorrow) +"\n(Σ= ${
            scaleOfkWh(
                arrayData.sum().toInt(),
                true
        )
        }): ${PreferenceMaestro.leftChartMonthandDay}"


    }

    private fun rightLineChartSetup(
            lineChart: LineChart,
            legend: Legend,
            arrayData: ArrayList<Float>
    ){
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)
        lineChart.description.isEnabled = false
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

        //leftAxis.enableGridDashedLine(30F, 30F, 0F)
        leftAxis.setDrawLimitLinesBehindData(true)


        val yValues = ArrayList<Entry>()
        try {

            for (i in 0..arrayData.size-1){
                yValues.add(Entry(i.toFloat(), arrayData.get(i)))
            }
        }catch (e: Exception){
            Timber.i("vvv4 ${e.message}")
        }

        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false)
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val data = LineData(dataSet)

        lineChart.data = data

        //val values = arrayOf("jan", "feb", "mar", "nov", "oct", "apr")

        //   X
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = MyXAxisValuesFormatter(values)
        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        //set1.isDrawFilledEnabled

//        set1.setCircleColor(Color.WHITE);
//        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        set1.setColor(Color.WHITE);
        set1.setDrawFilled(true)
//        set1.color = ContextCompat.getColor(requireActivity(),R.color.chart_stroke)
//        set1.fillColor = ContextCompat.getColor(requireActivity(),R.color.chart_fill_otherchart)
        set1.fillAlpha = 100

        set1.cubicIntensity = 0.2f

        lineChart.axisLeft.textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        xAxis.textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
        set1.setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
        set1.fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)

        /** Refresh all chart, i use this when i again setup new dataset*/
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()

        maxWindSpeedforAfterTomorrow = arrayData.maxOrNull()!!
        sumOfThirdChart = arrayData.sum().toInt()
        rightChartDescription.text = getString(R.string.mainscreen_chart_title_forecastaftertomorrow)+" \n(Σ= ${
            scaleOfkWh(
                arrayData.sum().roundToInt(),
                true
        )
        }): ${PreferenceMaestro.rightChartMonthandDay}"

    }




    private fun fourthLineChartSetup(
            lineChart: LineChart,
            legend: Legend,
            arrayData: ArrayList<Float>
    ){
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)
        lineChart.description.isEnabled = false
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

        //leftAxis.enableGridDashedLine(30F, 30F, 0F)
        leftAxis.setDrawLimitLinesBehindData(true)


        val yValues = ArrayList<Entry>()
        try {

            for (i in 0..arrayData.size-1){
                yValues.add(Entry(i.toFloat(), arrayData.get(i)))
            }
        }catch (e: Exception){
            Timber.i("vvv4 ${e.message}")
        }

        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false)
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val data = LineData(dataSet)

        lineChart.data = data

        //val values = arrayOf("jan", "feb", "mar", "nov", "oct", "apr")

        //   X
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = MyXAxisValuesFormatter(values)
        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        //set1.isDrawFilledEnabled

//        set1.setCircleColor(Color.WHITE);
//        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        set1.setColor(Color.WHITE);
        set1.setDrawFilled(true)
//        set1.color = ContextCompat.getColor(requireActivity(),R.color.chart_stroke)
//        set1.fillColor = ContextCompat.getColor(requireActivity(),R.color.chart_fill_otherchart)
        set1.fillAlpha = 100

        set1.cubicIntensity = 0.2f

        lineChart.axisLeft.textColor = ContextCompat.getColor(
                requireActivity(),
                R.color.hint_white2
        )
        xAxis.textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
        set1.setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
        set1.fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)

        /** Refresh all chart, i use this when i again setup new dataset*/
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()

        maxWindSpeedfor4day = arrayData.maxOrNull()!!
        sumOfFourthChart = arrayData.sum().toInt()
        fourthChartDescription.text = ""+"${PreferenceMaestro.fourChartMonthandDay} (Σ= ${scaleOfkWh(arrayData.sum().roundToInt(), true)})"

    }

    private fun fiveLineChartSetup(
            lineChart: LineChart,
            legend: Legend,
            arrayData: ArrayList<Float>
    ){
        lineChart.isDragEnabled = true
        lineChart.setScaleEnabled(false)
        lineChart.description.isEnabled = false
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

        //leftAxis.enableGridDashedLine(30F, 30F, 0F)
        leftAxis.setDrawLimitLinesBehindData(true)


        val yValues = ArrayList<Entry>()
        try {

            for (i in 0..arrayData.size-1){
                yValues.add(Entry(i.toFloat(), arrayData.get(i)))
            }
        }catch (e: Exception){
            Timber.i("vvv4 ${e.message}")
        }

        var set1 = LineDataSet(yValues, "")
        Legend.LegendPosition.RIGHT_OF_CHART

        set1.fillAlpha = 110
        set1.setDrawHorizontalHighlightIndicator(false)
        //set1.disableDashedLine()
        set1.isHighlightEnabled = false
        val dataSet = ArrayList<ILineDataSet>()
        dataSet.add(set1)

        val data = LineData(dataSet)

        lineChart.data = data


        //val values = arrayOf("jan", "feb", "mar", "nov", "oct", "apr")

        //   X
        val xAxis = lineChart.xAxis
        xAxis.valueFormatter = MyXAxisValuesFormatter(values)
        xAxis.granularity = 1F
        xAxis.position = XAxis.XAxisPosition.BOTTOM

        set1.mode = LineDataSet.Mode.CUBIC_BEZIER
        //set1.isDrawFilledEnabled

//        set1.setCircleColor(Color.WHITE);
//        set1.setHighLightColor(Color.rgb(244, 117, 117));
//        set1.setColor(Color.WHITE);
        set1.setDrawFilled(true)
//        set1.color = ContextCompat.getColor(requireActivity(),R.color.chart_stroke)
//        set1.fillColor = ContextCompat.getColor(requireActivity(),R.color.chart_fill_otherchart)
        set1.fillAlpha = 100

        set1.cubicIntensity = 0.2f

        lineChart.axisLeft.textColor = ContextCompat.getColor(
                requireActivity(),
                R.color.hint_white2
        )
        xAxis.textColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.valueTextColor = ContextCompat.getColor(requireActivity(), R.color.hint_white2)
        set1.color = ContextCompat.getColor(requireActivity(), R.color.chart_stroke)
        set1.setCircleColor(ContextCompat.getColor(requireActivity(), R.color.chart_stroke))
        set1.fillColor= ContextCompat.getColor(requireActivity(), R.color.chart_fill_mainchart)

        /** Refresh all chart, i use this when i again setup new dataset*/
        lineChart.notifyDataSetChanged()
        lineChart.invalidate()

        maxWindSpeedfor5day = arrayData.maxOrNull()!!
        sumOfFifthhart = arrayData.sum().toInt()
        fiveChartDescription.text = " "+"${PreferenceMaestro.fiveChartMonthandDay} (Σ= ${scaleOfkWh(arrayData.sum().roundToInt(), true)})"

    }


    private fun initHelper() {
        helper.setOnClickListener {
            showHelperDialog()

        }
    }

    private fun showHelperDialog() {
        val alertDialog: AlertDialog = AlertDialog.Builder(activity).create()
        // coming sazanley
        alertDialog.setTitle("Helper")
        alertDialog.setMessage("Attention! do not confuse units! " +
                "\n The charts show Watts / kW and after all charts the maximum wind speed is indicated in meters per second"+
        "\n \nForecast Accuracy:\n" +
                "per hour: ~75%\n" +
                "per week: ~55%\n" +
                "per month: ~40%\n" +
                "we work on increase accuracy")
        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
                DialogInterface.OnClickListener { dialog, which -> dialog.dismiss() })
//        alertDialog.setButton(
//            AlertDialog.BUTTON_POSITIVE, "Don't show again",
//            DialogInterface.OnClickListener { dialog, which ->
//                editor.putBoolean("showagain", false)
//                editor.apply()
//                dialog.dismiss()
//            })
        alertDialog.show()
    }

    private fun refreshMaxWindSpeedPerPeriod(view: View){
        //textview
        maxWindin24hr = view.findViewById(R.id.maxwindin24hr)
        maxWindForTomorrow = view.findViewById(R.id.maxwindfor2day)
        maxWindForAfterTomorrow = view.findViewById(R.id.maxwindfor3rdday)
        maxWindFor4day = view.findViewById(R.id.maxwindfor4day)
        maxWindFor5day = view.findViewById(R.id.maxwindfor5day)
        //set max wind speed
        maxWindin24hr.setText("${ForecastToWindSpeed(maxWindSpeedto24hr)}")
        maxWindForTomorrow.setText("${ForecastToWindSpeed(maxWindSpeedforTomorrow)}")
        maxWindForAfterTomorrow.setText("${ForecastToWindSpeed(maxWindSpeedforAfterTomorrow)}")
        maxWindFor4day.setText("${ForecastToWindSpeed(maxWindSpeedfor4day)}")
        maxWindFor5day.setText("${ForecastToWindSpeed(maxWindSpeedfor5day)}")
    }

    private fun goToUrl(url: String) {
        val uriUrl: Uri = Uri.parse(url)
        val launchBrowser = Intent(Intent.ACTION_VIEW, uriUrl)
        startActivity(launchBrowser)
    }



    fun getSumOfPowerPerWeek(){


         viewmodel.sumPowerFrom5ChartsInMainScreen.value=
                 sumOfFirstChart+sumOfSecondChart+sumOfThirdChart+sumOfFourthChart+sumOfFifthhart
    }
}