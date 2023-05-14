package com.example.taskmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Intent;

import androidx.appcompat.app.AppCompatDelegate;

public class MainActivity extends AppCompatActivity {

    private EditText taskDescriptionEditText;
    private Button addTaskButton;
    private RecyclerView tasksRecyclerView;
    private  String priority = "";
    private String category ="";
    private String notes;
    private List<Task> subtasks;

    private TaskAdapter taskAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        taskDescriptionEditText = findViewById(R.id.task_description);
        addTaskButton = findViewById(R.id.add_task_button);
        tasksRecyclerView = findViewById(R.id.tasks_recyclerview);

        taskAdapter = new TaskAdapter(this);
        tasksRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        tasksRecyclerView.setAdapter(taskAdapter);

        SharedPreferences sharedPreferences = getSharedPreferences("task_preferences", MODE_PRIVATE);
        taskAdapter.loadTasksFromSharedPreferences(sharedPreferences);

        addTaskButton.setOnClickListener(view -> {
            String taskDescription = taskDescriptionEditText.getText().toString().trim();
            long dueDate = Calendar.getInstance().getTimeInMillis();

            if (!taskDescription.isEmpty() && !priority.isEmpty()) {
                Task task = new Task(taskDescription, dueDate, false, category, priority,notes, (ArrayList) subtasks);
                taskAdapter.addTask(task);
                taskDescriptionEditText.setText("");
            } else {
                Toast.makeText(this, "Por favor, ingrese una descripción de tarea", Toast.LENGTH_SHORT).show();
            }
        });

        tasksRecyclerView.addOnItemTouchListener(new RecyclerView.SimpleOnItemTouchListener() {
            @Override
            public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                View childView = rv.findChildViewUnder(e.getX(), e.getY());
                if (childView != null && e.getAction() == MotionEvent.ACTION_DOWN) {
                    int position = rv.getChildAdapterPosition(childView);
                    Task task = taskAdapter.tasks.get(position);

                    if (task.isCompleted()) {
                        taskAdapter.removeTask(position);
                        Toast.makeText(MainActivity.this, "Tarea eliminada", Toast.LENGTH_SHORT).show();
                    }
                }
                return false;
            }
        });
    }

    @Override
    protected void onPause() {
        super.onPause();
        taskAdapter.saveTasksToSharedPreferences(getSharedPreferences("task_preferences", MODE_PRIVATE));
    }

    private void setTaskReminder(Task task) {
        AlarmManager alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        Intent intent = new Intent(this, TaskNotificationReceiver.class);
        intent.putExtra("task_description", task.getDescription());
        intent.putExtra("task_id", (int) task.getId());
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, (int) task.getId(), intent, PendingIntent.FLAG_UPDATE_CURRENT);

        // Configurar alarma en la fecha límite de la tarea
        alarmManager.set(AlarmManager.RTC_WAKEUP, task.getDueDate(), pendingIntent);
    }

    private void toggleTheme() {
        int currentNightMode = getResources().getConfiguration().uiMode & Configuration.UI_MODE_NIGHT_MASK;
        if (currentNightMode == Configuration.UI_MODE_NIGHT_YES) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main_menu, menu);
        return true;
    }
    private void updateTaskList(String priority) {
        List<Task> tasksByPriority = new ArrayList<>();
        for (Task task : taskAdapter.tasks) {
            if (task.getPriority().equals(priority)) {
                tasksByPriority.add(task);
            }
        }
        taskAdapter.tasks = tasksByPriority;
        taskAdapter.notifyDataSetChanged();
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        priority = "";
        switch (id) {
            case R.id.priority_high:
                priority = "Alta";
                break;
            case R.id.priority_medium:
                priority = "Media";
                break;
            case R.id.priority_low:
                priority = "Baja";
                break;
        }
        return super.onOptionsItemSelected(item);
    }
}