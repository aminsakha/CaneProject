//package com.caneproject.adaptors;
//
//import static com.caneproject.utils.GlideFunctionsKt.loadImageForRecView;
//
//import android.annotation.SuppressLint;
//import android.content.Context;
//import android.net.Uri;
//import android.view.LayoutInflater;
//import android.view.View;
//import android.view.ViewGroup;
//import android.widget.Button;
//import android.widget.ImageView;
//import android.widget.Spinner;
//import android.widget.TextView;
//
//import androidx.annotation.NonNull;
//import androidx.constraintlayout.widget.ConstraintLayout;
//import androidx.recyclerview.widget.RecyclerView;
//
//import com.caneproject.R;
//import com.caneproject.db.Data;
//import com.github.dhaval2404.colorpicker.ColorPickerDialog;
//import com.github.dhaval2404.colorpicker.MaterialColorPickerDialog;
//import com.github.dhaval2404.colorpicker.listener.ColorListener;
//import com.github.dhaval2404.colorpicker.model.ColorShape;
//import com.github.dhaval2404.colorpicker.model.ColorSwatch;
//
//import org.jetbrains.annotations.NotNull;
//
//import java.util.List;
//
//public class DataAnalyticAdaptor extends RecyclerView.Adapter<DataAnalyticAdaptor.ViewHolder> {
//    static List<Data> dataList;
//    //static Context context;
//
//
//    public DataAnalyticAdaptor(List<Data> myNoteList, Context context) {
//        DataAnalyticAdaptor.dataList = myNoteList;
//        DataAnalyticAdaptor.context = context;
//        setHasStableIds(true);
//    }
//
//    @Override
//    public long getItemId(int position) {
//        return position;
//    }
//
//    @Override
//    public int getItemViewType(int position) {
//        return position;
//    }
//
//    @NonNull
//    @Override
//    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
//        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.data_structure_in_rec_view, parent, false);
//        return new ViewHolder(view);
//    }
//
//    @Override
//    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        try {
//            holder.firstPartTV.setText(dataList.get(position).toString());
//            holder.secondPartTV.setText(dataList.get(position).toStringForSecondPart());
//            loadImageForRecView(context, Uri.parse(dataList.get(position).getUriString()), holder.imageView);
//        } catch (IndexOutOfBoundsException e) {
//            e.printStackTrace();
//        }
//    }
//
//    @Override
//    public int getItemCount() {
//        return dataList.size();
//    }
//
//    public static class ViewHolder extends RecyclerView.ViewHolder {
//        TextView firstPartTV, secondPartTV;
//        Button button;
//        ImageView imageView;
//        ConstraintLayout constraintLayout;
//
//        @SuppressLint("ClickableViewAccessibility")
//        public ViewHolder(View itemView) {
//            super(itemView);
//            firstPartTV = itemView.findViewById(R.id.firstPart);
//            button = itemView.findViewById(R.id.selectColorBTN);
//            secondPartTV = itemView.findViewById(R.id.secondPart);
//            imageView = itemView.findViewById(R.id.imageView);
//            constraintLayout = itemView.findViewById(R.id.parent_layout);
//            button.setOnClickListener(view -> {
//                new MaterialColorPickerDialog
//                        .Builder(context)
//                        .setTitle("Pick Theme")
//                        .setColorShape(ColorShape.SQAURE)
//                        .setColors(List("#f6e58d", "#ffbe76", "#ff7979", "#badc58", "#dff9fb", "#7ed6df", "#e056fd", "#686de0", "#30336b", "#95afc0"))
//                        .setColorSwatch(ColorSwatch._300)
//                        .setColorListener((color, colorHex) -> {
//                            // Handle Color Selection
//                        })
//                        .show();
//            });
//        }
//    }
//}
