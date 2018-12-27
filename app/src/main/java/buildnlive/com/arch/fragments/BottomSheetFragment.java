package buildnlive.com.arch.fragments;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.adapters.AddItemAdapter;
import buildnlive.com.arch.adapters.ChildAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.ChildComment;
import buildnlive.com.arch.elements.Comment;
import buildnlive.com.arch.elements.Item;
import buildnlive.com.arch.utils.Config;
import io.realm.Realm;
import io.realm.RealmResults;

public class BottomSheetFragment extends BottomSheetDialogFragment {
    private Realm realm;
    private ArrayList<ChildComment> childCommentList=new ArrayList<>();
    private RecyclerView items;
    private ChildAdapter adapter;
    private EditText writeComment;
    private ImageButton sendButton;
    private static String pid;
    private Context context;
    ChildAdapter.OnItemClickListener listner=new ChildAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(ChildComment project, int pos, View view) {

        }
    };


    public BottomSheetFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_bottom_sheet_dialog, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Bundle bundle= getArguments();
        pid=bundle.getString("parent_id");
        context=getContext();
        realm=Realm.getDefaultInstance();
        RealmResults<ChildComment> childComments=realm.where(ChildComment.class).equalTo("parent_id",pid).findAll();
        childCommentList.addAll(childComments);
        if(childCommentList.isEmpty()){
            childCommentList.add(new ChildComment("","","","",""));
        }
        for(int i=0;i<childCommentList.size();i++){
            console.log("Child Comment View "+childCommentList.get(i).getComment());
        }

        writeComment=view.findViewById(R.id.write_comment);
        sendButton=view.findViewById(R.id.send);

        items=view.findViewById(R.id.items);
        adapter=new ChildAdapter(getContext(),childCommentList,listner,pid);
        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        DividerItemDecoration decoration = new DividerItemDecoration(context, LinearLayoutManager.VERTICAL);
        items.addItemDecoration(decoration);
        items.setAdapter(adapter);


        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(!writeComment.getText().toString().isEmpty()){
                    sendRequest(writeComment.getText().toString());
                    writeComment.setText("");
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
//        if(childCommentList.get(0).getParent_id().equals(pid)){
//            text.setText(childCommentList.get(0).getComment());
//            }

    }

    private void sendRequest(final String comment) throws JSONException {
        App app = ((App) getActivity().getApplication());
        HashMap<String, String> params = new HashMap<>();
        childCommentList.clear();
        params.put("user_id", App.userId);
        params.put("parent_id",pid);
        params.put("comment",comment);
        console.log("params"+params.toString());
        app.sendNetworkRequest(Config.ADD_CHILD_COMMENT, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
//                progress.setVisibility(View.VISIBLE);
//                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
                Toast.makeText(getContext(),"Error:"+error,Toast.LENGTH_LONG).show();
//                final Snackbar snackbar = Snackbar.make(coordinatorLayout, "Something went wrong, Try again later", Snackbar.LENGTH_INDEFINITE);
//                snackbar.setAction("Dismiss", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View view) {
//                        snackbar.dismiss();
//                    }
//                }).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                try {
                    console.log("Response "+response);
                        JSONObject jsonObject=new JSONObject(response);
                        final ChildComment childComment = new ChildComment(jsonObject.getString("id"), jsonObject.getString("comment"), pid,jsonObject.getString("user"),jsonObject.getString("date"));
                        realm.executeTransaction(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                realm.copyToRealmOrUpdate(childComment);
                            }
                        });
                        RealmResults<ChildComment> childComments=realm.where(ChildComment.class).equalTo("parent_id",pid).findAll();
                        childCommentList.addAll(childComments);
                        if(childCommentList.isEmpty()){
                            childCommentList.add(new ChildComment("","","","",""));
                        }
                        adapter.notifyDataSetChanged();
                        Toast.makeText(getContext(), "Request Generated", Toast.LENGTH_SHORT).show();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
//                progress.setVisibility(View.GONE);
//                hider.setVisibility(View.GONE);
//                if (response.equals("1")) {
//                    Toast.makeText(getContext(), "Request Generated", Toast.LENGTH_SHORT).show();
//
//                }


            }
        });
    }
}