package com.example.rahulraj.red_revolution;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.io.IOException;
import java.text.DecimalFormat;
import java.util.List;
import java.util.Locale;

public class BloodbankList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    ListView listView;
    GetAllData get;
    BloodBankEntity[] bloodBankEntity, filteredBloodBankEntity;
    Double currLati, currLongi, BBLati, BBLongi, cityLati, cityLongi;
    JSONArray jsonArray, jsonArray2;
    int length, n = 2;
    Spinner spinner;
    Button search;
    EditText city2;
    String str[] = {"2", "3", "5", "10", "15", "20"};
    String JSON;
    int k = 0, j = 0;
    boolean flag = true;
    StringBuilder sb = new StringBuilder();
    boolean found = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bloodbank_list);

        SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
        currLati = Double.parseDouble(mPrefs.getString("Latitude", ""));
        currLongi = Double.parseDouble(mPrefs.getString("Longitude", ""));
        JSON = mPrefs.getString("JSON", "");

        Geocoder geocoder = new Geocoder(BloodbankList.this, Locale.ENGLISH);
        try {
            List<Address> myadd = geocoder.getFromLocation(currLati, currLongi, 10);
            Address address = myadd.get(0);

            for (int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                sb.append(address.getAddressLine(i));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }

        getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_HIDDEN); //HIDE DEFAULT KEYBOARD

        search = (Button) findViewById(R.id.button11);
        city2 = (EditText) findViewById(R.id.editText11);
        listView = (ListView) findViewById(R.id.listView);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent i = new Intent(BloodbankList.this, MapsActivity.class);
                Double a, b;

                a = Double.parseDouble(filteredBloodBankEntity[position].lati);
                b = Double.parseDouble(filteredBloodBankEntity[position].longi);
                i.putExtra("Latitude1", a);
                i.putExtra("Longitude1", b);
                i.putExtra("Latitude2", currLati);
                i.putExtra("Longitude2", currLongi);
                i.putExtra("BBEobject", bloodBankEntity);
                i.putExtra("Length", j);
                startActivity(i);
            }
        });

        spinner = (Spinner) findViewById(R.id.spinner);
        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, str);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);

        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                flag = false;
                found=false;
                j = 0;
                k = 0;
                getJSON();
                spinner.setVisibility(View.GONE);
                findViewById(R.id.textView8).setVisibility(View.GONE);
            }
        });

        ((Button) findViewById(R.id.contactDonor)).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
                final String str = mPrefs.getString("k", "");

                new AlertDialog.Builder(BloodbankList.this).setTitle(getResources().getString(R.string.SendSMS))
                        .setMessage(getResources().getString(R.string.SendEmergency))
                        .setPositiveButton(getResources().getString(R.string.YES), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                if (str == null || str.length() < 10) {
                                    AlertDialog alertDialog = new AlertDialog.Builder(BloodbankList.this).create();
                                    final EditText input = new EditText(BloodbankList.this);
                                    input.setHint(getResources().getString(R.string.MobileNumber));
                                    LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(
                                            LinearLayout.LayoutParams.MATCH_PARENT,
                                            LinearLayout.LayoutParams.MATCH_PARENT);
                                    input.setLayoutParams(lp);
                                    alertDialog.setView(input);
                                    alertDialog.setButton(AlertDialog.BUTTON_POSITIVE, getResources().getString(R.string.OK), new DialogInterface.OnClickListener() {
                                        public void onClick(DialogInterface dialog, int whichButton) {
                                            String number = input.getText().toString();
                                            SmsManager smsManager = SmsManager.getDefault();
                                            smsManager.sendTextMessage(number, null, "HELP!! I'm at address : " + sb, null, null);
                                        }
                                    });
                                    alertDialog.show();


                                } else {
                                    SmsManager smsManager = SmsManager.getDefault();
                                    smsManager.sendTextMessage(str, null, "HELP!!" + sb, null, null);
                                }
                            }
                        })
                        .setTitle(getResources().getString(R.string.SendSMS))
                        .setMessage(getResources().getString(R.string.SendEmergency))
                        .setNegativeButton(getResources().getString(R.string.NO), new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                dialogInterface.dismiss();
                            }
                        }).create().show();
            }
        });

    }

    private void getJSON() {
        get = new GetAllData(JSON);
        setData();
    }

    @Override
    public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
        k = 0;
        j = 0;
        n = Integer.parseInt((String) adapterView.getItemAtPosition(i));
        getJSON();
    }

    @Override
    public void onNothingSelected(AdapterView<?> adapterView) {
        getJSON();
    }

    public void setData() {
        class GetImage extends AsyncTask<Void, Void, Void> {

            ProgressDialog loading;

            @Override
            protected Void doInBackground(Void... params) {
                try {
                    get.getAllData();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                try {
                    loading = ProgressDialog.show(BloodbankList.this, getResources().getString(R.string.downloading), getResources().getString(R.string.Wait), false, false);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            protected void onPostExecute(Void v) {
                super.onPostExecute(v);
                try {
                    calculate();
                    Custom custom = new Custom();
                    listView.setAdapter(custom);
                    loading.dismiss();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        GetImage getImage = new GetImage();
        getImage.execute();
    }

    private void calculate() {
        float[] result = new float[2];

        filteredBloodBankEntity = new BloodBankEntity[jsonArray.length()];

        for (int i = 0; i < j; i++) {
            if (flag) {
                BBLati = Double.parseDouble(bloodBankEntity[i].lati);
                BBLongi = Double.parseDouble(bloodBankEntity[i].longi);
                Location.distanceBetween(currLati, currLongi, BBLati, BBLongi, result);

                result[0] /= 1000;
                result[0] = Float.parseFloat(new DecimalFormat("####.##").format(result[0]));

                if (result[0] <= n) {
                    BloodBankEntity entity = new BloodBankEntity();
                    filteredBloodBankEntity[k] = entity;
                    filteredBloodBankEntity[k].dist = Float.toString(result[0]);
                    filteredBloodBankEntity[k].lati = bloodBankEntity[i].lati;
                    filteredBloodBankEntity[k].longi = bloodBankEntity[i].longi;
                    filteredBloodBankEntity[k].h_name = bloodBankEntity[i].h_name;
                    filteredBloodBankEntity[k].state = bloodBankEntity[i].state;
                    filteredBloodBankEntity[k].add = bloodBankEntity[i].add;
                    filteredBloodBankEntity[k].pincode = bloodBankEntity[i].pincode;
                    filteredBloodBankEntity[k].contact = bloodBankEntity[i].contact;
                    filteredBloodBankEntity[k].city = bloodBankEntity[i].city;
                    k++;
                }
            } else {
                BBLati = Double.parseDouble(bloodBankEntity[i].lati);
                BBLongi = Double.parseDouble(bloodBankEntity[i].longi);
                Location.distanceBetween(currLati, currLongi, BBLati, BBLongi, result);

                result[0] /= 1000;
                result[0] = Float.parseFloat(new DecimalFormat("####.##").format(result[0]));

                if (city2.getText().toString().trim().equals(bloodBankEntity[i].city)) {
                    BloodBankEntity entity = new BloodBankEntity();
                    filteredBloodBankEntity[k] = entity;
                    filteredBloodBankEntity[k].dist = Float.toString(result[0]);
                    filteredBloodBankEntity[k].lati = bloodBankEntity[i].lati;
                    filteredBloodBankEntity[k].longi = bloodBankEntity[i].longi;
                    filteredBloodBankEntity[k].h_name = bloodBankEntity[i].h_name;
                    filteredBloodBankEntity[k].state = bloodBankEntity[i].state;
                    filteredBloodBankEntity[k].add = bloodBankEntity[i].add;
                    filteredBloodBankEntity[k].pincode = bloodBankEntity[i].pincode;
                    filteredBloodBankEntity[k].contact = bloodBankEntity[i].contact;
                    filteredBloodBankEntity[k].city = bloodBankEntity[i].city;
                    found = true;
                    k++;
                }
            }
        }
        if (!found) {
            Toast.makeText(this, "List of blood bank for city unavailable", Toast.LENGTH_LONG).show();
        }
    }

    public class GetAllData {
        public GetAllData(String s) {
            try {
                // json output in string
                jsonArray = new JSONArray(s);
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        public void getAllData() {
            bloodBankEntity = new BloodBankEntity[jsonArray.length() - 1];
            length = jsonArray.length();
            for (int i = 1; i < jsonArray.length(); i++) {
                try {
                    jsonArray2 = jsonArray.getJSONArray(i);
                    if (!"0".equals(jsonArray2.getString(25)) && !"0".equals(jsonArray2.getString(26))) {
                        BloodBankEntity entity = new BloodBankEntity();
                        bloodBankEntity[j] = entity;
                        bloodBankEntity[j].h_name = jsonArray2.getString(1);
                        bloodBankEntity[j].state = jsonArray2.getString(2);
                        bloodBankEntity[j].city = jsonArray2.getString(4);
                        bloodBankEntity[j].add = jsonArray2.getString(5);
                        bloodBankEntity[j].pincode = jsonArray2.getString(6);
                        bloodBankEntity[j].contact = jsonArray2.getString(7);
                        bloodBankEntity[j].lati = jsonArray2.getString(25);
                        bloodBankEntity[j].longi = jsonArray2.getString(26);
                        j++;
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public class Custom extends BaseAdapter {

        @Override
        public int getCount() {
            return k;
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

            t1.setText(filteredBloodBankEntity[position].h_name);
            t2.setText(filteredBloodBankEntity[position].dist);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(BloodbankList.this, BloodBankDetails.class);
                    intent.putExtra("Hospital", filteredBloodBankEntity[position].h_name);
                    intent.putExtra("Address", filteredBloodBankEntity[position].add);
                    intent.putExtra("City", filteredBloodBankEntity[position].city);
                    intent.putExtra("State", filteredBloodBankEntity[position].state);
                    intent.putExtra("Pincode", filteredBloodBankEntity[position].pincode);
                    intent.putExtra("Contact", filteredBloodBankEntity[position].contact);
                    startActivity(intent);
                }
            });
            return v;
        }
    }
}
