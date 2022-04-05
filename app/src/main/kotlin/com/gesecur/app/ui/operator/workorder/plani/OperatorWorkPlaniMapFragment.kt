package com.gesecur.app.ui.operator.workorder.plani

import android.content.Intent
import android.graphics.drawable.VectorDrawable
import android.net.Uri
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import by.kirich1409.viewbindingdelegate.viewBinding
import com.gesecur.app.R
import com.gesecur.app.databinding.FragmentOperatorWorkorderMapBinding
import com.gesecur.app.domain.models.WorkPlanification
import com.gesecur.app.ui.common.base.BaseFragment
import com.gesecur.app.ui.common.base.BaseViewModel
import com.gesecur.app.ui.common.toolbar.ToolbarOptions
import com.gesecur.app.ui.operator.OperatorViewModel
import com.gesecur.app.ui.operator.workorder.CustomInfoWindowAdapter
import com.gesecur.app.utils.toBitmapDescriptor
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.*
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

@ToolbarOptions(
    showToolbar = true)
class OperatorWorkPlaniMapFragment : BaseFragment(R.layout.fragment_operator_workorder_map), OnMapReadyCallback {

    private val binding by viewBinding(FragmentOperatorWorkorderMapBinding::bind)
    private val viewModel by sharedViewModel<OperatorViewModel>()

    private lateinit var mMap: GoogleMap
    private var lastMarkerSelected: Marker? = null

    private lateinit var inactivePoiBD: BitmapDescriptor
    private lateinit var activePoiBD: BitmapDescriptor

    override fun setupViews() {
        (childFragmentManager.findFragmentById(R.id.map) as SupportMapFragment)
            .getMapAsync(this@OperatorWorkPlaniMapFragment)

        activePoiBD = (ContextCompat.getDrawable(requireContext(), R.drawable.ic_location_on) as VectorDrawable).toBitmapDescriptor()!!
        inactivePoiBD = (ContextCompat.getDrawable(requireContext(), R.drawable.ic_location) as VectorDrawable).toBitmapDescriptor()!!
        binding.orderItem.separatorLine.isVisible = false
        binding.orderItem.root.isVisible = false

        binding.orderItem.root.setOnClickListener {
            viewModel.selectWorkPlani(lastMarkerSelected!!.tag as WorkPlanification)
        }
    }

    override fun setupViewModels() {
        viewModel.workPlanis.observe(viewLifecycleOwner, {
            onWorkPlanisLoaded(it)
        })
    }

    override fun stateManagedViewModels(): List<BaseViewModel>? {
        return arrayListOf(viewModel)
    }

    private fun onWorkPlanisLoaded(workPlaniList: List<WorkPlanification>) {
        if(!this::mMap.isInitialized) return

        printWorkPlanisOnMap(workPlaniList)
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        viewModel.workPlanis.value?.let {
            printWorkPlanisOnMap(it)
        }

        mMap.setOnMarkerClickListener {
            lastMarkerSelected?.setIcon(inactivePoiBD)

            it.setIcon(activePoiBD)
            lastMarkerSelected = it

            with(binding.orderItem) {
                root.isVisible = true
                tvDesc.text = (it.tag as WorkPlanification).desc
            }

            it.showInfoWindow()

            true
        }

        mMap.setOnInfoWindowClickListener {
            navigateToLocation(it.position.latitude, it.position.longitude)
        }

        mMap.setInfoWindowAdapter(CustomInfoWindowAdapter(requireContext()))
    }

    private fun printWorkPlanisOnMap(workPlaniList: List<WorkPlanification>) {
        mMap.clear()
        lastMarkerSelected = null

        if(workPlaniList.isEmpty()) return

        val latLngBounds = LatLngBounds.Builder()

        workPlaniList.forEach {
            with(LatLng(it.latitude, it.longitude)) {
                mMap.addMarker(MarkerOptions().position(this).icon(inactivePoiBD).anchor(0.5f,1f))
                    .tag = it

                latLngBounds.include(this)
            }
        }

        mMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds.build(), 100))
    }

    private fun navigateToLocation(lat: Double, lng: Double) {
        val gmmIntentUri =
            Uri.parse("google.navigation:q=$lat, $lng")
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps")
        startActivity(mapIntent)
    }
}