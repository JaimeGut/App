package com.example.taskmanager;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.text.DateFormat;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.List;

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.TaskViewHolder> {

    public List<Task> tasks;
    private Context context;

    public TaskAdapter(Context context) {
        this.context = context;
        this.tasks = new ArrayList<>();
    }

    @NonNull
    @Override
    public TaskViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
        return new TaskViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull TaskViewHolder holder, int position) {
        Task task = tasks.get(position);
        holder.taskDescriptionTextView.setText(task.getDescription());
        holder.taskDueDateTextView.setText(DateFormat.getDateInstance(DateFormat.SHORT).format(task.getDueDate()));
        holder.taskCheckBox.setChecked(task.isCompleted());
        holder.taskCheckBox.setOnCheckedChangeListener((buttonView, isChecked) -> {
            task.setCompleted(isChecked);
            ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(
                    holder.itemView.getContext(),
                    R.array.categories_array,
                    android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        holder.categorySpinner.setAdapter(adapter);
    });
        try{
            System.out.println(task.getPriority());
        }catch (Exception e){
            System.out.println(task.getPriority());
            e.printStackTrace();
        }
        switch (task.getPriority()) {
            case "Alta":
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.high_priority));
                break;
            case "Media":
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.medium_priority));
                break;
            case "Baja":
                holder.itemView.setBackgroundColor(ContextCompat.getColor(context, R.color.low_priority));
                break;
        }
    }

    @Override
    public int getItemCount() {
        return tasks.size();
    }

    public void addTask(Task task) {
        tasks.add(task);
        notifyItemInserted(tasks.size() - 1);
    }

    public void removeTask(int position) {
        tasks.remove(position);
        notifyItemRemoved(position);
    }

    class TaskViewHolder extends RecyclerView.ViewHolder {
        CheckBox taskCheckBox;
        TextView taskDescriptionTextView;
        TextView taskDueDateTextView;
        public Spinner categorySpinner;

        public TaskViewHolder(@NonNull View itemView) {
            super(itemView);
            taskCheckBox = itemView.findViewById(R.id.task_checkbox);
            taskDescriptionTextView = itemView.findViewById(R.id.task_description_textview);
            taskDueDateTextView = itemView.findViewById(R.id.task_due_date_textview);
            categorySpinner = itemView.findViewById(R.id.category_spinner);
        }
    }

    public void saveTasksToSharedPreferences(SharedPreferences sharedPreferences) {
        SharedPreferences.Editor editor = sharedPreferences.edit();
        Gson gson = new Gson();
        String json = gson.toJson(tasks);
        editor.putString("tasks", json);
        editor.apply();
    }

    public void loadTasksFromSharedPreferences(SharedPreferences sharedPreferences) {
        String json = sharedPreferences.getString("tasks", null);
        if (json != null) {
            Gson gson = new Gson();
            Type type = new TypeToken<List<Task>>(){}.getType();
            tasks = gson.fromJson(json, type);
            notifyDataSetChanged();
        }
    }
}
