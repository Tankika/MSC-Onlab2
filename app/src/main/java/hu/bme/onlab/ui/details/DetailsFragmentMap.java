package hu.bme.onlab.ui.details;

import android.support.v4.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.GoogleMapOptions;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import hu.bme.onlab.model.post.GetPostResponse;

public class DetailsFragmentMap extends SupportMapFragment {

    protected static Fragment newInstance(final GetPostResponse getPostResponse) {
        final LatLng mapPosition = new LatLng(getPostResponse.getLatitude(), getPostResponse.getLongitude());
        GoogleMapOptions googleMapOptions = new GoogleMapOptions();
        CameraPosition cameraPosition = CameraPosition.builder()
                .target(mapPosition)
                .zoom(12)
                .build();
        googleMapOptions.camera(cameraPosition);
        SupportMapFragment fragment = SupportMapFragment.newInstance(googleMapOptions);

        fragment.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(GoogleMap googleMap) {
                googleMap.addMarker(new MarkerOptions()
                        .position(mapPosition))
                        .setTitle(getPostResponse.getPostalCode() + ", " +  getPostResponse.getCity());
            }
        });

        return fragment;
    }
}
