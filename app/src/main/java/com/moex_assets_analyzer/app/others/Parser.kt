package com.moex_assets_analyzer.app.others

import com.moex_assets_analyzer.app.entities.HistoryRecord
import kotlinx.serialization.json.Json
import kotlinx.serialization.json.JsonObject
import kotlinx.serialization.json.contentOrNull
import kotlinx.serialization.json.doubleOrNull
import kotlinx.serialization.json.jsonArray
import kotlinx.serialization.json.jsonObject
import kotlinx.serialization.json.jsonPrimitive
import java.time.LocalDate

fun parseHistory(jsonString: String): List<HistoryRecord> = runCatching {
    val root = Json.parseToJsonElement(jsonString)
    if (root !is JsonObject) return emptyList()

    val dataArray = root.jsonObject["history"]
        ?.jsonObject
        ?.get("data")
        ?.jsonArray ?: return emptyList()

    dataArray.mapNotNull { element ->
        runCatching {
            val row = element.jsonArray
            val dateStr = row.getOrNull(0)?.jsonPrimitive?.contentOrNull
            val closeVal = row.getOrNull(1)?.jsonPrimitive?.doubleOrNull

            if (dateStr != null && closeVal != null && closeVal > 0.0) {
                HistoryRecord(
                    date = LocalDate.parse(dateStr),
                    close = closeVal
                )
            } else {
                null
            }
        }.getOrNull()
    }
}.getOrDefault(emptyList())

fun parseTickers(jsonString: String): List<String> = runCatching {
    val root = Json.parseToJsonElement(jsonString)
    if (root !is JsonObject) return emptyList()

    val data = root.jsonObject["securities"]
        ?.jsonObject
        ?.get("data")
        ?.jsonArray ?: return emptyList()

    data.mapNotNull { row ->
        runCatching {
            val rowArray = row.jsonArray
            val ticker = rowArray.getOrNull(0)?.jsonPrimitive?.contentOrNull
            val name = rowArray.getOrNull(1)?.jsonPrimitive?.contentOrNull
            val fullName = rowArray.getOrNull(2)?.jsonPrimitive?.contentOrNull
            if (name?.contains("ETF", ignoreCase = true) == true ||
                name?.contains("БПИФ", ignoreCase = true) == true ||
                name?.contains("ПИФ", ignoreCase = true) == true ||
                fullName?.contains("БПИФ", ignoreCase = true) == true ||
                fullName?.contains("ПИФ", ignoreCase = true) == true
            ) ticker else null
        }.getOrNull()
    }.distinct()
}.getOrDefault(emptyList())



//data class Fund(val ticker: String, val fullName: String)
//
//fun parseFunds(jsonString: String): List<Fund> {
//    val data = Json.parseToJsonElement(jsonString).jsonObject["securities"]!!.jsonObject["data"]!!.jsonArray
//    return data.mapNotNull { row ->
//        val ticker = row.jsonArray.getOrNull(0)?.jsonPrimitive?.content
//        val name = row.jsonArray.getOrNull(1)?.jsonPrimitive?.content
//        val fullName = row.jsonArray.getOrNull(2)?.jsonPrimitive?.content
//        if (name?.contains("ETF", ignoreCase = true) == true || name?.contains("БПИФ", ignoreCase = true) == true || name?.contains("ПИФ", ignoreCase = true) == true || fullName?.contains("БПИФ", ignoreCase = true) == true || fullName?.contains("ПИФ", ignoreCase = true) == true) {
//            if (ticker != null && fullName != null) Fund(ticker, fullName) else null
//        } else null
//    }.distinctBy { it.ticker }
//}