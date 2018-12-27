package buildnlive.com.arch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.activities.AddProjectActivity;
import buildnlive.com.arch.adapters.AddItemAdapter;
import buildnlive.com.arch.adapters.ProjectListAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Item;
import buildnlive.com.arch.elements.ProjectList;
import buildnlive.com.arch.utils.Config;
import io.realm.Realm;
import io.realm.RealmResults;

public class ProjectFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ArrayList<ProjectList> projectList = new ArrayList<>();
    private ProjectListAdapter adapter;
    private App app;
    private Fragment fragment;
    private android.app.AlertDialog.Builder builder;
    private TextView nothing,hider;
    private ImageView nothingToShow,gradient;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progress;

    public static ProjectFragment newInstance() {
        return new ProjectFragment();
    }

    private ProjectListAdapter.OnItemClickListener listner=new ProjectListAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(ProjectList project, int pos, View view) {
            App.projectId = project.getId();
            App.belongsTo = App.projectId + App.userId;
            fragment= new HomeFragment();
            getActivity().findViewById(R.id.notification).setVisibility(View.VISIBLE);
            changeFragment();
        }

        @Override
        public void onButtonClick(final ProjectList project, int pos, View view) {
            LayoutInflater inflater = getLayoutInflater();
            View dialogView = inflater.inflate(R.layout.alert_dialog_edit_project, null);
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.PinDialog);
            final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
            alertDialog.show();

            final EditText name = dialogView.findViewById(R.id.alert_project_name);
            final Spinner status = dialogView.findViewById(R.id.alert_project_status);
            name.setText(project.getName());
            Button positive = dialogView.findViewById(R.id.positive);
            positive.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (!(name.getText().toString().equals("") || status.getSelectedItem().toString().equals("Select Status")))
                        {
                            editRequest(name.getText().toString(),status.getSelectedItem().toString(),project.getId());
                            alertDialog.dismiss();
                        }
                        else
                            Toast.makeText(getContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
                    } catch (Exception e) {
                        Toast.makeText(getContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
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
    };

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.project_list_window, container, false);;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        refresh();
        TextView toolbar_title=getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("My Projects");

        Realm realm =Realm.getDefaultInstance();
        nothing = view.findViewById(R.id.no_content);
        progress=view.findViewById(R.id.progress);
        coordinatorLayout = view.findViewById(R.id.coordinatorLayout);
        hider=view.findViewById(R.id.hider);
        recyclerView=view.findViewById(R.id.project_list);
        nothingToShow=view.findViewById(R.id.nothingToShow);
        gradient=view.findViewById(R.id.gradient);
        app=(App) getActivity().getApplication();
//        projectList.clear();
//        final RealmResults<ProjectList> projects = realm.where(ProjectList.class).findAll();
//        projectList.addAll(projects);
//        if(projectList.isEmpty()){
//            nothing.setVisibility(View.VISIBLE);
//        }
        builder = new android.app.AlertDialog.Builder(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
        adapter = new ProjectListAdapter(getContext(),projectList,listner);
        recyclerView.setAdapter(adapter);
//
        floatingActionButton=view.findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                addProject();
                addProjectActivity();
            }
        });
    }
    private void addProjectActivity(){
        startActivity(new Intent(getActivity(),AddProjectActivity.class));
    }
    private void changeFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }
    private void refresh() {
        App app= ((App)getActivity().getApplication());
        projectList.clear();
        String requestUrl = Config.SYNC_PROJECTS;
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
//                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
                final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Check Network, Something went wrong", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Retry", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        refresh();
                        snackbar.dismiss();
                    }
                }).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
//                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
//                try {
//                    JSONArray array = new JSONArray(response);
//                    for (int i = 0; i < array.length(); i++) {
//                        projectList.add(new ProjectList().parseFromJSON(array.getJSONObject(i)));
//                    }
//                    if(projectList.isEmpty()){
//                        nothing.setVisibility(View.VISIBLE);
//                    }
//                    console.log("data set changed");
//                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
//                    recyclerView.setLayoutManager(linearLayoutManager);
//                    DividerItemDecoration decoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
//                    recyclerView.addItemDecoration(decoration);
//                    adapter = new ProjectListAdapter(getContext(),projectList,listner);
//                    recyclerView.setAdapter(adapter);
//
//
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
                try {
                    JSONArray array = new JSONArray(response);
                    Realm realm = Realm.getDefaultInstance();
                    for (int i = 0; i < array.length(); i++) {
                        final ProjectList project = new ProjectList().parseFromJSON(array.getJSONObject(i));
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(project);
                            }
                        });
                    }
                    final RealmResults<ProjectList> projects = realm.where(ProjectList.class).findAll();
                    projectList.addAll(projects);
                    adapter.notifyDataSetChanged();
                    if(projectList.isEmpty()){
//                        nothing.setVisibility(View.VISIBLE);
                        nothingToShow.setVisibility(View.VISIBLE);
                        gradient.setVisibility(View.VISIBLE);
                    }
                    else {
                        nothingToShow.setVisibility(View.GONE);
                        gradient.setVisibility(View.GONE);

                    }
                    realm.close();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void editRequest(String project_name,String project_status,String project_id) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        String requestUrl = Config.EDIT_PROJECT;
        requestUrl = requestUrl.replace("[0]", App.userId).replace("[1]", project_id);
        params.put("project_name", project_name);
        params.put("project_status", project_status);
        console.log("Res:" + params);

        app.sendNetworkRequest(requestUrl, com.android.volley.Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                projectList.clear();
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
//                Toast.makeText(getContext(),"Something went wrong, Try again later",Toast.LENGTH_LONG).show();
                final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Check Network, Something went wrong", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.log(response);

                if(response.equals("1")){
//                    Toast.makeText(getContext(),"Request Generated",Toast.LENGTH_LONG).show();
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Request Generated", Snackbar.LENGTH_LONG);
                    snackbar.show();
                    refresh();
                }

            }
        });
    }


}




//    private void addProject() {
//        LayoutInflater inflater = this.getLayoutInflater();
//        View dialogView = inflater.inflate(R.layout.alert_dialog_project, null);
//        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.PinDialog);
//        final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
//        alertDialog.show();
//
//        final EditText name = dialogView.findViewById(R.id.alert_project_name);
//        final Spinner type = dialogView.findViewById(R.id.alert_project_type);
//        Button positive = dialogView.findViewById(R.id.positive);
//        positive.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                try {
//                    if (!(name.getText().toString().equals("") || type.getSelectedItem().toString().equals("Select Type")))
//                    {
//                        sendRequest(name.getText().toString(),type.getSelectedItem().toString());
//                        nothing.setVisibility(View.GONE);
//                        alertDialog.dismiss();
//                    }
//                    else
//                        Toast.makeText(getContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
//                } catch (Exception e) {
//                    Toast.makeText(getContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });
//        Button negative = dialogView.findViewById(R.id.negative);
//        negative.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                alertDialog.dismiss();
//            }
//        });
//    }

//    private void sendRequest(String project_name,String project_type) throws JSONException {
//        HashMap<String, String> params = new HashMap<>();
//        params.put("user_id",App.userId);
//        params.put("project_name", project_name);
//        params.put("project_type", project_type);
//        console.log("Res:" + params);
//
//        app.sendNetworkRequest(Config.CREATE_PROJECT, com.android.volley.Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
//            @Override
//            public void onNetworkRequestStart() {
//                projectList.clear();
////                progress.setVisibility(View.VISIBLE);
////                hider.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onNetworkRequestError(String error) {
////                progress.setVisibility(View.GONE);
////                hider.setVisibility(View.GONE);
//                Toast.makeText(getContext(),"Something went wrong, Try again later",Toast.LENGTH_LONG).show();
//            }
//
//            @Override
//            public void onNetworkRequestComplete(String response) {
////                progress.setVisibility(View.GONE);
////                hider.setVisibility(View.GONE);
//                console.log(response);
////                try {
////                    JSONArray array = new JSONArray(response);
////                    for (int i = 0; i < array.length(); i++) {
////                        projectList.add(new ProjectList().parseFromJSON(array.getJSONObject(i)));
////                    }
////                    adapter.notifyDataSetChanged();
////                }
//                try {
//                    JSONArray array = new JSONArray(response);
//                    Realm realm = Realm.getDefaultInstance();
//                    for (int i = 0; i < array.length(); i++) {
//                        final ProjectList project = new ProjectList().parseFromJSON(array.getJSONObject(i));
//                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                realm.copyToRealmOrUpdate(project);
//                            }
//                        });
//                    }
//                    final RealmResults<ProjectList> projects = realm.where(ProjectList.class).findAll();
//                    projectList.addAll(projects);
//                    adapter.notifyDataSetChanged();
//                    if(projectList.isEmpty()){
//                        nothing.setVisibility(View.VISIBLE);
//                    }
//                    realm.close();
//                }
//                catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
