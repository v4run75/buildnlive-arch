package buildnlive.com.arch.activities;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.adapters.EmployeeAdapter;
import buildnlive.com.arch.adapters.EmployeeSpinAdapter;
import buildnlive.com.arch.adapters.ProjectSpinAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.CreateWork;
import buildnlive.com.arch.elements.Employee;
import buildnlive.com.arch.utils.Config;

public class WorkData extends AppCompatActivity {
    private ArrayList<Employee> employeeList=new ArrayList<>();
    private String cat_id,work_id,work_name,qty_unit,duration_type_text,assign_to_text;
    private EditText duration,quantity,start_date,end_date;
    private Button submit;
    private Spinner unit,duration_type,assign_to;
    private ImageButton back,start_date_button,end_date_button;
    private App app;
    private EmployeeSpinAdapter adapter;
    private Context context;
    private int mYear, mMonth, mDay, mHour, mMinute;
    private Calendar c;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_work_data);
        app= ((App)getApplication());
        context=this;
        duration=findViewById(R.id.duration);
        quantity=findViewById(R.id.quantity);
        start_date_button= findViewById(R.id.select_start_date);
        end_date_button= findViewById(R.id.select_end_date);
        start_date=findViewById(R.id.start_date);
        end_date=findViewById(R.id.end_date);
        unit=findViewById(R.id.quantity_unit);
        duration_type=findViewById(R.id.duration_type);
        submit=findViewById(R.id.submit);
        assign_to=findViewById(R.id.assign_to);
        back=findViewById(R.id.back);


        start_date_button.setOnClickListener(new View.OnClickListener() {
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
                                                      final int monthOfYear, final int dayOfMonth) {
                                    start_date.setText(year+"/"+monthOfYear+"/"+dayOfMonth);
                                }
                            }, mYear, mMonth, mDay);
                    datePickerDialog.show();

            }
        });

        end_date_button.setOnClickListener(new View.OnClickListener() {
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
                                                  final int monthOfYear, final int dayOfMonth) {
                                end_date.setText(year+"/"+monthOfYear+"/"+dayOfMonth);
                            }
                        }, mYear, mMonth, mDay);
                datePickerDialog.show();

            }
        });

   //        submit_act=findViewById(R.id.submit_activity);
//        submit_act.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Intent intent=new Intent();
//                setResult(100,intent);
//                finish();
//            }
//        });
        setEmployeeSpinner();

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
        assign_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                assign_to_text=adapter.getID(i);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    sendRequest(work_id,work_name,assign_to_text,duration.getText().toString(),duration_type_text,quantity.getText().toString()
                    ,qty_unit,start_date.getText().toString(),end_date.getText().toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
        
        
        Bundle bundle;
        Intent intent=getIntent();
        bundle=intent.getExtras();
        work_name=bundle.getString("work_name");
        work_id=bundle.getString("work_id");
        cat_id=bundle.getString("cat_id");

    }

//    project_id
//            user_id
//    work_id
//    work_name (in case does not choose work from dropdown)
//    assign_to
//    duration (float)
//    duration_type (days, hours)
//    qty (float)
//    qty_ unit (cm,m, feet)
//    start_date (only date)
//    end_date (only date)

    private void sendRequest(String work_id,String work_name,String assign_to,
                             String duration,String duration_type,String quantity,String quantity_type,String start_date,String end_date) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        params.put("project_id", App.projectId);
        params.put("user_id", App.userId);
        params.put("work_id", work_id);
        params.put("work_name", work_name);
        params.put("assign_to", assign_to);
        params.put("duration", duration);
        params.put("duration_type", duration_type);
        params.put("qty", quantity);
        params.put("qty_unit", quantity_type);
        params.put("start_date", start_date);
        params.put("end_date", end_date);
        console.log("Before Req"+params);

        app.sendNetworkRequest(Config.SAVE_WORK, 1, params, new Interfaces.NetworkInterfaceListener() {
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
                    finish();
                }
                else{
                    Toast.makeText(getApplicationContext(), "Check Your Network", Toast.LENGTH_SHORT).show();

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

            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
                Toast.makeText(getApplicationContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();

            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Work: "+response);

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        employeeList.add(new Employee().parseFromJSON(array.getJSONObject(i)));
                    }
                    adapter = new EmployeeSpinAdapter(getApplicationContext(),R.layout.custom_spinner, employeeList);
                    assign_to.setAdapter(adapter);
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
