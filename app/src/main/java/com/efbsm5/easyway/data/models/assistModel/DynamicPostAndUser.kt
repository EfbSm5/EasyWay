package com.efbsm5.easyway.data.models.assistModel

import android.net.Uri
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.Photo
import com.efbsm5.easyway.data.models.User

class DynamicPostAndUser(
    val dynamicPost: DynamicPost,
    val user: User,
    val commentCount: Int,
    val photo: List<Photo>
)