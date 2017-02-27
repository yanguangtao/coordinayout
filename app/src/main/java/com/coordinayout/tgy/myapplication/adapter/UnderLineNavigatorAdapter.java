package com.coordinayout.tgy.myapplication.adapter;

import android.content.Context;
import android.graphics.Color;
import android.support.v4.view.ViewPager;
import android.view.View;

import com.coordinayout.tgy.myapplication.R;
import com.coordinayout.tgy.myapplication.bean.TagBean;
import com.coordinayout.tgy.myapplication.view.ScaleTransitionPagerTitleView;

import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;

import java.util.List;

/**
 * Created by tgy on 2017/2/26.
 */

public class UnderLineNavigatorAdapter extends CommonNavigatorAdapter {
    private List<TagBean> tagList;
    private Context context;
    private ViewPager viewPager;
    public UnderLineNavigatorAdapter(Context context, List<TagBean> tagList){
        this.tagList = tagList;
        this.context = context;
    }
    public void setRelateViewPager(ViewPager viewPager){
        this.viewPager = viewPager;
    }
    @Override
    public int getCount() {
        return tagList == null ? 0 : tagList.size();
    }

    @Override
    public IPagerTitleView getTitleView(Context context, final int index) {
        // 缩放 + 颜色渐变
        ScaleTransitionPagerTitleView simplePagerTitleView = new ScaleTransitionPagerTitleView(context);
        simplePagerTitleView.setText(tagList.get(index).getTagName());
        simplePagerTitleView.setMinScale(0.83f);
        simplePagerTitleView.setTextSize(18);
        simplePagerTitleView.setNormalColor(context.getResources().getColor(R.color.color_selector));
        simplePagerTitleView.setSelectedColor(context.getResources().getColor(R.color.color_gold_text));
        simplePagerTitleView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(viewPager !=null && viewPager.getAdapter().getCount() > index){
                    viewPager.setCurrentItem(index, true);
                }

            }
        });
        return simplePagerTitleView;
    }

    @Override
    public IPagerIndicator getIndicator(Context context) {
        LinePagerIndicator indicator = new LinePagerIndicator(context);
        indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
        indicator.setColors(Color.parseColor("#A5862D"));
        return indicator;
    }
}


