package com.example.saturday;

import android.Manifest;
import android.app.Activity;
import android.support.v4.app.ActivityCompat;

/**
 * Created by krishna on 19/11/16.
 */
public class Utils {
  private static final String TAG = "Utils";

  public static void getLocationPermission (Activity activity) {
    ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 555);
  }
}
