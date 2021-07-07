package com.gongwu.wherecollect.util;

import android.animation.Keyframe;
import android.animation.ObjectAnimator;
import android.animation.PropertyValuesHolder;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.OvershootInterpolator;
import android.view.animation.TranslateAnimation;

/**
 * Function:
 * Date: 2017/11/16
 *
 * @author zhaojin
 * @since JDK 1.7
 */
public class AnimationUtil {
    /**
     * 左右抖动动画
     *
     * @param view
     * @param shakeFactor
     * @return
     */
    public static ObjectAnimator StartTada(View view, float shakeFactor) {
        PropertyValuesHolder pvhScaleX = PropertyValuesHolder.ofKeyframe(View.SCALE_X,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );
        PropertyValuesHolder pvhScaleY = PropertyValuesHolder.ofKeyframe(View.SCALE_Y,
                Keyframe.ofFloat(0f, 1f),
                Keyframe.ofFloat(.1f, .9f),
                Keyframe.ofFloat(.2f, .9f),
                Keyframe.ofFloat(.3f, 1.1f),
                Keyframe.ofFloat(.4f, 1.1f),
                Keyframe.ofFloat(.5f, 1.1f),
                Keyframe.ofFloat(.6f, 1.1f),
                Keyframe.ofFloat(.7f, 1.1f),
                Keyframe.ofFloat(.8f, 1.1f),
                Keyframe.ofFloat(.9f, 1.1f),
                Keyframe.ofFloat(1f, 1f)
        );
        PropertyValuesHolder pvhRotate = PropertyValuesHolder.ofKeyframe(View.ROTATION,
                Keyframe.ofFloat(0f, 0f),
                Keyframe.ofFloat(.1f, -3f * shakeFactor),
                Keyframe.ofFloat(.2f, -3f * shakeFactor),
                Keyframe.ofFloat(.3f, 3f * shakeFactor),
                Keyframe.ofFloat(.4f, -3f * shakeFactor),
                Keyframe.ofFloat(.5f, 3f * shakeFactor),
                Keyframe.ofFloat(.6f, -3f * shakeFactor),
                Keyframe.ofFloat(.7f, 3f * shakeFactor),
                Keyframe.ofFloat(.8f, -3f * shakeFactor),
                Keyframe.ofFloat(.9f, 3f * shakeFactor),
                Keyframe.ofFloat(1f, 0)
        );
        ObjectAnimator animator = ObjectAnimator.ofPropertyValuesHolder(view, pvhScaleX, pvhScaleY, pvhRotate).
                setDuration(500);
        animator.start();
        return animator;
    }

    public static void StartTranslate(View view) {
        TranslateAnimation animation = new TranslateAnimation(15, -15, 0, 0);
        animation.setInterpolator(new OvershootInterpolator());
        animation.setDuration(60);
        animation.setRepeatCount(3);
        animation.setRepeatMode(Animation.REVERSE);
        view.startAnimation(animation);
    }

    public static void StartTranslate(View view, boolean showOrHide) {
        if (showOrHide) {
            if (view.getVisibility() == View.VISIBLE) {
                return;
            } else {
                view.setVisibility(View.VISIBLE);
            }
        } else {
            if (view.getVisibility() != View.VISIBLE) {
                return;
            }
        }
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, showOrHide ? 0.0f : 1.0f,
                Animation.RELATIVE_TO_SELF, showOrHide ? 1.0f : 0.0f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        view.startAnimation(animation);
        if (!showOrHide) {
            view.setVisibility(View.GONE);
        }
    }

    public static void StartTranslateOutside(View view, boolean showOrHide) {
        if (showOrHide) {
            if (view.getVisibility() == View.VISIBLE) {
                return;
            }
        } else {
            if (view.getVisibility() != View.VISIBLE) {
                return;
            }
        }
        TranslateAnimation animation = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.0f,
                Animation.RELATIVE_TO_SELF, showOrHide ? -1.0f : 0.0f,
                Animation.RELATIVE_TO_SELF, showOrHide ? 0.0f : -1.0f);
        animation.setDuration(500);
        animation.setFillAfter(true);
        view.startAnimation(animation);
        view.setVisibility(showOrHide?View.VISIBLE:View.GONE);
    }

    /**
     * 向上滑动
     *
     * @param view
     * @param time 动画持续时间
     */
    public static void upSlide(View view, int time) {
        if (view.getTag() != null && ((Boolean) view.getTag())) {
            return;
        }
        ObjectAnimator.ofFloat(view, "translationY", view.getHeight(), 0)
                .setDuration(time).start();
        view.setTag(true);
    }

    public static void upSlide(View view, int time, float y) {
        if (view.getTag() != null && ((Boolean) view.getTag())) {
            return;
        }
        ObjectAnimator.ofFloat(view, "translationY", y, 0)
                .setDuration(time).start();
        view.setTag(true);
    }

    /**
     * 向下滑动
     *
     * @param view
     * @param time 动画持续时间
     */
    public static void downSlide(View view, int time) {
        if (view.getTag() != null && (!((Boolean) view.getTag()))) {
            return;
        }
        ObjectAnimator.ofFloat(view, "translationY", 0, view.getHeight())
                .setDuration(time).start();
        view.setTag(false);
    }

    public static void downSlide(View view, int time, float y) {
        if (view.getTag() != null && (!((Boolean) view.getTag()))) {
            return;
        }
        ObjectAnimator.ofFloat(view, "translationY", 0, y)
                .setDuration(time).start();
        view.setTag(false);
    }


    /**
     * 向右滑动
     *
     * @param view
     * @param time 动画持续时间
     */
    public static void upRight(View view, int time) {
        if (view.getTag() != null && (!(Boolean) view.getTag())) {
            return;
        }

        ObjectAnimator.ofFloat(view, "translationX", 0, view.getWidth() * 2)
                .setDuration(time).start();
        view.setTag(false);
    }


    /**
     * 向左滑动
     *
     * @param view
     * @param time 动画持续时间
     */
    public static void upLeft(View view, int time) {
        if (view.getTag() != null && ((Boolean) view.getTag())) {
            return;
        }
        ObjectAnimator.ofFloat(view, "translationX", view.getWidth() * 2, 0)
                .setDuration(time).start();
        view.setTag(true);
    }
}
