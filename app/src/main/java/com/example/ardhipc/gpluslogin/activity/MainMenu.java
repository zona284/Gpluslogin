package com.example.ardhipc.gpluslogin.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ardhipc.gpluslogin.R;
import com.example.ardhipc.gpluslogin.adapter.FragmentDrawer;

import java.io.InputStream;

/**
 * Created by Ardhipc on 5/5/2015.
 */
public class MainMenu extends ActionBarActivity implements FragmentDrawer.FragmentDrawerListener{
    private static String TAG = MainMenu.class.getSimpleName();

    private Toolbar mToolbar;
    private FragmentDrawer drawerFragment;
    private TextView namauserr;
    private ImageView fotouserr;
    private String email;
    private String photo,nama;




    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mainmenu);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        drawerFragment = (FragmentDrawer)
                getSupportFragmentManager().findFragmentById(R.id.fragment_navigation_drawer);
        drawerFragment.setUp(R.id.fragment_navigation_drawer, (DrawerLayout) findViewById(R.id.drawer_layout),mToolbar);
        drawerFragment.setDrawerListener(this);
        // display the first navigation drawer view on app launch
        Intent i = getIntent();
        nama = i.getStringExtra("nama");
        email = i.getStringExtra("email");
        photo = i.getStringExtra("photo");
        fotouserr = (ImageView) findViewById(R.id.imageView);
        namauserr = (TextView) findViewById(R.id.namauser);
        namauserr.setText(email);
        Log.e(TAG, "email: " + nama);
        new LoadProfileImage(fotouserr).execute(photo);

        displayView(1);
    }
    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        protected Bitmap doInBackground(String... urls) {
            String urldisplay = urls[0];
            Bitmap mIcon11 = null;
            try {
                InputStream in = new java.net.URL(urldisplay).openStream();
                mIcon11 = BitmapFactory.decodeStream(in);
            } catch (Exception e) {
                Log.e("Error", e.getMessage());
                e.printStackTrace();
            }
            return mIcon11;
        }

        protected void onPostExecute(Bitmap result) {
            if(result==null)
                bmImage.setImageResource(R.drawable.ic_profile);
            else
                bmImage.setImageBitmap(result);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
    // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public void onDrawerItemSelected(View view, int position) {
        displayView(position);
    }

    private void displayView(int position) {
        Fragment fragment = null;
        String title = getString(R.string.app_name);
        switch (position) {
            case 0:
                fragment = new ProfileFragment();
                title = getString(R.string.title_profile);
                break;
            case 1:
                fragment = new EventFragment();
                title = getString(R.string.title_event);
                break;
            case 2:
                fragment = new IsiEventFragment();
                title = getString(R.string.title_isievent);
                break;
            case 3:
                fragment = new AllEventFragment();
                title = getString(R.string.title_allevent);
                break;
            default:
                break;
        }
        if (fragment != null){
            FragmentManager fragmentManager = getSupportFragmentManager();

            Bundle bundle = new Bundle();
            bundle.putString("namauser",nama);
            bundle.putString("emailuser",email);
            bundle.putString("fotouser",photo);
            fragment.setArguments(bundle);

            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.container_body, fragment);
            fragmentTransaction.commit();

            // set the toolbar title
            getSupportActionBar().setTitle(title);
        }

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_search){
            Toast.makeText(getApplicationContext(), "Search action is selected!", Toast.LENGTH_SHORT).show();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}
