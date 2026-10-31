package com.moex_assets_analyzer.app.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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
            AssetSelectionPage({})
        }
    }
}

@Composable
fun AssetSelectionPage(onNext: () -> Unit) {
    var text by remember { mutableStateOf("") }
    val list = List(10) { it * 2 }

    val hasText = text.isNotEmpty()


    val topSpacerHeight by animateDpAsState(
        targetValue = if (hasText) 0.dp else 180.dp,
        label = "topSpacerHeight"
    )

    val middleSpacerHeight by animateDpAsState(
        targetValue = if (hasText) 0.dp else 200.dp,
        label = "middleSpacerHeight"
    )

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.TopCenter
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Spacer(modifier = Modifier.height(topSpacerHeight))


            AnimatedVisibility(
                visible = !hasText,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Text(
                    text = "Welcome to Moex Analyzer",
                    fontSize = 27.sp
                )
            }


            Spacer(modifier = Modifier.height(middleSpacerHeight).fillMaxWidth())


            TextField(
                modifier = Modifier.fillMaxWidth(),
                value = text,
                onValueChange = { text = it }
            )


            AnimatedVisibility(
                visible = hasText,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column {
                    list.forEach { item ->
                        Text(
                            text = item.toString(),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(30.dp)
                                .padding(start = 15.dp, top = 5.dp)
                        )
                    }
                }
            }
        }
    }
}