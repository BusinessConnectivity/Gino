package com.bizconnectivity.gino.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.models.Deal;

import java.util.List;


public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder> {

    private List<Deal> data;
    private AdapterCallBack adapterCallBack;

    public SearchResultsListAdapter(List<Deal> dealLists, AdapterCallBack adapterCallBack) {
        this.data = dealLists;
        this.adapterCallBack = adapterCallBack;
    }

    public void swapData(List<Deal> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (data.get(position).getDealImageFile() != null) {
            byte[] bloc = Base64.decode(data.get(position).getDealImageFile(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
            holder.mImageViewDeal.setImageBitmap(bitmap);
        }

        holder.mTextViewTitle.setText(data.get(position).getDealName());
        holder.mTextViewLocation.setText(data.get(position).getDealLocation());
        holder.mTextViewPromoPrice.setText(data.get(position).getDealPromoPrice());
        holder.mTextViewUsualPrice.setText(data.get(position).getDealUsualPrice());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTextViewTitle;
        TextView mTextViewLocation;
        TextView mTextViewUsualPrice;
        TextView mTextViewPromoPrice;
        ImageView mImageViewDeal;

        public ViewHolder(final View itemView) {

            super(itemView);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_title);
            mTextViewLocation = (TextView) itemView.findViewById(R.id.text_location);
            mTextViewPromoPrice = (TextView) itemView.findViewById(R.id.text_promo_price);
            mTextViewUsualPrice = (TextView) itemView.findViewById(R.id.text_usual_price);
            mImageViewDeal = (ImageView) itemView.findViewById(R.id.image_deal);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapterCallBack.adapterOnClick(getAdapterPosition());
        }
    }

    public interface AdapterCallBack {
        void adapterOnClick(int adapterPosition);
    }
}
