package com.example.spinwill.config

object SpinWillConfig {
    private var EXPIRE_TIME: Int = 3 * 60   // 3 hours by default

    /**
     * time in which the data expires and needs to be refreshed,
     * by default this time is 3 hours.
     * @param time time in minutes
     *
     */
    fun dataExpiresIn(time: Int) {
        EXPIRE_TIME = time
    }
}