package com.revolve44.mywindturbinepro.viewmodels

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.ConnectivityManager.TYPE_WIFI
import android.net.NetworkCapabilities.*
import android.os.Build
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.revolve44.mywindturbinepro.WindTurbineApp
import com.revolve44.mywindturbinepro.features.getForecast
import com.revolve44.mywindturbinepro.models.ForecastPer3hr
import com.revolve44.mywindturbinepro.repository.WindRepository
import com.revolve44.mywindturbinepro.storage.PreferenceMaestro
import com.revolve44.mywindturbinepro.utils.Resource
import com.revolve44.mywindturbinepro.features.getTimeZoneGMTstyle
import com.revolve44.mywindturbinepro.features.unixtoHumanTime
import com.revolve44.postmakermassive.models.beta.FiveDaysForecastMain
import kotlinx.coroutines.launch
import retrofit2.Response
import timber.log.Timber


class MainScreenViewModel(app: Application, val repo: WindRepository) : AndroidViewModel(app) {
    val requestFor5days : MutableLiveData<Resource<FiveDaysForecastMain>> = MutableLiveData()

    val sumPowerFrom5ChartsInMainScreen : MutableLiveData<Int> = MutableLiveData()

    private var allForecastPool : LiveData<List<ForecastPer3hr>> = repo.getAllForecastFrom5days()

    var forecastPowerPerWeek = MutableLiveData<Float>()
    var forecastNow = MutableLiveData<Float>()

    var dataHasBeenChanged = MutableLiveData<Boolean>()


    init {
        startRequestTo5days()

    }

    fun manualRequest(){
        startRequestTo5days()

    }

    private fun startRequestTo5days() =viewModelScope.launch {
        safe5daysRequest()

    }

    private suspend fun safe5daysRequest() {
        // now status -> Loading
        requestFor5days.postValue(Resource.Loading())
        try {
            if (hasInternetConnection()){

                Timber.i("start API request")
                val response = repo.getRequestFor5days()
                requestFor5days.postValue(handleRequestFor5days(response))

            }else{
                requestFor5days.postValue(Resource.Error("NO INTERNET",null))
                Timber.e("NO INTERNET")

            }

        }catch (t: Throwable){
            when(t) {
                is java.io.IOException -> requestFor5days.postValue(Resource.Error("Network Failure"))
                else -> requestFor5days.postValue(Resource.Error("Conversion Error"))
            }
            Timber.e("safeBetaRequest() error: " + t)
        }

    }

    private fun handleRequestFor5days(response: Response<FiveDaysForecastMain>): Resource<FiveDaysForecastMain>? {
        // is successful if {@link #code()} is in the range [200..300)
        if (response.isSuccessful){
            response.body()?.let { resultResponse ->
                try {
                    Timber.i("body ="+resultResponse.list)
                    val listx = resultResponse.list

                    var cloudcoeff = 0.0
                    var windSpeed = 0.0F
                    var windDirection = 0
                    var timestamp : Long = 0
                    var sunrise : Long = 0
                    var sunset : Long = 0
                    var timeZone : Long = 0

                    deleteALL_table_ForecastCell()
                    sunrise = resultResponse.city.sunrise.toLong()
                    sunset = resultResponse.city.sunset.toLong()
                    timeZone = resultResponse.city.timezone.toLong()

                    //get time zone in human style
                    PreferenceMaestro.chosenTimeZone = getTimeZoneGMTstyle(timeZone)
                    //get name of city
                    PreferenceMaestro.chosenStationCITY = resultResponse.city.name

                    var arrayX : ArrayList<Long> = ArrayList()

                    for (i in listx){
                        timestamp = i.dt.toLong()
                        //cloudcoeff = i.clouds.all.toDouble()

                        windSpeed = i.wind.speed.toFloat()
                        windDirection = i.wind.deg.toInt()

                        saveForecastCell(
                                timestamp,
                                unixtoHumanTime(timestamp),
                                getForecast(windSpeed,timeZone,timestamp),
                                windSpeed,
                                windDirection)

                        //arrayX.add(timestamp+timeZone)

                    }



                }catch (t: Throwable){
                    Timber.e("handleRequest error ${t.message}")

                }
                return Resource.Success(resultResponse)
            }
        }

        Timber.e("not successful ${response.message()}")
        return Resource.Error(response.message())
    }

    private fun deleteALL_table_ForecastCell() = viewModelScope.launch {
        repo.deleteAllElementsFromTable()
    }

    private fun saveForecastCell
            (timestamp :Long, humanTime :String, forecastEnergy :Int,windSpeed: Float,windDirection: Int ) = viewModelScope.launch {

        val forecastCell = ForecastPer3hr(timestamp, 1, humanTime, forecastEnergy,windSpeed,windDirection)
        repo.saveForecastPer5days(forecastCell)
    }

    fun getAllForecastForChart() : LiveData<List<ForecastPer3hr>>{

        return allForecastPool
    }


    //check Internet connection
    private fun hasInternetConnection(): Boolean {
        val connectivityManager = getApplication<WindTurbineApp>().getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        // below is mean - current SDK >= Marsmellow SDK
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val activeNetwork = connectivityManager.activeNetwork ?: return false
            val capabilities =
                connectivityManager.getNetworkCapabilities(activeNetwork) ?: return false
            return when {
                capabilities.hasTransport(TRANSPORT_WIFI) -> true
                capabilities.hasTransport(TRANSPORT_CELLULAR) -> true
                capabilities.hasTransport(TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.activeNetworkInfo?.run {
                return when (type) {
                    TYPE_WIFI -> true
                    ConnectivityManager.TYPE_MOBILE -> true
                    ConnectivityManager.TYPE_ETHERNET -> true
                    else -> false
                }
            }
        }
        return false
    }
}