package com.checkin.adapters;

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
import com.checkin.utils.SharedObjects;
import com.checkin.utils.User;
import com.moxtra.sdk.MXChatManager;
import com.moxtra.sdk.MXException.AccountManagerIsNotValid;

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
        
        vi.setOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
            	try {
            	    MXChatManager.getInstance().createChat(new MXChatManager.OnCreateChatListener() {
            	        @Override
            	        public void onCreateChatSuccess(String binderID) {
            	            Log.d(SharedObjects.TAG, "onCreateChatSuccess(), binderID = " + binderID);
            	        }
            	        @Override
            	        public void onCreateChatFailed(int errorCode, String message) {
            	            Log.d(SharedObjects.TAG, "onCreateChatFailed(), errorCode = " + errorCode + ", message = " + message);
            	        }
            	    });
            	} catch (AccountManagerIsNotValid e) {
            	    e.printStackTrace();
            	}
            }
        }); 
        
        ImageView profilePic = (ImageView) vi.findViewById(R.id.profilepic);
		profilePic.setImageResource(User.GenerateProfileId(friend.getPhoneNumber()));
        
        TextView text = (TextView) vi.findViewById(R.id.aboutme);
        String blurb = friend.getUsername() + '\n' + friend.getRealName() + '\n' + friend.getAboutMe();
        text.setText(blurb);
        return vi;
    }
}
