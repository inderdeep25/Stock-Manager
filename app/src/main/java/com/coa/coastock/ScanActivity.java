package com.coa.coastock;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.zxing.Result;

import me.dm7.barcodescanner.zxing.ZXingScannerView;


public class ScanActivity extends AppCompatActivity implements ZXingScannerView.ResultHandler{

    private ZXingScannerView mScannerView;
    private String TAG="ScanActivity";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mScannerView = new ZXingScannerView(this);
        setContentView(mScannerView);
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this); // Register ourselves as a handler for scan results.
        mScannerView.startCamera();          // Start camera on resume
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();           // Stop camera on pause
    }

    @Override
    public void handleResult(Result rawResult) {

//        Log.v(TAG, rawResult.getText()); // Prints scan results
       //Log.v(TAG, rawResult.getBarcodeFormat().toString()); // Prints the scan format (qrcode, pdf417 etc.)

        int calledFrom = getIntent().getExtras().getInt("Called From",2);
        if(calledFrom == 0){
            Intent openShowDataActivity = new Intent(getApplicationContext(), ShowItemDetailsActivity.class);
            openShowDataActivity.putExtra("Barcode",rawResult.getText().toString());
            openShowDataActivity.putExtra("Product Name", "Casrol Oil");
            openShowDataActivity.putExtra("Cost", "Rs. 250");
            openShowDataActivity.putExtra("Quantity Left", "17");
            openShowDataActivity.putExtra("Discount", "0%");
            startActivity(openShowDataActivity);
            finish();
        }
        if(calledFrom == 1){
            Intent openAddDataActivity = new Intent(getApplicationContext(),AddDataActivity.class);
            openAddDataActivity.putExtra("Barcode",rawResult.getText().toString());
            startActivity(openAddDataActivity);
            finish();
        }

        if(calledFrom == 2){
            super.onBackPressed();
        }

    }
}
