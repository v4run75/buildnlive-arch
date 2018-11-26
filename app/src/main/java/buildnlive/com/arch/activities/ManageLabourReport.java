package buildnlive.com.arch.activities;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.adapters.AddItemAdapter;
import buildnlive.com.arch.adapters.BreakUpAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Item;
import buildnlive.com.arch.elements.LabourModel;
import buildnlive.com.arch.fragments.ViewLabourReportFragment;
import buildnlive.com.arch.utils.Config;

public class ManageLabourReport extends AppCompatActivity {
    private ImageButton back;
    private TextView name;
    private EditText date;
    private RecyclerView items;
    private BreakUpAdapter adapter;
    private String user,quantity;
    private static ArrayList<LabourModel> labourModelList=new ArrayList<>();
    private Context context;

    BreakUpAdapter.OnItemClickListener listener= new BreakUpAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(LabourModel items, int pos, View view) {

        }

        @Override
        public void onItemCheck(boolean checked) {

        }

        @Override
        public void onItemInteract(int pos, int flag) {

        }
    };

    @Override
    protected void onStart() {
        super.onStart();
        refresh();
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_labour_report);
        context=this;
        back=findViewById(R.id.back);
        name=findViewById(R.id.name);
        date=findViewById(R.id.date);
        items=findViewById(R.id.item);
        
        name.setText(ViewLabourReportFragment.name_s);
        date.setText(ViewLabourReportFragment.date_s);
        name.setEnabled(false);
        date.setEnabled(false);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void refresh() {
        App app= ((App) getApplication());
        labourModelList.clear();
        String requestUrl = Config.VIEW_DETAILED_LABOUR_REPORT;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", ViewLabourReportFragment.daily_report_id_s);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
//                progress.setVisibility(View.VISIBLE);
//                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                console.error("Network request failed with error :" + error);
                Toast.makeText(context, "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        labourModelList.add(new LabourModel().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");
                    adapter = new BreakUpAdapter(context, labourModelList, listener);
                    DividerItemDecoration dividerItemDecoration = new DividerItemDecoration(items.getContext(),LinearLayoutManager.VERTICAL);
                    items.addItemDecoration(dividerItemDecoration);
                    items.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false));
                    items.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

}
