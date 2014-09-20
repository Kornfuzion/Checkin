package com.checkin.adapters;

import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.checkin.utils.Place;
import com.checkin.utils.SharedObjects;
import com.example.checkin.R;

class PlaceAdapter extends BaseAdapter {

    Context context;
    Vector<Place> data;
    private static LayoutInflater inflater = null;

    public PlaceAdapter(Context context, ArrayList<String> empty) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = null;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        // TODO Auto-generated method stub
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        // TODO Auto-generated method stub
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        // TODO Auto-generated method stub
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // TODO Auto-generated method stub
    	Log.d(SharedObjects.TAG,data.get(position).toString());
    	//this part is useful
        View vi = convertView;
        if (vi == null){
        	//TODO replace with Ernest's xml file for each row
//        	vi = inflater.inflate(R.layout.subscribed, null);
        }
            
        //TODO map data
        return vi;
    }
}
