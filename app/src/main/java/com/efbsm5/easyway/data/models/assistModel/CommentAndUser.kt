package com.efbsm5.easyway.data.models.assistModel

import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.User

data class CommentAndUser(val user: User, val comment: Comment)