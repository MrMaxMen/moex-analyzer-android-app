package com.moex_assets_analyzer.app.others

import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.HttpTimeout


fun configureHttpClient() = HttpClient(OkHttp) {
    install(HttpTimeout) {
        requestTimeoutMillis = 90_000
        connectTimeoutMillis = 60_000
        socketTimeoutMillis = 90_000
    }
}