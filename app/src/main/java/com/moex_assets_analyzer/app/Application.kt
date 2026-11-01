package com.moex_assets_analyzer.app

import android.app.Application
import com.moex_assets_analyzer.app.others.configureHttpClient
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob

class MaaApplication : Application() {
    private val appScope = CoroutineScope(SupervisorJob() + Dispatchers.Default)
    private val httpClient by lazy { configureHttpClient() }
    private val apiService by lazy { ApiService(httpClient) }

    val repository by lazy { Repository(apiService, appScope) }

    override fun onCreate() {
        super.onCreate()
        repository.fetchNames()
    }
}