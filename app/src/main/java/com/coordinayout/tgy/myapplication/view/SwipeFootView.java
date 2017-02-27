package com.coordinayout.tgy.myapplication.view;

/**
 * moses
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import com.aspsine.swipetoloadlayout.SwipeLoadMoreTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.coordinayout.tgy.myapplication.R;

/**
 * 上拉的尾部
 */
public class SwipeFootView extends RelativeLayout implements SwipeTrigger, SwipeLoadMoreTrigger {

    //    private View mTopView;
    // 箭头动画——向上
    private Animation mUpAnim;
    // 箭头动画——向下
    private Animation mDownAnim;
    // 箭头
    private ImageView mArrowImg;
    // 提示语
    private TextView mLoadTxt;
    // 没有更多
    private TextView mNoMoreTxt;
    // 菊花
    private ProgressBar mProgressBar;
    // 没有更多
    private boolean mIsEnd;
    // 控件高度
    private int mHeight;
    //
    private boolean mRotated = false;

    public SwipeFootView(Context context) {
        super(context);
        initView(context);
    }

    public SwipeFootView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SwipeFootView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    // 设置视图
    private void initView(Context context) {
        View.inflate(context, R.layout.view_swipe_foot, this);
        mHeight = 50;
       // mHeight = (int) getResources().getDimension(R.dimen.swipe_widget_height);
        mLoadTxt = (TextView) findViewById(R.id.txt_loading);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        mArrowImg = (ImageView) findViewById(R.id.img_arrow);
        mArrowImg.setRotation(-180f);
        mNoMoreTxt = (TextView) findViewById(R.id.txt_no_more);

        initAnimation();
    }

    // 初始化箭头动画
    private void initAnimation() {
        this.mUpAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_up);
        this.mDownAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_down);
    }

    @Override
    public void onLoadMore() {
        mProgressBar.setVisibility(VISIBLE);
        mArrowImg.clearAnimation();
        mArrowImg.setVisibility(INVISIBLE);
        mLoadTxt.setText(R.string.txt_loading);
        invalidate();
    }

    @Override
    public void onPrepare() {
        mProgressBar.setVisibility(INVISIBLE);
        mLoadTxt.setText(R.string.txt_before_load);
        mArrowImg.setVisibility(VISIBLE);
        mNoMoreTxt.setVisibility(INVISIBLE);
        invalidate();
    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {
        if (!b) {
            mArrowImg.setVisibility(VISIBLE);
            mProgressBar.setVisibility(GONE);
            if (i < -mHeight) {
                mLoadTxt.setText(R.string.txt_loose_load);
                if (!mRotated) {
                    mArrowImg.clearAnimation();
                    mArrowImg.startAnimation(mUpAnim);
                    mRotated = true;
                }
            } else if (i > -mHeight) {
                if (mRotated) {
                    mArrowImg.clearAnimation();
                    mArrowImg.startAnimation(mDownAnim);
                    mRotated = false;
                }
                mLoadTxt.setText(R.string.txt_before_load);
            }
        }
    }

    @Override
    public void onRelease() {
        mProgressBar.setVisibility(VISIBLE);
        mArrowImg.clearAnimation();
        mArrowImg.setVisibility(GONE);
        mLoadTxt.setText(R.string.txt_loading);
        invalidate();
    }

    @Override
    public void onComplete() {
        mRotated = false;
        mProgressBar.setVisibility(GONE);
        mArrowImg.clearAnimation();
        mArrowImg.setVisibility(GONE);
        if (mIsEnd) {
            mLoadTxt.setVisibility(GONE);
            mNoMoreTxt.setVisibility(VISIBLE);
        } else {
            mLoadTxt.setText(R.string.txt_loaded);
            mLoadTxt.setVisibility(VISIBLE);
            mNoMoreTxt.setVisibility(GONE);
        }
        invalidate();
    }

    @Override
    public void onReset() {
        mRotated = false;
        mProgressBar.setVisibility(GONE);
        mLoadTxt.setVisibility(VISIBLE);
        mLoadTxt.setText(R.string.txt_before_load);
        mArrowImg.setVisibility(VISIBLE);
        mNoMoreTxt.setVisibility(GONE);
        invalidate();
    }

    /**
     * 列表设置为已满
     */
    public void noMore(boolean isEnd) {
        this.mIsEnd = isEnd;
    }
}