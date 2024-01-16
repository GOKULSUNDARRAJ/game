package com.example.myapplication;

import android.content.Context;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {

    private List<Modelclass> bars;
    private Context context;

    Adapter(Context c, List<Modelclass> list) {
        bars = list;
        context = c;
    }

    @NonNull
    @NotNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull @NotNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.bars, parent, false);
        return new ViewHolder(view);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    public void onBindViewHolder(@NonNull @NotNull Adapter.ViewHolder holder, int position) {
        // Getting the color for every position
        String color = bars.get(position).getColor();

        // Set the background to the bar using an image
        if (color.equals("Yellow")) {
            holder.linearLayout.setBackgroundResource(R.drawable.rightwood); // Replace with your yellow image resource
        } else {
            holder.linearLayout.setBackgroundResource(R.drawable.leftwood); // Replace with your blue image resource
        }

        // Add fade-out animation to the bar
        setFadeOutAnimation(holder.linearLayout);
    }



    @Override
    public int getItemCount() {
        return bars.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        LinearLayout linearLayout;

        public ViewHolder(@NonNull @NotNull View itemView) {
            super(itemView);
            linearLayout = itemView.findViewById(R.id.barlayout);
        }
    }

    private void setFadeOutAnimation(View view) {
        Animation fadeOut = new AlphaAnimation(1, 0);
        fadeOut.setDuration(500); // Set your desired duration
        view.startAnimation(fadeOut);
    }
}
