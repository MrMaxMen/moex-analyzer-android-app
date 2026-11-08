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
import java.time.LocalDate

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
    private var selectedDate by mutableStateOf<Dates?>(null)
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
            selectedDate = selectedDate
        )

    val actions: Actions = object : Actions {
        override fun onInputChange(text: String) {
            userInput = text
        }
        override fun onDateSelectionStep() {
            selectionStep = SelectionStep.DATE_SELECTION
        }
        override fun onBackToAssetsSelection() {
            selectionStep = SelectionStep.ASSETS_SELECTION
        }
        override fun onAssetSelection(asset: String) {
            selectedAssets += asset
        }
        override fun onAssetCancelSelection(asset: String) {
            selectedAssets -= asset
        }
        override fun saveSelectionsToRepository(){
            repository.updateSelection(selectedAssets , selectedDate!!)
        }
        override fun onDateSelection(date: Dates) {
            selectedDate = date
        }
    }

    interface Actions {
        fun onInputChange(text: String)
        fun onDateSelectionStep()
        fun onBackToAssetsSelection()
        fun onAssetSelection(asset: String)
        fun onAssetCancelSelection(asset: String)
        fun saveSelectionsToRepository()
        fun onDateSelection(date : Dates)
    }
}

enum class SelectionStep {
    ASSETS_SELECTION,
    DATE_SELECTION
}

data class SelectionPageUiState(
    val userInput: String = "",
    val selectedAssets: Set<String> = emptySet(),
    val isLoading: Boolean = false,
    val assetNames: List<String> = emptyList(),
    val selectionStep: SelectionStep = SelectionStep.ASSETS_SELECTION,
    val selectedDate : Dates? = null
)

enum class Dates(val value  : LocalDate){
    WEEK(LocalDate.now().minusWeeks(1)),
    MONTH(LocalDate.now().minusMonths(1)),
    THREE_MONTHS(LocalDate.now().minusMonths(3)),
    SIX_MONTHS(LocalDate.now().minusMonths(6)),
    YEAR(LocalDate.now().minusYears(1)),
    TWO_YEARS(LocalDate.now().minusYears(2))
}