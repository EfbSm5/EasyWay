package com.efbsm5.easyway.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.efbsm5.easyway.database.getPointContent
import com.efbsm5.easyway.database.getPoints

@Preview
@Composable
fun CommentCard(id: Int) {
    val context = LocalContext.current
    val points = getPointContent(
        context = context, id = id
    )

    FacilityDetailScreen()
}

@Composable
fun FacilityDetailScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        FacilityInfoSection()
        Spacer(modifier = Modifier.height(16.dp))
        CommentSection()
        Spacer(modifier = Modifier.height(16.dp))
        BottomActionBar()
    }
}

@Composable
fun FacilityInfoSection() {
    Row(
        modifier = Modifier.fillMaxWidth()
    ) {
        Box(
            modifier = Modifier
                .size(120.dp)
                .background(Color.Gray, RoundedCornerShape(8.dp)),
            contentAlignment = Alignment.Center
        ) {
            Text(text = "照片，取首图", color = Color.White)
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f), verticalArrangement = Arrangement.SpaceEvenly
        ) {
            Text(text = "设施类别", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "标注来源", fontSize = 14.sp, color = Color.Gray)
            Text(text = "更新日期", fontSize = 14.sp, color = Color.Gray)

            Spacer(modifier = Modifier.height(8.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = "👍 次数", fontSize = 14.sp, color = Color.Gray)
                Spacer(modifier = Modifier.width(16.dp))
                Text(text = "👎 次数", fontSize = 14.sp, color = Color.Gray)
            }
        }
    }

    Spacer(modifier = Modifier.height(16.dp))

    Text(
        text = "设施说明", fontWeight = FontWeight.Bold, fontSize = 16.sp
    )
    Text(
        text = "例如：坡道损坏；无障碍洗手间在三楼", fontSize = 14.sp, color = Color.Gray
    )
}

@Composable
fun CommentSection() {
    Text(
        text = "评论 次数 / 历史版本", fontWeight = FontWeight.Bold, fontSize = 16.sp
    )

    Spacer(modifier = Modifier.height(8.dp))

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