package com.example.talkitout;

import static android.graphics.Color.rgb;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import java.util.ArrayList;
import java.util.List;

public class play extends AppCompatActivity {
    private Button leftButton;
    private Button rightButton;
    private String currentWord;
    private TextView timeText;
    private TextView mainText;
    private Preset currentPreset;
    private List<String> currentOptions;
    private List<String> skippedWords;
    private int score;
    private int goal;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Reached", "onCreate: play");
        setContentView(R.layout.play);

        //views assigned
        leftButton = findViewById(R.id.left_button);
        rightButton = findViewById(R.id.right_button);
        timeText = findViewById(R.id.timeCountdown);
        mainText = findViewById(R.id.main_text_play);
        //extras from playSettings
        currentPreset = getIntent().getParcelableExtra("preset");
        int timeInSeconds = getIntent().getIntExtra("time",30);
        goal = getIntent().getIntExtra("goal",0);
        currentOptions = new ArrayList<>();
        skippedWords = new ArrayList<>();
        if(goal == 0){
            goal = currentPreset.items.size();
        }
        //starts prep timer > main timer
        Log.d("Reached", "onCreate: "+timeInSeconds);
        startPrepTimer(timeInSeconds);

    }
    private void newWord(boolean gotWordRight){
        if(!gotWordRight){
            skippedWords.add(currentWord);
        }
        //refills list either with wrong words or with all putting all the words back
        if (currentOptions.isEmpty()){
            if(!skippedWords.isEmpty()){
                currentOptions.addAll(skippedWords);
            }else{
                currentOptions.addAll(currentPreset.items);
            }
        }
        //gets a random word from this list and removes it to stop excessive repetition
            int randomIndex = (int)(Math.random()*(currentOptions.size()-1));
            currentWord = currentOptions.get(randomIndex);
            mainText.setText(currentWord);
            currentOptions.remove(randomIndex);

    }
    private void enableGame(){
        score=0;
        rightButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newWord(false);
            }
        });
        leftButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newWord(true);
                score +=1;
            }
        });
    }
    private void startPrepTimer(int timeInSeconds){
            new CountDownTimer(5000,1000){

                @Override
                public void onTick(long millisUntilFinished) {
                    int secToFinish = (int)(millisUntilFinished / 1000);
                    String timeString = convertTimeToString(secToFinish);
                    if (secToFinish < 10){
                        timeText.setTextColor(rgb(238, 75, 43));
                    }
                    timeText.setText(timeString);
                }

                @Override
                public void onFinish() {
                    newWord(true);
                    enableGame();
                    startMainTimer(timeInSeconds);
                }
            }.start();
    }
    private void startMainTimer(int timeInSeconds){
        Log.d("Reached", "startMainTimer: "+timeInSeconds);
        int timeInMili = timeInSeconds *1000;
        timeText.setTextColor(rgb(34, 32, 36));
        new CountDownTimer(timeInMili,1000){
            @Override
            public void onTick(long millisUntilFinished) {
                int secToFinish = (int)(millisUntilFinished / 1000);
                String timeString = convertTimeToString(secToFinish);
                if(score >= goal){
                    gameOver(timeString);
                    cancel();
                }
                if (secToFinish < 10){
                    timeText.setTextColor(rgb(238, 75, 43));
                }
                timeText.setText(timeString);
            }

            @Override
            public void onFinish() {
                gameOver("0:00");
            }
        }.start();
    }
    public String convertTimeToString(int timeInSeconds){
        int min = timeInSeconds/60;
        int sec = timeInSeconds%60;
        if(sec<10){
             return Integer.toString(min)+":0"+ Integer.toString(sec);
        }
        else{
             return Integer.toString(min)+":"+ Integer.toString(sec);
        }
    }
    private void gameOver(String timeInSeconds){
        Intent endGame = new Intent(this, gameEnd.class);
        endGame.putExtra("preset", currentPreset.name);
        endGame.putExtra("time", timeInSeconds);
        endGame.putExtra("goal", goal);
        endGame.putExtra("score", score);
        this.startActivity(endGame);
        finish();
    }
}
