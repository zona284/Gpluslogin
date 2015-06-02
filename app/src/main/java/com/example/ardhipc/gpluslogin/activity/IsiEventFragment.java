package com.example.ardhipc.gpluslogin.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.example.ardhipc.gpluslogin.R;
import com.example.ardhipc.gpluslogin.activity.event.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ardhipc on 5/30/2015.
 */
public class IsiEventFragment extends Fragment {
    // Progress Dialog
    private ProgressDialog pDialog;

    JSONParser jsonParser = new JSONParser();
    EditText nama, cp, deskripsi, kategori, waktu ;
    Button input;

    // url to create new product
    private static String url_create_product = "http://eventhere.belibun.com/inputkebun.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    public IsiEventFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

        // Edit Text
        nama = (EditText) rootView.findViewById(R.id.et_nama);
        cp = (EditText) rootView.findViewById(R.id.et_cp);
        deskripsi = (EditText) rootView.findViewById(R.id.et_deskripsi);
        waktu = (EditText) rootView.findViewById(R.id.et_waktu);
        kategori = (EditText) rootView.findViewById(R.id.et_kategori);

        // Create button
        input = (Button) rootView.findViewById(R.id.input);

        // button click event
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new CreateNewProduct().execute();
            }
        });
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    private class CreateNewProduct extends AsyncTask<String, String, String> {
        /**
         * Before starting background thread Show Progress Dialog
         * */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage("Creating Event..");
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(true);
            pDialog.show();
        }
        /**
         * Creating product
         * */
        protected String doInBackground(String... args) {
            String d_nama = nama.getText().toString();
            String d_cp = cp.getText().toString();
            String d_deskripsi = deskripsi.getText().toString();
            String d_waktu = waktu.getText().toString();
            String d_kategori = kategori.getText().toString();

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nama", d_nama));
            params.add(new BasicNameValuePair("cp", d_cp));
            params.add(new BasicNameValuePair("deskripsi", d_deskripsi));
            params.add(new BasicNameValuePair("waktu", d_waktu));
            params.add(new BasicNameValuePair("kategori", d_kategori));

            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            // check log cat fro response
            Log.d("Create Response", json.toString());

            // check for success tag
            try {
                int success = json.getInt(TAG_SUCCESS);

                if (success == 1) {
                    // successfully created product
                    Intent i = new Intent(getActivity(), EventFragment.class);
                    startActivity(i);
                } else {
                    // failed to create product
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;
        }
    }
}
