package com.mrwang.coffeeapp.presentation.settings

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

enum class AppLanguage {
    English,
    Chinese
}

class AppSettingsViewModel : ViewModel() {
    private val _language = MutableStateFlow(AppLanguage.English)
    val language: StateFlow<AppLanguage> = _language.asStateFlow()

    fun setLanguage(language: AppLanguage) {
        _language.value = language
    }
}
