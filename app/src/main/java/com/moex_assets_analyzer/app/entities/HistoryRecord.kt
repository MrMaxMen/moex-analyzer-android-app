package com.moex_assets_analyzer.app.entities

import java.time.LocalDate

data class HistoryRecord(
    val date: LocalDate,
    val close: Double
)
