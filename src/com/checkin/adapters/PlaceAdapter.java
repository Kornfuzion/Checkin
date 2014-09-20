package com.checkin.adapters;

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
        this.context = context;
        this.data = list;
        inflater = (LayoutInflater) context
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return data.size();
    }

    @Override
    public Object getItem(int position) {
        return data.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        Place p = data.get(position);
    	Log.d(SharedObjects.TAG,data.get(position).toString());
    	//this part is useful
        View vi = convertView;
        if (vi == null){
        	vi = inflater.inflate(R.layout.place_custom_row, null);
        }
        
        TextView placeName = (TextView) vi.findViewById(R.id.place_name);
        placeName.setText(p.getName());
        
        TextView numFriends = (TextView) vi.findViewById(R.id.place_friends);
        numFriends.setText(p.getNumFriends() + "");
        
        TextView placeId = (TextView) vi.findViewById(R.id.place_id_field);
        placeId.setText(p.getPlace_id() + "");
            
        return vi;
    }
}
