package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.data.ViewModelRepository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class NewPointCardViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private var _tempPoint = MutableStateFlow(
        EasyPoint(
            commentId = 0,
            userId = 0,
            name = "",
            type = "",
            info = "",
            location = "",
            photo = ("https://27142293.s21i.faiusr.com/2/ABUIABACGAAg_I_bmQYokt25kQUwwAc4gAU.jpg".toUri()),
            refreshTime = "2024-12-29",
            likes = 100,
            dislikes = 10,
            lat = 37.7749,
            lng = -122.4194,
            pointId = 0
        )
    )
    val tempPoint: StateFlow<EasyPoint> = _tempPoint


    fun changeTempPoint(easyPoint: EasyPoint) {
        _tempPoint.value = easyPoint
    }

    fun publishPoint() {
        viewModelScope.launch(Dispatchers.IO) {
            repository.uploadPoint(_tempPoint.value)
        }
    }
}