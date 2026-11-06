package com.moex_assets_analyzer.app.ui

import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moex_assets_analyzer.app.viewmodels.MetricsSelection
import com.moex_assets_analyzer.app.viewmodels.SelectionPageUiState
import com.moex_assets_analyzer.app.viewmodels.SelectionStep
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
                    assetNames = listOf("AMFL", "AKMM", "LQDT"),
                    selectionStep = SelectionStep.ASSETS_SELECTION,
                    selectedAssets = setOf("AMFL")
                ),
                onNext = {}
            )
        }
    }
}

@Composable
fun AssetSelectionPageRoute(
    vm: SelectionVM,
    onNext: () -> Unit
) {
    AssetSelectionPage(
        uiState = vm.uiState,
        actions = vm.actions,
        onNext = onNext
    )
}

@Composable
fun AssetSelectionPage(
    uiState: SelectionPageUiState,
    actions: SelectionVM.Actions? = null,
    onNext: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .imePadding(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            when (uiState.selectionStep) {
                SelectionStep.ASSETS_SELECTION -> {
                    AssetsSelectionStage(
                        uiState = uiState,
                        actions = actions,
                        modifier = Modifier.weight(1f)
                    )
                }

                SelectionStep.METRICS_SELECTION -> {
                    MetricsSelectionPage(
                        metricsSelection = uiState.metricsSelection,
                        actions = actions,
                        modifier = Modifier.weight(1f),
                        onNext = onNext
                    )
                }
            }
        }
    }
}

@Composable
private fun AssetsSelectionStage(
    uiState: SelectionPageUiState,
    actions: SelectionVM.Actions?,
    modifier: Modifier = Modifier
) {
    var isTextFieldFocus by remember { mutableStateOf(false) }
    val isSearchActive = isTextFieldFocus || uiState.userInput.isNotEmpty()

    val selectedAssets = uiState.selectedAssets
    val filteredAssets = uiState.assetNames
        .filter { it !in selectedAssets }
        .filter { it.contains(uiState.userInput, ignoreCase = true) }

    val topSpacerHeight by animateDpAsState(
        targetValue = if (isSearchActive) 0.dp else 180.dp,
        animationSpec = tween(durationMillis = 600)
    )

    val middleSpacerHeight by animateDpAsState(
        targetValue = if (isSearchActive) 0.dp else 200.dp,
        animationSpec = tween(durationMillis = 600)
    )

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(topSpacerHeight))

        AnimatedVisibility(
            visible = !isSearchActive,
            enter = fadeIn(animationSpec = tween(600)) + expandVertically(tween(600)),
            exit = fadeOut(animationSpec = tween(600)) + shrinkVertically(tween(600))
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
            modifier = Modifier
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isTextFieldFocus = focusState.isFocused
                },
            value = uiState.userInput,
            onValueChange = { actions?.onInputChange(it) }
        )

        AnimatedVisibility(
            visible = isSearchActive,
            modifier = Modifier.weight(1f),
            enter = fadeIn(animationSpec = tween(600)) + expandVertically(tween(600)),
            exit = fadeOut(animationSpec = tween(600)) + shrinkVertically(tween(600))
        ) {
            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(
                    items = selectedAssets.toList(),
                    key = { "selected_$it" }
                ) { assetName ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem()
                            .background(Color.Green),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = assetName,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 15.dp)
                        )

                        IconButton(
                            onClick = { actions?.onAssetCancelSelection(assetName) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Clear,
                                contentDescription = "remove asset"
                            )
                        }
                    }
                }

                items(
                    items = filteredAssets,
                    key = { "search_$it" }
                ) { assetName ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .animateItem(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = assetName,
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 15.dp)
                        )

                        IconButton(
                            onClick = { actions?.onAssetSelection(assetName) }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = "add asset"
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = isSearchActive && selectedAssets.isNotEmpty(),
            enter = slideInVertically(
                initialOffsetY = { it },
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioLowBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ) + fadeIn(animationSpec = tween(300)),
            exit = slideOutVertically(
                targetOffsetY = { it },
                animationSpec = tween(250)
            ) + fadeOut(animationSpec = tween(250))
        ) {
            Button(
                onClick = { actions?.onMetricsSelection() },
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

@Composable
private fun MetricsSelectionPage(
    metricsSelection: MetricsSelection,
    actions: SelectionVM.Actions?,
    modifier: Modifier = Modifier,
    onNext: () -> Unit
) {


    BackHandler {
        actions?.onBackToAssetsSelection()
    }

    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier.weight(1f),
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
                            actions?.onMetricsSelectionChange(metricsSelection.copy(dailyRatePercent = it))
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
                            actions?.onMetricsSelectionChange(
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
                            actions?.onMetricsSelectionChange(metricsSelection.copy(annualRatePercent = it))
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
                            actions?.onMetricsSelectionChange(
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
                            actions?.onMetricsSelectionChange(
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
                            actions?.onMetricsSelectionChange(metricsSelection.copy(rSquared = it))
                        }
                    )
                    Text("Coefficient of Determination (R²)")
                }
            }
        }

        Spacer(modifier = Modifier.fillMaxWidth().weight(0.1f))

        Button(
            onClick = {
                actions?.saveSelectionsToRepository()
                onNext()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 25.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("Analyze")
        }
    }
}