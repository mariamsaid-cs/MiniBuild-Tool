package com.minibuild.core;

public interface BuildEventListener {

    void onLog(String level, String message);

    void onTaskStarted(BuildExecutor.TaskRecord taskRecord);

    void onTaskCompleted(BuildExecutor.TaskRecord taskRecord);
}