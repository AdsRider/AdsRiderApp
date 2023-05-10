package com.capstone.adsrider.utility

import android.content.Context
import android.content.SharedPreferences
import com.capstone.adsrider.model.User

class UserSharedPreference(context: Context) {
    private val prefsFilename = "UserPrefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    fun getUserPrefs(key: String): String? {
        return prefs.getString(key, "")
    }

    fun getUserPrefs(): User {
        return User(
            email = prefs.getString("email", "")!!,
            level = prefs.getString("level", "")!!,
            address = prefs.getString("address", "")!!,
            expire_date = prefs.getString("expire_date", "")!!,
            join_time = prefs.getString("join_time", "")!!
        )
    }

    fun setUserPrefs(key: String, value: String) {
        return prefs.edit().putString(key, value).apply()
    }

    fun setUserPrefs(user: User) {
        prefs.edit().putString("email", user.email.toString()).apply()
        prefs.edit().putString("level", user.level).apply()
        prefs.edit().putString("address", user.address).apply()
        prefs.edit().putString("expire_date", user.expire_date).apply()
        prefs.edit().putString("join_time", user.join_time).apply()
    }
}
