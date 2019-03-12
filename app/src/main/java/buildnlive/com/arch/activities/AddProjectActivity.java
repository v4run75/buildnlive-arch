package buildnlive.com.arch.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;

import java.util.HashMap;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.ProjectList;
import buildnlive.com.arch.utils.Config;
import io.realm.Realm;

public class AddProjectActivity extends AppCompatActivity {
    private EditText name,about,site_location;
    private Spinner type;
    private App app;
    private Button submit;
    private CoordinatorLayout coordinatorLayout;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_project_window);
        coordinatorLayout = findViewById(R.id.coordinatorLayout);
        name=findViewById(R.id.name);
        type=findViewById(R.id.type);
        site_location=findViewById(R.id.site_location);
        about=findViewById(R.id.about);
        app=(App) getApplication();
        submit=findViewById(R.id.submit);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);


        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (!(name.getText().toString().equals("") || type.getSelectedItem().toString().equals("Select Type")))
                    {
                        sendRequest(name.getText().toString(),type.getSelectedItem().toString(),site_location.getText().toString(),about.getText().toString());
//                nothing.setVisibility(View.GONE);
//                alertDialog.dismiss();
                    }
                    else
                    {
                        Snackbar snackbar = Snackbar.make(coordinatorLayout, "Fill Data Properly", Snackbar.LENGTH_LONG);
                        snackbar.show();
                    }
                } catch (Exception e) {
                    Snackbar snackbar = Snackbar.make(coordinatorLayout, "Fill Data Properly", Snackbar.LENGTH_LONG);
                    snackbar.show();
                }
            }
        });


    }


    private void sendRequest(String project_name,String project_type,String site_location,String about) throws JSONException {
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id",App.userId);
        params.put("project_name", project_name);
        params.put("project_type", project_type);
        params.put("address", site_location);
        params.put("site_details", about);
        console.log("Res:" + params);

        app.sendNetworkRequest(Config.CREATE_PROJECT, com.android.volley.Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
//                projectList.clear();
//                progress.setVisibility(View.VISIBLE);
//                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
//                Toast.makeText(getApplicationContext(),"Something went wrong, Try again later",Toast.LENGTH_LONG).show();
                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Something went wrong, Try again later", Snackbar.LENGTH_LONG);
                snackbar.show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                console.log(response);
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
                    realm.close();
                }
                catch (JSONException e) {
                    e.printStackTrace();
                }
                Toast.makeText(getApplicationContext(),"Request Generated",Toast.LENGTH_LONG).show();
                finish();
//                Snackbar snackbar = Snackbar.make(coordinatorLayout, "Request Generated", Snackbar.LENGTH_LONG).setAction("DISMISS", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        finish();
//                    }
//                });
//                snackbar.show();
            }
        });
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

}
