package com.moex_assets_analyzer.app.navigation

import kotlinx.serialization.Serializable

@Serializable
object SelectionRoute

@Serializable
object ConfigRoute

@Serializable
object ResultsRoute

@Serializable
data class ChartRoute(val ticker: String, val monthsBack: Int)