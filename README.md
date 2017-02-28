最近项目中需要实现一个新闻资讯页，但是要求标签上面有轮播图，而且要支持刷新和加载，标签页要滑动到顶部固定
想必大家对Coordinayout 和AppbarLayout都比较熟悉了，这里我就不介绍了

我们这里要解决的问题是滑动刷新和加载的手势冲突
这里先上效果图

![image](https://github.com/yanguangtao/coordinayout/screenshots/20170227213141140.gif)

布局如下，分四个模块，
SwipeRefresh
Coordinayout
AppBarLayout
Indicator

<?xml version="1.0" encoding="utf-8"?>
<LinearLayout   xmlns:android="http://schemas.android.com/apk/res/android"
    xmlns:app="http://schemas.android.com/apk/res-auto"
    android:layout_height="match_parent"
    android:orientation="vertical"
    android:layout_width="match_parent" >
    <android.support.v7.widget.Toolbar
    android:id="@+id/toolbar"
    android:title="咨询"
    app:titleTextColor="@color/white"
    android:background="@color/color_gold_text"
    android:layout_width="match_parent"
    android:layout_height="@dimen/action_bar_size"/>
<com.coordinayout.tgy.myapplication.view.SwipeLayout
    android:id="@+id/swipe_layout"
    android:layout_width="match_parent"
    app:load_more_enabled="false"
    android:layout_height="match_parent">
    <android.support.design.widget.CoordinatorLayout
        android:id="@+id/swipe_target"
        android:layout_width="match_parent"
        android:layout_height="match_parent">
        <android.support.design.widget.AppBarLayout
            android:id="@+id/bar_layout"
            android:layout_width="match_parent"
            android:layout_height="wrap_content">

            <android.support.v4.view.ViewPager
                android:id="@+id/img_viewpager"
                android:layout_width="match_parent"
                android:background="@color/white"
                app:layout_scrollFlags="scroll"
                android:layout_height="150dp"/>
            <View
                android:layout_width="match_parent"
                android:background="@color/color_drive_gray"
                android:layout_height="2dp"/>
            <net.lucode.hackware.magicindicator.MagicIndicator
                android:id="@+id/vp_indicator"
                android:layout_width="match_parent"
                android:background="@color/white"
                android:layout_height="45dp" />
            <View
                android:layout_width="match_parent"
                android:background="@color/color_drive_gray"
                android:layout_height="2dp"/>
        </android.support.design.widget.AppBarLayout>
        <android.support.v4.view.ViewPager
            android:id="@+id/vp_list"
            android:layout_width="match_parent"
            android:layout_height="match_parent"
            app:layout_behavior="@string/appbar_scrolling_view_behavior"
            />
    </android.support.design.widget.CoordinatorLayout>
</com.coordinayout.tgy.myapplication.view.SwipeLayout>
</LinearLayout>

AppBarLayout他的子View可以定义一个特殊的属性scrollFlags,该属性有4个值，可以定义多个

scroll: 这个flag是给那些滑动到顶部时需要滚出屏幕的View设置的，没有设置这个flag的view滑动到顶部时将会被固定在屏幕的顶端。 
enterAlways:属性和scroll一样。这个flag是给那些需要“快速返回”的view设置的， 快速到顶部
enterAlwaysCollapsed: 当view设置了这个属性时，滑动显示只会显示他的最小高度。（需要结合Scroll使用）

exitUntilCollapsed: 当view设置了这个属性时，滑动隐藏只会隐藏他的最小高度。（需要结合Scroll使用）

MagicIndicator是用的一个开源的控件
好了进入正题
首先解决的第一个问题就是下拉刷新时，有可能Appbar还没有被覆盖的地方，所以这里我们刷新的时候要监听AppBarLayout的滑动

![image](https://github.com/yanguangtao/coordinayout/screenshots/QQ截图20170228103908.png)

这里我们监听了appBar的偏移量，可以看到这里用了两个条件，一个是AppBar的偏移量一个是当前recyclerView是否在顶部，因为可能出现的问题是recycle不在顶部，然后能刷新的情况。

这里介绍几种常用的判断Recyler到达底部的方法
第一种也是网上用的最多的一种，屏幕中最后一个子项lastVisibleItemPosition等于所有子项个数totalItemCount - 1，那么RecyclerView就到达了底部， 代码如下：

![image](https://github.com/yanguangtao/coordinayout/screenshots/QQ截图20170228104436.png)

第二种是view自带的，computeVerticalScrollExtent()是当前屏幕显示的区域高度，computeVerticalScrollOffset() 是当前屏幕之前滑过的距离，而computeVerticalScrollRange()是整个View控件的高度。

![image](https://github.com/yanguangtao/coordinayout/screenshots/QQ截图20170228105026.png)

第三种就是Recycer自带的，也是我们项目中用的
RecyclerView.canScrollVertically(1)的值表示是否能向上滚动，false表示已经滚动到底部
RecyclerView.canScrollVertically(-1)的值表示是否能向下滚动，false表示已经滚动到顶部

有了上面的监听之后，刷新冲突的问题就解决了

下面我们处理加载更多的问题。 因为我们要及时监听Rcycler是否到达了底部，所以我们给Recycler 添加一个滚动监听，代码如下：

![image](https://github.com/yanguangtao/coordinayout/screenshots/QQ截图20170228105221.png)



当滑动到达底部的时候那么久设置为可以加载更多
到这里问题应该解决了，但是在测试的时候发现，加载更多还是会出现问题，第一个页面加载更多后，第二个页面没到底部就能加载更多。
这个跟我用的这个刷新有点关系，在刷新后，虽然数据被添加到Recycler中了，此时没有滑动Recycler 所以，Swipe的状态是还可以加载更多。所以这个时候切换页面直接就变成可以加载更多了

![image](https://github.com/yanguangtao/coordinayout/screenshots/QQ截图20170228105357.png)

所以，在数据加载完成后我们设置loadMoreEnable(false)


这样做之后，问题基本上都解决了





