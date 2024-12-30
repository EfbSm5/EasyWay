import com.amap.api.maps.MapView
import com.amap.api.maps.model.LatLng
import com.amap.api.maps.model.Marker
import com.amap.api.maps.model.MarkerOptions
import com.efbsm5.easyway.data.MarkerData

fun fromMarker(marker: Marker): MarkerData {
    val markerData = MarkerData(
        latitude = marker.position.latitude,
        longitude = marker.position.longitude,
        title = marker.title,
        snippet = marker.snippet
    )
    return markerData
}


fun toMarker(markerData: MarkerData, mapView: MapView) {
    val markerOptions = MarkerOptions().position(LatLng(markerData.latitude, markerData.longitude))
        .title(markerData.title).snippet(markerData.snippet)
    mapView.map.addMarker(markerOptions)
}

