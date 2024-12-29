package com.efbsm5.easyway.data

import android.content.Context
import com.amap.api.maps.MapView
import com.efbsm5.easyway.ultis.MapUtil.fromComment
import com.efbsm5.easyway.ultis.MapUtil.getImageUrl
import java.net.URL


class TestData {
    private val comment =
        Comment(index = 1, nickname = "developer", content = "test", like = 2, dislike = 3)
    private val comment1 =
        Comment(index = 2, nickname = "developer2", content = "test1", like = 3, dislike = 4)
    private val comments = ArrayList<Comment>()
    fun initFirstData(context: Context, mapView: MapView): EasyPoints {
        val test1Uri = getImageUrl(context, "test1")
        comments.add(comment)
        comments.add(comment1)
        val marker = MarkerData(
            latitude = 30.507950, longitude = 114.413514, title = "测试用点", snippet = null
        )

        val firstData = EasyPoints(
            id = 1,
            pointId = 1,
            type = "不详",
            introduce = "这个是介绍",
            name = "测试用点",
            photos = URL(
                "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/%E5%8D%8E%E7%A7%91%E6%A0%A1%E9%97%A8.JPG/220px-%E5%8D%8E%E7%A7%91%E6%A0%A1%E9%97%A8.JPG"
            ),
            refreshTime = "2024-12-29",
            likes = 999,
            dislikes = 999,
            marker = marker,
            comments = fromComment(comments)
        )
        return firstData
    }
}


