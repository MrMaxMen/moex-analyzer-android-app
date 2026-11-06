package com.moex_assets_analyzer.app

import com.moex_assets_analyzer.app.entities.HistoryRecord
import com.moex_assets_analyzer.app.entities.TrendResult
import java.time.temporal.ChronoUnit
import kotlin.math.exp
import kotlin.math.ln
import kotlin.math.pow

fun calculateTrend(asset: String, records: List<HistoryRecord>): TrendResult = runCatching {
    val validRecords = records.filter { it.close > 0.0 }.sortedBy { it.date }
    if (validRecords.size < 2) return TrendResult(asset, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    val startDate = validRecords.first().date

    val tValues = validRecords.map { ChronoUnit.DAYS.between(startDate, it.date).toDouble() }
    val yValues = validRecords.map { ln(it.close) }

    val tBar = tValues.average()
    val yBar = yValues.average()

    var up = 0.0
    var down = 0.0

    for (i in validRecords.indices) {
        val dt = tValues[i] - tBar
        val dy = yValues[i] - yBar
        up += dt * dy
        down += dt * dt
    }

    if (down == 0.0) return TrendResult(asset, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0)

    val b = up / down
    val totalDays = tValues.last()

    val a = yBar - b * tBar
    var ssTot = 0.0
    var ssRes = 0.0
    for (i in validRecords.indices) {
        val yPred = a + b * tValues[i]
        ssRes += (yValues[i] - yPred).pow(2)
        ssTot += (yValues[i] - yBar).pow(2)
    }
    val rSquared = if (ssTot != 0.0) 1.0 - (ssRes / ssTot) else 1.0

    val dailyRate = (exp(b) - 1.0) * 100.0
    val monthlyRate = (exp(b * 30) - 1.0) * 100.0
    val annualRate = (exp(b * 365.0) - 1.0) * 100.0
    val totalPeriodReturn = (exp(b * totalDays) - 1.0) * 100.0

    var peak = 0.0
    var worst = 0.0
    for ((_, close) in validRecords) {
        if (close > peak) peak = close
        if (peak > 0.0) {
            val drop = (peak - close) / peak * 100.0
            if (drop > worst) worst = drop
        }
    }

    TrendResult(
        asset = asset,
        dailyRatePercent = if (dailyRate.isNaN()) 0.0 else dailyRate,
        monthlyRatePercent = if (monthlyRate.isNaN()) 0.0 else monthlyRate,
        annualRatePercent = if (annualRate.isNaN()) 0.0 else annualRate,
        totalPeriodReturnPercent = if (totalPeriodReturn.isNaN()) 0.0 else totalPeriodReturn,
        maxDrawdownPercent = if (worst.isNaN()) 0.0 else worst,
        rSquared = if (rSquared.isNaN()) 0.0 else rSquared,
        slope = if (b.isNaN()) 0.0 else b,
        intercept = if (a.isNaN()) 0.0 else a
    )
}.getOrDefault(TrendResult(asset, 0.0, 0.0, 0.0, 0.0, 0.0, 0.0))



