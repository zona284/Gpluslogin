package com.example.ardhipc.gpluslogin.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.ardhipc.gpluslogin.R;
import com.example.ardhipc.gpluslogin.activity.event.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ardhipc on 5/30/2015.
 */
public class IsiEventFragment extends Fragment {
    String res = "";
    private ProgressDialog pDialog;
    InputStream inputStream;
    JSONParser jsonParser = new JSONParser();
    EditText nama, cp, deskripsi, kategori, waktu ;
    Button input;
    //     url to create new product
    private static String url_create_product = "http://eventhere.belibun.com/inputevent.php";

    // JSON Node names
    private static final String TAG_SUCCESS = "success";

    public IsiEventFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_isievent, container, false);
        // Edit Text
        nama = (EditText) rootView.findViewById(R.id.tx_nama);
        cp = (EditText) rootView.findViewById(R.id.tx_cp);
        deskripsi = (EditText) rootView.findViewById(R.id.tx_deskripsi);
        waktu = (EditText) rootView.findViewById(R.id.tx_waktu);
        kategori = (EditText) rootView.findViewById(R.id.tx_kategori);

        // Create button
        input = (Button) rootView.findViewById(R.id.input);

        // button click event
        input.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new IsiData().execute();
            }
        });

        // Inflate the layout for this fragment
        return rootView;
    }
    public class IsiData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(getActivity());
            pDialog.setMessage(Html
                    .fromHtml("<b>Eventhere</b><br/>Saving Data..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

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

            return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Fragment fragment = new EventFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            Toast toast = Toast.makeText(getActivity(), "Data Event Berhasil Masuk",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

    }
    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }
}