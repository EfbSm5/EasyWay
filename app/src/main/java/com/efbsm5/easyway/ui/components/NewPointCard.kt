package com.efbsm5.easyway.ui.components

import android.app.Activity
import android.content.Intent
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.rememberAsyncImagePainter
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPointCardViewModel
import java.io.File
import java.io.FileOutputStream

@Composable
fun NewPointCard(location: LatLng?, back: () -> Unit, viewModel: NewPointCardViewModel) {
    val context = LocalContext.current
    val newPoint by viewModel.tempPoint.collectAsState()
    val expanded by viewModel.expanded.collectAsState()
    val selectedOption by viewModel.selectedOption.collectAsState()
    NewPointCardSurface(
        point = newPoint,
        onInfoValueChange = { viewModel.changeTempPoint(newPoint.copy(info = it)) },
        onLocationValueChange = { viewModel.changeTempPoint(newPoint.copy(location = it)) },
        onUploadImage = { uri ->
            uri?.let { uri1 ->
                val inputStream = context.contentResolver.openInputStream(uri1)
                inputStream?.let {
                    val file = File(context.cacheDir, "temp_image")
                    val outputStream = FileOutputStream(file)
                    it.copyTo(outputStream)
                    outputStream.close()
                    it.close()
                    viewModel.changeTempPoint(newPoint.copy(photo = file.toUri()))
                }
            }
        },
        menuExpanded = expanded,
        selectedOption = selectedOption,
        onSelectType = { viewModel.changeType(it) },
        onExpanded = { viewModel.changeExpanded(it) },
        confirm = {
            viewModel.changeTempPoint(
                newPoint.copy(
                    lat = location!!.latitude, lng = location.longitude
                )
            )
            viewModel.publishPoint()
        },
        cancel = { back() },
        onNameValueChange = { viewModel.changeTempPoint(newPoint.copy(name = it)) },
    )
}

@Composable
private fun NewPointCardSurface(
    point: EasyPoint,
    menuExpanded: Boolean,
    selectedOption: String,
    onInfoValueChange: (String) -> Unit,
    onLocationValueChange: (String) -> Unit,
    onNameValueChange: (String) -> Unit,
    onUploadImage: (Uri?) -> Unit,
    onSelectType: (String) -> Unit,
    onExpanded: (Boolean) -> Unit,
    confirm: () -> Unit,
    cancel: () -> Unit
) {
    Column(
        modifier = Modifier
            .padding(16.dp)
            .background(color = MaterialTheme.colorScheme.surface),
        verticalArrangement = Arrangement.Bottom,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "新增标识",
            style = TextStyle(fontSize = 20.sp),
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(16.dp))
        Column(modifier = Modifier.fillMaxWidth()) {
            DropdownField(label = "设施类别",
                expanded = menuExpanded,
                selectedOption = selectedOption,
                onSelectType = { onSelectType(it) },
                onExpanded = { onExpanded(it) })
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldWithText(
                label = "设施名", text = point.name
            ) { onNameValueChange(it) }
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldWithText(label = "设施说明", text = point.info) { onInfoValueChange(it) }
            Spacer(modifier = Modifier.height(16.dp))
            TextFieldWithText(
                label = "所在位置", text = point.location
            ) { onLocationValueChange(it) }
            Spacer(modifier = Modifier.height(16.dp))
            UploadImageSection { onUploadImage(it) }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
        ) {
            Button(
                onClick = {
                    confirm()
                }, colors = ButtonDefaults.buttonColors(contentColor = Color.Green)
            ) {
                Text(text = "确认上传")
            }
            Button(
                onClick = { cancel() },
                colors = ButtonDefaults.buttonColors(contentColor = Color.Gray)
            ) {
                Text(text = "取消")
            }
        }
    }
}

@Composable
private fun DropdownField(
    label: String,
    expanded: Boolean,
    selectedOption: String,
    onSelectType: (String) -> Unit,
    onExpanded: (Boolean) -> Unit
) {
    Column {
        Text(text = label, style = TextStyle(fontSize = 16.sp))
        Box(modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, Color.Gray)
            .padding(8.dp)
            .clickable { onExpanded(true) }) {
            Text(text = selectedOption.ifEmpty { "请选择" })
        }
        DropdownMenu(expanded = expanded, onDismissRequest = { onExpanded(false) }) {
            listOf(
                "无障碍电梯",
                "无障碍厕所",
                "停车位",
                "公共交通",
                "轮椅租赁",
                "爱心站点",
                "AED",
                "坡道"
            ).forEach { option ->
                DropdownMenuItem(onClick = {
                    onSelectType(option)
                    onExpanded(false)
                }, text = { Text(option) })
            }
        }
    }
}

@Composable
private fun TextFieldWithText(label: String, text: String, onValueChange: (String) -> Unit) {
    Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = label,
            style = TextStyle(fontSize = 16.sp),
            modifier = Modifier
                .width(70.dp) // 固定宽度
                .wrapContentWidth(Alignment.CenterHorizontally) // 居中
        )
        TextField(
            value = text,
            onValueChange = { onValueChange(it) },
            modifier = Modifier.weight(1f) // 占据剩余空间
        )
    }
}

@Composable
private fun UploadImageSection(onChoosePicture: (Uri?) -> Unit) {
    var selectedImageUri: Uri? by remember { mutableStateOf(null) }
    val launcher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            onChoosePicture(result.data?.data)
            selectedImageUri = result.data?.data
        }
    }
    Column {
        Text(text = "上传图片", style = TextStyle(fontSize = 16.sp))
        Box(
            modifier = Modifier
                .size(100.dp)
                .background(MaterialTheme.colorScheme.onBackground)
                .border(1.dp, MaterialTheme.colorScheme.primary)
                .clickable {
                    val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                        addCategory(Intent.CATEGORY_OPENABLE)
                        type = "image/*"
                    }
                    launcher.launch(intent)
                }, contentAlignment = Alignment.Center
        ) {
            if (selectedImageUri != null) {
                Image(
                    painter = rememberAsyncImagePainter(model = selectedImageUri),
                    contentDescription = null,
                    modifier = Modifier.fillMaxSize()
                )
            } else {
                Text(text = "严禁上传无关图片", color = Color.Red, fontSize = 12.sp)
            }
        }
    }
}

