package com.mrwang.coffeeapp.data.network

object SupabaseConfig {
    const val ANON_KEY =
        "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9.eyJpc3MiOiJzdXBhYmFzZSIsInJlZiI6ImFsb3ppcWJ1bWV5eGp1a3hmaXliIiwicm9sZSI6ImFub24iLCJpYXQiOjE3NzY0ODE2NjUsImV4cCI6MjA5MjA1NzY2NX0.GoMBa3iYqt2oQlKwDnjlYzCpFZh771dp-Fp6GsAn8Nk"

    val authorizationHeader: String
        get() = "Bearer $ANON_KEY"
}
