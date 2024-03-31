package com.mobdeve.s11.patawaran.yuan.dummy2

import android.Manifest
import android.content.pm.PackageManager
import android.location.Geocoder
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import org.osmdroid.api.IMapController
import org.osmdroid.config.Configuration
import org.osmdroid.events.MapListener
import org.osmdroid.events.ScrollEvent
import org.osmdroid.events.ZoomEvent
import org.osmdroid.tileprovider.tilesource.TileSourceFactory
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import org.osmdroid.views.overlay.mylocation.GpsMyLocationProvider
import org.osmdroid.views.overlay.mylocation.MyLocationNewOverlay
import org.osmdroid.util.GeoPoint
import java.io.IOException

class mapsPage : Fragment(), MapListener {

    private lateinit var mMap: MapView
    private lateinit var controller: IMapController
    private lateinit var mMyLocationOverlay: MyLocationNewOverlay
    private lateinit var searchView: SearchView
    private var searchMarker: Marker? = null

    companion object {
        private const val MY_PERMISSIONS_REQUEST_LOCATION = 1
    }

    private var shouldFollowLocation: Boolean = true

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_maps_page, container, false)
        Configuration.getInstance().userAgentValue = "travary"

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

    private fun initializeMap(view: View) {
        mMap = view.findViewById(R.id.osmmap)
        mMap.setTileSource(TileSourceFactory.MAPNIK)
        mMap.setMultiTouchControls(true)

        searchView = view.findViewById(R.id.searchView)
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchLocation(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                // Handle search text changes if needed
                return true
            }
        })

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
                }
            }
        }

        controller.setZoom(20.5)

        mMap.overlays.add(mMyLocationOverlay)
        mMap.addMapListener(this)
    }

    private fun searchLocation(locationName: String) {
        // Disable follow location during the search
        shouldFollowLocation = false

        // Remove existing marker if present
        searchMarker?.let {
            mMap.overlays.remove(it)
            searchMarker = null
        }

        val geocoder = Geocoder(requireContext())
        try {
            val addresses = geocoder.getFromLocationName(locationName, 1)

            if (addresses?.isNotEmpty() == true) {
                val location = addresses[0]
                val lat = location.latitude
                val lon = location.longitude

                val newCenter = GeoPoint(lat, lon)
                controller.animateTo(newCenter)
                controller.setZoom(20.5) // You can adjust the zoom level as needed

                // Add a marker at the searched location
                searchMarker = Marker(mMap)
                searchMarker?.position = newCenter
                mMap.overlays.add(searchMarker)
            } else {
                // Handle the case where no addresses were found
                // You might want to show a message or handle it as per your application logic
            }
        } catch (e: IOException) {
            e.printStackTrace()
        } finally {
            // Re-enable follow location after the search
            shouldFollowLocation = true
        }
    }

    override fun onScroll(event: ScrollEvent?): Boolean {
        // Handle scroll events if needed
        return true
    }

    override fun onZoom(event: ZoomEvent?): Boolean {
        // Handle zoom events if needed
        return false
    }

    override fun onResume() {
        super.onResume()
        // Ensure that follow location is enabled when the fragment is resumed
        shouldFollowLocation = true
    }
}
