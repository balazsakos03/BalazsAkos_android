package com.example.progresshabitplanner.data

import android.content.Context

class UserPreferences(context: Context) {

    private val prefs = context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    fun getUserName(): String =
        prefs.getString("user_name", "Ákos") ?: "Ákos"

    fun getUserEmail(): String =
        prefs.getString("user_email", "balazsakos81@gmail.com") ?: "balazsakos81@gmail.com"

    fun saveProfileImageBase64(base64: String?) {
        prefs.edit().putString("profile_image_base64", base64).apply()
    }

    fun getProfileImageBase64(): String? {
        return prefs.getString("profile_image_base64", null)
    }

    fun saveProfileImageUrl(url: String?) {
        prefs.edit().putString("profile_image_url", url).apply()
    }

    fun getProfileImageUrl(): String? {
        return prefs.getString("profile_image_url", null)
    }

    fun saveUser(name: String, email: String, imageUri: String?) {
        prefs.edit()
            .putString("user_name", name)
            .putString("user_email", email)
            .apply()
    }

    fun clear() {
        prefs.edit().clear().apply()
    }

    fun setLoggedIn(value: Boolean) {
        prefs.edit().putBoolean("logged_in", value).apply()
    }

    fun isLoggedIn(): Boolean {
        return prefs.getBoolean("logged_in", false)
    }
}
