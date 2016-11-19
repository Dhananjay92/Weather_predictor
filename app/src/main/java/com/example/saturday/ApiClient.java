package com.example.saturday;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by krishna on 19/11/16.
 */
public class ApiClient {
  //public static final String BASE_URL = "http://35.154.28.222:8081";
  public static final String BASE_URL = "http://192.168.0.108:8081";
  private static Retrofit retrofit = null;

  public static Retrofit getClient () {
    if (retrofit == null) {
      retrofit = new Retrofit.Builder()
              .baseUrl(BASE_URL)
              .addConverterFactory(GsonConverterFactory.create())
              .build();
    }
    return retrofit;
  }
}
