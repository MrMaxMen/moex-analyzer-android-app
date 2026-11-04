package com.moex_assets_analyzer.app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.moex_assets_analyzer.app.entities.HistoryRecord
import com.moex_assets_analyzer.app.entities.TrendResult
import com.moex_assets_analyzer.app.viewmodels.MetricsSelection
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher

class Repository(
    private val apiService: ApiService,
    private val appScope: CoroutineScope
) {

    private val _assetNames = MutableStateFlow<List<String>>(emptyList())
    val assetNames: StateFlow<List<String>> = _assetNames.asStateFlow()

    private val _histories = MutableStateFlow<Map<String, List<HistoryRecord>>>(emptyMap())
    val histories: StateFlow<Map<String, List<HistoryRecord>>> = _histories.asStateFlow()

    private val _trendsResults = MutableStateFlow<List<TrendResult>>(emptyList())
    val trendsResults: StateFlow<List<TrendResult>> = _trendsResults.asStateFlow()


    var selectedAssets: Set<String> = emptySet()
        private set

    var metricsSelection: MetricsSelection = MetricsSelection()
        private set

    fun updateSelection(assets: Set<String>, metrics: MetricsSelection) {
        this.selectedAssets = assets
        this.metricsSelection = metrics
    }

    fun fetchNames() {
        appScope.launch {
            try {
                _assetNames.value = apiService.getAssetsList()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("Repository", "Failed to fetch asset names", e)
            }
        }
    }

    suspend fun fetchHistory() = withContext(Dispatchers.IO){

    }

    suspend fun calculateMetrics() = withContext(Dispatchers.Default) {

    }
}

