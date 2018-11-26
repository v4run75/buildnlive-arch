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
import buildnlive.com.arch.elements.Employee;
import buildnlive.com.arch.elements.Work;

public class EmployeeSpinAdapter extends ArrayAdapter<Employee> {

    private final ArrayList<Employee> item;
    private final LayoutInflater mInflater;
    private final Context mContext;
    private final int mResource;

    public EmployeeSpinAdapter(@NonNull Context context, int resource, ArrayList<Employee> items) {
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
    public Employee getItem(int position){
        return item.get(position);
    }

    @Override
    public long getItemId(int position){
        return position;
    }

    public String getID(int position){
        return item.get(position).getUser_id();
    }

    public String getName(int position) {
        return item.get(position).getEmail_add();
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

        Employee offerData = item.get(position);

        offTypeTv.setText(offerData.getEmail_add());

        return view;
    }


}


