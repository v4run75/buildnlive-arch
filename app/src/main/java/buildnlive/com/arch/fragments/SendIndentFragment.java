package buildnlive.com.arch.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import buildnlive.com.arch.App;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.adapters.CategorySpinAdapter;
import buildnlive.com.arch.adapters.CreatePurchaseOrderAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Category;
import buildnlive.com.arch.elements.IndentItem;
import buildnlive.com.arch.utils.Config;

public class SendIndentFragment extends Fragment{
    private static List<IndentItem> siteIndentItem = new ArrayList<>();
    private RecyclerView items;
    private Button next, submit,add_item;
    private ProgressBar progress;
    private TextView hider, checkout_text,search_textview;
    private boolean LOADING;
    private ImageButton close;
    private CreatePurchaseOrderAdapter adapter;
    public static String category="";
    private Spinner categorySpinner;
    private CategorySpinAdapter categoryAdapter;
    private ArrayList<Category> categoryList = new ArrayList<>();
    private android.support.v7.widget.SearchView searchView;
    AlertDialog.Builder builder;
    final static List<IndentItem> newItems = new ArrayList<>();
    private CoordinatorLayout coordinatorLayout;


    public CreatePurchaseOrderAdapter.OnItemSelectedListener listener = new CreatePurchaseOrderAdapter.OnItemSelectedListener() {
        @Override
        public void onItemCheck(boolean checked) {
            if (checked) {
                add_item.setVisibility(View.VISIBLE);
                next.setVisibility(View.VISIBLE);
            } else {
                add_item.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
            }
        }

        @Override
        public void onItemInteract(int pos, int flag) {

        }
    };

    public static SendIndentFragment newInstance() {
//        siteIndentItem = u;
        return new SendIndentFragment();
    }
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        categoryList.clear();
        newItems.clear();
        CreatePurchaseOrderAdapter.ViewHolder.CHECKOUT=false;
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view= inflater.inflate(R.layout.fragment_indent_item, container, false);

        return view;
    }



    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        add_item = view.findViewById(R.id.add_item);
        items = view.findViewById(R.id.items);
        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
        builder= new AlertDialog.Builder(getContext());
        searchView = view.findViewById(R.id.search_view);
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        submit = view.findViewById(R.id.submit);
        next = view.findViewById(R.id.next);
        close = view.findViewById(R.id.close_checkout);
        setCategorySpinner();
        categorySpinner=view.findViewById(R.id.category);
        categorySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                String a = categoryAdapter.getID(i);
                    if(!a.equals("0")){
                        refresh(a);
//                        searchView.setVisibility(View.VISIBLE);
//                        search_textview.setVisibility(View.VISIBLE);

                    }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        categoryAdapter=new CategorySpinAdapter(getContext(), R.layout.custom_spinner,categoryList);
        categoryAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(categoryAdapter);





        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newItems.clear();
                CreatePurchaseOrderAdapter.ViewHolder.CHECKOUT = false;
                checkout_text.setVisibility(View.GONE);
                close.setVisibility(View.GONE);
                categorySpinner.setVisibility(View.VISIBLE);
                CreatePurchaseOrderAdapter.ViewHolder.CHECKOUT = false;
                adapter = new CreatePurchaseOrderAdapter(getContext(), siteIndentItem, listener);
                items.setAdapter(adapter);
                submit.setVisibility(View.GONE);
                next.setVisibility(View.GONE);
                add_item.setVisibility(View.GONE);
            }
        });
        checkout_text = view.findViewById(R.id.checkout_text);
        add_item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (int i = 0; i < siteIndentItem.size(); i++) {
                    if (siteIndentItem.get(i).isUpdated()) {
                        newItems.add(siteIndentItem.get(i));
                    }
                }
                add_item.setVisibility(View.GONE);
            }
        });

        next.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_textview.setVisibility(View.GONE);
                searchView.setVisibility(View.GONE);
                checkout_text.setVisibility(View.VISIBLE);
                close.setVisibility(View.VISIBLE);
                categorySpinner.setVisibility(View.GONE);
                CreatePurchaseOrderAdapter.ViewHolder.CHECKOUT = true;
//                final List<IndentItem> newItems = new ArrayList<>();
//                for (int i = 0; i < siteIndentItem.size(); i++) {
//                    if (siteIndentItem.get(i).isUpdated()) {
//                        newItems.add(siteIndentItem.get(i));
//                    }
//                }

                adapter = new CreatePurchaseOrderAdapter(getContext(), newItems, new CreatePurchaseOrderAdapter.OnItemSelectedListener() {
                    @Override
                    public void onItemCheck(boolean checked) {

                    }

                    @Override
                    public void onItemInteract(int pos, int flag) {
                        if (flag == 100) {
                            newItems.remove(pos);
                            adapter.notifyItemRemoved(pos);
                        }
                    }
                });

                items.setAdapter(adapter);
                submit.setVisibility(View.VISIBLE);
                next.setVisibility(View.GONE);
                add_item.setVisibility(View.GONE);
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        builder.setMessage("Are you sure?") .setTitle("Add Stock");

                        //Setting message manually and performing action on button click
                        builder.setMessage("Do you want to Submit?")
                                .setCancelable(false)
                                .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        try {
                                            sendRequest(newItems);
                                        } catch (JSONException e) {

                                        }
                                    }
                                })
                                .setNegativeButton("No", new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        //  Action for 'NO' Button
                                        dialog.cancel();

                                    }
                                });
                        //Creating dialog box
                        AlertDialog alert = builder.create();
                        //Setting the title manually
                        alert.setTitle("Request Item");
                        alert.show();


                    }
                });
            }
        });
        progress = view.findViewById(R.id.progress);
        hider = view.findViewById(R.id.hider);
//        adapter = new CreatePurchaseOrderAdapter(getContext(), siteIndentItem, listener);
//        items.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.VERTICAL, false));
//        items.setAdapter(adapter);
        if (LOADING) {
            progress.setVisibility(View.VISIBLE);
            hider.setVisibility(View.VISIBLE);
        } else {
            progress.setVisibility(View.GONE);
            hider.setVisibility(View.GONE);
        }


        search_textview=view.findViewById(R.id.search_textview);
        searchView = view.findViewById(R.id.search_view);

        // Associate searchable configuration with the SearchView
//        SearchManager searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
//        searchView.setSearchableInfo(searchManager
//                .getSearchableInfo(getActivity().getComponentName()));
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                search_textview.setVisibility(View.GONE);
            }
        });
        // listening to search query text change
        searchView.setOnQueryTextListener(new android.support.v7.widget.SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                // filter recycler view when query submitted
                adapter.getFilter().filter(query);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {
                // filter recycler view when text is changed
                adapter.getFilter().filter(query);
                return false;
            }
        });

    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        siteIndentItem.clear();
//        adapter.notifyItemRangeChanged(0,siteIndentItem.size());
    }

    private void sendRequest(List<IndentItem> items) throws JSONException {
        App app= ((App)getActivity().getApplication());
        HashMap<String, String> params = new HashMap<>();
        params.put("user_id", App.userId);
        params.put("project_id", App.projectId);
        JSONArray array = new JSONArray();
        for (IndentItem i : items) {
            array.put(new JSONObject().put("item_id", i.getId()).put("qty", i.getQuantity()));
        }
        params.put("item_list", array.toString());
        console.log("Res:" + params);
        console.log("Site"+Config.SEND_STOCK_ITEM_LIST);
        app.sendNetworkRequest(Config.SEND_STOCK_ITEM_LIST, 1, params, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progress.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
//                Toast.makeText(getContext(),"Error"+error,Toast.LENGTH_LONG).show();
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
                console.log("New Reply"+response);
                if (response.equals("1")) {
                    Toast.makeText(getContext(), "Request Generated", Toast.LENGTH_SHORT).show();
                    CreatePurchaseOrderAdapter.ViewHolder.CHECKOUT=false;
                    getActivity().finish();
                }
            }
        });
    }


    private void setCategorySpinner() {
        App app= ((App)getActivity().getApplication());
        String requestURl= Config.REQ_SENT_CATEGORIES ;
        requestURl = requestURl.replace("[0]", App.userId);
        categoryList.clear();
        app.sendNetworkRequest(requestURl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {

            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
//                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
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

                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
//                        categoryList.add(array.getJSONObject(i).getString("name"));
                        categoryList.add(new Category().parseFromJSON(array.getJSONObject(i)));
                    }
                    categoryAdapter.notifyDataSetChanged();
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void refresh(String a) {
        App app= ((App)getActivity().getApplication());
        siteIndentItem.clear();
        String requestUrl = Config.CREATE_ITEM;
        requestUrl = requestUrl.replace("[1]", App.userId);
        requestUrl = requestUrl.replace("[0]", a);
        console.log(requestUrl);
        app.sendNetworkRequest(requestUrl, Request.Method.GET, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progress.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {

                console.error("Network request failed with error :" + error);
//                Toast.makeText(getContext(), "Check Network, Something went wrong", Toast.LENGTH_LONG).show();
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
                        siteIndentItem.add(new IndentItem().parseFromJSON(array.getJSONObject(i)));
                    }
                    console.log("data set changed");
                    adapter = new CreatePurchaseOrderAdapter(getContext(), siteIndentItem, listener);
                    items.setAdapter(adapter);
                    adapter.notifyDataSetChanged();
                    console.log(""+adapter.getItemCount());

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


}
