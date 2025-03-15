package com.efbsm5.easyway.ui.page.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.Build
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.map.MapUtil
import com.efbsm5.easyway.viewmodel.pageViewmodel.HomePageState


@Preview
@Composable
fun MainPageScreen(
    user: User = MapUtil.getInitUser(), changeState: (HomePageState) -> Unit = {}
) {
    Surface {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            UserProfileHeader(
                user = user, edit = { changeState(HomePageState.EditUser) })
            Spacer(modifier = Modifier.height(16.dp))
            UserActionButtons(
                reg = { changeState(HomePageState.Reg) },
                point = { changeState(HomePageState.Point) },
                manage = { changeState(HomePageState.Comment) })
            Spacer(modifier = Modifier.height(16.dp))
            UserStats()
            Spacer(modifier = Modifier.height(16.dp))
            BottomMenu(
                change = { changeState(HomePageState.Version) },
                help = { changeState(HomePageState.Help) },
                settings = { changeState(HomePageState.Settings) })
        }
    }

}

@Composable
private fun UserProfileHeader(user: User, edit: () -> Unit) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(64.dp)
                .background(Color.Gray, CircleShape)
        ) {
            Image(
                rememberAsyncImagePainter(user.avatar),
                contentDescription = "User Avatar",
                modifier = Modifier
                    .size(64.dp)
                    .align(Alignment.Center)
            )
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(
                text = user.name, fontSize = 20.sp, fontWeight = FontWeight.Bold
            )
        }
        Spacer(modifier = Modifier.weight(1f))
        IconButton(onClick = { edit() }) {
            Icon(
                imageVector = Icons.Default.Edit, contentDescription = "edit"
            )
        }
    }
}

@Composable
private fun UserActionButtons(reg: () -> Unit, point: () -> Unit, manage: () -> Unit) {
    Row(
        modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        ActionButton("活动报名", Icons.Default.AddCircle, reg)
        ActionButton("点位标注", Icons.Default.Add, point)
        ActionButton("发帖管理", Icons.Default.Email, manage)
    }
}

@Composable
private fun ActionButton(label: String, imageVector: ImageVector, click: () -> Unit) {
    Button(
        click,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(8.dp),
        colors = ButtonDefaults.buttonColors(disabledContentColor = MaterialTheme.colorScheme.onBackground)
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.clip(RoundedCornerShape(16))
        ) {
            Icon(
                imageVector = imageVector,
                contentDescription = label,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color(0xFFEFEFEF), CircleShape)
                    .padding(8.dp)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = label, fontSize = 14.sp)
        }
    }

}

@Composable
fun UserStats() {
    Card(colors = CardDefaults.cardColors()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.weight(2f))
            StatItem("关注", "2")
            StatItem("粉丝", "3+")
            Spacer(modifier = Modifier.weight(2f))
        }
    }

}

@Composable
fun StatItem(label: String, value: String) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold)
        Text(text = label, fontSize = 14.sp, color = Color.Gray)
    }
}

@Composable
fun BottomMenu(change: () -> Unit, help: () -> Unit, settings: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White, RoundedCornerShape(12.dp))
            .padding(16.dp)
    ) {
        MenuItem("版本切换", Icons.Default.Build, change)
        MenuItem("帮助中心", Icons.Default.Face, help)
        MenuItem("设置", Icons.Default.Settings, settings)
    }
}

@Composable
fun MenuItem(label: String, imageVector: ImageVector, click: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp)
            .clickable { click },
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = imageVector,
            contentDescription = label,
            tint = Color.Blue,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 16.sp, color = Color.Black)
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight, // 替换为右箭头图标资源
            contentDescription = "Arrow", tint = Color.Gray
        )
    }
}