package com.demo.wyd.materialDesignerDemo.activity;

import android.Manifest;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.widget.ImageView;
import android.widget.TextView;

import com.demo.wyd.materialDesignerDemo.R;
import com.demo.wyd.materialDesignerDemo.fragment.Fragment1;
import com.demo.wyd.materialDesignerDemo.fragment.Fragment2;
import com.demo.wyd.materialDesignerDemo.fragment.Fragment3;
import com.demo.wyd.materialDesignerDemo.fragment.Fragment4;
import com.demo.wyd.materialDesignerDemo.fragment.Fragment5;
import com.demo.wyd.materialDesignerDemo.fragment.Fragment6;
import com.demo.wyd.materialDesignerDemo.fragment.Fragment7;
import com.demo.wyd.materialDesignerDemo.fragment.Fragment8;
import com.umeng.analytics.MobclickAgent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

//    SnackBar
//    FloatingActionButton
//    CoordinatorLayout
//    TextInputLayout
//    TabLayout
//    AppBarLayout
//    CollapsingToolbarLayout
//    DrawerLayout

public class MainActivity extends AppCompatActivity {

    private ViewPager viewPager;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private List<String> tabTitles;
    private int[] imageIds;
    private List<Fragment> fragments;
    private List<FloatingActionButton> fabList;
    private boolean isOpen; //菜单是否弹出

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
//      requestWindowFeature(Window.FEATURE_NO_TITLE);//继承Activity使用
        supportRequestWindowFeature(Window.FEATURE_NO_TITLE);//继承AppCompatActivity使用
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.tool_bar);
        setSupportActionBar(toolbar);
        initDrawerLayout();
        initData();
        initViewPager();
        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab_button);

        fabButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                triggerMenuItem();
            }
        });


        Snackbar.make(toolbar, "onCreate", Snackbar.LENGTH_LONG).show();
        MobclickAgent.onProfileSignIn("myLogin");
        /* 置是否对日志信息进行加密, 默认false(不加密). */
//        MobclickAgent.enableEncrypt(true);  //6.0.0版本及以后
//        getDeviceInfo(this);
    }


    /**
     * 初始化Fragment，tab的文字，tab的图标
     */
    private void initData() {
        fragments = new ArrayList<>();
        Fragment fragment;
        fragment = new Fragment1();
        fragments.add(fragment);
        fragment = new Fragment2();
        fragments.add(fragment);
        fragment = new Fragment3();
        fragments.add(fragment);
        fragment = new Fragment4();
        fragments.add(fragment);
        fragment = new Fragment5();
        fragments.add(fragment);
        fragment = new Fragment6();
        fragments.add(fragment);
        fragment = new Fragment7();
        fragments.add(fragment);
        fragment = new Fragment8();
        fragments.add(fragment);

        tabTitles = new ArrayList<>();
        tabTitles.add("第一页");
        tabTitles.add("第二页");
        tabTitles.add("第三页");
        tabTitles.add("第四页");
        tabTitles.add("第五页");
        tabTitles.add("第六页");
        tabTitles.add("第七页");
        tabTitles.add("第八页");

        imageIds = new int[tabTitles.size()];
        for (int i = 0; i < imageIds.length; i++) {
            imageIds[i] = R.mipmap.ic_tab_left;
        }

        FloatingActionButton fab1, fab2, fab3, fab4, fab5;
        fab1 = (FloatingActionButton) findViewById(R.id.fab_button1);
        fab2 = (FloatingActionButton) findViewById(R.id.fab_button2);
        fab3 = (FloatingActionButton) findViewById(R.id.fab_button3);
        fab4 = (FloatingActionButton) findViewById(R.id.fab_button4);
        fab5 = (FloatingActionButton) findViewById(R.id.fab_button5);
        fabList = new ArrayList<>(5);
        fabList.add(fab1);
        fabList.add(fab2);
        fabList.add(fab3);
        fabList.add(fab4);
        fabList.add(fab5);
    }


    /**
     * 初始化侧滑菜单
     */
    private void initDrawerLayout() {
        drawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        NavigationView navigationView = (NavigationView) findViewById(R.id.navigation_view);
        //  ActionBarDrawerToggle的作用，实现DrawerListener接口
        // 1.改变Toolbar上的返回按钮图片
        // 2.在打开和关闭Drawer的时候，Toolbar的返回图标会有动画效果。
        // 3.监听侧边栏的打开和收起
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.app_name, R.string.bottom_sheet_behavior);
        drawerToggle.syncState();//初始化状态
        drawerLayout.addDrawerListener(drawerToggle);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    case R.id.item_one:
                        startActivity(new Intent(getBaseContext(), CtlActivity.class));
                        break;
                    case R.id.item_two:
                        startActivity(new Intent(getBaseContext(), CustomViewActivity.class));
//                        viewPager.setCurrentItem(3);
                        break;
                    case R.id.item_three:
                        viewPager.setCurrentItem(4);
                        break;
                }
                item.setChecked(true);//点击了把它设为选中状态
                drawerLayout.closeDrawers();//关闭抽屉
                return true;
            }
        });
    }


    /**
     * 初始化ViewPager
     */
    private void initViewPager() {
        MyPagerAdapter adapter = new MyPagerAdapter(getSupportFragmentManager(), fragments);
        viewPager = (ViewPager) findViewById(R.id.viewpager);
        viewPager.setAdapter(adapter);
        TabLayout tabLayout = (TabLayout) findViewById(R.id.sliding_tabs);
        //使用ViewPager和TabLayout一站式管理Tab，不需要addTab()方法手动添加Tab
        tabLayout.setupWithViewPager(viewPager);
        for (int i = 0; i < tabLayout.getTabCount(); i++) {
            TabLayout.Tab tab = tabLayout.getTabAt(i);
            if (tab != null) {
                tab.setCustomView(getTabView(i));//自定义view
            }
        }
    }

    /**
     * 利用属性动画处理菜单的弹出与收回
     */
    private void triggerMenuItem() {
        int radius = 380;
        AnimatorSet animatorSet = new AnimatorSet();
        if (!isOpen) {
            isOpen = true;
            for (int i = 0; i < fabList.size(); i++) {
                if (fabList.get(i).getVisibility() != View.VISIBLE)
                    fabList.get(i).setVisibility(View.VISIBLE);
                //角度转换为弧度
                double radians = Math.toRadians(90 / (fabList.size() - 1) * i);
                Log.e("degree", "与y轴的夹角：" + 90 / (fabList.size() - 1) * i);
                float translateX = -(float) (Math.sin(radians) * radius);
                float translateY = -(float) (Math.cos(radians) * radius);
                Log.e("translateX", "X方向的位移：" + translateX);
                Log.e("translateY", "Y方向的位移：" + translateY);
                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(fabList.get(i), "TranslationX", 0, translateX),
                        ObjectAnimator.ofFloat(fabList.get(i), "TranslationY", 0, translateY),
                        ObjectAnimator.ofFloat(fabList.get(i), "ScaleX", 0, 1),
                        ObjectAnimator.ofFloat(fabList.get(i), "ScaleY", 0, 1),
                        ObjectAnimator.ofFloat(fabList.get(i), "alpha", 0, 1),
                        ObjectAnimator.ofFloat(fabList.get(i), "alpha", 0, 1)
                );
                animatorSet.setDuration(800);
                animatorSet.setInterpolator(new DecelerateInterpolator());
                animatorSet.start();
            }
        } else {
            isOpen = false;
            for (int i = 0; i < fabList.size(); i++) {
                double radians = Math.toRadians(90 / (fabList.size() - 1) * i);
                float translateX = (float) -Math.sin(radians) * radius;
                float translateY = (float) -Math.cos(radians) * radius;
                animatorSet.playTogether(
                        ObjectAnimator.ofFloat(fabList.get(i), "TranslationX", translateX, 0),
                        ObjectAnimator.ofFloat(fabList.get(i), "TranslationY", translateY, 0),
                        ObjectAnimator.ofFloat(fabList.get(i), "ScaleX", 1, 0),
                        ObjectAnimator.ofFloat(fabList.get(i), "ScaleY", 1, 0),
                        ObjectAnimator.ofFloat(fabList.get(i), "alpha", 1, 0),
                        ObjectAnimator.ofFloat(fabList.get(i), "alpha", 1, 0)
                );
                animatorSet.setDuration(800);
                animatorSet.setInterpolator(new AccelerateInterpolator());
                animatorSet.start();
            }
        }
    }


    /**
     * 获取自定义的tab View
     *
     * @param position tab的位置
     * @return 自定义tab的样式
     */
    private View getTabView(int position) {
        @SuppressLint("InflateParams")
        View view = LayoutInflater.from(getApplicationContext()).inflate(R.layout.v_custom_tab, null);
        TextView tv = (TextView) view.findViewById(R.id.textView);
        tv.setText(tabTitles.get(position));
        ImageView img = (ImageView) view.findViewById(R.id.imageView);
        img.setImageResource(imageIds[position]);
        return view;
    }


    /**
     * $Class
     * ViewPager的适配器，继承FragmentPagerAdapter
     */
    private class MyPagerAdapter extends FragmentPagerAdapter {
        private List<Fragment> fragments;

        MyPagerAdapter(FragmentManager fm, List<Fragment> fragments) {
            super(fm);
            this.fragments = fragments;
        }

        @Override
        public Fragment getItem(int position) {
            return fragments.get(position);
        }

        @Override
        public int getCount() {
            return fragments.size();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);  //统计时长
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    public void onSaveInstanceState(Bundle outState, PersistableBundle outPersistentState) {
        super.onSaveInstanceState(outState, outPersistentState);
        MobclickAgent.onKillProcess(this);
    }

    @Override
    public void onCreate(Bundle savedInstanceState, PersistableBundle persistentState) {
        super.onCreate(savedInstanceState, persistentState);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        MobclickAgent.onProfileSignOff();
    }


    public static boolean checkPermission(Context context, String permission) {
        boolean result = false;
        if (Build.VERSION.SDK_INT >= 23) {
            try {
                Class<?> clazz = Class.forName("android.content.Context");
                Method method = clazz.getMethod("checkSelfPermission", String.class);
                int rest = (Integer) method.invoke(context, permission);
                result = rest == PackageManager.PERMISSION_GRANTED;
            } catch (Exception e) {
                result = false;
            }
        } else {
            PackageManager pm = context.getPackageManager();
            if (pm.checkPermission(permission, context.getPackageName()) == PackageManager.PERMISSION_GRANTED) {
                result = true;
            }
        }
        return result;
    }

//    {"mac":"38:bc:1a:b7:66:25","device_id":"865479025402652"}
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
            String device_id = null;
            if (checkPermission(context, Manifest.permission.READ_PHONE_STATE)) {
                device_id = tm.getDeviceId();
            }
            String mac = null;
            FileReader fstream = null;
            try {
                fstream = new FileReader("/sys/class/net/wlan0/address");
            } catch (FileNotFoundException e) {
                fstream = new FileReader("/sys/class/net/eth0/address");
            }
            BufferedReader in = null;
            if (fstream != null) {
                try {
                    in = new BufferedReader(fstream, 1024);
                    mac = in.readLine();
                } catch (IOException e) {
                } finally {
                    if (fstream != null) {
                        try {
                            fstream.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                    if (in != null) {
                        try {
                            in.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
            json.put("mac", mac);
            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }
            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(),
                        android.provider.Settings.Secure.ANDROID_ID);
            }
            json.put("device_id", device_id);
            Log.e("device_id", json.toString());
            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

}
