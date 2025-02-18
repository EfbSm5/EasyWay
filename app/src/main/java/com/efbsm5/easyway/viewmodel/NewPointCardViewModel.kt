package com.efbsm5.easyway.viewmodel

import android.content.Context
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
    private var _tempPoint = MutableStateFlow(EasyPoint())
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