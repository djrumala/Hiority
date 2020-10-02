package com.common_id.hiority;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class ClassroomAdapter extends RecyclerView.Adapter<ClassroomAdapter.MyViewHolder>{

    private List<Classroom> classroomList;
    private Context context;
    private PriceAdapterListener listener;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, floor;

        public MyViewHolder(View view) {
            super(view);
            name = (TextView) view.findViewById(R.id.name);
            floor = (TextView) view.findViewById(R.id.floor);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    listener.onClassroomSelected(classroomList.get(getAdapterPosition()));
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    listener.onClassroomLongClick(classroomList.get(getAdapterPosition()));
                    return true;
                }
            });
        }
    }

    public ClassroomAdapter(Context context, List<Classroom> classroomList, PriceAdapterListener listener) {
        this.context = context;
        this.classroomList = classroomList;
        this.listener = listener;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_class, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Classroom classroom= classroomList.get(position);
        holder.name.setText(classroom.getName());
        holder.floor.setText("Lantai "+classroom.getFloor());
    }

    @Override
    public int getItemCount() {
        return classroomList.size();
    }

    public interface PriceAdapterListener{
        void onClassroomSelected(Classroom classroom);
        void onClassroomLongClick(Classroom classroom);
    }



}