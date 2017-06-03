package com.bizconnectivity.gino.adapters;

import android.content.Context;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.models.Event;
import com.squareup.picasso.Picasso;

import java.text.ParseException;
import java.util.List;

import static com.bizconnectivity.gino.Constant.format1;
import static com.bizconnectivity.gino.Constant.format2;

public class PulseRecyclerListAdapter extends RecyclerView.Adapter<PulseRecyclerListAdapter.ViewHolder> {

    private Context context;
    private List<Event> data;
    private AdapterCallBack adapterCallBack;

    public PulseRecyclerListAdapter(Context context, List<Event> data, AdapterCallBack adapterCallBack) {

        this.context = context;
        this.data = data;
        this.adapterCallBack = adapterCallBack;
    }

    public void swapData(List<Event> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.pulse_list_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (data.get(position).getImageUrl() != null)
            Picasso.with(context).load(Uri.parse(data.get(position).getImageUrl())).into(holder.mImageViewPulse);

        holder.mTextViewTitle.setText(data.get(position).getEventName());
        try {
            holder.mTextViewDatetime.setText(format2.format(format1.parse(data.get(position).getEventStartDateTime())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        holder.mTextViewLocation.setText(data.get(position).getEventLocation());
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        ImageView mImageViewPulse;
        TextView mTextViewTitle;
        TextView mTextViewDatetime;
        TextView mTextViewLocation;

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
            adapterCallBack.adapterOnClick(data.get(getAdapterPosition()).getEventID());
        }
    }

    public interface AdapterCallBack {
        void adapterOnClick(int adapterPosition);
    }
}
