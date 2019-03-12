package buildnlive.com.arch.fragments;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.adapters.CategorySpinAdapter;
import buildnlive.com.arch.adapters.EmployeeSpinAdapter;
import buildnlive.com.arch.adapters.WorkSpinAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Category;
import buildnlive.com.arch.elements.CreateWork;
import buildnlive.com.arch.elements.Employee;
import buildnlive.com.arch.utils.Config;

public class CreateWorkFragment extends Fragment {
    private static ArrayList<Category> categoryList=new ArrayList<>();
    private static ArrayList<CreateWork> workList=new ArrayList<>();
    private Button submit;
    private ProgressBar progress;
    private TextView hider;
    private boolean LOADING;
    private Spinner categorySpinner,workSpinner;
    private CategorySpinAdapter categoryAdapter;
    private WorkSpinAdapter workAdapter;
    private AlertDialog.Builder builder;
    private EditText work_name_text;
    private static String cat_id,work_id,workName;
    private boolean val=true;
    private CoordinatorLayout coordinatorLayout;

    private ArrayList<Employee> employeeList=new ArrayList<>();
    private String qty_unit,duration_type_text,assign_to_text;
    private EditText duration_text,quantity_text;
    private TextView start_date,end_date;
    private Spinner unitSpinner,duration_type_spinner,assign_to;
    private App app;
    private EmployeeSpinAdapter adapter;
    private Context context;
    private int mYear, mMonth, mDay;
    private Calendar c;

    public static CreateWorkFragment newInstance() {
        return new CreateWorkFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_create_work, container, false);

        return view;
    }

    @Override
    public void onStart() {
        super.onStart();

        setCategorySpinner();
        setEmployeeSpinner();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categoryList.clear();
        workList.clear();
        employeeList.clear();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getContext();
        TextView toolbar_title=getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Create Work & Activity");
        app= ((App)getActivity().getApplication());
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        progress=view.findViewById(R.id.progress);
        hider=view.findViewById(R.id.hider);
        submit = view.findViewById(R.id.submit);
        builder = new AlertDialog.Builder(getContext());
        categorySpinner=view.findViewById(R.id.category);
        workSpinner=view.findViewById(R.id.work);
        work_name_text=view.findViewById(R.id.work_name);

        duration_text=view.findViewById(R.id.duration);
        quantity_text=view.findViewById(R.id.quantity);
        start_date=view.findViewById(R.id.start_date);
        end_date=view.findViewById(R.id.end_date);
        unitSpinner=view.findViewById(R.id.quantity_unit);
        duration_type_spinner=view.findViewById(R.id.duration_type);
        assign_to=view.findViewById(R.id.assign_to);


        start_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, final int year,
                                                   int monthOfYear, final int dayOfMonth) {
                                monthOfYear=monthOfYear+1;
                                start_date.setText(year+"/"+monthOfYear+"/"+dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();
            }
        });



        end_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                c = Calendar.getInstance();
                mYear = c.get(Calendar.YEAR);
                mMonth = c.get(Calendar.MONTH);
                mDay = c.get(Calendar.DAY_OF_MONTH);
                DatePickerDialog datePickerDialog = new DatePickerDialog(context,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, final int year,
                                                   int monthOfYear, final int dayOfMonth) {
                                monthOfYear=monthOfYear+1;
                                end_date.setText(year+"/"+monthOfYear+"/"+dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
                datePickerDialog.show();

            }
        });

        unitSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                qty_unit=unitSpinner.getSelectedItem().toString();
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
        assign_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assign_to_text=adapter.getID(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat_id = categoryAdapter.getItem(i).getId();
                setWorkSpinner(cat_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        categoryAdapter=new CategorySpinAdapter(context, R.layout.custom_spinner,categoryList);
        categorySpinner.setAdapter(categoryAdapter);


        workSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                work_id= workAdapter.getItem(i).getId();
                if(workAdapter.getItem(i).getName().equals("Select Work")){
                    work_name_text.setEnabled(true);
                    work_name_text.setText("");
                }
                else{
                    work_name_text.setEnabled(false);
                    work_name_text.setText("Work Selected");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        workAdapter=new WorkSpinAdapter(context, R.layout.custom_spinner,workList);
        workSpinner.setAdapter(workAdapter);






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
                                    if(validate(cat_id,work_name_text.getText().toString(),work_id)) {
//                                        Snackbar.make(coordinatorLayout,"True",Snackbar.LENGTH_LONG).show();
                                        sendRequest(cat_id,work_id, work_name_text.getText().toString(), assign_to_text, duration_text.getText().toString(), duration_type_text, quantity_text.getText().toString()
                                                , qty_unit, start_date.getText().toString(), end_date.getText().toString());
                                    }
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
//                                workName=work_name.getText().toString();
//                                Intent intent= new Intent(getActivity(),WorkData.class);
//                                if(validate(cat_id,work_id,workName)){
//                                Bundle bundle= new Bundle();
//                                bundle.putString("cat_id",cat_id);
//                                bundle.putString("work_id",work_id);
//                                bundle.putString("work_name",workName);
//                                intent.putExtras(bundle);
//                                startActivity(intent);
//                                startActivityForResult(intent,100);
                                }
                            }).setNegativeButton("No", new DialogInterface.OnClickListener() {
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


            }
        });



    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if(requestCode==100){
//            console.log("OnActivityResult");
//            Fragment fragment = CreateActivityFragment.newInstance();
//            getActivity().getSupportFragmentManager().beginTransaction()
//                    .replace(R.id.site_content, fragment)
//                    .commit();
//        }
//    }

    private boolean validate(String category,String name,String item)
    {

        if(TextUtils.equals(category,"0")){
//            Toast.makeText(getContext(),"Please Select Category",Toast.LENGTH_LONG).show();
            final Snackbar snackbar= Snackbar.make(coordinatorLayout,"Please Select Category",Snackbar.LENGTH_INDEFINITE);
            snackbar.setAction("Dismiss", new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    snackbar.dismiss();
                }
            }).show();
            val=false;
        }
//        if((!TextUtils.isEmpty(name))&&(!TextUtils.equals(item,"0"))){
//
//            Snackbar.make(coordinatorLayout,"Either Choose Item from the list or Enter name",Snackbar.LENGTH_LONG).show();
//            val=false;
//
//        }
//        if(TextUtils.isEmpty(name)&&TextUtils.equals(item,"0")){
//
//            Snackbar.make(coordinatorLayout,"Either Choose Item from the list or Enter name",Snackbar.LENGTH_LONG).show();
//            val=false;
//
//        }




        return val;
    }




    private void setCategorySpinner() {
        App app= ((App)getActivity().getApplication());
        categoryList.clear();
        String requestURl= Config.GET_WORK_CATEGORY ;
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
                final Snackbar snackbar= Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG);
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
                        categoryList.add(new Category().parseFromJSON(array.getJSONObject(i)));
                    }
                    categoryAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void setWorkSpinner(String id) {
        App app= ((App)getActivity().getApplication());
        workList.clear();
        String requestURl= Config.GET_WORK ;
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
                final Snackbar snackbar= Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG);
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
                if(response.equals("-1")){
                    workSpinner.setVisibility(View.GONE);
                }
                else {
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        workList.add(new CreateWork().parseFromJSON(array.getJSONObject(i)));
                    }
                    workAdapter.notifyDataSetChanged();
                    workSpinner.setSelection(0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
            }
        });
    }
    private void sendRequest(String cat_id,String work_id, String work_name, String assign_to,
                             String duration, final String duration_type, String quantity, String quantity_type, final String start_date_text, String end_date_text) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        params.put("project_id", App.projectId);
        params.put("user_id", App.userId);
        params.put("work_id", work_id);
        params.put("cat_id", cat_id);
        if(work_name.equals("Work Selected")){
            params.put("work_name","");
        }
        else {
            params.put("work_name", work_name);
        }

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
        params.put("assign_to", assign_to);
//        params.put("duration", duration);
//        params.put("duration_type", duration_type);
//        params.put("qty", quantity);
//        params.put("qty_unit", quantity_type);

        params.put("start_date", start_date_text);
        params.put("end_date", end_date_text);
        console.log("Before Req"+params);

        app.sendNetworkRequest(Config.SAVE_WORK, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                final Snackbar snackbar= Snackbar.make(coordinatorLayout,"Check Network, Something went wrong",Snackbar.LENGTH_LONG);
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
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                if(response.equals("1")) {
                    categorySpinner.setSelection(0);
                    workSpinner.setVisibility(View.GONE);
                    work_name_text.setText("");
                    duration_text.setText("");
                    duration_type_spinner.setSelection(0);
                    quantity_text.setText("");
                    unitSpinner.setSelection(0);
                    start_date.setText("");
                    end_date.setText("");


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
                    final Snackbar snackbar=Snackbar.make(coordinatorLayout,"Check Your Network",Snackbar.LENGTH_INDEFINITE);
                    snackbar.setAction("Dismiss", new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            snackbar.dismiss();
                        }
                    });
                    snackbar.show();

                }
            }
        });
    }

    private void setEmployeeSpinner() {
        employeeList.clear();
        String requestURl= Config.VIEW_EMPLOYEE ;
        requestURl = requestURl.replace("[0]",App.userId);
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
                final Snackbar snackbar=Snackbar.make(coordinatorLayout,"Check Your Network,Something went Wrong",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                });
                snackbar.show();

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
