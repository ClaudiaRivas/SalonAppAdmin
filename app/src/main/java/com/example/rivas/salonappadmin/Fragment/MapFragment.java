package com.example.rivas.salonappadmin.Fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.rivas.salonappadmin.Apputilities.BaseFragment;
import com.example.rivas.salonappadmin.Apputilities.CustomInfoWindowAdapter;
import com.example.rivas.salonappadmin.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

/**
 * A simple {@link Fragment} subclass.
 */
public class MapFragment extends BaseFragment implements OnMapReadyCallback {


    View rootView;
    private MapView mMapView;
    private GoogleMap mGoogleMap;
    private final LatLng mLocation = new LatLng(13.7230736, -89.7258093);
    private Marker myMarker;

    public MapFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        rootView = inflater.inflate(R.layout.fragment_map, container, false);

        mMapView = (MapView) rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }
        MapsInitializer.initialize(this.getContext());
        mMapView.getMapAsync(this);

        return rootView;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        this.mGoogleMap = googleMap;

        CustomInfoWindowAdapter customInfoWindow = new CustomInfoWindowAdapter(LayoutInflater.from(getActivity()));
        mGoogleMap.setInfoWindowAdapter(customInfoWindow);

        myMarker = mGoogleMap.addMarker(new MarkerOptions()
                .position(mLocation).title((getString(R.string.app_name)))
                .icon((BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE))));

        mGoogleMap.moveCamera(CameraUpdateFactory.newLatLngZoom(mLocation, 15));
    }
}
