package Smit.expensemanager;

import android.Manifest;
import android.os.Environment;
import android.support.v4.app.Fragment;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import static android.app.Activity.RESULT_OK;
import static android.content.ContentValues.TAG;
import static android.provider.MediaStore.Files.FileColumns.MEDIA_TYPE_IMAGE;

public class billFragment extends Fragment {


    static final int REQUEST_IMAGE_CAPTURE = 1;

    private static final int PERMISSION_CODE = 1000;
    private static final int IMAGE_CAPTURE_CODE = 1001;
    Button mCaptureBtn;
    ImageView mImageView;
    Uri image_uri;


  @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                         Bundle savedInstanceState) {
      //setContentView(R.layout.activity_bill_fragment);
      View myview = inflater.inflate(R.layout.activity_bill_fragment, container, false);
      mCaptureBtn = myview.findViewById(R.id.capture_img_btn);
      mImageView = myview.findViewById(R.id.image_view);

      mCaptureBtn.setOnClickListener(new View.OnClickListener() {
          @Override
          public void onClick(View v) {
              dispatchTakePictureIntent();
              if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//                  if (checkSelfPermission(Manifest.permission.CAMERA) == PackageManager.PERMISSION_DENIED ||
//                          checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_DENIED) {
//                      String[] permission = {Manifest.permission.CAMERA, Manifest.permission.WRITE_EXTERNAL_STORAGE};
//
//                      //popup
//                      requestPermissions(permission, PERMISSION_CODE);
//                  } else {
//                      openCamera();
//                  }
              } else {
                  openCamera();
              }

          }
      });
      return myview;
  }



    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }


    public Uri getOutputMediaFileUri(int type) {
        return Uri.fromFile(getOutputMediaFile(type));
    }

    private File getOutputMediaFile(int type) {
        final String IMAGE_DIRECTORY_NAME = "FileUpload";
        // External sdcard location
        File mediaStorageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_PICTURES),IMAGE_DIRECTORY_NAME);

        // Create the storage directory if it does not exist
        if (!mediaStorageDir.exists()) {
            if (!mediaStorageDir.mkdirs()) {
                Log.d(TAG, "Oops! Failed create "
                        + IMAGE_DIRECTORY_NAME + " directory");
                return null;
            }
        }

        // Create a media file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                Locale.getDefault()).format(new Date());
        File mediaFile;

        mediaFile = new File(mediaStorageDir.getPath() + File.separator+ "IMG_" + timeStamp + ".jpg");

//        imageName = "IMG_" + timeStamp + ".jpg";
//
//        fullimagepath = mediaStorageDir.getPath() + File.separator+ "IMG_" + timeStamp + ".jpg";


        Toast.makeText(getActivity(),"1",Toast.LENGTH_SHORT).show();


        return mediaFile;
    }

    private void openCamera() {
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "New Picture");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From the Camera");
        image_uri = getActivity().getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);
        //image_uri = getOutputMediaFileUri(MEDIA_TYPE_IMAGE);
        //camera intent
        //Toast.makeText(getActivity(),"imahe il "+ image_uri,Toast.LENGTH_SHORT).show();
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,image_uri);
        startActivityForResult(cameraIntent, IMAGE_CAPTURE_CODE);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        // used when allow or deny is used
        switch (requestCode){
            case PERMISSION_CODE:{
                if (grantResults.length > 0 && grantResults[0] ==  PackageManager.PERMISSION_GRANTED){
                    openCamera();
                }
                else {
                    Toast.makeText(getActivity(),"Denied",Toast.LENGTH_SHORT).show();

                }
            }
        }
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        // called when image was captured from the camera
        if (resultCode == RESULT_OK){
            //set the image captured to the imageview
            //Toast.makeText(getActivity(),"imahe il "+ requestCode,Toast.LENGTH_SHORT).show();
            mImageView.setImageURI(image_uri);
        }
    }
}



