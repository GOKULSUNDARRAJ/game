package com.example.myapplication;

import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private List<Modelclass> barsColor;
    private AlertDialog alertDialog;
    private Adapter adapter;
    private boolean isGameOverDueToWrongMove = false;
    private CountDownTimer countDownTimer;

    private int level = 1;

    private static final String COLOR_YELLOW = "Yellow";
    private static final String COLOR_RED = "Red";

    private boolean isGameInProgress = true;
    private boolean isGameWon = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initializeGame();

        TextView textView = findViewById(R.id.textViewLevel);
        textView.setText("Level-" + level);
    }

    private void initializeGame() {
        barsColor = new ArrayList<>();
        Random random = new Random();

        // Initialize bars with random colors
        for (int i = 0; i < 15; i++) {
            int n = random.nextInt(2);
            if (n == 0) {
                barsColor.add(new Modelclass(COLOR_YELLOW));
            } else {
                barsColor.add(new Modelclass(COLOR_RED));
            }
        }


        // Set up RecyclerView
        recyclerView = findViewById(R.id.recyclerview);
        adapter = new Adapter(this, barsColor);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        // Set up ItemTouchHelper for swipe actions
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(simpleCallback);
        itemTouchHelper.attachToRecyclerView(recyclerView);

        // Initialize CountDownTimer with a time limit (e.g., 60 seconds)
        int timeLimitInSeconds = 10;
        startCountDownTimer(timeLimitInSeconds);

        adapter.notifyDataSetChanged();


    }
    ItemTouchHelper.SimpleCallback simpleCallback = new ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT) {
        @Override
        public boolean onMove(@NonNull @NotNull RecyclerView recyclerView, @NonNull @NotNull RecyclerView.ViewHolder viewHolder, @NonNull @NotNull RecyclerView.ViewHolder target) {
            return false;
        }

        @Override
        public void onSwiped(@NonNull @NotNull RecyclerView.ViewHolder viewHolder, int direction) {
            int position = viewHolder.getAdapterPosition();
            switch (direction) {
                case ItemTouchHelper.LEFT: {
                    handleSwipe(position, COLOR_RED);
                    break;
                }
                case ItemTouchHelper.RIGHT: {
                    handleSwipe(position, COLOR_YELLOW);
                    break;
                }
            }
        }
    };


    private void handleSwipe(int position, String expectedColor) {
        if ((barsColor.get(position).getColor()).equals(expectedColor)) {
            barsColor.remove(position);
            adapter.notifyItemRemoved(position);
            checkForWin(); // Call checkForWin after each successful swipe
        } else {
            endTheGameDueToWrongMove();
            alertDialog.show();
        }
    }



    private void restartGame() {
        barsColor.clear();
        Random random = new Random();

        // Add 5 bars to each level
        int barsCount = 10 + level * 5;

        // Generate new bars with random colors
        for (int i = 0; i < barsCount; i++) {
            int n = random.nextInt(2);
            if (n == 0) {
                barsColor.add(new Modelclass(COLOR_YELLOW));
            } else {
                barsColor.add(new Modelclass(COLOR_RED));
            }
        }

        adapter.notifyDataSetChanged();

        // Cancel the existing timer
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }

        // Start a new timer with the initial time limit (e.g., 60 seconds)
        int initialTimeLimitInSeconds = 10;
        startCountDownTimer(initialTimeLimitInSeconds);

        String levelText = "Level-" + level;
        TextView textViewLevel = findViewById(R.id.textViewLevel);
        textViewLevel.setText(levelText);
        // Delay before checking for win again (optional)
        isGameInProgress = true;

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                checkForWin();
            }
        }, 1000); // Adjust the delay time if needed
        isGameWon = false;
        TextView textViewBarsCount = findViewById(R.id.textView);
        textViewBarsCount.setText("Woods:" +barsCount);
    }





    private void showGameOverAlert(boolean isWin) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View customView = getLayoutInflater().inflate(R.layout.custom_alert_dialog, null);
        customView.setBackgroundColor(Color.TRANSPARENT);
        builder.setView(customView);
        alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Access the views in the custom layout
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) TextView messageTextView = customView.findViewById(R.id.alert_message);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button positiveButton = customView.findViewById(R.id.positive_button);
        @SuppressLint({"MissingInflatedId", "LocalSuppress"}) Button negativeButton = customView.findViewById(R.id.negative_button);
        TextView textTitle = customView.findViewById(R.id.textTitle);
        ImageView imageView=customView.findViewById(R.id.imagewon);

        if (isWin) {
            textTitle.setText("Congratulations");
            messageTextView.setText("Congratulations! You cleared all bars!");
            positiveButton.setText("Next Level");
            negativeButton.setVisibility(View.GONE);
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.won);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isWin) {
                        level++;
                        Toast.makeText(MainActivity.this, "Level up! Current Level: " + level, Toast.LENGTH_SHORT).show();
                        String barsCountString = Integer.toString(level);

                    }
                    restartGame();
                    alertDialog.dismiss();
                }
            });

        } else {
            textTitle.setText("All the Best");
            messageTextView.setText("Oops! Wrong side! Try Again!");
            positiveButton.setText("Try Again");
            imageView.setVisibility(View.VISIBLE);
            imageView.setImageResource(R.drawable.wrong);
            positiveButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    restartGame();
                    Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                    alertDialog.dismiss();
                }
            });
            negativeButton.setText("Later");
            negativeButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent anotherActivityIntent = new Intent(MainActivity.this, GameoverActivity.class);
                    startActivity(anotherActivityIntent);
                    finish();
                    alertDialog.dismiss();
                }
            });
        }
        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void endTheGameDueToWrongMove() {
        isGameOverDueToWrongMove = true;
        isGameInProgress = false; // Set the flag to false
        showGameOverAlert(false);
    }


    private void startCountDownTimer(int seconds) {
        int initialTimeInSeconds = seconds + (level - 1) * 5; // Add 5 seconds for each level
        countDownTimer = new CountDownTimer(initialTimeInSeconds * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (isGameInProgress) {
                    long secondsRemaining = millisUntilFinished / 1000;
                    TextView timerTextView = findViewById(R.id.timerTextView);
                    timerTextView.setText("Time: " + secondsRemaining + "s");
                }
            }

            @Override
            public void onFinish() {
                if (isGameInProgress) {
                    countDownTimer.cancel();
                    showTimeUpAlert();
                }
            }
        };

        countDownTimer.start();
    }

    private void showTimeUpAlert() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        View customView = getLayoutInflater().inflate(R.layout.custom_alert_dialogfortime, null);
        customView.setBackgroundColor(Color.TRANSPARENT);
        builder.setView(customView);
        alertDialog = builder.create();

        alertDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        // Access the views in the custom layout
         TextView messageTextView = customView.findViewById(R.id.alert_message);
  Button positiveButton = customView.findViewById(R.id.positive_button);
        Button negativeButton = customView.findViewById(R.id.negative_button);
        TextView textTitle = customView.findViewById(R.id.textTitle);

        textTitle.setText("Time's Up!");
        messageTextView.setText("Sorry, your time is up! Better luck next time!");
        positiveButton.setText("Try Again");
        positiveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                restartGame();
                Toast.makeText(MainActivity.this, "Try again", Toast.LENGTH_SHORT).show();
                alertDialog.dismiss();
            }
        });
        negativeButton.setText("Later");
        negativeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent anotherActivityIntent = new Intent(MainActivity.this, GameoverActivity.class);
                startActivity(anotherActivityIntent);
                finish();
                alertDialog.dismiss();
            }
        });

        alertDialog.setCanceledOnTouchOutside(false);
        alertDialog.show();
    }

    private void endTheGame() {
        isGameInProgress = false; // Set the flag to false
        // Show game over alert for a loss
        showGameOverAlert(false);
    }

    private void checkForWin() {
        if (!isGameWon && barsColor.isEmpty()) {
            // If all bars are cleared and the game is not won yet
            isGameWon = true;  // Set the flag to true
            countDownTimer.cancel();  // Stop the countdown timer

            // Show the win message and save the level
            showGameOverAlert(true);
            saveLevel();
        }
    }


    private void saveLevel() {
        // Save the level when the player wins
        SharedPreferences preferences = getSharedPreferences("MyPreferences", MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt("currentLevel", level);
        editor.apply();
    }


    @Override
    public void onBackPressed() {

    }

    private int calculateBarsCount() {
        return 10 + level * 5;
    }
}






