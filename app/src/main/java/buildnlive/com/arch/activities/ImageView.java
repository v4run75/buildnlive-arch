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
import android.view.View;
import android.widget.ImageButton;

import java.util.ArrayList;

import buildnlive.com.arch.R;
import buildnlive.com.arch.adapters.ImageAdapter;

public class ImageView extends AppCompatActivity {
    ImageButton back;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_image_view);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent=getIntent();
        Bundle bundle= intent.getExtras();
        String item =bundle.getString("Link");
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
}
