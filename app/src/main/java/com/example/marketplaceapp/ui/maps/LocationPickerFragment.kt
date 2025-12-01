package com.example.marketplaceapp.ui.location

import android.location.Geocoder
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.example.marketplaceapp.R
import com.example.marketplaceapp.databinding.FragmentLocationPickerBinding
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class LocationPickerFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentLocationPickerBinding? = null
    private var typedAddress: String? = null

    private val binding get() = _binding!!

    private lateinit var googleMap: GoogleMap
    private var selectedLatLng: LatLng? = null

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLocationPickerBinding.inflate(inflater, container, false)
        binding.btnLocationBack.bringToFront()
        binding.locationSearchInputLayout.bringToFront()
        binding.btnLocationBack.translationZ = 16f
        binding.locationSearchInputLayout.translationZ = 16f

        //load map fragment
        val mapFragment = childFragmentManager.findFragmentById(R.id.mapFragment)
                as SupportMapFragment
        mapFragment.getMapAsync(this)

        // back button
        binding.btnLocationBack.setOnClickListener {
            findNavController().navigateUp()
        }

        //search bar- convert text to lat/lng
        binding.locationSearchBar.setOnEditorActionListener { v, actionId, event ->
            val text = binding.locationSearchBar.text.toString().trim()
            if (text.isNotEmpty()) searchAddress(text)
            true
        }

        // confirm- return address
        binding.btnConfirmLocation.setOnClickListener {
            if (selectedLatLng == null) {
                Toast.makeText(requireContext(), "Pick a location", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val finalAddress = typedAddress ?: run {
                val geocoder = Geocoder(requireContext())
                val result = geocoder.getFromLocation(
                    selectedLatLng!!.latitude,
                    selectedLatLng!!.longitude,
                    1
                )
                result?.firstOrNull()?.getAddressLine(0) ?: "Unknown location"
            }

            parentFragmentManager.setFragmentResult(
                "locationRequestKey",
                bundleOf("selectedAddress" to finalAddress)
            )

            findNavController().navigateUp()
        }


        return binding.root
    }

    override fun onMapReady(map: GoogleMap) {
        googleMap = map

        //move camera to Toronto initially
        val toronto = LatLng(43.6532, -79.3832)
        googleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(toronto, 12f))

        // when user taps map- place marker
        googleMap.setOnMapClickListener { latLng ->
            googleMap.clear()
            googleMap.addMarker(MarkerOptions().position(latLng))
            selectedLatLng = latLng
        }
    }

    //search address and update map
    private fun searchAddress(query: String) {
        typedAddress = query  // save what user typed

        val geocoder = Geocoder(requireContext())
        val list = geocoder.getFromLocationName(query, 1)

        if (list.isNullOrEmpty()) {
            Toast.makeText(requireContext(), "Address not found", Toast.LENGTH_SHORT).show()
            return
        }

        val address = list.first()
        val latLng = LatLng(address.latitude, address.longitude)

        googleMap.clear()
        googleMap.addMarker(MarkerOptions().position(latLng))
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))

        selectedLatLng = latLng
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
