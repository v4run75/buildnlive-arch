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
import buildnlive.com.arch.elements.CustomWork;
import buildnlive.com.arch.elements.Work;

public class CustomWorkSpinAdapter extends ArrayAdapter<CustomWork> {

    private final ArrayList<CustomWork> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public CustomWorkSpinAdapter(@NonNull Context context, int resource, ArrayList<CustomWork> items) {
        super(context, resource,items);
        mContext = context;
        mInflater = LayoutInflater.from(context);
        mResource = resource;
        item = items;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @Override
    public int getCount(){
        return item.size();
    }

    @Override
    public CustomWork getItem(int position){
        return item.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public String getID(int position){
        return item.get(position).getId();
    }

    public String getProjectWorkListId(int position){
        return item.get(position).getProjectWorkListId();
    }

    public String getMasterWorkId(int position){
        return item.get(position).getMasterWorkId();
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

        CustomWork offerData = item.get(position);

        offTypeTv.setText(offerData.getName());

        return view;
    }


}


