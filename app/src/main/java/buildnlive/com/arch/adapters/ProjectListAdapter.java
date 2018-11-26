package buildnlive.com.arch.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.arch.R;
import buildnlive.com.arch.elements.ProjectList;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(ProjectList project, int pos, View view);
        void onButtonClick(ProjectList project, int pos, View view);
    }

    private final List<ProjectList> items;
    private Context context;
    private final OnItemClickListener listener;

    public ProjectListAdapter(Context context, ArrayList<ProjectList> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ProjectListAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_project_list_item, parent, false);
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
        private TextView name,type,status;
        private ImageButton edit;
        private LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
           name= view.findViewById(R.id.project_name);
           type = view.findViewById(R.id.project_type);
           status= view.findViewById(R.id.project_status);
           edit = view.findViewById(R.id.edit);
           linearLayout = view.findViewById(R.id.content);

        }

        public void bind(final Context context, final ProjectList item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getName());
            type.setText(item.getType());
            status.setText(item.getStatus());
            edit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onButtonClick(item,pos,edit);
                }
            });
            linearLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item,pos,linearLayout);
                }
            });
        }
    }

}