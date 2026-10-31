package com.moex_assets_analyzer.app.others

import java.time.LocalDate

const val ASSETS_LIST_URL = "https://iss.moex.com/iss/engines/stock/markets/shares/securities.json?iss.meta=off&iss.only=securities&securities.columns=SECID%2CSHORTNAME%2CSECNAME"

fun historyUrlFor(asset: String, ago : LocalDate, start: Int = 0) =
    "https://iss.moex.com/iss/history/engines/stock/markets/shares/securities/$asset.json?from=$ago&start=$start&history.columns=TRADEDATE,CLOSE"
