package com.coordinayout.tgy.myapplication.adapter;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import com.coordinayout.tgy.myapplication.R;

/**
 * Created by tgy on 2017/2/26.
 */

public class ImgPagerAdapter extends PagerAdapter {
    private Context context = null;
    private int[] list;
    public ImgPagerAdapter(Context context, int[] list){
        this.context = context;
        this.list = list;
    }
    @Override
    public int getCount() {
        return list.length;
    }

    @Override
    public Object instantiateItem(ViewGroup container, int position) {
        ImageView imageView = new ImageView(context);
        imageView.setBackgroundResource(list[position]);
        //LinearLayout.LayoutParams params = new LinearLayout.LayoutParams();
        container.addView(imageView);
        return imageView;
    }
    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }
    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view == object;
    }
}
