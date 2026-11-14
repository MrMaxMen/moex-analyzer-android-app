package com.moex_assets_analyzer.app.exceptions

class NetworkException(
    message: String = "Failed to connect to MOEX server. Check internet connection.",
    cause: Throwable? = null
) : Exception(message, cause)
