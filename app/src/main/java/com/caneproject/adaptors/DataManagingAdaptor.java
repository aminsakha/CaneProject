package com.caneproject.adaptors;

import static com.caneproject.classes.GlobalVariablesKt.setSelectedItemInRecView;
import static com.caneproject.utils.UtilityFunctionsKt.changeFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.caneproject.R;
import com.caneproject.utils.UtilityFunctionsKt;

import java.util.List;

public class DataManagingAdaptor extends RecyclerView.Adapter<DataManagingAdaptor.ViewHolderDataManaging> {
    public static List<String> dateAndTimeList;

    public DataManagingAdaptor(List<String> recordList) {
        DataManagingAdaptor.dateAndTimeList = recordList;
        setHasStableIds(true);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public int getItemCount() {
        return dateAndTimeList.size();
    }

    @NonNull
    @Override
    public ViewHolderDataManaging onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_manager_structure, parent, false);
        return new ViewHolderDataManaging(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDataManaging holder, int position) {
        try {
            String title = "data in : " + dateAndTimeList.get(position);
            holder.recordTxt.setText(title);
        } catch (IndexOutOfBoundsException ignore) {
        }
    }

    @Override
    public int getItemViewType(int position) {
        return position;
    }

    public static class ViewHolderDataManaging extends RecyclerView.ViewHolder {
        TextView recordTxt;

        public ViewHolderDataManaging(View itemView) {
            super(itemView);
            recordTxt = itemView.findViewById(R.id.recordTextBox);
            itemView.setOnClickListener(view -> {
                setSelectedItemInRecView(dateAndTimeList.get(getAdapterPosition()));
                changeFragment(itemView, R.id.action_dataManaging_to_dataAnaliticsPage);
            });
        }
    }
}
