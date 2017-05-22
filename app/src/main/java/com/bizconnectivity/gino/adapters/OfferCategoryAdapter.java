package com.bizconnectivity.gino.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.models.DealCategoryList;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class OfferCategoryAdapter extends RealmRecyclerViewAdapter<DealCategoryList, OfferCategoryAdapter.ViewHolder> {

    private Context context;
    private List<DealCategoryList> dealCategoryLists;
    AdapterCallBack adapterCallBack;

    public OfferCategoryAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<DealCategoryList> data, boolean autoUpdate, AdapterCallBack adapterCallBack) {

        super(data, autoUpdate);
        this.context = context;
        this.dealCategoryLists = data;
        this.adapterCallBack = adapterCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfferCategoryAdapter.ViewHolder holder, int position) {

//        holder.mImageView.setImageResource(dealCategoryLists.get(position).getCategoryImageURL());
        Picasso.with(context).load(dealCategoryLists.get(position).getCategoryImageURL()).into(holder.mImageView);

        holder.mTextView.setText(dealCategoryLists.get(position).getCategoryTitle());
    }

    @Override
    public int getItemCount() {
        return dealCategoryLists.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public TextView mTextView;
        public ImageView mImageView;

        public ViewHolder(final View itemView) {

            super(itemView);

            mTextView = (TextView) itemView.findViewById(R.id.category_title);
            mImageView = (ImageView) itemView.findViewById(R.id.category_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {

            adapterCallBack.categoryAdapterOnClick(dealCategoryLists.get(getAdapterPosition()).getCategoryID());
        }
    }

    public interface AdapterCallBack {
        void categoryAdapterOnClick(int adapterPosition);
    }

    public void remove(int position) {

        dealCategoryLists.remove(position);
        notifyItemRemoved(position);
    }

    public void add(int position, DealCategoryList dealCategory) {

        dealCategoryLists.add(position, dealCategory);
        notifyItemInserted(position);
    }
}
