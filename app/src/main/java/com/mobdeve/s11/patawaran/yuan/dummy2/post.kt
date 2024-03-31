package com.mobdeve.s11.patawaran.yuan.dummy2

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.util.GeoPoint
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import java.io.ByteArrayOutputStream
import java.util.Locale
import java.io.IOException
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent


class post : Fragment(), MapListener {

    private lateinit var mMap: MapView
    private lateinit var controller: IMapController
    private lateinit var mMyLocationOverlay: MyLocationNewOverlay
    private lateinit var locationInfoTextView: TextView

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 1
    }

    private var shouldFollowLocation: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_post, container, false)
        Configuration.getInstance().userAgentValue = "travary"

        // Initialize TextView
        locationInfoTextView = view.findViewById(R.id.locationInfoTextView)

        // Check and request location permissions
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            // Request the permission
            ActivityCompat.requestPermissions(
                requireActivity(),
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                ),
                MY_PERMISSIONS_REQUEST_LOCATION
            )
        } else {
            // Permission already granted, initialize the map
            initializeMap(view)
        }

        return view
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Find the record button
        val recordButton = view.findViewById<Button>(R.id.recordButton)

        // Set OnClickListener for the record button
        recordButton.setOnClickListener {
            // Capture the map screenshot
            val mapScreenshot = captureMapScreenshot()

            // Convert Bitmap to ByteArray
            val stream = ByteArrayOutputStream()
            mapScreenshot?.compress(Bitmap.CompressFormat.PNG, 100, stream)
            val byteArray = stream.toByteArray()

            // Create an intent to start the record activity
            val intent = Intent(requireContext(), record::class.java)

            // Pass the screenshot as a byte array to the intent extras
            intent.putExtra("mapScreenshot", byteArray)

            // Start the record activity
            startActivity(intent)
        }
    }

    private fun initializeMap(view: View) {
        mMap = view.findViewById(R.id.osmmap2)
        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.setMultiTouchControls(true)

        val locationProvider = GpsMyLocationProvider(requireContext())
        mMyLocationOverlay = MyLocationNewOverlay(locationProvider, mMap)
        controller = mMap.controller

        mMyLocationOverlay.enableMyLocation()
        mMyLocationOverlay.isDrawAccuracyEnabled = true

        mMyLocationOverlay.runOnFirstFix {
            requireActivity().runOnUiThread {
                if (shouldFollowLocation) {
                    val myLocation = mMyLocationOverlay.myLocation
                    controller.setCenter(myLocation)
                    controller.animateTo(myLocation)

                    // Update the location info TextView
                    updateLocationInfo(myLocation)
                }
            }
        }

        controller.setZoom(20.5)

        mMap.overlays.add(mMyLocationOverlay)
        mMap.addMapListener(this)
    }

    private fun updateLocationInfo(geoPoint: GeoPoint?) {
        try {
            if (geoPoint != null) {
                val geocoder = Geocoder(requireContext(), Locale.getDefault())
                val addresses: List<android.location.Address>? =
                    geocoder.getFromLocation(geoPoint.latitude, geoPoint.longitude, 1)

                if (addresses != null && addresses.isNotEmpty()) {
                    val country = addresses[0].countryName
                    val region = addresses[0].adminArea

                    val locationInfo = "$country, $region"
                    locationInfoTextView.text = locationInfo
                }
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    private fun captureMapScreenshot(): Bitmap? {
        // Get the MapView's drawing cache
        mMap.isDrawingCacheEnabled = true
        mMap.buildDrawingCache(true)
        val bitmap = Bitmap.createBitmap(mMap.drawingCache)
        mMap.isDrawingCacheEnabled = false

        return bitmap
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        // Handle scroll events if needed
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        // Handle zoom events if needed
        return false
    }
}
