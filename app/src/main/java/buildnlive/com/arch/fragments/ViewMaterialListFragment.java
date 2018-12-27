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
import buildnlive.com.arch.activities.ManageItem;
import buildnlive.com.arch.adapters.AddItemAdapter;
import buildnlive.com.arch.adapters.ItemListByUserAdapter;
import buildnlive.com.arch.adapters.ProjectListAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Item;
import buildnlive.com.arch.elements.ItemListByUser;
import buildnlive.com.arch.elements.ProjectList;
import buildnlive.com.arch.utils.Config;
import io.realm.Realm;
import io.realm.RealmResults;

public class ViewMaterialListFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ArrayList<ItemListByUser> itemList = new ArrayList<>();
    private ItemListByUserAdapter adapter;
    private App app;
    private Fragment fragment;
    private android.app.AlertDialog.Builder builder;
    private TextView nothing;
    private ProgressBar progress;
    private TextView hider;
    private CoordinatorLayout coordinatorLayout;

    public static ViewMaterialListFragment newInstance() {
        return new ViewMaterialListFragment();
    }

    private ItemListByUserAdapter.OnItemClickListener listener= new ItemListByUserAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(ItemListByUser project, int pos, View view) {

        }
    };
//
//    private ProjectListAdapter.OnItemClickListener listner=new ProjectListAdapter.OnItemClickListener() {
//        @Override
//        public void onItemClick(ProjectList project, int pos, View view) {
//            App.projectId = project.getId();
//            App.belongsTo = App.projectId + App.userId;
//            fragment= new HomeFragment();
//            getActivity().findViewById(R.id.notification).setVisibility(View.VISIBLE);
//            getActivity().findViewById(R.id.badge_notification).setVisibility(View.VISIBLE);
//            changeFragment();
//        }
//
//        @Override
//        public void onButtonClick(final ProjectList project, int pos, View view) {
//            LayoutInflater inflater = getLayoutInflater();
//            View dialogView = inflater.inflate(R.layout.alert_dialog_edit_project, null);
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.PinDialog);
//            final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
//            alertDialog.show();
//
//            final EditText name = dialogView.findViewById(R.id.alert_project_name);
//            final Spinner status = dialogView.findViewById(R.id.alert_project_status);
//            name.setText(project.getName());
//            Button positive = dialogView.findViewById(R.id.positive);
//            positive.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//                        if (!(name.getText().toString().equals("") || status.getSelectedItem().toString().equals("Select Status")))
//                        {
////                            editRequest(name.getText().toString(),status.getSelectedItem().toString(),project.getId());
//                            alertDialog.dismiss();
//                        }
//                        else
//                            Toast.makeText(getContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
//                    } catch (Exception e) {
//                        Toast.makeText(getContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//            Button negative = dialogView.findViewById(R.id.negative);
//            negative.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertDialog.dismiss();
//                }
//            });
//        }
//    };

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_material_list, container, false);;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        progress=view.findViewById(R.id.progress);
        hider=view.findViewById(R.id.hider);
        TextView toolbar_title=getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Add Item");

        Realm realm =Realm.getDefaultInstance();
        nothing = view.findViewById(R.id.no_content);
        recyclerView=view.findViewById(R.id.item);
        app=(App) getActivity().getApplication();
        builder = new android.app.AlertDialog.Builder(getContext());
        floatingActionButton=view.findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getActivity(),ManageItem.class));
            }
        });
//        refresh();
    }

    private void refresh() {
        App app= ((App)getActivity().getApplication());
        itemList.clear();
        String requestUrl = Config.GET_ITEM_BY_USER;
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
                Snackbar snackbar= Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
//                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        itemList.add(new ItemListByUser().parseFromJSON(array.getJSONObject(i)));
                    }
                    if(itemList.isEmpty()){
                        nothing.setVisibility(View.VISIBLE);
                    }
                    else
                    {
                        nothing.setVisibility(View.GONE);
                    }
                    console.log("data set changed");
                    LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
                    linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
                    recyclerView.setLayoutManager(linearLayoutManager);
                    DividerItemDecoration decoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
                    recyclerView.addItemDecoration(decoration);
                    adapter = new ItemListByUserAdapter(getContext(),itemList,listener);
                    recyclerView.setAdapter(adapter);


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
//    private void editRequest(String project_name,String project_status,String project_id) throws JSONException {
//        HashMap<String, String> params = new HashMap<>();
//        String requestUrl = Config.EDIT_PROJECT;
//        requestUrl = requestUrl.replace("[0]", App.userId).replace("[1]", project_id);
//        params.put("project_name", project_name);
//        params.put("project_status", project_status);
//        console.log("Res:" + params);
//
//        app.sendNetworkRequest(requestUrl, com.android.volley.Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
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
//                if(response.equals("1")){
//                    Toast.makeText(getContext(),"Request Generated",Toast.LENGTH_LONG).show();
//                    refresh();
//                }
//
//            }
//        });
//    }


}
