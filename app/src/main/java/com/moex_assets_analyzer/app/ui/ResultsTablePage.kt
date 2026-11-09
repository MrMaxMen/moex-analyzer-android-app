package com.moex_assets_analyzer.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.LocalOverscrollFactory
import androidx.compose.foundation.background
import androidx.compose.foundation.basicMarquee
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moex_assets_analyzer.app.viewmodels.ResultsPageUiState
import com.moex_assets_analyzer.app.viewmodels.ResultsVM
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Locale

fun Double.format(digits: Int = 4): String = String.format(Locale.US, "%.${digits}f", this)

fun rateBackgroundColor(value: Double): Color = when {
    value > 0 -> GainGreen.copy(alpha = 0.2f)
    value < 0 -> LossRed.copy(alpha = 0.2f)
    else -> Color.Transparent
}

@Composable
fun rateFontColor(value: Double): Color = when {
    value > 0 -> MaterialTheme.colorScheme.tertiary
    value < 0 -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.onSurface
}

@Composable
fun maxDrawdownFontColor(value: Double): Color = when {
    value != 0.0 -> MaterialTheme.colorScheme.error
    else -> MaterialTheme.colorScheme.onSurface
}

@Composable
@Preview(showSystemUi = true)
fun ResultsTablePagePreview() {
    AppTheme {
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
                        trendResultList = sampleTrendResultList,
                        selectedDate = LocalDate.now().minusMonths(1)
                    )
                )
            }
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

    val assetColumnWidth = 75.dp
    val metricColumnWidth = 94.dp

    CompositionLocalProvider(LocalOverscrollFactory provides null) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 10.dp)
        ) {
            item {
                ResultsHeaderSection(selectedDate = uiState.selectedDate)
                Spacer(modifier = Modifier.height(20.dp))
            }

            stickyHeader {
                Surface(
                    color = MaterialTheme.colorScheme.background,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Row(modifier = Modifier.fillMaxWidth().height(IntrinsicSize.Min)) {

                        Text(
                            text = "Asset",
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .width(assetColumnWidth)
                                .padding(8.dp)
                        )

                        Row(modifier = Modifier.horizontalScroll(horizontalScrollState).height(IntrinsicSize.Min)) {
                            Text(
                                "Daily",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            Text(
                                "Monthly",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            Text(
                                "Annual",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            Text(
                                "Total",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            Text(
                                "Max DD",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            Text(
                                "R²",
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                        }
                    }
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
                            .basicMarquee()
                            .padding(8.dp)
                    )

                    Row(modifier = Modifier.horizontalScroll(horizontalScrollState).height(IntrinsicSize.Min)) {
                        Text(
                            text = "${item.dailyRatePercent.format(2)}%",
                            textAlign = TextAlign.Center,
                            color = rateFontColor(item.dailyRatePercent),
                            modifier = Modifier
                                .width(metricColumnWidth)
                                .background(rateBackgroundColor(item.dailyRatePercent))
                                .padding(8.dp)
                        )
                        Text(
                            text = "${item.monthlyRatePercent.format(2)}%",
                            textAlign = TextAlign.Center,
                            color = rateFontColor(item.monthlyRatePercent),
                            modifier = Modifier
                                .width(metricColumnWidth)
                                .background(rateBackgroundColor(item.monthlyRatePercent))
                                .padding(8.dp)
                        )
                        Text(
                            text = "${item.annualRatePercent.format(2)}%",
                            textAlign = TextAlign.Center,
                            color = rateFontColor(item.annualRatePercent),
                            modifier = Modifier
                                .width(metricColumnWidth)
                                .background(rateBackgroundColor(item.annualRatePercent))
                                .padding(8.dp)
                        )
                        Text(
                            text = "${item.totalPeriodReturnPercent.format(2)}%",
                            textAlign = TextAlign.Center,
                            color = rateFontColor(item.totalPeriodReturnPercent),
                            modifier = Modifier
                                .width(metricColumnWidth)
                                .background(rateBackgroundColor(item.totalPeriodReturnPercent))
                                .padding(8.dp)
                        )
                        Text(
                            text = "${item.maxDrawdownPercent.format(2)}%",
                            textAlign = TextAlign.Center,
                            color = maxDrawdownFontColor(item.maxDrawdownPercent),
                            modifier = Modifier
                                .width(metricColumnWidth)
                                .padding(8.dp)
                        )
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

@Composable
fun ResultsHeaderSection(selectedDate: LocalDate?) {
    var isExplanationExpanded by remember { mutableStateOf(false) }

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Selected Date",
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(end = 8.dp)
                    )
                    Column {
                        Text(
                            text = "Analysis Start Date",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = selectedDate?.format(DateTimeFormatter.ofPattern("MMMM d, yyyy", Locale.US))
                                ?: "Not specified",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                TextButton(
                    onClick = { isExplanationExpanded = !isExplanationExpanded }
                ) {
                    Icon(
                        imageVector = Icons.Default.Info,
                        contentDescription = null,
                        modifier = Modifier.padding(end = 4.dp)
                    )
                    Text(if (isExplanationExpanded) "Hide Info" else "Metrics Info")
                }
            }

            AnimatedVisibility(visible = isExplanationExpanded) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 12.dp)
                ) {
                    HorizontalDivider(modifier = Modifier.padding(bottom = 8.dp))
                    Text(
                        text = "Metrics Guide",
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    MetricExplanationItem(
                        title = "Daily",
                        description = "Daily growth rate (%) from log-linear trend slope."
                    )
                    MetricExplanationItem(
                        title = "Monthly",
                        description = "Projected monthly return (%) compounding 30 days."
                    )
                    MetricExplanationItem(
                        title = "Annual",
                        description = "Annualized return (% CAGR) calculated from trend."
                    )
                    MetricExplanationItem(
                        title = "Total",
                        description = "Cumulative trend growth (%) over the whole period."
                    )
                    MetricExplanationItem(
                        title = "Max DD",
                        description = "Maximum drawdown (%) peak-to-trough drop."
                    )
                    MetricExplanationItem(
                        title = "R²",
                        description = "Coefficient of determination (0 to 1) for trend fit."
                    )
                }
            }
        }
    }
}

@Composable
private fun MetricExplanationItem(title: String, description: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 2.dp)
    ) {
        Text(
            text = "• $title: ",
            fontWeight = FontWeight.Bold,
            style = MaterialTheme.typography.bodySmall
        )
        Text(
            text = description,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}
