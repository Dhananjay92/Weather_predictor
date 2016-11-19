package com.example.saturday;

import com.google.gson.annotations.SerializedName;

/**
 * Created by krishna on 19/11/16.
 */
public class DataModel {
  @SerializedName ("rain")
  public double rain;
  @SerializedName ("desc")
  public String desc;

  @Override
  public String toString () {
    return "DataModel{" +
            "rain='" + rain + '\'' +
            ", desc='" + desc + '\'' +
            '}';
  }
}
