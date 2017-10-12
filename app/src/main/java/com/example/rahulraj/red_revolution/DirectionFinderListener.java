package com.example.rahulraj.red_revolution;

import java.util.List;

/**
 * Created by Rahul Raj on 16-03-2017.
 */

public interface DirectionFinderListener {
    void onDirectionFinderStart();
    void onDirectionFinderSuccess(List<Route> route);
}
