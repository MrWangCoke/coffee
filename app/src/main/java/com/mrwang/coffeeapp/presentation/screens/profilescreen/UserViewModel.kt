package com.mrwang.coffeeapp.presentation.screens.profilescreen

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel : ViewModel() {
    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email.asStateFlow()

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken.asStateFlow()

    fun login(userId: String?, email: String, accessToken: String) {
        _userId.value = userId
        _email.value = email
        _accessToken.value = accessToken
        _isLoggedIn.value = true
    }

    fun logout() {
        _userId.value = null
        _email.value = null
        _accessToken.value = null
        _isLoggedIn.value = false
    }
}
