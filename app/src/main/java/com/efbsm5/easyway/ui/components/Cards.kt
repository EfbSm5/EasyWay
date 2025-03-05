package com.efbsm5.easyway.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amap.api.maps.model.LatLng
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.ui.page.SearchPage
import com.efbsm5.easyway.viewmodel.ViewModelFactory
import com.efbsm5.easyway.viewmodel.componentsViewmodel.CommentAndHistoryCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPlaceCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPointCardViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.Screen
import com.efbsm5.easyway.viewmodel.pageViewmodel.SearchPageViewModel

@Composable
fun MapPageCard(
    content: Screen,
    onChangeScreen: (Screen) -> Unit,
    poiItemV2: PoiItemV2?,
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
    val searchPageViewModel = viewModel<SearchPageViewModel>(factory = ViewModelFactory(context))
    when (content) {
        is Screen.Comment -> {
            CommentAndHistoryCard(
                marker = content.marker,
                viewModel = commentAndHistoryCardViewModel,
                poiItemV2 = poiItemV2
            )
        }

        Screen.IconCard -> FunctionCard(onclick = {
            onChangeScreen(Screen.Places(it))
        }, onChangeSearchPage = { onChangeScreen(Screen.Search) })

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

        Screen.Search -> {
            SearchPage(
                viewModel = searchPageViewModel,
                onSelected = {
                    onChangeScreen(
                        Screen.Comment(
                            marker = null
                        )
                    )
                }
            )
        }
    }
}