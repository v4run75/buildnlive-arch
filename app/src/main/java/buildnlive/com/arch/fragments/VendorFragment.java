package buildnlive.com.arch.fragments;

import android.content.Context;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
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
import buildnlive.com.arch.activities.ManageEmployee;
import buildnlive.com.arch.activities.ManageVendor;
import buildnlive.com.arch.adapters.VendorAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Vendor;
import buildnlive.com.arch.utils.Config;
import io.realm.Realm;

public class VendorFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Vendor> vendorList = new ArrayList<>();
    private VendorAdapter adapter;
    private App app;
    private Fragment fragment;
    private TextView no_content;
    private android.app.AlertDialog.Builder builder;
    private ProgressBar progress;
    private TextView hider;
    private CoordinatorLayout coordinatorLayout;
    public static String vendor_name_s,vendor_type_s,vendor_id_s,vendor_address_s,vendor_gst_s;

    public static VendorFragment newInstance() {
        return new VendorFragment();
    }

    private VendorAdapter.OnItemClickListener listner=new VendorAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Vendor vendor, int pos, View view) {
            vendor_name_s=vendor.getVendor_name();
            vendor_address_s=vendor.getVendor_add();
            vendor_type_s=vendor.getType_id();
            vendor_id_s=vendor.getVendor_id();
            vendor_gst_s=vendor.getGST();
            startActivity(new Intent(getActivity(),ManageVendor.class));

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
        View view = inflater.inflate(R.layout.fragment_employee, container, false);;
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Realm realm =Realm.getDefaultInstance();
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        progress=view.findViewById(R.id.progress);
        hider=view.findViewById(R.id.hider);

        no_content=view.findViewById(R.id.no_content);
        recyclerView=view.findViewById(R.id.items);
        app=(App) getActivity().getApplication();
        builder = new android.app.AlertDialog.Builder(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
        adapter = new VendorAdapter(getContext(),vendorList,listner);
        recyclerView.setAdapter(adapter);
        floatingActionButton=view.findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addVendor();
            }
        });
    }

    private void addVendor() {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_vendor, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.PinDialog);
        final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
        alertDialog.show();

        final EditText name = dialogView.findViewById(R.id.alert_vendor_name);
        final EditText address = dialogView.findViewById(R.id.alert_vendor_add);
        final EditText gst = dialogView.findViewById(R.id.alert_vendor_gst);
        final Spinner type = dialogView.findViewById(R.id.alert_vendor_type);
        Button positive = dialogView.findViewById(R.id.positive);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!(name.getText().toString().equals("") || address.getText().toString().equals("")))
                    {
                        sendRequest(name.getText().toString(),address.getText().toString(),gst.getText().toString(),type.getSelectedItem().toString());
                        final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        imm.hideSoftInputFromWindow(alertDialog.getWindow().getDecorView().getWindowToken(), 0);

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
                final InputMethodManager imm = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(alertDialog.getWindow().getDecorView().getWindowToken(), 0);
                alertDialog.dismiss();
            }
        });
    }
    private void sendRequest(String vendor_name,String vendor_address,String gst,String vendor_type) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        String requestURl= Config.CREATE_VENDOR ;
        requestURl = requestURl.replace("[0]", App.userId);
        params.put("vendor_name",vendor_name);
        params.put("vendor_add", vendor_address);
        params.put("gst",gst);
        params.put("type_id", vendor_type);
        console.log("Res:" + params);

        app.sendNetworkRequest(requestURl, com.android.volley.Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                vendorList.clear();
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
//                Toast.makeText(getContext(),"Something went wrong, Try again later",Toast.LENGTH_LONG).show();
                Snackbar snackbar= Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.log(response);
//                try {
//                    JSONArray array = new JSONArray(response);
//                    for (int i = 0; i < array.length(); i++) {
//                        projectList.add(new ProjectList().parseFromJSON(array.getJSONObject(i)));
//                    }
//                    adapter.notifyDataSetChanged();
//                }
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        vendorList.add(new Vendor().parseFromJSON(array.getJSONObject(i)));
                    }
                    adapter.notifyDataSetChanged();
                    if(vendorList.isEmpty()){
                        no_content.setVisibility(View.VISIBLE);
                        no_content.setText("No Vendor");
                    }
                    else
                    {
                        no_content.setVisibility(View.GONE);
                    }
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void refresh() {
        App app= ((App)getActivity().getApplication());
        vendorList.clear();
        String requestUrl = Config.VIEW_VENDOR;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", "");
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
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        vendorList.add(new Vendor().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");
                    if(vendorList.isEmpty()){
                        no_content.setVisibility(View.VISIBLE);
                        no_content.setText("No Vendor");
                    }
                    else
                    {
                        no_content.setVisibility(View.GONE);
                    }
                    adapter = new VendorAdapter(getContext(), vendorList, listner);
                    recyclerView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }

    private void changeFragment() {
        getActivity().getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.content_frame, fragment)
                .commit();
    }


}
