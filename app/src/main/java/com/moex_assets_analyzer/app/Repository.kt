package com.moex_assets_analyzer.app

import com.moex_assets_analyzer.app.entities.HistoryRecord
import com.moex_assets_analyzer.app.entities.TrendResult
import com.moex_assets_analyzer.app.viewmodels.MetricsSelection
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.withContext
import kotlinx.coroutines.sync.Semaphore
import kotlinx.coroutines.sync.withPermit
import java.time.LocalDate


class Repository(
    private val apiService: ApiService,
    private val appScope: CoroutineScope
) {


    private var assetNamesCache: List<String>? = null

    var historiesCache: Map<String, List<HistoryRecord>>? = null
    var trendsResultsCache: List<TrendResult>? = null


    var selectedAssets: Set<String> = emptySet()
        private set

    var metricsSelection: MetricsSelection = MetricsSelection()
        private set

    fun updateSelection(assets: Set<String>, metrics: MetricsSelection) {
        if (this.selectedAssets != assets) {
            this.historiesCache = null
            this.trendsResultsCache = null
        }
        this.selectedAssets = assets
        this.metricsSelection = metrics
    }

    suspend fun getAssetNames() = withContext(Dispatchers.IO){
        assetNamesCache ?: apiService.getAssetsList().also { assetNamesCache = it }
    }


    suspend fun getHistory(since: LocalDate = LocalDate.now().minusYears(1)) = withContext(Dispatchers.IO) {
        historiesCache?: fetchAssetHistory(since).also { historiesCache = it }

    }

    private suspend fun fetchAssetHistory (since : LocalDate) : Map<String, List<HistoryRecord>> {
        val semaphore = Semaphore(permits = 20)
        return coroutineScope {
            selectedAssets.map { asset ->
                async {
                    semaphore.withPermit {
                        asset to apiService.getAssetHistoryFor(asset, since)
                    }
                }
            }.awaitAll().toMap()
        }
    }

    suspend fun getMetrics() = withContext(Dispatchers.Default) {
        trendsResultsCache ?: getHistory().map { (asset, history) ->
            calculateTrend(asset,history)
        }.also { trendsResultsCache = it }
    }
}

