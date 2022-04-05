package com.gesecur.app.ui.operator.workorder;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;

import com.gesecur.app.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Marker;

/** Demonstrates customizing the info window and/or its contents. */
  public  class CustomInfoWindowAdapter implements GoogleMap.InfoWindowAdapter {

        // These are both viewgroups containing an ImageView with id "badge" and two TextViews with id
        // "title" and "snippet".
        private final View mWindow;

        private final View mContents;


    public CustomInfoWindowAdapter(Context context) {
        mWindow = LayoutInflater.from(context).inflate(R.layout.map_info_contents, null);
        mContents = LayoutInflater.from(context).inflate(R.layout.map_info_contents, null);
    }

        @Override
        public View getInfoWindow(Marker marker) {

            render(marker, mWindow);
            return mWindow;
        }

        @Override
        public View getInfoContents(Marker marker) {

            render(marker, mContents);
            return mContents;
        }

        private void render(Marker marker, View view) {

        }
    }