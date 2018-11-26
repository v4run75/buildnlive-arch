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
import buildnlive.com.arch.elements.Category;
import buildnlive.com.arch.elements.Vendor;

public class VendorSpinAdapter extends ArrayAdapter<Vendor> {

    private final ArrayList<Vendor> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public VendorSpinAdapter(@NonNull Context context, int resource, ArrayList<Vendor> items) {
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
    public Vendor getItem(int position){
        return item.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public String getVendorID(int position){
        return item.get(position).getVendor_id();
    }

    public String getVendorName(int position) {
        return item.get(position).getVendor_name();
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

        Vendor offerData = item.get(position);

        offTypeTv.setText(offerData.getVendor_name());

        return view;
    }


}


