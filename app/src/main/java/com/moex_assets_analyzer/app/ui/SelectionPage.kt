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
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.selectable
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalInspectionMode
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.moex_assets_analyzer.app.R
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
                    AssetsSelectionStep(
                        uiState = uiState,
                        actions = actions,
                        modifier = Modifier.weight(1f)
                    )
                }

                SelectionStep.DATE_SELECTION -> {
                    DateSelectionStep(
                        actions = actions,
                        modifier = Modifier.weight(1f),
                        onNext = onNext,
                        selectedDate = uiState.selectedDate,
                        selectedAssets = uiState.selectedAssets
                    )
                }
            }
        }
    }
}





@Composable
private fun AssetsSelectionStep(
    uiState: SelectionPageUiState,
    actions: SelectionVM.Actions?,
    modifier: Modifier = Modifier
) {
    if (uiState.isLoading) {
        Box(
            modifier = modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
        return
    }

    if (uiState.errorMessage != null) {
        ConnectionErrorScreen(
            errorMessage = uiState.errorMessage,
            modifier = modifier,
            onRetry = { actions?.onRetryLoadAssetNames() }
        )
        return
    }

    val isInspectionMode = LocalInspectionMode.current
    var isTextFieldFocus by remember { mutableStateOf(false) }
    val isSearchActive = (isTextFieldFocus && !isInspectionMode) || uiState.userInput.isNotEmpty()

    BackHandler {
        isTextFieldFocus = false
        actions?.onInputChange("")
    }

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

    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(topSpacerHeight))

            WelcomeHeader(visible = !isSearchActive)

            Spacer(modifier = Modifier.height(middleSpacerHeight))

            TextField(
                modifier = Modifier
                    .padding(horizontal = 30.dp, vertical = if(isSearchActive) 12.dp else 0.dp)
                    .fillMaxWidth()
                    .onFocusChanged { focusState ->
                        isTextFieldFocus = focusState.isFocused
                    },
                placeholder = {
                    Text(stringResource(R.string.search_placeholder))
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
                    if (selectedAssets.isNotEmpty()) {
                        item(key = "selected_assets_flow") {
                            SelectedAssetsSection(
                                selectedAssets = selectedAssets,
                                onRemoveAsset = { actions?.onAssetCancelSelection(it) }
                            )
                        }
                    }

                    items(
                        items = filteredAssets,
                        key = { "search_$it" }
                    ) { assetName ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 15.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .clickable(onClick = {actions?.onAssetSelection(assetName)})
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

                            Icon(
                                imageVector = Icons.Default.Add,
                                contentDescription = stringResource(R.string.content_desc_add_asset)
                            )

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
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 30.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {

                    IconButton(
                        onClick = {
                            isTextFieldFocus = false
                            actions?.onInputChange("")
                        },
                        modifier = Modifier
                            .height(50.dp)
                            .weight(0.3f),
                        colors = IconButtonDefaults.iconButtonColors(
                            containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
                        ),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.content_desc_go_back)
                        )
                    }

                    Button(
                        onClick = { actions?.onDateSelectionStep() },
                        modifier = Modifier
                            .height(50.dp)
                            .weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(stringResource(R.string.btn_next))
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = !isSearchActive,
            enter = fadeIn(animationSpec = tween(600)),
            exit = fadeOut(animationSpec = tween(600)),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 12.dp, end = 16.dp)
        ) {
            LanguageToggleButton()
        }
    }
}


@Composable
private fun WelcomeHeader(
    visible: Boolean,
    modifier: Modifier = Modifier
) {
    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(animationSpec = tween(600)) + expandVertically(tween(600)),
        exit = fadeOut(animationSpec = tween(600)) + shrinkVertically(tween(600)),
        modifier = modifier
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = stringResource(R.string.welcome_to),
                fontSize = 23.sp,
                textAlign = TextAlign.Center
            )
            Text(
                text = stringResource(R.string.app_name),
                fontSize = 43.sp,
                textAlign = TextAlign.Center
            )
            HorizontalDivider(
                modifier = Modifier.width(290.dp),
                thickness = 3.dp,
                color = MaterialTheme.colorScheme.primary
            )
        }
    }
}



@Composable
 fun SelectedAssetsSection(
    selectedAssets: Set<String>,
    onRemoveAsset: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    if (selectedAssets.isEmpty()) return

    FlowRow(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 10.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        selectedAssets.forEach { assetName ->
            Surface(
                shape = RoundedCornerShape(16.dp),
                color = MaterialTheme.colorScheme.primaryContainer,
                contentColor = MaterialTheme.colorScheme.onPrimaryContainer
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(start = 12.dp, end = 4.dp, top = 2.dp, bottom = 2.dp)
                ) {
                    Text(
                        text = assetName,
                        fontSize = 14.sp
                    )
                    IconButton(
                        onClick = { onRemoveAsset(assetName) },
                        modifier = Modifier.size(24.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Clear,
                            contentDescription = stringResource(R.string.content_desc_remove_asset, assetName),
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }
        }
    }
}


@Composable
private fun DateSelectionStep(
    actions: SelectionVM.Actions?,
    modifier: Modifier = Modifier,
    selectedDate: Dates?,
    onNext: () -> Unit,
    selectedAssets: Set<String>
) {
    val context = LocalContext.current
    val dates = Dates.entries.toList()

    BackHandler {
        actions?.onBackToAssetsSelection()
    }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(top = 7.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        StageTopBar(
            title = stringResource(R.string.select_date_title),
            onBackClick = { actions?.onBackToAssetsSelection() }
        )

        Column(
            modifier = Modifier
                .weight(1f)
                .fillMaxWidth()
                .verticalScroll(rememberScrollState())
                .padding(top = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                modifier = Modifier.padding(horizontal = 30.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Text(
                    text = stringResource(R.string.assets_count, selectedAssets.size),
                    modifier = Modifier
                        .alpha(0.7f)
                        .align(Alignment.Start)
                )

                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 10.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    selectedAssets.forEach { assetName ->
                        Surface(
                            shape = RoundedCornerShape(16.dp),
                            color = MaterialTheme.colorScheme.primaryContainer,
                            contentColor = MaterialTheme.colorScheme.onPrimaryContainer
                        ) {


                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.padding(vertical = 2.dp, horizontal = 8.dp)
                            ) {
                                Text(
                                    text = assetName,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }


                Text(
                    text = stringResource(R.string.records_since),
                    modifier = Modifier
                        .align(Alignment.Start)
                )

                Spacer(Modifier.fillMaxWidth().height(30.dp))

                Surface(
                    modifier = Modifier
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(15.dp),
                    color = MaterialTheme.colorScheme.surfaceContainerHighest
                ) {

                    Column {

                        dates.forEach {
                            val isSelected = (selectedDate == it)
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(70.dp)
                                    .padding(10.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .selectable(
                                        selected = isSelected,
                                        onClick = { actions?.onDateSelection(it) },
                                    )
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                RadioButton(
                                    selected = isSelected,
                                    onClick = null
                                )
                                Text(
                                    text = it.value.toString(),
                                    modifier = Modifier
                                        .alpha(0.4f)
                                        .padding(start = 5.dp)
                                )
                                Spacer(Modifier.weight(1f))
                                Text(text = stringResource(it.textResId))

                            }

                        }
                    }
                }

            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                if (selectedDate != null) {
                    actions?.saveSelectionsToRepository()
                    onNext()
                } else {
                    Toast.makeText(context, R.string.toast_select_date, Toast.LENGTH_SHORT).show()
                }
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .padding(horizontal = 25.dp),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(stringResource(R.string.btn_analyze))
        }
    }
}
