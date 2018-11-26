package buildnlive.com.arch.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.activities.CreateItem;
import buildnlive.com.arch.activities.ImageView;
import buildnlive.com.arch.adapters.ImageAdapter;
import buildnlive.com.arch.adapters.ViewItemAdapter;
import buildnlive.com.arch.adapters.ViewPaymentAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Item;
import buildnlive.com.arch.elements.PaymentItem;
import buildnlive.com.arch.elements.ProjectList;
import buildnlive.com.arch.utils.Config;

import static android.widget.LinearLayout.HORIZONTAL;
import static android.widget.LinearLayout.VERTICAL;

public class ViewPaymentFragment extends Fragment {
    private static List<PaymentItem> itemsList=new ArrayList<>();
    private RecyclerView items;
    private static App app;
    private ProgressBar progress;
    private TextView hider;
    private FloatingActionButton fab;
    ViewPaymentAdapter.OnItemClickListener listener= new ViewPaymentAdapter.OnItemClickListener() {
        @Override
        public void onItemClick(PaymentItem project, int pos, View view) {

        }

        @Override
        public void onButtonClick(PaymentItem item, int pos, View view) {
            Bundle bundle=new Bundle();
            bundle.putString("Link",item.getBill_copy());
            Intent intent=new Intent(getActivity(),ImageView.class);
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
//                    catch (Exception e) {
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

    public static ViewPaymentFragment newInstance(App a) {
        app=a;
        return new ViewPaymentFragment();
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
        toolbar_title.setText("Payments");
        items = view.findViewById(R.id.items);
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
        refresh();
    }


    private void refresh() {
        itemsList.clear();
        String url = Config.VIEW_SITE_PAYMENTS;
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
                        itemsList.add(new PaymentItem().parseFromJSON(obj));

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
                    final ViewPaymentAdapter adapter = new ViewPaymentAdapter(getContext(), itemsList,listener);
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