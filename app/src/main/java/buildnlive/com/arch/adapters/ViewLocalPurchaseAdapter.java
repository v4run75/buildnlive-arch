package buildnlive.com.arch.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import java.util.List;

import buildnlive.com.arch.R;
import buildnlive.com.arch.elements.Item;
import buildnlive.com.arch.elements.LocalPurchaseItem;
import buildnlive.com.arch.elements.ProjectList;

public class ViewLocalPurchaseAdapter extends RecyclerView.Adapter<ViewLocalPurchaseAdapter.ViewHolder> {
    private final List<LocalPurchaseItem> items;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(LocalPurchaseItem item, int pos, View view);
        void onButtonClick(LocalPurchaseItem item, int pos, View view);
    }

    public ViewLocalPurchaseAdapter(Context context, List<LocalPurchaseItem> users, OnItemClickListener listner) {
        this.items = users;
        this.context = context;
        this.listener=listner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_local_puchase_item, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position,listener);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        private TextView name,quantity,vendor,bill_no,amount;
        private Button billcopy;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            vendor = view.findViewById(R.id.vendor);
            quantity = view.findViewById(R.id.quantity);
            amount = view.findViewById(R.id.amount);
            bill_no = view.findViewById(R.id.billno);
            billcopy = view.findViewById(R.id.view_bill);
        }

        public void bind(final Context context, final LocalPurchaseItem item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getName());
            amount.setText("Total Amount: "+item.getTotal_amount());
            vendor.setText("Vendor: "+item.getVendor_name());
            bill_no.setText("Bill No: "+item.getBill_no());
            quantity.setText("Quantity: "+item.getQuantity());
            billcopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onButtonClick(item,pos,view);
                }
            });
        }
    }
}