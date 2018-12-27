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

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import buildnlive.com.arch.R;
import buildnlive.com.arch.elements.ProjectList;

public class ProjectListAdapter extends RecyclerView.Adapter<ProjectListAdapter.ViewHolder> {
    public interface OnItemClickListener {
        void onItemClick(ProjectList project, int pos, View view);
        void onButtonClick(ProjectList project, int pos, View view);
    }
    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
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
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_project_list_item_new, parent, false);
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
        private TextView name,type,date;
        private ImageButton edit;
        private ImageView status;
        private LinearLayout linearLayout;

        public ViewHolder(View view) {
            super(view);
           name= view.findViewById(R.id.project_name);
           type = view.findViewById(R.id.project_type);
           date= view.findViewById(R.id.start_date);
           status= view.findViewById(R.id.status);
           edit = view.findViewById(R.id.edit);
           linearLayout = view.findViewById(R.id.content);

        }

        public void bind(final Context context, final ProjectList item, final int pos, final OnItemClickListener listener){
            name.setText(item.getName());
            type.setText(item.getType());
//            try {
//                SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMddHHmmss");
//                Date date_text = dateFormat.parse(item.getDate());
            date.setText(item.getDate());
//            }catch (ParseException e){}
            if(item.getStatus().equals("Started")){
                status.setImageResource(R.drawable.ic_status_green);
            }
            if(item.getStatus().equals("On Hold")){
                status.setImageResource(R.drawable.ic_status_orange);
            }
            if(item.getStatus().equals("Delete")){
                status.setImageResource(R.drawable.ic_status_red);
            }
            if(item.getStatus().equals("Archive")){
                status.setImageResource(R.drawable.ic_status_yellow);
            }


//            status.setText(item.getStatus());
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