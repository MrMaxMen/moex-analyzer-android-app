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
import kotlinx.coroutines.launch

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
    private var selectedAssets by mutableStateOf<Set<String>>(emptySet())
    private var isLoading by mutableStateOf(false)
    private var selectionStep by mutableStateOf(SelectionStep.ASSETS_SELECTION)
    private var metricsSelection by mutableStateOf(MetricsSelection())

    private var assetNames by mutableStateOf<List<String>>(emptyList())

    init {
        viewModelScope.launch {
            isLoading = true
            assetNames = repository.getAssetNames()
            isLoading = false
        }
    }


    val uiState: SelectionPageUiState
        get() = SelectionPageUiState(
            userInput = userInput,
            selectedAssets = selectedAssets,
            isLoading = isLoading,
            assetNames = assetNames,
            selectionStep = selectionStep,
            metricsSelection = metricsSelection
        )

    val actions: Actions = object : Actions {
        override fun onInputChange(text: String) {
            userInput = text
        }
        override fun onMetricsSelection() {
            selectionStep = SelectionStep.METRICS_SELECTION
        }
        override fun onBackToAssetsSelection() {
            selectionStep = SelectionStep.ASSETS_SELECTION
        }
        override fun onMetricsSelectionChange(newMetricsSelection: MetricsSelection) {
            metricsSelection = newMetricsSelection
        }
        override fun onAssetSelection(asset: String) {
            selectedAssets += asset
        }
        override fun onAssetCancelSelection(asset: String) {
            selectedAssets -= asset
        }
        override fun saveSelectionsToRepository(){
            repository.updateSelection(selectedAssets,metricsSelection)
        }
    }

    interface Actions {
        fun onInputChange(text: String)
        fun onMetricsSelection()
        fun onBackToAssetsSelection()
        fun onMetricsSelectionChange(newMetricsSelection: MetricsSelection)
        fun onAssetSelection(asset: String)
        fun onAssetCancelSelection(asset: String)
        fun saveSelectionsToRepository()
    }
}

enum class SelectionStep {
    ASSETS_SELECTION,
    METRICS_SELECTION
}

data class SelectionPageUiState(
    val userInput: String = "",
    val selectedAssets: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val assetNames: List<String> = emptyList(),
    val selectionStep: SelectionStep = SelectionStep.ASSETS_SELECTION,
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