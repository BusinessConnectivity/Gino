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
import com.bizconnectivity.gino.activities.DealsListActivity;
import com.bizconnectivity.gino.activities.SettingsActivity;
import com.bizconnectivity.gino.helpers.ItemTouchHelperAdapter;
import com.bizconnectivity.gino.helpers.ItemTouchHelperViewHolder;
import com.bizconnectivity.gino.helpers.OnStartDragListener;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class OfferRecyclerListAdapter extends RecyclerView.Adapter<OfferRecyclerListAdapter.ItemViewHolder> implements ItemTouchHelperAdapter{

    private final List<String> mItems = new ArrayList<>();
    private final OnStartDragListener mDragStartListener;
    Context context;

    public OfferRecyclerListAdapter(Context context, OnStartDragListener dragStartListener) {

        this.context = context;
        mDragStartListener = dragStartListener;
        mItems.addAll(Arrays.asList(context.getResources().getStringArray(R.array.dummy_items)));
    }

    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_main, parent, false);
        return new ItemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {

        holder.textView.setText(mItems.get(position));
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
    public boolean onItemMove(int fromPosition, int toPosition) {
        Collections.swap(mItems, fromPosition, toPosition);
        notifyItemMoved(fromPosition, toPosition);
        return true;
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

        public final TextView textView;
        public final ImageView handleView;

        public ItemViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text);
            handleView = (ImageView) itemView.findViewById(R.id.image);

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
