package com.bizconnectivity.gino.activities;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;

import com.bizconnectivity.gino.R;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.SignInButton;
import com.google.android.gms.common.api.GoogleApiClient;

import org.json.JSONException;
import org.json.JSONObject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Common.shortToast;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;

public class SignUpActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{

    @BindView(R.id.button_facebook_signup)
    LoginButton mButtonFacebookSignUp;

    @BindView(R.id.button_google_signup)
    SignInButton mButtonGoogleSignUp;

    @BindView(R.id.progress_bar)
    ProgressBar mProgressBar;

    boolean isActivityStarted = false;
    SharedPreferences sharedPreferences;
    private CallbackManager mCallbackManager;
    private GoogleApiClient mGoogleApiClient;
    private static final int RC_SIGN_IN = 9001;


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        // Layout Binding
        ButterKnife.bind(this);

        // Shared Preferences
        sharedPreferences = getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        // Initialize & handle Facebook Sign In
        facebookSignIn();

        // Initialize Google Sign In
        initializeGoogle();
    }

    private void facebookSignIn() {

        mCallbackManager = CallbackManager.Factory.create();
        mButtonFacebookSignUp.setReadPermissions("email", "public_profile");

        mButtonFacebookSignUp.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                // Retrieve User Details
                GraphRequest request = GraphRequest.newMeRequest(
                        loginResult.getAccessToken(), new GraphRequest.GraphJSONObjectCallback() {
                            @Override
                            public void onCompleted(JSONObject object, GraphResponse response) {

                                try {
                                    if (object.getString("id")!=null)
                                        Log.d("FACEBOOK", "onCompleted: " +  object.getString("id"));

                                    if (object.getString("name")!=null)
                                        Log.d("FACEBOOK", "onCompleted: " + object.getString("name"));

                                    if (object.getString("birthday")!=null)
                                        Log.d("FACEBOOK", "onCompleted: " + object.getString("birthday"));

                                    if (object.getString("gender")!=null)
                                        Log.d("FACEBOOK", "onCompleted: " + object.getString("gender"));

                                    if (object.getString("email")!=null)
                                        Log.d("FACEBOOK", "onCompleted: " + object.getString("email"));

                                    if (object.getJSONObject("picture").getJSONObject("data").getString("url")!=null)
                                        Log.d("FACEBOOK", "onCompleted: " + object.getJSONObject("picture").getJSONObject("data").getString("url"));

                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                Bundle parameters = new Bundle();
                parameters.putString("fields", "id,name,birthday,gender,email,picture.type(large)");
                request.setParameters(parameters);
                request.executeAsync();

                // Update user signed in
                SharedPreferences.Editor editor = sharedPreferences.edit();
                editor.putBoolean(SHARED_PREF_IS_SIGNED_IN, true).apply();

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                isActivityStarted = true;
                startActivity(intent);

                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancel() {

                mProgressBar.setVisibility(View.GONE);
                Log.d("TAG", "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {

                mProgressBar.setVisibility(View.GONE);
                shortToast(getApplicationContext(), error.toString());
            }
        });
    }

    private void initializeGoogle() {

        // Configure sign-in to request the user's ID, email address, and basic
        // profile. ID and basic profile are included in DEFAULT_SIGN_IN.
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .requestProfile()
                .build();

        // Build a GoogleApiClient with access to the Google Sign-In API and the
        // options specified by gso.
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    private void googleSignIn() {

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }

    private void handleGoggleSignInResult(GoogleSignInResult result) {

        if (result.isSuccess()) {

            // Signed in successfully, show authenticated UI.
            GoogleSignInAccount googleAcc = result.getSignInAccount();

            if (googleAcc!=null) {

                Log.d("GOOGLE", "handleGoggleSignInResult: " + googleAcc.getId());
                Log.d("GOOGLE", "handleGoggleSignInResult: " + googleAcc.getDisplayName());
                Log.d("GOOGLE", "handleGoggleSignInResult: " + googleAcc.getEmail());
                Log.d("GOOGLE", "handleGoggleSignInResult: " + googleAcc.getPhotoUrl());
            }

            // Update user signed in
            SharedPreferences.Editor editor = sharedPreferences.edit();
            editor.putBoolean(SHARED_PREF_IS_SIGNED_IN, true).apply();

            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            isActivityStarted = true;
            startActivity(intent);

            mProgressBar.setVisibility(View.GONE);

        } else {
            // Signed out, show unauthenticated UI.
            mProgressBar.setVisibility(View.GONE);
            shortToast(this, "Authentication Failed.");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);

        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {

            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            handleGoggleSignInResult(result);
        }
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

        mProgressBar.setVisibility(View.GONE);
        shortToast(this, "Google Play Services Error.");
    }

    @OnClick(R.id.button_facebook)
    public void buttonFacebookOnClick (View view) {

        mProgressBar.setVisibility(View.VISIBLE);
        mButtonFacebookSignUp.performClick();
    }

    @OnClick(R.id.button_google)
    public void buttonGoogleOnClick(View view) {

        mProgressBar.setVisibility(View.VISIBLE);
        mButtonGoogleSignUp.performClick();
        googleSignIn();
    }

    @Override
    public void onBackPressed() {

        Intent intent = new Intent(this, SplashActivity.class);
        isActivityStarted = true;
        startActivity(intent);
    }

    @Override
    protected void onStop() {

        super.onStop();

        if (isActivityStarted) finish();
    }
}
