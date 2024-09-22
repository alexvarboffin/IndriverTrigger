package com.dev.indrivertrigger.utils

import android.content.Context
import android.content.SharedPreferences

object PreferenceHelper {

    fun defaultPrefs(context: Context): SharedPreferences =
        context.getSharedPreferences("sharedPrefs", Context.MODE_PRIVATE)


    private inline fun SharedPreferences.edit(operation: (SharedPreferences.Editor) -> Unit) {
        val editor = this.edit()
        operation(editor)
        editor.apply()
    }

    operator fun SharedPreferences.set(key: String, value: Any?) {
        when (value) {
            is String? -> edit { it.putString(key, value) }
            is Int -> edit { it.putInt(key, value) }
            is Boolean -> edit { it.putBoolean(key, value) }
            is Float -> edit { it.putFloat(key, value) }
            is Long -> edit { it.putLong(key, value) }
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    @Suppress("UNCHECKED_CAST")
    operator fun <T> SharedPreferences.get(key: String, defaultValue: T? = null): T {
        return when (defaultValue) {
            is String, null -> getString(key, defaultValue as? String) as T
            is Int -> getInt(key, defaultValue as? Int ?: -1) as T
            is Boolean -> getBoolean(key, defaultValue as? Boolean ?: false) as T
            is Float -> getFloat(key, defaultValue as? Float ?: -1f) as T
            is Long -> getLong(key, defaultValue as? Long ?: -1) as T
            else -> throw UnsupportedOperationException("Not yet implemented")
        }
    }

    fun SharedPreferences.remove(key: String) {
        edit {
            it.remove(key)
        }

    }

    fun SharedPreferences.clearPreferences(context: Context) {
        val preferences = defaultPrefs(context = context)
        preferences.all.clear().apply {  }
    }

}



/*
// To set value in prefrence
        PreferenceHelper.defaultPrefs(applicationContext)["key"] = 10

        // To get value from prefrence
        PreferenceHelper.defaultPrefs(applicationContext)["key", 0]

        // To remove key from prefrence
        PreferenceHelper.defaultPrefs(applicationContext).remove("key")
 */