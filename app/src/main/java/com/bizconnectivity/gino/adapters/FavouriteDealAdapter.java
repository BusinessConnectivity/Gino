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
import com.bizconnectivity.gino.asynctasks.DeleteFavouriteDealAsyncTask;
import com.bizconnectivity.gino.helpers.ItemTouchHelperAdapter;
import com.bizconnectivity.gino.helpers.ItemTouchHelperViewHolder;
import com.bizconnectivity.gino.models.FavDealModel;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.Realm;
import io.realm.RealmRecyclerViewAdapter;

public class FavouriteDealAdapter extends RealmRecyclerViewAdapter<FavDealModel, FavouriteDealAdapter.ItemViewHolder> implements
        ItemTouchHelperAdapter {

    private List<FavDealModel> data;
    private Realm realm;
    private AdapterCallBack adapterCallBack;

    public FavouriteDealAdapter(@Nullable OrderedRealmCollection<FavDealModel> data, Realm realm, AdapterCallBack adapterCallBack) {

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
    public void onBindViewHolder(ItemViewHolder holder, int position) {

        if (data.get(position).getDeals().get(0).getDealImageFile() != null) {
            byte[] bloc = Base64.decode(data.get(position).getDeals().get(0).getDealImageFile(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
            holder.mImageViewDeal.setImageBitmap(bitmap);
        }
        holder.mTextViewTitle.setText(data.get(position).getDeals().get(0).getDealName());
        holder.mTextViewLocation.setText(data.get(position).getDeals().get(0).getDealLocation());
        holder.mTextViewPrice.setText(data.get(position).getDeals().get(0).getDealPromoPrice());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onItemLeftSwipe(final int position) {

        new DeleteFavouriteDealAsyncTask(data.get(position).getUserFavDealID()).execute();

        realm.executeTransaction(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {

                realm.where(FavDealModel.class).equalTo("userFavDealID", data.get(position).getUserFavDealID()).findFirst().deleteFromRealm();
            }
        });
    }

    @Override
    public void onItemRightSwipe(int position) {

    }

    class ItemViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder, View.OnClickListener {

        public TextView mTextViewTitle;
        public TextView mTextViewLocation;
        public TextView mTextViewPrice;
        public ImageView mImageViewDeal;

        public ItemViewHolder(final View itemView) {

            super(itemView);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_title);
            mTextViewLocation = (TextView) itemView.findViewById(R.id.text_location);
            mTextViewPrice = (TextView) itemView.findViewById(R.id.text_price);
            mImageViewDeal = (ImageView) itemView.findViewById(R.id.image_deal);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapterCallBack.adapterOnClick(data.get(getAdapterPosition()).getDealID());
        }

        @Override
        public void onItemSelected() {

        }

        @Override
        public void onItemClear() {

        }
    }

    public interface AdapterCallBack {
        void adapterOnClick(int adapterPosition);
    }
}
