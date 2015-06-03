package com.example.ardhipc.gpluslogin.activity.event.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;
import com.example.ardhipc.gpluslogin.R;
import com.example.ardhipc.gpluslogin.activity.event.app.AppController;
import com.example.ardhipc.gpluslogin.activity.event.model.Event;

import java.util.List;

/**
 * Created by Ardhipc on 5/28/2015.
 */
public class CustomListAdapterr extends BaseAdapter {
    private Activity activity;
    private LayoutInflater inflater;
    private List<Event> eventItems;
    ImageLoader imageLoader = AppController.getInstance().getImageLoader();

    public CustomListAdapterr(Activity activity, List<Event> eventItems) {
        this.activity = activity;
        this.eventItems = eventItems;
    }

    @Override
    public int getCount() {
        return eventItems.size();
    }

    @Override
    public Object getItem(int location) {
        return eventItems.get(location);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        if (inflater == null)
            inflater = (LayoutInflater) activity
                    .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        if (convertView == null)
            convertView = inflater.inflate(R.layout.list_row, null);

        if (imageLoader == null)
            imageLoader = AppController.getInstance().getImageLoader();
        NetworkImageView foto = (NetworkImageView) convertView
                .findViewById(R.id.foto);
        TextView nama = (TextView) convertView.findViewById(R.id.nama);
        TextView kategori = (TextView) convertView.findViewById(R.id.kategori);
        TextView cp = (TextView) convertView.findViewById(R.id.cp);
        TextView waktu = (TextView) convertView.findViewById(R.id.waktu);
        TextView deskripsi = (TextView) convertView.findViewById(R.id.deskripsi);
        TextView iid = (TextView) convertView.findViewById(R.id.id);

        // getting movie data for the row
        Event m = eventItems.get(position);

        // thumbnail image
        foto.setImageUrl(m.getFoto(), imageLoader);

        // title
        nama.setText(m.getNama());
        // id
        iid.setText(m.getId());

        // rating
        cp.setText(String.valueOf(m.getCp()));

        // genre
        waktu.setText(m.getWaktu());

        // release year
        kategori.setText(String.valueOf(m.getKategori()));

        // deskripsi
        deskripsi.setText(String.valueOf(m.getDeskripsi()));

        return convertView;
    }
}
