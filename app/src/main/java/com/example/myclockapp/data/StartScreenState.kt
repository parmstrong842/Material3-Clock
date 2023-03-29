package com.example.myclockapp.data

import android.content.Context
import android.content.SharedPreferences

class StartScreenState(context: Context) {
    private val prefsFileName = "com.example.myclockapp.data.STATE"
    private val stateKey = "com.example.myclockapp.data.KEY"

    private val sharedPref: SharedPreferences =
        context.getSharedPreferences(prefsFileName, Context.MODE_PRIVATE)

    fun getState(): Int {
        return sharedPref.getInt(stateKey, 0)
    }

    fun setState(newState: Int) {
        with (sharedPref.edit()) {
            putInt(stateKey, newState)
            apply()
        }
    }
}