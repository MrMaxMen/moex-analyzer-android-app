package com.moex_assets_analyzer.app

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import com.moex_assets_analyzer.app.entities.HistoryRecord
import com.moex_assets_analyzer.app.entities.TrendResult
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class Repository(
    private val apiService: ApiService,
    private val appScope: CoroutineScope
) {

    var assetNames: List<String> by mutableStateOf(emptyList())
        private set

    var histories: Map<String, List<HistoryRecord>> by mutableStateOf(emptyMap())
        private set

    var trendsResults: List<TrendResult> by mutableStateOf(emptyList())
        private set

    fun fetchNames() {
        appScope.launch {
            try {
                assetNames = apiService.getAssetsList()
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                Log.e("Repository", "Failed to fetch asset names", e)
            }
        }
    }

    fun fetchHistory() {
        //зовём метод core передовая список имён активов для запроса их истории
    }

    fun calculateMetrics() {
        //зовём метод core который вызывает метод калькулятора , передаём список активов и их историю
    }
}