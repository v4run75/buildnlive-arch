package buildnlive.com.arch.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.arch.R;
import buildnlive.com.arch.elements.Comment;

public class TrailAdapter extends RecyclerView.Adapter<TrailAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(Comment project, int pos, View view);
        void onButtonClick(Comment project, int pos, View view);

    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    private final List<Comment> items;
    private Context context;
    private final OnItemClickListener listener;

    public TrailAdapter(Context context, ArrayList<Comment> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public TrailAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_parent_comment, parent, false);
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
        private TextView name,quantity,user,date,images;

        public ViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.parent_comment);
            quantity= view.findViewById(R.id.qty);
            user=view.findViewById(R.id.user);
            date=view.findViewById(R.id.date);
            images=view.findViewById(R.id.images);

        }

        public void bind(final Context context, final Comment item, final int pos, final OnItemClickListener listener) {
                name.setVisibility(View.VISIBLE);
                name.setText(item.getComment());
                quantity.setText(item.getQuantity());
                user.setText(item.getUser());
                date.setText(item.getDate());
                if(item.getImages().equals("1")){
                images.setVisibility(View.VISIBLE);
                images.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        listener.onButtonClick(item,pos,itemView);
                    }
                });
                }
                else if(item.getImages().equals("0")){
                    images.setVisibility(View.GONE);
                }
            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item,pos,itemView);
                }
            });
        }
    }

}