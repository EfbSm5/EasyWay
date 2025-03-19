package com.efbsm5.easyway.ui.components.mapcards

import androidx.compose.runtime.Composable
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.viewmodel.componentsViewmodel.CommentAndHistoryCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPointCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.FunctionCardViewModel
import org.koin.androidx.compose.koinViewModel
import org.koin.core.parameter.parametersOf

@Composable
fun MapPageCard(
    onNavigate: (LatLng) -> Unit, content: Screen, onChangeScreen: (Screen) -> Unit
) {
    when (content) {
        is Screen.Comment -> {
            val commentAndHistoryCardViewModel: CommentAndHistoryCardViewModel =
                koinViewModel(parameters = { parametersOf(content.easyPoint) })
            CommentAndHistoryCard(
                viewModel = commentAndHistoryCardViewModel,
                navigate = onNavigate,
                changeScreen = onChangeScreen
            )
        }

        Screen.Function -> {
            val functionCardViewModel: FunctionCardViewModel = koinViewModel()
            FunctionCard(
                viewModel = functionCardViewModel, changeScreen = onChangeScreen,
                navigate = onNavigate,
            )
        }

        is Screen.NewPoint -> {
            val newPointCardViewModel: NewPointCardViewModel = koinViewModel()
            NewPointCard(
                viewModel = newPointCardViewModel,
                changeScreen = onChangeScreen,
                label = content.label
            )
        }
    }
}

sealed interface Screen {
    data object Function : Screen
    data class NewPoint(val label: String) : Screen
    data class Comment(val easyPoint: EasyPoint) : Screen
}