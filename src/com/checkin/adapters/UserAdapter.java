package com.checkin.adapters;

import java.math.BigInteger;
import java.util.Random;
import java.util.Vector;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.checkin.R;
import com.checkin.delegates.GetUser;
import com.checkin.utils.SharedObjects;
import com.checkin.utils.User;

public class UserAdapter extends BaseAdapter {

    Context context;
    Vector<User> data = new Vector<User>();
    private static LayoutInflater inflater = null;

    public UserAdapter(Context context, Vector<User> list) {
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
        User friend = data.get(position);
    	Log.d(SharedObjects.TAG,data.get(position).toString());
    	//this part is useful
        View vi = convertView;
        if (vi == null){
        	vi = inflater.inflate(R.layout.friend_custom_row, null);
        }
        
        ImageView profilePic = (ImageView) vi.findViewById(R.id.profilepic);
        int index;
		if (friend.getPhoneNumber() == null){
			Random rand = new Random(System.currentTimeMillis());
		    index = rand.nextInt(8);
		}
		else{
			BigInteger phoneNum = new BigInteger(friend.getPhoneNumber());
			BigInteger indexBig = phoneNum.mod(new BigInteger("8"));
			index = indexBig.intValue();
		}
		profilePic.setImageResource(GetUser.profileResIds[index]);
        
        TextView text = (TextView) vi.findViewById(R.id.aboutme);
        String blurb = friend.getUsername() + '\n' + friend.getRealName() + '\n' + friend.getAboutMe();
        text.setText(blurb);
        return vi;
    }
}
