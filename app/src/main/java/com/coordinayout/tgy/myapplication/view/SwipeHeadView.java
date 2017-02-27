package com.coordinayout.tgy.myapplication.view;

/**
 * Created by moses
 */

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import com.aspsine.swipetoloadlayout.SwipeRefreshTrigger;
import com.aspsine.swipetoloadlayout.SwipeTrigger;
import com.coordinayout.tgy.myapplication.R;

/**
 * 下拉头部
 */
public class SwipeHeadView extends FrameLayout implements SwipeTrigger, SwipeRefreshTrigger {

    // 箭头动画——向上
    private Animation mUpAnim;
    // 箭头动画——向下
    private Animation mDownAnim;
    // 文本
    TextView mHeadTxt;
    // 菊花
    ProgressBar mProgressBar;
    // 控件高度
    private int mHeight;
    // 箭头
    private ImageView mArrowImg;
    //
    private boolean mRotated = false;

    public SwipeHeadView(Context context) {
        super(context);
        initView(context);
    }

    public SwipeHeadView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView(context);
    }

    public SwipeHeadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    // 设置视图
    private void initView(Context context) {
        View.inflate(context, R.layout.view_swipe_head, this);
        mHeight = (int) getResources().getDimension(R.dimen.swipe_widget_height);
        mArrowImg = (ImageView) findViewById(R.id.img_arrow);
        mHeadTxt = (TextView) findViewById(R.id.txt_refresh_head);
        mProgressBar = (ProgressBar) findViewById(R.id.progress_bar);
        initAnimation();
    }

    // 初始化箭头动画
    private void initAnimation() {
        this.mUpAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_up);
        this.mDownAnim = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_down);
    }

    @Override
    public void onPrepare() {
        mHeadTxt.setText(getContext().getResources().getString(R.string.txt_before_refresh));
        mProgressBar.setVisibility(GONE);
        mArrowImg.setVisibility(VISIBLE);
        invalidate();
    }

    @Override
    public void onMove(int i, boolean b, boolean b1) {
        if (!b) {
            mArrowImg.setVisibility(VISIBLE);
            mProgressBar.setVisibility(INVISIBLE);
            if (i > mHeight) {
                mHeadTxt.setText(R.string.txt_loose_refresh);
                if (!mRotated) {
                    mArrowImg.clearAnimation();
                    mArrowImg.startAnimation(mUpAnim);
                    mRotated = true;
                }
            } else if (i < mHeight) {
                if (mRotated) {
                    mArrowImg.clearAnimation();
                    mArrowImg.startAnimation(mDownAnim);
                    mRotated = false;
                }
                mHeadTxt.setText(R.string.txt_before_refresh);
            }
        }
    }

    @Override
    public void onRefresh() {
        mHeadTxt.setText(getResources().getString(R.string.txt_refreshing));
        mArrowImg.clearAnimation();
        mArrowImg.setVisibility(GONE);
        mProgressBar.setVisibility(VISIBLE);
        invalidate();
    }

    @Override
    public void onRelease() {
        mHeadTxt.setText(getResources().getString(R.string.txt_refreshing));
        mArrowImg.setVisibility(GONE);
        mProgressBar.setVisibility(VISIBLE);
        invalidate();
    }

    @Override
    public void onComplete() {
        mRotated = false;
        mHeadTxt.setText(getResources().getString(R.string.txt_refreshed));
        mArrowImg.clearAnimation();
        mArrowImg.setVisibility(GONE);
        mProgressBar.setVisibility(GONE);
        invalidate();
    }

    @Override
    public void onReset() {
        mRotated = false;
        mHeadTxt.setText(getResources().getString(R.string.txt_before_refresh));
        mArrowImg.setVisibility(VISIBLE);
        mProgressBar.setVisibility(GONE);
    }
}
