package buildnlive.com.arch.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import buildnlive.com.arch.R;
import buildnlive.com.arch.elements.Assets;

public class AssetsSpinAdapter extends ArrayAdapter<Assets> {

    private final ArrayList<Assets> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public AssetsSpinAdapter(@NonNull Context context, int resource, ArrayList<Assets> items) {
        super(context, resource,items);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        item = items;
    }

    @Override
    public int getCount(){
        return item.size();
    }

    @Override
    public Assets getItem(int position){
        return item.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public String getRentId(int position){
        return item.get(position).getItem_rent_id();
    }

    public String getName(int position) {
        return item.get(position).getName();
    }

    @Override
    public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    @Override
    public @NonNull View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        return createItemView(position, convertView, parent);
    }

    private View createItemView(int position, View convertView, ViewGroup parent){
        final View view = mInflater.inflate(mResource, parent, false);

        TextView offTypeTv = (TextView) view.findViewById(R.id.title);

        Assets offerData = item.get(position);

        offTypeTv.setText(offerData.getName());

        return view;
    }


}


