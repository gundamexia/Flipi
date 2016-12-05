package com.example.dam212.flipi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Flipi extends Activity {

    TextView textViewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        initVariables();
        receiveBundle(savedInstanceState);
    }

    private void initVariables() {
        textViewText = (TextView) findViewById(R.id.textViewText);
    }

    private void receiveBundle(Bundle bundleConfig) {
        textViewText.setText(bundleConfig.getInt("seekBarRows"));
    }
}
