package com.coa.coastock;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.EditText;
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


public class AddDataActivity extends AppCompatActivity {

    EditText productNameEditText, costEditTex, discountEditText;
    TextView barcodeTextView;
    String productName, cost, discount, barcode;

    OkHttpClient client = new OkHttpClient();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_data);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF9900")));
        barcodeTextView = (TextView) findViewById(R.id.barcode);
        productNameEditText = (EditText) findViewById(R.id.product_name_edit_text);
        costEditTex = (EditText) findViewById(R.id.product_cost_edit_text);
        discountEditText = (EditText) findViewById(R.id.current_discount_edit_text);

        barcode = getIntent().getExtras().getString("Barcode");
        barcodeTextView.setText(barcode);
    }

    public void onAddClicked(View v) {

        productName = productNameEditText.getText().toString();
        cost = costEditTex.getText().toString();
        discount = discountEditText.getText().toString();

        //now save it to database
        if (!productName.matches("") && !cost.matches("") && !discount.matches("")) {
            try {
                run("http://coa.hol.es/COA/create_product.php");
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            openDialog("One of the fields is empty!!");
        }

    }

    void run(String url) throws IOException {
        RequestBody formBody = new FormEncodingBuilder()
                .add("barcode", barcode)
                .add("name", productName)
                .add("price", "Rs. "+cost).build();

        Request request = new Request.Builder().url(url).post(formBody).build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Request request, IOException e) {

                AddDataActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        openDialog("No Internet!");
                    }
                });

            }

            @Override
            public void onResponse(final Response response) throws IOException {
                final String resp = response.body().string();
                int success = 5;
                try {
                    JSONObject responseJSON = new JSONObject(resp);
                    success = responseJSON.getInt("success");

                } catch (JSONException e) {
                    e.printStackTrace();

                }


                final int finalSuccess = success;
                AddDataActivity.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {

                        AlertDialog.Builder builder = new AlertDialog.Builder(AddDataActivity.this);
                        if (finalSuccess == 0) {
                            builder.setMessage("Wrong or Already Existing Barcode!")
                                    .setTitle("Oops!");
                        } else if (finalSuccess == 1) {
                            builder.setMessage("Data Successfully Added!")
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
                    }
                });

            }
        });
    }

    private void openDialog(String message) {

        AlertDialog.Builder builder = new AlertDialog.Builder(AddDataActivity.this);
        builder.setMessage(message)
                .setTitle("Oops!");
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int id) {
                dialog.dismiss();
            }
        });
        AlertDialog dialog = builder.create();
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();

    }
}
