package com.revolve44.mywindturbine.viewmodels

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.revolve44.mywindturbine.repository.WindRepository

class ViewModelProviderFactory(
    private val app :Application,
    private val repository: WindRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        return MainScreenViewModel(app, repository) as T
    }
}