package com.example.ardhipc.gpluslogin.activity;

import android.app.Activity;
<<<<<<< HEAD
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
=======
<<<<<<< HEAD
<<<<<<< HEAD
import android.app.ProgressDialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
=======
import android.content.Intent;
>>>>>>> origin/master
=======
import android.content.Intent;
>>>>>>> 424cf4439692d0f53dd97ce2c097fb983e1b54a5
>>>>>>> origin/master
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ardhipc on 5/6/2015.
 */
<<<<<<< HEAD
public class EventFragment extends Fragment {
    // Log tag
    private static final String TAG = MainActivity.class.getSimpleName();
    Context context;
=======
<<<<<<< HEAD
<<<<<<< HEAD
public class EventFragment extends Activity {
    // Log tag
    private static final String TAG = EventFragment.class.getSimpleName();
=======
=======
>>>>>>> 424cf4439692d0f53dd97ce2c097fb983e1b54a5
<<<<<<< HEAD:app/src/main/java/com/example/ardhipc/gpluslogin/activity/HomeFragment.java
public class HomeFragment extends Fragment{
    public HomeFragment(){
    Intent i = new Intent();
        i.getStringExtra("email");
=======
public class EventFragment extends Fragment {
    public EventFragment(){
>>>>>>> upstream/master:app/src/main/java/com/example/ardhipc/gpluslogin/activity/EventFragment.java
<<<<<<< HEAD
>>>>>>> origin/master
=======
>>>>>>> 424cf4439692d0f53dd97ce2c097fb983e1b54a5

>>>>>>> origin/master
    // Movies json url
    private static final String url = "http://eventhere.belibun.com/prosesbaru.php";
    private ProgressDialog pDialog;
    private List<Event> eventList = new ArrayList<Event>();
    private CustomListAdapterr adapter;
    String namauser, emailuser, fotouser;
    public EventFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable final Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_event, container, false);
        final ListView listV = (ListView) rootView.findViewById(R.id.list);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            namauser = bundle.getString("namauser");
            emailuser = bundle.getString("emailuser");
            fotouser = bundle.getString("fotouser");
        }

        context=getActivity();
        adapter = new CustomListAdapterr(getActivity(), eventList);
        listV.setAdapter(adapter);
        listV.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                // when user clicks on ListView Item , onItemClick is called
                // with position and View of the item which is clicked
                // we can use the position parameter to get index of clicked item
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


                // Show The Dialog with Selected Event
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.setTitle("Event : "+nnama);
                dialog.setIcon(android.R.drawable.ic_dialog_info);
                dialog.setMessage("kategori:"+nkategori + "\n Cp:" + ncp + "\n" + nwaktu);
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Booking",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                Fragment fragment = new RincianEventFragment();

                                String title = "Rincian Event...";
                                Bundle bundle = new Bundle();
                                bundle.putString("id", nid);
                                bundle.putString("nama", nnama);
                                bundle.putString("cp", ncp);
                                bundle.putString("waktu", nwaktu);
                                bundle.putString("deskripsi", ndeskripsi);
                                bundle.putString("kategori", nkategori);
                                bundle.putString("emailuser", emailuser);
                                bundle.putString("namauser", namauser);
                                fragment.setArguments(bundle);

                                FragmentManager fragmentManager = getFragmentManager();
                                FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
                                fragmentTransaction.replace(R.id.container_body, fragment);
                                fragmentTransaction.commit();
                            }
                        });
                dialog.setButton(DialogInterface.BUTTON_NEGATIVE, "Back",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        });
                dialog.show();

                Toast.makeText(getActivity(),nnama +" Selected", Toast.LENGTH_SHORT).show();
            }
        });

        pDialog = new ProgressDialog(getActivity());
        // Showing progress dialog before making http request
        pDialog.setTitle("Fetching Event");
        pDialog.setMessage("Loading...");
        pDialog.show();

        // changing action bar color
        //getActionBar().setBackgroundDrawable(new ColorDrawable(Color.parseColor("#000000")));

        // Creating volley request obj
        JsonArrayRequest movieReq = new JsonArrayRequest(url,
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
                                event.setId(obj.getString("id"));
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
