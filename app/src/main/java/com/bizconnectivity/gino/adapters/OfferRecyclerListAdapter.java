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
import com.bizconnectivity.gino.activities.DealsActivity;
import com.bizconnectivity.gino.helpers.ItemTouchHelperAdapter;
import com.bizconnectivity.gino.helpers.ItemTouchHelperViewHolder;
import com.bizconnectivity.gino.models.DealList;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class OfferRecyclerListAdapter extends RealmRecyclerViewAdapter<DealList, OfferRecyclerListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter{

    private List<DealList> dealLists = new ArrayList<>();
    private Context context;
    private Realm realm;
    AdapterCallBack adapterCallBack;

    public OfferRecyclerListAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<DealList> data, boolean autoUpdate, Realm realm, AdapterCallBack adapterCallBack) {

        super(data, autoUpdate);
        this.context = context;
        this.dealLists = data;
        this.realm = realm;
        this.adapterCallBack = adapterCallBack;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_list_item, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        if (!dealLists.get(position).getDealImageURL().isEmpty())
            Picasso.with(context).load(dealLists.get(position).getDealImageURL()).into(holder.mImageViewDeal);

        holder.mTextViewTitle.setText(dealLists.get(position).getDealTitle());
        holder.mTextViewLocation.setText(dealLists.get(position).getDealLocation());
        holder.mTextViewPrice.setText(dealLists.get(position).getDealPrice());
    }



    @Override
    public void onItemLeftSwipe(int position) {

        // Update Deal to Dismissed
        updateDismissedDealList(position);

        dealLists.remove(position);
        notifyItemRemoved(position);
    }

    private void updateDismissedDealList(final int position) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                DealList dealList = realm.where(DealList.class).equalTo("dealID", dealLists.get(position).getDealID()).findFirst();
                dealList.setIsFavorite("No");
                realm.copyToRealmOrUpdate(dealList);
            }
        });
    }

    @Override
    public void onItemRightSwipe(int position) {

        // Update Deal to Favorite
        updateFavoriteDealList(position);

        dealLists.add(position, dealLists.get(position));
        notifyItemInserted(position);

        dealLists.remove(position + 1);
        notifyItemRemoved(position + 1);
    }

    private void updateFavoriteDealList(final int position) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                DealList dealList = realm.where(DealList.class).equalTo("dealID", dealLists.get(position).getDealID()).findFirst();
                dealList.setIsFavorite("Yes");
                realm.copyToRealmOrUpdate(dealList);
            }
        });
    }

    @Override
    public int getItemCount() {
        return dealLists.size();
    }

    /**
     * Simple example of a view holder that implements {@link ItemTouchHelperViewHolder} and has a
     * "handle" view that initiates a drag event when touched.
     */
    public class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder, View.OnClickListener {

        public TextView mTextViewTitle;
        public TextView mTextViewLocation;
        public TextView mTextViewPrice;
        public ImageView mImageViewDeal;

        public ItemViewHolder(View itemView) {

            super(itemView);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_title);
            mTextViewLocation = (TextView) itemView.findViewById(R.id.text_location);
            mTextViewPrice = (TextView) itemView.findViewById(R.id.text_price);
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

            adapterCallBack.dealAdapterOnClick(getAdapterPosition());

            Intent intent = new Intent(context, DealsActivity.class);
            intent.putExtra("POSITION", dealLists.get(getAdapterPosition()).getDealID());
            context.startActivity(intent);
        }
    }

    public interface AdapterCallBack {
        void dealAdapterOnClick(int adapterPosition);
    }
}
