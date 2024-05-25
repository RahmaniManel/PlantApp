package com.example.planter_app.shared_preferences

import android.content.Context
import android.content.SharedPreferences

// sharedPreferences to save user themes
class ThemePreferences(context: Context) {
    private val sharedPreferences: SharedPreferences = context.getSharedPreferences("ThemePreferences", Context.MODE_PRIVATE)
    private val  darkThemeKey = "dark_theme_key"
    private val dynamicThemeKey = "dynamic_theme_key"

    fun saveTheme(darkTheme: Boolean, dynamicTheme: Boolean) {
        sharedPreferences.edit()
            .putBoolean(darkThemeKey, darkTheme)
            .putBoolean(dynamicThemeKey, dynamicTheme)
            .apply()
    }
    fun loadDarkTheme(): Boolean {
        return sharedPreferences.getBoolean(darkThemeKey, false)
    }
    fun loadDynamicTheme(): Boolean {
        return sharedPreferences.getBoolean(dynamicThemeKey, false)
    }
}
