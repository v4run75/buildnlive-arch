package buildnlive.com.arch.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.arch.R;
import buildnlive.com.arch.elements.Employee;
import buildnlive.com.arch.elements.LabourModel;

public class BreakUpAdapter extends RecyclerView.Adapter<BreakUpAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(LabourModel items, int pos, View view);
        void onItemCheck(boolean checked);
        void onItemInteract(int pos, int flag);

    }

    private final List<LabourModel> items;
    private Context context;
    private final OnItemClickListener listener;

    public BreakUpAdapter(Context context, ArrayList<LabourModel> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public BreakUpAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_labour_report_item, parent, false);
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
        private TextView name;
        private TextView quantity;

        public ViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.name);
            quantity= view.findViewById(R.id.quantity);

        }

        public void bind(final Context context, final LabourModel item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getName());
            quantity.setText(item.getQuantity());
        }
    }

}