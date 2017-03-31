package com.demo.wyd.materialDesignerDemo.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Patterns;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.demo.wyd.materialDesignerDemo.R;
import com.umeng.analytics.MobclickAgent;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Description :{@link TextInputLayout 控件}
 * {@link SharedPreferences 使用 }
 * Created by wyd on 2016/7/20.
 */
public class Fragment1 extends Fragment {
    private View rootView;
    private TextInputLayout tilUser;
    private TextInputLayout tilPass;
    private AutoCompleteTextView etUser;
    //一种特殊的EditText 的子类，用来在'extract' mode 下在输入法编辑器中显示我们的hint提示信息，这里的'extract' mode 其实就是全屏模式
    private TextInputEditText etPassWord;
    private List<String> autoTexts;
    private Set<String> users = new HashSet<>();
    private ArrayAdapter<String> adapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_1, container, false);
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
        tilUser = (TextInputLayout) rootView.findViewById(R.id.til_user);
        tilPass = (TextInputLayout) rootView.findViewById(R.id.til_pass);
        Button btnLogin = (Button) rootView.findViewById(R.id.btn_login);
        etUser = (AutoCompleteTextView) rootView.findViewById(R.id.et_user);
        etPassWord = (TextInputEditText) rootView.findViewById(R.id.et_password);

        //用户名控件获取焦点的时候
        etUser.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View view, boolean hasFocus) {
                if (hasFocus) {
                    setAutoText();
                    tilPass.setErrorEnabled(false);
                }
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String user = etUser.getText().toString();
                String passWord = etPassWord.getText().toString();

                HashMap<String, String> eventAttrs = new HashMap<>();
                eventAttrs.put("userName", user);
                eventAttrs.put("passWord", passWord);
                //context 指当前的Activity, eventId 为当前统计的事件ID。eventAttrs 事件在不同属性上的取值
                MobclickAgent.onEvent(getContext(), "Login", eventAttrs);

                if (!validateEmail(user)) {
                    //setError()方法如果传入非空参数,则setErrorEnabled(true)将自动被调用。
                    tilUser.setError("the input user style error,please check email..");
                } else if (!validatePassWord(passWord)) {
                    tilUser.setErrorEnabled(false);
                    tilPass.setError("password must contain at least 6 ");
                } else {
                    tilPass.setErrorEnabled(false);
                    tilUser.setErrorEnabled(false);
                    saveHistory(user);
                    autoTexts.add(user);
                    adapter.notifyDataSetChanged();
                    //必须以snackBar弹出的父CoordinatorLayout作为相对的view
                    final CoordinatorLayout parentCLRoot = (CoordinatorLayout) getActivity().findViewById(R.id.cl_root);
                    Snackbar.make(parentCLRoot, "login...", Snackbar.LENGTH_LONG).setAction("cancel", new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            Snackbar.make(parentCLRoot, "login cancel..", Snackbar.LENGTH_INDEFINITE).show();//一直显示
                        }
                    }).setActionTextColor(ContextCompat.getColor(getContext(), R.color.colorAccent)).show();
                }
            }
        });

    }


    /**
     * 获取焦点时自动提示登录的历史账号
     */
    private void setAutoText() {
        autoTexts = new ArrayList<>(users);
        SharedPreferences sp = getActivity().getSharedPreferences("history", Context.MODE_PRIVATE);
        Set<String> historyUser = sp.getStringSet("historyUser", null);
        if (historyUser != null) {
            autoTexts.addAll(historyUser);
        }
        adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_dropdown_item, autoTexts);
        etUser.setAdapter(adapter);
    }


    /**
     * 保存登录过的历史账号名
     *
     * @param userName 账号名
     */
    private void saveHistory(String userName) {
        SharedPreferences sp = getActivity().getSharedPreferences("history", Context.MODE_PRIVATE);
        //取出之前保存的数据
        Set<String> longHistory = sp.getStringSet("historyUser", new HashSet<String>());
        if (!longHistory.contains(userName)) {
            SharedPreferences.Editor editor = sp.edit();
            users.add(userName);
            users.addAll(longHistory);
            editor.putStringSet("historyUser", users);
            editor.apply();
        }
    }


    /**
     * 简单的密码校验，判断是否少于6位数
     *
     * @param passWord 密码
     * @return boolean
     */
    private boolean validatePassWord(String passWord) {
        return passWord.length() > 5 && passWord.length() < 13;
    }


    /**
     * 检验邮箱
     *
     * @param user 用户名
     * @return boolean
     */
    private boolean validateEmail(String user) {
//        Patterns.EMAIL_ADDRESS.matcher(user).matches();
//        String EMAIL_PATTERN = "^[a-zA-Z0-9#_~!$&'()*+,;=:.\"<>@\\[\\]\\\\]+@[a-zA-Z0-9-]+(\\.[a-zA-Z0-9-]+)*$";
//        Pattern pattern = Pattern.compile(EMAIL_PATTERN);
//        Matcher matcher = pattern.matcher(user);
//        return matcher.matches();
//        第二种方法
        return Patterns.EMAIL_ADDRESS.matcher(user).matches();
    }

    @Override
    public void onResume() {
        super.onResume();
        MobclickAgent.onPageStart(getContext().getPackageName());//统计页面
    }

    @Override
    public void onPause() {
        super.onPause();
        MobclickAgent.onPageEnd(getContext().getPackageName()); //统计页面
    }
}
