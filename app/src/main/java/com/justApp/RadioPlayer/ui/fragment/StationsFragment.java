package com.justApp.RadioPlayer.ui.fragment;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.justApp.RadioPlayer.R;
import com.justApp.RadioPlayer.data.pojo.Station;
import com.justApp.RadioPlayer.ui.adapter.IRecyclerViewItemListener;
import com.justApp.RadioPlayer.ui.adapter.StationsAdapter;
import com.justApp.RadioPlayer.ui.contract.StationsContract;
import com.justApp.RadioPlayer.ui.presenter.StationsPresenter;
import com.justApp.RadioPlayer.utils.SimpleHorizontalDivider;

import java.util.List;


/**
 * @author Sergey Rodionov
 */
public class StationsFragment extends Fragment implements StationsContract.View,
        IRecyclerViewItemListener, View.OnClickListener {

    private StationsContract.Presenter mPresenter;
    private StationsAdapter mStationsAdapter;
    private RecyclerView mRecyclerView;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private View mEmptyStationsView;
    private ImageView mStationItemLogo;
    private ImageView mStationLogo;
    private TextView mStationItemName;
    private TextView mStationItemStatus;
    private BottomSheetBehavior mRecentlyItemBottomBehavior;
    private FloatingActionButton mFloatingButton;
    private ImageButton mStartPauseButton;
    private ImageButton mStationPlayButton;
    private LinearLayout mRecentlyItemBottom;

    public static StationsFragment newInstance() {
        return new StationsFragment();
    }

    // region Fragment lifecycle >>>

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = new StationsPresenter();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_stations, container, false);
        mRecentlyItemBottom = (LinearLayout) view.findViewById(R.id.recently_item_bottom);
        mStartPauseButton = (ImageButton) view.findViewById(R.id.start_pause_button);
        mStartPauseButton.setOnClickListener(this);
        mRecyclerView = (RecyclerView) view.findViewById(R.id.stations_list);
        mFloatingButton = (FloatingActionButton) view.findViewById(R.id.floating_button);
        mFloatingButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        mEmptyStationsView = view.findViewById(R.id.empty_stations);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.refresh_layout);
        mSwipeRefreshLayout.setOnRefreshListener(() -> mPresenter.loadStations(true));
        mStationItemLogo = (ImageView) view.findViewById(R.id.station_item_logo);
        mStationItemName = (TextView) view.findViewById(R.id.station_item_name);
        mStationItemStatus = (TextView) view.findViewById(R.id.station_item_status);

        mStationLogo = (ImageView) view.findViewById(R.id.station_logo);
        ImageButton stationPrevButton = (ImageButton) view.findViewById(R.id.station_prev);
        ImageButton stationNextButton = (ImageButton) view.findViewById(R.id.station_next);
        mStationPlayButton = (ImageButton) view.findViewById(R.id.station_play);

        stationPrevButton.setOnClickListener(this);
        mStationPlayButton.setOnClickListener(this);
        stationNextButton.setOnClickListener(this);
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        mStationsAdapter = new StationsAdapter(this);
        initRecyclerView();
        initBottomBehavior();
        mPresenter.subscribe(this);
    }

    @Override
    public void onDestroy() {
        mPresenter.unSubscribe();
        super.onDestroy();
    }

    // endregion Fragment lifecycle >>>

    @Override
    public void setLoading(boolean active) {
        if (getView() != null) {
            mSwipeRefreshLayout.post(() -> mSwipeRefreshLayout.setRefreshing(active));
        }
    }

    @Override
    public void showStations(List<Station> stations) {
        mStationsAdapter.setStations(stations);
    }

    @Override
    public void showPlayingStation(Station station) {
        setContentPlayingStation(station);
        mStationsAdapter.setFooter(true);
    }

    @Override
    public void showLoadingStationsError() {
        mRecyclerView.setVisibility(View.GONE);
        mEmptyStationsView.setVisibility(View.VISIBLE);
    }

    @Override
    public void showNoStations() {
        mRecyclerView.setVisibility(View.VISIBLE);
        mEmptyStationsView.setVisibility(View.GONE);
    }

    @Override
    public void updateBottomView(String status, boolean playing) {
        mStationItemStatus.setText(status);
        int icon = playing ? R.drawable.ic_pause : R.drawable.ic_play;
        mStartPauseButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), icon));
        mStationPlayButton.setImageDrawable(ContextCompat.getDrawable(getActivity(), icon));
        mRecentlyItemBottomBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
    }

    @Override
    public void hideBottomView() {
        mFloatingButton.setVisibility(View.GONE);
        mRecentlyItemBottomBehavior.setHideable(true);
        mRecentlyItemBottomBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);
    }

    @Override
    public void onItemClick(int position) {
        Station station = mStationsAdapter.getItem(position);
        mPresenter.saveNowPlayingStation(station);
        mPresenter.playSpecifiedStation(position, station.getId());
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.start_pause_button:
            case R.id.station_play:
                mPresenter.playPauseStation();
                break;
            case R.id.station_prev:
                mPresenter.playPrev();
                break;
            case R.id.station_next:
                mPresenter.playNext();
                break;
            default:
                break;
        }
    }

    private void setContentPlayingStation(Station station) {
        Glide.with(getContext())
                .load(station.getImage().getUrl())
                .placeholder(R.drawable.ic_default_art)
                .into(mStationItemLogo);

        Glide.with(getContext())
                .load(station.getImage().getUrl())
                .placeholder(R.drawable.ic_default_art)
                .into(mStationLogo);

        mStationItemName.setText(station.getCategories().get(0).getTitle());
    }

    private void initRecyclerView() {
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setNestedScrollingEnabled(false);
        mRecyclerView.addItemDecoration(new SimpleHorizontalDivider(getContext()));
        mRecyclerView.setAdapter(mStationsAdapter);
    }

    private void initBottomBehavior() {
        mRecentlyItemBottomBehavior = BottomSheetBehavior.from(mRecentlyItemBottom);
        mRecentlyItemBottomBehavior.setBottomSheetCallback(
                new BottomSheetBehavior.BottomSheetCallback() {
                    @Override
                    public void onStateChanged(@NonNull View bottomSheet, int newState) {
                    }

                    @Override
                    public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                        mFloatingButton.animate()
                                .scaleX(1 - slideOffset)
                                .scaleY(1 - slideOffset)
                                .setDuration(0)
                                .start();
                    }
                });
    }
}
