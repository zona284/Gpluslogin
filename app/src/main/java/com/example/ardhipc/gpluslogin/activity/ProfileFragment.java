package com.example.ardhipc.gpluslogin.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.VolleyLog;
import com.android.volley.toolbox.JsonArrayRequest;
import com.example.ardhipc.gpluslogin.R;
import com.example.ardhipc.gpluslogin.activity.event.adapter.CustomListAdapterr;
import com.example.ardhipc.gpluslogin.activity.event.app.AppController;
import com.example.ardhipc.gpluslogin.activity.event.model.Event;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ardhipc on 5/6/2015.
 */
public class ProfileFragment extends Fragment{
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    Context context;
    // Movies json url
    private static final String url = "http://eventhere.belibun.com/getorder.php";
    private ProgressDialog pDialog;
    private List<Event> eventList = new ArrayList<Event>();
    private CustomListAdapterr adapter;
    String namauser, emailuser, fotouser;
    String nid;
    ListView listv;
    public ProfileFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);
        listv = (ListView) rootView.findViewById(R.id.list);

        Bundle bundle = this.getArguments();
        if (bundle != null) {

            namauser = bundle.getString("namauser");
            emailuser = bundle.getString("emailuser");
            fotouser = bundle.getString("fotouser");
        }

        context=getActivity();
        adapter = new CustomListAdapterr(getActivity(), eventList);
        listv.setAdapter(adapter);
        listv.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {
            @Override
            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {

                TextView nama=(TextView)view.findViewById(R.id.nama);
                TextView kategori=(TextView)view.findViewById(R.id.kategori);
                TextView cp=(TextView)view.findViewById(R.id.cp);
                TextView waktu=(TextView)view.findViewById(R.id.waktu);
                TextView deskripsi=(TextView)view.findViewById(R.id.deskripsi);
                TextView idd=(TextView)view.findViewById(R.id.id);
                final String nnama=nama.getText().toString();
                final String nkategori=kategori.getText().toString();
                final String ncp=cp.getText().toString();
                final String nwaktu=waktu.getText().toString();
                final String ndeskripsi=deskripsi.getText().toString();
                final String nid=idd.getText().toString();

                Intent sendIntent = new Intent();
                sendIntent.setAction(Intent.ACTION_SEND);

                String pesan = "Hei, Saya sudah memakai aplikasi EventHere! Lho!\n"+"Saya akan mengikuti Event: "
                        +nnama+" pada waktu: "+nwaktu;
                sendIntent.putExtra(Intent.EXTRA_TEXT, pesan);
                sendIntent.setType("text/plain");
                startActivity(Intent.createChooser(sendIntent, "Bagikan Ke..."));
                return false;
            }
        });

    try {
        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setTitle("Fetching Your Timeline");
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
        //getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url + "?id=" + emailuser,
                new Response.Listener<JSONArray>() {
                    @Override
                    public void onResponse(JSONArray response) {
                        Log.d(TAG, response.toString());
                        hidePDialog();

                        // Parsing json
                        for (int i = 0; i < response.length(); i++) {
                            try {

                                JSONObject obj = response.getJSONObject(i);
                                Event event = new Event();
                                event.setNama(obj.getString("nama"));
                                event.setDeskripsi(obj.getString("deskripsi"));
                                event.setFoto(obj.getString("foto"));
                                event.setCp(obj.getString("cp"));
                                event.setKategori(obj.getString("kategori"));
                                event.setWaktu(obj.getString("waktu"));
                                event.setId(obj.getString("idevent"));
                                event.setVerivikasi(obj.getString("verifikasi"));

                                // adding movie to movies array
                                eventList.add(event);

                            } catch (JSONException e) {
                                e.printStackTrace();
                            }

                        }

                        // notifying list adapter about data changes
                        // so that it renders the list view with updated data
                        adapter.notifyDataSetChanged();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                VolleyLog.d(TAG, "Error: " + error.getMessage());
                hidePDialog();

            }
        });

        // Adding request to request queue
        AppController.getInstance().addToRequestQueue(movieReq);
    }
    catch (Exception e){

    }
        // Inflate the layout for this fragment
        return rootView;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        hidePDialog();
    }

    private void hidePDialog() {
        if (pDialog != null) {
            pDialog.dismiss();
            pDialog = null;
        }
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_main, menu);
        super.onCreateOptionsMenu(menu, inflater);
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
