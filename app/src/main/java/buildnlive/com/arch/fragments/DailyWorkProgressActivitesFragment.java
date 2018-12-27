package buildnlive.com.arch.fragments;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
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

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import buildnlive.com.arch.App;
import buildnlive.com.arch.BuildConfig;
import buildnlive.com.arch.Interfaces;
import buildnlive.com.arch.R;
import buildnlive.com.arch.adapters.ActivityImagesAdapter;
import buildnlive.com.arch.adapters.DailyWorkActivityAdapter;
import buildnlive.com.arch.console;
import buildnlive.com.arch.elements.Activity;
import buildnlive.com.arch.elements.Packet;
import buildnlive.com.arch.utils.AdvancedRecyclerView;
import buildnlive.com.arch.utils.Config;

public class DailyWorkProgressActivitesFragment extends Fragment {
    private App app;
    private RecyclerView items;
    private DailyWorkActivityAdapter adapter;
    private List<Activity> activities;
    private static String id;
    public static final int QUALITY = 10;
    private TextView no_content;
    private CoordinatorLayout coordinatorLayout;
    private ProgressBar progressBar;
    private TextView hider;
    private Context context;


    public static DailyWorkProgressActivitesFragment newInstance(String workId) {
        id=workId;
        return new DailyWorkProgressActivitesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_daily_work_progress, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        context=getContext();
        TextView toolbar_title=getActivity().findViewById(R.id.toolbar_title);
        toolbar_title.setText("Work Progress");
        app = (App) getActivity().getApplication();

//        Bundle bundle = getArguments();
//        id = bundle.getString("id");

        items = view.findViewById(R.id.items);
        coordinatorLayout=view.findViewById(R.id.coordinatorLayout);
        no_content=view.findViewById(R.id.no_content);
        progressBar=view.findViewById(R.id.progress);
        hider=view.findViewById(R.id.hider);
        activities = new ArrayList<>();
        adapter = new DailyWorkActivityAdapter(getContext(), activities, new DailyWorkActivityAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int pos, View view) {

                menuUpdate(activities.get(pos));
            }
        });
        items.setLayoutManager(new LinearLayoutManager(getContext()));
        items.setAdapter(adapter);
//        if(adapter.getItemCount()==0)
//        {
//            Toast.makeText(this,"Nothing to Display",Toast.LENGTH_LONG).show();
//        }
        fetchActivities(id);
       
    }

    public static final int REQUEST_CAPTURE_IMAGE = 7190;
    private ArrayList<Packet> images;
    private ActivityImagesAdapter imagesAdapter;
    private String imagePath;

    private void menuUpdate(final Activity activity) {
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.alert_dialog_activity, null);
        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(context, R.style.PinDialog);
        final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
        alertDialog.show();
        final TextView disable = dialogView.findViewById(R.id.disableView);
        final TextView max = dialogView.findViewById(R.id.max);
        max.setText("Total: " + activity.getQuantity() + " " + activity.getUnits());
        final ProgressBar progress = dialogView.findViewById(R.id.progress);
        final EditText message = dialogView.findViewById(R.id.message);
        final EditText quantity = dialogView.findViewById(R.id.quantity);
        final TextView completed = dialogView.findViewById(R.id.completed);
        final AdvancedRecyclerView list = dialogView.findViewById(R.id.images);
        final TextView attache = dialogView.findViewById(R.id.attache_images);
        final View line = dialogView.findViewById(R.id.view);
        message.setVisibility(View.GONE);
        line.setVisibility(View.GONE);
        attache.setVisibility(View.GONE);
        list.setVisibility(View.GONE);
//        images = new ArrayList<>();
//        images.add(new Packet());
//        imagesAdapter = new ActivityImagesAdapter(getContext(), images, new ActivityImagesAdapter.OnItemClickListener() {
//            @Override
//            public void onItemClick(Packet packet, int pos, View view) {
//                if (pos == 0) {
//                    Intent pictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//                    if (pictureIntent.resolveActivity(getActivity().getPackageManager()) != null) {
//
//                        File photoFile = null;
//                        try {
//                            photoFile = createImageFile();
//                        } catch (IOException ex) {
//                        }
//                        if (photoFile != null) {
//                            Uri photoURI = FileProvider.getUriForFile(getContext(), BuildConfig.APPLICATION_ID + ".provider", photoFile);
//                            pictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
//                            imagePath = photoFile.getAbsolutePath();
//                            startActivityForResult(pictureIntent, REQUEST_CAPTURE_IMAGE);
//                        }
//                    }
//                }
//            }
//        });
//        list.setAdapter(imagesAdapter);
//        list.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
//        list.setmMaxHeight(400);
        completed.setText("Completed: " + activity.getQty_completed() + " " + activity.getUnits());
        TextView title = dialogView.findViewById(R.id.alert_title);
        title.setText("Activity Status");
        final TextView alert_message = dialogView.findViewById(R.id.alert_message);
        alert_message.setText("Please fill work progress.");
        Button positive = dialogView.findViewById(R.id.positive);
        positive.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    if (quantity.getText().toString() != null)
                        submit(activity, "", quantity.getText().toString(), images, alertDialog);
                    else
                        Toast.makeText(getContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
                } catch (Exception e) {
                    Toast.makeText(getContext(), "Fill data properly!", Toast.LENGTH_SHORT).show();
                }
            }
        });
        Button negative = dialogView.findViewById(R.id.negative);
        negative.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
    }
//
//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        if (requestCode == REQUEST_CAPTURE_IMAGE) {
//            if (resultCode == android.app.Activity.RESULT_OK) {
//                Packet packet = images.remove(0);
//                packet.setName(imagePath);
//                images.add(0, new Packet());
//                images.add(packet);
//                imagesAdapter.notifyDataSetChanged();
//            } else if (resultCode == android.app.Activity.RESULT_CANCELED) {
//                console.log("Canceled");
//            }
//        }
//    }


//    private File createImageFile() throws IOException {
//        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(new Date());
//        String imageFileName = "IMG_" + timeStamp + "_";
//        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
//        File image = File.createTempFile(imageFileName, ".jpg", storageDir);
//        return image;
//    }

    private void submit(Activity activity, String message, String quantity, ArrayList<Packet> images, final AlertDialog alertDialog) throws JSONException {
        float q = Float.parseFloat(quantity);
        float c = Float.parseFloat(activity.getQty_completed());
        float qo = Float.parseFloat(activity.getQuantity());
        if (q <= (qo - c)) {
            HashMap<String, String> params = new HashMap<>();
            params.put("work_update", new JSONObject()
                    .put("activity_list_id", activity.getActivityListId())
                    .put("type", "activity")
                    .put("project_comment", message)
                    .put("quantity_done", quantity)
                    .put("units", activity.getUnits())
                    .put("user_id", App.userId)
                    .put("project_id", App.projectId)
                    .put("percentage_work", q / qo).toString());
            JSONArray array = new JSONArray();
            for (Packet p : images) {
                if (p.getName() != null) {
                    Bitmap bm = BitmapFactory.decodeFile(p.getName());
                    ByteArrayOutputStream baos = new ByteArrayOutputStream();
                    bm.compress(Bitmap.CompressFormat.JPEG, QUALITY, baos);
                    byte[] b = baos.toByteArray();
                    String encodedImage = Base64.encodeToString(b, Base64.DEFAULT);
                    array.put(encodedImage);
                }
            }
            params.put("images",array.toString());

            app.sendNetworkRequest(Config.REQ_DAILY_WORK_ACTIVITY_UPDATE, 1, params, new Interfaces.NetworkInterfaceListener() {
                @Override
                public void onNetworkRequestStart() {
                    progressBar.setVisibility(View.VISIBLE);
                    hider.setVisibility(View.VISIBLE);
                }

                @Override
                public void onNetworkRequestError(String error) {
                    progressBar.setVisibility(View.GONE);
                    hider.setVisibility(View.GONE);
                }

                @Override
                public void onNetworkRequestComplete(String response) {
                    progressBar.setVisibility(View.GONE);
                    hider.setVisibility(View.GONE);
                    if (response.equals("1")) {
                        Toast.makeText(getContext(), "Status Updated", Toast.LENGTH_SHORT).show();
                        alertDialog.dismiss();
                        getActivity().finish();
                    }
                }
            });
        } else {
            Toast.makeText(getContext(), "Put right quantity", Toast.LENGTH_SHORT).show();
        }
    }

    private void fetchActivities(String id) {
        String url = Config.REQ_DAILY_WORK_ACTIVITY;
        url = url.replace("[0]", App.userId);
        url = url.replace("[1]", id);
        console.log(url);
        app.sendNetworkRequest(url, 0, null, new Interfaces.NetworkInterfaceListener() {
            @Override
            public void onNetworkRequestStart() {
                progressBar.setVisibility(View.VISIBLE);
                hider.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNetworkRequestError(String error) {
                progressBar.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                final Snackbar snackbar= Snackbar.make(coordinatorLayout,"Check Your Network",Snackbar.LENGTH_INDEFINITE);
                snackbar.setAction("Dismiss", new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        snackbar.dismiss();
                    }
                }).show();
            }

            @Override
            public void onNetworkRequestComplete(String response) {
                progressBar.setVisibility(View.GONE);
                hider.setVisibility(View.GONE);
                console.log("Response:" + response);
                try {
                    JSONArray array = new JSONArray(response);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject par = array.getJSONObject(i);
                        JSONObject sch = par.getJSONObject("work_schedule");
                        final Activity activity = new Activity().parseFromJSON(sch.getJSONObject("work_details"), par.getString("activity_list_id"),sch.getString("work_duration"),
                                sch.getString("qty"), sch.getString("units"),
                                sch.getString("schedule_start_date"), sch.getString("schedule_finish_date"),
                                sch.getString("current_status"), sch.getString("qty_completed"),sch.getString("percent_compl"));
                        activities.add(activity);

                        console.log(""+activities.get(i));
                    }
                    if(activities.isEmpty())
                    {
                        no_content.setVisibility(View.VISIBLE);
//                        LayoutInflater inflater = getLayoutInflater();
//                        View dialogView = inflater.inflate(R.layout.alert_dialog, null);
//                        AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(getContext(), R.style.PinDialog);
//                        final AlertDialog alertDialog = dialogBuilder.setCancelable(false).setView(dialogView).create();
//                        alertDialog.show();
//                        final EditText alert_message=dialogView.findViewById(R.id.alert_message);
//                        final Button close=dialogView.findViewById(R.id.negative);
//                        final Button approve=dialogView.findViewById(R.id.positive);
//
//                        approve.setVisibility(View.GONE);
//                        close.setOnClickListener(new View.OnClickListener() {
//                            @Override
//                            public void onClick(View view) {
//                                alertDialog.dismiss();
//                            }
//                        });
//
//                        alert_message.setText("Nothing to Show");
//                        Toast.makeText(getContext(),"Nothing to Display",Toast.LENGTH_SHORT).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                adapter.notifyDataSetChanged();
            }
        });
    }
}
