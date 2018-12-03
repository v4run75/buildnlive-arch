package buildnlive.com.arch.activities;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
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
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.adapters.AddItemAdapter;
import buildnlive.com.arch.adapters.CategorySpinAdapter;
import buildnlive.com.arch.adapters.ItemListSpinAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Category;
import buildnlive.com.arch.elements.CreateWork;
import buildnlive.com.arch.elements.Item;
import buildnlive.com.arch.elements.ItemList;
import buildnlive.com.arch.utils.Config;

public class ManageItem extends AppCompatActivity {
    private ArrayList<ItemList> itemsList = new ArrayList<>();
    private App app;
    private static ArrayList<Category> categoryList=new ArrayList<>();
    private Button submit;
    private ProgressBar progress;
    private TextView hider;
    private boolean LOADING;
    private Spinner categorySpinner,itemSpinner;
    private CategorySpinAdapter categoryAdapter;
    private ItemListSpinAdapter itemAdapter;
    private AlertDialog.Builder builder;
    private EditText name,codeView,unitView;
    private static String cat_id,item_id,itemName,code,units;
    private boolean val=true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manage_item);
        setCategorySpinner();
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        app = (App) getApplication();
        progress=findViewById(R.id.progress);
        submit = findViewById(R.id.submit);
        builder = new AlertDialog.Builder(this);
        categorySpinner=findViewById(R.id.category);
        itemSpinner=findViewById(R.id.item);
        codeView=findViewById(R.id.code);
        unitView=findViewById(R.id.unit);
        name=findViewById(R.id.name);
        submit=findViewById(R.id.submit);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cat_id = categoryAdapter.getItem(i).getId();
                setItemSpinner(cat_id);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });


        categoryAdapter=new CategorySpinAdapter(getApplicationContext(), R.layout.custom_spinner,categoryList);
        categorySpinner.setAdapter(categoryAdapter);


        itemSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                item_id= itemAdapter.getItem(i).getId();
                code=itemAdapter.getItem(i).getCode();
                units=itemAdapter.getItem(i).getUnit();
                codeView.setText(code);
                unitView.setText(units);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        itemAdapter=new ItemListSpinAdapter(getApplicationContext(), R.layout.custom_spinner,itemsList);
        itemSpinner.setAdapter(itemAdapter);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Do You Want To Submit").setCancelable(false).setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {

                        if(validate(name.getText().toString(),cat_id,item_id,codeView.getText().toString(),unitView.getText().toString())){
                            try {
                                sendRequest(name.getText().toString(),cat_id,item_id,codeView.getText().toString(),unitView.getText().toString());
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }

                }).setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        dialogInterface.cancel();
                    }
                });

                AlertDialog alert = builder.create();
                alert.show();
            }
        });



//        back = findViewById(R.id.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
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
    private void setCategorySpinner() {
        categoryList.clear();
        String requestURl= Config.GET_ITEM_CATEGORY ;
        console.log(requestURl);
        app = (App) getApplication();
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
                console.log("Category: "+response);

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
    private void setItemSpinner(String id) {
        itemsList.clear();
        String requestURl= Config.GET_ITEM_LIST ;
        requestURl = requestURl.replace("[0]", id);
        console.log(requestURl);
        app = (App) getApplication();
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
                        itemsList.add(new ItemList().parseFromJSON(array.getJSONObject(i)));
                    }
                    itemAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }
    private void sendRequest(String name,String cat_id,String item_id,String code,String unit) throws JSONException {
        App app = ((App) getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        params.put("cat_id", cat_id);
        params.put("item_id", item_id);
        params.put("code", code);
        params.put("unit", unit);
        params.put("name", name);

        console.log("Res:" + params);
        app.sendNetworkRequest(Config.SAVE_ITEM, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
//                progress.setVisibility(View.VISIBLE);
//                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                Toast.makeText(getApplicationContext(),"Error:"+error,Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                if (response.equals("1")) {
                    Toast.makeText(getApplicationContext(), "Request Generated", Toast.LENGTH_SHORT).show();
//                    AddItemAdapter.ViewHolder.CHECKOUT=false;
                    finish();
                }
            }
        });
    }

    private boolean validate(String name,String category, String item, String code,String units)
    {

        if(TextUtils.equals(category,"0")){
            Toast.makeText(getApplicationContext(),"Please Select Category",Toast.LENGTH_LONG).show();
            val=false;
        }

        if((!TextUtils.isEmpty(name))&&(!TextUtils.equals(item,"0"))){

            Toast.makeText(getApplicationContext(),"Either Choose Item from the list or Enter name",Toast.LENGTH_LONG).show();
            val=false;

        }
        if(TextUtils.isEmpty(name)&&TextUtils.equals(item,"0")){

            Toast.makeText(getApplicationContext(),"Either Choose Item from the list or Enter name",Toast.LENGTH_LONG).show();
            val=false;

        }
        if(!(TextUtils.isEmpty(name))&&TextUtils.isEmpty(code)&&TextUtils.isEmpty(units))
        {
            unitView.setError("Enter Unit");
            codeView.setError("Enter Code");
            val=false;
        }
        return val;
    }


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.attendance_menu, menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        int id = item.getItemId();
//        switch (id){
//            case R.id.nav_home:
//                Toast.makeText(getApplicationContext(),"Item 1 Selected",Toast.LENGTH_LONG).show();
//                return true;
//            case R.id.nav_profile:
//                Toast.makeText(getApplicationContext(),"Item 2 Selected",Toast.LENGTH_LONG).show();
//                return true;
//            default:
//                return super.onOptionsItemSelected(item);
//        }
//    }
}
