package com.example.talkitout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.room.Room;
import java.util.List;

public class playSettings extends AppCompatActivity implements AdapterView.OnItemSelectedListener {
    public int primaryPreset;
    List<Preset> presetList;
    public int timeInSeconds = 30;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("Reached", "onCreate: playsettings");
        setContentView(R.layout.play_settings);
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "choiceDataset").allowMainThreadQueries().build();
        presetList = db.itemListDao().getAll();
        db.close();
        primaryPreset = getIntent().getIntExtra("pos",0);
        String[] presetNames= new String[presetList.size()];

        for(int i = 0; i < presetList.size();i++){
            presetNames[i] = presetList.get(i).name;
        }

        //preset
        Spinner spinnerPreset = findViewById(R.id.spinner_presets);
        spinnerPreset.setOnItemSelectedListener(this);
        ArrayAdapter<String> spinnerAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, presetNames);
        spinnerAdapter.setDropDownViewResource(androidx.appcompat.R.layout.support_simple_spinner_dropdown_item);
        spinnerPreset.setAdapter(spinnerAdapter);
        spinnerPreset.setSelection(primaryPreset);
        //for time
        TextView timeText = findViewById(R.id.slider_time_text);
        SeekBar timeSeek = findViewById(R.id.seek_time);
        timeSeek.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                // should say 0:30 to 10 min
                String timeString = "0:00";
                timeInSeconds = (int)((progress * 5.7)+30);
                int min = timeInSeconds/60;
                int sec = timeInSeconds%60;
                if(sec<10){
                     timeString = Integer.toString(min)+":0"+ Integer.toString(sec);
                }
                else{
                     timeString = Integer.toString(min)+":"+ Integer.toString(sec);
                }
                timeText.setText(timeString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }
        });
        //button for moving
        Button playButton = findViewById(R.id.play_button);
        playButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                play(presetList.get(primaryPreset),timeInSeconds);
            }
        });
    }
    public void play(Preset preset, int timeInSeconds){
        EditText goalTextview = findViewById(R.id.goal_int);
        String goalString = goalTextview.getText().toString();
        int goalInt = 0;
        try{
            goalInt = Integer.parseInt(goalString);
        } catch(NumberFormatException ex){
            Log.d("Reached", "goal int parse fail");;
        }
        int finalGoal = goalInt;
        Intent presetTest = new Intent(this, play.class);
        presetTest.putExtra("preset", preset);
        presetTest.putExtra("time", timeInSeconds);
        presetTest.putExtra("goal", finalGoal);
        presetTest.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        Log.d("Reached", "finished: playsettings");
        getApplicationContext().startActivity(presetTest);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        primaryPreset = position;
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
