package com.moex_assets_analyzer.app.ui

import android.widget.Toast
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moex_assets_analyzer.app.viewmodels.Dates
import com.moex_assets_analyzer.app.viewmodels.SelectionPageUiState
import com.moex_assets_analyzer.app.viewmodels.SelectionStep
import com.moex_assets_analyzer.app.viewmodels.SelectionVM

@Preview(showSystemUi = true)
@Composable
fun SelectionPagePreview() {
    AppTheme {
        Scaffold(
            modifier = Modifier.fillMaxSize()
        ) { innerPadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                SelectionPage(
                    uiState = SelectionPageUiState(
                        userInput = "",
                        assetNames = listOf("AMFL", "AKMM", "LQDT"),
                        selectionStep = SelectionStep.ASSETS_SELECTION,
                        selectedAssets = setOf("AMFL")
                    ),
                    onNext = {}
                )
            }
        }
    }
}

@Composable
fun SelectionPageRoute(
    vm: SelectionVM,
    onNext: () -> Unit
) {
    SelectionPage(
        uiState = vm.uiState,
        actions = vm.actions,
        onNext = onNext
    )
}



@Composable
fun SelectionPage(
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
                    AssetsSelectionScreen(
                        uiState = uiState,
                        actions = actions,
                        modifier = Modifier.weight(1f)
                    )
                }

                SelectionStep.DATE_SELECTION -> {
                    DateSelectionScreen(
                        actions = actions,
                        modifier = Modifier.weight(1f),
                        onNext = onNext,
                        selectedDate = uiState.selectedDate
                    )
                }
            }
        }
    }
}



@Composable
private fun AssetsSelectionScreen(
    uiState: SelectionPageUiState,
    actions: SelectionVM.Actions?,
    modifier: Modifier = Modifier
) {
    val isInspectionMode = LocalInspectionMode.current
    var isTextFieldFocus by remember { mutableStateOf(false) }
    val isSearchActive = (isTextFieldFocus && !isInspectionMode) || uiState.userInput.isNotEmpty()

    val selectedAssets = uiState.selectedAssets
    val filteredAssets = uiState.assetNames
        .filter { it !in selectedAssets }
        .filter { it.contains(uiState.userInput, ignoreCase = true) }

    val topSpacerHeight by animateDpAsState(
        targetValue = if (isSearchActive) 0.dp else 180.dp,
        animationSpec = tween(durationMillis = 600)
    )

    val middleSpacerHeight by animateDpAsState(
        targetValue = if (isSearchActive) 0.dp else 140.dp,
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
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = "Welcome to",
                    fontSize = 23.sp,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Moex Analyzer",
                    fontSize = 43.sp,
                    textAlign = TextAlign.Center
                )
                HorizontalDivider(
                    modifier = Modifier
                        .width(283.dp)
                        ,
                    thickness = 3.dp,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }

        Spacer(
            modifier = Modifier
                .height(middleSpacerHeight)
                .fillMaxWidth()
        )

        TextField(
            modifier = Modifier
                .padding(horizontal = 30.dp)
                .fillMaxWidth()
                .onFocusChanged { focusState ->
                    isTextFieldFocus = focusState.isFocused
                },
            placeholder = {
                Text("Enter asset name")
            },
            shape = RoundedCornerShape(12.dp),
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
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 30.dp)
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
                onClick = { actions?.onDateSelectionStep() },
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
private fun DateSelectionScreen(
    actions: SelectionVM.Actions?,
    modifier: Modifier = Modifier,
    selectedDate: Dates?,
    onNext: () -> Unit
) {
    val context = LocalContext.current
    val dates = Dates.entries.toList()

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
                modifier = Modifier.padding(horizontal = 50.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = "Records Since: ${selectedDate ?: "not selected"}",
                    Modifier.padding(bottom = 50.dp)
                )

                Spacer(Modifier.fillMaxWidth().height(50.dp))

                dates.forEach {
                    Button(
                        onClick = {
                            actions?.onDateSelection(it)
                        }
                    ) {
                        Text(it.toString())
                    }
                }

            }
        }

        Spacer(modifier = Modifier.fillMaxWidth().weight(0.1f))

        Button(
            onClick = {
                if (selectedDate != null) {
                    actions?.saveSelectionsToRepository()
                    onNext()
                } else {
                    Toast.makeText(context, "Please select a date", Toast.LENGTH_SHORT).show()
                }
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