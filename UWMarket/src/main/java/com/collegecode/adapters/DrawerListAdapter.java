package com.collegecode.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.collegecode.uwmarket.R;

/**
 * Created by saurabh on 5/20/14.
 */
public class DrawerListAdapter extends BaseAdapter {
    String titles[];
    int img_resources[];
    private static LayoutInflater inflater=null;

    public DrawerListAdapter(Context context, String title[],  int img[]){
        this.titles = title;
        this.img_resources = img;
        inflater = (LayoutInflater)context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

    }
    @Override
    public int getCount() {
        return titles.length;
    }

    @Override
    public Object getItem(int i) {
        return i;
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View vi = view;
        if(vi==null)
            vi = inflater.inflate(R.layout.list_item_drawer, null);

        TextView lbl_drawer = (TextView) vi.findViewById(R.id.lbl_drawer_title);
        ImageView img_drawer = (ImageView) vi.findViewById(R.id.img_drawer);

        lbl_drawer.setText(titles[i]);
        img_drawer.setImageResource(img_resources[i]);

        return vi;
    }
}
