package buildnlive.com.arch.fragments;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.text.TextUtils;
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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.adapters.CustomWorkSpinAdapter;
import buildnlive.com.arch.adapters.EmployeeSpinAdapter;
import buildnlive.com.arch.adapters.CustomActivitySpinAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.CustomActivity;
import buildnlive.com.arch.elements.CustomWork;
import buildnlive.com.arch.elements.Employee;
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
    private static String project_work_list_id,work_id,activityName,master_work_id,activity_id;
    private boolean val=true;
    private String qty_unit,duration_type_text,assign_to_text;
    private EditText duration_text,quantity_text,activityName_text;
    private Spinner unit,duration_type_spinner,assign_to;
    private CoordinatorLayout coordinatorLayout;
    private Context context;



    public static CreateActivityFragment newInstance() {
        return new CreateActivityFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_create_activity, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        setWorkSpinner();
        setEmployeeSpinner();
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
        context=getContext();
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        progress=view.findViewById(R.id.progress);
        hider=view.findViewById(R.id.hider);

        duration_text=view.findViewById(R.id.duration);
        quantity_text=view.findViewById(R.id.quantity);
        unit=view.findViewById(R.id.quantity_unit);
        duration_type_spinner=view.findViewById(R.id.duration_type);
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


        workSpinAdapter=new CustomWorkSpinAdapter(context, R.layout.custom_spinner,customWorkList);
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
        duration_type_spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                duration_type_text=duration_type_spinner.getSelectedItem().toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        activitySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                activity_id= activityAdapter.getItem(i).getId();
                if(activityAdapter.getItem(i).getName().equals("Select Activities")){
                    work_name.setEnabled(true);
                    work_name.setText("");
                }
                else{
                    work_name.setEnabled(false);
                    work_name.setText("Activity Selected");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        activityAdapter=new CustomActivitySpinAdapter(context, R.layout.custom_spinner,activityList);
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
                                    sendRequest(activity_id,work_name.getText().toString(),project_work_list_id,assign_to_text,duration_text.getText().toString(),duration_type_text,quantity_text.getText().toString()
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
            final Snackbar snackbar= Snackbar.make(coordinatorLayout,"Please Select Category",Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            }).show();
            val=false;
        }

        if((!TextUtils.equals(name,"Activity Selected"))&&(!TextUtils.equals(item,"0"))){

            final Snackbar snackbar=Snackbar.make(coordinatorLayout,"Either Choose Item from the list or Enter name",Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            }).show();
            val=false;

        }
        if(TextUtils.equals(name,"Activity Selected")&&TextUtils.equals(item,"0")){

            final Snackbar snackbar=Snackbar.make(coordinatorLayout,"Either Choose Item from the list or Enter name",Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            }).show();
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
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.error("Network request failed with error :" + error);
//                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
                final Snackbar snackbar= Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Category: "+response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
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
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.error("Network request failed with error :" + error);
//                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
                final Snackbar snackbar= Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Work: "+response);

                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                activitySpinner.setVisibility(View.VISIBLE);
                if(response.equals("-1"))
                {
                    activitySpinner.setVisibility(View.GONE);
                }
                else {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        activityList.add(new CustomActivity().parseFromJSON(array.getJSONObject(i)));
                    }
                    activityAdapter.notifyDataSetChanged();
                    activitySpinner.setSelection(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            }
        });
    }


    private void sendRequest(String activity_id,String activityName, String project_work_list_id, String assign_to,
                             final String duration, final String duration_type, final String quantity, final String quantity_type) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id",App.userId);
        params.put("project_id", App.projectId);
        params.put("project_work_list_id", project_work_list_id);
        params.put("activity_id", activity_id);
        if(TextUtils.equals(activityName,"Activity Selected")) {
            params.put("activity_name", "");
        }
        else{
            params.put("activity_name", activityName);
        }
        params.put("assign_to", assign_to);

        if(TextUtils.equals(duration_type,"Select Duration Type")||TextUtils.equals(duration,"")){
            params.put("duration", "");
            params.put("duration_type", "");
            }
            else {
            params.put("duration", duration);
            params.put("duration_type", duration_type);

        }
        if (TextUtils.equals(quantity_type,"Select Unit")||TextUtils.equals(quantity,"")) {
            params.put("qty", "");
            params.put("qty_unit", "");
        }
        else {
            params.put("qty", quantity);
            params.put("qty_unit", quantity_type);
        }

        console.log("Before Req"+params);

        App app= ((App)getActivity().getApplication());

        app.sendNetworkRequest(Config.SAVE_ACTIVITES, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
//                Toast.makeText(getContext(),"Error"+error,Toast.LENGTH_LONG).show();
                final Snackbar snackbar= Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);

                if(response.equals("1")) {

                    progress.setVisibility(View.GONE);
                    hider.setVisibility(View.GONE);

                    customWorkSpinner.setSelection(0);
                    activitySpinner.setVisibility(View.GONE);
                    work_name.setText("");
                    duration_text.setText("");
                    quantity_text.setText("");
                    duration_type_spinner.setSelection(0);
                    unit.setSelection(0);

//                    Toast.makeText(getContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar=Snackbar.make(coordinatorLayout,"Request Generated",Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();
                }
                else{
//                    Toast.makeText(getContext(), "Check Your Network", Toast.LENGTH_SHORT).show();
                    final Snackbar snackbar= Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    }).show();

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
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.error("Network request failed with error :" + error);
//                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
                final Snackbar snackbar= Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Work: "+response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        employeeList.add(new Employee().parseFromJSON(array.getJSONObject(i)));
                    }
                    adapter = new EmployeeSpinAdapter(context,R.layout.custom_spinner, employeeList);
                    assign_to.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
}
