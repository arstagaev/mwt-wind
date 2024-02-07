package com.revolve44.mywindturbine.fragments


import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
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
import com.revolve44.mywindturbine.R
import com.revolve44.mywindturbine.activity.MainActivity
import com.revolve44.mywindturbine.fragments.ChildFragmentofMainScreen
import com.revolve44.mywindturbine.storage.PreferenceMaestro
import com.revolve44.mywindturbine.utils.*
import com.revolve44.mywindturbine.utils.Constants.Companion.isOpenedByAssistant
import com.revolve44.mywindturbine.viewmodels.MainScreenViewModel
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

    private lateinit var navController: NavController
    private var viewmodel : MainScreenViewModel? = null

    private var arrayX : ArrayList<Any> = ArrayList()

    private lateinit var mSwipeRefreshLayout: SwipeRefreshLayout

    lateinit var lastUpdateDataIndicator : TextView
//=======
    lateinit var tvLastUpdates : TextView

    lateinit var energyforecastindicator : TickerView
    lateinit var moneyforecastindicator : TickerView
    lateinit var dualIndicator : LinearLayout

    lateinit var mainscreen_back : RelativeLayout
    lateinit var currentCity : TextView
    lateinit var leftdescriptionOfDualIndicator: TextView
    lateinit var currencyIndicator: TextView
    var a = 0


    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewmodel = (activity as MainActivity).mainScreenViewModel

        navController = findNavController() //Initialising navController

        insertNestedFragment()


        lastUpdateDataIndicator = view.findViewById<TextView>(R.id.lastUpdatedtv)


        tvLastUpdates = view.findViewById<TextView>(R.id.lastUpdatedtv)
        energyforecastindicator = view.findViewById(R.id.energyforecastindicator)
        moneyforecastindicator = view.findViewById(R.id.moneyforecastindicator)
        dualIndicator = view.findViewById(R.id.dual_indicator)
        leftdescriptionOfDualIndicator = view.findViewById(R.id.leftdescriptionOfDualIndicator)
        currencyIndicator = view.findViewById(R.id.currencyIndicator)

        currentCity = view.findViewById(R.id.currentCity)


        swipeRefreshTools(view)

        Timber.i("xxx" + "pizdec")
        var forecastArray :ArrayList<Float> = ArrayList()



        viewmodel?.requestFor5days?.observe(viewLifecycleOwner, Observer { response ->
            Timber.i("zzz " + response.message + " data:" + response.data)
            when (response) {
                is Resource.Success -> {
                    dateOfLastUpdate(Status.SUCCESS)
                    mSwipeRefreshLayout.isRefreshing = false
                    runTwoIndicators(1)
                    setCurrentCity()

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
                    runTwoIndicators(1)
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
                    runTwoIndicators(1)
                    a = 7
                }
                7 -> {
                    runTwoIndicators(7)
                    a = 30
                }
                30 -> {
                    runTwoIndicators(30)
                    a = 1
                }
            }

        }
        mainscreen_back = view.findViewById<RelativeLayout>(R.id.mainscreen_back)


        //gradientAnimation(mainscreen_back,resources.getColor(R.color.black_night),resources.getColor(R.color.day_sun_zenith),0,3200)
    }

    private fun setCurrentCity() {
        currentCity.text = "City: ${PreferenceMaestro.chosenStationCITY}"
    }


    fun dateOfLastUpdate(currentMODE: Status) {
        val c = Calendar.getInstance()
        c.get(Calendar.AM_PM)

        when(currentMODE){
            Status.SUCCESS -> {
                PreferenceMaestro.lastUpdateDate = "Updated: " + lastUpdateDate(
                    c.get(Calendar.MONTH)+1, c.get(
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

    fun runTwoIndicators(lenghtOfForecastInDays: Int) {
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

            1 -> { (activity as MainActivity).showProgressBar("Forecast per Hour:")
                Timber.i("npe vm ${PreferenceMaestro.forecastForNow} calibr coeff: ${PreferenceMaestro.calibrationCoeff/100f} ")
                forecastPerPeriod =
                    (PreferenceMaestro.forecastForNow*(PreferenceMaestro.calibrationCoeff/100f))
                (activity as MainActivity).showOutputIndicator(forecastPerPeriod)
            }

            7 -> { (activity as MainActivity).showProgressBar("per Week:")
                forecastPerPeriod =
                    ((PreferenceMaestro.averageForecastperThreeHours* PreferenceMaestro.solarDayDuration * lenghtOfForecastInDays)*(PreferenceMaestro.calibrationCoeff/100f))
            }

            30 -> { (activity as MainActivity).showProgressBar("per Month:")
                Timber.i("${PreferenceMaestro.averageForecastperThreeHours} *"+PreferenceMaestro.solarDayDuration+"*"+lenghtOfForecastInDays+" * "+(PreferenceMaestro.calibrationCoeff/100f))
                forecastPerPeriod =
                    ((PreferenceMaestro.averageForecastperThreeHours* PreferenceMaestro.solarDayDuration * lenghtOfForecastInDays)*(PreferenceMaestro.calibrationCoeff/100f))
            }
            //else -> { (activity as MainActivity).showProgressBar("per Day:") }
        }

        Timber.i("Two indicators input: $forecastPerPeriod")


        //display  forecast indicator
        if (forecastPerPeriod>999){
            forecastPerPeriod /= 1000f
            leftdescriptionOfDualIndicator.text = "forecast output, kWh"
            energyforecastindicator.setText("" + roundTo1decimials(forecastPerPeriod), true)

            runSaverMoneyIndicator(forecastPerPeriod,1f)



        }else{
            leftdescriptionOfDualIndicator.text = "forecast output, Wh"
            energyforecastindicator.setText("" + Math.round(forecastPerPeriod), true)

            runSaverMoneyIndicator(forecastPerPeriod,1000f)
        }



        currencyIndicator.setText("you will save, ${PreferenceMaestro.chosenCurrency}")

    }
    private fun runSaverMoneyIndicator(forecastPerPeriod: Float, formatOfNumber: Float){
        //display $ saved money indicator
        if (forecastPerPeriod.toFloat() * PreferenceMaestro.pricePerkWh<1.0f){
            moneyforecastindicator.setText(
                "" + roundTo2decimials(forecastPerPeriod.toFloat() *( PreferenceMaestro.pricePerkWh/formatOfNumber)),
                true
            )
        }else{
            moneyforecastindicator.setText(
                "" + Math.round(forecastPerPeriod.toFloat() * (PreferenceMaestro.pricePerkWh/formatOfNumber)),
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
        viewmodel?.manualRequest()
    }

    // need for timely refresh of indicators
    override fun onResume() {
        super.onResume()
        if (isOpenedByAssistant) {
            mainscreen_back.setBackgroundColor(Color.LTGRAY)
        } else {
            mainscreen_back.setBackgroundColor(Color.parseColor("#004FD5"))
        }
        runTwoIndicators(1)
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

