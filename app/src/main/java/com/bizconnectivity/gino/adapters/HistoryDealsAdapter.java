package com.bizconnectivity.gino.adapters;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.models.UserDealModel;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class HistoryDealsAdapter extends RealmRecyclerViewAdapter<UserDealModel, HistoryDealsAdapter.ViewHolder> {

    private Context context;
    private List<UserDealModel> data;
    private AdapterCallBack adapterCallBack;

    public HistoryDealsAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<UserDealModel> data, AdapterCallBack adapterCallBack) {

        super(data, true);
        this.context = context;
        this.data = data;
        this.adapterCallBack = adapterCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.deal_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (data.get(position).getDeals().get(0).getDealImageFile() != null) {
            byte[] bloc = Base64.decode(data.get(position).getDeals().get(0).getDealImageFile(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
            holder.mImageViewDeal.setImageBitmap(bitmap);
        }

        holder.mTextViewTitle.setText(data.get(position).getDeals().get(0).getDealName());

        if (data.get(position).isRedeemed()) {
            holder.mTextViewStatus.setText("REDEEMED");
        } else {
            holder.mTextViewStatus.setText("EXPIRED");
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextViewTitle;
        public TextView mTextViewStatus;
        public ImageView mImageViewDeal;

        public ViewHolder(final View itemView) {

            super(itemView);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_title);
            mTextViewStatus = (TextView) itemView.findViewById(R.id.text_status);
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
