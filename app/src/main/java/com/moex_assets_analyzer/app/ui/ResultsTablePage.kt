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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.moex_assets_analyzer.app.R
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
    ResultsTablePage(
        uiState = vm.uiState,
        onRetry = { vm.loadMetrics() }
    )
}

@Composable
fun ResultsTablePage(
    uiState: ResultsPageUiState,
    onRetry: (() -> Unit)? = null
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
        ConnectionErrorScreen(
            errorMessage = uiState.errorMessage,
            onRetry = onRetry
        )
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
                            text = stringResource(R.string.col_asset),
                            fontWeight = FontWeight.Bold,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier
                                .width(assetColumnWidth)
                                .padding(8.dp)
                        )

                        Row(modifier = Modifier.horizontalScroll(horizontalScrollState).height(IntrinsicSize.Min)) {
                            Text(
                                stringResource(R.string.col_daily),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            Text(
                                stringResource(R.string.col_monthly),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            Text(
                                stringResource(R.string.col_annual),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            Text(
                                stringResource(R.string.col_total),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            Text(
                                stringResource(R.string.col_max_dd),
                                fontWeight = FontWeight.Bold,
                                textAlign = TextAlign.Center,
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.width(metricColumnWidth).padding(8.dp)
                            )
                            Text(
                                stringResource(R.string.col_r_squared),
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
                            text = stringResource(R.string.analysis_start_date),
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = selectedDate?.format(DateTimeFormatter.ofPattern("d MMMM yyyy", Locale.getDefault()))
                                ?: stringResource(R.string.date_not_specified),
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
                    Text(if (isExplanationExpanded) stringResource(R.string.hide_info) else stringResource(R.string.metrics_info))
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
                        text = stringResource(R.string.metrics_guide),
                        style = MaterialTheme.typography.titleSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.padding(bottom = 6.dp)
                    )
                    MetricExplanationItem(
                        title = stringResource(R.string.col_daily),
                        description = stringResource(R.string.desc_daily)
                    )
                    MetricExplanationItem(
                        title = stringResource(R.string.col_monthly),
                        description = stringResource(R.string.desc_monthly)
                    )
                    MetricExplanationItem(
                        title = stringResource(R.string.col_annual),
                        description = stringResource(R.string.desc_annual)
                    )
                    MetricExplanationItem(
                        title = stringResource(R.string.col_total),
                        description = stringResource(R.string.desc_total)
                    )
                    MetricExplanationItem(
                        title = stringResource(R.string.col_max_dd),
                        description = stringResource(R.string.desc_max_dd)
                    )
                    MetricExplanationItem(
                        title = stringResource(R.string.col_r_squared),
                        description = stringResource(R.string.desc_r_squared)
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
