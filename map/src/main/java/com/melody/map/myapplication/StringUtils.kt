package com.melody.map.myapplication

import androidx.annotation.StringRes

object StringUtils {

    fun getString(@StringRes resID: Int): String {
        return SDKUtils.getApplicationContext().getString(resID)
    }

}