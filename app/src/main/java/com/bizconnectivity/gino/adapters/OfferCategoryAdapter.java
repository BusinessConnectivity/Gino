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
import com.bizconnectivity.gino.models.DealCategoryModel;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class OfferCategoryAdapter extends RealmRecyclerViewAdapter<DealCategoryModel, OfferCategoryAdapter.ViewHolder> {

    private Context context;
    private List<DealCategoryModel> data;
    AdapterCallBack adapterCallBack;

    public OfferCategoryAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<DealCategoryModel> data, boolean autoUpdate, AdapterCallBack adapterCallBack) {

        super(data, autoUpdate);
        this.context = context;
        this.data = data;
        this.adapterCallBack = adapterCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfferCategoryAdapter.ViewHolder holder, int position) {

        byte[] bloc = Base64.decode(data.get(position).getImageFile(), Base64.DEFAULT);
        Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);

        holder.mTextView.setText(data.get(position).getDealCategoryName());
        holder.mImageView.setImageBitmap(bitmap);
    }

    @Override
    public int getItemCount() {
        return data.size();
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

            adapterCallBack.categoryAdapterOnClick(getAdapterPosition());
        }
    }

    public interface AdapterCallBack {
        void categoryAdapterOnClick(int adapterPosition);
    }
}
