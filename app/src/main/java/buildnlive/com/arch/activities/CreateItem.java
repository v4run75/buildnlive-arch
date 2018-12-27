package buildnlive.com.arch.activities;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import org.w3c.dom.Text;

import buildnlive.com.arch.fragments.SendIndentFragment;
import buildnlive.com.arch.App;
import buildnlive.com.arch.R;

public class CreateItem extends AppCompatActivity {
    private App app;
    private Fragment fragment;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indent_item);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView textView=findViewById(R.id.toolbar_title);
        textView.setText("Add Stock");
        app = (App) getApplication();
        fragment = SendIndentFragment.newInstance();
        changeScreen();
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
