package com.example.mapwithmarker

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import android.app.AlertDialog
import android.view.LayoutInflater
import android.widget.TextView
import android.widget.Button


class MapsMarkerActivity : AppCompatActivity(), OnMapReadyCallback {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_maps)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as? SupportMapFragment
        mapFragment?.getMapAsync(this)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        val locations = listOf(
            LatLng(55.759585, 37.644650),
            LatLng(55.7666184, 37.631983),
            LatLng(55.760499, 37.627365),
            LatLng(55.755135502654426, 37.648749628189776),
            LatLng(55.75531664175077, 37.64613962450155)
        )


        val pos = LatLng(55.75750503493823, 37.64224705872067)
        for ((i, location) in locations.withIndex()) {
            googleMap.addMarker(
                MarkerOptions()
                    .position(location)
                    .title("Market$i")
            )
        }
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(pos))

        googleMap.setOnMarkerClickListener { marker ->
            val title: String = marker.title ?: "title"
            val marketInfo = MarkerInfo(title, "descrioption", "working hours")
            showMarkerInfoDialog(marketInfo)
            false
        }
    }


    data class MarkerInfo(val title: String, val description: String, val workingHours: String)

    private fun showMarkerInfoDialog(markerInfo: MarkerInfo) {
        val builder = AlertDialog.Builder(this)
        val inflater = LayoutInflater.from(this)
        val dialogView = inflater.inflate(R.layout.market_info, null)

        val titleTextView: TextView = dialogView.findViewById(R.id.titleTextView)
        val descriptionTextView: TextView = dialogView.findViewById(R.id.descriptionTextView)
        val workingHoursTextView: TextView = dialogView.findViewById(R.id.workingHoursTextView)

        titleTextView.text = markerInfo.title
        descriptionTextView.text = markerInfo.description
        workingHoursTextView.text = markerInfo.workingHours

        builder.setView(dialogView)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .create()
            .show()
    }
}