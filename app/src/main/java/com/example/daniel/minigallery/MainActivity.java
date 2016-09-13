package com.example.daniel.minigallery;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

public class MainActivity extends AppCompatActivity {

    private List<String> mImageList = new ArrayList<>();
    private Inflater mInflater;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //Populate data
        mImageList.add("ASDASDASd");
        //Find UI
        GridView gridView = (GridView) findViewById(R.id.activity_main_gallery_grid);
        if (gridView != null) {
            //Set the grid Adapter
            gridView.setAdapter(grid_adapter);
        }


    }


    ListAdapter grid_adapter = new BaseAdapter() {
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

            return convertView;
        }
    };
}
