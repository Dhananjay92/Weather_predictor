package com.example.saturday;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback, GoogleMap.OnMapClickListener, View.OnClickListener {
  private static final String TAG = "MapsActivity";
  private GoogleMap mMap;
  private MarkerOptions markerOption;
  private LatLng curLatLng;
  private Button btnSubmitLocation;

  @Override
  protected void onCreate (Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_maps);
    SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
            .findFragmentById(R.id.map);
    mapFragment.getMapAsync(this);
    btnSubmitLocation = (Button) findViewById(R.id.btn_submit);
    btnSubmitLocation.setOnClickListener(this);
  }

  @Override
  public void onMapReady (GoogleMap googleMap) {
    mMap = googleMap;
    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      Utils.getLocationPermission(this);
    } else
      onPermissionGraanted();
  }


  @Override
  public void onRequestPermissionsResult (int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
    if (requestCode == 555 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
      onPermissionGraanted();
    }
    super.onRequestPermissionsResult(requestCode, permissions, grantResults);
  }

  private void onPermissionGraanted () {
    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      return;
    }
    mMap.setMyLocationEnabled(true);
    mMap.setOnMapClickListener(this);
    markerOption = new MarkerOptions();
    getCurrentLocation();
  }

  private void getCurrentLocation () {
    if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
      Utils.getLocationPermission(this);
      return;
    }
    LocationManager locman = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
    locman.requestLocationUpdates(LocationManager.GPS_PROVIDER, 5000, 10, new android.location.LocationListener() {
      @Override
      public void onLocationChanged (Location location) {
        curLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        curLatLng = new LatLng(location.getLatitude(), location.getLongitude());
        mMap.moveCamera(CameraUpdateFactory.newLatLng(curLatLng));
        if (mMap != null) {
          mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(curLatLng, 16.0f));
        }
      }

      @Override
      public void onStatusChanged (String provider, int status, Bundle extras) {

      }

      @Override
      public void onProviderEnabled (String provider) {

      }

      @Override
      public void onProviderDisabled (String provider) {

      }
    });
  }

  private void changeMarkerPosition (LatLng loc) {
    curLatLng = loc;
    mMap.clear();
    if (mMap != null) {
      mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(loc, 16.0f));
    }
  }

  @Override
  public void onMapClick (LatLng latLng) {
    changeMarkerPosition(latLng);
    makeGetApiRequest(latLng);
    markerOption.position(latLng);
    mMap.addMarker(markerOption);
  }

  @Override
  public void onClick (View v) {
    if (v.getId() == R.id.btn_submit) {

      Intent intent = new Intent();
      if (curLatLng != null) {
        intent.putExtra("lat", curLatLng.latitude + "");
        intent.putExtra("lng", curLatLng.longitude + "");
      }
      setResult(RESULT_OK, intent);
      finish();

    }
  }

  private void makeGetApiRequest (final LatLng latLng) {
    ApiInterface apiService =
            ApiClient.getClient().create(ApiInterface.class);

    Call<DataModel> call = apiService.getData(latLng.latitude + "", latLng.longitude + "");
    call.enqueue(new Callback<DataModel>() {
      @Override
      public void onResponse (Call<DataModel> call, Response<DataModel> response) {
        DataModel model = response.body();
        mMap.clear();
        MarkerOptions markerOptions = new MarkerOptions().position(latLng).title(model.desc + ":" + model.rain);
        mMap.addMarker(markerOptions).showInfoWindow();
        Log.d(TAG, "model: " + model);
      }

      @Override
      public void onFailure (Call<DataModel> call, Throwable t) {
        Log.e(TAG, t.toString());
      }
    });
  }
}
