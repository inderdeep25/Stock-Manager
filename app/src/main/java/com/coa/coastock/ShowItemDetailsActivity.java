package com.coa.coastock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.ActionBar;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.TextView;

import com.coa.coastock.R;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;


public class ShowItemDetailsActivity extends AppCompatActivity {

    String productName,cost,quantity,barcode;
    TextView productNameTextView,costTextView,quantityyTextView,discountTextView;

    String response;
    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_item_details);
        productNameTextView = (TextView) findViewById(R.id.product_name);
        costTextView = (TextView) findViewById(R.id.product_cost);
        quantityyTextView = (TextView) findViewById(R.id.products_left);
        discountTextView = (TextView) findViewById(R.id.current_discount);
        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF9900")));

        barcode = getIntent().getExtras().getString("Barcode");

        try {
            run("http://coa.hol.es/COA/get_product_details.php","main");
        } catch (IOException e) {
            e.printStackTrace();
        }
        
    }


    void run(String url, final String sender) throws IOException {
        RequestBody formBody = new FormEncodingBuilder()
                .add("barcode", barcode).build();

        Request request = new Request.Builder().url(url).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                ShowItemDetailsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        openDialog();
                    }
                });

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String resp = response.body().string();

                ShowItemDetailsActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if(sender.equals("main")) {
                            displayData(resp);
                        }
                        else if(sender.equals("done")){
                            displayDeletionResponse(resp);
                        }
                    }
                });

            }
        });
    }

    private void displayDeletionResponse(String resp) {

        try {
            JSONObject product = new JSONObject(resp);
            int success = product.getInt("success");

            AlertDialog.Builder builder = new AlertDialog.Builder(ShowItemDetailsActivity.this);
            if(success == 0){
                builder.setMessage("Something Went Wrong!")
                        .setTitle("Oops!");
            }
            else if(success == 1) {
                builder.setMessage("Data Successfully Sold!")
                        .setTitle("Done");
            }
            builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    finish();
                }
            });
            AlertDialog dialog = builder.create();
            dialog.setCanceledOnTouchOutside(false);
            dialog.show();

        } catch (JSONException e) {
            e.printStackTrace();
        }

    }

    public void displayData(String resp){
        try {
            JSONObject product = new JSONObject(resp);
            int success = product.getInt("success");

            if(success == 0){
                AlertDialog.Builder builder = new AlertDialog.Builder(ShowItemDetailsActivity.this);
                builder.setMessage("Data not found!")
                        .setTitle("Oops!");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                        finish();
                    }
                });
                AlertDialog dialog = builder.create();
                dialog.setCanceledOnTouchOutside(false);
                dialog.show();
            }else{
                productName = product.getString("name");
                cost = product.getString("price");
                quantity = product.getString("count");
                productNameTextView.setText(productName);
                costTextView.setText(cost);
                quantityyTextView.setText(quantity);
                discountTextView.setText("0%");
            }

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    public void onDoneClicked(View v){

        try {
            run("http://coa.hol.es/COA/delete_product.php","done");
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    public void onCancelClicked(View v){

        super.onBackPressed();

    }

    private void openDialog() {

        AlertDialog.Builder builder = new AlertDialog.Builder(ShowItemDetailsActivity.this);
        builder.setMessage("No Internet!")
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
