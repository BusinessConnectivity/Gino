package com.bizconnectivity.gino.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.annotation.Nullable;
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
import com.bizconnectivity.gino.models.DealModel;
import com.bizconnectivity.gino.models.UserModel;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class OfferRecyclerListAdapter extends RealmRecyclerViewAdapter<DealModel, OfferRecyclerListAdapter.ItemViewHolder> implements
        ItemTouchHelperAdapter {

    private List<DealModel> data;
    private Realm realm;
    private AdapterCallBack adapterCallBack;

    public OfferRecyclerListAdapter(@Nullable OrderedRealmCollection<DealModel> data, Realm realm, AdapterCallBack adapterCallBack) {

        super(data, true);
        this.data = data;
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

        if (data.get(0).getDealImageFile() != null) {
            byte[] bloc = Base64.decode(data.get(position).getDealImageFile(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
            holder.mImageViewDeal.setImageBitmap(bitmap);
        }
        holder.mTextViewTitle.setText(data.get(position).getDealName());
        holder.mTextViewLocation.setText(data.get(position).getDealLocation());
        holder.mTextViewPrice.setText(data.get(position).getDealPromoPrice());
    }

    // Swipe Left
    @Override
    public void onItemLeftSwipe(final int position) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                DealModel deal = realm.where(DealModel.class).equalTo("dealID", data.get(position).getDealID()).findFirst();
                deal.setDismissed(true);
                realm.copyToRealmOrUpdate(deal);
            }
        });

        UserModel user = realm.where(UserModel.class).findFirst();

        new CreateDismissedDealAsyncTask(user.getUserID(), data.get(position).getDealID()).execute();
    }

    // Swipe Right
    @Override
    public void onItemRightSwipe(final int position) {

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                DealModel deal = realm.where(DealModel.class).equalTo("dealID", data.get(position).getDealID()).findFirst();
                deal.setDismissed(false);
                realm.copyToRealmOrUpdate(deal);
            }
        });

        UserModel userModel = realm.where(UserModel.class).findFirst();

        new CreateFavouriteDealAsyncTask(userModel.getUserID(), data.get(position).getDealID()).execute();
    }

    @Override
    public int getItemCount() {
        return data.size();
    }


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
            adapterCallBack.dealAdapterOnClick(data.get(getAdapterPosition()).getDealID());
        }
    }

    public interface AdapterCallBack {
        void dealAdapterOnClick(int adapterPosition);
    }
}
