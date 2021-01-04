package com.revolve44.mywindturbine.fragments


import android.os.Bundle
import android.os.CountDownTimer
import androidx.fragment.app.Fragment
import android.view.View
import android.widget.TextView
import com.google.android.material.snackbar.Snackbar
import com.revolve44.mywindturbine.R
import com.revolve44.mywindturbine.activity.MainActivity
import com.revolve44.mywindturbine.storage.PreferenceMaestro
import com.revolve44.mywindturbine.utils.scaleOfkWh
import com.revolve44.mywindturbine.viewmodels.MainScreenViewModel
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
    var coeff = 1.0f

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as MainActivity).showProgressBar("Calibration")

        viewmodel =(activity as MainActivity).mainScreenViewModel

        circleSeekBar = view.findViewById(R.id.circleseekbar)
        calibrate_indicator = view.findViewById(R.id.calibrate_coeff)
        calibratedOutputPower = view.findViewById(R.id.calibrated_outputPower)


        //remind saved position of circle seek bar
        circleSeekBar.maxProcess = 200
        circleSeekBar.curProcess = PreferenceMaestro.calibrationCoeff

        //refresh indicators
        calibratedOutputPower.text = "${PreferenceMaestro.forecastForNow*PreferenceMaestro.calibrationCoeff/100f}Wh"
        calibrate_indicator.text = "${PreferenceMaestro.calibrationCoeff}%"


        PreferenceMaestro.calibrationCoeff = circleSeekBar.curProcess

        circleSeekBar.setOnSeekBarChangeListener { seekbar, curValue ->
            Timber.i("New Calibration value = $curValue")

            calibrate_indicator.text = "$curValue %"
            coeff = (curValue/100f).toFloat()

            Timber.i("calibr")
            //calibratedOutputPower.text = (scaleOfkWh((viewmodel.forecastPower.value)!! * coeff).roundTo(2))).toString()+"W"

            calibratedOutputPower.text = scaleOfkWh(((PreferenceMaestro.forecastForNow) * coeff).toInt())

            PreferenceMaestro.calibrationCoeff = curValue

            notifyAboutSavedChanges()
        }

    }

    fun notifyAboutSavedChanges(){
        val timer = object: CountDownTimer(1000, 1000) {
            override fun onTick(millisUntilFinished: Long) {

            }

            override fun onFinish() {
                Snackbar.make(requireActivity().findViewById(android.R.id.content),"Changes Saved",Snackbar.LENGTH_LONG).show()

            }
        }
        timer.start()
    }
}

