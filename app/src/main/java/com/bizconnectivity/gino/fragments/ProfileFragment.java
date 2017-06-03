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
import android.widget.LinearLayout;
import android.widget.TextView;

import com.afollestad.materialdialogs.DialogAction;
import com.afollestad.materialdialogs.MaterialDialog;
import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.AboutGinoActivity;
import com.bizconnectivity.gino.activities.DismissedActivity;
import com.bizconnectivity.gino.activities.FavouriteActivity;
import com.bizconnectivity.gino.activities.HelpSupportActivity;
import com.bizconnectivity.gino.activities.SettingsActivity;
import com.bizconnectivity.gino.activities.SplashActivity;
import com.bizconnectivity.gino.asynctasks.RetrieveUserAsyncTask;
import com.bizconnectivity.gino.asynctasks.UpdateUserPhotoAsyncTask;
import com.bizconnectivity.gino.models.User;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Common.isNetworkAvailable;
import static com.bizconnectivity.gino.Common.snackBar;
import static com.bizconnectivity.gino.Constant.LOGIN_FACEBOOK;
import static com.bizconnectivity.gino.Constant.LOGIN_GOOGLE;
import static com.bizconnectivity.gino.Constant.MSG_CANNOT_ACCESS_DEVICE_STORAGE;
import static com.bizconnectivity.gino.Constant.MSG_SOMETHING_WENT_WRONG;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_IS_SIGNED_IN;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_KEY;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_EMAIL;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_ID;
import static com.bizconnectivity.gino.Constant.SHARED_PREF_USER_SIGN_IN_TYPE;

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

    @BindView(R.id.text_message)
    TextView mTextViewMessage;

    @BindView(R.id.layout_favourite)
    LinearLayout mLayoutFavourite;

    @BindView(R.id.layout_dismissed)
    LinearLayout mLayoutDismissed;

    @BindView(R.id.layout_settings)
    LinearLayout mLayoutSettings;

    private Uri cameraImageUri = null;
    public static final int REQUEST_STORAGE = 0;
    public static final int REQUEST_IMAGE_CAPTURE = REQUEST_STORAGE + 1;
    public static final int REQUEST_LOAD_IMAGE = REQUEST_IMAGE_CAPTURE + 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;
    private GoogleApiClient mGoogleApiClient;
    private SharedPreferences sharedPreferences;
    private User user;

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

        initializeGoogle();

        // Swipe Refresh Listener
        mSwipeRefreshLayout.setColorSchemeColors(ContextCompat.getColor(getContext(), R.color.colorPrimaryDark));
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

                fetchData();
            }
        });

        fetchData();
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

    // Retrieve data from WS
    private void fetchData() {

        mSwipeRefreshLayout.setRefreshing(true);

        if (isNetworkAvailable(getContext())) {

            // Check User Sign In
            if (sharedPreferences.getBoolean(SHARED_PREF_IS_SIGNED_IN, false)) {

                new RetrieveUserAsyncTask(this, sharedPreferences.getString(SHARED_PREF_USER_EMAIL, "")).execute();

            } else {

                updateUI(false);
            }

        } else {

            mSwipeRefreshLayout.setRefreshing(false);
            mSwipeRefreshLayout.setVisibility(View.GONE);
            mTextViewMessage.setVisibility(View.VISIBLE);
        }
    }

    // Retrieve User Detail Callback
    @Override
    public void retrieveUserDetail(User result) {

        if (result != null) {

            user = result;
            updateUI(true);

        } else {

            snackBar(mCoordinatorLayout, "Retrieve User Detail Error");
            updateUI(false);
        }
    }

    // Update UI
    private void updateUI(boolean response) {

        if (response) {

            mButtonLogout.setVisibility(View.VISIBLE);
            mButtonSignInOut.setVisibility(View.GONE);

            if (user.getUserName() != null) {
                mTextViewName.setVisibility(View.VISIBLE);
                mTextViewName.setText(user.getUserName());
            } else {
                mTextViewName.setVisibility(View.GONE);
            }

            if (user.getPhotoFile() != null) {

                byte[] bloc = Base64.decode(user.getPhotoFile(), Base64.DEFAULT);
                Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
                mImageViewProfile.setImageBitmap(bitmap);

            } else if (user.getPhotoUrl() != null) {

                Picasso.with(getContext()).load(Uri.parse(user.getPhotoUrl())).into(mImageViewProfile);
            } else {

                mImageViewProfile.setImageResource(R.drawable.ic_perm_identity_black_24dp);
            }

            mSwipeRefreshLayout.setRefreshing(false);

        } else {

            mButtonLogout.setVisibility(View.GONE);
            mButtonSignInOut.setVisibility(View.VISIBLE);
            mTextViewName.setVisibility(View.GONE);
            mLayoutFavourite.setVisibility(View.GONE);
            mLayoutDismissed.setVisibility(View.GONE);
            mLayoutSettings.setVisibility(View.GONE);
            mImageViewProfile.setImageResource(R.drawable.ic_perm_identity_black_24dp);
            mImageViewProfile.setOnClickListener(null);

            mSwipeRefreshLayout.setRefreshing(false);
        }
    }

    // Update shared preference log out
    private void updateIsLogOut() {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        editor.putBoolean(SHARED_PREF_IS_SIGNED_IN, false).apply();
        editor.remove(SHARED_PREF_USER_ID).apply();
        editor.remove(SHARED_PREF_USER_EMAIL).apply();
        editor.remove(SHARED_PREF_USER_SIGN_IN_TYPE).apply();
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

    @OnClick(R.id.layout_favourite)
    public void loveOnClick(View view) {

        Intent intent = new Intent(getContext(), FavouriteActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_dismissed)
    public void prelovedOnClick(View view) {

        Intent intent = new Intent(getContext(), DismissedActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.layout_settings)
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

        new MaterialDialog.Builder(getContext())
                .title("Logout Confirmation")
                .content("Are you sure you want to log out?")
                .positiveText(android.R.string.ok)
                .negativeText(android.R.string.cancel)
                .onPositive(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                        switch (sharedPreferences.getString(SHARED_PREF_USER_SIGN_IN_TYPE, "")) {

                            case LOGIN_FACEBOOK:

                                updateIsLogOut();
                                LoginManager.getInstance().logOut();
                                updateUI(false);
                                navigateToSplashScreen();
                                break;

                            case LOGIN_GOOGLE:

                                updateIsLogOut();
                                googleSignOut();
                                navigateToSplashScreen();
                                break;

                            default:

                                updateIsLogOut();
                                updateUI(false);
                                navigateToSplashScreen();
                                break;
                        }
                    }
                })
                .onNegative(new MaterialDialog.SingleButtonCallback() {
                    @Override
                    public void onClick(@NonNull MaterialDialog dialog, @NonNull DialogAction which) {

                    }
                })
                .show();
    }

    @OnClick(R.id.button_sign_in_out)
    public void signInOutOnClick(View view) {
        navigateToSplashScreen();
    }

    private void navigateToSplashScreen() {

        Intent intent = new Intent(getContext(), SplashActivity.class);
        startActivity(intent);
    }

    // region Profile Pictue BottomSheetLayout & ImagePicker

    @OnClick(R.id.profile_picture)
    public void profilePictureOnClick(View view) {

        //check read storage permission
        if (ContextCompat.checkSelfPermission(getActivity(), Manifest.permission.READ_EXTERNAL_STORAGE)
                != PackageManager.PERMISSION_GRANTED) {

            requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.CAMERA},
                    MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);

        } else {

            showSheetView();
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {

        switch (requestCode) {
            case MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    showSheetView();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                    snackBar(mCoordinatorLayout, MSG_CANNOT_ACCESS_DEVICE_STORAGE);
                }
            }
        }
    }

    private void showSheetView() {

        ImagePickerSheetView sheetView = new ImagePickerSheetView.Builder(getActivity())
                .setMaxItems(30)
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

                            snackBar(mCoordinatorLayout, MSG_SOMETHING_WENT_WRONG);
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
            takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        } else {
            takePictureIntent = null;
        }

        return takePictureIntent;
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
                snackBar(mCoordinatorLayout, "Could not create imageFile for camera");
            }
        }
    }

    private File createImageFile() throws IOException {

        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "PNG_" + timeStamp + "_";
        File storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES);
        File imageFile = File.createTempFile(
                imageFileName,  /* prefix */
                ".png",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        cameraImageUri = Uri.fromFile(imageFile);
        return imageFile;
    }

    private void showSelectedImage(Uri selectedImageUri) {

        try {

            byte[] photoFile;
            String photoName, photoExt;

            Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), selectedImageUri);
            int nh = (int) (bitmap.getHeight() * (512.0 / bitmap.getWidth()));
            Bitmap scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

            File file = new File(String.valueOf(selectedImageUri));
            photoName = file.getName();
            photoExt = photoName.substring(photoName.indexOf(".") + 1);
            photoFile = convertToBinary(scaled);

            new UpdateUserPhotoAsyncTask(sharedPreferences.getInt(SHARED_PREF_USER_ID, 0), photoFile,
                    photoName, photoExt).execute();

            mImageViewProfile.setImageDrawable(null);
            mImageViewProfile.setScaleType(ImageView.ScaleType.CENTER_CROP);
            mImageViewProfile.setImageBitmap(scaled);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private byte[] convertToBinary(Bitmap image) {

        ByteArrayOutputStream stream = new ByteArrayOutputStream();
        image.compress(Bitmap.CompressFormat.PNG, 100, stream);
        return stream.toByteArray();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {

        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {

            Uri selectedImage = null;

            if (requestCode == REQUEST_LOAD_IMAGE && data != null) {

                selectedImage = data.getData();

                if (selectedImage == null) {

                    snackBar(mCoordinatorLayout, MSG_SOMETHING_WENT_WRONG);
                }

            } else if (requestCode == REQUEST_IMAGE_CAPTURE) {

                // Do something with imagePath
                selectedImage = cameraImageUri;
            }

            if (selectedImage != null) {

                showSelectedImage(selectedImage);

            } else {

                snackBar(mCoordinatorLayout, MSG_SOMETHING_WENT_WRONG);
            }
        }
    }
    // endregion

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mGoogleApiClient.stopAutoManage(getActivity());
        mGoogleApiClient.disconnect();
    }

    @Override
    public void onResume() {
        super.onResume();
        fetchData();
    }
}
