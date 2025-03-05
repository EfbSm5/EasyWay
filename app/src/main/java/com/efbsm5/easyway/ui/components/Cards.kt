package com.efbsm5.easyway.ui.components

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.viewmodel.compose.viewModel
import com.efbsm5.easyway.viewmodel.ViewModelFactory
import com.efbsm5.easyway.viewmodel.componentsViewmodel.CommentAndHistoryCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPlaceCardViewModel
import com.efbsm5.easyway.viewmodel.componentsViewmodel.NewPointCardViewModel
import com.efbsm5.easyway.viewmodel.pageViewmodel.Screen

@Composable
fun MapPageCard(content: Screen, onChangeScreen: (Screen) -> Unit) {
    val context = LocalContext.current
    val commentAndHistoryCardViewModel =
        viewModel<CommentAndHistoryCardViewModel>(factory = ViewModelFactory(context))
    val newPointCardViewModel =
        viewModel<NewPointCardViewModel>(factory = ViewModelFactory(context))
    val newPlaceCardViewModel =
        viewModel<NewPlaceCardViewModel>(factory = ViewModelFactory(context))
    when (content) {
        is Screen.Comment -> {
            CommentAndHistoryCard(
                marker = content.marker,
                viewModel = commentAndHistoryCardViewModel,
                poiItemV2 =
            )
        }

        Screen.IconCard -> FunctionCard(onclick = {
            onChangeScreen(Screen.Places(it))
        }, onChangePage = { onChangeScreen(Screen.Search) })

        is Screen.NewPoint -> NewPointCard(
            content.location,
            back = { onChangeScreen(Screen.IconCard) },
            viewModel = newPointCardViewModel
        )

        is Screen.Places -> {
            newPlaceCardViewModel.getLocation(latLng = latLng)
            newPlaceCardViewModel.search(string = txet, context = context)
            NewPlaceCard(location, content.name, onNavigate = {
                navigate(it)
            })
        }

        Screen.Search -> ShowSearchScreen(markerList = markerList, searchForPoi = {
            getPoint(
                it
            )
        }, onSelected = {
            onChangeScreen(
                Screen.Comment(
                    marker = null
                )
            )
        })
    }
}