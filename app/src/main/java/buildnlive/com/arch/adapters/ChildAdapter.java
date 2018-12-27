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
import buildnlive.com.arch.elements.ChildComment;

public class ChildAdapter extends RecyclerView.Adapter<ChildAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(ChildComment project, int pos, View view);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }
    private final List<ChildComment> items;
    private Context context;
    private final OnItemClickListener listener;
    private String pid;

    public ChildAdapter(Context context, ArrayList<ChildComment> users, OnItemClickListener listener, String pid) {
        this.items = users;
        this.context = context;
        this.listener = listener;
        this.pid=pid;
    }

    @Override
    public ChildAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_child_comment, parent, false);
        return new ViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.bind(context, items.get(position), position, listener,pid);
    }


    @Override
    public int getItemCount() {
        return items.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        private TextView name,date,user;

        public ViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.child);
            user=view.findViewById(R.id.user);
            date=view.findViewById(R.id.date);

        }

        public void bind(final Context context, final ChildComment item, final int pos, final OnItemClickListener listener,String pid) {
            if (item.getParent_id().equals(pid)){
                name.setText(item.getComment());
                user.setText(item.getUser());
                date.setText(item.getDate());

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