package com.bizconnectivity.gino.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.AboutGinoActivity;
import com.bizconnectivity.gino.activities.DismissedActivity;
import com.bizconnectivity.gino.activities.HelpSupportActivity;
import com.bizconnectivity.gino.activities.FavouriteActivity;
import com.bizconnectivity.gino.activities.SettingsActivity;
import com.bizconnectivity.gino.activities.SplashActivity;
import com.bizconnectivity.gino.asynctasks.RetrieveUserAsyncTask;
import com.bizconnectivity.gino.models.UserModel;
import com.bumptech.glide.Glide;
import com.facebook.login.LoginManager;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.ImagePickerSheetView;
import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.realm.Realm;
import io.realm.RealmResults;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.shortToast;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.ERR_MSG_NO_INTERNET_CONNECTION;
import static com.bizconnectivity.gino.Constant.MSG_CANNOT_ACCESS_DEVICE_STORAGE;
import static com.bizconnectivity.gino.Constant.MSG_SOMETHING_WENT_WRONG;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;

public class ProfileFragment extends Fragment implements RetrieveUserAsyncTask.AsyncResponse, GoogleApiClient.OnConnectionFailedListener {

    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.bottom_sheet_layout)
    BottomSheetLayout mBottomSheetLayout;

    @BindView(R.id.profile_picture)
    ImageView mImageViewProfile;

    @BindView(R.id.text_name)
    TextView mTextViewName;

    @BindView(R.id.button_sign_in_out)
    Button mButtonSignInOut;

    @BindView(R.id.button_logout)
    Button mButtonLogout;

    @BindView(R.id.coordinator_layout)
    CoordinatorLayout mCoordinatorLayout;

    @BindView(R.id.swipeRefreshLayout)
    SwipeRefreshLayout mSwipeRefreshLayout;

    private Uri cameraImageUri = null;
    public static final int REQUEST_STORAGE = 0;
    public static final int REQUEST_IMAGE_CAPTURE = REQUEST_STORAGE + 1;
    public static final int REQUEST_LOAD_IMAGE = REQUEST_IMAGE_CAPTURE + 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences sharedPreferences;
    private Realm realm;
    private RealmResults<UserModel> user;
    String loginType;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_profile, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

        // Layout Binding
        ButterKnife.bind(this, view);

        // Action Bar
        ((AppCompatActivity) getActivity()).setSupportActionBar(mToolbar);

        // Shared Preferences
        sharedPreferences = getContext().getSharedPreferences(SHARED_PREF_KEY, Context.MODE_PRIVATE);

        // Initial Realm
        realm = Realm.getDefaultInstance();
        user = realm.where(UserModel.class).findAll();

        initializeGoogle();

        // Swipe Refresh Listener
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchData();
            }
        });

        mSwipeRefreshLayout.setRefreshing(true);
        fetchData();
    }

    private void fetchData() {

        // Check User Sign In
        if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

            if (isNetworkAvailable(getContext())) {
                new RetrieveUserAsyncTask(this, user.get(0).getUserEmail()).execute();
            } else {
                updateUI(true);
                snackBar(mCoordinatorLayout, ERR_MSG_NO_INTERNET_CONNECTION);
            }

        } else {
            updateUI(false);
        }
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
        mGoogleApiClient = new GoogleApiClient.Builder(getActivity())
                .enableAutoManage(getActivity() /* FragmentActivity */, this /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
    }

    @Override
    public void retrieveUserDetail(final UserModel response) {

        if (response != null) {

            realm.executeTransaction(new Realm.Transaction() {
                @Override
                public void execute(Realm realm) {

                    realm.copyToRealmOrUpdate(response);
                }
            });

            // Retrieve Latest User Details
            if (response.getFacebookID() != null) {
                loginType = "FACEBOOK";
            } else if (response.getGoogleID() != null) {
                loginType = "GOOGLE";
            } else {
                loginType = "EMAIL";
            }

            updateUI(true);
        } else {
            updateUI(true);
        }
    }

    // Update UI
    private void updateUI(boolean response) {

        if (response) {

            mButtonLogout.setVisibility(View.VISIBLE);
            mButtonSignInOut.setVisibility(View.GONE);

            if (user.get(0).getUserName() != null) {
                mTextViewName.setVisibility(View.VISIBLE);
                mTextViewName.setText(user.get(0).getUserName());
            } else {
                mTextViewName.setVisibility(View.GONE);
            }

            if (user.get(0).getPhotoFile() != null) {
                byte[] bloc = Base64.decode(user.get(0).getPhotoFile(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
                mImageViewProfile.setImageBitmap(bitmap);
            } else if (user.get(0).getPhotoUrl() != null) {
                Picasso.with(getContext()).load(Uri.parse(user.get(0).getPhotoUrl())).into(mImageViewProfile);
            } else {
                mImageViewProfile.setImageResource(R.drawable.ic_perm_identity_black_24dp);
            }

            mSwipeRefreshLayout.setRefreshing(false);

        } else {

            mButtonLogout.setVisibility(View.GONE);
            mButtonSignInOut.setVisibility(View.VISIBLE);
            mTextViewName.setVisibility(View.GONE);
            mImageViewProfile.setImageResource(R.drawable.ic_perm_identity_black_24dp);

            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    // Google Sign Out
    private void googleSignOut() {
        Auth.GoogleSignInApi.signOut(mGoogleApiClient).setResultCallback(
                new ResultCallback<Status>() {
                    @Override
                    public void onResult(Status status) {
                        updateUI(false);
                    }
                });
    }

    @OnClick(R.id.love_layout)
    public void loveOnClick(View view) {

        Intent intent = new Intent(getContext(), FavouriteActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.preloved_layout)
    public void prelovedOnClick(View view) {

        Intent intent = new Intent(getContext(), DismissedActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.settings_layout)
    public void settingsOnClick(View view) {

        Intent intent = new Intent(getContext(), SettingsActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.about_layout)
    public void aboutOnClick(View view) {

        Intent intent = new Intent(getContext(), AboutGinoActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.feedback_layout)
    public void feedbackOnClick(View view) {

        Intent intent = new Intent(getContext(), HelpSupportActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.button_logout)
    public void logoutOnClick(View view) {

        switch (loginType) {

            case "FACEBOOK":

                clearUserData();
                updateIsLogOut();
                LoginManager.getInstance().logOut();
                updateUI(false);
                navigateToSplashScreen();
                break;

            case "GOOGLE":

                clearUserData();
                updateIsLogOut();
                googleSignOut();
                navigateToSplashScreen();
                break;

            default:

                clearUserData();
                updateIsLogOut();
                updateUI(false);
                navigateToSplashScreen();
                break;
        }
    }

    @OnClick(R.id.button_sign_in_out)
    public void signInOutOnClick(View view) {

        navigateToSplashScreen();
    }

    private void navigateToSplashScreen() {

        Intent intent = new Intent(getContext(), SplashActivity.class);
        startActivity(intent);
    }

    // Update shared preference log out
    private void updateIsLogOut() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREF_IS_SIGNED_IN, false).apply();
    }

    // Clear User Data
    private void clearUserData() {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.where(UserModel.class).findAll().deleteAllFromRealm();
            }
        });
    }

    @OnClick(R.id.profile_picture)
    public void profilePictureOnClick(View view) {

        //check read storage permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
        } else {

            showSheetView();
        }
    }

    // region BottomSheetLayout & ImagePicker
    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    // permission was granted, yay! Do the
                    // contacts-related task you need to do.

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    shortToast(getContext(), MSG_CANNOT_ACCESS_DEVICE_STORAGE);
                }

                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void showSheetView() {

        ImagePickerSheetView sheetView = new ImagePickerSheetView.Builder(getActivity())
                .setMaxItems(50)
                .setShowCameraOption(createCameraIntent() != null)
                .setShowPickerOption(createPickIntent() != null)
                .setImageProvider(new ImagePickerSheetView.ImageProvider() {
                    @Override
                    public void onProvideImage(ImageView imageView, Uri imageUri, int size) {

                        Glide.with(ProfileFragment.this)
                                .load(imageUri)
                                .centerCrop()
                                .crossFade()
                                .into(imageView);
                    }
                })
                .setOnTileSelectedListener(new ImagePickerSheetView.OnTileSelectedListener() {
                    @Override
                    public void onTileSelected(ImagePickerSheetView.ImagePickerTile selectedTile) {

                        mBottomSheetLayout.dismissSheet();

                        if (selectedTile.isCameraTile()) {

                            dispatchTakePictureIntent();

                        } else if (selectedTile.isPickerTile()) {

                            startActivityForResult(createPickIntent(), REQUEST_LOAD_IMAGE);

                        } else if (selectedTile.isImageTile()) {

                            showSelectedImage(selectedTile.getImageUri());

                        } else {

                            shortToast(getActivity(), MSG_SOMETHING_WENT_WRONG);
                        }
                    }
                })
                .setTitle("Choose an image...")
                .create();

        mBottomSheetLayout.showWithSheetView(sheetView);
    }

    @Nullable
    public Intent createCameraIntent() {

        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);

        if (takePictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            return takePictureIntent;
        } else {
            return null;
        }
    }

    @Nullable
    public Intent createPickIntent() {

        Intent picImageIntent = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);

        if (picImageIntent.resolveActivity(getActivity().getPackageManager()) != null) {
            return picImageIntent;
        } else {
            return null;
        }
    }

    /**
     * This utility function combines the camera intent creation and image file creation, and
     * ultimately fires the intent.
     *
     * @see {@link #createCameraIntent()}
     * @see {@link #createImageFile()}
     */
    private void dispatchTakePictureIntent() {

        Intent takePictureIntent = createCameraIntent();

        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent != null) {
            // Create the File where the photo should go
            try {

                File imageFile = createImageFile();
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, Uri.fromFile(imageFile));
                startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);

            } catch (IOException e) {
                // Error occurred while creating the File
                shortToast(getContext(), "Could not create imageFile for camera");
            }
        }
    }

    /**
     * For images captured from the camera, we need to create a File first to tell the camera
     * where to store the image.
     *
     * @return the File created for the image to be store under.
     */
    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        cameraImageUri = Uri.fromFile(imageFile);
        return imageFile;
    }

    private void showSelectedImage(Uri selectedImageUri) {

        try {

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

            mImageViewProfile.setImageDrawable(null);
            mImageViewProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageViewProfile.setImageBitmap(scaled);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Uri selectedImage = null;

            if (requestCode == REQUEST_LOAD_IMAGE && data != null) {

                selectedImage = data.getData();

                if (selectedImage == null) {

                    shortToast(getContext(), MSG_SOMETHING_WENT_WRONG);
                }

            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {

                // Do something with imagePath
                selectedImage = cameraImageUri;
            }

            if (selectedImage != null) {

                showSelectedImage(selectedImage);

            } else {

                shortToast(getActivity(), MSG_SOMETHING_WENT_WRONG);
            }
        }
    }
    // endregion

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onResume(){
        super.onResume();
        realm = Realm.getDefaultInstance();
    }

    @Override
    public void onStop() {
        super.onStop();
        if (!realm.isClosed()) realm.close();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }
}
