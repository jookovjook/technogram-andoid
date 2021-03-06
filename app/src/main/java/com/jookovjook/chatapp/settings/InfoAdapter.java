package com.jookovjook.chatapp.settings;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.jookovjook.chatapp.R;
import com.jookovjook.chatapp.utils.AuthHelper;

import java.util.ArrayList;

class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.ViewHolder>{

    static final String USERNAME = "Username";
    static final String NAME = "Name";
    static final String SURNAME = "Surname";
    static final String ABOUT = "About";

    private class SettingsProvider {

        String type;
        String value;

        SettingsProvider(String type, String value){
            this.type = type;
            this.value = value;
        }


    }

    private ArrayList<SettingsProvider> mList;
    private Context context;

    public InfoAdapter(Context context){
        this.context = context;
        update();
    }

    public void update(){
        mList = new ArrayList<>();
        mList.add(new SettingsProvider(USERNAME, AuthHelper.getUsername(context)));
        mList.add(new SettingsProvider(NAME , AuthHelper.getNAME(context)));
        mList.add(new SettingsProvider(SURNAME , AuthHelper.getNAME(context)));
        mList.add(new SettingsProvider(ABOUT , AuthHelper.getABOUT(context)));
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.settings_item, parent, false);
        return new InfoAdapter.ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        final SettingsProvider sProvider = mList.get(position);
        holder.name.setText(sProvider.type);
        holder.value.setText(sProvider.value);
        if(sProvider.type.equals(USERNAME)) holder.value.setText("\u0040" + sProvider.value);
        holder.itemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, InfoSettActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("type", sProvider.type);
                bundle.putString("value", sProvider.value);
                intent.putExtras(bundle);
                ((Activity)context).startActivityForResult(intent, SettingsActivity.REQUEST_CODE_SETTING);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        TextView name, value;
        LinearLayout itemLayout;

        ViewHolder (View itemView) {
            super(itemView);
            name = (TextView) itemView.findViewById(R.id.name);
            value = (TextView) itemView.findViewById(R.id.value);
            itemLayout = (LinearLayout) itemView.findViewById(R.id.itemLayout);

        }
    }

}
