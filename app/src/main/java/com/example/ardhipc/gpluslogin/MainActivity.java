package com.example.ardhipc.gpluslogin;

import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.plus.Plus;
import com.google.android.gms.plus.model.people.Person;

import java.io.InputStream;

public class MainActivity extends Activity implements View.OnClickListener,
        GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static final int RC_SIGN_IN = 0;
    // Logcat tag
    private static final String TAG = "MainActivity";

    // Profile pic image size in pixels
    private static final int PROFILE_PIC_SIZE = 400;

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
    private Button btnSignOut, btnRevokeAccess;
    private ImageView imgProfilePic;
//    private ImageView logo;
    private TextView txtName, txtEmail;
    private LinearLayout llProfileLayout;
    private RelativeLayout rel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
        btnRevokeAccess = (Button) findViewById(R.id.btn_revoke_access);
        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
//        logo = (ImageView) findViewById(R.id.logo);
        txtName = (TextView) findViewById(R.id.txtName);
        txtEmail = (TextView) findViewById(R.id.txtEmail);
        llProfileLayout = (LinearLayout) findViewById(R.id.llProfile);
        rel = (RelativeLayout) findViewById(R.id.relativlayout);

        // Button click listeners
        btnSignIn.setOnClickListener(this);
        btnSignOut.setOnClickListener(this);
        btnRevokeAccess.setOnClickListener(this);

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
            btnRevokeAccess.setVisibility(View.VISIBLE);
            llProfileLayout.setVisibility(View.VISIBLE);
            rel.setVisibility(View.GONE);
        } else {
            btnSignIn.setVisibility(View.VISIBLE);
            btnSignOut.setVisibility(View.GONE);
            btnRevokeAccess.setVisibility(View.GONE);
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
                String personPhotoUrl = currentPerson.getImage().getUrl();
                String personGooglePlusProfile = currentPerson.getUrl();
                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);

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
            case R.id.btn_revoke_access:
                // Revoke access button clicked
                revokeGplusAccess();
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
    private void revokeGplusAccess() {
        if (mGoogleApiClient.isConnected()) {
            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
                    .setResultCallback(new ResultCallback<Status>() {
                        @Override
                        public void onResult(Status arg0) {
                            Log.e(TAG, "User access revoked!");
                            mGoogleApiClient.connect();
                            updateUI(false);
                        }

                    });
        }
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
            bmImage.setImageBitmap(result);
        }
    }

}


//public class MainActivity extends Activity implements View.OnClickListener,GoogleApiClient.ConnectionCallbacks,GoogleApiClient.OnConnectionFailedListener {
//    private static final int RC_SIGN_IN =0;
//    private static final String TAG ="MainActivity";
//    private static final int PROFILE_PIC_SIZE = 400;
//    private GoogleApiClient mGoogleApiClient;
//
//    private boolean mIntentInProgress;
//    private boolean mSignInClicked;
//    private ConnectionResult mConnectionResult;
//
//    private SignInButton btnSignIn;
//    private Button btnSignOut, btnRevokeAccess;
//    private ImageView imgProfilePic;
//    private TextView txtName, txtEmail;
//    private LinearLayout llProfileLayout;
//
//
//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_main);
//
//        btnSignIn = (SignInButton) findViewById(R.id.btn_sign_in);
//        btnSignOut = (Button) findViewById(R.id.btn_sign_out);
//        btnRevokeAccess = (Button)findViewById(R.id.btn_revoke_access);
//        imgProfilePic = (ImageView) findViewById(R.id.imgProfilePic);
//        txtName = (TextView) findViewById(R.id.txtName);
//        txtEmail = (TextView) findViewById(R.id.txtEmail);
//        llProfileLayout =(LinearLayout) findViewById(R.id.llProfile);
//
//        btnSignIn.setOnClickListener(this);
//        btnSignOut.setOnClickListener(this);
//        btnRevokeAccess.setOnClickListener(this);
//
//        mGoogleApiClient = new GoogleApiClient.Builder(this)
//                .addConnectionCallbacks(this)
//                .addOnConnectionFailedListener(this)
//                .addApi(Plus.API)
//                .addScope(Plus.SCOPE_PLUS_LOGIN).build();
//    }
//
//    protected void onStart(){
//        super.onStart();
//        mGoogleApiClient.connect();
//    }
//
//    protected void onStop(){
//        super.onStop();
//        if (mGoogleApiClient.isConnected()){
//            mGoogleApiClient.disconnect();
//        }
//    }
//
//    @Override
//    public void onClick(View v) {
//        switch (v.getId()){
//            case R.id.btn_sign_in: signInWithGplus(); break;
//            case R.id.btn_sign_out: signOutWithGplus(); break;
//            case R.id.btn_revoke_access: revokeGplusAccess(); break;
//        }
////        if (v.getId() == R.id.btn_sign_in && !mGoogleApiClient.isConnecting()) {
////            mSignInClicked = true;
////            mGoogleApiClient.connect();
////        }
//    }
//
//    private void revokeGplusAccess() {
//        if(mGoogleApiClient.isConnected()){
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            Plus.AccountApi.revokeAccessAndDisconnect(mGoogleApiClient)
//                    .setResultCallback(new ResultCallback<Status>() {
//                        @Override
//                        public void onResult(Status status) {
//                            Log.e(TAG,"USER access revoke!");
//                            mGoogleApiClient.connect();
//                            updateUI(false);
//                        }
//                    });
//        }
//    }
//
//    private void signOutWithGplus() {
//        if (mGoogleApiClient.isConnected()) {
//            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            mGoogleApiClient.disconnect();
//            mGoogleApiClient.connect();
//            updateUI(false);
//        }
//    }
//
//    private void signInWithGplus() {
//        if(!mGoogleApiClient.isConnecting()){
////            Plus.AccountApi.clearDefaultAccount(mGoogleApiClient);
//            mSignInClicked=true;
//            resolveSignInError();
////            mGoogleApiClient.disconnect();
////            mGoogleApiClient.connect();
//        }
//    }
//    /**
//     * Method to resolve any signin errors
//     * */
//    private void resolveSignInError() {
//        if (mConnectionResult.hasResolution()) {
//            try {
//                mIntentInProgress = true;
//                mConnectionResult.startResolutionForResult(this, RC_SIGN_IN);
//            } catch (IntentSender.SendIntentException e) {
//                mIntentInProgress = false;
//                mGoogleApiClient.connect();
//            }
//        }
//    }
//
//    @Override
//    public void onConnectionFailed(ConnectionResult connectionResult) {
//        if(!connectionResult.hasResolution()) {
//            GooglePlayServicesUtil.getErrorDialog(connectionResult.getErrorCode(), this, 0).show();
//            return;
//        }
//        if (!mIntentInProgress) {
//            mConnectionResult = connectionResult;
//
//            if (mSignInClicked){
//                resolveSignInError();
//            }
//        }
//    }
//
//    @Override
//    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
////        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == RC_SIGN_IN ) {
//            if(resultCode !=RESULT_OK) {
//                mSignInClicked = false;
//                //mIntentInProgress=false;
//            }
//            mIntentInProgress=false;
//            if (!mGoogleApiClient.isConnecting()) {
////                mGoogleApiClient.reconnect();
//                mGoogleApiClient.connect();
//            }
//        }
//    }
//
//    @Override
//    public void onConnected(Bundle bundle) {
//        mSignInClicked = false;
//        Toast.makeText(this, "User is Connectes!", Toast.LENGTH_LONG).show();
//        getProfileInformation();
//        updateUI(true);
//    }
//
//    private void getProfileInformation() {
//        try{
//            if(Plus.PeopleApi.getCurrentPerson(mGoogleApiClient)!=null){
//                Person currentPerson = Plus.PeopleApi
//                        .getCurrentPerson(mGoogleApiClient);
//                String personName = currentPerson.getDisplayName();
//                String personPhotoUrl = currentPerson.getImage().getUrl();
//                String personGooglePlusProfile = currentPerson.getUrl();
//                String email = Plus.AccountApi.getAccountName(mGoogleApiClient);
//
//                Log.e(TAG,"Name: "+ personName
//                        +", plusProfile: "+ personGooglePlusProfile
//                        +", email: "+email
//                        +", Image: "+personPhotoUrl);
//
//                txtName.setText(personName);
//                txtEmail.setText(email);
//
//                personPhotoUrl = personPhotoUrl.substring(0,
//                        personPhotoUrl.length() - 2)
//                        + PROFILE_PIC_SIZE;
//
//                new LoadProfileImage(imgProfilePic).execute(personPhotoUrl);
//            } else {
//                Toast.makeText(getApplicationContext(),
//                        "Person information is null", Toast.LENGTH_LONG).show();
//            }
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public void onConnectionSuspended(int i) {
//        mGoogleApiClient.connect();
//        updateUI(false);
//    }
//
//    private void updateUI(boolean b) {
//        if(b){
//            btnSignIn.setVisibility(View.GONE);
//            btnSignOut.setVisibility(View.VISIBLE);
//            btnRevokeAccess.setVisibility(View.VISIBLE);
//            llProfileLayout.setVisibility(View.VISIBLE);
//        } else {
//            btnSignIn.setVisibility(View.VISIBLE);
//            btnSignOut.setVisibility(View.GONE);
//            btnRevokeAccess.setVisibility(View.GONE);
//            llProfileLayout.setVisibility(View.GONE);
//        }
//    }
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_main, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        // Handle action bar item clicks here. The action bar will
//        // automatically handle clicks on the Home/Up button, so long
//        // as you specify a parent activity in AndroidManifest.xml.
//        int id = item.getItemId();
//
//        //noinspection SimplifiableIfStatement
//        if (id == R.id.action_settings) {
//            return true;
//        }
//
//        return super.onOptionsItemSelected(item);
//    }
//
//    private class LoadProfileImage extends AsyncTask<String, Void, Bitmap>{
//        ImageView bmImage;
//        public LoadProfileImage(ImageView bmImage) {
//            this.bmImage = bmImage;
//        }
//
//        protected Bitmap doInBackground(String... urls){
//            String urldisplay = urls[0];
//            Bitmap mIcon11 = null;
//            try{
//                InputStream in = new URL(urldisplay).openStream();
//                mIcon11 = BitmapFactory.decodeStream(in);
//            } catch (Exception e){
//                Log.e("Error", e.getMessage());
//                e.printStackTrace();
//            }
//            return mIcon11;
//        }
//         protected void onPostExecute(Bitmap result){
//             bmImage.setImageBitmap(result);
//         }
//    }
//}
