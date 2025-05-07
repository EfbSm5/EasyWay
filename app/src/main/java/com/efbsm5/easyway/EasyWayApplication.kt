package com.efbsm5.easyway

import android.app.Application

class EasyWayApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        AppUtils.init(this)
    }
}