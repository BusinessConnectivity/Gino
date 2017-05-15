package com.bizconnectivity.gino.adapters;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.activities.DealsActivity;
import com.bizconnectivity.gino.activities.PulseDetailActivity;
import com.bizconnectivity.gino.models.PulseList;
import com.bumptech.glide.Glide;
import com.squareup.picasso.Picasso;

import java.io.Serializable;
import java.util.List;

public class PulseRecyclerListAdapter extends RecyclerView.Adapter<PulseRecyclerListAdapter.ViewHolder>{

    private Context context;
    private List<PulseList> pulseLists;
    AdapterCallBack adapterCallBack;

    public PulseRecyclerListAdapter(Context context, List<PulseList> pulseLists, AdapterCallBack adapterCallBack) {

        this.context = context;
        this.pulseLists = pulseLists;
        this.adapterCallBack = adapterCallBack;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.pulse_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (!pulseLists.get(position).getPulseImage().isEmpty())
            Picasso.with(context).load(pulseLists.get(position).getPulseImage()).into(holder.mImageViewPulse);

        holder.mTextViewTitle.setText(pulseLists.get(position).getPulseTitle());
        holder.mTextViewDatetime.setText(pulseLists.get(position).getPulseDatetime());
        holder.mTextViewLocation.setText(pulseLists.get(position).getPulseLocation());
    }

    @Override
    public int getItemCount() {
        return pulseLists.size();
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
        public void onClick(View view) {

            adapterCallBack.adapterOnClick(getAdapterPosition());

            Intent intent = new Intent(context, PulseDetailActivity.class);
            intent.putExtra("PULSE", (Serializable) pulseLists);
            intent.putExtra("POSITION", getAdapterPosition());
            context.startActivity(intent);
        }
    }

    public interface AdapterCallBack {
        void adapterOnClick(int adapterPosition);
    }
}
