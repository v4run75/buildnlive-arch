package buildnlive.com.arch.activities;

import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.HashMap;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.adapters.ProjectEmployeeAdapter;
import buildnlive.com.arch.adapters.ProjectSpinAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.ProjectEmployee;
import buildnlive.com.arch.elements.ProjectList;
import buildnlive.com.arch.fragments.VendorFragment;
import buildnlive.com.arch.utils.Config;
import io.realm.Realm;

public class ManageVendor extends AppCompatActivity {
    private App app;
    private static ArrayList<ProjectEmployee> projectList=new ArrayList<>();
    private static ArrayList<ProjectList> list=new ArrayList<>();
    private Realm realm;
    private Fragment fragment;
    private TextView edit, view,name,address,type_id,gst_no,no_content;
    Interfaces.SyncListener listener;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ProjectEmployeeAdapter adapter;
    private static String selection,type;
    private android.app.AlertDialog.Builder builder;
    private ProgressBar progress;
    private TextView hider,editButton;
    private CoordinatorLayout coordinatorLayout;
    private Context context;

    private ProjectEmployeeAdapter.OnItemClickListener listner = new ProjectEmployeeAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(final ProjectEmployee project, final int pos, View view) {
            console.log(project.getName());
            builder.setTitle("Delete");

            //Setting message manually and performing action on button click
            builder.setMessage("Are you sure?")
                    .setCancelable(false)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            try {
                                deleteRequest(project.getId());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            projectList.remove(pos);
                            adapter.notifyItemRemoved(pos);
                            adapter.notifyDataSetChanged();

                        }
                    })
                    .setNegativeButton("No", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            //  Action for 'NO' Button
                            dialog.cancel();

                        }
                    });
            //Creating dialog box
            android.app.AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("Delete");
            alert.show();



        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        refresh();
        setProjectList();
//        name.setEnabled(false);
//        address.setEnabled(false);
//        type_id.setEnabled(false);
//        gst_no.setEnabled(false);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vendor_profile);
        context=this;
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

        TextView toolbar_title=findViewById(R.id.toolbar_title);
        toolbar_title.setText("Employee Profile");


        app= ((App)getApplication());
        builder = new android.app.AlertDialog.Builder(this);
        coordinatorLayout=findViewById(R.id.coordinatorLayout);
        progress=findViewById(R.id.progress);
        hider=findViewById(R.id.hider);
        no_content=findViewById(R.id.no_content);
        name=findViewById(R.id.name);
        address=findViewById(R.id.address);
        type_id=findViewById(R.id.type);
        gst_no=findViewById(R.id.gst);
        editButton=findViewById(R.id.edit);
        fab=findViewById(R.id.add);
//        save=findViewById(R.id.save);
        recyclerView=findViewById(R.id.item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration decoration = new DividerItemDecoration(getApplicationContext(), LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(decoration);


        name.setText(VendorFragment.vendor_name_s);
        address.setText(VendorFragment.vendor_address_s);
        type_id.setText(VendorFragment.vendor_type_s);
        gst_no.setText(VendorFragment.vendor_gst_s);
//        contact.setText(EmployeeFragment.mobile_no_s);
//        name.setEnabled(false);
//        address.setEnabled(false);
//        type_id.setEnabled(false);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                LayoutInflater inflater = getLayoutInflater();
                View dialogView = inflater.inflate(R.layout.alert_dialog_vendor, null);
                AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.PinDialog);
                final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
                alertDialog.show();

                final EditText name = dialogView.findViewById(R.id.alert_vendor_name);
                final EditText address = dialogView.findViewById(R.id.alert_vendor_add);
                final EditText gst = dialogView.findViewById(R.id.alert_vendor_gst);
                final Spinner type = dialogView.findViewById(R.id.alert_vendor_type);
                name.setText(VendorFragment.vendor_name_s);
                address.setText(VendorFragment.vendor_address_s);
//                type_id.setText(VendorFragment.vendor_type_s);
                gst.setText(VendorFragment.vendor_gst_s);
                Button positive = dialogView.findViewById(R.id.positive);
                positive.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        try {
                            editRequest(name.getText().toString(),address.getText().toString(),gst_no.getText().toString(),type.getSelectedItem().toString());
                            final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                            imm.hideSoftInputFromWindow(alertDialog.getWindow().getDecorView().getWindowToken(), 0);
                            alertDialog.dismiss();

//                            if (!(name.getText().toString().equals("") || address.getText().toString().equals("")))
//                            {
//                                sendRequest(name.getText().toString(),address.getText().toString(),gst.getText().toString(),type.getSelectedItem().toString());
//                                alertDialog.dismiss();
//                            }
//                            else
//                                Toast.makeText(getContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
                        } catch (Exception e) {
                            Toast.makeText(context, "Fill data properly!", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                Button negative = dialogView.findViewById(R.id.negative);
                negative.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        final InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(alertDialog.getWindow().getDecorView().getWindowToken(), 0);
                        alertDialog.dismiss();
                    }
                });
//                editButton.setVisibility(View.GONE);
//                save.setVisibility(View.VISIBLE);
//                name.setEnabled(true);
//                address.setEnabled(true);
////                type_id.setEnabled(true);
//                type_id.setVisibility(View.INVISIBLE);
//                gst_no.setEnabled(true);

            }
        });
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                editButton.setVisibility(View.VISIBLE);
//                save.setVisibility(View.GONE);
//                name.setEnabled(false);
//                address.setEnabled(false);
//                type_id.setVisibility(View.VISIBLE);
//                type_id.setEnabled(false);
//                gst_no.setEnabled(false);
//                try {
//                    //TODO: Type ID Update
//                    editRequest(name.getText().toString(),address.getText().toString(),gst_no.getText().toString());
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//
//
//            }
//        });
//        refresh();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProject();
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return super.onSupportNavigateUp();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }
    private void addProject() {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dropdown_list, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this, R.style.PinDialog);
        final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
        alertDialog.show();
        final Spinner project_list = dialogView.findViewById(R.id.item_id);
//        final Spinner type_id_list = dialogView.findViewById(R.id.type_id);
        final ProjectSpinAdapter adapter = new ProjectSpinAdapter(getApplicationContext(),R.layout.custom_spinner, list);
        project_list.setAdapter(adapter);
        project_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                selection = adapter.getProjectID(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
//        type_id_list.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
//            @Override
//            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
//                type= type_id_list.getSelectedItem().toString();
//                console.log(type);
//            }
//
//            @Override
//            public void onNothingSelected(AdapterView<?> adapterView) {
//
//            }
//        });
        Button positive = dialogView.findViewById(R.id.positive);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    sendRequest(selection);
                    alertDialog.dismiss();
                } catch (Exception e) {

                }
            }
        });
        Button negative = dialogView.findViewById(R.id.negative);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }



    private void refresh() {
        projectList.clear();
        String requestUrl = Config.GET_PROJECT_LIST_VENDOR;
        requestUrl = requestUrl.replace("[0]", VendorFragment.vendor_id_s);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.error("Network request failed with error :" + error);
//                Toast.makeText(getApplicationContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
                Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("New Response"+response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        projectList.add(new ProjectEmployee().parseFromJSON(array.getJSONObject(i)));
                    }
                    if(projectList.isEmpty())
                    {
                        no_content.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        no_content.setVisibility(View.GONE);
                    }
                    adapter = new ProjectEmployeeAdapter(getApplicationContext(), projectList, listner);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void setProjectList() {
        list.clear();
        String requestUrl = Config.GET_PROJECTS;
        requestUrl = requestUrl.replace("[0]", App.userId);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.error("Network request failed with error :" + error);
//                Toast.makeText(getApplicationContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
                Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        list.add(new ProjectList().parseFromJSON(array.getJSONObject(i)));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void sendRequest(String pro_id) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        params.put("project_id",pro_id);
        params.put("vendor_id",VendorFragment.vendor_id_s);
//        params.put("type",type);
        console.log(params.toString());
        app.sendNetworkRequest(Config.ADD_VENDOR_TO_PROJECT, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(),"Error"+error,Toast.LENGTH_LONG).show();
                Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                if(response.equals("1")) {
//                    Toast.makeText(getApplicationContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    Snackbar.make(coordinatorLayout,"Request Generated",Snackbar.LENGTH_LONG).show();
                    refresh();
                }
                else{
//                    Toast.makeText(getApplicationContext(), "Check Your Network", Toast.LENGTH_SHORT).show();
                    Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG).show();

                }
            }
        });
    }

    private void deleteRequest(String pro_id) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        String requestUrl=Config.UPDATE_PROJECT_LIST;
        requestUrl = requestUrl.replace("[0]", pro_id);
        console.log(params.toString());
        app.sendNetworkRequest(requestUrl, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(),"Error"+error,Toast.LENGTH_LONG).show();
                Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                if(response.equals("1")) {
//                    Toast.makeText(getApplicationContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    Snackbar.make(coordinatorLayout,"Request Generated",Snackbar.LENGTH_LONG).show();
                    refresh();
                }
                else{
//                    Toast.makeText(getApplicationContext(), "Check Your Network", Toast.LENGTH_SHORT).show();
                    Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG).show();

                }
            }
        });
    }

    private void editRequest(String name,String address,String gst,String type_id) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        String requestUrl=Config.UPDATE_VENDOR_DETAILS;
        params.put("vendor_id",VendorFragment.vendor_id_s);
        params.put("vendor_name",name);
        params.put("vendor_add",address);
        params.put("type_id",type_id);
        params.put("gst",gst);
        console.log(params.toString());
        app.sendNetworkRequest(requestUrl, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(),"Error"+error,Toast.LENGTH_LONG).show();
                Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                if(response.equals("1")) {
//                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                    Snackbar.make(coordinatorLayout,"Saved",Snackbar.LENGTH_LONG).show();
                    refresh();
                }
                else{
//                    Toast.makeText(getApplicationContext(), "Check Your Network", Toast.LENGTH_SHORT).show();
                    Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG).show();

                }
            }
        });
    }



}