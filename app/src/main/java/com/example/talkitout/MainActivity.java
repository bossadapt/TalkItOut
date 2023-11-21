package com.example.talkitout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    public static List<Preset> currentPresets = new ArrayList<>();
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        refreshRecycleView();
        Button add_button = findViewById(R.id.add_button);
        Intent add = new Intent(this, listCreator.class);
        add_button.setOnClickListener(view -> startActivity(add));
    }
    private void refreshRecycleView(){
        AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "choiceDataset").allowMainThreadQueries().build();
        currentPresets = db.itemListDao().getAll();
        RecyclerView presetsRecyclerView = findViewById(R.id.main_recycler);
        presetsRecyclerView.setAdapter(new mainAdapter(currentPresets,db,this));
    }
    @Override
    protected void onResume() {
        super.onResume();
        refreshRecycleView();
    }
}