package com.demo.wyd.materialDesignerDemo.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.demo.wyd.materialDesignerDemo.R;

import java.util.List;

/**
 * Description :RecyclerView的适配器，包含点击事件
 * Created by wyd on 2016/8/15.
 */
public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.MyViewHolder> {
    private Context mContext;
    private List<String> data;
    private OnItemClickListener mOnItemClickListener;

    public RecyclerAdapter(Context mContext, List<String> data) {
        this.mContext = mContext;
        this.data = data;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(mContext).inflate(R.layout.item_recycler_view, parent, false);
        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        holder.tvItemTitle.setText(data.get(position));
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
    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.mOnItemClickListener = onItemClickListener;
    }

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView tvItemTitle;

        MyViewHolder(View itemView) {
            super(itemView);
            tvItemTitle = (TextView) itemView.findViewById(R.id.tv_item_title);

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
