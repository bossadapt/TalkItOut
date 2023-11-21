package com.example.talkitout;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.RecyclerView;
import androidx.room.Room;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class listCreator extends AppCompatActivity {
    private static List<String> currentItems = new ArrayList<>();
    private static String recommendedTitle = "";
    private static Preset oldPreset;
    private static boolean editMode;
    private static boolean numberToggle;
    private AlertDialog finished;
    private AlertDialog.Builder dialog;
    private Button nToggleButton;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.list_creator);
        nToggleButton = findViewById(R.id.number_toggle_button);
        Button addItemButton = findViewById(R.id.add_item_button);
        Button exitButton = findViewById(R.id.exit_button);
        Button finishButton = findViewById(R.id.finish_button);

        dialog = new AlertDialog.Builder(this);
        numberToggle = true;
        currentItems.clear();


        finishButton.setOnClickListener(v -> finalize(dialog));
        exitButton.setOnClickListener(v -> finishUpgraded());
        addItemButton.setOnClickListener(view -> addItem(null));
        nToggleButton.setOnClickListener(v -> toggleNumber());

        //getting intents and setting private variables
        if (getIntent().hasExtra("preset")) {
            oldPreset = getIntent().getParcelableExtra("preset");
            recommendedTitle = oldPreset.name;
            editMode = true;
            for (int i = 0; i < oldPreset.items.size(); i++) {
                addItem(oldPreset.items.get(i));
            }
        }
        else{
            editMode = false;
            recommendedTitle ="";
        }
    }
    private void toggleNumber(){
        if(numberToggle){
            nToggleButton.setText("T");
        }else{
            nToggleButton.setText("T+#");
        }
        numberToggle = !numberToggle;
    }
    private void finishUpgraded(){
        // have to have the activity here or it forgets what activity it came from for some reason
        Intent backToStart = new Intent(getApplicationContext(),MainActivity.class);
        backToStart.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(backToStart);
        finish();
    }
    public void addItem(String item){
        String title;
        RecyclerView optionsRecyclerView = findViewById(R.id.items_recyclerview);
        EditText edittext = findViewById(R.id.item_edittext);
        if(item != null){
            title = item;
        }
        else {
            title = edittext.getText().toString();
        }
        //add the option to the recyclingView and list
        List<Boolean> checkList = new ArrayList<>();
        checkList.add(title.contains("\n"));
        checkList.add(title.contains(","));
        checkList.add(title.contains("|"));
        if(!title.isEmpty()) {
            List<String> listAdded = new ArrayList<>();
            if(checkList.get(0)){
                listAdded = Arrays.asList(title.split("\n"));
            }
            if(checkList.get(1)){
                if(listAdded.isEmpty()) {
                    listAdded = Arrays.asList(title.split(","));
                }else{
                    for(int i =0; i<listAdded.size();i++){
                        listAdded = Arrays.asList(title.split(","));
                    }
                }
            }
            if(checkList.get(2)) {
                if (listAdded.isEmpty()) {
                    listAdded = Arrays.asList(title.split(","));
                } else {
                    for (int i = 0; i < listAdded.size(); i++) {
                        listAdded = Arrays.asList(title.split(","));
                    }
                }
            }
            if(listAdded.isEmpty()) {
                currentItems.add(title);

            }else{
                currentItems.addAll(cleanList(listAdded));
            }
            }
            optionsRecyclerView.setAdapter(new creatorAdapter(currentItems));
            edittext.setText("");
            Log.d("Reached", "addItem:"+ currentItems.toString());
        }

    private List<String> cleanList(List<String> list){
        List<String> list2 = new ArrayList<>();
        for(int i = 0; i<list.size();i++){
            String temp = list.get(i);
            if(!numberToggle){
                // helps with lists with ids and removes them
                temp = temp.replaceAll("\\d","");
                temp = temp.replaceAll("\\.","");
            }
            temp = temp.trim();
            if(!temp.isEmpty()){
                //if the strings still contain tabs even after trim then split the terms
                if(temp.contains("\t")){
                    List<String> tempList = Arrays.asList(temp.split("\t"));
                    tempList.replaceAll(String::trim);
                    list2.addAll(tempList);
                }else {
                    list2.add(temp);
                }
            }
        }
        return list2;
    }
    public void finalize(AlertDialog.Builder dialog){
        Log.d("Reached", "finalize: ");
        //layout to fill the dialog
        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        // Add a TextView here for the "Title" label, as noted in the comments
        final EditText titleBox = new EditText(this);
        if(recommendedTitle != ""){
            titleBox.setText(recommendedTitle);
        }
        else{
            titleBox.setHint("Title");
        }
        layout.addView(titleBox);
        //Add button
        final Button finishButton = new Button(this);
        finishButton.setText("Finish");
        layout.addView(finishButton);
        dialog.setView(layout);
        dialog.show();
        finished = dialog.create();
        finishButton.setOnClickListener(v -> end(titleBox));


    }

    public void end(EditText eTitle){
        if(finished!=null) {
            finished.dismiss();
            if(finished!=null) {
                finished = null;
            }
        }
        if(this.dialog!=null){
            this.dialog = null;
        }
        String title = eTitle.getText().toString();
        Preset newPreset = new Preset();
        newPreset.setName(title);
        newPreset.setItems(currentItems);

        if(!title.equals("")) {
            AppDatabase db = Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "choiceDataset").allowMainThreadQueries().build();
            if(editMode){
                db.itemListDao().delete(oldPreset);;
            }
            Log.d("Reached", "end: "+ newPreset.toString());
            db.itemListDao().insert(newPreset);
            db.close();
            Intent backToStart = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(backToStart);
            finishUpgraded();
        }
        else {
            Toast wrongTitle = Toast.makeText(getApplicationContext(),"Cant leave the title empty",Toast.LENGTH_SHORT);
            wrongTitle.show();
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(finished!=null) {
            finished.dismiss();
            if(finished!=null) {
                finished = null;
            }
        }
        if(dialog!=null){
            dialog = null;
        }
    }
}
