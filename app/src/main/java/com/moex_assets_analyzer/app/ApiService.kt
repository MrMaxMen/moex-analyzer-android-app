package com.moex_assets_analyzer.app

import com.moex_assets_analyzer.app.entities.HistoryRecord
import com.moex_assets_analyzer.app.exceptions.NetworkException
import com.moex_assets_analyzer.app.others.ASSETS_LIST_URL
import com.moex_assets_analyzer.app.others.historyUrlFor
import com.moex_assets_analyzer.app.others.parseHistory
import com.moex_assets_analyzer.app.others.parseTickers
import io.ktor.client.HttpClient
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import java.time.LocalDate
import kotlinx.coroutines.CancellationException

class ApiService(
    val client: HttpClient
) {
    suspend fun getAssetsList(): List<String> {
        return try {
            val text = client.get(ASSETS_LIST_URL).bodyAsText(Charsets.UTF_8)
            parseTickers(text)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            throw NetworkException(cause = e)
        }
    }

    suspend fun getAssetHistoryFor(
        asset: String,
        since: LocalDate
    ): List<HistoryRecord> {
        val history = mutableListOf<HistoryRecord>()
        while (true) {
            val page = try {
                val url = historyUrlFor(asset, since, start = history.size)
                val responseText = client.get(url).bodyAsText()
                parseHistory(responseText)
            } catch (e: CancellationException) {
                throw e
            } catch (e: Exception) {
                throw NetworkException(cause = e)
            }

            if (page.isEmpty()) break
            history += page
            if (page.size < 100) break
        }
        return history
    }
}
