package com.example.fastpass.ui.home; // Replace with your actual package name

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;

import com.example.fastpass.Entrance;
import com.example.fastpass.R;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class HomeFragment extends Fragment {

    private TextView textViewTime;
    private Handler handler = new Handler();
    private Runnable runnable;

    CardView entrance;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Initialize the TextView for date
        textViewTime = view.findViewById(R.id.time);

        // Define the Runnable to update date
        runnable = new Runnable() {
            @Override
            public void run() {
                updateDate();
                handler.postDelayed(this, 1000 * 60 * 60); // Update every hour
            }
        };

        // Start the Runnable
        handler.post(runnable);

        // Other existing code goes here
        // For example:
        // TextView someOtherTextView = view.findViewById(R.id.some_other_text_view);
        // someOtherTextView.setText("Hello, World!");
        entrance = view.findViewById(R.id.entrance_card);

        entrance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent entrance = new Intent(getActivity(), Entrance.class);
                startActivity(entrance);
            }
        });

        return view;
    }

    private void updateDate() {
        SimpleDateFormat sdf = new SimpleDateFormat("EEEE, MMMM d, yyyy", Locale.getDefault());
        String currentDate = sdf.format(new Date());
        textViewTime.setText(currentDate);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacks(runnable); // Remove callbacks to prevent memory leaks
    }
}
