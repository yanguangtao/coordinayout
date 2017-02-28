package com.coordinayout.tgy.myapplication.activity;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import com.coordinayout.tgy.myapplication.R;
import com.aspsine.swipetoloadlayout.OnLoadMoreListener;
import com.aspsine.swipetoloadlayout.OnRefreshListener;
import com.coordinayout.tgy.myapplication.R;
import com.coordinayout.tgy.myapplication.adapter.ImgPagerAdapter;
import com.coordinayout.tgy.myapplication.adapter.NewsAdapter;
import com.coordinayout.tgy.myapplication.adapter.UnderLineNavigatorAdapter;
import com.coordinayout.tgy.myapplication.base.BaseActivity;
import com.coordinayout.tgy.myapplication.bean.TagBean;
import com.coordinayout.tgy.myapplication.view.DividerItemDecoration;
import com.coordinayout.tgy.myapplication.view.ScaleTransitionPagerTitleView;
import com.coordinayout.tgy.myapplication.view.SwipeLayout;
import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;

import android.util.SparseArray;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;


public class MainActivity extends BaseActivity implements OnRefreshListener, OnLoadMoreListener, AppBarLayout.OnOffsetChangedListener{

    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.img_viewpager)
    ViewPager imgViewpager;
    @BindView(R.id.vp_indicator)
    MagicIndicator channelIndicator;
    @BindView(R.id.bar_layout)
    AppBarLayout barLayout;
    @BindView(R.id.vp_list)
    ViewPager vpList;
    @BindView(R.id.swipe_target)
    CoordinatorLayout swipeTarget;
    @BindView(R.id.swipe_layout)
    SwipeLayout swipeLayout;


    private ImgPagerAdapter imgPagerAdapter = null;
    private PagerAdapter listPagerAdapter = null;
    private int[] imgList = new int[]{R.mipmap.timg, R.mipmap.timg2, R.mipmap.timg2};
    private List<TagBean> tagList = new ArrayList<>();
    private SparseArray<RecyclerView> viewSparseArray = new SparseArray<>();
    private UnderLineNavigatorAdapter indicatorAdapter;
    private boolean isRefresh = false;
    private List<String> mList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        initView();
        initVPIndicator();
    }

    private void initView() {
        toolbar.setTitle("咨询");
        swipeLayout = (SwipeLayout) findViewById(R.id.swipe_layout);
        tagList.add(new TagBean(0, "推荐"));
        tagList.add(new TagBean(1, "财经"));
        tagList.add(new TagBean(2, "娱乐"));
        tagList.add(new TagBean(3, "体育"));
        tagList.add(new TagBean(4, "情感"));

        imgPagerAdapter = new ImgPagerAdapter(this, imgList);
        imgViewpager.setAdapter(imgPagerAdapter);

        swipeLayout.setOnRefreshListener(this);
        swipeLayout.setOnLoadMoreListener(this);
        barLayout.addOnOffsetChangedListener(this);

        listPagerAdapter = new PagerAdapter() {
            @Override
            public int getCount() {
                return tagList == null ? 0 : tagList.size();
            }

            @Override
            public Object instantiateItem(ViewGroup container, int position) {
                RecyclerView recyclerView = getView(tagList.get(position).getTagId());
                if(null != recyclerView){
                    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                        @Override
                        public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                            super.onScrollStateChanged(recyclerView, newState);
                            if(isBottom()){
                                swipeLayout.setLoadMoreEnabled(true);
                            }else{
                                swipeLayout.setLoadMoreEnabled(false);
                            }
                        }
                    });
                }
                container.addView(recyclerView);
                return recyclerView;
            }

            @Override
            public boolean isViewFromObject(View view, Object object) {
                return view == object;
            }

            @Override
            public void destroyItem(ViewGroup container, int position, Object object) {
                container.removeView((View) object);
            }
        };
        vpList.setAdapter(listPagerAdapter);
        onRefresh();
    }

    private void initVPIndicator() {
        final CommonNavigator commonNavigator = new CommonNavigator(this);
        indicatorAdapter = new UnderLineNavigatorAdapter(this, tagList);
        indicatorAdapter.setRelateViewPager(vpList);
        commonNavigator.setAdapter(indicatorAdapter);
        channelIndicator.setNavigator(commonNavigator);
        vpList.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                channelIndicator.onPageScrolled(position, positionOffset, positionOffsetPixels);
            }

            @Override
            public void onPageSelected(int position) {
                endLoadEverything();
                channelIndicator.onPageSelected(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {
                channelIndicator.onPageScrollStateChanged(state);
                if (state == ViewPager.SCROLL_STATE_IDLE) {

                }
            }
        });
    }

    private RecyclerView getView(int tagId){
        RecyclerView recyclerView = viewSparseArray.get(tagId, null);
        if(null  == recyclerView){
            recyclerView = generateView(tagId);
        }
        return recyclerView;
    }

    private RecyclerView generateView(int tagId){
        RecyclerView recyclerView = new RecyclerView(this);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        DividerItemDecoration itemDecoration = new DividerItemDecoration(this, LinearLayoutManager.VERTICAL);
        recyclerView.addItemDecoration(itemDecoration);
        NewsAdapter adapter = new NewsAdapter(this);
        recyclerView.setAdapter(adapter);
        viewSparseArray.put(tagId, recyclerView);
        return recyclerView;
    }

    @Override
    public void onLoadMore() {
        isRefresh = false;
        getData(tagList.get(vpList.getCurrentItem()).getTagId());
    }

    @Override
    public void onRefresh() {
        isRefresh = true;
        getData(tagList.get(vpList.getCurrentItem()).getTagId());
    }

    /**
     * 拉取数据
     * @param tagId
     */
    private void getData(int tagId){
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(1000);
                }catch (Exception e){

                }

            }
        }).start();
        Integer tag = (Integer) getView(tagId).getTag();
        NewsAdapter adapter = (NewsAdapter) getView(tagId).getAdapter();
        if(null == tag || tag == 0){
            tag = 1;
        }
        endLoadEverything();
        if(isRefresh){
            tag = 1;
            adapter.updateItems(createData(tagId, tag));
            isRefresh = false;
        }else{
            adapter.addItems(createData(tagId, tag));
            swipeLayout.setLoadMoreEnabled(false);
        }
        getView(tagId).setTag(++tag);

    }

    /**
     * 造数据 默认每页20条
     * @param pageIdx
     * @return
     */
    private List<String> createData(int tagId, int pageIdx){
        String str = tagList.get(tagId).getTagName() + "界面 ";
        List<String> list = new ArrayList<>();
        int i = (pageIdx - 1) * 20;
        for(; i < pageIdx * 20; i++){
            list.add(str + i);
        }
        return list;
    }


    private void endLoadEverything(){
        if(swipeLayout.isRefreshing()){
            swipeLayout.endRefresh();
        }
        if(swipeLayout.isLoadingMore()){
            swipeLayout.endLoadMore();
        }
    }

    @Override
    public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
        if(tagList == null){
            return;
        }
        if(verticalOffset >= 0 && isTop()){
            swipeLayout.setRefreshEnabled(true);
        }else{
            swipeLayout.setRefreshEnabled(false);
        }
    }

    public boolean isTop(){
        int tagId = tagList.get(vpList.getCurrentItem()).getTagId();
        return !(getView(tagId)).canScrollVertically(-1);
    }
    public boolean isBottom(){
        int tagId = tagList.get(vpList.getCurrentItem()).getTagId();
        return !(getView(tagId)).canScrollVertically(1);
    }

    public static boolean isVisBottom(RecyclerView recyclerView){
        LinearLayoutManager layoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
        //屏幕中最后一个可见子项的position
        int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
        //当前屏幕所看到的子项个数
        int visibleItemCount = layoutManager.getChildCount();
        //当前RecyclerView的所有子项个数
        int totalItemCount = layoutManager.getItemCount();
        //RecyclerView的滑动状态
        int state = recyclerView.getScrollState();
        if(visibleItemCount > 0 && lastVisibleItemPosition == totalItemCount - 1 && state == recyclerView.SCROLL_STATE_IDLE){
            return true;
        }else {
            return false;
        }
    }

    public static boolean isSlideToBottom(RecyclerView recyclerView) {
        if (recyclerView == null) return false;
        if (recyclerView.computeVerticalScrollExtent() + recyclerView.computeVerticalScrollOffset()
                >= recyclerView.computeVerticalScrollRange())
            return true;
        return false;
    }
}
