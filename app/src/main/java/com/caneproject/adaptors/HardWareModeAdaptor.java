package com.caneproject.adaptors;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.caneproject.classes.Data;
import com.caneproject.R;

import java.util.List;

public class HardWareModeAdaptor extends RecyclerView.Adapter<HardWareModeAdaptor.ViewHolder> {
    static List<Data> dataList;

    public HardWareModeAdaptor(List<Data> myNoteList) {
        HardWareModeAdaptor.dataList = myNoteList;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.listitem, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        try {
            holder.firstPartTV.setText(dataList.get(position).toString());
            holder.secondPartTV.setText(dataList.get(position).toStringForSecondPart());
            holder.imageView.setImageURI(dataList.get(position).getUri());
        } catch (IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
    }

    @Override
    public int getItemCount() {
        return dataList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView firstPartTV, secondPartTV;
        ImageView imageView;
        ConstraintLayout constraintLayout;

        @SuppressLint("ClickableViewAccessibility")
        public ViewHolder(View itemView) {
            super(itemView);
            firstPartTV = itemView.findViewById(R.id.firstPart);
            secondPartTV = itemView.findViewById(R.id.secondPart);
            imageView = itemView.findViewById(R.id.imageView);
            constraintLayout = itemView.findViewById(R.id.parent_layout);
        }
    }
}
