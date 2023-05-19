package com.capstone.adsrider.utility

import android.content.Context
import android.content.SharedPreferences
import com.capstone.adsrider.model.User

class UserSharedPreference(context: Context) {
    private val prefsFilename = "UserPrefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    fun getUserPrefs(key: String): String {
        return prefs.getString(key, "")!!
    }

    fun getUserPrefs(): User {
        return User(
            email = prefs.getString("email", "")!!,
            level = prefs.getString("level", "")!!,
            address = prefs.getString("address", "")!!,
            expired_date = prefs.getLong("expire_date", System.currentTimeMillis()),
            join_time = prefs.getLong("join_time", System.currentTimeMillis())
        )
    }

    fun setUserPrefs(key: String, value: Long) {
        return prefs.edit().putLong(key, value).apply()
    }

    fun setUserPrefs(user: User) {
        prefs.edit()
            .putString("email", user.email)
            .putString("level", user.level)
            .putString("address", user.address)
            .putLong("expired_date", user.expired_date)
            .putLong("join_time", user.join_time)
            .apply()
    }
}
