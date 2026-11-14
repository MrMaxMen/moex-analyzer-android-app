package com.moex_assets_analyzer.app.others

import android.app.LocaleManager
import android.content.Context
import android.os.Build
import android.os.LocaleList
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.os.LocaleListCompat
import java.util.Locale

object LocaleHelper {
    fun isRussian(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                val localeManager = context.getSystemService(LocaleManager::class.java)
                val currentLocales = localeManager?.applicationLocales
                if (currentLocales != null && !currentLocales.isEmpty) {
                    return currentLocales.get(0).language.equals("ru", ignoreCase = true)
                }
            } catch (_: Throwable) {
                // Layoutlib / BridgeContext in Android Studio Compose Preview throws AssertionError for Unsupported Service: locale
            }
        } else {
            try {
                val appLocales = AppCompatDelegate.getApplicationLocales()
                if (!appLocales.isEmpty) {
                    return appLocales[0]?.language.equals("ru", ignoreCase = true)
                }
            } catch (_: Throwable) {
            }
        }
        return Locale.getDefault().language.equals("ru", ignoreCase = true)
    }

    fun toggleLanguage(context: Context) {
        val nextLang = if (isRussian(context)) "en" else "ru"
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            try {
                val localeManager = context.getSystemService(LocaleManager::class.java)
                localeManager?.applicationLocales = LocaleList.forLanguageTags(nextLang)
            } catch (_: Throwable) {
                AppCompatDelegate.setApplicationLocales(
                    LocaleListCompat.forLanguageTags(nextLang)
                )
            }
        } else {
            AppCompatDelegate.setApplicationLocales(
                LocaleListCompat.forLanguageTags(nextLang)
            )
        }
    }
}
