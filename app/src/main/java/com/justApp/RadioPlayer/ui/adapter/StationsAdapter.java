package com.justApp.RadioPlayer.ui.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justApp.RadioPlayer.R;
import com.justApp.RadioPlayer.data.pojo.Station;

import java.util.ArrayList;
import java.util.List;

/**
 * @author Sergey Rodionov
 */

public class StationsAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private static final int TYPE_ITEM = 0;
    private static final int TYPE_FOOTER = 1;
    private final IRecyclerViewItemListener mItemListener;
    private List<Station> mStations;
    private boolean mWithFooter;

    public StationsAdapter(IRecyclerViewItemListener itemListener) {
        mStations = new ArrayList<>();
        mItemListener = itemListener;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view;
        RecyclerView.ViewHolder viewHolder = null;
        if (viewType == TYPE_ITEM) {
            view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.stations_list_item, parent, false);
            viewHolder = new ItemViewHolder(view);
        } else if (viewType == TYPE_FOOTER) {
            view = LayoutInflater.from(parent.getContext()).inflate(
                    R.layout.stations_list_item_footer, parent, false);
            viewHolder = new EmptyViewHolder(view);
        }
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof ItemViewHolder) {
            ItemViewHolder viewHolder = (ItemViewHolder) holder;
            Station station = getItem(position);
            String urlThumb = station.getImage().getThumb().getUrl();
            viewHolder.mTitle.setText(station.getName());
            viewHolder.mSubTitle.setText(station.getCategories().get(0).getTitle());
            Glide.with(viewHolder.mImage.getContext())
                    .load(urlThumb)
                    .skipMemoryCache(true)
                    .placeholder(R.drawable.ic_default_art)
                    .animate(android.R.anim.fade_in)
                    .into(viewHolder.mImage);
        }
    }

    @Override
    public int getItemCount() {
        int itemCount = mStations.size();
        if (mWithFooter) {
            itemCount++;
        }
        return itemCount;
    }

    @Override
    public int getItemViewType(int position) {
        if (mWithFooter && isPositionFooter(position)) {
            return TYPE_FOOTER;
        }
        return TYPE_ITEM;
    }

    private boolean isPositionFooter(int position) {
        return position == getItemCount() - 1;
    }

    public Station getItem(int position) {
        return mStations.get(position);
    }

    public void setStations(@NonNull List<Station> stations) {
        mStations.clear();
        mStations.addAll(stations);
        notifyDataSetChanged();
    }

    public List<Station> getStations() {
        return mStations;
    }

    public void setFooter(boolean withFooter) {
        mWithFooter = withFooter;
        notifyDataSetChanged();
    }

    protected class ItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        private TextView mTitle;
        private TextView mSubTitle;
        private ImageView mImage;
        private ViewGroup mLayoutView;

        ItemViewHolder(View itemView) {
            super(itemView);
            mTitle = (TextView) itemView.findViewById(R.id.title);
            mSubTitle = (TextView) itemView.findViewById(R.id.sub_title);
            mImage = (ImageView) itemView.findViewById(R.id.image);
            mLayoutView = (ViewGroup) itemView.findViewById(R.id.rootItem);
            mLayoutView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            mItemListener.onItemClick(getAdapterPosition());
        }
    }

    protected class EmptyViewHolder extends RecyclerView.ViewHolder {
        EmptyViewHolder(View itemView) {
            super(itemView);
        }
    }
}
