package tp.moviesearch.util;

import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

/**
 * RecyclerView's scroll listener used to load more data as the list scrolls.
 */
public abstract class EndlessRecyclerViewScrollListener extends RecyclerView.OnScrollListener {

    private int visibleThreshold;

    private LinearLayoutManager mLayoutManager;

    protected EndlessRecyclerViewScrollListener(LinearLayoutManager layoutManager) {
        mLayoutManager = layoutManager;
        visibleThreshold = 2;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        int totalItemCount = mLayoutManager.getItemCount();
        int lastVisibleItemPosition = mLayoutManager.findLastVisibleItemPosition();
        if (lastVisibleItemPosition + visibleThreshold > totalItemCount) {
            onLoadMore();
        }
    }

    public abstract void onLoadMore();
}
