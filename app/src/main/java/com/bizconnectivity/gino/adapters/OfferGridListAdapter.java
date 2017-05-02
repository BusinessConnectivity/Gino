package com.bizconnectivity.gino.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;

public class OfferGridListAdapter extends RecyclerView.Adapter<OfferGridListAdapter.ViewHolder>{

    private LayoutInflater mInflater;
    private String[] categories;
    private int[] categoriesImage;
    private ItemClickListener mClickListener;

    // Constructor
    public OfferGridListAdapter(Context context, String[] categories, int[] categoriesImage) {

        this.mInflater = LayoutInflater.from(context);
        this.categories = categories;
        this.categoriesImage = categoriesImage;
    }

    // Inflates the layout from xml when needed
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = mInflater.inflate(R.layout.grid_view_item, parent, false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    // Binds Data
    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        holder.mTextView.setText(categories[position]);
        holder.mImageView.setImageResource(categoriesImage[position]);
    }

    // Get Total Items
    @Override
    public int getItemCount() {
        return categories.length;
    }

    // View Holder and Onclick of RecycleView
    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(View itemView) {

            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.gridText);
            mImageView = (ImageView) itemView.findViewById(R.id.gridImage);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            if (mClickListener != null) mClickListener.onItemClick(view, getAdapterPosition());
        }
    }

    // Onclick Item Position
    public String getItem(int id) {
        return categories[id];
    }

    // Item Click Event
    public void setClickListener(ItemClickListener itemClickListener) {
        this.mClickListener = itemClickListener;
    }

    // Implement to Parent Activity for Click Event
    public interface ItemClickListener {
        void onItemClick(View view, int position);
    }
}
