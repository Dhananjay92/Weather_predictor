package com.example.saturday;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by krishna on 19/11/16.
 */
public interface ApiInterface {
  @GET ("/getRainData")
  Call<DataModel> getData (@Query ("lat") String latitude, @Query ("lon") String longitude);

  @GET ("/sendData")
  Call<DataModel> sendData (@Query ("lat") String latitude, @Query ("lon") String longitude, @Query ("p") String pressure, @Query ("h") String humadity, @Query ("t") String temperat);
}
