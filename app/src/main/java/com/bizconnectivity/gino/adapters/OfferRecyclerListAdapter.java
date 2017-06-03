package com.bizconnectivity.gino.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.asynctasks.CreateDismissedDealAsyncTask;
import com.bizconnectivity.gino.asynctasks.CreateFavouriteDealAsyncTask;
import com.bizconnectivity.gino.helpers.ItemTouchHelperAdapter;
import com.bizconnectivity.gino.helpers.ItemTouchHelperViewHolder;
import com.bizconnectivity.gino.models.Deal;

import java.util.List;

public class OfferRecyclerListAdapter extends RecyclerView.Adapter<OfferRecyclerListAdapter.ItemViewHolder> implements
        ItemTouchHelperAdapter {

    private List<Deal> data;
    private AdapterCallBack adapterCallBack;

    public OfferRecyclerListAdapter(List<Deal> data, AdapterCallBack adapterCallBack) {

        this.data = data;
        this.adapterCallBack = adapterCallBack;
    }

    public void swapData(List<Deal> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        if (data.get(0).getDealImageFile() != null) {
            byte[] bloc = Base64.decode(data.get(position).getDealImageFile(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
            holder.mImageViewDeal.setImageBitmap(bitmap);
        }
        holder.mTextViewTitle.setText(data.get(position).getDealName());
        holder.mTextViewLocation.setText(data.get(position).getDealLocation());
        holder.mTextViewUsualPrice.setText(data.get(position).getDealUsualPrice());
        holder.mTextViewUsualPrice.setPaintFlags(holder.mTextViewUsualPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.mTextViewPromoPrice.setText(data.get(position).getDealPromoPrice());
    }

    // Swipe Left
    @Override
    public void onItemLeftSwipe(int userId, int position) {

        if (userId != 0) new CreateDismissedDealAsyncTask(userId, data.get(position).getDealID()).execute();

        data.remove(position);
        notifyItemRemoved(position);
    }

    // Swipe Right
    @Override
    public void onItemRightSwipe(int userId, int position) {

        if (userId != 0) new CreateFavouriteDealAsyncTask(userId, data.get(position).getDealID()).execute();

        data.add(position, data.get(position));
        notifyItemInserted(position);
        data.remove(position + 1);
        notifyItemRemoved(position + 1);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


    class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder, View.OnClickListener {

        TextView mTextViewTitle;
        TextView mTextViewLocation;
        TextView mTextViewUsualPrice;
        TextView mTextViewPromoPrice;
        ImageView mImageViewDeal;

        public ItemViewHolder(View itemView) {

            super(itemView);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_title);
            mTextViewLocation = (TextView) itemView.findViewById(R.id.text_location);
            mTextViewUsualPrice = (TextView) itemView.findViewById(R.id.text_usual_price);
            mTextViewPromoPrice = (TextView) itemView.findViewById(R.id.text_promo_price);
            mImageViewDeal = (ImageView) itemView.findViewById(R.id.image_deal);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }

        @Override
        public void onClick(View v) {
            adapterCallBack.dealAdapterOnClick(data.get(getAdapterPosition()).getDealID());
        }
    }

    public interface AdapterCallBack {
        void dealAdapterOnClick(int adapterPosition);
    }
}
