package com.bizconnectivity.gino.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;

public class GridViewAdapter extends BaseAdapter{

    private Context context;
    private final String[] categories;
    private final int[] categoriesImage;

    public GridViewAdapter(Context context, String[] categories, int[] categoriesImage) {

        this.context = context;
        this.categories = categories;
        this.categoriesImage = categoriesImage;
    }

    @Override
    public int getCount() {
        return categories.length;
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View grid;
        LayoutInflater layoutInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        if (convertView == null) {

            grid = new View(context);
            grid = layoutInflater.inflate(R.layout.grid_view_item, null);
            TextView mTextView = (TextView) grid.findViewById(R.id.gridText);
            ImageView mImageView = (ImageView) grid.findViewById(R.id.gridImage);

            mTextView.setText(categories[position]);
            mImageView.setImageResource(categoriesImage[position]);

        } else {

            grid = (View) convertView;
        }

        return grid;
    }
}
