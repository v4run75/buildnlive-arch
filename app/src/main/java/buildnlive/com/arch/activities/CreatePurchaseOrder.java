package buildnlive.com.arch.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.console;
import buildnlive.com.arch.fragments.CreatePurchaseOrderFragment;
import buildnlive.com.arch.fragments.LocalPurchaseFragment;
import buildnlive.com.arch.fragments.ViewLocalPurchaseFragment;
import io.realm.Realm;

public class CreatePurchaseOrder extends AppCompatActivity {
    private App app;
    private Realm realm;
    private Fragment fragment;
    private TextView edit;
    private ImageButton back;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_purchase_order);
        app = (App) getApplication();
        realm = Realm.getDefaultInstance();
        fragment = CreatePurchaseOrderFragment.newInstance();
        back =findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        changeScreen();
        edit = findViewById(R.id.edit);
        edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragment = CreatePurchaseOrderFragment.newInstance();
                changeScreen();
            }
        });
    }

    private void changeScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.site_content, fragment)
                .commit();
    }
}
