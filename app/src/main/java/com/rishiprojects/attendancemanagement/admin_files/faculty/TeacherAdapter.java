package com.rishiprojects.attendancemanagement.admin_files.faculty;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rishiprojects.attendancemanagement.R;
import com.squareup.picasso.Picasso;

import java.util.List;

public class TeacherAdapter extends RecyclerView.Adapter<TeacherAdapter.TeacherViewAdapter> {
    private final List<Teacher> list;
    private final Context context;

    public TeacherAdapter(List<Teacher> list, Context context) {
        this.list = list;
        this.context = context;
    }

    @NonNull
    @Override
    public TeacherViewAdapter onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.teacher_item_layout, parent, false);
        return new TeacherViewAdapter(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TeacherViewAdapter holder, int position) {
        Teacher item = list.get(position);
        holder.name.setText(item.getTeacherName());
        holder.uname.setText(item.getTeacherUsername());
        holder.email.setText(item.getTeacherEmail());
        try {
            Picasso.get().load(item.getTeacherImage()).into(holder.imageView);
        } catch (Exception e) {
            e.printStackTrace();
        }
        holder.update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, UpdateTeacher.class);
                intent.putExtra("name", item.getTeacherName());
                intent.putExtra("email", item.getTeacherEmail());
                intent.putExtra("username", item.getTeacherUsername());
                intent.putExtra("image", item.getTeacherImage());
                context.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    public class TeacherViewAdapter extends RecyclerView.ViewHolder {

        private TextView name, uname, email;
        private Button update;
        private ImageView imageView;

        public TeacherViewAdapter(@NonNull View itemView) {
            super(itemView);
            name = itemView.findViewById(R.id.teacherName);
            uname = itemView.findViewById(R.id.teacherUname);
            email = itemView.findViewById(R.id.teacherEmail);
            imageView = itemView.findViewById(R.id.teacherPhoto);
            update = itemView.findViewById(R.id.teacherUpdate);
        }
    }
}
