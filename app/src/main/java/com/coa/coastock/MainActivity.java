package com.coa.coastock;

import android.support.v7.app.ActionBar;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.coa.coastock.R;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ActionBar bar = getSupportActionBar();
        bar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#FF9900")));
    }

    public void onSalePurchaseButtonClicked(View v){
        Intent openScanActivity = new Intent(getApplicationContext(),ScanActivity.class);
        openScanActivity.putExtra("Called From",0);
        startActivity(openScanActivity);
    }

    public void onAddDataButtonClicked(View v){
        Intent openScanActivity = new Intent(getApplicationContext(),ScanActivity.class);
        openScanActivity.putExtra("Called From",1);
        startActivity(openScanActivity);
    }

    public void onProductsClicked(View v){
        Intent displayProductsActivity = new Intent(getApplicationContext(),DisplayAllProducts.class);
        startActivity(displayProductsActivity);
    }
}
