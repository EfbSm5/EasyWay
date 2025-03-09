package com.efbsm5.easyway.ui.components.mapcards

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Poi
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.data.models.EasyPoint
import com.efbsm5.easyway.viewmodel.ViewModelFactory
import com.efbsm5.easyway.viewmodel.componentsViewmodel.CommentAndHistoryCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPointCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.FunctionCardViewModel

@Composable
fun MapPageCard(
    location: LatLng,
    onNavigate: (LatLng) -> Unit,
    content: Screen,
    onChangeScreen: (Screen) -> Unit
) {
    val context = LocalContext.current
    val commentAndHistoryCardViewModel =
        viewModel<CommentAndHistoryCardViewModel>(factory = ViewModelFactory(context))
    val newPointCardViewModel =
        viewModel<NewPointCardViewModel>(factory = ViewModelFactory(context))
    val functionCardViewModel =
        viewModel<FunctionCardViewModel>(factory = ViewModelFactory(context))
    when (content) {
        is Screen.Comment -> {
            if (content.poi != null) {
                commentAndHistoryCardViewModel.addPoi(content.poi)
            } else if (content.poiItemV2 != null) {
                commentAndHistoryCardViewModel.addPoiItem(content.poiItemV2)
            } else if (content.easyPoint != null) {
                commentAndHistoryCardViewModel.addEasyPoint(content.easyPoint)
            }
            CommentAndHistoryCard(
                viewModel = commentAndHistoryCardViewModel,
                navigate = onNavigate,
                changeScreen = onChangeScreen
            )
        }

        Screen.Function -> FunctionCard(
            viewModel = functionCardViewModel, location = location, changeScreen = onChangeScreen,
            navigate = onNavigate,
        )

        is Screen.NewPoint -> NewPointCard(
            location = content.location,
            viewModel = newPointCardViewModel,
            changeScreen = onChangeScreen,
            label = content.label
        )
    }
}

sealed interface Screen {
    data object Function : Screen
    data class NewPoint(val location: LatLng, val label: String) : Screen
    data class Comment(
        val poi: Poi?, val poiItemV2: PoiItemV2?, val easyPoint: EasyPoint?
    ) : Screen
}