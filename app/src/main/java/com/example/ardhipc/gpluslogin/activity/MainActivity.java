package com.example.ardhipc.gpluslogin.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.ardhipc.gpluslogin.R;
import com.example.ardhipc.gpluslogin.activity.event.JSONParser;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import org.apache.http.NameValuePair;
import org.apache.http.message.BasicNameValuePair;
import org.json.JSONObject;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    JSONParser jsonParser = new JSONParser();
    private static String url_create_product = "http://eventhere.belibun.com/login.php";
    private String fotouser;
    private String emailuser;
    private String namauser;
    private String gplususer;
    private ProgressDialog pDialog;

    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

    String personPhotoUrl;
    String email;
    // Google client to interact with Google API
    private GoogleApiClient mGoogleApiClient;

    /**
     * A flag indicating that a PendingIntent is in progress and prevents us
     * from starting further intents.
     */
    private boolean mIntentInProgress;

    private boolean mSignInClicked;

    private ConnectionResult mConnectionResult;

    private SignInButton btnSignIn;
    private Button btnSignOut, btnEnterApps;
    private ImageView imgProfilePic;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout;
    private RelativeLayout rel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnEnterApps = (Button) findViewById(R.id.btn_enter_apps);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        rel = (RelativeLayout) findViewById(R.id.relativlayout);

        // Button click listeners
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnEnterApps.setOnClickListener(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this).addApi(Plus.API)
                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
    }

    protected void onStart() {
        super.onStart();
        mGoogleApiClient.connect();
    }

    protected void onStop() {
        super.onStop();
        if (mGoogleApiClient.isConnected()) {
            mGoogleApiClient.disconnect();
        }
    }

    /**
     * Method to resolve any signin errors
     * */
    private void resolveSignInError() {
        if (mConnectionResult.hasResolution()) {
            try {
                mIntentInProgress = true;
                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
            } catch (IntentSender.SendIntentException e) {
                mIntentInProgress = false;
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnectionFailed(ConnectionResult result) {
        if (!result.hasResolution()) {
            GooglePlayServicesUtil.getErrorDialog(result.getErrorCode(), this,
                    0).show();
            return;
        }

        if (!mIntentInProgress) {
            // Store the ConnectionResult for later usage
            mConnectionResult = result;

            if (mSignInClicked) {
                // The user has already clicked 'sign-in' so we attempt to
                // resolve all
                // errors until the user is signed in, or they cancel.
                resolveSignInError();
            }
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int responseCode,Intent intent) {
        if (requestCode == RC_SIGN_IN) {
            if (responseCode != RESULT_OK) {
                mSignInClicked = false;
            }

            mIntentInProgress = false;

            if (!mGoogleApiClient.isConnecting()) {
                mGoogleApiClient.connect();
            }
        }
    }

    @Override
    public void onConnected(Bundle arg0) {
        mSignInClicked = false;
        Toast.makeText(this, "User is connected!", Toast.LENGTH_LONG).show();

        // Get user's information
        getProfileInformation();

        // Update the UI after signin
        updateUI(true);

    }

    /**
     * Updating the UI, showing/hiding buttons and profile layout
     * */
    private void updateUI(boolean isSignedIn) {
        if (isSignedIn) {
            btnSignIn.setVisibility(View.GONE);
            btnSignOut.setVisibility(View.VISIBLE);
            btnEnterApps.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
            rel.setVisibility(View.GONE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnEnterApps.setVisibility(View.GONE);
            llProfileLayout.setVisibility(View.GONE);
            rel.setVisibility(View.VISIBLE);
        }
    }

    /**
     * Fetching user's information name, email, profile pic
     * */
    private void getProfileInformation() {
        try {
            if (Plus.PeopleApi.getCurrentPerson(mGoogleApiClient) != null) {
                Person currentPerson = Plus.PeopleApi
                        .getCurrentPerson(mGoogleApiClient);
                String personName = currentPerson.getDisplayName();
                personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                email = Plus.AccountApi.getAccountName(mGoogleApiClient);

                namauser = currentPerson.getDisplayName();
                fotouser = currentPerson.getImage().getUrl();
                gplususer = currentPerson.getUrl();
                emailuser = Plus.AccountApi.getAccountName(mGoogleApiClient);

                Log.e(TAG, "Name: " + personName + ", plusProfile: "
                        + personGooglePlusProfile + ", email: " + email
                        + ", Image: " + personPhotoUrl);

                txtName.setText(personName);
                txtEmail.setText(email);

                // by default the profile url gives 50x50 px image only
                // we can replace the value with whatever dimension we want by
                // replacing sz=X
                personPhotoUrl = personPhotoUrl.substring(0,
                        personPhotoUrl.length() - 2)
                        + PROFILE_PIC_SIZE;

                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);

            } else {
                Toast.makeText(getApplicationContext(),
                        "Person information is null", Toast.LENGTH_LONG).show();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onConnectionSuspended(int arg0) {
        mGoogleApiClient.connect();
        updateUI(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    /**
     * Button on click listener
     * */
    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.btn_sign_in:
                // Signin button clicked
                signInWithGplus();
                break;
            case R.id.btn_sign_out:
                // Signout button clicked
                signOutFromGplus();
                break;
            case R.id.btn_enter_apps:
                // Revoke access button clicked
                enterapps();
                break;
        }
    }

    /**
     * Sign-in into google
     * */
    private void signInWithGplus() {
        if (!mGoogleApiClient.isConnecting()) {
            mSignInClicked = true;
            resolveSignInError();
        }
    }

    /**
     * Sign-out from google
     * */
    private void signOutFromGplus() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            mGoogleApiClient.disconnect();
           // mGoogleApiClient.connect();
            updateUI(false);
        }
    }

    /**
     * Revoking access from google
     * */
    private void enterapps() {
<<<<<<< HEAD
        new IsiData().execute();
    }
    public class IsiData extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(Html
                    .fromHtml("<b>Login</b><br/>Please wait..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }

        protected String doInBackground(String... args) {

            // Building Parameters
            List<NameValuePair> params = new ArrayList<NameValuePair>();
            params.add(new BasicNameValuePair("nama", namauser));
            params.add(new BasicNameValuePair("emailuser", emailuser));
            params.add(new BasicNameValuePair("foto", fotouser));
            // getting JSON Object
            // Note that create product url accepts POST method
            JSONObject json = jsonParser.makeHttpRequest(url_create_product,
                    "POST", params);

            return null;
        }
        protected void onPostExecute(String file_url) {
            Intent d = new Intent(MainActivity.this,MainMenu.class);
            d.putExtra("photo",fotouser);
            d.putExtra("email",emailuser);
            d.putExtra("nama",namauser);
            d.putExtra("profile", gplususer);
            startActivity(d);
            Toast toast = Toast.makeText(MainActivity.this, "Login Berhasil",
                    Toast.LENGTH_SHORT);
            toast.show();
        }

=======
        Intent d = new Intent(MainActivity.this,MainMenu.class);
        d.putExtra("photo",personPhotoUrl);
        d.putExtra("email",email);
        startActivity(d);
//        if (mGoogleApiClient.isConnected()) {
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
//                    .setResultCallback(new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(Status arg0) {
//                            Log.e(TAG, "User access revoked!");
//                            mGoogleApiClient.connect();
//                            updateUI(false);
//                        }
//
//                    });
//        }
>>>>>>> origin/master
    }

    /**
     * Background Async task to load user profile picture from url
     * */
    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap> {
        ImageView bmImage;

        public LoadProfileImage(ImageView bmImage) {
            this.bmImage = bmImage;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(MainActivity.this);
            pDialog.setMessage(Html
                    .fromHtml("<b>EventHere</b><br/>Get Your Profile..."));
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
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
            bmImage.setImageBitmap(result);
            pDialog.dismiss();
        }
    }

}
