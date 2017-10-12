package com.example.rahulraj.red_revolution;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;


/**
 * Created by Rahul Raj on 16-03-2017.
 */

public class Route {
    public Distance distance;
    public Duration duration;
    public String endAddress;
    public LatLng endLocation;
    public String startAddress;
    public LatLng startLocation;

    public List<LatLng> points;
}
