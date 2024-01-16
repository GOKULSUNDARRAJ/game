package com.example.myapplication;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewPropertyAnimator;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class CustomItemAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        final View view = holder.itemView;
        final ViewPropertyAnimator animation = view.animate();

        // Customize the removal animation
        ObjectAnimator rightSideUpAnimator = ObjectAnimator.ofFloat(view, "rotationX", 0f, -90f);
        rightSideUpAnimator.setDuration(getRemoveDuration() / 2);
        rightSideUpAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                dispatchRemoveFinished(holder);
            }
        });

        ObjectAnimator goAnimator = ObjectAnimator.ofFloat(view, "translationX", view.getWidth());
        goAnimator.setDuration(getRemoveDuration() / 2);

        rightSideUpAnimator.start();
        goAnimator.start();

        return true;
    }

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        return super.animateAdd(holder);
    }
}
