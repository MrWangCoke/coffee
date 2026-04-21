package com.mrwang.coffeeapp.data.local

import android.content.Context

private const val SESSION_PREFS_NAME = "user_session"
private const val KEY_USER_ID = "user_id"
private const val KEY_EMAIL = "email"
private const val KEY_ACCESS_TOKEN = "access_token"

data class PersistedUserSession(
    val userId: String?,
    val email: String,
    val accessToken: String
)

class UserSessionStorage(context: Context) {
    private val sharedPreferences =
        context.getSharedPreferences(SESSION_PREFS_NAME, Context.MODE_PRIVATE)

    fun saveSession(userId: String?, email: String, accessToken: String) {
        sharedPreferences.edit()
            .putString(KEY_USER_ID, userId)
            .putString(KEY_EMAIL, email)
            .putString(KEY_ACCESS_TOKEN, accessToken)
            .apply()
    }

    fun loadSession(): PersistedUserSession? {
        val email = sharedPreferences.getString(KEY_EMAIL, null)?.takeIf { it.isNotBlank() }
            ?: return null
        val accessToken = sharedPreferences.getString(KEY_ACCESS_TOKEN, null)
            ?.takeIf { it.isNotBlank() }
            ?: return null

        return PersistedUserSession(
            userId = sharedPreferences.getString(KEY_USER_ID, null),
            email = email,
            accessToken = accessToken
        )
    }

    fun clearSession() {
        sharedPreferences.edit()
            .remove(KEY_USER_ID)
            .remove(KEY_EMAIL)
            .remove(KEY_ACCESS_TOKEN)
            .apply()
    }
}
