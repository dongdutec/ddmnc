package com.dongdutec.ddmnc.ui.home.activity;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.dongdutec.ddmnc.R;
import com.dongdutec.ddmnc.base.BaseActivity;
import com.dongdutec.ddmnc.utils.rx.rxbinding.RxViewAction;
import com.journeyapps.barcodescanner.CaptureManager;
import com.journeyapps.barcodescanner.DecoratedBarcodeView;

import rx.functions.Action1;

public class ScanActivity extends BaseActivity {
    private ImageView bar_left_img;
    private TextView bar_title_text;
    private TextView bar_right_text;
    private CaptureManager capture;
    private ImageButton buttonLed;
    private DecoratedBarcodeView barcodeScannerView;
    private boolean bTorch = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        barcodeScannerView = initializeContent();
        buttonLed = findViewById(R.id.button_led);
        bar_left_img = findViewById(R.id.bar_left_img);
        bar_title_text = findViewById(R.id.bar_title_text);
        bar_right_text = findViewById(R.id.bar_right_text);
        //返回
        RxViewAction.clickNoDouble(bar_left_img).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                finish();
            }
        });
        bar_title_text.setText("扫一扫");
        /*bar_right_text.setText("相册");
        //相册
        RxViewAction.clickNoDouble(bar_right_text).subscribe(new Action1<Void>() {
            @Override
            public void call(Void aVoid) {
                Toast.makeText(ScanActivity.this, "相册", Toast.LENGTH_SHORT).show();
            }
        });*/

        /*根据闪光灯状态设置imagebutton*/
        barcodeScannerView.setTorchListener(new DecoratedBarcodeView.TorchListener() {
            @Override
            public void onTorchOn() {
                buttonLed.setBackground(getResources().getDrawable(R.mipmap.browser_check));
                bTorch = true;
            }

            @Override
            public void onTorchOff() {
                buttonLed.setBackground(getResources().getDrawable(R.mipmap.browser));
                bTorch = false;
            }
        });

        /*开关闪光灯*/
        buttonLed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (bTorch) {
                    barcodeScannerView.setTorchOff();
                } else {
                    barcodeScannerView.setTorchOn();
                }

            }
        });

        capture = new CaptureManager(this, barcodeScannerView);

        capture.initializeFromIntent(getIntent(), savedInstanceState);

        capture.decode();
    }


    /**
     * Override to use a different layout.
     *
     * @return the DecoratedBarcodeView
     */
    protected DecoratedBarcodeView initializeContent() {
        setContentView(R.layout.activity_scan);
        return (DecoratedBarcodeView) findViewById(R.id.dbv);
    }

    @Override
    protected void onResume() {
        super.onResume();
        capture.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        capture.onPause();
        barcodeScannerView.setTorchOff();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        capture.onDestroy();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        capture.onSaveInstanceState(outState);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String permissions[], @NonNull int[] grantResults) {
        capture.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        return barcodeScannerView.onKeyDown(keyCode, event) || super.onKeyDown(keyCode, event);
    }
}
