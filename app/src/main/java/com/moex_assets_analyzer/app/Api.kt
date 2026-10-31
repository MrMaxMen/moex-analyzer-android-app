package com.moex_assets_analyzer.app

import com.moex_assets_analyzer.app.entities.HistoryRecord
import com.moex_assets_analyzer.app.others.ASSETS_LIST_URL
import com.moex_assets_analyzer.app.others.historyUrlFor
import com.moex_assets_analyzer.app.others.parseHistory
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import java.time.LocalDate


suspend fun getAssetsList(client: HttpClient) =
    client.get(ASSETS_LIST_URL)
        .bodyAsText(Charsets.UTF_8)


suspend fun getAssetHistoryFor(client: HttpClient, asset: String, since : LocalDate) : List<HistoryRecord> {
    val history = mutableListOf<HistoryRecord>()
    while (true) {
        val page =
            parseHistory(client.get(historyUrlFor(asset, since, start = history.size)).bodyAsText())
        history += page
        if (page.size != 100) break
    }
    return history
}