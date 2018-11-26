package buildnlive.com.arch.activities;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import java.util.HashMap;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.console;
import buildnlive.com.arch.utils.Config;

public class RegisterActivity extends AppCompatActivity {
    private App app;
    private EditText mobile,password,email,company,repassword;
    private Button register;
    private ProgressBar progress;
    private TextView hider,sign_in;
    private AlertDialog.Builder builder;
    private Context context;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        mobile = findViewById(R.id.mobile);
        email = findViewById(R.id.email);
        company = findViewById(R.id.company_name);
        password = findViewById(R.id.pass);
        repassword = findViewById(R.id.repass);
        register = findViewById(R.id.register);
        sign_in= findViewById(R.id.screen_login);
        app = (App) getApplication();
        context=this;
        builder = new AlertDialog.Builder(context);
        progress= findViewById(R.id.progress);
        hider = findViewById(R.id.hider);
        sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                builder.setMessage("Are you sure?") .setTitle("Register?");

                //Setting message manually and performing action on button click
                builder.setMessage("Do you want to Submit?")
                        .setCancelable(false)
                        .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                if(validate(mobile.getText().toString(),email.getText().toString(),company.getText().toString(),password.getText().toString(),repassword.getText().toString())){
                                    register(mobile.getText().toString(),email.getText().toString(),company.getText().toString(),password.getText().toString());
                                }
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
                alert.setTitle("Register");
                alert.show();


            }
        });
    }
    public boolean validate(String mob,String email_add,String comp,String pass,String repass)
    {   Boolean val=true;

        if(mob.length()!=10){
            mobile.setError("Enter Correct Phone Number");
            val=false;
        }
        if (TextUtils.isEmpty(comp))
        {
            company.setError("Enter Company");
            val=false;
        }
        if (TextUtils.isEmpty(email_add))
        {
            email.setError("Enter Email");
            val=false;
        }
        if (TextUtils.isEmpty(pass))
        {
            password.setError("Enter Password");
            val=false;
        }
        if (TextUtils.isEmpty(repass))
        {
            repassword.setError("Enter Password");
            val=false;
        }
        if(!(pass.equals(repass))){
            repassword.setError("Password doesn't match");
            val=false;
        }
        return val;
    }

    private void register(String mob,String email_add,String comp,String pass) {
        progress.setVisibility(View.VISIBLE);
        hider.setVisibility(View.VISIBLE);
        String resetPass = Config.SIGN_UP;
        HashMap<String, String> params = new HashMap<>();
        params.put("mobile_no", mob);
        params.put("email_add", email_add);
        params.put("password", pass);
        params.put("company_name", comp);
        app.sendNetworkRequest(resetPass, Request.Method.POST, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
                Toast.makeText(context, "Check Internet Connection", Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log("Response:" + response);
                if (response.equals("0")) {
                    Toast.makeText(context, "User Does Not Exist", Toast.LENGTH_LONG).show();
                } else if(response.equals("1")) {
                    Toast.makeText(context,"New password has been sent to your email",Toast.LENGTH_LONG).show();
                    finish();
                }
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
            }
        });
    }
}
