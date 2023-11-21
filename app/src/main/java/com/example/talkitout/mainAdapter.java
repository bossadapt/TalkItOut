package com.example.talkitout;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class mainAdapter extends RecyclerView.Adapter<mainAdapter.creatorViewHolder> {
    private List<Preset> presets;
    private AppDatabase db;
    private Context context;

    mainAdapter(List<Preset> presets, AppDatabase db, Context context) {
        this.presets = presets;
        this.db = db;
        this.context = context;
    }

    @NonNull
    @Override
    public creatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new creatorViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.main_container,
                        parent,
                        false
                )
        );
    }
    @Override
    public void onBindViewHolder(@NonNull creatorViewHolder holder, int position) {
        holder.textTitle.setText(presets.get(position).name);
    }
    @Override
    public int getItemCount() {
        return presets.size();
    }
    class creatorViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;

        creatorViewHolder(@NonNull View itemView) {
            super(itemView);
            itemView.setOnClickListener(v -> playList(getAdapterPosition()));
            textTitle = itemView.findViewById(R.id.itemText);
            itemView.findViewById(R.id.delete_button).setOnClickListener(v -> removeAt(getAdapterPosition()));
            itemView.findViewById(R.id.edit_button).setOnClickListener(v -> editPreset(getAdapterPosition()));
        }

        //remove and update the other positions so that there is not empty space
        public void removeAt(int position){
            db.itemListDao().delete(presets.get(position));
            presets.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, presets.size());
        }
        public void editPreset(int position){
            Intent presetTest = new Intent(context,listCreator.class);
            presetTest.putExtra("preset", presets.get(position));
            presetTest.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(presetTest);
        }
        public void playList(int position){
            Intent presetTest = new Intent(context,playSettings.class);
            presetTest.putExtra("pos", position);
            presetTest.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(presetTest);
        }
    }
}
