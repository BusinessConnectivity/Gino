package com.bizconnectivity.gino.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.models.FavEventModel;
import com.squareup.picasso.Picasso;

import java.util.List;

import io.realm.OrderedRealmCollection;
import io.realm.RealmRecyclerViewAdapter;

public class FavouriteEventAdapter extends RealmRecyclerViewAdapter<FavEventModel, FavouriteEventAdapter.ViewHolder> {

    private Context context;
    private List<FavEventModel> data;
    private AdapterCallBack adapterCallBack;

    public FavouriteEventAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<FavEventModel> data, AdapterCallBack adapterCallBack) {

        super(data, true);
        this.context = context;
        this.data = data;
        this.adapterCallBack = adapterCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.pulse_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (data.get(position).getEvents().get(0).getImageUrl() != null)
            Picasso.with(context).load(Uri.parse(data.get(position).getEvents().get(0).getImageUrl())).into(holder.mImageViewPulse);

        holder.mTextViewTitle.setText(data.get(position).getEvents().get(0).getEventName());
        holder.mTextViewDatetime.setText(data.get(position).getEvents().get(0).getEventStartDateTime());
        holder.mTextViewLocation.setText(data.get(position).getEvents().get(0).getEventLocation());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ImageView mImageViewPulse;
        public TextView mTextViewTitle;
        public TextView mTextViewDatetime;
        public TextView mTextViewLocation;

        public ViewHolder(final View itemView) {

            super(itemView);

            mImageViewPulse = (ImageView) itemView.findViewById(R.id.image_pulse);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_title);
            mTextViewDatetime = (TextView) itemView.findViewById(R.id.text_datetime);
            mTextViewLocation = (TextView) itemView.findViewById(R.id.text_location);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapterCallBack.adapterOnClick(data.get(getAdapterPosition()).getEventID());
        }
    }

    public interface AdapterCallBack {
        void adapterOnClick(int adapterPosition);
    }
}