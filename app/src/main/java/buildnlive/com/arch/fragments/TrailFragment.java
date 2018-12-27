package buildnlive.com.arch.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.activities.BillImageView;
import buildnlive.com.arch.adapters.TrailAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.ChildComment;
import buildnlive.com.arch.elements.Comment;
import buildnlive.com.arch.utils.Config;
import io.realm.Realm;

public class TrailFragment extends Fragment {
    private ArrayList<Comment> comments=new ArrayList<>();
    private TrailAdapter adapter;
    private RecyclerView items;
    private Realm realm;
    private static String id,url;
    private Context context;
    private ProgressBar progress;
    private TextView hider;
    private CoordinatorLayout coordinatorLayout;
//    private static String status="0";

    public static TrailFragment newInstance(String workID) {
       id=workID;
        return new TrailFragment();
    }


    private TrailAdapter.OnItemClickListener listener= new TrailAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(Comment project, int pos, View view) {
//            View view1 = getLayoutInflater().inflate(R.layout.fragment_bottom_sheet_dialog, null);
//            BottomSheetDialog dialog = new BottomSheetDialog(getContext());
            BottomSheetFragment bottomSheetFragment = new BottomSheetFragment();
            bottomSheetFragment.show(getActivity().getSupportFragmentManager(), bottomSheetFragment.getTag());
            Bundle bundle= new Bundle();
            bundle.putString("parent_id",project.getId());
            bottomSheetFragment.setArguments(bundle);
//            dialog.setContentView(view1);
//            dialog.show();
        }

        @Override
        public void onButtonClick(Comment project, int pos, View view) {
                GetImages(project.getId());

////            if(){
//            Intent intent=new Intent(getActivity(),BillImageView.class);
//            Bundle bundle=new Bundle();
//            bundle.putString("Link",url);
//                intent.putExtras(bundle);
//                startActivity(intent);
//            }

        }
    };

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        comments.clear();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
//        Bundle bundleId=getArguments();
//        id=bundleId.getString("id");
        return inflater.inflate(R.layout.fragment_trail, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getContext();
//        TextView toolbar_title=getActivity().findViewById(R.id.toolbar_title);
//        toolbar_title.setText("Trails");
        items=view.findViewById(R.id.trail);
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        progress=view.findViewById(R.id.progress);
        hider=view.findViewById(R.id.hider);
        realm=Realm.getDefaultInstance();
        adapter= new TrailAdapter(getContext(),comments,listener);
//        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        items.setAdapter(adapter);
//        adapter.notifyDataSetChanged();

    }

    private void refresh() {
        App app= ((App)getActivity().getApplication());
        comments.clear();
        String requestUrl = Config.GET_WORK_TRAIL;
        requestUrl = requestUrl.replace("[0]", App.userId);
        requestUrl = requestUrl.replace("[1]", id);
        requestUrl = requestUrl.replace("[2]", App.projectId);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
                final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Something went wrong, Try again later", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                console.log(response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject parent=array.getJSONObject(i);
                        JSONArray childarray= parent.getJSONArray("child_comments");

//                        if (childarray != null) {
//                            int len = childarray.length();
//                            for (int j=0;j<len;j++){
//                               ArrayList<String> arrayList= new ArrayList<>();
//                               arrayList.add(childarray.get(i).toString());
//                            }
//                        }
                        realm=Realm.getDefaultInstance();
                        if(childarray.length()!=0) {
                            for (int j = 0; j < childarray.length(); j++) {
                                JSONObject jsonObject = childarray.getJSONObject(j);
                                final ChildComment childComment = new ChildComment(jsonObject.getString("id"), jsonObject.getString("comment"), parent.getString("id"),jsonObject.getString("user"),jsonObject.getString("date"));
                                console.log("Child Comment Save " + childComment.getComment());
                                realm.executeTransaction(new Realm.Transaction() {
                                    @Override
                                    public void execute(Realm realm) {
                                        realm.copyToRealmOrUpdate(childComment);
                                    }
                                });
                            }
                        }
                        realm.close();

//                        if(!(childarray.length()==0)){
//                            JSONObject child=childarray.getJSONObject(0);
                            comments.add(new Comment().parseFromJSON(parent));
//                        }
//                        else {
//                            comments.add(new Comment().parseFromJSON(parent));
//                        }
                    }
                    console.log("data set changed");
                    adapter= new TrailAdapter(getContext(),comments,listener);
                    items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    DividerItemDecoration decoration = new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
                    items.addItemDecoration(decoration);
                    items.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    console.log(""+adapter.getItemCount());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }



    private void GetImages(String pid) {
        App app= ((App)getActivity().getApplication());
        String requestUrl = Config.GET_TRAIL_IMAGES;
        requestUrl = requestUrl.replace("[0]", pid);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.error("Network request failed with error :" + error);
                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
                final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Something went wrong, Try again later", Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
               console.log("Response Image "+response);
                try {
                    JSONArray jsonObject=new JSONArray(response);
                    url=jsonObject.toString();
                    console.log(url);
                    Intent intent=new Intent(getActivity(),BillImageView.class);
                    Bundle bundle=new Bundle();
                    bundle.putString("Link",url);
                    intent.putExtras(bundle);
                    startActivity(intent);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }
}
