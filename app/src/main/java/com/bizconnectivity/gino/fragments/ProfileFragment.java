package com.bizconnectivity.gino.fragments;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.AboutGinoActivity;
import com.bizconnectivity.gino.activities.HelpSupportActivity;
import com.bizconnectivity.gino.activities.LoveActivity;
import com.bizconnectivity.gino.activities.PrelovedActivity;
import com.bizconnectivity.gino.activities.SettingsActivity;
import com.bumptech.glide.Glide;
import com.flipboard.bottomsheet.BottomSheetLayout;
import com.flipboard.bottomsheet.commons.ImagePickerSheetView;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.bizconnectivity.gino.Common.*;
import static com.bizconnectivity.gino.Constant.*;

public class ProfileFragment extends Fragment {

    @BindView(R.id.bottom_sheet_layout)
    BottomSheetLayout mBottomSheetLayout;

    @BindView(R.id.profile_picture)
    ImageView mImageViewProfile;

    private Uri cameraImageUri = null;
    public static final int REQUEST_STORAGE = 0;
    public static final int REQUEST_IMAGE_CAPTURE = REQUEST_STORAGE + 1;
    public static final int REQUEST_LOAD_IMAGE = REQUEST_IMAGE_CAPTURE + 1;
    private static final int MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 0;

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
    }

    @OnClick(R.id.love_layout)
    public void loveOnClick(View view) {

        Intent intent = new Intent(getContext(), LoveActivity.class);
        startActivity(intent);
    }

    @OnClick(R.id.preloved_layout)
    public void prelovedOnClick(View view) {

        Intent intent = new Intent(getContext(), PrelovedActivity.class);
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

    // region BottomSheetLayout & ImagePicker
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
}
