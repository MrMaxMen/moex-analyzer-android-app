package com.moex_assets_analyzer.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moex_assets_analyzer.app.viewmodels.MetricsSelection
import com.moex_assets_analyzer.app.viewmodels.SelectionPageUiState
import com.moex_assets_analyzer.app.viewmodels.SelectionVM

@Preview(showSystemUi = true)
@Composable
fun AssetSelectionPagePreview() {
    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            AssetSelectionPage(
                uiState = SelectionPageUiState(
                    userInput = "A",
                    assetNames = listOf("AMFL", "AKMM"),
                    isMetricsSelection = false
                ),
                onInputChange = {},
                onNext = {},
                onMetricsSelectionChange = {},
                onMetricsSelection = {}
            )
        }
    }
}

@Composable
fun AssetSelectionPageRoute(
    vm: SelectionVM,
    onNext: () -> Unit
){
    AssetSelectionPage(
        vm.uiState,
        vm::onInputChange,
        vm::onMetricsSelection ,
        vm::onMetricsSelectionChange,
        onNext
    )
}

@Composable
fun AssetSelectionPage(
    uiState: SelectionPageUiState,
    onInputChange: (String) -> Unit,
    onMetricsSelection : () -> Unit,
    onMetricsSelectionChange: (MetricsSelection) -> Unit,
    onNext: () -> Unit
) {
    val hasText = uiState.userInput.isNotEmpty()
    val filteredAssets = uiState.assetNames.filter {
        it.contains(uiState.userInput, ignoreCase = true)
    }
    val isMetricsSelection = uiState.isMetricsSelection
    val metricsSelection = uiState.metricsSelection

    val topSpacerHeight by animateDpAsState(
        targetValue = if (hasText) 0.dp else 180.dp,
        animationSpec = tween(durationMillis = 600)
    )

    val middleSpacerHeight by animateDpAsState(
        targetValue = if (hasText) 0.dp else 200.dp,
        animationSpec = tween(durationMillis = 600)
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if(!isMetricsSelection) {
                Spacer(modifier = Modifier.height(topSpacerHeight))

                AnimatedVisibility(
                    visible = !hasText,
                    enter = fadeIn(animationSpec = tween(600)) + expandVertically(
                        animationSpec = tween(
                            600
                        )
                    ),
                    exit = fadeOut(animationSpec = tween(600)) + shrinkVertically(
                        animationSpec = tween(
                            600
                        )
                    )
                ) {
                    Text(
                        text = "Welcome to Moex Analyzer",
                        fontSize = 27.sp
                    )
                }

                Spacer(
                    modifier = Modifier
                        .height(middleSpacerHeight)
                        .fillMaxWidth()
                )

                TextField(
                    modifier = Modifier.fillMaxWidth(),
                    value = uiState.userInput,
                    onValueChange = onInputChange
                )

                AnimatedVisibility(
                    visible = hasText,
                    enter = fadeIn(animationSpec = tween(600)) + expandVertically(
                        animationSpec = tween(
                            600
                        )
                    ),
                    exit = fadeOut(animationSpec = tween(600)) + shrinkVertically(
                        animationSpec = tween(
                            600
                        )
                    )
                ) {
                    Column {
                        filteredAssets.forEach { assetName ->
                            Text(
                                text = assetName,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(30.dp)
                                    .padding(start = 15.dp, top = 5.dp)
                            )
                        }
                    }
                }


                Spacer(modifier = Modifier.fillMaxWidth().weight(1f))

                Button(
                    onClick = {
                        onMetricsSelection()
                        onNext()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 25.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Next")
                }
            }else{

                Box(
                    modifier = Modifier
                        .weight(1f),
                    contentAlignment = Alignment.Center
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 50.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = metricsSelection.dailyRatePercent,
                                onCheckedChange = {
                                    onMetricsSelectionChange(metricsSelection.copy(dailyRatePercent = it))
                                }
                            )
                            Text("Daily Growth Rate (%)")
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = metricsSelection.monthlyRatePercent,
                                onCheckedChange = {
                                    onMetricsSelectionChange(
                                        metricsSelection.copy(
                                            monthlyRatePercent = it
                                        )
                                    )
                                }
                            )
                            Text("Monthly Return (%)")
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = metricsSelection.annualRatePercent,
                                onCheckedChange = {
                                    onMetricsSelectionChange(metricsSelection.copy(annualRatePercent = it))
                                }
                            )
                            Text("Annual Return (CAGR, %)")
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = metricsSelection.totalPeriodReturnPercent,
                                onCheckedChange = {
                                    onMetricsSelectionChange(
                                        metricsSelection.copy(
                                            totalPeriodReturnPercent = it
                                        )
                                    )
                                }
                            )
                            Text("Total Period Return (%)")
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = metricsSelection.maxDrawdownPercent,
                                onCheckedChange = {
                                    onMetricsSelectionChange(
                                        metricsSelection.copy(
                                            maxDrawdownPercent = it
                                        )
                                    )
                                }
                            )
                            Text("Maximum Drawdown (%)")
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Checkbox(
                                checked = metricsSelection.rSquared,
                                onCheckedChange = {
                                    onMetricsSelectionChange(metricsSelection.copy(rSquared = it))
                                }
                            )
                            Text("Coefficient of Determination (R²)")
                        }
                    }
                }


                Spacer(modifier = Modifier.fillMaxWidth().weight(0.5f))

                Button(
                    onClick = {
                        TODO()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp)
                        .padding(horizontal = 25.dp),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text("Next")
                }
            }
        }
    }
}