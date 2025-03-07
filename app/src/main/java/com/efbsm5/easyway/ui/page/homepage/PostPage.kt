package com.efbsm5.easyway.ui.page.homepage

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ThumbUp
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.unit.dp
import coil.compose.rememberAsyncImagePainter
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.data.models.assistModel.DynamicPostAndUser
import com.efbsm5.easyway.ui.components.DynamicPostList
import com.efbsm5.easyway.ui.page.communityPage.DetailPage

@Composable
fun PostPage(postAndUser: DynamicPostAndUser) {
//    var showDetail by remember { mutableStateOf(false) }
//    if (showDetail){
//        DynamicPostList(
//            posts =
//        ) { }
//    }else{
//        DetailPage(
//            onBack = TODO(),
//            viewModel = TODO()
//        )
//    }

}
