package com.example.sendit

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions

class MapFragment : Fragment(), OnMapReadyCallback {

    private var mMap: GoogleMap? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // 1. On gonfle le layout du fragment (assurez-vous que le fichier res/layout/fragment_map.xml existe)
        val view = inflater.inflate(R.layout.fragment_map, container, false)

        // 2. On récupère le SupportMapFragment inclus dans votre XML et on demande le chargement de la carte
        val mapFragment = childFragmentManager.findFragmentById(R.id.google_map_container) as? SupportMapFragment
        mapFragment?.getMapAsync(this)

        return view
    }

    /**
     * Cette fonction se déclenche automatiquement dès que la carte Google Maps est prête.
     */
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap

        // Exemple : Positionner la carte sur Paris par défaut avec un marqueur
        val paris = LatLng(48.8566, 2.3522)
        mMap?.addMarker(MarkerOptions().position(paris).title("Marker in Paris"))
        mMap?.moveCamera(CameraUpdateFactory.newLatLngZoom(paris, 12f)) // Zoom de 12
    }
}
