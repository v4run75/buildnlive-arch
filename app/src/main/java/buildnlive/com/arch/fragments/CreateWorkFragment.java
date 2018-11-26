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
import buildnlive.com.arch.adapters.ItemSpinAdapter;
import buildnlive.com.arch.adapters.SingleImageAdapter;
import buildnlive.com.arch.adapters.WorkSpinAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Category;
import buildnlive.com.arch.elements.CreateWork;
import buildnlive.com.arch.elements.IndentItem;
import buildnlive.com.arch.elements.Packet;
import buildnlive.com.arch.utils.AdvancedRecyclerView;
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
    private EditText work_name;
    private static String cat_id,work_id,workName;
    private boolean val;

    public static CreateWorkFragment newInstance() {
        return new CreateWorkFragment();
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_create_work, container, false);
        setCategorySpinner();
        return view;
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categoryList.clear();
        workList.clear();
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView toolbar_title=getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Create Work & Activity");


        progress=view.findViewById(R.id.progress);
        submit = view.findViewById(R.id.submit);
        builder = new AlertDialog.Builder(getContext());
        categorySpinner=view.findViewById(R.id.category);
        workSpinner=view.findViewById(R.id.work);
        work_name=view.findViewById(R.id.work_name);


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


        categoryAdapter=new CategorySpinAdapter(getContext(), R.layout.custom_spinner,categoryList);
        categorySpinner.setAdapter(categoryAdapter);


        workSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                work_id= workAdapter.getItem(i).getId();
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        workAdapter=new WorkSpinAdapter(getContext(), R.layout.custom_spinner,workList);
        workSpinner.setAdapter(workAdapter);


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

//                builder.setMessage("Are you sure?") .setTitle("Work");
//
//                //Setting message manually and performing action on button click
//                builder.setMessage("Do you want to Submit?")
//                        .setCancelable(false)
//                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
                                workName=work_name.getText().toString();
//                                if(validate(cat_id,work_id,workName)){
                                Intent intent= new Intent(getActivity(),WorkData.class);
                                Bundle bundle= new Bundle();
                                bundle.putString("cat_id",cat_id);
                                bundle.putString("work_id",work_id);
                                bundle.putString("work_name",workName);
                                intent.putExtras(bundle);
                                startActivity(intent);
//                                startActivityForResult(intent,100);
//                                }
//                            }
//                        })
//                        .setNegativeButton("No", new DialogInterface.OnClickListener() {
//                            public void onClick(DialogInterface dialog, int id) {
//                                //  Action for 'NO' Button
//                                dialog.cancel();
//
//                            }
//                        });
//                //Creating dialog box
//                AlertDialog alert = builder.create();
//                //Setting the title manually
//                alert.setTitle("Are You Sure?");
//                alert.show();
//

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

    private boolean validate(String category, String item, String name)
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
                        workList.add(new CreateWork().parseFromJSON(array.getJSONObject(i)));
                    }
                    workAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }


}
