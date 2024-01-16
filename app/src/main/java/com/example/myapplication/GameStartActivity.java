package com.example.myapplication;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

public class GameStartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game_start);

        Button startButton = findViewById(R.id.startbtn);
        ImageView woodImageView = findViewById(R.id.wood1);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) ImageView woodImageView2 = findViewById(R.id.wood2);
        animateWoodImageViewleft(woodImageView);
        animateWoodImageViewRigth(woodImageView2);



        startButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent intent=new Intent(view.getContext(),MainActivity.class);
                startActivity(intent);
            }
        });
    }

    private void animateWoodImageViewleft(ImageView imageView) {
        // Set up ObjectAnimator to animate translationX
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationX", 0f, 500f);
        animator.setDuration(1000); // Set the duration of the animation in milliseconds
        // Start the animation
        animator.start();
    }


    private void animateWoodImageViewRigth(ImageView imageView) {
        // Set up ObjectAnimator to animate translationX
        ObjectAnimator animator = ObjectAnimator.ofFloat(imageView, "translationX", 500f, 0f);
        animator.setDuration(1000); // Set the duration of the animation in milliseconds

        // Start the animation
        animator.start();
    }
}
