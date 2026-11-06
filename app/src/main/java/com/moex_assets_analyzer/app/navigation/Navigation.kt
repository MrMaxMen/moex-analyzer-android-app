package com.moex_assets_analyzer.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.moex_assets_analyzer.app.ui.AssetSelectionPageRoute
import com.moex_assets_analyzer.app.ui.ChartPage
import com.moex_assets_analyzer.app.ui.ConfigPage
import com.moex_assets_analyzer.app.ui.ResultsPage
import com.moex_assets_analyzer.app.ui.ResultsTablePageRout
import com.moex_assets_analyzer.app.viewmodels.ResultsVM
import com.moex_assets_analyzer.app.viewmodels.SelectionVM


@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SelectionRoute,
        modifier = modifier
    ) {
        composable<SelectionRoute> {
            val vm: SelectionVM = viewModel(factory = SelectionVM.Factory)
            AssetSelectionPageRoute(
                vm,
                onNext = { navController.navigate(ResultsRoute) }
            )
        }
        composable<ResultsRoute> {
            val vm : ResultsVM = viewModel(factory = ResultsVM.Factory)
            ResultsTablePageRout(vm)
        }
        composable<ChartRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ChartRoute>()
            ChartPage(ticker = route.ticker, monthsBack = route.monthsBack)
        }
    }
}

