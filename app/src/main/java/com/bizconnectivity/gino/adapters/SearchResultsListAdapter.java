package com.bizconnectivity.gino.adapters;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.DealsActivity;
import com.bizconnectivity.gino.models.DealList;
import com.squareup.picasso.Picasso;

import java.util.List;


public class SearchResultsListAdapter extends RecyclerView.Adapter<SearchResultsListAdapter.ViewHolder> {

    private Context context;
    private List<DealList> dealLists;
    AdapterCallBack adapterCallBack;

    public SearchResultsListAdapter(Context context, List<DealList> dealLists, AdapterCallBack adapterCallBack) {

        this.context = context;
        this.dealLists = dealLists;
        this.adapterCallBack = adapterCallBack;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (!dealLists.get(position).getDealImageURL().isEmpty())
            Picasso.with(context).load(dealLists.get(position).getDealImageURL()).into(holder.mImageViewDeal);

        holder.mTextViewTitle.setText(dealLists.get(position).getDealTitle());
        holder.mTextViewLocation.setText(dealLists.get(position).getDealLocation());
        holder.mTextViewPrice.setText(dealLists.get(position).getDealPrice());
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.deal_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public int getItemCount() {
        return dealLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextViewTitle;
        public TextView mTextViewLocation;
        public TextView mTextViewPrice;
        public ImageView mImageViewDeal;

        public ViewHolder(final View itemView) {

            super(itemView);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_title);
            mTextViewLocation = (TextView) itemView.findViewById(R.id.text_location);
            mTextViewPrice = (TextView) itemView.findViewById(R.id.text_price);
            mImageViewDeal = (ImageView) itemView.findViewById(R.id.image_deal);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            adapterCallBack.adapterOnClick(getAdapterPosition());

            Intent intent = new Intent(context, DealsActivity.class);
            intent.putExtra("POSITION", dealLists.get(getAdapterPosition()).getDealID());
            context.startActivity(intent);
        }
    }

    public interface AdapterCallBack {
        void adapterOnClick(int adapterPosition);
    }
}
