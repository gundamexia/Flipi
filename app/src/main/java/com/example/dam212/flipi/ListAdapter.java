package com.example.dam212.flipi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

/**
 * Adaptador personalizado para poder crear las vistas que queramos en la clase Score.
 */
public class ListAdapter extends ArrayAdapter<ListItem> {

    /**
     * El constructor de la clase, básicamente un wrapper de ArrayAdapter<T>, un adaptador propio.
     * @param context Context - Contexto donde se creará.
     * @param resource int - Id del recurso donde se añadirá.
     * @param items List<ListItem> - Lista de todos los elementos que queremos representar.
     */
    public ListAdapter(Context context, int resource, List<ListItem> items) {
        super(context, resource, items);
    }

    /**
     * Callback del adaptador.
     * @param position int - posición del elemento a adquirir.
     * @param convertView View - elemento el cual va a ser la base atómica del ViewList.
     * @param parent ViewGroup - padre del elemento. No usado.
     * @return View - retorna la vista compuesta.
     */
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) view = (LayoutInflater.from(getContext())).inflate(R.layout.list_view_item, null);

        ListItem item = getItem(position);

        if (item != null) {
            TextView name = (TextView) view.findViewById(R.id.textViewNombre);
            TextView date = (TextView) view.findViewById(R.id.textViewDate);
            TextView score = (TextView) view.findViewById(R.id.textViewScore);

            if (name != null) {
                name.setText(item.getName());
            }

            if (date != null) {
                date.setText(item.getDate());
            }

            if (score != null) {
                score.setText(item.getScore());
            }
        }

        return view;
    }

}
