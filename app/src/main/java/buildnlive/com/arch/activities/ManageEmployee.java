package buildnlive.com.arch.activities;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
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
import buildnlive.com.arch.fragments.EmployeeFragment;
import buildnlive.com.arch.utils.Config;
import io.realm.Realm;

public class ManageEmployee extends AppCompatActivity {
    private App app;
    private static ArrayList<ProjectEmployee> projectList=new ArrayList<>();
    private static ArrayList<ProjectList> list=new ArrayList<>();
    private Realm realm;
    private Fragment fragment;
    private TextView edit, view,name,email,profession,contact,no_content;
    Interfaces.SyncListener listener;
    private ImageButton back,editButton,save;
    private FloatingActionButton fab;
    private RecyclerView recyclerView;
    private ProjectEmployeeAdapter adapter;
    private static String selection,type;
    private android.app.AlertDialog.Builder builder;

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
        name.setEnabled(false);
        email.setEnabled(false);
        profession.setEnabled(false);
        contact.setEnabled(false);
    }

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_employee_profile);
        builder = new android.app.AlertDialog.Builder(this);

        no_content=findViewById(R.id.no_content);
        app= ((App)getApplication());
        name=findViewById(R.id.name);
        email=findViewById(R.id.email);
        profession=findViewById(R.id.profession);
        contact=findViewById(R.id.mobile_no);
        back=findViewById(R.id.back);
        editButton=findViewById(R.id.edit);
        fab=findViewById(R.id.add);
        save=findViewById(R.id.save);
        recyclerView=findViewById(R.id.item);
        recyclerView.setLayoutManager(new LinearLayoutManager(getApplicationContext(), LinearLayoutManager.VERTICAL, false));




        email.setText(EmployeeFragment.email_s);
        profession.setText(EmployeeFragment.profession_s);
        contact.setText(EmployeeFragment.mobile_no_s);
        name.setEnabled(false);
        email.setEnabled(false);
        profession.setEnabled(false);
        contact.setEnabled(false);
        editButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editButton.setVisibility(View.GONE);
                save.setVisibility(View.VISIBLE);
                name.setEnabled(true);
                email.setEnabled(true);
//                profession.setEnabled(true);
                profession.setVisibility(View.INVISIBLE);
                contact.setEnabled(true);

            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editButton.setVisibility(View.VISIBLE);
                save.setVisibility(View.GONE);
                name.setEnabled(false);
                email.setEnabled(false);
                profession.setVisibility(View.VISIBLE);
                profession.setEnabled(false);
                contact.setEnabled(false);
                try {
                    editRequest(name.getText().toString(),email.getText().toString(),contact.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }
        });
//        refresh();
        setProjectList();
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addProject();
            }
        });
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
        String requestUrl = Config.GET_PROJECT_LIST;
        requestUrl = requestUrl.replace("[0]", EmployeeFragment.user_id_s);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
//                progress.setVisibility(View.VISIBLE);
//                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
                Toast.makeText(getApplicationContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("New Response"+response);
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
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
//                progress.setVisibility(View.VISIBLE);
//                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
                Toast.makeText(getApplicationContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
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
        params.put("user_id",EmployeeFragment.user_id_s);
//        params.put("type",type);
        console.log(params.toString());
        app.sendNetworkRequest(Config.ADD_USER_TO_PROJECT, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
//                progress.setVisibility(View.VISIBLE);
//                hider.setVisibility(View.VISIBLE);;
            }

            @Override
            public void onNetworkRequestError(String error) {
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Error"+error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                if(response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    refresh();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Check Your Network", Toast.LENGTH_SHORT).show();

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
//                progress.setVisibility(View.VISIBLE);
//                hider.setVisibility(View.VISIBLE);;
            }

            @Override
            public void onNetworkRequestError(String error) {
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Error"+error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                if(response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    refresh();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Check Your Network", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void editRequest(String name,String email,String contact ) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        String requestUrl=Config.UPDATE_USER_DETAILS;
        params.put("user_id",EmployeeFragment.user_id_s);
        params.put("name",name);
        params.put("email_add",email);
//        params.put("profession_id",profession);
        params.put("mobile_no",contact);
        console.log(params.toString());
        app.sendNetworkRequest(requestUrl, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
//                progress.setVisibility(View.VISIBLE);
//                hider.setVisibility(View.VISIBLE);;
            }

            @Override
            public void onNetworkRequestError(String error) {
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Error"+error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                if(response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Saved", Toast.LENGTH_SHORT).show();
                    refresh();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Check Your Network", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }



}