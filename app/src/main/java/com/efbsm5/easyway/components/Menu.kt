package com.efbsm5.easyway.components

import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun Menu(onChangeMap: () -> Unit, onFactoryReset: () -> Unit) {
    DropdownMenuItem(text = { Text(text = "更换地图显示样式") }, onClick = {
        onChangeMap()
    })
    DropdownMenuItem(text = { Text(text = "重置所有") }, onClick = { onFactoryReset() })
}