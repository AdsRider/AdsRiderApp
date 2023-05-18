package com.capstone.adsrider.utility

import android.content.Context
import android.content.SharedPreferences
import com.capstone.adsrider.model.ResultBody

class RidingSharedPreference(context: Context) {
    private val prefsFilename = "RidingPrefs"
    private val prefs: SharedPreferences = context.getSharedPreferences(prefsFilename, 0)

    fun getRidingPrefs(key: String): String {
        return prefs.getString(key, "")!!
    }

    fun getRidingPrefs(): ResultBody {
        return ResultBody(
            ads_id = prefs.getString("ads_id", "")!!,
            meters = prefs.getInt("meters", 0)!!,
            path = prefs.getString("path", "")!!,
            start_time = prefs.getString("start_time", "")!!,
            end_time = prefs.getString("end_time", "")!!
        )
    }

    fun setRidingPrefs(key: String, value: String) {
        return prefs.edit().putString(key, value).apply()
    }

    fun setRidingPrefs(riding: ResultBody) {
        prefs.edit().putString("ads_id", riding.ads_id).apply()
        prefs.edit().putInt("meters", riding.meters).apply()
        prefs.edit().putString("path", riding.path).apply()
        prefs.edit().putString("start_time", riding.start_time).apply()
        prefs.edit().putString("end_time", riding.end_time).apply()
    }
}
