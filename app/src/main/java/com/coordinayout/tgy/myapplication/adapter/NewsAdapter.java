package com.coordinayout.tgy.myapplication.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.coordinayout.tgy.myapplication.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by tgy on 2017/2/26.
 */

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.VH> {
    private Context context;
    private List<String> mDatas;
    public NewsAdapter(Context context){
        this.context = context;
    }
    @Override
    public VH onCreateViewHolder(ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(context).inflate(R.layout.news_item, parent, false));
    }

    @Override
    public void onBindViewHolder(VH holder, int position) {
        holder.tvContent.setText(mDatas.get(position).toString());
    }

    @Override
    public int getItemCount() {
        return mDatas == null ? 0 : mDatas.size();
    }
    /** 更新数据，替换原有数据 */
    public void updateItems(List<String> items) {
        mDatas = items;
        notifyDataSetChanged();
    }

    /** 在列表尾添加一串数据 */
    public void addItems(List<String> items) {
        int start = mDatas.size();
        mDatas.addAll(items);
        // notifyItemRangeChanged(start, items.size());会闪屏
        notifyDataSetChanged();
    }

    public class VH extends RecyclerView.ViewHolder{
        @BindView(R.id.content)
        TextView tvContent;

        public VH(View viewItem){
            super(viewItem);
            ButterKnife.bind(this, itemView);
        }
    }
}
