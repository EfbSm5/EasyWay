package com.efbsm5.easyway.page

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.BottomSheetScaffold
import androidx.compose.material3.BottomSheetScaffoldState
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.rememberBottomSheetScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.lifecycle.viewmodel.compose.viewModel
import com.amap.api.maps.AMapOptions
import com.amap.api.maps.MapView
import com.amap.api.maps.model.BitmapDescriptor
import com.amap.api.maps.model.BitmapDescriptorFactory
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.amap.api.services.core.PoiItemV2
import com.efbsm5.easyway.components.AddAndLocateButton
import com.efbsm5.easyway.components.CommentAndHistoryCard
import com.efbsm5.easyway.components.FunctionCard
import com.efbsm5.easyway.components.NewPlaceCard
import com.efbsm5.easyway.components.NewPointCard
import com.efbsm5.easyway.data.EasyPoint
import com.efbsm5.easyway.data.EasyPointSimplify
import com.efbsm5.easyway.database.fromMarkerToPoints
import com.efbsm5.easyway.map.MapController
import com.efbsm5.easyway.map.MapSearchController
import com.efbsm5.easyway.map.MapUtil.showMsg
import com.efbsm5.easyway.viewmodel.PointsViewModel
import com.efbsm5.easyway.viewmodel.PointsViewModelFactory

private const val TAG = "MapPage"

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapPage() {
    val context = LocalContext.current

    var content: Screen by remember { mutableStateOf(Screen.IconCard) }
    var searchBarText by remember { mutableStateOf("") }
    val scaffoldState = rememberBottomSheetScaffoldState()
    var selectedMarker: Marker? by remember { mutableStateOf(null) }
    var newPoint: EasyPoint
    var pois by remember { mutableStateOf(ArrayList<PoiItemV2>()) }

    val mapController = MapController(
        onPoiClick = { showMsg(it!!.name, context) },
        onMapClick = { showMsg(it!!.latitude.toString(), context) },
        onMarkerClick = {
            it?.let {
                showMsg(it.id, context)
                content = Screen.Comment
                selectedMarker = it
            }
        },
        context = context
    )

    mapController.MapLifecycle(mapView)
    val mapSearch = MapSearchController(context) { pois = it }
//    LaunchedEffect(points) {
//        points?.forEach {
//            mapView.map.addMarker(
//                MarkerOptions().position(LatLng(it.lat, it.lng)).title(it.name).icon(
//                    getIcon()
//                )
//            )
//            Log.e(TAG, "InitPoints: add point ${it.name}, ${it.lat}, ${it.lng}")
//        }
//        mapView.map.addMarker(
//            MarkerOptions().position(LatLng(30.513447, 114.426866)).title("666")
//                .icon(BitmapDescriptorFactory.defaultMarker())
//        )
//        mapView.map.addMarker(
//            MarkerOptions().position(LatLng(30.513447, 114.426866)).title("666")
//                .icon(BitmapDescriptorFactory.defaultMarker())
//        )
//    }
//    InitPoints(
//        mapView = mapView, points = points
//    )
//不理解，log有输出也不显示点位
    MapContent(
        scaffoldState = scaffoldState,
        mapView = mapView,
        onAdd = { content = Screen.NewPoint },
        onLocate = { mapController.onLocate(mapView = mapView) },
        content = {
            when (content) {
                Screen.Comment -> CommentAndHistoryCard(
                    fromMarkerToPoints(
                        context = context, selectedMarker!!
                    )
                )

                Screen.IconCard -> FunctionCard(text = searchBarText, onclick = {
                    content = Screen.Places
                    mapSearch.searchForPoi(it)
                }, onTextChange = { searchBarText = it })

                Screen.NewPoint -> NewPointCard(onUploadPoint = {
                    newPoint = it
                    newPoint.lat = mapController.getLastKnownLocation()!!.latitude
                    newPoint.lng = mapController.getLastKnownLocation()!!.longitude
                })

                Screen.Places -> NewPlaceCard(
                    mapController.getLastKnownLocation()!!, pois = pois, easyPoints = null
                )
            }
        },
    )
    BackHandler(enabled = content != Screen.IconCard, onBack = { content = Screen.IconCard })
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun MapContent(
    scaffoldState: BottomSheetScaffoldState,
    mapView: MapView,
    onAdd: () -> Unit,
    onLocate: () -> Unit,
    content: @Composable () -> Unit
) {
    BottomSheetScaffold(
        scaffoldState = scaffoldState, sheetContent = {
            content()
        }, sheetPeekHeight = 120.dp
    ) {
        AndroidView(modifier = Modifier.fillMaxSize(), factory = { mapView })
        AddAndLocateButton(onAdd = {
            onAdd()
        }, onLocate = { onLocate() })
    }
}

sealed interface Screen {
    data object IconCard : Screen
    data object NewPoint : Screen
    data object Places : Screen
    data object Comment : Screen
}

private fun getIcon(): BitmapDescriptor {
    return BitmapDescriptorFactory.defaultMarker(5f)
}
//
//@Composable
//private fun InitPoints(mapView: MapView, points: List<EasyPointSimplify>?) {
//
//}