package buildnlive.com.arch.activities;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.LinearSnapHelper;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SnapHelper;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;

import java.util.ArrayList;

import buildnlive.com.arch.R;
import buildnlive.com.arch.adapters.ImageAdapter;

public class BillImageView extends AppCompatActivity {
    TextView no_content;
    String item="";
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bill_view);
        final Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
        TextView textView=findViewById(R.id.toolbar_title);
        textView.setText("View Bills");
        no_content=findViewById(R.id.no_content);
        Intent intent=getIntent();
        Bundle bundle= intent.getExtras();
        item =bundle.getString("Link");

        if(item.equals("")){
            no_content.setVisibility(View.VISIBLE);
        }
        else no_content.setVisibility(View.GONE);
        RecyclerView images= findViewById(R.id.list);
        String new_item=item.replace("[","");
        new_item=new_item.replace("\"","");
        new_item=new_item.replace("]","");
            String[] array=new_item.split(",");
            ArrayList<String> arrayList = new ArrayList<>();
            for (String anArray : array) {
                String arraynew = anArray.replaceAll("\\\\", "");
                arrayList.add(arraynew);
            }

            if(arrayList.isEmpty()){
                no_content.setVisibility(View.VISIBLE);
            }
            else no_content.setVisibility(View.GONE);

            ImageAdapter imageAdapter=new ImageAdapter(getApplicationContext(),arrayList);
            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
            SnapHelper snapHelper=new LinearSnapHelper();
            snapHelper.attachToRecyclerView(images);
            DividerItemDecoration decoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
            images.addItemDecoration(decoration);
            images.setLayoutManager(linearLayoutManager);
            images.setAdapter(imageAdapter);
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
