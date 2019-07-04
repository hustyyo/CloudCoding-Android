package com.cloudcoding.ViewLib;

import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;

/**
 * Created by steve.yang on 5/10/16.
 */
public class AnimationLib {

    static int defaultAnimationDuration = 100;

    public static Animation inFromRightAnimation() {

        Animation inFromRight = new TranslateAnimation(
                Animation.RELATIVE_TO_SELF, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        inFromRight.setDuration(defaultAnimationDuration);
        inFromRight.setInterpolator(new AccelerateDecelerateInterpolator());
        return inFromRight;
    }

    public static Animation outToRightAnimation() {
        Animation outtoRight = new TranslateAnimation(
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_SELF, +1.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f,
                Animation.RELATIVE_TO_PARENT, 0.0f);
        outtoRight.setDuration(defaultAnimationDuration);
        outtoRight.setInterpolator(new AccelerateDecelerateInterpolator());
        return outtoRight;
    }

    public static void animateBackgroundColor(View view, int colorFrom, int colorTo, int duration){

        ObjectAnimator.ofObject(view, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
                .setDuration(duration)
                .start();
    }

    public static void animateBackgroundColor(View view, int colorFrom, int colorTo){

        ObjectAnimator.ofObject(view, "backgroundColor", new ArgbEvaluator(), colorFrom, colorTo)
                .setDuration(defaultAnimationDuration)
                .start();
    }

    public static void animateAlpha(View view, float from, float to){
        ObjectAnimator anim = ObjectAnimator.ofFloat(view, "alpha", from, to);
        anim.setDuration(defaultAnimationDuration);
        anim.start();
    }



}
