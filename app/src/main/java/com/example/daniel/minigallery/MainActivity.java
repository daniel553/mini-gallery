package com.example.daniel.minigallery;

import android.content.Intent;
import android.net.Uri;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;
import android.widget.Toast;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.ImageSize;
import com.nostra13.universalimageloader.utils.ImageSizeUtils;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private List<String> mImageList = new ArrayList<>();
    private Inflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //load dummy images
        for(int i=10; i>0; i--)
            mImageList.add("https://pbs.twimg.com/profile_images/616076655547682816/6gMRtQyY.jpg");

        //Library init
        if (!ImageLoader.getInstance().isInited())
            ImageLoader.getInstance().init(ImageLoaderConfiguration.createDefault(MainActivity.this));

        //Find UI
        GridView gridView = (GridView) findViewById(R.id.activity_main_gallery_grid);
        if (gridView != null) {
            //Set the grid Adapter
            gridView.setAdapter(grid_adapter);
        }
        View buttonAdd = findViewById(R.id.activity_main_gallery_button);
        if (buttonAdd != null)
            buttonAdd.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    dispatchTakePictureIntent();
                }
            });
    }

    /**
     * Grid Adapter. Takes the set of image uris to display the gallery
     */
    BaseAdapter grid_adapter = new BaseAdapter() {
        private LayoutInflater mInflater = null;


        @Override
        public int getCount() {
            if (mImageList != null)
                return mImageList.size();
            return 0;
        }

        @Override
        public Object getItem(int position) {
            if (mImageList != null)
                return mImageList.get(position);
            return null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        private LayoutInflater getInflater() {
            if (mInflater == null)
                mInflater = (LayoutInflater) MainActivity.this.getSystemService(LAYOUT_INFLATER_SERVICE);
            return mInflater;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View holder;
            if (convertView == null) {
                //Inflate the layout before
                convertView = getInflater().inflate(R.layout.grid_item, parent, false);
            }
            holder = convertView;

            //Set image
            ImageView imageView = (ImageView) holder.findViewById(R.id.grid_item_image);
            String uri = (String) getItem(position);
            if (uri != null) {
                //Reset
                imageView.setImageResource(R.mipmap.ic_launcher);
                //Set
                ImageLoader.getInstance().displayImage(uri, imageView);
            }

            return convertView;
        }
    };


    //---------------TAKING PICTURE SECTION
    static final int REQUEST_TAKE_PHOTO = 1;
    private String mCurrentPhotoPath;
    private Uri mCurrentPhotoUri;

    /**
     * Creates a Image File and sets its Path
     *
     * @return a File
     * @throws IOException
     */
    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );

        // Save a file: path for use with ACTION_VIEW intents
        mCurrentPhotoPath = "file:" + image.getAbsolutePath();
        return image;
    }

    /**
     * Dispatches the Take picture intent, dont forget the REQUEST_TAKE_PHOTO = 1;
     */
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Ensure that there's a camera activity to handle the intent
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            // Create the File where the photo should go
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex) {
                Toast.makeText(this, "Error", Toast.LENGTH_SHORT).show();
            }
            // Continue only if the File was successfully created
            if (photoFile != null) {
                /* for android N
                Uri photoURI = FileProvider.getUriForFile(this,
                        "com.example.android.fileprovider",
                        photoFile);*/
                mCurrentPhotoUri = Uri.fromFile(photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, mCurrentPhotoUri);
                startActivityForResult(takePictureIntent, REQUEST_TAKE_PHOTO);
            }
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if(resultCode == RESULT_OK){
            if (requestCode == REQUEST_TAKE_PHOTO){
                //Here we know we have a new picture
                mImageList.add(mCurrentPhotoUri.toString());
                //Notify to the adapter the string array has changed
                grid_adapter.notifyDataSetChanged();
            }
        }else
            Toast.makeText(MainActivity.this, "Error", Toast.LENGTH_SHORT).show();

    }
}
