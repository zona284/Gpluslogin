package com.example.ardhipc.gpluslogin.activity;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
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
import android.widget.TextView;
import android.widget.Toast;

import com.example.ardhipc.gpluslogin.R;
import com.example.ardhipc.gpluslogin.activity.event.JSONParser;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Ardhipc on 5/28/2015.
 */
public class RincianEventFragment extends Fragment {
    String fid;
    String  fnama="Ardhi";
    String  fcp="085959236369";
    String  fwaktu="ccdscsd";
    String  fdeskripsi="sdfsdfsdfsdfsdfsdfsd";
    String  fkategori="pendidikam", emailuser, namauser;
    Button booking;
    private ProgressDialog pDialog;
    JSONParser jsonParser = new JSONParser();
    private static String url_create_product = "http://eventhere.belibun.com/order.php";
    public RincianEventFragment(){

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_rincianevent, container, false);

        Bundle bundle = this.getArguments();
        if (bundle != null) {
            fid = bundle.getString("id");
            fnama = bundle.getString("nama");
            fcp = bundle.getString("cp");
            fwaktu = bundle.getString("waktu");
            fdeskripsi = bundle.getString("deskripsi");
            fkategori = bundle.getString("kategori");
            namauser = bundle.getString("namauser");
            emailuser = bundle.getString("emailuser");
        }

        TextView id, tnama, tkategori, tcp, tdeskripsi, twaktu;

        tnama = (TextView) rootView.findViewById(R.id.tx_nama);
        tkategori = (TextView) rootView.findViewById(R.id.tx_kategori);
        tcp = (TextView) rootView.findViewById(R.id.tx_cp);
        twaktu = (TextView) rootView.findViewById(R.id.tx_waktu);
        tdeskripsi = (TextView) rootView.findViewById(R.id.tx_deskripsi);
        booking = (Button) rootView.findViewById(R.id.booking);


//        tnama.setText(fid);
        tnama.setText(fnama);
        tkategori.setText(fkategori);
        tcp.setText(fcp);
        twaktu.setText(fwaktu);
        tdeskripsi.setText(fdeskripsi);

        booking.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Show The Dialog with Selected Event
                AlertDialog dialog = new AlertDialog.Builder(getActivity()).create();
                dialog.setTitle("Booking Acara...");
                dialog.setIcon(android.R.drawable.ic_input_add);
                dialog.setMessage("Cara melakukan konfirmasi:\n" +
                        "1. Lakukan Pembaran menggunakan ATM ke rekening a/n Ardhi Ma'arik nomor rekening 546645116876\n" +
                        "2. Lakukan konvirmasi pembayaran ke nomor 085959236369 \n" +
                        "\n" +
                        "Konfirmasi dilakukan paling lambat 1x24 jam setelah melakukan pembookingan tempat\n" +
                        "\n" +
                        "terimakasih,\n \n" +
                        "(tekan 'booking' untuk memesan tempat)");
                dialog.setButton(DialogInterface.BUTTON_POSITIVE, "Booking",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                new IsiData().execute();
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
            }
        });
//        Configuration configuration = getActivity().getResources().getConfiguration();
//        int ori = configuration.orientation;

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
            String d_id = fid;
            String d_emailuser = emailuser;

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("id", d_id));
            params.add(new BasicNameValuePair("emailuser", d_emailuser));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            return null;
        }
        protected void onPostExecute(String file_url) {
            // dismiss the dialog once done
            pDialog.dismiss();
            Fragment fragment = new ProfileFragment();
            FragmentManager fragmentManager = getFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();
            Toast toast = Toast.makeText(getActivity(), "Tempat telah dibooking",
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
