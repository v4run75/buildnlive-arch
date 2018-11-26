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

import buildnlive.com.arch.fragments.SendIndentFragment;
import buildnlive.com.arch.App;
import buildnlive.com.arch.R;

public class CreateItem extends AppCompatActivity {
    private App app;
    private Fragment fragment;
    private TextView edit, view;
    private ImageButton back;

    @Override
    protected void onCreate(@Nullable final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indent_item);
        app = (App) getApplication();
        fragment = SendIndentFragment.newInstance();
        back = findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
        changeScreen();
    }

    private void changeScreen() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.site_content, fragment)
                .commit();
    }
}
