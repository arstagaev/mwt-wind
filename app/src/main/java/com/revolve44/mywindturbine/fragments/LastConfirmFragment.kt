package com.revolve44.mywindturbine.fragments

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.widget.*
import android.widget.AdapterView.OnItemSelectedListener
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar
import com.revolve44.mywindturbine.R
import com.revolve44.mywindturbine.activity.AddSolarStationActivity
import com.revolve44.mywindturbine.activity.MainActivity
import com.revolve44.mywindturbine.storage.PreferenceMaestro
import com.revolve44.mywindturbine.utils.LockableScrollView
//import com.revolve44.solarpanelx.activity.MainActivity
//import com.revolve44.solarpanelx.activity.AddSolarStationActivity
//import com.revolve44.solarpanelx.storage.PreferenceMaestro
import io.feeeei.circleseekbar.CircleSeekBar
import timber.log.Timber


class LastConfirmFragment : Fragment(R.layout.fragment_confirm_station) {

    //lateinit var viewModelAddSolarStation: ViewModelAddSolarStation
    private lateinit var circleSeekBarRotorDiameter: CircleSeekBar
    private lateinit var average_eff_indicator1: TextView

    private lateinit var circleSeekBarStartupWindSpeed: CircleSeekBar
    private lateinit var install_date_indicator2: TextView


    private lateinit var nominalPowerOfStation: EditText
    private lateinit var currencySpinner: Spinner
    private lateinit var pricePerkWh: EditText

    private lateinit var confirmButton: Button
    private lateinit var scrollView: LockableScrollView


    //variables
    private var efficiencyOfSolarPanels = 0
    private var installationDate = 0
    private var nominalPowerOfPVStation = 0



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        nominalPowerOfStation = view.findViewById(R.id.nominalPowerOfStation)
        currencySpinner = view.findViewById(R.id.currencySpinner)
        pricePerkWh = view.findViewById(R.id.pricePerkWh)

        //circle seekbars with textviews
        circleSeekBarRotorDiameter = view.findViewById(R.id.circleseekbar_rotordiameter)
        average_eff_indicator1 = view.findViewById(R.id.calibrate_coeff1)
        circleSeekBarStartupWindSpeed = view.findViewById(R.id.circleseekbar_startup_windspeed)
        install_date_indicator2 = view.findViewById(R.id.calibrate_coeff2)

        confirmButton = view.findViewById(R.id.confirm_changes)
        scrollView = view.findViewById(R.id.scrollViewFromFragmentConfirmStation)


        circleSeekBarRotorDiameter.maxProcess = 30
        circleSeekBarStartupWindSpeed.maxProcess = 25

        loadCharacteristicsFromPreferences()

        activateCircleSeekBar()
        activateSpinnerofCurrency(view)


        confirmButton.setOnClickListener {

            if((nominalPowerOfStation.text).toString().toInt()!=0){
                saveInputCharacteristicsFromTwoFragmentsInViewPagerToPreferences()

            }else{
                Snackbar.make(
                    requireActivity().findViewById(android.R.id.content),
                    "Nominal power can't be a 0",
                    Snackbar.LENGTH_LONG
                ).show()
            }


        }

    }

    private fun activateSpinnerofCurrency(view: View) {
        var chosenPrice = 0.13f
        val values = arrayOf("$", "€", "₹", "₽", "SAR", "£", "¥")
        val spinner = view.findViewById(R.id.currencySpinner) as Spinner
        val adapter: ArrayAdapter<String> =
            ArrayAdapter<String>(requireActivity(), android.R.layout.simple_spinner_item, values)
        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line)
        spinner.adapter = adapter
        spinner.setSelection(values.indexOf(PreferenceMaestro.chosenCurrency))


        spinner.onItemSelectedListener = object : OnItemSelectedListener {
            override fun onItemSelected(
                adapterView: AdapterView<*>, view: View, pos: Int,
                id: Long
            ) {
                PreferenceMaestro.chosenCurrency = adapterView.getItemAtPosition(pos).toString()
                when(PreferenceMaestro.chosenCurrency){
                    "$"->{
                        (pricePerkWh.setText("0.13"))
                    }
                    "€"->{(pricePerkWh.setText("0.21"))}
                    "₹"->{(pricePerkWh.setText("6"))}
                    "₽"->{(pricePerkWh.setText("4.4"))}
                    "SAR"->{(pricePerkWh.setText("0.18"))}
                    "£"->{(pricePerkWh.setText("0.16"))}
                    "¥"->{(pricePerkWh.setText("25"))}

                }
//                Toast.makeText(
//                    adapter.context, PreferenceMaestro.pricePerkWh.toString() + " is chosen",
//                    Toast.LENGTH_LONG
//                ).show()
            }

            override fun onNothingSelected(adapterView: AdapterView<*>?) {

            }
        }


    }



    @SuppressLint("ClickableViewAccessibility")
    private fun activateCircleSeekBar() {
        //remind data
//        circleSeekBarRotorDiameter.curProcess = PreferenceMaestro.chosenWindTurbineRotorDiameter
//        circleSeekBarStartupWindSpeed.curProcess = PreferenceMaestro.chosenWindTurbineStartupWindSpeed
//        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
//            circleSeekBarRotorDiameter.focusable
//            circleSeekBarStartupWindSpeed.focusable
//        }
        circleSeekBarRotorDiameter.setOnTouchListener { v, event ->
            if(event.action==MotionEvent.ACTION_DOWN ){
               //scrollView.isSmoothScrollingEnabled= false
                scrollView.setScrollingEnabled(false)
            }else if(event.action==MotionEvent.ACTION_UP){
                //scrollView.isSmoothScrollingEnabled = true
                scrollView.setScrollingEnabled(true)

            }
            false
        }
        circleSeekBarStartupWindSpeed.setOnTouchListener { v, event ->
            if(event.action==MotionEvent.ACTION_DOWN ){
                //scrollView.isSmoothScrollingEnabled= false
                scrollView.setScrollingEnabled(false)
            }else if(event.action==MotionEvent.ACTION_UP){
                //scrollView.isSmoothScrollingEnabled = true
                scrollView.setScrollingEnabled(true)

            }
            false
        }


        circleSeekBarRotorDiameter.setOnSeekBarChangeListener { seekbar, curValue ->
            Timber.i("New Calibration value = $curValue")


//            if (curValue<9){
//
//                Snackbar.make(
//                    requireActivity().findViewById(android.R.id.content),
//                    "Efficiency coeff. of solar panel must be at least 9%",
//                    Snackbar.LENGTH_LONG
//                ).show()
//            }

            average_eff_indicator1.text = "${curValue}m"
            efficiencyOfSolarPanels = curValue
            PreferenceMaestro.chosenWindTurbineRotorDiameter = curValue

            //PreferenceMaestro.calibration = curValue
        }

        circleSeekBarStartupWindSpeed.setOnSeekBarChangeListener { seekbar, curValue ->
            Timber.i("New Calibration value = $curValue")

            install_date_indicator2.text = "$curValue"
            installationDate = curValue

            PreferenceMaestro.chosenWindTurbineStartupWindSpeed = curValue

            //PreferenceMaestro.calibration = curValue
        }
    }


    private fun loadCharacteristicsFromPreferences() {
        nominalPowerOfStation.setText("" + PreferenceMaestro.chosenStationNOMINALPOWER)

        circleSeekBarRotorDiameter.curProcess = PreferenceMaestro.chosenWindTurbineRotorDiameter
        average_eff_indicator1.text = circleSeekBarRotorDiameter.curProcess.toString()


        circleSeekBarStartupWindSpeed.curProcess = PreferenceMaestro.chosenWindTurbineStartupWindSpeed
        install_date_indicator2.text = circleSeekBarStartupWindSpeed.curProcess.toString()
    }

    private fun saveInputCharacteristicsFromTwoFragmentsInViewPagerToPreferences(){
        try {
            //nominalPowerOfPVStation = (nominalPowerOfStation.text).toString()

            PreferenceMaestro.firstStart = false

            Timber.i("savedInputData: " + nominalPowerOfPVStation)
            PreferenceMaestro.chosenStationNAME = "ExampleName" // future feature
            PreferenceMaestro.chosenStationNOMINALPOWER = (nominalPowerOfStation.text).toString().toInt()
            PreferenceMaestro.chosenWindTurbineRotorDiameter = efficiencyOfSolarPanels
            PreferenceMaestro.chosenWindTurbineStartupWindSpeed = installationDate
            PreferenceMaestro.pricePerkWh = (pricePerkWh.text).toString().toFloat()


            val intent = Intent(activity, MainActivity::class.java)
            startActivity(intent)
            (activity as AddSolarStationActivity).finish()


        }catch (e: Exception){

            Snackbar.make(
                requireActivity().findViewById(android.R.id.content),
                "Please, fill correct all forms",
                Snackbar.LENGTH_LONG
            ).show()
            Timber.e("ERROR " + e.message)

        }
    }
    override fun onAttach(context: Context) {
        super.onAttach(context)
        Timber.i("attach Called")
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.i("oncreate Called")
    }
    override fun onResume() {
        super.onResume()
        loadCharacteristicsFromPreferences()
        Timber.i("onResume Called")
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