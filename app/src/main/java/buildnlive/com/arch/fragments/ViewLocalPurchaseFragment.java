package buildnlive.com.arch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.FloatingActionButton;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.activities.BillImageView;
import buildnlive.com.arch.adapters.ViewLocalPurchaseAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.LocalPurchaseItem;
import buildnlive.com.arch.utils.Config;

import static android.widget.LinearLayout.VERTICAL;

public class ViewLocalPurchaseFragment extends Fragment {
    private static List<LocalPurchaseItem> itemsList=new ArrayList<>();
    private RecyclerView items;
    private static App app;
    private ProgressBar progress;
    private TextView hider,no_content;
    private FloatingActionButton fab;
    private CoordinatorLayout coordinatorLayout;
    ViewLocalPurchaseAdapter.OnItemClickListener listener= new ViewLocalPurchaseAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(LocalPurchaseItem project, int pos, View view) {

        }

        @Override
        public void onButtonClick(LocalPurchaseItem item, int pos, View view) {
            Bundle bundle=new Bundle();
            bundle.putString("Link",item.getBill_copy());
            Intent intent=new Intent(getActivity(),BillImageView.class);
            intent.putExtras(bundle);
            startActivity(intent);

//            LayoutInflater inflater = getLayoutInflater();
//            View dialogView = inflater.inflate(R.layout.alert_images, null);
//            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.PinDialog);
//            final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
//            alertDialog.show();
//
//            final RecyclerView images= dialogView.findViewById(R.id.list);
//            String[] array=item.getBill_copy().split(",");
//            ArrayList<String> arrayList = new ArrayList<>();
//            for (String anArray : array) {
//                String arraynew = anArray.replaceAll("\\\\", "");
//                arrayList.add(arraynew);
//            }
//
//            ImageAdapter imageAdapter=new ImageAdapter(getContext(),arrayList);
//            LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
//            linearLayoutManager.setOrientation(LinearLayoutManager.HORIZONTAL);
//            images.setLayoutManager(linearLayoutManager);
//            images.setAdapter(imageAdapter);
//            Button positive = dialogView.findViewById(R.id.positive);
//            positive.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    try {
//
//                        alertDialog.dismiss();
//                    }
//                        catch (Exception e) {
//                        Toast.makeText(getContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
//                    }
//                }
//            });
//            Button negative = dialogView.findViewById(R.id.negative);
//            negative.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    alertDialog.dismiss();
//                }
//            });
        }
    };

    public static ViewLocalPurchaseFragment newInstance(App a) {
        app=a;
        return new ViewLocalPurchaseFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_view_local_purchase, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        TextView toolbar_title=getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Purchase");
        no_content=view.findViewById(R.id.no_content);
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        items = view.findViewById(R.id.items);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        refresh();
    }


    private void refresh() {
        itemsList.clear();
        String url = Config.VIEW_LOCAL_PURCHASE;
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
                        itemsList.add(new LocalPurchaseItem().parseFromJSON(obj));

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
                    if(itemsList.isEmpty()){
                        no_content.setVisibility(View.VISIBLE);
                    }
                    else no_content.setVisibility(View.GONE);                    final ViewLocalPurchaseAdapter adapter = new ViewLocalPurchaseAdapter(getContext(), itemsList,listener);
                    items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
                    DividerItemDecoration decoration = new DividerItemDecoration(getContext(), VERTICAL);
                    items.addItemDecoration(decoration);
                    items.setAdapter(adapter);

//                    realm.close();

                } catch (JSONException e) {

                }
            }
        });
    }


}