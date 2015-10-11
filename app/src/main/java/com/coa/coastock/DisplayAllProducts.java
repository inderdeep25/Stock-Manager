package com.coa.coastock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.coa.coastock.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;


public class DisplayAllProducts extends AppCompatActivity {

    RecyclerView productsListView;
    ArrayList<HashMap<String, String>> dataList = new ArrayList<>();
    CustomListAdapter adapter;

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_display_all_products);
        productsListView = (RecyclerView) findViewById(R.id.products_list_recycler_view);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF9900")));
        adapter = new CustomListAdapter(this, dataList);
        try {
            run("http://coa.hol.es/COA/get_all_products.php");
        } catch (IOException e) {
            e.printStackTrace();
        }

        productsListView.setLayoutManager(new LinearLayoutManager(this));
        productsListView.setAdapter(adapter);

    }

    void run(String url) throws IOException {

        Request request = new Request.Builder()
                .url(url)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {
                DisplayAllProducts.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        openDialog();
                    }
                });
            }

            @Override
            public void onResponse(Response response) throws IOException {

                String resp = response.body().string();
                int success = 0;
                try {
                    JSONObject jsonResponse = new JSONObject(resp);
                    JSONArray productsArray = jsonResponse.getJSONArray("products");
                    for (int i = 0; i < productsArray.length(); i++) {
                        HashMap<String, String> dummydata = new HashMap<>();
                        JSONObject obj = (JSONObject) productsArray.get(i);
                        dummydata.put("name", obj.getString("name"));
                        dummydata.put("quantity", obj.getString("count"));
                        dataList.add(dummydata);
                    }

                    success = jsonResponse.getInt("success");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                final int finalSuccess = success;
                DisplayAllProducts.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (finalSuccess == 0) {
                            openDialog();
                        } else if (finalSuccess == 1) {
                            adapter.notifyDataSetChanged();
                        }
                    }
                });

            }
        });


    }

    private void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(DisplayAllProducts.this);
        builder.setMessage("Something Went Wrong!")
                .setTitle("Oops!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                finish();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }


}
