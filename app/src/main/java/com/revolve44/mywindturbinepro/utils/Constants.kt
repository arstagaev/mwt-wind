package com.revolve44.mywindturbinepro.utils

import com.revolve44.mywindturbinepro.BuildConfig

class Constants {
    companion object{
        const val IS_PRO_VERSION : Boolean = BuildConfig.IS_PRO_VERSION
        const val BASE_URL = "https://api.openweathermap.org"
        const val API_KEY = "ac79fea59e9d15377b787a610a29b784"

        const val DUAL_INDICATOR_SIZEOFFACTORS = 32
    }
}