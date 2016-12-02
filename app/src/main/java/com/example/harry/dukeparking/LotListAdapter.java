package com.example.harry.dukeparking;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ListAdapter;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Harry on period10/period20/period16.
 */
public class LotListAdapter extends BaseAdapter implements ListAdapter {
    private List<Lot> lotList;
    private Context context;

    public LotListAdapter(List<Lot> lotList,Context context) {
        this.lotList = lotList;
        this.context = context;
    }

    @Override
    public int getCount() {
        return lotList.size();
    }

    @Override
    public Object getItem(int pos) {
        return lotList.get(pos);
    }
    @Override
    public long getItemId(int pos) {
        return 0;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        View view = convertView;
        if (view == null) {
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
            view = inflater.inflate(R.layout.lot_list_item, null);
        }

        TextView lotNameView = (TextView)view.findViewById(R.id.lot_name);
        lotNameView.setText(lotList.get(position).getName());

        TextView lotInfoView = (TextView)view.findViewById(R.id.lot_info);
        double percentAvailable = 100;
        if(lotList.get(position).getCapacity()!=0){
            percentAvailable = 100*((double)(lotList.get(position).getCapacity()-lotList.get(position).getCurrent())/(double)lotList.get(position).getCapacity());
        }
        lotInfoView.setText((int)percentAvailable+"% Available");

        return view;
    }

}
