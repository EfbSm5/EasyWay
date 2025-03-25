package com.efbsm5.easyway

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.os.Bundle
import android.os.PersistableBundle

class Myapplication : Activity() {
    companion object {
        @SuppressLint("StaticFieldLeak")
        lateinit var context: Context
        fun getContext(): Context {
            return context
        }
    }

    @Override
    override fun onCreate(savedInstanceState: Bundle?, persistentState: PersistableBundle?) {
        super.onCreate(savedInstanceState, persistentState)
        context = this.applicationContext
    }
}