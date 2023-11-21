package com.example.talkitout;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;


public class gameEnd extends AppCompatActivity {
    private Button returnButton;
    private TextView countdownText;
    private TextView mainText;
    private TextView conclusionText;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.game_ended);
        returnButton = findViewById(R.id.return_button);
        mainText = findViewById(R.id.main_end_screen_text);
        conclusionText = findViewById(R.id.conclusion_text);
        countdownText = findViewById(R.id.coutdown_text_end);
        int goal = getIntent().getIntExtra("goal",0);
        int score = getIntent().getIntExtra("score",0);
        String timeLeft = getIntent().getStringExtra("time");
        String presetName = getIntent().getStringExtra("preset");
        if (score >= goal){
            mainText.setText("NICE JOB");
        }else{
            mainText.setText("UNLUCKY");
        }
        conclusionText.setText("You got "+score+" out of "+goal+" with "+timeLeft+" time left on the "+presetName+" preset");
        returnButton.setOnClickListener(v -> finish());
    }
}
