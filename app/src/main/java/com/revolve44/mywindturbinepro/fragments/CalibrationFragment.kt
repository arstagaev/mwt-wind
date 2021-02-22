package com.revolve44.mywindturbinepro.fragments


import android.graphics.Color
import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.revolve44.mywindturbinepro.R
import com.revolve44.mywindturbinepro.activity.MainActivity
import com.revolve44.mywindturbinepro.storage.PreferenceMaestro
import com.revolve44.mywindturbinepro.utils.blinkATextView
import com.revolve44.mywindturbinepro.utils.scaleOfkWh
import com.revolve44.mywindturbinepro.viewmodels.MainScreenViewModel
//import com.revolve44.solarpanelx.activity.MainActivity
//import com.revolve44.solarpanelx.R
//import com.revolve44.mywindturbine.utils.scaleOfkWh
//import com.revolve44.solarpanelx.storage.PreferenceMaestro
//import com.revolve44.solarpanelx.viewmodels.MainViewModel
import io.feeeei.circleseekbar.CircleSeekBar
import timber.log.Timber

class CalibrationFragment : Fragment(R.layout.fragment_calibration) {
    private lateinit var viewmodel : MainScreenViewModel

    private lateinit var circleSeekBar: CircleSeekBar
    private lateinit var calibrate_indicator: TextView
    private lateinit var calibratedOutputPower: TextView
    private lateinit var changesSaved: TextView
    var coeff = 1.0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showProgressBar(getString(R.string.navigation_drawer_calibration))

        viewmodel =(activity as MainActivity).viewModel

        circleSeekBar = view.findViewById(R.id.circleseekbar)
        calibrate_indicator = view.findViewById(R.id.calibrate_coeff)
        calibratedOutputPower = view.findViewById(R.id.calibrated_outputPower)
        changesSaved = view.findViewById(R.id.changes_saved_indicator_fragment_calibr)


        //remind saved position of circle seek bar
        circleSeekBar.maxProcess = 200
        circleSeekBar.curProcess = (PreferenceMaestro.calibrationCoeff)
        //PreferenceMaestro.calibrationCoeff = circleSeekBar.curProcess

        //remind data of indicators
        if (viewmodel.forecastNow.value != null){
            calibratedOutputPower.text = scaleOfkWh(((viewmodel.forecastNow.value!!) * (circleSeekBar.curProcess/100f).toInt()).toInt(),true)
        }
        calibrate_indicator.text = "${PreferenceMaestro.calibrationCoeff}%"



        // for moving circle seekbar
        circleSeekBar.setOnSeekBarChangeListener { seekbar, curValue ->
            Timber.i("New Calibration value = $curValue")

            calibrate_indicator.text = "$curValue %"
            coeff = (curValue/100f).toFloat()

            //Timber.i("calibr")
            //calibratedOutputPower.text = (scaleOfkWh((viewmodel.forecastPower.value)!! * coeff).roundTo(2))).toString()+"W"
            if (viewmodel.forecastNow.value!=null){
                calibratedOutputPower.text = scaleOfkWh(((viewmodel.forecastNow.value!!) * coeff).toInt(),true)+""

            }else{
                calibratedOutputPower.text = "error"
                Timber.e("ERROR in calibrating fragment")
            }

            PreferenceMaestro.calibrationCoeff = curValue

            notifyAboutSavedChanges()
        }

    }

//    fun selfCalibrating() : Int {
//        if ()
//        return
//
//    }

    fun notifyAboutSavedChanges(){
        blinkATextView(
            changesSaved,
            Color.GREEN,
            Color.BLACK,
            Color.BLACK,
            4200
        )

    }
}


//class CalibrationFragment : Fragment(R.layout.fragment_calibration) {
//    private lateinit var viewmodel : MainScreenViewModel
//
//    private lateinit var circleSeekBar: CircleSeekBar
//    private lateinit var calibrate_indicator: TextView
//    private lateinit var calibratedOutputPower: TextView
//    var coeff = 1.0f
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//        (activity as MainActivity).showProgressBar("Calibration")
//
//        viewmodel =(activity as MainActivity).viewModel
//
//        circleSeekBar = view.findViewById(R.id.circleseekbar)
//        calibrate_indicator = view.findViewById(R.id.calibrate_coeff)
//        calibratedOutputPower = view.findViewById(R.id.calibrated_outputPower)
//
//
//        //remind saved position of circle seek bar
//        circleSeekBar.maxProcess = 200
//        circleSeekBar.curProcess = PreferenceMaestro.calibrationCoeff
//
//        //refresh indicators
//        calibratedOutputPower.text = "${PreferenceMaestro.forecastForNow*PreferenceMaestro.calibrationCoeff/100f}Wh"
//        calibrate_indicator.text = "${PreferenceMaestro.calibrationCoeff}%"
//
//
//        PreferenceMaestro.calibrationCoeff = circleSeekBar.curProcess
//
//        circleSeekBar.setOnSeekBarChangeListener { seekbar, curValue ->
//            Timber.i("New Calibration value = $curValue")
//
//            calibrate_indicator.text = "$curValue %"
//            coeff = (curValue/100f).toFloat()
//
//            Timber.i("calibr")
//            //calibratedOutputPower.text = (scaleOfkWh((viewmodel.forecastPower.value)!! * coeff).roundTo(2))).toString()+"W"
//
//            calibratedOutputPower.text = scaleOfkWh(((PreferenceMaestro.forecastForNow) * coeff).toInt(),true)
//
//            PreferenceMaestro.calibrationCoeff = curValue
//
//            notifyAboutSavedChanges()
//        }
//
//    }
//
//    fun notifyAboutSavedChanges(){
//        val timer = object: CountDownTimer(1000, 1000) {
//            override fun onTick(millisUntilFinished: Long) {
//
//            }
//
//            override fun onFinish() {
//                Snackbar.make(requireActivity().findViewById(android.R.id.content),"Changes Saved",Snackbar.LENGTH_LONG).show()
//
//            }
//        }
//        timer.start()
//    }
//}

