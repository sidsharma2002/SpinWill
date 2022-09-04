package com.example.spinwill.manager

import android.content.Context
import androidx.core.content.edit

class WillManager constructor(context: Context) {

    private val sharedPreferences =
        context.getSharedPreferences("spinwill_library", Context.MODE_PRIVATE)

    fun isDataUpdated(): Boolean {
        return true
    }

    fun notifyDataUpdated() {
        sharedPreferences.edit {
            this.putLong(UPDATE_TIME_TAG, System.currentTimeMillis())
            commit()
        }
    }

    companion object {
        @JvmStatic
        private val UPDATE_TIME_TAG = "spinwill_updatetime"
    }
}