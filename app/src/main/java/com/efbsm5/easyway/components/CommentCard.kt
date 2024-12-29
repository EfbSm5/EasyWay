package com.efbsm5.easyway.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.amap.api.maps.MapView
import com.efbsm5.easyway.data.EasyPoints
import com.efbsm5.easyway.data.TestData

@Preview
@Composable
fun pres() {
    val context = LocalContext.current
    val testData = TestData().initFirstData(context, mapView = MapView(context))
    CommentAndHistoryCard(
        points = testData
    )
}


@Composable
fun CommentAndHistoryCard(points: EasyPoints) {
    var state: Screen by remember { mutableStateOf(Screen.Comment) }
    FacilityDetailScreen(points = points, screen = state, onChangeScreen = { state = it })
}

@Composable
fun FacilityDetailScreen(points: EasyPoints, screen: Screen, onChangeScreen: (Screen) -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        FacilityInfoSection(points)
        Spacer(modifier = Modifier.height(16.dp))
        Section { onChangeScreen(it) }
        when (screen) {
            Screen.Comment -> CommentCard(points)
            Screen.History -> HistoryCard()
        }
        Spacer(modifier = Modifier.height(16.dp))
        BottomActionBar()
    }
}

@Composable
fun FacilityInfoSection(points: EasyPoints) {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.Gray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {


        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "设施类别:${points.type}", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "标注来源:", fontSize = 14.sp, color = Color.Gray)
            Text(text = "更新日期:${points.refreshTime}", fontSize = 14.sp, color = Color.Gray)
            Spacer(modifier = Modifier.height(8.dp))
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "👍 次数:${points.likes}", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "👎 次数:${points.dislikes}", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }
    Spacer(modifier = Modifier.height(16.dp))
    Text(
        text = "设施说明:", fontWeight = FontWeight.Bold, fontSize = 16.sp
    )
    Text(
        text = points.introduce, fontSize = 14.sp, color = Color.Gray
    )
}

@Composable
fun Section(onClick: (Screen) -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = "评论次数",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.clickable {
                onClick(Screen.Comment)
            })
        Spacer(Modifier.width(20.dp))
        Text(text = "历史版本",
            fontWeight = FontWeight.Bold,
            fontSize = 16.sp,
            modifier = Modifier.clickable {
                onClick(Screen.History)
            })
    }

}

@Composable
fun CommentCard(points: EasyPoints) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Box(
            modifier = Modifier
                .size(48.dp)
                .background(Color.Gray, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "头像", color = Color.White)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "用户XXX", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "👍", fontSize = 14.sp)
            }

            Text(
                text = "评论内容评论内容评论内容评论内容评论内容评论内容评论内容评论内容",
                fontSize = 14.sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Box(
                modifier = Modifier
                    .size(80.dp)
                    .background(Color.LightGray),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "图片")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "时间XX/XX/XX", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "回复", color = Color.Blue, fontSize = 12.sp)
                Spacer(modifier = Modifier.weight(1f))
                Text(text = "👍 次数", fontSize = 12.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "👎 次数", fontSize = 12.sp, color = Color.Gray)
            }
        }
    }
}

@Composable
fun HistoryCard() {

}

@Composable
fun BottomActionBar() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Button(
            onClick = { /* 更新内容 */ }, modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text(text = "更新内容")
        }

        Spacer(modifier = Modifier.width(16.dp))

        OutlinedButton(
            onClick = { /* 发布评论 */ }, modifier = Modifier
                .weight(1f)
                .height(48.dp)
        ) {
            Text(text = "发布评论")
        }
    }
}

sealed interface Screen {
    data object Comment : Screen
    data object History : Screen
}
