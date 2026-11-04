package com.moex_assets_analyzer.app.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.moex_assets_analyzer.app.MaaApplication
import com.moex_assets_analyzer.app.Repository
import com.moex_assets_analyzer.app.entities.TrendResult
import kotlinx.coroutines.launch

class ResultsVM (private val repository: Repository)  : ViewModel(){

    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MaaApplication
                ResultsVM(app.repository)
            }
        }
    }

    val uiState : ResultsPageUiState
        get() = ResultsPageUiState(
            isLoading = true
        )

    var trendResults : List<TrendResult> = emptyList()

    init {
        viewModelScope.launch {
            repository.fetchHistory()
            repository.calculateMetrics()
            trendResults = repository.trendsResults.value
        }
    }


}


data class ResultsPageUiState(
    val isLoading : Boolean = false
)