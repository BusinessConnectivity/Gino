package com.bizconnectivity.gino.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.DealHistoryActivity;
import com.bizconnectivity.gino.models.DealList;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class HistoryDealsAdapter extends RealmRecyclerViewAdapter<DealList, HistoryDealsAdapter.ViewHolder> {

    private Context context;
    private List<DealList> dealLists;
    AdapterCallBack adapterCallBack;

    public HistoryDealsAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<DealList> data, AdapterCallBack adapterCallBack) {

        super(data, true);
        this.context = context;
        this.dealLists = data;
        this.adapterCallBack = adapterCallBack;
    }

//    public HistoryDealsAdapter(Context context, List<DealList> dealLists, AdapterCallBack adapterCallBack) {
//
//        this.context = context;
//        this.dealLists = dealLists;
//        this.adapterCallBack = adapterCallBack;
//    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.deal_history_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (!dealLists.get(position).getDealImageURL().isEmpty())
            Picasso.with(context).load(dealLists.get(position).getDealImageURL()).into(holder.mImageViewDeal);

        holder.mTextViewTitle.setText(dealLists.get(position).getDealTitle());
        holder.mTextViewStatus.setText(dealLists.get(position).getIsExpired());
    }

    @Override
    public int getItemCount() {
        return dealLists.size();
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

            Intent intent = new Intent(context, DealHistoryActivity.class);
            intent.putExtra("POSITION", dealLists.get(getAdapterPosition()).getDealID());
            context.startActivity(intent);
        }
    }

    public interface AdapterCallBack {
        void adapterOnClick(int adapterPosition);
    }
}
