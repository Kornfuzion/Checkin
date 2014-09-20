package com.checkin.adapters;

import java.util.ArrayList;
import java.util.Vector;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.checkin.utils.Place;
import com.checkin.utils.SharedObjects;
import com.example.checkin.R;

public class PlaceAdapter extends BaseAdapter {

    Context context;
    Vector<Place> data = new Vector<Place>();
    private static LayoutInflater inflater = null;

    public PlaceAdapter(Context context, Vector<Place> list) {
        // TODO Auto-generated constructor stub
        this.context = context;
        this.data = list;
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
        Place p = data.get(position);
    	Log.d(SharedObjects.TAG,data.get(position).toString());
    	//this part is useful
        View vi = convertView;
        if (vi == null){
        	//TODO replace with Ernest's xml file for each row
        	vi = inflater.inflate(R.layout.place_custom_row, null);
        }
        
        TextView placeName = (TextView) vi.findViewById(R.id.place_name);
        placeName.setText(p.getName());
        
            
        //TODO map data
        return vi;
    }
}
