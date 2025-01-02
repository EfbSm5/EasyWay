package com.efbsm5.easyway

import android.net.Uri
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.data.Comment
import com.efbsm5.easyway.data.DynamicPost
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.data.User
import com.efbsm5.easyway.map.MapUtil.fromComment
import java.net.URL


fun getPointData(): EasyPoint {
    val comments = ArrayList<Comment>()
    comments.add(
        Comment(
            index = 2,
            content = "test1",
            like = 3,
            dislike = 4,
            date = "2024-12-30",
            user = User(
                name = "developer1",
                avatar = Uri.parse("https://bkimg.cdn.bcebos.com/pic/2f738bd4b31c8701a18be16bb327892f0708293851bf?x-bce-process=image/resize,m_lfit,w_525,h_700,limit_0/quality,Q_90\n")
            )
        )
    )
    comments.add(
        Comment(
            index = 1,
            content = "test",
            like = 2,
            dislike = 3,
            date = "2024-12-30",
            user = User(
                name = "developer",
                avatar = Uri.parse("https://bkimg.cdn.bcebos.com/pic/2f738bd4b31c8701a18be16bb327892f0708293851bf?x-bce-process=image/resize,m_lfit,w_525,h_700,limit_0/quality,Q_90\n")
            )
        )
    )
    val firstData = EasyPoint(
        id = 1, pointId = 1, type = "不详", introduce = "这个是介绍", photos = URL(
            "https://upload.wikimedia.org/wikipedia/commons/thumb/c/ce/%E5%8D%8E%E7%A7%91%E6%A0%A1%E9%97%A8.JPG/220px-%E5%8D%8E%E7%A7%91%E6%A0%A1%E9%97%A8.JPG"
        ), refreshTime = "2024-12-29", likes = 999, dislikes = 999, comments = fromComment(comments)
    )
    return firstData
}

fun getPostData(): DynamicPost {
    val comments = ArrayList<Comment>()
    comments.add(
        Comment(
            index = 2,
            content = "test1",
            like = 3,
            dislike = 4,
            date = "2024-12-30",
            user = User(
                name = "developer",
                avatar = Uri.parse("https://bkimg.cdn.bcebos.com/pic/2f738bd4b31c8701a18be16bb327892f0708293851bf?x-bce-process=image/resize,m_lfit,w_525,h_700,limit_0/quality,Q_90\n")
            )
        )
    )
    comments.add(
        Comment(
            index = 1,
            content = "test",
            like = 2,
            dislike = 3,
            date = "2024-12-30",
            user = User(
                name = "developer1",
                avatar = Uri.parse("https://bkimg.cdn.bcebos.com/pic/2f738bd4b31c8701a18be16bb327892f0708293851bf?x-bce-process=image/resize,m_lfit,w_525,h_700,limit_0/quality,Q_90\n")
            )
        )
    )
    return DynamicPost(
        title = "测试",
        date = "2024-12-31",
        like = 6,
        content = "测试测试",
        comment = comments,
        location = LatLng(0.0, 0.0),
        position = "测试地点",
        photos = ArrayList<Uri>(),
        user = User(
            name = "developer",
            avatar = Uri.parse("https://bkimg.cdn.bcebos.com/pic/2f738bd4b31c8701a18be16bb327892f0708293851bf?x-bce-process=image/resize,m_lfit,w_525,h_700,limit_0/quality,Q_90\n")
        )
    )
}

fun getPostDatas(): ArrayList<DynamicPost> {
    val comments = ArrayList<Comment>()
    comments.add(
        Comment(
            index = 2,
            content = "test1",
            like = 3,
            dislike = 4,
            date = "2024-12-30",
            user = User(
                name = "developer",
                avatar = Uri.parse("https://bkimg.cdn.bcebos.com/pic/2f738bd4b31c8701a18be16bb327892f0708293851bf?x-bce-process=image/resize,m_lfit,w_525,h_700,limit_0/quality,Q_90\n")
            )
        )
    )
    comments.add(
        Comment(
            index = 1,
            content = "test",
            like = 2,
            dislike = 3,
            date = "2024-12-30",
            user = User(
                name = "developer1111",
                avatar = Uri.parse("https://bkimg.cdn.bcebos.com/pic/2f738bd4b31c8701a18be16bb327892f0708293851bf?x-bce-process=image/resize,m_lfit,w_525,h_700,limit_0/quality,Q_90\n")
            )
        )
    )
    val array = ArrayList<DynamicPost>()
    array.add(
        DynamicPost(
            title = "测试",
            date = "2024-12-31",
            like = 6,
            content = "测试测试",
            comment = comments,
            location = LatLng(0.0, 0.0),
            position = "测试地点",
            photos = ArrayList<Uri>(),
            user = User(
                name = "developer",
                avatar = Uri.parse("https://bkimg.cdn.bcebos.com/pic/2f738bd4b31c8701a18be16bb327892f0708293851bf?x-bce-process=image/resize,m_lfit,w_525,h_700,limit_0/quality,Q_90\n")
            )
        )
    )
    array.add(
        DynamicPost(
            title = "测试1",
            date = "2024-12-32",
            like = 6,
            content = "测试测试111",
            comment = comments,
            location = LatLng(0.0, 0.0),
            position = "测试地点111",
            photos = ArrayList<Uri>(),
            user = User(
                name = "developer2",
                avatar = Uri.parse("https://bkimg.cdn.bcebos.com/pic/2f738bd4b31c8701a18be16bb327892f0708293851bf?x-bce-process=image/resize,m_lfit,w_525,h_700,limit_0/quality,Q_90\n")
            )
        )
    )
    return array
}


