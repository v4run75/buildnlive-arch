package buildnlive.com.arch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.activities.CreateItem;
import buildnlive.com.arch.adapters.ViewItemAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Item;
import buildnlive.com.arch.utils.Config;

public class ViewItemFragment extends Fragment {
    private static List<Item> itemsList=new ArrayList<>();
    private RecyclerView items;
    private static App app;
    private ProgressBar progress;
    private TextView hider;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;

    public static ViewItemFragment newInstance(App a) {
        app=a;
        return new ViewItemFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_item, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        TextView toolbar_title=getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Inventory");
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        items = view.findViewById(R.id.items);
        fab= view.findViewById(R.id.add);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(getContext(),CreateItem.class));
            }
        });
    }

    @Override
    public void onStart() {
        super.onStart();
        refresh();
    }

    private void refresh() {
        itemsList.clear();
        String url = Config.REQ_GET_INVENTORY;
        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", App.projectId);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
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
                console.log("Response:" + response);
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        final JSONObject obj = array.getJSONObject(i);
                        itemsList.add(new Item().parseFromJSON(obj));

//                        realm.executeTransaction(new Realm.Transaction() {
//                            @Override
//                            public void execute(Realm realm) {
//                                try {
//                                    RequestList request = new RequestList().parseFromJSON(obj);
//                                    realm.copyToRealmOrUpdate(request);
//                                } catch (JSONException e) {
//                                    e.printStackTrace();
//                                }
//                            }
//                        });
                    }
                    final ViewItemAdapter adapter = new ViewItemAdapter(getContext(), itemsList);
                    items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    items.setAdapter(adapter);

//                    realm.close();

                } catch (JSONException e) {

                }
            }
        });
    }


}