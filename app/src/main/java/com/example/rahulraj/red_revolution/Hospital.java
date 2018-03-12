package com.example.rahulraj.red_revolution;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class Hospital extends AppCompatActivity {
    Spinner state_spinner, city_spinner;
    Double currLati, currLongi;
    List<Address> myadd;
    String state, JSON, city, locality;
    String states[] = {"--Select--", "Andaman & Nicobar", "Arunachal Pradesh", "Assam", "Bihar", "Chandigarh", "Chattisgarh", "Dadar & Nagar Haveli", "Daman & Diu", "Delhi", "Goa", "Gujarat", "Haryana", "Himachal Pradesh", "Jammu and Kashmir", "Jharkhand", "Karnataka", "Kerala", "Lakshadweep", "Madhya Pradesh", "Maharashtra", "Manipur", "Meghalaya", "Mizoram", "Nagaland", "Odisha", "Puducherry", "Punjab", "Rajasthan", "Sikkim", "Tamil Nadu", "Telangana", "Tripura", "Uttrakhand", "Uttar Pradesh", "West Bengal"};
    String url, district[];
    RequestQueue requestQueue;
    int len;
    BloodBankEntity[] bloodBankEntity;
    ListView listView;
    CheckInternet c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        c1 = new CheckInternet(this);
        boolean check = c1.checkInternet();
        if (!check) {
            Intent intent = new Intent(Hospital.this, NoInternet.class);
            startActivity(intent);
        }
        setContentView(R.layout.hospital_home);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
        currLati = Double.parseDouble(mPrefs.getString("Latitude", ""));
        currLongi = Double.parseDouble(mPrefs.getString("Longitude", ""));
        requestQueue = Volley.newRequestQueue(this);

        Geocoder geocoder = new Geocoder(Hospital.this, Locale.ENGLISH);
        try {
            myadd = geocoder.getFromLocation(currLati, currLongi, 10);
            Address address = myadd.get(0);
            state = address.getAdminArea();
            city = address.getSubAdminArea();
        } catch (IOException e) {
            e.printStackTrace();
        }

        getJSONDistrictData();//fetches json data of particular district

        state_spinner = (Spinner) findViewById(R.id.spinner);
        city_spinner = (Spinner) findViewById(R.id.spinner2);
        listView = (ListView) findViewById(R.id.listView);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, states);
        state_spinner.setAdapter(adapter);
        state_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(final AdapterView<?> parent, View view, int position, long l) {
                if (position == 0) {
                    city_spinner.setOnTouchListener(new View.OnTouchListener() {
                        @Override
                        public boolean onTouch(View view, MotionEvent motionEvent) {
                            if (parent.getSelectedItem().toString().equals("--Select--"))
                                Toast.makeText(Hospital.this, "Please select a state", Toast.LENGTH_SHORT).show();
                            return false;
                        }
                    });
                }
                if (position == 1) {
                    state = "AndamanNicobar";
                    getDistrictNames();
                } else if (position == 2) {
                    state = "ArunachalPradesh";
                    getDistrictNames();
                } else if (position == 3) {
                    state = "Assam";
                    getDistrictNames();
                } else if (position == 4) {
                    state = "Bihar";
                    getDistrictNames();
                } else if (position == 5) {
                    state = "Chandigarh";
                    getDistrictNames();
                } else if (position == 6) {
                    state = "Chhattisgarh";
                    getDistrictNames();
                } else if (position == 7) {
                    state = "DadraNagarHaveli";
                    getDistrictNames();
                } else if (position == 8) {
                    state = "DamanDiu";
                    getDistrictNames();
                } else if (position == 9) {
                    state = "Delhi";
                    getDistrictNames();
                } else if (position == 10) {
                    state = "Goa";
                    getDistrictNames();
                } else if (position == 11) {
                    state = "Gujarat";
                    getDistrictNames();
                } else if (position == 12) {
                    state = "Haryana";
                    getDistrictNames();
                } else if (position == 13) {
                    state = "HimachalPradesh";
                    getDistrictNames();
                } else if (position == 14) {
                    state = "JammuKashmir";
                    getDistrictNames();
                } else if (position == 15) {
                    state = "Jharkhand";
                    getDistrictNames();
                } else if (position == 16) {
                    state = "Karnataka";
                    getDistrictNames();
                } else if (position == 17) {
                    state = "Kerala";
                    getDistrictNames();
                } else if (position == 18) {
                    state = "Lakshadweep";
                    getDistrictNames();
                } else if (position == 19) {
                    state = "MadhyaPradesh";
                    getDistrictNames();
                } else if (position == 20) {
                    state = "Maharashtra";
                    getDistrictNames();
                } else if (position == 21) {
                    state = "Manipur";
                    getDistrictNames();
                } else if (position == 22) {
                    state = "Meghalaya";
                    getDistrictNames();
                } else if (position == 23) {
                    state = "Mizoram";
                    getDistrictNames();
                } else if (position == 24) {
                    state = "Nagaland";
                    getDistrictNames();
                } else if (position == 25) {
                    state = "Odisha";
                    getDistrictNames();
                } else if (position == 26) {
                    state = "Puducherry";
                    getDistrictNames();
                } else if (position == 27) {
                    state = "Punjab";
                    getDistrictNames();
                } else if (position == 28) {
                    state = "Rajasthan";
                    getDistrictNames();
                } else if (position == 29) {
                    state = "Sikkim";
                    getDistrictNames();
                } else if (position == 30) {
                    state = "TamilNadu";
                    getDistrictNames();
                } else if (position == 31) {
                    state = "Telangana";
                    getDistrictNames();
                } else if (position == 32) {
                    state = "Tripura";
                    getDistrictNames();
                } else if (position == 33) {
                    state = "Uttarakhand";
                    getDistrictNames();
                } else if (position == 34) {
                    state = "UttarPradesh";
                    getDistrictNames();
                } else if (position == 35) {
                    state = "WestBengal";
                    getDistrictNames();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                View v = getCurrentFocus();
                if (v != null) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
                Intent i = new Intent(Hospital.this, MapsActivity.class);
                Double a, b;

                a = Double.parseDouble(bloodBankEntity[position].lati);
                b = Double.parseDouble(bloodBankEntity[position].longi);
                i.putExtra("Latitude1", a);
                i.putExtra("Longitude1", b);
                i.putExtra("Latitude2", currLati);
                i.putExtra("Longitude2", currLongi);
                i.putExtra("BBEobject", bloodBankEntity);
                i.putExtra("Length", len);
                startActivity(i);
            }
        });
    }

    public void getJSONDistrictData() {

        final ProgressDialog loading;
        loading = ProgressDialog.show(Hospital.this, getResources().getString(R.string.downloading), getResources().getString(R.string.Wait), false, false);
        url = "http://www.rahulraj47.esy.es/stateCityData.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    float[] result = new float[2];
                    Double latitude, longitude;
                    len = jsonArray.length();
                    bloodBankEntity = new BloodBankEntity[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        BloodBankEntity entity = new BloodBankEntity();
                        bloodBankEntity[i] = entity;
                        bloodBankEntity[i].h_name = jsonObject.getString("Facility_Name");
                        bloodBankEntity[i].state = jsonObject.getString("State_Name");
                        bloodBankEntity[i].city = jsonObject.getString("District_Name");
                        bloodBankEntity[i].lati = jsonObject.getString("Latitude");
                        bloodBankEntity[i].longi = jsonObject.getString("Longitude");

                        latitude = Double.parseDouble(bloodBankEntity[i].lati);
                        longitude = Double.parseDouble(bloodBankEntity[i].longi);
                        Location.distanceBetween(currLati, currLongi, latitude, longitude, result);

                        result[0] /= 1000;
                        result[0] = Float.parseFloat(new DecimalFormat("####.##").format(result[0]));
                        bloodBankEntity[i].dist = String.valueOf(result[0]);
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                Custom custom = new Custom();
                listView.setVisibility(View.VISIBLE);
                listView.setAdapter(custom);
                loading.dismiss();
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("State", state);
                params.put("City", city);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public void getDistrictNames() {

        url = "http://rahulraj47.esy.es/uniqueDistrict.php";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url, new Response.Listener<String>() {
            @Override
            public void onResponse(String s) {
                try {
                    JSONArray jsonArray = new JSONArray(s);
                    district = new String[jsonArray.length()];
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        district[i] = jsonObject.getString("District_Name");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(Hospital.this, android.R.layout.simple_spinner_dropdown_item, district);
                city_spinner.setAdapter(adapter);
                city_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                    @Override
                    public void onItemSelected(AdapterView<?> parent, View view, int position, long l) {
                        city = (String) parent.getItemAtPosition(position);
                        getJSONDistrictData();
                    }

                    @Override
                    public void onNothingSelected(AdapterView<?> adapterView) {

                    }
                });
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
            }
        }) {
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                Map<String, String> params = new HashMap<String, String>();
                params.put("State", state);
                return params;
            }
        };
        requestQueue.add(stringRequest);
    }

    public class Custom extends BaseAdapter {

        @Override
        public int getCount() {
            return len;
        }

        @Override
        public Object getItem(int position) {
            return position;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            View v = getLayoutInflater().inflate(R.layout.custom_layout, parent, false);
            TextView t1, t2;
            ImageView imageView;

            t1 = (TextView) v.findViewById(R.id.textView);
            t2 = (TextView) v.findViewById(R.id.textView2);
            imageView = (ImageView) v.findViewById(R.id.imageButton);

            t1.setText(bloodBankEntity[position].h_name);
            t2.setText(bloodBankEntity[position].dist);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean check = c1.checkInternet();
                    if (!check) {
                        Intent intent = new Intent(Hospital.this, NoInternet.class);
                        startActivity(intent);
                    } else {
                        Intent intent = new Intent(Hospital.this, HospitalDetails.class);
                        intent.putExtra("Hospital", bloodBankEntity[position].h_name);
                        intent.putExtra("City", bloodBankEntity[position].city);
                        intent.putExtra("State", bloodBankEntity[position].state);
                        Geocoder geocoder = new Geocoder(Hospital.this, Locale.ENGLISH);
                        try {
                            myadd = geocoder.getFromLocation(Double.parseDouble(bloodBankEntity[position].lati), Double.parseDouble(bloodBankEntity[position].longi), 10);
                            Address address = myadd.get(0);
                            bloodBankEntity[position].add=address.getAddressLine(0);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        intent.putExtra("Address", bloodBankEntity[position].add);
                        startActivity(intent);
                    }
                }
            });
            return v;
        }
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        finish();
        return super.onOptionsItemSelected(item);
    }

}
