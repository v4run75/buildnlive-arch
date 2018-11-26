package buildnlive.com.arch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
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
import android.widget.Spinner;
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
import buildnlive.com.arch.adapters.EmployeeAdapter;
import buildnlive.com.arch.adapters.ProjectListAdapter;
import buildnlive.com.arch.adapters.VendorAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Employee;
import buildnlive.com.arch.elements.ProjectList;
import buildnlive.com.arch.elements.Vendor;
import buildnlive.com.arch.utils.Config;
import io.realm.Realm;
import io.realm.RealmResults;

public class EmployeeFragment extends Fragment {
    private RecyclerView recyclerView;
    private FloatingActionButton floatingActionButton;
    private ArrayList<Employee> employeeList = new ArrayList<>();
    private EmployeeAdapter adapter;
    private App app;
    private Fragment fragment;
    private android.app.AlertDialog.Builder builder;
    public static String name_s,email_s,profession_s,user_id_s,mobile_no_s;

    public static EmployeeFragment newInstance() {
        return new EmployeeFragment();
    }

    private EmployeeAdapter.OnItemClickListener listner=new EmployeeAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Employee emp, int pos, View view) {
            email_s=emp.getEmail_add();
            profession_s=emp.getProfession_name();
            user_id_s=emp.getUser_id();
            mobile_no_s=emp.getMobile_no();
            startActivity(new Intent(getActivity(),ManageEmployee.class));
        }
    };

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
        recyclerView=view.findViewById(R.id.items);
        app=(App) getActivity().getApplication();
        builder = new android.app.AlertDialog.Builder(getContext());
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(linearLayoutManager);
        DividerItemDecoration decoration = new DividerItemDecoration(getContext(), linearLayoutManager.getOrientation());
        recyclerView.addItemDecoration(decoration);
        adapter = new EmployeeAdapter(getContext(),employeeList,listner);
        recyclerView.setAdapter(adapter);
        floatingActionButton=view.findViewById(R.id.add);
        floatingActionButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                addEmployee();
            }
        });

    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    private void addEmployee() {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_employee, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.PinDialog);
        final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
        alertDialog.show();

        final EditText email = dialogView.findViewById(R.id.alert_employee_name);
        final EditText mob = dialogView.findViewById(R.id.alert_employee_type);
        final Spinner profession = dialogView.findViewById(R.id.profession_id);
        Button positive = dialogView.findViewById(R.id.positive);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (!(email.getText().toString().equals("") || mob.getText().toString().equals("")||profession.getSelectedItem().equals("Select ID")))
                    {
                        sendRequest(email.getText().toString(),mob.getText().toString(),profession.getSelectedItem().toString());
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
    private void sendRequest(String employee_email,String employee_mob,String profession_id) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        String requestURl= Config.CREATE_EMPLOYEE ;
        requestURl = requestURl.replace("[0]", App.userId);
        params.put("mobile_no",employee_mob);
        params.put("email_add", employee_email);
        params.put("profession_id", profession_id);
        console.log("Res:" + params);

        app.sendNetworkRequest(requestURl, com.android.volley.Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                employeeList.clear();
//                progress.setVisibility(View.VISIBLE);
//                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Something went wrong, Try again later",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
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
                        employeeList.add(new Employee().parseFromJSON(array.getJSONObject(i)));
                    }
                    adapter.notifyDataSetChanged();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
    private void refresh() {
        App app= ((App)getActivity().getApplication());
        employeeList.clear();
        String requestUrl = Config.VIEW_EMPLOYEE;
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
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        employeeList.add(new Employee().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");
                    adapter = new EmployeeAdapter(getContext(), employeeList, listner);
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
