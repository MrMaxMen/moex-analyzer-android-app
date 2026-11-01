package com.moex_assets_analyzer.app.viewmodels

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.moex_assets_analyzer.app.MaaApplication
import com.moex_assets_analyzer.app.Repository

class SelectionVM(private val repository: Repository) : ViewModel() {
    companion object {
        val Factory = viewModelFactory {
            initializer {
                val app = this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MaaApplication
                SelectionVM(app.repository)
            }
        }
    }

    private var userInput by mutableStateOf("")
    private var selectedAssets by mutableStateOf<List<String>>(emptyList())
    private var isLoading by mutableStateOf(false)

    private var isMetricsSelection by mutableStateOf(false)

    private var metricsSelection by mutableStateOf(MetricsSelection())

    val uiState: SelectionPageUiState
        get() = SelectionPageUiState(
            userInput = userInput,
            selectedAssets = selectedAssets,
            isLoading = isLoading,
            assetNames = repository.assetNames,
            isMetricsSelection = isMetricsSelection,
            metricsSelection = metricsSelection
        )

    fun onInputChange(text: String) {
        userInput = text
    }

    fun onMetricsSelection(){
        isMetricsSelection = true
    }

    fun onMetricsSelectionChange(newMetricsSelection: MetricsSelection){
        metricsSelection = newMetricsSelection
    }
}



data class SelectionPageUiState(
    val userInput: String = "",
    val selectedAssets: List<String> = emptyList(),
    val isLoading: Boolean = false,
    val assetNames: List<String> = emptyList(),
    val isMetricsSelection : Boolean = false,
    val metricsSelection: MetricsSelection = MetricsSelection()
)


data class MetricsSelection(
    val dailyRatePercent: Boolean = true,         // Дневной темп роста (%)
    val monthlyRatePercent : Boolean = true,      // Месячная доходность (%)
    val annualRatePercent: Boolean = true,        // Годовая доходность (% годовых, CAGR)
    val totalPeriodReturnPercent: Boolean = true, // Рост тренда за весь период (%)
    val maxDrawdownPercent : Boolean = true,        //максимальное падение (%)
    val rSquared: Boolean = true,                  // Коэффициент детерминации R^2
)