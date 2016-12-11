package com.example.dam212.flipi;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.util.Arrays;

public class score extends AppCompatActivity{
    ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        mAdapter = new ListAdapter(this, R.id.listViewScore , Arrays.asList(genTest()) );

        ((ListView)findViewById(R.id.listViewScore)).setAdapter(mAdapter);
    }


    private ListItem[] genTest(){
        return new ListItem[]{
                new ListItem("a", "b", "c", "d"),
                new ListItem("e", "f", "g", "h"),
                new ListItem("i", "j", "k", "l")
        };
    }

}
