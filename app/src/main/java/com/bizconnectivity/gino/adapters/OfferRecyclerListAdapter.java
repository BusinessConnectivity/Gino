package com.bizconnectivity.gino.adapters;

import android.content.Context;
import android.content.Intent;
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

import java.util.ArrayList;
import java.util.List;

import static com.bizconnectivity.gino.Common.shortToast;

public class OfferRecyclerListAdapter extends RecyclerView.Adapter<OfferRecyclerListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter{

    private List<DealList> mItems = new ArrayList<>();
    private Context context;

    public OfferRecyclerListAdapter(Context context, List<DealList> mItems) {

        this.context = context;
        this.mItems = mItems;
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        holder.mTextViewTitle.setText(mItems.get(position).getDealTitle());
        holder.mTextViewLocation.setText(mItems.get(position).getDealLocation());
        holder.mTextViewPrice.setText(mItems.get(position).getDealPrice());
        holder.mImageViewDeal.setImageResource(mItems.get(position).getDealImage());
    }

    @Override
    public void onItemLeftSwipe(int position) {

        mItems.remove(position);
        notifyItemRemoved(position);
    }

    @Override
    public void onItemRightSwipe(int position) {

        mItems.add(position, mItems.get(position));
        notifyItemInserted(position);

        mItems.remove(position + 1);
        notifyItemRemoved(position + 1);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
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
        public ImageView mImageViewLove;

        public ItemViewHolder(View itemView) {

            super(itemView);

            mTextViewTitle = (TextView) itemView.findViewById(R.id.text_title);
            mTextViewLocation = (TextView) itemView.findViewById(R.id.text_location);
            mTextViewPrice = (TextView) itemView.findViewById(R.id.text_price);
            mImageViewDeal = (ImageView) itemView.findViewById(R.id.image_deal);
            mImageViewLove = (ImageView) itemView.findViewById(R.id.image_love);

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

            Intent intent = new Intent(context, DealsActivity.class);
            context.startActivity(intent);
        }
    }
}
