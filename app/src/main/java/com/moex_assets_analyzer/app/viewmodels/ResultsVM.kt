package com.moex_assets_analyzer.app.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.moex_assets_analyzer.app.MaaApplication
import com.moex_assets_analyzer.app.Repository
import com.moex_assets_analyzer.app.entities.TrendResult
import kotlinx.coroutines.launch
import java.time.LocalDate

class ResultsVM(private val repository: Repository) : ViewModel() {
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MaaApplication
                ResultsVM(app.repository)
            }
        }
    }

    private var isLoading by mutableStateOf(false)
    var trendResultList by mutableStateOf<List<TrendResult>>(emptyList())
    var errorMessage by mutableStateOf<String?>(null)

    init {
        loadMetrics()
    }

    fun loadMetrics() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                trendResultList = repository.getMetrics()
            } catch (e: Exception) {
                e.printStackTrace()
                errorMessage = e.localizedMessage ?: "Error loading data"
            } finally {
                isLoading = false
            }
        }
    }

    val uiState: ResultsPageUiState
        get() = ResultsPageUiState(
            isLoading = isLoading,
            trendResultList = trendResultList,
            errorMessage = errorMessage,
            selectedDate = repository.selectedDate
        )
}

data class ResultsPageUiState(
    val isLoading: Boolean = false,
    val trendResultList: List<TrendResult> = emptyList(),
    val errorMessage: String? = null,
    val selectedDate: LocalDate? = null
)
