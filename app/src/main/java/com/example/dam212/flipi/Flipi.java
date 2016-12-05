package com.example.dam212.flipi;

import android.app.Activity;
import android.os.Bundle;
import android.widget.TextView;

public class Flipi extends Activity {

    private TextView textViewText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.flipi);

        // Bundle que contiene los extras que se hayan puesto desde otra actividad
        Bundle configs = getIntent().getExtras();

        initVariables();
        receiveBundle(configs);
    }

    private void initVariables() {
        textViewText = (TextView) findViewById(R.id.textViewText);
    }

    private void receiveBundle(Bundle configs) {
        textViewText.setText(String.valueOf(configs.getInt("seekBarRows")));
    }
}
