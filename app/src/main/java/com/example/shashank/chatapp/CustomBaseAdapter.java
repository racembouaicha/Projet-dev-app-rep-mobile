package com.example.shashank.chatapp;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

public class CustomBaseAdapter extends BaseAdapter {

    Context ctx;
    String userList[];
    LayoutInflater inflater ;
   public CustomBaseAdapter(Context ctx, String [] userList){
       this.ctx = ctx;
       this.userList = userList ;
       inflater = LayoutInflater.from(ctx);
   }

    @Override
    public int getCount() {
        return userList.length;
    }

    @Override
    public Object getItem(int i) {
        return userList[i];
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @SuppressLint("ViewHolder")
    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
       view = inflater.inflate(R.layout.chatviewlayout, null);
        TextView txtView = (TextView) view.findViewById(R.id.nameofuser);
        txtView.setText(userList[i]);
        return view;
    }
}
