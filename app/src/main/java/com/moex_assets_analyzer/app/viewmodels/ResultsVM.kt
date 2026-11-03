package com.moex_assets_analyzer.app.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.moex_assets_analyzer.app.MaaApplication
import com.moex_assets_analyzer.app.Repository

class ResultsVM (private val repository: Repository)  : ViewModel(){

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MaaApplication
                ResultsVM(app.repository)
            }
        }
    }





}