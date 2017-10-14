package com.example.rahulraj.red_revolution;

import android.content.SharedPreferences;
import android.location.Location;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;

import java.text.DecimalFormat;

public class Offline extends AppCompatActivity {
    Double currLati, currLongi, BBLati, BBLongi;
    String JSON;
    JSONArray jsonArray, jsonArray2;
    BloodBankEntity[] bloodBankEntity, filteredBloodBankEntity;
    ListView listView;
    int j = 0, k = 0, n = 100;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.offline);

        SharedPreferences mPrefs = getSharedPreferences("IDvalue", 0);
        currLati = Double.parseDouble(mPrefs.getString("Latitude", ""));
        currLongi = Double.parseDouble(mPrefs.getString("Longitude", ""));
        JSON = mPrefs.getString("JSON", "");
        listView = (ListView) findViewById(R.id.listView3);
        try {
            jsonArray = new JSONArray(JSON);
            bloodBankEntity = new BloodBankEntity[jsonArray.length() - 1];
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

        } catch (JSONException e) {
            e.printStackTrace();
        }

        filteredBloodBankEntity = new BloodBankEntity[jsonArray.length()];
        for (int i = 0; i < j; i++) {
            BBLati = Double.parseDouble(bloodBankEntity[i].lati);
            BBLongi = Double.parseDouble(bloodBankEntity[i].longi);
            float[] result = new float[2];
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
        }
        MyCustom myCustom = new MyCustom();
        listView.setAdapter(myCustom);
    }

    public class MyCustom extends BaseAdapter {

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
        public View getView(int position, View view, ViewGroup parent) {
            View v = getLayoutInflater().inflate(R.layout.custom_layout2, parent, false);
            TextView t1, t2, t3, t4, t5, t6;
            t1 = (TextView) v.findViewById(R.id.textView92);
            t2 = (TextView) v.findViewById(R.id.textView93);
            t3 = (TextView) v.findViewById(R.id.textView94);
            t4 = (TextView) v.findViewById(R.id.textView95);
            t5 = (TextView) v.findViewById(R.id.textView96);
            t6 = (TextView) v.findViewById(R.id.textView97);

            t1.setText(filteredBloodBankEntity[position].h_name);
            t2.setText(filteredBloodBankEntity[position].add);
            t3.setText(filteredBloodBankEntity[position].city);
            t4.setText(filteredBloodBankEntity[position].state);
            t5.setText(filteredBloodBankEntity[position].contact);
            t6.setText(filteredBloodBankEntity[position].dist);
            return v;
        }
    }
}
