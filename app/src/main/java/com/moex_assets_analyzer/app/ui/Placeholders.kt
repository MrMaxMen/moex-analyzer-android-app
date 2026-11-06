package com.moex_assets_analyzer.app.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.moex_assets_analyzer.app.entities.TrendResult

val sampleTrendResultList = listOf(
    TrendResult(asset = "SBER", dailyRatePercent = 0.08, monthlyRatePercent = 2.45, annualRatePercent = 33.5, totalPeriodReturnPercent = 125.4, maxDrawdownPercent = 18.2, rSquared = 0.94, slope = 0.0008, intercept = 5.23),
    TrendResult(asset = "GAZP", dailyRatePercent = -0.02, monthlyRatePercent = -0.60, annualRatePercent = -7.0, totalPeriodReturnPercent = -12.3, maxDrawdownPercent = 34.5, rSquared = 0.65, slope = -0.0002, intercept = 5.10),
    TrendResult(asset = "LKOH", dailyRatePercent = 0.09, monthlyRatePercent = 2.75, annualRatePercent = 38.2, totalPeriodReturnPercent = 154.8, maxDrawdownPercent = 14.1, rSquared = 0.96, slope = 0.0009, intercept = 8.85),
    TrendResult(asset = "YNDX", dailyRatePercent = 0.12, monthlyRatePercent = 3.65, annualRatePercent = 53.8, totalPeriodReturnPercent = 210.5, maxDrawdownPercent = 25.6, rSquared = 0.88, slope = 0.0012, intercept = 8.28),
    TrendResult(asset = "NVTK", dailyRatePercent = 0.04, monthlyRatePercent = 1.21, annualRatePercent = 15.6, totalPeriodReturnPercent = 48.2, maxDrawdownPercent = 21.4, rSquared = 0.82, slope = 0.0004, intercept = 7.15),
    TrendResult(asset = "ROSN", dailyRatePercent = 0.06, monthlyRatePercent = 1.82, annualRatePercent = 24.1, totalPeriodReturnPercent = 85.3, maxDrawdownPercent = 16.8, rSquared = 0.91, slope = 0.0006, intercept = 6.35),
    TrendResult(asset = "GMKN", dailyRatePercent = -0.03, monthlyRatePercent = -0.91, annualRatePercent = -10.4, totalPeriodReturnPercent = -22.1, maxDrawdownPercent = 38.7, rSquared = 0.73, slope = -0.0003, intercept = 9.60),
    TrendResult(asset = "MOEX", dailyRatePercent = 0.07, monthlyRatePercent = 2.15, annualRatePercent = 28.9, totalPeriodReturnPercent = 104.2, maxDrawdownPercent = 15.4, rSquared = 0.93, slope = 0.0007, intercept = 5.40),
    TrendResult(asset = "PLZL", dailyRatePercent = 0.10, monthlyRatePercent = 3.10, annualRatePercent = 44.2, totalPeriodReturnPercent = 175.6, maxDrawdownPercent = 19.8, rSquared = 0.89, slope = 0.0010, intercept = 9.45),
    TrendResult(asset = "TATN", dailyRatePercent = 0.05, monthlyRatePercent = 1.55, annualRatePercent = 20.3, totalPeriodReturnPercent = 68.7, maxDrawdownPercent = 17.5, rSquared = 0.87, slope = 0.0005, intercept = 6.42),
    TrendResult(asset = "MGNT", dailyRatePercent = 0.03, monthlyRatePercent = 0.95, annualRatePercent = 12.0, totalPeriodReturnPercent = 36.4, maxDrawdownPercent = 22.3, rSquared = 0.79, slope = 0.0003, intercept = 8.65),
    TrendResult(asset = "OZON", dailyRatePercent = 0.14, monthlyRatePercent = 4.20, annualRatePercent = 63.5, totalPeriodReturnPercent = 260.0, maxDrawdownPercent = 31.2, rSquared = 0.85, slope = 0.0014, intercept = 8.10),
    TrendResult(asset = "CHMF", dailyRatePercent = 0.05, monthlyRatePercent = 1.50, annualRatePercent = 19.6, totalPeriodReturnPercent = 65.2, maxDrawdownPercent = 20.1, rSquared = 0.86, slope = 0.0005, intercept = 7.30),
    TrendResult(asset = "NLMK", dailyRatePercent = 0.02, monthlyRatePercent = 0.61, annualRatePercent = 7.6, totalPeriodReturnPercent = 22.4, maxDrawdownPercent = 26.8, rSquared = 0.75, slope = 0.0002, intercept = 5.25),
    TrendResult(asset = "MAGN", dailyRatePercent = 0.01, monthlyRatePercent = 0.30, annualRatePercent = 3.7, totalPeriodReturnPercent = 10.8, maxDrawdownPercent = 29.4, rSquared = 0.68, slope = 0.0001, intercept = 3.90),
    TrendResult(asset = "ALRS", dailyRatePercent = -0.04, monthlyRatePercent = -1.20, annualRatePercent = -13.5, totalPeriodReturnPercent = -28.4, maxDrawdownPercent = 42.1, rSquared = 0.62, slope = -0.0004, intercept = 4.20),
    TrendResult(asset = "VTBR", dailyRatePercent = -0.01, monthlyRatePercent = -0.30, annualRatePercent = -3.5, totalPeriodReturnPercent = -6.8, maxDrawdownPercent = 36.0, rSquared = 0.58, slope = -0.0001, intercept = -3.20),
    TrendResult(asset = "PHOR", dailyRatePercent = 0.04, monthlyRatePercent = 1.25, annualRatePercent = 16.0, totalPeriodReturnPercent = 50.5, maxDrawdownPercent = 16.2, rSquared = 0.90, slope = 0.0004, intercept = 8.80),
    TrendResult(asset = "SNGS", dailyRatePercent = 0.02, monthlyRatePercent = 0.60, annualRatePercent = 7.5, totalPeriodReturnPercent = 22.0, maxDrawdownPercent = 24.5, rSquared = 0.72, slope = 0.0002, intercept = 3.40),
    TrendResult(asset = "SNGSP", dailyRatePercent = 0.08, monthlyRatePercent = 2.40, annualRatePercent = 32.8, totalPeriodReturnPercent = 120.0, maxDrawdownPercent = 18.0, rSquared = 0.88, slope = 0.0008, intercept = 4.00),
    TrendResult(asset = "TRNFP", dailyRatePercent = 0.03, monthlyRatePercent = 0.90, annualRatePercent = 11.4, totalPeriodReturnPercent = 34.2, maxDrawdownPercent = 19.0, rSquared = 0.81, slope = 0.0003, intercept = 7.20),
    TrendResult(asset = "IRAO", dailyRatePercent = 0.01, monthlyRatePercent = 0.31, annualRatePercent = 3.8, totalPeriodReturnPercent = 11.2, maxDrawdownPercent = 23.0, rSquared = 0.70, slope = 0.0001, intercept = 1.40),
    TrendResult(asset = "HYDR", dailyRatePercent = -0.02, monthlyRatePercent = -0.62, annualRatePercent = -7.2, totalPeriodReturnPercent = -12.8, maxDrawdownPercent = 31.0, rSquared = 0.64, slope = -0.0002, intercept = -0.35),
    TrendResult(asset = "CBOM", dailyRatePercent = 0.02, monthlyRatePercent = 0.65, annualRatePercent = 8.1, totalPeriodReturnPercent = 24.5, maxDrawdownPercent = 15.8, rSquared = 0.83, slope = 0.0002, intercept = 2.05),
    TrendResult(asset = "AFKS", dailyRatePercent = 0.06, monthlyRatePercent = 1.80, annualRatePercent = 23.5, totalPeriodReturnPercent = 82.0, maxDrawdownPercent = 35.0, rSquared = 0.77, slope = 0.0006, intercept = 2.80),
    TrendResult(asset = "PIKK", dailyRatePercent = 0.05, monthlyRatePercent = 1.50, annualRatePercent = 19.5, totalPeriodReturnPercent = 64.0, maxDrawdownPercent = 28.0, rSquared = 0.80, slope = 0.0005, intercept = 6.40),
    TrendResult(asset = "RUAL", dailyRatePercent = -0.03, monthlyRatePercent = -0.90, annualRatePercent = -10.2, totalPeriodReturnPercent = -21.5, maxDrawdownPercent = 40.5, rSquared = 0.66, slope = -0.0003, intercept = 3.60),
    TrendResult(asset = "POSI", dailyRatePercent = 0.15, monthlyRatePercent = 4.50, annualRatePercent = 69.8, totalPeriodReturnPercent = 310.0, maxDrawdownPercent = 27.5, rSquared = 0.92, slope = 0.0015, intercept = 7.80),
    TrendResult(asset = "HEAD", dailyRatePercent = 0.11, monthlyRatePercent = 3.30, annualRatePercent = 47.5, totalPeriodReturnPercent = 190.0, maxDrawdownPercent = 22.0, rSquared = 0.89, slope = 0.0011, intercept = 8.05),
    TrendResult(asset = "TCSG", dailyRatePercent = 0.09, monthlyRatePercent = 2.70, annualRatePercent = 37.5, totalPeriodReturnPercent = 150.0, maxDrawdownPercent = 29.0, rSquared = 0.86, slope = 0.0009, intercept = 7.90)
)

@Composable
fun ConfigPage(onNext: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Config")
        Button(onClick = onNext) { Text("Next") }
    }
}

@Composable
fun ResultsPage(onOpenChart: (ticker: String) -> Unit) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Results")
        Button(onClick = { onOpenChart("AMFL") }) { Text("Chart AMFL") }
    }
}

@Composable
fun ChartPage(ticker: String, monthsBack: Int) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Chart: $ticker, $monthsBack months")
    }
}
