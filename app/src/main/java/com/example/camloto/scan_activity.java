package com.example.camloto;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.google.zxing.Result;

public class scan_activity extends AppCompatActivity {
    private static final int MY_CAMERA_REQUEST_CODE = 100;
    private CodeScanner mscanner;
    @Override
    protected void  onCreate(Bundle savedInstanceState){
        super.onCreate(savedInstanceState);
        setContentView(R.layout.main_activity);

        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
                == PackageManager.PERMISSION_DENIED){
            Toast.makeText(scan_activity.this,"No Camera", Toast.LENGTH_LONG).show();
        }else{

            ActivityCompat.requestPermissions(scan_activity.this, new String[] {Manifest.permission.CAMERA}, MY_CAMERA_REQUEST_CODE);

            CodeScannerView scannerView = findViewById(R.id.scanner_view);
            mscanner = new CodeScanner(this,scannerView);
            scannerView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mscanner.startPreview();
                }
            });

            mscanner.setDecodeCallback(new DecodeCallback() {
                @Override
                public void onDecoded(@NonNull Result result) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //Toast.makeText(scan_activity.this,result.getText(), Toast.LENGTH_LONG).show();
                            Intent myIntent = new Intent(scan_activity.this, home_activity.class);
                            myIntent.putExtra("qrcode", result.getText()); //Optional parameters
                            scan_activity.this.startActivity(myIntent);
                        }
                    });
                }
            });
        }


    }

    @Override
    protected  void  onPause(){
        mscanner.releaseResources();
        super.onPause();
    }
}
