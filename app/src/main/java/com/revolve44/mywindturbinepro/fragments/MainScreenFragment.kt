package com.revolve44.mywindturbinepro.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.view.Gravity
import android.view.View
import android.widget.LinearLayout
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.lifecycle.Observer
import androidx.navigation.NavController
import androidx.navigation.fragment.findNavController
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.revolve44.mywindturbinepro.R
import com.revolve44.mywindturbinepro.activity.MainActivity
import com.revolve44.mywindturbinepro.features.*
import com.revolve44.mywindturbinepro.storage.PreferenceMaestro
import com.revolve44.mywindturbinepro.utils.*
import com.revolve44.mywindturbinepro.viewmodels.MainScreenViewModel
import com.revolve44.mywindturbinepro.utils.TypeOfSky
import com.revolve44.mywindturbinepro.utils.Status
//import com.revolve44.solarpanelx.activity.MainActivity
//import com.revolve44.solarpanelx.R
//
//import com.revolve44.mywindturbine.utils.lastUpdateDate
//import com.revolve44.mywindturbine.utils.gradientAnimation
//import com.revolve44.mywindturbine.utils.roundTo1decimials
//import com.revolve44.mywindturbine.utils.roundTo2decimials
//
//import com.revolve44.solarpanelx.storage.PreferenceMaestro
//import com.revolve44.solarpanelx.viewmodels.MainViewModel
//import com.revolve44.solarpanelx.utils.Resource
//import com.revolve44.solarpanelx.utils.Status
//import com.revolve44.solarpanelx.utils.Status.*
import com.robinhood.ticker.TickerView
import timber.log.Timber
import java.util.*
import kotlin.collections.ArrayList


/**
 * HomeFragment
 *  2 Buttons
 *  Nav graph start fragment
 */

class MainScreenFragment : Fragment(R.layout.fragment_mainscreen), View.OnClickListener, SwipeRefreshLayout.OnRefreshListener { //OnClickListener

//    private lateinit var navController: NavController
//    private lateinit var viewmodel : MainScreenViewModel
//
//    private var arrayX : ArrayList<Any> = ArrayList()
//
//    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout
//
//    lateinit var lastUpdateDataIndicator : TextView
////=======
//    lateinit var tvLastUpdates : TextView
//
//    lateinit var energyforecastindicator : TickerView
//    lateinit var moneyforecastindicator : TickerView
//    lateinit var dualIndicator : LinearLayout
//
//    lateinit var mainscreen_back : RelativeLayout
//    lateinit var currentCity : TextView
//    lateinit var leftdescriptionOfDualIndicator: TextView
//    lateinit var currencyIndicator: TextView
//    var a = 0
    private lateinit var navController: NavController
    private lateinit var viewmodel : MainScreenViewModel

    private var arrayX : ArrayList<Any> = ArrayList()

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    private lateinit var lastUpdateDataIndicator : TextView
    private lateinit var dayIndicator : TextView

    private lateinit var tvLastUpdates : TextView

    private lateinit var energyforecastindicator : TickerView
    private lateinit var moneyforecastindicator : TickerView
    private lateinit var dualIndicator : LinearLayout

    private lateinit var mainscreen_back : RelativeLayout
    private lateinit var currentCity : TextView
    private lateinit var leftdescriptionOfDualIndicator: TextView
    private lateinit var currencyIndicator: TextView
    var a = 0
    private var typeOfSky: TypeOfSky = TypeOfSky.NIGHT


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel =(activity as MainActivity).viewModel
        navController = findNavController() //Initialising navController

        insertNestedFragment()


        lastUpdateDataIndicator = view.findViewById<TextView>(R.id.lastUpdatedtv)


        tvLastUpdates = view.findViewById<TextView>(R.id.lastUpdatedtv)
        energyforecastindicator = view.findViewById(R.id.energyforecastindicator)
        moneyforecastindicator = view.findViewById(R.id.moneyforecastindicator)
        dualIndicator = view.findViewById(R.id.dual_indicator)
        leftdescriptionOfDualIndicator = view.findViewById(R.id.leftdescriptionOfDualIndicator)
        currencyIndicator = view.findViewById(R.id.currencyIndicator)
        dayIndicator = view.findViewById(R.id.day_indicator)
        mainscreen_back = view.findViewById<RelativeLayout>(R.id.mainscreen_back)

        currentCity = view.findViewById(R.id.currentCity)

        swipeRefreshTools(view)

        //var forecastArray :ArrayList<Float> = ArrayList()
//        var fragment2 = requireActivity().supportFragmentManager.findFragmentByTag(ChildFragmentofMainScreen.FRAGMENT_TAG2)
//        fragment2

        //        var fragment = .findFragmentByTag(MainScreenFragment.FRAGMENT_TAG)
//        fragment.




        viewmodel.requestFor5days.observe(viewLifecycleOwner, Observer { response ->
            Timber.i("zzz " + response.message + " data:" + response.data)
            when (response) {
                is Resource.Success -> {
                    Timber.i("lifecycle success")
                    dateOfLastUpdate(Status.SUCCESS)
                    mSwipeRefreshLayout.isRefreshing = false
                    switchColorOfSky()

                    if (viewmodel.dataHasBeenChanged.value != null){
                        runTwoIndicators(1, viewmodel.dataHasBeenChanged.value!!)
                    }else{
                        runTwoIndicators(1, true)
                    }

                    setCurrentCity()

//                    ChildFragmentofMainScreen.create()
//                    val fragment: ChildFragmentofMainScreen = childFragmentManager.findFragmentByTag(ChildFragmentofMainScreen.FRAGMENT_TAG2) as ChildFragmentofMainScreen
//                    fragment.showIndicatorNowYouMayUse(PreferenceMaestro.forecastForNow.toInt())



                    response.data?.let { finishedResponse ->

                        Timber.i("xxx " + finishedResponse.list.get(1).clouds)
                    }
                }
                is Resource.Error -> {
                    dateOfLastUpdate(Status.ERROR)
                    mSwipeRefreshLayout.isRefreshing = false

                    response.message?.let { message ->

                        Toast.makeText(activity, "An error occured: $message", Toast.LENGTH_LONG)
                                .show()
                        Timber.i("zzz2 $message   mess - ${response.message} data - ${response.data}   ")
                    }
                }
                is Resource.Loading -> {
                    dateOfLastUpdate(Status.LOADING)
                    runTwoIndicators(1, true)
                    setCurrentCity()
                    mSwipeRefreshLayout.isRefreshing = true

                }
            }

        })

        dualIndicator.setOnClickListener {
            if(a==0){
                a = 7
            }


            when(a){
                1 -> {
                    runTwoIndicators(1, true)
                    a = 7
                }
                7 -> {
                    runTwoIndicators(7, true)
                    a = 30
                }
                30 -> {
                    runTwoIndicators(30, true)
                    a = 1
                }
            }

        }

    }



    // make color of sky
    private fun switchColorOfSky(){

        //if (getCurrentTimeInDefineLocation(PreferenceMaestro.currentGMTinDefineLocation))
        /**
         * if first main indicator = 0 we got night sky, for example
         */

        typeOfSky = defineTimeOfDay()

        when(typeOfSky){
            TypeOfSky.NIGHT -> {
                dayIndicator.text = "night"
                gradientAnimation(
                        mainscreen_back,
                        resources.getColor(R.color.day_sun_zenith),
                        resources.getColor(
                                R.color.black_night
                        ),
                        0,
                        3200
                )
                Timber.i("time of day is ${TypeOfSky.NIGHT}")
            }
            TypeOfSky.MORNING -> {
                dayIndicator.text = ""
                gradientAnimation(
                        mainscreen_back,
                        resources.getColor(R.color.black_night),
                        resources.getColor(
                                R.color.morning
                        ),
                        0,
                        3200
                )
                Timber.i("time of day is ${TypeOfSky.MORNING}")
            }
            TypeOfSky.DAY -> {
                dayIndicator.text = ""
                gradientAnimation(
                        mainscreen_back,
                        resources.getColor(R.color.black_night),
                        resources.getColor(
                                R.color.day_sun_zenith
                        ),
                        0,
                        3200
                )
                Timber.i("time of day is ${TypeOfSky.DAY}")
            }
            TypeOfSky.EVENING -> {
                dayIndicator.text = ""
                gradientAnimation(
                        mainscreen_back,
                        resources.getColor(R.color.black_night),
                        resources.getColor(
                                R.color.evening
                        ),
                        0,
                        3200
                )
                Timber.i("time of day is ${TypeOfSky.EVENING}")
            }
        }

//

//        if (PreferenceMaestro.forecastForNow == 0F){
//            dayIndicator.text = "night"
//            gradientAnimation(mainscreen_back,
//                resources.getColor(R.color.day_sun_zenith),
//                resources.getColor(
//                    R.color.black_night),
//                0,
//                3200)
//        }else{
//            dayIndicator.text = ""
//            gradientAnimation(mainscreen_back,
//                resources.getColor(R.color.black_night),
//                resources.getColor(
//                    R.color.day_sun_zenith),
//                0,
//                3200)
//        }


    }

    private fun setCurrentCity() {
        currentCity.text = getString(R.string.mainscreen_cityindicator)+" ${PreferenceMaestro.chosenStationCITY}"
    }


    fun dateOfLastUpdate(currentMODE: Status) {
        val c = Calendar.getInstance()
        c.get(Calendar.AM_PM)

        when(currentMODE){
            Status.SUCCESS -> {
                PreferenceMaestro.lastUpdateDate =
                        getString(R.string.mainscreen_LastUpdatedIndicator) + lastUpdateDate(
                                c.get(Calendar.MONTH) + 1, c.get(
                                Calendar.DAY_OF_MONTH
                        ), c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE)
                        )
                tvLastUpdates.text = PreferenceMaestro.lastUpdateDate
            }
            Status.ERROR -> {
                tvLastUpdates.text = PreferenceMaestro.lastUpdateDate
            }
            Status.LOADING -> {
                tvLastUpdates.text = PreferenceMaestro.lastUpdateDate
            }
        }
    }

    fun runTwoIndicators(lenghtOfForecastInDays: Int, animate: Boolean) {
//        if (viewmodel.forecastPowerPerHour.value!=null){
//            PreferenceMaestro.forecastForNow = viewmodel.forecastPowerPerHour.value!!
//        }
//        viewmodel.forecastPowerPerHour.observe(viewLifecycleOwner, Observer { chg->
//            PreferenceMaestro.forecastForNow = chg.absoluteValue
//
//        })

        // UI options
        energyforecastindicator.gravity = Gravity.CENTER
        moneyforecastindicator.gravity = Gravity.CENTER
        var forecastPerPeriod : Float = 0f

        when(lenghtOfForecastInDays){

            1 -> {
                (activity as MainActivity).showProgressBar(getString(R.string.mainscreen_toolbartitle_perhour))
                Timber.i("npe vm ${viewmodel.forecastNow.value} calibr coeff: ${PreferenceMaestro.calibrationCoeff / 100f} ")

                // calibrate
                //forecastPerPeriod = (PreferenceMaestro.forecastForNow)
                //* (PreferenceMaestro.calibrationCoeff / 100f)
                viewmodel.forecastNow.observe(viewLifecycleOwner, Observer {fNow->
                    forecastPerPeriod = fNow * ( PreferenceMaestro.calibrationCoeff / 100f )

                })


//                if (forecastPerPeriod == 0F && typeOfSky != TypeOfSky.NIGHT) {
//                    forecastPerPeriod = PreferenceMaestro.averageForecastperThreeHours * 0.8F
////                    PreferenceMaestro.forecastForNow = forecastPerPeriod
////                    viewmodel.forecastNow.value = PreferenceMaestro.forecastForNow
//                    ///
//                    viewmodel.forecastNow.value = forecastPerPeriod
//
//                }else if (typeOfSky == TypeOfSky.NIGHT && forecastPerPeriod > 0){
//                    forecastPerPeriod = 0F
//                    //PreferenceMaestro.forecastForNow = forecastPerPeriod
//                    //viewmodel.forecastNow.value = PreferenceMaestro.forecastForNow
//                    viewmodel.forecastNow.value = forecastPerPeriod
//                }
                // in future upds must set fewer accuracy of forecast -10% mb

                // correct forecast period
                // forecastPerPeriod = nowIsDayOrNot(forecastPerPeriod)
                // notknow
                (activity as MainActivity).showOutputIndicator(forecastPerPeriod)
            }

            7 -> {
                (activity as MainActivity).showProgressBar(getString(R.string.mainscreen_toolbartitle_perweek))
                if (viewmodel.forecastPowerPerWeek.value != null) {
                    forecastPerPeriod =
                            (viewmodel.forecastPowerPerWeek.value)!! * (PreferenceMaestro.calibrationCoeff / 100f)
                }

                //((PreferenceMaestro.averageForecastperThreeHours * PreferenceMaestro.solarDayDuration * lenghtOfForecastInDays) * (PreferenceMaestro.calibrationCoeff / 100f))
            }

            30 -> {
                (activity as MainActivity).showProgressBar(getString(R.string.mainscreen_toolbartitle_permonth))
                Timber.i("${PreferenceMaestro.averageForecastperThreeHours} *" + PreferenceMaestro.solarDayDuration + "*" + lenghtOfForecastInDays + " * " + (PreferenceMaestro.calibrationCoeff / 100f))
                if (viewmodel.forecastPowerPerWeek.value != null) {

                    forecastPerPeriod =
                            ((viewmodel.forecastPowerPerWeek.value)!! * (PreferenceMaestro.calibrationCoeff / 100f)) * 4

                } else {
                    forecastPerPeriod =
                            ((PreferenceMaestro.averageForecastperThreeHours * PreferenceMaestro.solarDayDuration * lenghtOfForecastInDays) * (PreferenceMaestro.calibrationCoeff / 100f))
                }

            }
            //else -> { (activity as MainActivity).showProgressBar("per Day:") }
        }

        Timber.i("Two indicators input: $forecastPerPeriod")


        //display  forecast indicator
        if (forecastPerPeriod>999){
            forecastPerPeriod /= 1000f
            leftdescriptionOfDualIndicator.text = getString(R.string.mainscreen_indicator_outputkWh)
            energyforecastindicator.setText("" + roundTo1decimials(forecastPerPeriod), animate)

            runSaverMoneyIndicator(forecastPerPeriod, 1f)



        }else if(forecastPerPeriod>1000000){
            forecastPerPeriod /= 1000000f
            leftdescriptionOfDualIndicator.text = getString(R.string.mainscreen_indicator_outputMWh)
            energyforecastindicator.setText("" + roundTo1decimials(forecastPerPeriod), animate)

            runSaverMoneyIndicator(forecastPerPeriod, 1f)

        }else{
            leftdescriptionOfDualIndicator.text = getString(R.string.mainscreen_indicator_outputWh)
            energyforecastindicator.setText("" + Math.round(forecastPerPeriod), animate)

            runSaverMoneyIndicator(forecastPerPeriod, 1000f)
        }


        //display  savings money indicator
        currencyIndicator.setText(getString(R.string.mainscreen_indicator_you_will_save) + " ${PreferenceMaestro.chosenCurrency}")

        //refresh "now you may use console"
//        val fm: FragmentManager? = fragmentManager
//        val fragm: ChildFragmentofMainScreen = fm.findFragmentById(R.id.child_fragment_container) as ChildFragmentofMainScreen
//        fragm.
        //(childFragmentManager as ChildFragmentofMainScreen?)?.showIndicatorNowYouMayUse()
//        var ccc : ChildFragmentofMainScreen = ChildFragmentofMainScreen()
//        ccc.showIndicatorNowYouMayUse()


    }
    private fun runSaverMoneyIndicator(forecastPerPeriod: Float, formatOfNumber: Float){
        //display $ saved money indicator
        if (forecastPerPeriod.toFloat() * PreferenceMaestro.pricePerkWh<1.0f){
            moneyforecastindicator.setText(
                    "" + roundTo2decimials(forecastPerPeriod.toFloat() * (PreferenceMaestro.pricePerkWh / formatOfNumber)),
                    true
            )
        }else{
            moneyforecastindicator.setText(
                    "" + Math.round(forecastPerPeriod.toFloat() * (PreferenceMaestro.pricePerkWh / formatOfNumber)),
                    true
            )
        }
    }


    private fun swipeRefreshTools(view: View) {

        mSwipeRefreshLayout = view.findViewById(R.id.swipecontainer)
        mSwipeRefreshLayout.setOnRefreshListener(this as SwipeRefreshLayout.OnRefreshListener)

        mSwipeRefreshLayout.nestedScrollAxes

        mSwipeRefreshLayout.setColorSchemeResources(
            android.R.color.holo_blue_bright,
            android.R.color.holo_red_light
        )

    }


    fun insertNestedFragment() {
        var childfragment : Fragment = ChildFragmentofMainScreen()
        var transaction : FragmentTransaction = childFragmentManager.beginTransaction()
        transaction.replace(R.id.child_fragment_container, childfragment).commit()

    }

    override fun onClick(v: View?) {

    }

    override fun onRefresh() {
        //mSwipeRefreshLayout.isRefreshing = true
        viewmodel.manualRequest()
    }

    // need for timely refresh of indicators
    override fun onResume() {
        super.onResume()
        runTwoIndicators(1,false)
        setCurrentCity()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("attach Called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("oncreate Called")
    }


    override fun onPause() {
        super.onPause()
        Timber.i("onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Timber.i("onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Timber.i("onDestroy Called")
    }




}

