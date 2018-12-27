//package buildnlive.com.arch.activities;
//
//import android.content.Intent;
//import android.os.Bundle;
//import android.support.annotation.Nullable;
//import android.support.v7.app.AppCompatActivity;
//import android.support.v7.widget.DividerItemDecoration;
//import android.support.v7.widget.LinearLayoutManager;
//import android.support.v7.widget.LinearSnapHelper;
//import android.support.v7.widget.RecyclerView;
//import android.support.v7.widget.SnapHelper;
//import android.support.v7.widget.Toolbar;
//import android.view.View;
//import android.widget.ImageButton;
//import android.widget.TextView;
//import android.widget.Toast;
//
//import com.android.volley.Request;
//
//import org.json.JSONArray;
//import org.json.JSONException;
//
//import java.util.ArrayList;
//
//import buildnlive.com.arch.App;
//import buildnlive.com.arch.Interfaces;
//import buildnlive.com.arch.R;
//import buildnlive.com.arch.adapters.ImageAdapter;
//import buildnlive.com.arch.console;
//import buildnlive.com.arch.utils.Config;
//
//public class BillImageView extends AppCompatActivity {
//    TextView no_content;
//    @Override
//    protected void onCreate(@Nullable Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        setContentView(R.layout.activity_bill_view);
//        final Toolbar toolbar = findViewById(R.id.toolbar);
//        setSupportActionBar(toolbar);
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//        TextView textView=findViewById(R.id.toolbar_title);
//        textView.setText("View Bills");
//        no_content=findViewById(R.id.no_content);
//        Intent intent=getIntent();
//        Bundle bundle= intent.getExtras();
//        assert bundle != null;
//        String item =bundle.getString("id");
//
//        if(item.isEmpty()){
//            no_content.setVisibility(View.VISIBLE);
//        }
//        else no_content.setVisibility(View.GONE);
//        RecyclerView images= findViewById(R.id.list);
//        String new_item=item.replace("[","");
//        new_item=new_item.replace("\"","");
//        new_item=new_item.replace("]","");
//        String[] array=new_item.split(",");
//        ArrayList<String> arrayList = new ArrayList<>();
//        for (String anArray : array) {
//            String arraynew = anArray.replaceAll("\\\\", "");
//            arrayList.add(arraynew);
//        }
//
//        if(arrayList.isEmpty()){
//            no_content.setVisibility(View.VISIBLE);
//        }
//        else no_content.setVisibility(View.GONE);
//
//        ImageAdapter imageAdapter=new ImageAdapter(getApplicationContext(),arrayList);
//        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
//        linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//        SnapHelper snapHelper=new LinearSnapHelper();
//        snapHelper.attachToRecyclerView(images);
//        DividerItemDecoration decoration = new DividerItemDecoration(this, linearLayoutManager.getOrientation());
//        images.addItemDecoration(decoration);
//        images.setLayoutManager(linearLayoutManager);
//        images.setAdapter(imageAdapter);
//    }
//
//
//    @Override
//    public boolean onSupportNavigateUp() {
//        onBackPressed();
//        return super.onSupportNavigateUp();
//    }
//
//    @Override
//    public void onBackPressed() {
//        super.onBackPressed();
//        finish();
//    }
//
//    private void GetImages() {
//        App app= ((App)getApplication());
//        String requestUrl = Config.GET_WORK_TRAIL;
//        requestUrl = requestUrl.replace("[0]", id);
//        console.log(requestUrl);
//        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
//            @Override
//            public void onNetworkRequestStart() {
////                progress.setVisibility(View.VISIBLE);
////                hider.setVisibility(View.VISIBLE);
//            }
//
//            @Override
//            public void onNetworkRequestError(String error) {
////                progress.setVisibility(View.GONE);
////                hider.setVisibility(View.GONE);
//                console.error("Network request failed with error :" + error);
//                Toast.makeText(getApplicationContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
////                final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Something went wrong, Try again later", Snackbar.LENGTH_INDEFINITE);
////                snackbar.setAction("Dismiss", new View.OnClickListener() {
////                    @Override
////                    public void onClick(View view) {
////                        snackbar.dismiss();
////                    }
////                }).show();
//            }
//
//            @Override
//            public void onNetworkRequestComplete(String response) {
//                try {
//                    JSONArray jsonObject=new JSONArray(response);
//                    url=jsonObject.toString();
//                } catch (JSONException e) {
//                    e.printStackTrace();
//                }
//            }
//        });
//    }
//}
