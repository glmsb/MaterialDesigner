package com.demo.wyd.materialDesignerDemo.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.demo.wyd.materialDesignerDemo.R;
import com.demo.wyd.materialDesignerDemo.activity.TestConstraintLayoutActivity;

/**
 * Description :
 * Created by wyd on 2016/7/20.
 */
public class Fragment5 extends Fragment implements View.OnClickListener {
    private View rootView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_5, container, false);
            initView();
        } else {
            ViewGroup p = (ViewGroup) rootView.getParent();
            if (p != null) {
                p.removeAllViewsInLayout();
            }
        }
        return rootView;
    }

    private void initView() {
        rootView.findViewById(R.id.toolbar).setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        startActivity(new Intent(getContext(), TestConstraintLayoutActivity.class));
    }
}
