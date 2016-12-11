package com.example.dam212.flipi;

import android.content.Context;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.widget.ListView;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;

public class score extends AppCompatActivity{
    ListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.score);

        if(genList() == null) mAdapter = new ListAdapter(this, R.id.listViewScore , Arrays.asList(genNotFound()));
        else mAdapter = new ListAdapter(this, R.id.listViewScore , genList());

        ((ListView)findViewById(R.id.listViewScore)).setAdapter(mAdapter);
    }

    /**
     * Busca en la memoria interna y externa los resultados de los jugadores para ponerlos en un array
     * de ListItem.
     * @return array lista de ListItem
     */
    private ArrayList<ListItem> genList() {
        String fileName = "results.txt";
        FileInputStream fis = null;
        ArrayList<ListItem> lista = null;
        try {
            File file = new File(this.getFilesDir(), fileName);
            File fileSD = new File(Environment.getExternalStorageDirectory().toString() + "/flipi/" + fileName);
            if(file.exists()) {
                fis = new FileInputStream(file);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String data[] = line.split(",");
                    lista.add(new ListItem(data[1], data[2], data[0]));
                }
            }
            if(fileSD.exists()) {
                fis = new FileInputStream(fileSD);
                InputStreamReader isr = new InputStreamReader(fis);
                BufferedReader bufferedReader = new BufferedReader(isr);
                String line;
                while ((line = bufferedReader.readLine()) != null) {
                    String data[] = line.split(",");
                    lista.add(new ListItem(data[1], data[2], data[0]));
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (fis != null) try {
                fis.close();
            } catch (IOException e) {
                //e.printStackTrace();
            }
        }
        return lista;
    }

    private ListItem[] genNotFound(){
        return new ListItem[]{
                new ListItem("List", "not", "found.")
        };
    }

}
