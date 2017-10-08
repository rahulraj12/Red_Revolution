package com.example.rahulraj.red_revolution.location;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.v4.app.ActivityCompat;
import android.util.Log;

import com.example.rahulraj.red_revolution.R;

import java.util.List;



    public abstract class LocationCaptureTask extends AsyncTask<Void, Void, Location> {

        private Context context;
        private ProgressDialog dialog;

        private Location newLocation, bestLocation;
        private com.example.rahulraj.red_revolution.location.LocationClient locationClient;
        private static final int GPS_WAIT_TIME = 30 * 1000;
        private static final int THREAD_SLEEP_TIME = 2 * 1000;

        private static final String TAG = LocationCaptureTask.class.getSimpleName();

        public LocationCaptureTask(Context context) {
            super();
            Log.d(TAG, "inside LocationCaptureTask");
            this.context = context;
            locationClient = new com.example.rahulraj.red_revolution.location.LocationClient(context) {

                @Override
                public void onLocationChanged(Location location) {
                    Log.d(TAG, "inside onLocationChanged");

                    newLocation = location;
                }
            };
        }

        boolean toggleGPS = false;

        boolean isCancelled;
        @Override
        protected void onPreExecute() {
            Log.d(TAG, "inside onPreExecute");
            locationClient.start();
            dialog = new ProgressDialog(context);
            dialog.setMessage(context.getResources().getString(R.string.Fetchinglocation));
            dialog.setCanceledOnTouchOutside(true);
            dialog.show();

            dialog.setOnCancelListener(new DialogInterface.OnCancelListener() {
                @Override
                public void onCancel(DialogInterface dialog) {
                    isCancelled = true;
                }
            });
            super.onPreExecute();
        }

        private Location getLocationMethod1() {
            Log.d(TAG, "inside getLocationMethod1");
            // Get the location manager
            LocationManager locationManager = (LocationManager) context
                    .getSystemService(Service.LOCATION_SERVICE);
            Criteria criteria = new Criteria();
            String bestProvider = locationManager.getBestProvider(criteria,
                    false);
            Location location;
            if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                return null;
            }
            location = locationManager.getLastKnownLocation(bestProvider);
            return location;
        }

        private Location getLocationMethod2() {
            Log.d(TAG, "inside getLocationMethod2");
            LocationManager lm = (LocationManager) context
                    .getSystemService(Context.LOCATION_SERVICE);
            List<String> providers = lm.getProviders(false);

            Location l = null;

            for (int i = providers.size() - 1; i >= 0; i--) {
                Log.d(TAG, "inside for getLocationMethod2");
                if (ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(context, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    return null;
                }
                l = lm.getLastKnownLocation(providers.get(i));
                Log.d(TAG, "call getLastKnownLocation");
                if (l != null)
                    break;
            }
            return l;
        }

        @Override
        protected Location doInBackground(Void... params) {

            try {
                Log.d(TAG, "doInBackground");


                int maxCount = GPS_WAIT_TIME / THREAD_SLEEP_TIME;
                int count = 0;

                if (bestLocation == null) {
                    Log.d(TAG, "call method 1");
                    bestLocation = getLocationMethod1();
                }
                if (bestLocation == null) {
                    Log.d(TAG, "call method 2");
                    bestLocation = getLocationMethod2();
                }

                    while (count <= maxCount && !isCancelled) {
                        count++;
                        try {
                            Thread.sleep(THREAD_SLEEP_TIME);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }

                        if (isLocationBetter(newLocation, bestLocation)) {
                            bestLocation = newLocation;
                            Log.d(TAG, "if is better location");
                            break;
                        }
                    }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return bestLocation;
        }

        private boolean isLocationBetter(Location newLocation,
                                         Location currentLocation) {
            Log.d(TAG, "inside isLocationBetter");
            boolean retval = false;
            if (currentLocation == null) {
                if (newLocation == null) {
                    retval = false;
                } else {
                    retval = true;
                }
            } else if (newLocation == null) {
                retval = false;
            } else {

                boolean isNewerLocation = newLocation.getTime()
                        - currentLocation.getTime() >= 0 ? true : false;
                boolean isMoreAccurate = newLocation.getAccuracy()
                        - currentLocation.getAccuracy() <= 500 ? true : false;
                if (isMoreAccurate) {
                    if (isNewerLocation) {
                        retval = true;
                    } else {
                        retval = false;

                    }
                } else {
                    if (!isNewerLocation) {
                        retval = false;
                    } else {
                            retval = true;

                    }
                }
            }

            return retval;
        }

        @Override
        protected void onPostExecute(Location result) {
            super.onPostExecute(result);
            Log.d(TAG, "inside onPostExecute");
            dialog.dismiss();
            locationClient.stop();
            afterLocationCapture(result);
        }

        public abstract void afterLocationCapture(Location location);

    }