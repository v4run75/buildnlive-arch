package buildnlive.com.arch.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.arch.R;
import buildnlive.com.arch.elements.Vendor;

public class VendorAdapter extends RecyclerView.Adapter<VendorAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Vendor project, int pos, View view);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    private final List<Vendor> items;
    private Context context;
    private final OnItemClickListener listener;

    public VendorAdapter(Context context, ArrayList<Vendor> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public VendorAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_manage_employee_vendor_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position, listener);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,number,type;

        public ViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.project_name);
            number= view.findViewById(R.id.number);
            type= view.findViewById(R.id.type);

        }

        public void bind(final Context context, final Vendor item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getVendor_name());
            number.setText("GST Number: " + item.getGST());
            type.setText(item.getType_id());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item,pos,itemView);
                }
            });
        }
    }

}