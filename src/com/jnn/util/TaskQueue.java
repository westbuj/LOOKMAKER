package com.jnn.util;

import java.util.LinkedList;



import android.util.Log;

public class TaskQueue {
	private LinkedList<Runnable> tasks;
	private Thread thread;
	private boolean running;
	private Runnable internalRunnable;
	private Runnable currentTask=null;

	private class InternalRunnable implements Runnable {
	public void run() {
	internalRun();
		}
	}

	public TaskQueue() {
	tasks = new LinkedList<Runnable>();
	internalRunnable = new InternalRunnable();
	}

	public void start() {
	if (!running) {
	thread = new Thread(internalRunnable);
	thread.setDaemon(true);
	running = true;
	thread.start();
		}
	}

	public void stop() {
	running = false;
	}

public void addTask(Runnable task) {
	synchronized(tasks) {
		
		if (currentTask != null && task.getClass().getName().contains("RenderThread"))
		{
			//need a static var fro reset thread queue
			//MainActivity.resetRendering=true;
			
		}
		
	tasks.addFirst(task);
	tasks.notify(); // notify any waiting threads
	}
	}

	private Runnable getNextTask() {
	synchronized(tasks) {
	if (tasks.isEmpty()) {
	try {
	tasks.wait();
	} catch (InterruptedException e) {
		Log.e("com.jnn.jw", "Task queue interrupted", e);
		stop();
	}
	}
	return tasks.removeLast();
	}
	}


	private void internalRun() {
	while(running) {
	currentTask = getNextTask();
	try {
		currentTask.run();
		currentTask=null;
	} catch (Throwable t) {
		Log.e("com.jnn.jw", "Exception on task queue", t);
	}
	}
	}
	}
