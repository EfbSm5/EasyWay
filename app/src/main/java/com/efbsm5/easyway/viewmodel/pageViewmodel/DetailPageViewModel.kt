package com.efbsm5.easyway.viewmodel.pageViewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.UserManager
import com.efbsm5.easyway.data.models.DynamicPost
import com.efbsm5.easyway.data.models.User
import com.efbsm5.easyway.data.repository.DataRepository
import com.efbsm5.easyway.data.models.Comment
import com.efbsm5.easyway.data.models.assistModel.CommentAndUser
import com.efbsm5.easyway.map.MapUtil
import com.efbsm5.easyway.map.MapUtil.getInitUser
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class DetailPageViewModel(val repository: DataRepository, val userManager: UserManager) :
    ViewModel() {
    private lateinit var _dynamicPost: MutableStateFlow<DynamicPost>
    private var _postUser = MutableStateFlow(getInitUser())
    private val _commentAndUsers = MutableStateFlow(emptyList<CommentAndUser>().toMutableList())
    val postUser: StateFlow<User> = _postUser
    val commentAndUser: StateFlow<MutableList<CommentAndUser>> = _commentAndUsers
    val post: StateFlow<DynamicPost?> = _dynamicPost

    fun getPost(dynamicPost: DynamicPost) {
        _dynamicPost.value = dynamicPost
        getData()
    }

    private fun getData() {
        viewModelScope.launch(Dispatchers.IO) {
            _postUser.value = repository.getUserById(_dynamicPost.value.userId)
            repository.getAllCommentsById(commentId = _dynamicPost.value.commentId)
                .collect { comments ->
                    _commentAndUsers.value.clear()
                    comments.forEach {
                        _commentAndUsers.value.add(
                            CommentAndUser(
                                repository.getUserById(it.userId), it
                            )
                        )
                    }
                }

        }
    }

    fun likePost(boolean: Boolean) {
        if (boolean) {
            _dynamicPost.value.like + 1
            repository.addLikeForPost(_dynamicPost.value.id)
        } else {
            _dynamicPost.value.like - 1
            repository.decreaseLikeForPost(_dynamicPost.value.id)
        }
    }

    fun likeComment(boolean: Boolean, commentIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (boolean) {
                _commentAndUsers.value.find { commentAndUser ->
                    commentAndUser.comment.index == commentIndex
                }!!.comment.like + 1
                repository.addLikeForComment(commentIndex)
            } else {
                _commentAndUsers.value.find { commentAndUser ->
                    commentAndUser.comment.index == commentIndex
                }!!.comment.like - 1
                repository.decreaseLikeForComment(commentIndex)
            }
        }
    }

    fun dislikeComment(boolean: Boolean, commentIndex: Int) {
        viewModelScope.launch(Dispatchers.IO) {
            if (boolean) {
                _commentAndUsers.value.find { commentAndUser ->
                    commentAndUser.comment.index == commentIndex
                }!!.comment.dislike + 1
                repository.addDisLikeForComment(commentIndex)
            } else {
                _commentAndUsers.value.find { commentAndUser ->
                    commentAndUser.comment.index == commentIndex
                }!!.comment.dislike - 1
                repository.decreaseDisLikeForComment(commentIndex)
            }
        }
    }

    fun comment(string: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val comment = Comment(
                index = repository.getCommentCount() + 1,
                commentId = _dynamicPost.value.commentId,
                userId = userManager.userId,
                content = string,
                like = 0,
                dislike = 0,
                date = MapUtil.getCurrentFormattedTime()
            )
            repository.uploadComment(comment)
            _commentAndUsers.value.add(
                CommentAndUser(
                    user = repository.getUserById(userManager.userId), comment = comment
                )
            )
        }
    }
}