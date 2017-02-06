package com.demo.wyd.materialDesignerDemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.demo.wyd.materialDesignerDemo.R;
import com.demo.wyd.materialDesignerDemo.activity.CtlActivity;

import java.util.ArrayList;
import java.util.List;

/**
 * Description :CardView的适配器，包含点击事件，包含瀑布流的随机高度
 * Created by wyd on 2016/9/26.
 */

public class CardViewAdapter extends RecyclerView.Adapter<CardViewAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> data;
    private RecyclerAdapter.OnItemClickListener mOnItemClickListener;
    private List<Integer> randomHeight;

    public CardViewAdapter(Context mContext, List<String> data) {
        this.mContext = mContext;
        this.data = data;
        randomHeight = new ArrayList<>(data.size());
        for (int i = 0; i < data.size(); i++) {
            randomHeight.add((int) (250 + Math.random() * 300));
        }
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_card_view, parent, false);
        return new CardViewAdapter.MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        holder.tvItemTitle.setText(data.get(position));
        //用随机的高度来控制每个item的高度
        if (mContext instanceof CtlActivity) {
            holder.llRoot.getLayoutParams().height = randomHeight.get(position);
            holder.llRoot.setLayoutParams(holder.llRoot.getLayoutParams());
        }
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    /**
     * 对外提供调用接口的方法
     *
     * @param onItemClickListener 传入接口
     */
    public void setOnItemClickListener(RecyclerAdapter.OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemTitle;
        LinearLayout llRoot;

        MyViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = (TextView) itemView.findViewById(R.id.tv_item_title);
            llRoot = (LinearLayout) itemView.findViewById(R.id.ll_root);

            //监听item中控件的点击事件，在点击事件中调用自定义接口中的方法
            tvItemTitle.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mOnItemClickListener != null) {
                        //调用接口中的方法，具体的逻辑由实现接口的类去实现；
                        mOnItemClickListener.onItemClick(view, MyViewHolder.this.getAdapterPosition());
                    }
                }
            });
        }
    }

    /**
     * 定义item点击事件的接口
     */
    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }
}
