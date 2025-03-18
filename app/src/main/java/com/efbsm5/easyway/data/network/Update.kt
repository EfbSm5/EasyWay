package com.efbsm5.easyway.data.network

import android.content.Context
import android.content.Intent
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.platform.LocalContext
import androidx.core.net.toUri
import com.alibaba.idst.nui.BuildConfig
import com.efbsm5.easyway.data.models.assistModel.UpdateInfo

@Composable
fun CheckUpdate() {
    val context = LocalContext.current
    var updateInfo by remember { mutableStateOf<UpdateInfo?>(null) }
    LaunchedEffect(Unit) {
        HttpClient().checkForUpdate { info ->
            if (info != null && shouldUpdate(info.versionCode)) {
                updateInfo = info
            }
        }

    }
    updateInfo?.let {
        UpdateDialog(context, it) {
            updateInfo = if (it) null
            else null
        }
    }
}


@Composable
private fun UpdateDialog(context: Context, updateInfo: UpdateInfo, callback: (Boolean) -> Unit) {
    AlertDialog(
        onDismissRequest = { callback(false) },
        title = { Text(text = "发现新版本 ${updateInfo.versionName}") },
        text = { Text(text = updateInfo.updateMessage) },
        confirmButton = {
            Button(onClick = {
                val intent = Intent(Intent.ACTION_VIEW, updateInfo.apkUrl.toUri())
                context.startActivity(intent)
                callback(true)
            }) {
                Text("立即更新")
            }
        },
        dismissButton = {
            Button(onClick = { callback(false) }) {
                Text("稍后再说")
            }
        })
}

private fun shouldUpdate(latestVersionCode: Int): Boolean {
    return latestVersionCode > BuildConfig.VERSION_CODE
}