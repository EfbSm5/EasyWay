package com.efbsm5.easyway.viewmodel

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.data.EasyPointSimplify
import com.efbsm5.easyway.database.AppDataBase
import com.efbsm5.easyway.repository.DataRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class PointsViewModel(context: Context) : ViewModel() {
    private val repository = DataRepository(context)
    private val _points = MutableStateFlow<List<EasyPointSimplify>?>(null)
    val points: StateFlow<List<EasyPointSimplify>?> = _points

    fun fetchPoints() {
        viewModelScope.launch(Dispatchers.IO) {
            _points.value = repository.getAllPoints()
        }
    }

    fun insertPoint(point: EasyPoint, context: Context) {
        viewModelScope.launch(Dispatchers.IO) {
            val database = AppDataBase.getDatabase(context)
            database.pointsDao().insert(point)
        }
    }
}