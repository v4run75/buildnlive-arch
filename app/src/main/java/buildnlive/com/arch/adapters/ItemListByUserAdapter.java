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
import buildnlive.com.arch.elements.ItemListByUser;

public class ItemListByUserAdapter extends RecyclerView.Adapter<ItemListByUserAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(ItemListByUser project, int pos, View view);
    }

    private final List<ItemListByUser> items;
    private Context context;
    private final OnItemClickListener listener;

    public ItemListByUserAdapter(Context context, ArrayList<ItemListByUser> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ItemListByUserAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_material_list, parent, false);
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
        private TextView name,category,unit,code;

        public ViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.name);
            code= view.findViewById(R.id.code);
            unit= view.findViewById(R.id.unit);
            category= view.findViewById(R.id.category);

        }

        public void bind(final Context context, final ItemListByUser item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getName());
            code.setText(item.getCode());
            unit.setText(item.getUnit());
            category.setText(item.getCategory());
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item,pos,itemView);
                }
            });
        }
    }

}