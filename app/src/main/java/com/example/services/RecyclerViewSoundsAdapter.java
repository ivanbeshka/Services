package com.example.services;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class RecyclerViewSoundsAdapter extends RecyclerView.Adapter<SoundViewHolder> {

    private String[] sounds;
    private OnItemClickListener onItemClickListener;

    RecyclerViewSoundsAdapter(String[] sounds, OnItemClickListener onItemClickListener) {
        this.sounds = sounds;
        this.onItemClickListener = onItemClickListener;
    }

    @NonNull
    @Override
    public SoundViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View itemView = inflater.inflate(R.layout.item_sound, parent, false);

        return new SoundViewHolder(itemView, onItemClickListener);
    }

    @Override
    public void onBindViewHolder(@NonNull SoundViewHolder holder, int position) {
        holder.soundName.setText(sounds[position]);
    }

    @Override
    public int getItemCount() {
        return sounds.length;
    }
}

class SoundViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    TextView soundName;

    OnItemClickListener onItemClickListener;

    public SoundViewHolder(@NonNull View itemView, OnItemClickListener onItemClickListener) {
        super(itemView);

        soundName = itemView.findViewById(R.id.tv_sound_name);

        this.onItemClickListener = onItemClickListener;
        soundName.setOnClickListener(this);
    }

    @Override
    public void onClick(View view) {
        onItemClickListener.onItemClick(getAdapterPosition());
    }
}

interface OnItemClickListener {
    void onItemClick(int position);
}