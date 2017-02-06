package com.demo.wyd.materialDesignerDemo.fragment;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.animation.AnimatorSet;
import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.AnimationUtils;
import android.view.animation.AnticipateOvershootInterpolator;
import android.view.animation.BounceInterpolator;
import android.view.animation.LinearInterpolator;
import android.view.animation.OvershootInterpolator;
import android.view.animation.RotateAnimation;
import android.view.animation.ScaleAnimation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;

import com.demo.wyd.materialDesignerDemo.R;

/**
 * Description :补间动画/属性动画
 * Created by wyd on 2016/7/20.
 */
public class Fragment6 extends Fragment {
    private View rootView;
    private Button btnAnimObject;
    private View.OnClickListener mOnClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            tweenAnimationWidgetClick(v);
        }
    };
    private View.OnClickListener mOnClickListener1 = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            propertyAnimationWidgetClick(v);
        }
    };
    private ValueAnimator valueAnimator;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (rootView == null) {
            rootView = inflater.inflate(R.layout.frag_6, container, false);
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
        Button btnScale = (Button) rootView.findViewById(R.id.btn_scale);
        Button btnAlpha = (Button) rootView.findViewById(R.id.btn_alpha);
        Button btnRotate = (Button) rootView.findViewById(R.id.btn_rotate);
        Button btnTranslate = (Button) rootView.findViewById(R.id.btn_translate);
        Button btnSet = (Button) rootView.findViewById(R.id.btn_set);

        Button btnValueAnimator = (Button) rootView.findViewById(R.id.btn_value_animator);
        Button btnObjectAnimator = (Button) rootView.findViewById(R.id.btn_object_animator);
        Button btnAnimatorSet = (Button) rootView.findViewById(R.id.btn_animator_set);
        Button btnAnimatorCode = (Button) rootView.findViewById(R.id.btn_animator_code);
        Button btnViewPropertyAnimator = (Button) rootView.findViewById(R.id.btn_view_property_Animator);

        btnScale.setOnClickListener(mOnClickListener);
        btnAlpha.setOnClickListener(mOnClickListener);
        btnRotate.setOnClickListener(mOnClickListener);
        btnTranslate.setOnClickListener(mOnClickListener);
        btnSet.setOnClickListener(mOnClickListener);
        btnValueAnimator.setOnClickListener(mOnClickListener1);
        btnObjectAnimator.setOnClickListener(mOnClickListener1);
        btnAnimatorSet.setOnClickListener(mOnClickListener1);
        btnAnimatorCode.setOnClickListener(mOnClickListener1);
        btnViewPropertyAnimator.setOnClickListener(mOnClickListener1);

        btnAnimObject = (Button) rootView.findViewById(R.id.animator_object);
        btnAnimObject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Snackbar.make(v, btnAnimObject.getText(), Snackbar.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * 实现补间动画的按钮的点击事件
     *
     * @param view 按钮
     */
    private void tweenAnimationWidgetClick(View view) {
        Animation animation = null;
        switch (view.getId()) {
            case R.id.btn_scale:
                animation = new ScaleAnimation(0.5f, 2.0f, 0.5f, 2.0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(3000);
                animation.setFillAfter(true);
//                animation = AnimationUtils.loadAnimation(getContext(), R.anim.scale_anim_test);
                break;
            case R.id.btn_alpha:
                animation = new AlphaAnimation(1, 0);
                animation.setDuration(1200);
                animation.setRepeatCount(Animation.INFINITE);
                animation.setRepeatMode(Animation.REVERSE);
//                animation = AnimationUtils.loadAnimation(getContext(), R.anim.alpha_anim_test);
                break;
            case R.id.btn_rotate:
                animation = new RotateAnimation(-90, 450, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
                animation.setDuration(4000);
                animation.setInterpolator(new AccelerateDecelerateInterpolator());
                animation.setFillAfter(true);
//                animation = AnimationUtils.loadAnimation(getContext(), R.anim.rotate_anim_test);
                break;
            case R.id.btn_translate:
                animation = new TranslateAnimation(Animation.ABSOLUTE, 0, Animation.ABSOLUTE, 0, Animation.RELATIVE_TO_SELF, -0.5f, Animation.RELATIVE_TO_SELF, 1);
                animation.setDuration(2000);
                animation.setFillAfter(true);
                animation.setInterpolator(new BounceInterpolator());
//                animation = AnimationUtils.loadAnimation(getContext(), R.anim.translate_anim_test);
                break;
            case R.id.btn_set:
                animation = AnimationUtils.loadAnimation(getContext(), R.anim.set_anim_test);
                AnimationSet animationSet = new AnimationSet(true);
                animationSet.setInterpolator(new OvershootInterpolator());
                animationSet.addAnimation(animation);
                break;
        }
        btnAnimObject.startAnimation(animation);
    }

    /**
     * 实现属性动画的按钮的点击事件
     *
     * @param view 按钮
     */
    private void propertyAnimationWidgetClick(View view) {
        switch (view.getId()) {
            case R.id.btn_value_animator:
                testValueAnimator();
                break;
            case R.id.btn_object_animator:
                testObjectAnimator();
                break;
            case R.id.btn_animator_set:
                testAnimationSet();
                break;
            case R.id.btn_animator_code:
                testAnimationCode();
                break;
            case R.id.btn_view_property_Animator:
                testViewPropertyAnimator();
                break;
        }
    }


    private void testValueAnimator() {
//        ValueAnimator valueAnimator = ValueAnimator.ofInt(0, getContext().getResources().getDisplayMetrics().heightPixels - btnAnimObject.getHeight());
//        valueAnimator.setDuration(5000);
//        valueAnimator.setRepeatMode(ValueAnimator.REVERSE);
//        valueAnimator.setRepeatCount(ValueAnimator.INFINITE);
//        valueAnimator.setInterpolator(new MyInterpolator());
//        valueAnimator.setInterpolator(new BounceInterpolator());
//        valueAnimator.setEvaluator(new ArgbEvaluator());
//        valueAnimator.setStartDelay(1000);  //延时多久时间开始，单位是毫秒
        if (valueAnimator == null)
            valueAnimator = (ValueAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.value_animator_test);
        if (valueAnimator.isRunning()) {
            valueAnimator.cancel(); //取消动画
            return;
        }
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                //获取ValueAnimator在运动时，当前运动点的值
                int curValue = (int) animation.getAnimatedValue();
                btnAnimObject.layout(curValue, btnAnimObject.getTop(), btnAnimObject.getWidth() + curValue, btnAnimObject.getBottom());
            }
        });


        valueAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {
                btnAnimObject.setText("Start");
            }

            @Override
            public void onAnimationEnd(Animator animation) {
                btnAnimObject.setText("End");
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                btnAnimObject.setText("Cancel");
                animation.removeAllListeners();
            }

            @Override
            public void onAnimationRepeat(Animator animation) {
                btnAnimObject.setText("Repeat");
//                animation.removeAllListeners(); //取消监听之后就不会走到End函数里面
            }
        });
        //开始动画
        valueAnimator.start();
    }

    private void testObjectAnimator() {
        ObjectAnimator objectAnimator = (ObjectAnimator) AnimatorInflater.loadAnimator(getContext(), R.animator.object_animator_test);
        objectAnimator.setTarget(btnAnimObject);
        objectAnimator.start();
    }

    private void testAnimationSet() {
        Animator animationSet = AnimatorInflater.loadAnimator(getContext(), R.animator.animator_set_test);
        animationSet.setTarget(btnAnimObject);
        animationSet.start();
    }

    private void testAnimationCode() {
        PropertyValuesHolder holderX = PropertyValuesHolder.ofFloat("translationX", 0, -260);
        PropertyValuesHolder holderY = PropertyValuesHolder.ofFloat("translationY", 0, -50);
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(btnAnimObject, holderX, holderY);
        animator.setInterpolator(new AnticipateOvershootInterpolator());

        ObjectAnimator animator1 = ObjectAnimator.ofFloat(btnAnimObject, "translationY", -50, 500);

        PropertyValuesHolder holder = PropertyValuesHolder.ofFloat("translationX", -260, 350);
        ObjectAnimator animator2 = ObjectAnimator.ofPropertyValuesHolder(btnAnimObject, holder);

        Keyframe keyframe1 = Keyframe.ofFloat(0, 500);
        Keyframe keyframe2 = Keyframe.ofFloat(0.5f, 200);
        Keyframe keyframe3 = Keyframe.ofFloat(1, -50);
        PropertyValuesHolder holder1 = PropertyValuesHolder.ofKeyframe("translationY", keyframe1, keyframe2, keyframe3);
        ObjectAnimator animator3 = ObjectAnimator.ofPropertyValuesHolder(btnAnimObject, holder1);

        PropertyValuesHolder holder2 = PropertyValuesHolder.ofFloat("translationX", 350, 100, -260);
        ObjectAnimator animator4 = ObjectAnimator.ofPropertyValuesHolder(btnAnimObject, holder2);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(3000);
        animatorSet.playSequentially(animator, animator1, animator2, animator3, animator4);
        animatorSet.start();
    }

    private void testViewPropertyAnimator() {
        //移动到坐标（0,-50）,
        btnAnimObject.animate().x(0).y(-50).setDuration(3000).setInterpolator(new LinearInterpolator());
    }
}

