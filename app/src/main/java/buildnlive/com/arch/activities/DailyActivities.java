package buildnlive.com.arch.activities;

import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.arch.App;
import buildnlive.com.arch.R;
import buildnlive.com.arch.adapters.DailyWorkActivityAdapter;
import buildnlive.com.arch.elements.Activity;
import buildnlive.com.arch.fragments.AddItemFragment;
import buildnlive.com.arch.fragments.DailyWorkProgressActivitesFragment;
import buildnlive.com.arch.fragments.TrailFragment;
import buildnlive.com.arch.fragments.ViewItemFragment;

public class DailyActivities extends AppCompatActivity {
    private App app;
    private RecyclerView items;
    private DailyWorkActivityAdapter adapter;
    private List<Activity> activities;
    private String id;
    public static final int QUALITY = 10;
    private TextView no_content;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;
    private TextView hider;
    private Fragment fragment;
    private TextView edit, view;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_daily_work_activities);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView textView = findViewById(R.id.toolbar_title);
        textView.setText("Activities");

        final Bundle bundle = getIntent().getExtras();
        assert bundle != null;
        id = bundle.getString("id");
        app = (App) getApplication();
//        edit = findViewById(R.id.edit);
//        view = findViewById(R.id.view);
//        fragment.setArguments(bundle);
        fragment = DailyWorkProgressActivitesFragment.newInstance(id);
        changeScreen();
//        edit = findViewById(R.id.edit);
//        edit.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                enableEdit();
//                disableView();
////                fragment.setArguments(bundle);
//                fragment = TrailFragment.newInstance(id);
//                changeScreen();
//            }
//        });
//        view = findViewById(R.id.view);
//        view.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                enableView();
//                disableEdit();
////                fragment.setArguments(bundle);
//                fragment = DailyWorkProgressActivitesFragment.newInstance(id);
//                changeScreen();
//            }
//        });
    }

    private void changeScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.attendance_content, fragment)
                .commit();
    }

    private void disableView() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_left, null));
        } else {
            view.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_left));
        }
        view.setTextColor(getResources().getColor(R.color.color2));
    }

    private void enableView() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            view.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_left_selected, null));
        } else {
            view.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_left_selected));
        }
        view.setTextColor(getResources().getColor(R.color.white));
    }

    private void disableEdit() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            edit.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_right, null));
        } else {
            edit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_right));
        }
        edit.setTextColor(getResources().getColor(R.color.color2));
    }

    private void enableEdit() {
        int sdk = android.os.Build.VERSION.SDK_INT;
        if (sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
            edit.setBackgroundDrawable(ResourcesCompat.getDrawable(getResources(), R.drawable.round_right_selected, null));
        } else {
            edit.setBackground(ContextCompat.getDrawable(getApplicationContext(), R.drawable.round_right_selected));
        }
        edit.setTextColor(getResources().getColor(R.color.white));
    }
}