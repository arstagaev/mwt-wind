package com.revolve44.mywindturbinepro.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import com.revolve44.mywindturbinepro.repository.WindRepository
//import com.revolve44.solarpanelx.models.SolarStation
//import com.revolve44.solarpanelx.repository.SolarRepository

class ViewModelAddSolarStation(app: Application) : AndroidViewModel(app) {

    val repository : WindRepository = WindRepository(app)
    val lat = MutableLiveData<Float>()
    val lon = MutableLiveData<Float>()

//    fun addSolarStation(solarStation: SolarStation) = viewModelScope.launch {
//        repository.addSolarStation(solarStation)
//    }




}