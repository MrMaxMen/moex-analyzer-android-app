package com.moex_assets_analyzer.app.ui

import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.VerticalDivider
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moex_assets_analyzer.app.viewmodels.ResultsPageUiState
import com.moex_assets_analyzer.app.viewmodels.ResultsVM
import java.util.Locale

fun Double.format(digits: Int = 4): String = String.format(Locale.US, "%.${digits}f", this)

fun rateBackgroundColor(value: Double): Color = when {
    value > 0 -> Color(0x334CAF50) // полупрозрачный зелёный
    value < 0 -> Color(0x33F44336) // полупрозрачный красный
    else -> Color.Transparent
}

@Composable
@Preview(showSystemUi = true)
fun ResultsTablePagePreview() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            ResultsTablePage(
                ResultsPageUiState(
                    trendResultList = sampleTrendResultList
                )
            )
        }
    }
}

@Composable
fun ResultsTablePageRout(vm: ResultsVM) {
    ResultsTablePage(vm.uiState)
}

@Composable
fun ResultsTablePage(
    uiState: ResultsPageUiState
) {
    val trendResult = uiState.trendResultList

    if (uiState.isLoading) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (uiState.errorMessage != null) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(text = uiState.errorMessage)
        }
        return
    }

    val horizontalScrollState = rememberScrollState()

    val assetColumnWidth = 70.dp
    val metricColumnWidth = 94.dp



    CompositionLocalProvider(LocalOverscrollFactory provides null) {
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            stickyHeader {
                Surface(
                    tonalElevation = 3.dp,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {

                        Text(
                            text = "Asset",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(assetColumnWidth)
                                .padding(8.dp)
                        )
                        VerticalDivider()
                        VerticalDivider()

                        Row(modifier = Modifier.horizontalScroll(horizontalScrollState).height(IntrinsicSize.Min)) {
                            Text(
                                "Daily",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            VerticalDivider()
                            Text(
                                "Monthly",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            VerticalDivider()
                            Text(
                                "Annual",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            VerticalDivider()
                            Text(
                                "Total",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            VerticalDivider()
                            Text(
                                "Max DD",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            VerticalDivider()
                            Text(
                                "R²",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                        }
                    }
                    HorizontalDivider()
                }
                HorizontalDivider()
            }

            items(trendResult) { item ->

                Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {

                    Text(
                        text = item.asset,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center,
                        maxLines = 1,
                        modifier = Modifier
                            .width(assetColumnWidth)
                            .horizontalScroll(rememberScrollState())
                            .padding(8.dp)
                    )

                    VerticalDivider()
                    VerticalDivider()

                    Row(modifier = Modifier.horizontalScroll(horizontalScrollState).height(IntrinsicSize.Min)) {
                        Text(
                            text = "${item.dailyRatePercent.format(2)}%",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(metricColumnWidth)
                                .background(rateBackgroundColor(item.dailyRatePercent))
                                .padding(8.dp)
                        )
                        VerticalDivider()
                        Text(
                            text = "${item.monthlyRatePercent.format(2)}%",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(metricColumnWidth)
                                .background(rateBackgroundColor(item.monthlyRatePercent))
                                .padding(8.dp)
                        )
                        VerticalDivider()
                        Text(
                            text = "${item.annualRatePercent.format(2)}%",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(metricColumnWidth)
                                .background(rateBackgroundColor(item.annualRatePercent))
                                .padding(8.dp)
                        )
                        VerticalDivider()
                        Text(
                            text = "${item.totalPeriodReturnPercent.format(2)}%",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(metricColumnWidth)
                                .background(rateBackgroundColor(item.totalPeriodReturnPercent))
                                .padding(8.dp)
                        )
                        VerticalDivider()
                        Text(
                            text = "${item.maxDrawdownPercent.format(2)}%",
                            textAlign = TextAlign.Center,
                            modifier = Modifier
                                .width(metricColumnWidth)
                                .background(rateBackgroundColor(-item.maxDrawdownPercent.coerceAtLeast(0.0).let { if (item.maxDrawdownPercent > 0) -item.maxDrawdownPercent else item.maxDrawdownPercent }))
                                .padding(8.dp)
                        )
                        VerticalDivider()
                        Text(
                            text = item.rSquared.format(2),
                            textAlign = TextAlign.Center,
                            modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                        )
                    }
                }
                HorizontalDivider()
            }
        }
    }
}
