package com.mrwang.coffeeapp.presentation.screens.profilescreen

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.ViewModel
import com.mrwang.coffeeapp.data.local.UserSessionStorage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class UserViewModel(application: Application) : AndroidViewModel(application) {
    private val sessionStorage = UserSessionStorage(application.applicationContext)

    private val _isLoggedIn = MutableStateFlow(false)
    val isLoggedIn: StateFlow<Boolean> = _isLoggedIn.asStateFlow()

    private val _email = MutableStateFlow<String?>(null)
    val email: StateFlow<String?> = _email.asStateFlow()

    private val _userId = MutableStateFlow<String?>(null)
    val userId: StateFlow<String?> = _userId.asStateFlow()

    private val _accessToken = MutableStateFlow<String?>(null)
    val accessToken: StateFlow<String?> = _accessToken.asStateFlow()

    private val _isSessionRestored = MutableStateFlow(false)
    val isSessionRestored: StateFlow<Boolean> = _isSessionRestored.asStateFlow()

    init {
        restoreSession()
    }

    fun login(userId: String?, email: String, accessToken: String) {
        _userId.value = userId
        _email.value = email
        _accessToken.value = accessToken
        _isLoggedIn.value = true
        _isSessionRestored.value = true
        sessionStorage.saveSession(
            userId = userId,
            email = email,
            accessToken = accessToken
        )
    }

    fun logout() {
        _userId.value = null
        _email.value = null
        _accessToken.value = null
        _isLoggedIn.value = false
        _isSessionRestored.value = true
        sessionStorage.clearSession()
    }

    private fun restoreSession() {
        val session = sessionStorage.loadSession()
        if (session != null) {
            _userId.value = session.userId
            _email.value = session.email
            _accessToken.value = session.accessToken
            _isLoggedIn.value = true
        }
        _isSessionRestored.value = true
    }
}
