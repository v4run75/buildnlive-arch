package buildnlive.com.arch.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.widget.LinearLayoutManager;
import android.text.TextUtils;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

import buildnlive.com.arch.App;
import buildnlive.com.arch.BuildConfig;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.activities.WorkData;
import buildnlive.com.arch.adapters.CategorySpinAdapter;
import buildnlive.com.arch.adapters.CustomWorkSpinAdapter;
import buildnlive.com.arch.adapters.EmployeeSpinAdapter;
import buildnlive.com.arch.adapters.ItemSpinAdapter;
import buildnlive.com.arch.adapters.SingleImageAdapter;
import buildnlive.com.arch.adapters.CustomActivitySpinAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Category;
import buildnlive.com.arch.elements.CustomActivity;
import buildnlive.com.arch.elements.CustomWork;
import buildnlive.com.arch.elements.Employee;
import buildnlive.com.arch.elements.IndentItem;
import buildnlive.com.arch.elements.Packet;
import buildnlive.com.arch.utils.AdvancedRecyclerView;
import buildnlive.com.arch.utils.Config;

public class CreateActivityFragment extends Fragment {
    private static ArrayList<CustomWork> customWorkList=new ArrayList<>();
    private static ArrayList<CustomActivity> activityList=new ArrayList<>();
    private ArrayList<Employee> employeeList=new ArrayList<>();
    private Button submit;
    private ProgressBar progress;
    private TextView hider;
    private boolean LOADING;
    private Spinner customWorkSpinner,activitySpinner;
    private CustomWorkSpinAdapter workSpinAdapter;
    private CustomActivitySpinAdapter activityAdapter;
    private EmployeeSpinAdapter adapter;
    private AlertDialog.Builder builder;
    private EditText work_name;
    private static String project_work_list_id,work_id,workName,master_work_id,activity_id;
    private boolean val=true;
    private String qty_unit,duration_type_text,assign_to_text;
    private EditText duration,quantity,start_date,end_date;
    private Spinner unit,duration_type,assign_to;


    public static CreateActivityFragment newInstance() {
        return new CreateActivityFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_create_activity, container, false);
        setWorkSpinner();
        setEmployeeSpinner();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        customWorkList.clear();
        activityList.clear();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView toolbar_title=getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Create Work & Activity");

        duration=view.findViewById(R.id.duration);
        quantity=view.findViewById(R.id.quantity);
        unit=view.findViewById(R.id.quantity_unit);
        duration_type=view.findViewById(R.id.duration_type);
        assign_to=view.findViewById(R.id.assign_to);

        progress=view.findViewById(R.id.progress);
        submit = view.findViewById(R.id.submit);
        builder = new AlertDialog.Builder(getContext());
        customWorkSpinner=view.findViewById(R.id.category);
        activitySpinner=view.findViewById(R.id.work);
        work_name=view.findViewById(R.id.work_name);


        customWorkSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                project_work_list_id = workSpinAdapter.getItem(i).getProjectWorkListId();
                master_work_id= workSpinAdapter.getItem(i).getMasterWorkId();
                work_id=workSpinAdapter.getItem(i).getId();
                setActivitySpinner(work_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        workSpinAdapter=new CustomWorkSpinAdapter(getContext(), R.layout.custom_spinner,customWorkList);
        customWorkSpinner.setAdapter(workSpinAdapter);

        unit.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                qty_unit=unit.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        duration_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                duration_type_text=duration_type.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity_id= activityAdapter.getItem(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        activityAdapter=new CustomActivitySpinAdapter(getContext(), R.layout.custom_spinner,activityList);
        activitySpinner.setAdapter(activityAdapter);

        assign_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assign_to_text=adapter.getID(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);

        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }





        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                builder.setMessage("Are you sure?") .setTitle("Work");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                try {
                                    if(validate(work_id,activity_id,work_name.getText().toString()))
                                    sendRequest(activity_id,project_work_list_id,assign_to_text,duration.getText().toString(),duration_type_text,quantity.getText().toString()
                                            ,qty_unit);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

//                workName=work_name.getText().toString();
//                                if(validate(cat_id,work_id,workName)){
//                Intent intent= new Intent(getActivity(),WorkData.class);
//                Bundle bundle= new Bundle();
//                bundle.putString("cat_id",cat_id);
//                bundle.putString("work_id",work_id);
//                bundle.putString("work_name",workName);
//                intent.putExtras(bundle);
//                startActivity(intent);
//                                }
                            }
                        })
                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                //  Action for 'NO' Button
                                dialog.cancel();

                            }
                        });
                //Creating dialog box
                AlertDialog alert = builder.create();
                //Setting the title manually
                alert.setTitle("Are You Sure?");
                alert.show();
//

            }
        });



    }

    private boolean validate(String category,String item,String name)
    {

        if(TextUtils.equals(category,"0")){
            Toast.makeText(getContext(),"Please Select Category",Toast.LENGTH_LONG).show();
            val=false;
        }

        if((!TextUtils.isEmpty(name))&&(!TextUtils.equals(item,"0"))){

            Toast.makeText(getContext(),"Either Choose Item from the list or Enter name",Toast.LENGTH_LONG).show();
            val=false;

        }
        if(TextUtils.isEmpty(name)&&TextUtils.equals(item,"0")){

            Toast.makeText(getContext(),"Either Choose Item from the list or Enter name",Toast.LENGTH_LONG).show();
            val=false;

        }
//        if(TextUtils.equals(duration,"Select Duration")||TextUtils.equals(unit,"Select Unit")){
//            val=false;
//        }
        return val;
    }
//
//
//
//
    private void setWorkSpinner() {
        App app= ((App)getActivity().getApplication());
        customWorkList.clear();
        String requestURl= Config.GET_CUSTOM_WORK ;
        console.log(requestURl);
        app.sendNetworkRequest(requestURl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Category: "+response);

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        customWorkList.add(new CustomWork().parseFromJSON(array.getJSONObject(i)));
                    }
                    workSpinAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setActivitySpinner(String id) {
        App app= ((App)getActivity().getApplication());
        activityList.clear();
        String requestURl= Config.GET_CUSTOM_ACTIIVITES ;
        requestURl = requestURl.replace("[0]", id);
        console.log(requestURl);
        app.sendNetworkRequest(requestURl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Work: "+response);

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        activityList.add(new CustomActivity().parseFromJSON(array.getJSONObject(i)));
                    }
                    activityAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


    private void sendRequest(String activity_id,String project_work_list_id,String assign_to,
                             String duration,String duration_type,String quantity,String quantity_type) throws JSONException {
        HashMap<String, String> params = new HashMap<>();

        params.put("project_id", App.projectId);
        params.put("project_work_list_id", project_work_list_id);
        params.put("activity_id", activity_id);
        params.put("assign_to", assign_to);
        params.put("duration", duration);
        params.put("duration_type", duration_type);
        params.put("qty", quantity);
        params.put("qty_unit", quantity_type);

        console.log("Before Req"+params);

        App app= ((App)getActivity().getApplication());

        app.sendNetworkRequest(Config.SAVE_ACTIVITES, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
//                progress.setVisibility(View.VISIBLE);
//                hider.setVisibility(View.VISIBLE);;
            }

            @Override
            public void onNetworkRequestError(String error) {
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Error"+error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                if(response.equals("1")) {
                    Toast.makeText(getContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    getActivity().finish();
                }
                else{
                    Toast.makeText(getContext(), "Check Your Network", Toast.LENGTH_SHORT).show();

                }
            }
        });
    }

    private void setEmployeeSpinner() {
        employeeList.clear();
        String requestURl= Config.VIEW_EMPLOYEE ;
        requestURl = requestURl.replace("[0]",App.userId);
        console.log(requestURl);
        App app= ((App)getActivity().getApplication());

        app.sendNetworkRequest(requestURl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Work: "+response);

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        employeeList.add(new Employee().parseFromJSON(array.getJSONObject(i)));
                    }
                    adapter = new EmployeeSpinAdapter(getContext(),R.layout.custom_spinner, employeeList);
                    assign_to.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
