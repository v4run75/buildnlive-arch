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
import buildnlive.com.arch.elements.ProjectEmployee;

public class ProjectEmployeeAdapter extends RecyclerView.Adapter<ProjectEmployeeAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(ProjectEmployee project, int pos, View view);
    }

    private final List<ProjectEmployee> items;
    private Context context;
    private final OnItemClickListener listener;

    public ProjectEmployeeAdapter(Context context, ArrayList<ProjectEmployee> users, OnItemClickListener listener) {
        this.items = users;
        this.context = context;
        this.listener = listener;
    }

    @Override
    public ProjectEmployeeAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_project_employee_item, parent, false);
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
        private ImageButton delete;

        public ViewHolder(View view) {
            super(view);
            name= view.findViewById(R.id.project_name);
            delete= view.findViewById(R.id.delete);

        }

        public void bind(final Context context, final ProjectEmployee item, final int pos, final OnItemClickListener listener) {
            name.setText(item.getName());
            delete.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    listener.onItemClick(item,pos,itemView);
                }
            });
        }
    }

}