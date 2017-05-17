package com.bizconnectivity.gino.activities;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;


import com.bizconnectivity.gino.R;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.bizconnectivity.gino.Common.shortToast;

public class LoginActivity extends AppCompatActivity {

    @BindView(R.id.button_facebook_login)
    LoginButton mLoginButton;

    private FirebaseAuth mAuth;
    private CallbackManager mCallbackManager;
    private AccessToken mAccessToken;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_in);

        // Layout Binding
        ButterKnife.bind(this);

        // Initialize Firebase Auth
        mAuth = FirebaseAuth.getInstance();

        // Initialize Facebook Login button
        mCallbackManager = CallbackManager.Factory.create();
        mLoginButton.setReadPermissions("email", "public_profile");

        mLoginButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.d("TAG", "facebook:onSuccess:" + loginResult);
                handleFacebookAccessToken(loginResult.getAccessToken());

//                GraphRequest request = GraphRequest.newGraphPathRequest(
//                        loginResult.getAccessToken(),
//                        "/search",
//                        new GraphRequest.Callback() {
//                            @Override
//                            public void onCompleted(GraphResponse response) {
//                                Log.d("RESPONSE FROM GRAPH API", "onCompleted: " + response.toString());
//                            }
//                        });
//
//                Bundle parameters = new Bundle();
//                parameters.putString("type", "event");
//                parameters.putString("center", "1.32961,103.87547");
//                parameters.putString("distance", "300");
//                parameters.putString("since", "now");
//                parameters.putString("until", "+ 3 months");
//                parameters.putString("q", "singapore");
//                request.setParameters(parameters);
//                request.executeAsync();
            }

            @Override
            public void onCancel() {

                Log.d("TAG", "facebook:onCancel");

                updateUI(null);
            }

            @Override
            public void onError(FacebookException error) {

                Log.d("TAG", "facebook:onError", error);

                updateUI(null);
            }
        });
    }

    private void handleFacebookAccessToken(AccessToken token) {

        Log.d("TAG", "handleFacebookAccessToken:" + token);

//        showProgressDialog();

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {

                            // Sign in success, update UI with the signed-in user's information
                            Log.d("TAG", "signInWithCredential:success");
                            FirebaseUser user = mAuth.getCurrentUser();
                            updateUI(user);

                        } else {

                            // If sign in fails, display a message to the user.
                            Log.w("TAg", "signInWithCredential:failure", task.getException());

                            shortToast(getApplicationContext(), "Authentication failed.");

                            updateUI(null);
                        }

//                        hideProgressDialog();
                    }
                });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        // Pass the activity result back to the Facebook SDK
        mCallbackManager.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onStart() {

        super.onStart();

        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();

        updateUI(currentUser);
    }

    private void updateUI(FirebaseUser firebaseUser) {


        firebaseUser.getDisplayName();

        if (firebaseUser != null) {

//            shortToast(this, firebaseUser.getDisplayName());

//            Intent intent = new Intent(this, MainActivity.class);
//            startActivity(intent);
//            finish();

            if (AccessToken.getCurrentAccessToken() !=  null) {
                Log.d("TAG", "Access Token: " + AccessToken.getCurrentAccessToken().toString());
            }
        }
    }


}
