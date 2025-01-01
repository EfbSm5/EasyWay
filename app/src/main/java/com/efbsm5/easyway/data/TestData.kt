package com.efbsm5.easyway.data

import com.efbsm5.easyway.ultis.MapUtil.fromComment
import java.net.URL


fun getFirstData(): EasyPoint {
    val comments = ArrayList<Comment>()
    comments.add(
        Comment(
            index = 2,
            nickname = "developer2",
            content = "test1",
            like = 3,
            dislike = 4,
            date = "2024-12-30"
        )
    )
    comments.add(
        Comment(
            index = 1,
            nickname = "developer",
            content = "test",
            like = 2,
            dislike = 3,
            date = "2024-12-30"
        )
    )
    val firstData = EasyPoint(
        id = 1,
        pointId = 1,
        type = "不详",
        introduce = "这个是介绍",
        photos = URL(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/%E5%8D%8E%E7%A7%91%E6%A0%A1%E9%97%A8.JPG/220px-%E5%8D%8E%E7%A7%91%E6%A0%A1%E9%97%A8.JPG"
        ),
        refreshTime = "2024-12-29",
        likes = 999,
        dislikes = 999,
        comments = fromComment(comments)
    )
    return firstData
}



