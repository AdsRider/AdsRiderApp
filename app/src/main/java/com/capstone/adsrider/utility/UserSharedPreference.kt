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
            expire_date = prefs.getLong("expire_date", 0)!!,
            join_time = prefs.getLong("join_time", 0)!!
        )
    }

    fun setUserPrefs(key: String, value: String) {
        return prefs.edit().putString(key, value).apply()
    }

    fun setUserPrefs(user: User) {
        prefs.edit().putString("email", user.email.toString()).apply()
        prefs.edit().putString("level", user.level).apply()
        prefs.edit().putString("address", user.address).apply()
        prefs.edit().putLong("expire_date", user.expire_date).apply()
        prefs.edit().putLong("join_time", user.join_time).apply()
    }
}
