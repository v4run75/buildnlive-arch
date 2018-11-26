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
import buildnlive.com.arch.elements.PaymentItem;
import buildnlive.com.arch.elements.ProjectList;

public class ViewPaymentAdapter extends RecyclerView.Adapter<ViewPaymentAdapter.ViewHolder> {
    private final List<PaymentItem> items;
    private Context context;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(PaymentItem item, int pos, View view);
        void onButtonClick(PaymentItem item, int pos, View view);
    }

    public ViewPaymentAdapter(Context context, List<PaymentItem> users, OnItemClickListener listner) {
        this.items = users;
        this.context = context;
        this.listener=listner;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_payment, parent, false);
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

        private TextView name,payee,payment_type,amount,comment,overheads;
        private Button billcopy;

        public ViewHolder(View view) {
            super(view);
            name = view.findViewById(R.id.name);
            payee = view.findViewById(R.id.payee);
            overheads = view.findViewById(R.id.overheads);
            amount = view.findViewById(R.id.amount);
            payment_type = view.findViewById(R.id.payment_type);
            comment = view.findViewById(R.id.comment);
            billcopy = view.findViewById(R.id.view_bill);
        }

        public void bind(final Context context, final PaymentItem item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getName());
            amount.setText("Amount: "+item.getTotal_amount());
            payee.setText("Payee: "+item.getPayee());
            overheads.setText("Overheads: "+item.getOverheads());
            payment_type.setText("Type: "+item.getPayment_type());
            comment.setText("Comment: "+item.getComments());
            billcopy.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onButtonClick(item,pos,view);
                }
            });
        }
    }
}