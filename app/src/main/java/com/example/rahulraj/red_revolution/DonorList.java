package com.example.rahulraj.red_revolution;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsManager;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DonorList extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    EditText searchCity;
    Button Search;
    Spinner spinner;
    ListView listView;
    JSONArray jsonArray;
    JSONObject jsonObject;
    String[] name, bloodGroup, phone, str = {"A+", "A-", "AB+", "AB-", "B+", "B-", "O+", "O-"};
    int j, k;
    boolean flag = false;
    String JSON,BG;
    CheckInternet c1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        c1 = new CheckInternet(this);
        boolean check = c1.checkInternet();
        if (!check) {
            Intent intent = new Intent(DonorList.this, NoInternet.class);
            startActivity(intent);
        }
        setContentView(R.layout.donor_list);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        Bundle bundle = getIntent().getExtras();
        JSON = bundle.getString("DonorJSON", "");

        searchCity = (EditText) findViewById(R.id.editText11);
        Search = (Button) findViewById(R.id.button11);
        spinner = (Spinner) findViewById(R.id.spinner);
        listView = (ListView) findViewById(R.id.listView);

        Search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                boolean check=c1.checkInternet();
                if (!check) {
                    Intent intent = new Intent(DonorList.this, NoInternet.class);
                    startActivity(intent);
                } else {
                    flag = false;
                    j = 0;
                    String city1 = searchCity.getText().toString().trim();
                    if (!TextUtils.isEmpty(city1)) {
                        SetDataCity setDataCity = new SetDataCity();
                        setDataCity.execute(city1);
                    } else {
                        Toast.makeText(DonorList.this, "Please enter a city", Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_dropdown_item, str);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        k = 0;
        flag = true;
        BG = (String) parent.getItemAtPosition(position);
        SetDataBloodGroup setDataBloodGroup = new SetDataBloodGroup();
        setDataBloodGroup.execute(BG);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    class SetDataCity extends AsyncTask<String, Void, Void> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(DonorList.this, getResources().getString(R.string.downloading), getResources().getString(R.string.Wait), false, false);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                getDataCity(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyCustom myCustom = new MyCustom();
            listView.setAdapter(myCustom);
            loading.dismiss();
        }
    }

    class SetDataBloodGroup extends AsyncTask<String, Void, Void> {
        ProgressDialog loading;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            loading = ProgressDialog.show(DonorList.this, getResources().getString(R.string.downloading), getResources().getString(R.string.Wait), false, false);
        }

        @Override
        protected Void doInBackground(String... params) {
            try {
                getDataBloodGroup(params[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            MyCustom myCustom = new MyCustom();
            listView.setAdapter(myCustom);
            loading.dismiss();
        }
    }

    void getDataCity(String City) throws JSONException {
        jsonArray = new JSONArray(JSON);
        name = new String[jsonArray.length()];
        bloodGroup = new String[jsonArray.length()];
        phone = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); ++i) {
            jsonObject = jsonArray.getJSONObject(i);
            if ("yes".equalsIgnoreCase(jsonObject.getString("Willness_of_donation")) && !TextUtils.isEmpty(jsonObject.getString("Phone")) && City.equalsIgnoreCase(jsonObject.getString("City"))) {
                name[j] = jsonObject.getString("Name");
                bloodGroup[j] = jsonObject.getString("Blood_group");
                phone[j] = jsonObject.getString("Phone");
                j++;
            }
        }
    }

    void getDataBloodGroup(String BloodGroup) throws JSONException {
        jsonArray = new JSONArray(JSON);
        name = new String[jsonArray.length()];
        bloodGroup = new String[jsonArray.length()];
        phone = new String[jsonArray.length()];
        for (int i = 0; i < jsonArray.length(); ++i) {
            jsonObject = jsonArray.getJSONObject(i);
            if ("yes".equalsIgnoreCase(jsonObject.getString("Willness_of_donation")) && !TextUtils.isEmpty(jsonObject.getString("Phone")) && BloodGroup.equalsIgnoreCase(jsonObject.getString("Blood_group"))) {
                name[k] = jsonObject.getString("Name");
                bloodGroup[k] = jsonObject.getString("Blood_group");
                phone[k] = jsonObject.getString("Phone");
                k++;
            }
        }
    }

    public class MyCustom extends BaseAdapter {

        @Override
        public int getCount() {
            if (!flag)
                return j;
            else
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
        public View getView(final int position, View converVview, ViewGroup parent) {
            View v = getLayoutInflater().inflate(R.layout.custom_layout3, parent, false);
            TextView t1, t2;
            ImageView imageView;

            t1 = (TextView) v.findViewById(R.id.textView6);
            t2 = (TextView) v.findViewById(R.id.textView9);
            imageView = (ImageView) v.findViewById(R.id.imageView4);
            t1.setText(name[position]);
            t2.setText(bloodGroup[position]);
            imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    boolean check=c1.checkInternet();
                    if (!check) {
                        Intent intent = new Intent(DonorList.this, NoInternet.class);
                        startActivity(intent);
                    } else {
                        new AlertDialog.Builder(DonorList.this).
                                setTitle(getResources().getString(R.string.SendSMS))
                                .setMessage(getResources().getString(R.string.contact_donor))
                                .setPositiveButton(R.string.YES, new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        SmsManager smsManager = SmsManager.getDefault();
                                        smsManager.sendTextMessage(phone[position], null, getResources().getString(R.string.Message), null, null);
                                        Toast.makeText(DonorList.this, R.string.sms_sent_donor, Toast.LENGTH_LONG).show();
                                    }
                                }).setNegativeButton(R.string.NO, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                            }
                        }).show();
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
