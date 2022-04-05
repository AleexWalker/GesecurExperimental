package com.gesecur.app.ui.incidences

import android.graphics.drawable.VectorDrawable
import androidx.core.content.ContextCompat
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentIncidenceMapBinding
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.utils.toBitmapDescriptor
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ToolbarOptions(
    showToolbar = false)
class IncidenceMapFragment : BaseFragment(R.layout.fragment_incidence_map), OnMapReadyCallback {

    private val binding by viewBinding(FragmentIncidenceMapBinding::bind)
    private val viewModel by sharedViewModel<IncidencesViewModel>()

    private lateinit var mMap: GoogleMap
    private lateinit var marker: Marker
    private lateinit var activePoiBD: BitmapDescriptor

    val args: IncidenceMapFragmentArgs by navArgs()

    override fun setupViews() {
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment)
            .getMapAsync(this@IncidenceMapFragment)

        activePoiBD = (ContextCompat.getDrawable(requireContext(), R.drawable.ic_location_on) as VectorDrawable).toBitmapDescriptor()!!

        binding.toolbarModal.setOptionButton(R.string.ADD_INCIDENCE_SAVE_BUTTON) {
            viewModel.changeIncidenceLocation(marker.position.latitude, marker.position.longitude)
            popBack()
        }

        binding.toolbarModal.setCloseAction { popBack() }

        binding.toolbarModal.title = getString(R.string.ADD_INCIDENCE_LOCALIZATION_TITLE)
    }

    override fun setupViewModels() {
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap


        printAddress(LatLng(args.lat.toDouble(), args.lon.toDouble()))
    }

    private fun printAddress(latlng: LatLng) {
        mMap.clear()

        mMap.setOnMarkerDragListener(object: GoogleMap.OnMarkerDragListener {
            override fun onMarkerDrag(p0: Marker?) {}

            override fun onMarkerDragEnd(p0: Marker?) {
                marker = p0!!
            }

            override fun onMarkerDragStart(p0: Marker?) {}
        })

        with(LatLng(latlng.latitude, latlng.longitude)) {
            marker = mMap.addMarker(MarkerOptions()
                .position(this)
                .icon(activePoiBD)
                .anchor(0.5f,1f)
                .draggable(true))

            mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(this, 12f))
        }
    }

    private fun popBack() {
        parentFragmentManager.primaryNavigationFragment?.findNavController()?.popBackStack()
    }
}