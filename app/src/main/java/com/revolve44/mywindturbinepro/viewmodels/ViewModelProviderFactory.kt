package com.revolve44.mywindturbinepro.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.revolve44.mywindturbinepro.repository.WindRepository

class ViewModelProviderFactory(
    private val app :Application,
    private val repository: WindRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainScreenViewModel(app, repository) as T
    }
}