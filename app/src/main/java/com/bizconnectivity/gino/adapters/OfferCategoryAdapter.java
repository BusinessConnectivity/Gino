package com.bizconnectivity.gino.adapters;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.PictureDrawable;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.DealsListActivity;
import com.bizconnectivity.gino.helpers.SvgDecoder;
import com.bizconnectivity.gino.helpers.SvgDrawableTranscoder;
import com.bizconnectivity.gino.helpers.SvgSoftwareLayerSetter;
import com.bizconnectivity.gino.models.DealCategoryList;
import com.bumptech.glide.GenericRequestBuilder;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.model.StreamEncoder;
import com.bumptech.glide.load.resource.file.FileToStreamDecoder;
import com.caverock.androidsvg.SVG;

import java.io.InputStream;
import java.util.List;

public class OfferCategoryAdapter extends RecyclerView.Adapter<OfferCategoryAdapter.ViewHolder>{

    private Context context;
    private List<DealCategoryList> dealCategoryLists;
    AdapterCallBack adapterCallBack;
    private GenericRequestBuilder<Uri, InputStream, SVG, PictureDrawable> requestBuilder;

    public OfferCategoryAdapter(Context context, List<DealCategoryList> dealCategoryLists, AdapterCallBack adapterCallBack) {

        this.context = context;
        this.dealCategoryLists = dealCategoryLists;
        this.adapterCallBack = adapterCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.category_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(OfferCategoryAdapter.ViewHolder holder, int position) {

        requestBuilder = Glide.with(context)
                .using(Glide.buildStreamModelLoader(Uri.class, context), InputStream.class)
                .from(Uri.class)
                .as(SVG.class)
                .transcode(new SvgDrawableTranscoder(), PictureDrawable.class)
                .sourceEncoder(new StreamEncoder())
                .cacheDecoder(new FileToStreamDecoder<SVG>(new SvgDecoder()))
                .decoder(new SvgDecoder())
                .placeholder(R.drawable.image_loading)
                .error(R.drawable.image_error)
                .animate(android.R.anim.fade_in)
                .listener(new SvgSoftwareLayerSetter<Uri>());

        Uri uri = Uri.parse(dealCategoryLists.get(position).getCategoryImageURL());

        if (!dealCategoryLists.get(position).getCategoryImageURL().isEmpty())
            requestBuilder.diskCacheStrategy(DiskCacheStrategy.SOURCE).load(uri).into(holder.mImageView);

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

            adapterCallBack.categoryAdapterOnClick(getAdapterPosition());
//
//            Intent intent = new Intent(context, DealsListActivity.class);
//            intent.putExtra("POSITION", dealCategoryLists.get(getAdapterPosition()).getCategoryID());
//            context.startActivity(intent);
        }
    }

    public interface AdapterCallBack {
        void categoryAdapterOnClick(int adapterPosition);
    }
}
