package com.efbsm5.easyway.data

import android.content.Context
import android.content.SharedPreferences

class UserManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("user_prefs", Context.MODE_PRIVATE)

    var userId: Int
        get() = prefs.getInt("user_id", 0)
        set(value) {
            prefs.edit().putInt("user_id", value).apply()
        }
}