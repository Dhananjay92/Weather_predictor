package com.example.saturday;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by krishna on 19/11/16.
 */
public class MainActivity extends AppCompatActivity implements View.OnClickListener {
  private static final String TAG = "MainActivity";
  private static final int REQ_CODE = 515;
  private EditText edTemperature;
  private EditText edPressure;
  private EditText edHumadity;
  private EditText edLocation;
  private Button btnSubmit;
  private ImageButton btnLocation;
  private TextView tvResult;
  private String lat = "";
  private String lng = "";

  @Override
  protected void onCreate (@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);

    edTemperature = (EditText) findViewById(R.id.ed_temparature);
    edPressure = (EditText) findViewById(R.id.ed_pressure);
    edHumadity = (EditText) findViewById(R.id.ed_humadity);
    edLocation = (EditText) findViewById(R.id.ed_location);
    btnSubmit = (Button) findViewById(R.id.btn_submit);
    btnLocation = (ImageButton) findViewById(R.id.btn_location);
    tvResult = (TextView) findViewById(R.id.tv_result);
    btnLocation.setOnClickListener(this);
    btnSubmit.setOnClickListener(this);
  }

  @Override
  public void onClick (View v) {
    switch (v.getId()) {
      case R.id.btn_submit:
        String temp = edTemperature.getText().toString();
        String pressure = edPressure.getText().toString();
        String humadity = edHumadity.getText().toString();
        makeSendApiRequest(lat, lng, temp, pressure, humadity);
        break;
      case R.id.btn_location:
        startActivityForResult(new Intent(MainActivity.this, MapsActivity.class), REQ_CODE);
        break;
    }
  }

  @Override
  protected void onActivityResult (int requestCode, int resultCode, Intent data) {
    if (requestCode == REQ_CODE) {
      lat = data.getStringExtra("lat");
      lng = data.getStringExtra("lng");
      edLocation.setText(lat + " , " + lng);
    }
    super.onActivityResult(requestCode, resultCode, data);
  }

  private void makeSendApiRequest (String lat, String lon, String temp, String pressure, String humadity) {
    ApiInterface apiService =
            ApiClient.getClient().create(ApiInterface.class);

    Call<DataModel> call = apiService.sendData(lat, lon, pressure, humadity, temp);
    call.enqueue(new Callback<DataModel>() {
      @Override
      public void onResponse (Call<DataModel> call, Response<DataModel> response) {
        DataModel model = response.body();
        tvResult.setText("Rain: " + model.rain + "\n" + "Description: " + model.desc);
        Log.d(TAG, "model: " + model);
      }

      @Override
      public void onFailure (Call<DataModel> call, Throwable t) {
        // Log error here since request failed
        Log.e(TAG, t.toString());
      }
    });
  }
}
