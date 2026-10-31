package com.moex_assets_analyzer.app.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import com.moex_assets_analyzer.app.ui.AssetSelectionPage
import com.moex_assets_analyzer.app.ui.ChartPage
import com.moex_assets_analyzer.app.ui.ConfigPage
import com.moex_assets_analyzer.app.ui.ResultsPage


@Composable
fun AppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = SearchRoute,
        modifier = modifier
    ) {
        composable<SearchRoute> {
            AssetSelectionPage(
                onNext = { navController.navigate(ConfigRoute) }
            )
        }
        composable<ConfigRoute> {
            ConfigPage(
                onNext = { navController.navigate(ResultsRoute) }
            )
        }
        composable<ResultsRoute> {
            ResultsPage(
                onOpenChart = { ticker ->
                    navController.navigate(ChartRoute(ticker = ticker, monthsBack = 12))
                }
            )
        }
        composable<ChartRoute> { backStackEntry ->
            val route = backStackEntry.toRoute<ChartRoute>()
            ChartPage(ticker = route.ticker, monthsBack = route.monthsBack)
        }
    }
}

