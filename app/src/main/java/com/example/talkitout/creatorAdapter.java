package com.example.talkitout;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class creatorAdapter extends RecyclerView.Adapter<creatorAdapter.creatorViewHolder> {
    private List<String> items;

    creatorAdapter(List<String> items) {
        this.items = items;
    }

    @NonNull
    @Override
    public creatorViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new creatorViewHolder(
                LayoutInflater.from(parent.getContext()).inflate(
                        R.layout.creator_container,
                        parent,
                        false
                )
        );
    }
    @Override
    public void onBindViewHolder(@NonNull creatorViewHolder holder, int position) {
        holder.textTitle.setText(items.get(position));
    }
    @Override
    public int getItemCount() {
        return items.size();
    }
    class creatorViewHolder extends RecyclerView.ViewHolder {
        private TextView textTitle;

        creatorViewHolder(@NonNull View itemView) {
            super(itemView);
            textTitle = itemView.findViewById(R.id.itemText);
            itemView.findViewById(R.id.delete_button).setOnClickListener(v -> removeAt(getAdapterPosition()));
        }

        //remove and update the other positions so that there is not empty space
        public void removeAt(int position) {
            items.remove(position);
            notifyItemRemoved(position);
            notifyItemRangeChanged(position, items.size());
        }
    }
}
