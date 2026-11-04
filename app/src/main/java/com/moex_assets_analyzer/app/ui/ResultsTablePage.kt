package com.moex_assets_analyzer.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.moex_assets_analyzer.app.viewmodels.ResultsVM
import androidx.compose.foundation.lazy.items
import kotlin.collections.listOf


@Composable
@Preview(showSystemUi = true)
fun ResultsTablePagePreview(){
    ResultsTablePage()
}

@Composable
fun ResultsTablePageRout(vm : ResultsVM){

}



@Composable
fun ResultsTablePage(

){
    Box(
        modifier = Modifier.fillMaxSize()
    ){

        Column() {
            Text("Rows")
            LazyRow(){
                stickyHeader(){
                    Column() {

                    }
                }
                items(
                    items =listOf<String>(),
                    key = {it}
                ){

                }
            }
        }
    }
}