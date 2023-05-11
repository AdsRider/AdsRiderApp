package com.capstone.adsrider.utility

import android.content.Context
import android.content.SharedPreferences
import com.capstone.adsrider.model.Riding

class RidingSharedPreference(context: Context) {
    private val prefsFilename = "RidingPrefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    fun getRidingPrefs(key: String): String {
        return prefs.getString(key, "")!!
    }

    fun getRidingPrefs(): Riding {
        return Riding(
            ads_id = prefs.getString("ads_id", "")!!,
            distance = prefs.getLong("distance", 0)!!,
            path = prefs.getString("path", "")!!,
            start_at = prefs.getLong("start_at", 0)!!,
            completed_at = prefs.getLong("completed_at", 0)!!
        )
    }

    fun setRidingPrefs(key: String, value: String) {
        return prefs.edit().putString(key, value).apply()
    }

    fun setRidingPrefs(riding: Riding) {
        prefs.edit().putString("ads_id", riding.ads_id).apply()
        prefs.edit().putLong("distance", riding.distance).apply()
        prefs.edit().putString("path", riding.path).apply()
        prefs.edit().putLong("start_at", riding.start_at).apply()
        prefs.edit().putLong("completed_at", riding.completed_at).apply()
    }
}
