package com.efbsm5.easyway.ui.components.mapcards

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amap.api.maps.model.LatLng
import com.efbsm5.easyway.viewmodel.ViewModelFactory
import com.efbsm5.easyway.viewmodel.componentsViewmodel.CommentAndHistoryCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPlaceCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPointCardViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.Screen
import com.efbsm5.easyway.viewmodel.componentsViewmodel.FunctionCardViewModel

@Composable
fun MapPageCard(
    content: Screen,
    onChangeScreen: (Screen) -> Unit,
    location: LatLng,
    onNavigate: (LatLng, Boolean) -> Unit
) {
    val context = LocalContext.current
    val commentAndHistoryCardViewModel =
        viewModel<CommentAndHistoryCardViewModel>(factory = ViewModelFactory(context))
    val newPointCardViewModel =
        viewModel<NewPointCardViewModel>(factory = ViewModelFactory(context))
    val newPlaceCardViewModel =
        viewModel<NewPlaceCardViewModel>(factory = ViewModelFactory(context))
    val functionCardViewModel =
        viewModel<FunctionCardViewModel>(factory = ViewModelFactory(context))
    when (content) {
        is Screen.Comment -> {
            if (content.marker != null) {
                commentAndHistoryCardViewModel.getPoint(content.marker.position)
            } else if (content.poi != null) {
                commentAndHistoryCardViewModel.addPoi(content.poi)
            } else if (content.poiItemV2 != null) {
                commentAndHistoryCardViewModel.addPoiItem(content.poiItemV2)
            }
            CommentAndHistoryCard(
                viewModel = commentAndHistoryCardViewModel, navigate = { onNavigate(it, true) })
        }

        Screen.IconCard -> FunctionCard(
            viewModel = functionCardViewModel, location = location, changeScreen = onChangeScreen
        )

        is Screen.NewPoint -> NewPointCard(
            content.location,
            back = { onChangeScreen(Screen.IconCard) },
            viewModel = newPointCardViewModel
        )

        is Screen.Places -> {
            newPlaceCardViewModel.getLocation(latLng = location)
            newPlaceCardViewModel.search(string = content.name, context = context)
            NewPlaceCard(
                onNavigate = { latLng: LatLng, boolean: Boolean -> onNavigate(latLng, boolean) },
                viewModel = newPlaceCardViewModel
            )
        }
    }
}