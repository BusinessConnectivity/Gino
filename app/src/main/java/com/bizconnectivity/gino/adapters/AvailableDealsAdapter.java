package com.bizconnectivity.gino.adapters;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bizconnectivity.gino.R;
import com.bizconnectivity.gino.models.PurchasedDeal;

import java.text.ParseException;
import java.util.List;

import static com.bizconnectivity.gino.Constant.format1;
import static com.bizconnectivity.gino.Constant.format3;

public class AvailableDealsAdapter extends RecyclerView.Adapter<AvailableDealsAdapter.ViewHolder> {

    private List<PurchasedDeal> data;
    private AdapterCallBack adapterCallBack;

    public AvailableDealsAdapter(List<PurchasedDeal> data, AdapterCallBack adapterCallBack) {

        this.data = data;
        this.adapterCallBack = adapterCallBack;
    }

    public void swapData(List<PurchasedDeal> newData) {
        data = newData;
        notifyDataSetChanged();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.deal_available_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {

        if (data.get(position).getImageFile() != null) {
            byte[] bloc = Base64.decode(data.get(position).getImageFile(), Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(bloc, 0, bloc.length);
            holder.mImageViewDeal.setImageBitmap(bitmap);
        }

        if (data.get(position).getDealName() != null && !data.get(position).getDealName().isEmpty())
            holder.mTextViewTitle.setText(data.get(position).getDealName());

        try {
            holder.mTextViewRedeemEnd.setText(format3.format(format1.parse(data.get(position).getDealRedeemEndDate())));
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        TextView mTextViewTitle;
        TextView mTextViewRedeemEnd;
        ImageView mImageViewDeal;

        public ViewHolder(final View itemView) {

            super(itemView);
            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_title);
            mTextViewRedeemEnd = (TextView) itemView.findViewById(R.id.text_redeem_end);
            mImageViewDeal = (ImageView) itemView.findViewById(R.id.image_deal);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            adapterCallBack.adapterOnClick(getAdapterPosition());
        }
    }

    public interface AdapterCallBack {
        void adapterOnClick(int adapterPosition);
    }
}
