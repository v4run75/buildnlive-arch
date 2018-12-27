package buildnlive.com.arch.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.widget.TextView;

import org.w3c.dom.Text;

import buildnlive.com.arch.App;
import buildnlive.com.arch.R;
import buildnlive.com.arch.fragments.CreatePurchaseOrderFragment;
import io.realm.Realm;

public class CreatePurchaseOrder extends AppCompatActivity {
    private App app;
    private Realm realm;
    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_purchase_order);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView textView=findViewById(R.id.toolbar_title);
        textView.setText("Create Purchase");
        app = (App) getApplication();
        realm = Realm.getDefaultInstance();
        fragment = CreatePurchaseOrderFragment.newInstance();
//        back =findViewById(R.id.back);
//        back.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                finish();
//            }
//        });
        changeScreen();
//        edit = findViewById(R.id.edit);
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                fragment = CreatePurchaseOrderFragment.newInstance();
//                changeScreen();
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
    private void changeScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.site_content, fragment)
                .commit();
    }
}
