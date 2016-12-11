package com.example.dam212.flipi;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.List;

public class ListAdapter extends ArrayAdapter<ListItem> {

    public ListAdapter(Context context, int resource, List<ListItem> items) {
        super(context, resource, items);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = convertView;

        if (view == null) view = (LayoutInflater.from(getContext())).inflate(R.layout.list_view_item, null);

        ListItem item = getItem(position);

        if (item != null) {
            TextView name = (TextView) view.findViewById(R.id.textViewNombre);
            TextView date = (TextView) view.findViewById(R.id.textViewDate);
            TextView time = (TextView) view.findViewById(R.id.textViewTime);
            TextView score = (TextView) view.findViewById(R.id.textViewScore);

            if (name != null) {
                name.setText(item.getName());
            }

            if (date != null) {
                date.setText(item.getDate());
            }

            if (time != null) {
                time.setText(item.getTime());
            }

            if (score != null) {
                score.setText(item.getScore());
            }
        }

        return view;
    }

}
