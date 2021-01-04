package com.revolve44.mywindturbine.viewmodels

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.revolve44.mywindturbine.repository.WindRepository
//import com.revolve44.solarpanelx.models.SolarStation
//import com.revolve44.solarpanelx.repository.SolarRepository
import kotlinx.coroutines.launch

class ViewModelAddSolarStation(app: Application) : AndroidViewModel(app) {

    val repository : WindRepository = WindRepository(app)
    val lat = MutableLiveData<Float>()
    val lon = MutableLiveData<Float>()

//    fun addSolarStation(solarStation: SolarStation) = viewModelScope.launch {
//        repository.addSolarStation(solarStation)
//    }




}