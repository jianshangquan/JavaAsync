package org.bytete;

import org.bytete.Callbacks.FinallyCallback;

public class Task extends Thread{
    private FinallyCallback task;
    private int priority;

    public Task(){}

    public Task(FinallyCallback voidCallback, int priority){
        super();
        task = voidCallback;
        this.priority = priority;
    }

    public Task(FinallyCallback voidCallback){
        super();
        task = voidCallback;
        priority = Async.NORMAL_PROIRITY;
    }


    @Override
    public void run() {
        task.run();
    }
}
