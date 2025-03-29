package com.efbsm5.easyway

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.efbsm5.easyway.ui.EasyWay
import com.efbsm5.easyway.ui.theme.EasyWayTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            EasyWayTheme {
                EasyWay()
            }
        }
    }
}