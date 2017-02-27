package com.coordinayout.tgy.myapplication.view;


import android.content.Context;
import android.util.AttributeSet;
import android.view.View;

import com.aspsine.swipetoloadlayout.SwipeToLoadLayout;
import com.coordinayout.tgy.myapplication.R;

/**
 * 用固定视图封装过的滑动控件。
 */
public class SwipeLayout extends SwipeToLoadLayout {
    public static final int STATE_NORMAL = 0x00;
    public static final int STATE_REFRESH = 0x01;
    public static final int STATE_LOADMORE = 0x02;

    public interface ISwipeEndListener {
        void refreshEnd(boolean succ);

        void loadMoreEnd(boolean succ);
    }

    public SwipeLayout(Context context) {
        super(context);
        initView(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SwipeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        View.inflate(context, R.layout.swipe_head, this);
        View.inflate(context, R.layout.swipe_foot, this);
//        setLoadMoreFinalDragOffset((int) context.getResources()
//                .getDimension(R.dimen.swipe_widget_height));
    }

    /**
     * 结束刷新动作
     */
    public void endRefresh() {
        setRefreshing(false);
    }

    /**
     * 结束加载更多动作
     */
    public void endLoadMore() {
        setLoadingMore(false);
    }

    /**
     * 当加载至尾页时调用
     */
    public void noMore(boolean noMore) {
        ((SwipeFootView) findViewById(R.id.swipe_load_more_footer)).noMore(noMore);
    }
}
