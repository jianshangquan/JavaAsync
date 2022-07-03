package org.bytete;


import org.bytete.Callbacks.*;

import java.util.ArrayList;
import java.util.List;

public class Async<R> implements Asyncable{

    public static final int MAX_PROIRITY = 10;
    public static final int NORMAL_PROIRITY = 5;
    public static final int MIN_PROIRITY = 0;

    private List<Callback> thenCallBacks;
    private List<FinallyCallback> onCompleteEvent;
    private List<CatchErrorCallback> onErrorEvents;
    private CatchErrorCallback catchErrorCallback;
    private FinallyCallback finallyCallback;
    private int priority;
    private Task currentTask = null;

    private Object returnResult = null;

    private boolean isCompleted = false;
    private boolean isInterrupted = false;



    public Async(){
        thenCallBacks = new ArrayList<>();
        onCompleteEvent = new ArrayList<>();
        onErrorEvents = new ArrayList<>();
    }

    public Async(int priority){
        this();
        this.priority = priority;
    }

    public Async(VoidCallback voidCallback){
        this();
        thenCallBacks.add(voidCallback);
    }

    public Async(VoidCallback voidCallback, int priority){
        this(priority);
        thenCallBacks.add(voidCallback);
    }

    public Async(ReturnTypeCallback<R> voidCallback){
        this();
        thenCallBacks.add(voidCallback);
    }

    public Async(ReturnTypeCallback<R> voidCallback, int priority){
        this(priority);
        thenCallBacks.add(voidCallback);
    }



    public static AsyncAll all(Async ...async){
        return new AsyncAll(async);
    }



    public Async then(VoidCallback voidCallback){
        thenCallBacks.add(voidCallback);
        return this;
    }

    public <R> Async then(ReturnTypeCallback<R> typeCallback){
        thenCallBacks.add(typeCallback);
        return this;
    }

    public <T, R> Async then(ParameterReturnTypeCallback<T, R> parameterReturnTypeCallback){
        thenCallBacks.add(parameterReturnTypeCallback);
        return this;
    }

    public <T> Async then(ParameterVoidCallback parameterVoidCallback){
        thenCallBacks.add(parameterVoidCallback);
        return this;
    }


    public void addOnComplete(FinallyCallback finallyCallback){
        onCompleteEvent.add(finallyCallback);
    }
    public void addOnError(CatchErrorCallback finallyCallback) {onErrorEvents.add(finallyCallback); }


    @Override
    public void async() {
        currentTask = new Task(() -> run(), priority);
        currentTask.start();
    }

    @Override
    public <R> R await() {
        run();
        return (R) returnResult;
    }

    @Override
    public void interrupt() {
        isInterrupted = true;
        if(currentTask != null) currentTask.interrupt();
    }

    public Async catchError(CatchErrorCallback voidCallback){
        if(catchErrorCallback != null) return this;
        catchErrorCallback = voidCallback;
        return this;
    }

    public Async handleFinally(FinallyCallback voidCallback){
        if(finallyCallback != null) return this;
        finallyCallback = voidCallback;
        return this;
    }



    private void runOnComplete(){
        onCompleteEvent.forEach(complete -> {
            complete.run();
        });
    }

    private void runOnError(Exception e){
        onErrorEvents.forEach(error -> {
            error.run(e);
        });
    }


    private void run(){
        try{
            for(int i = 0; i < thenCallBacks.size(); i++) {
                if(isInterrupted) throw new Exception("Was interrupted");
                Callback cb = thenCallBacks.get(i);
                if (cb instanceof VoidCallback) {
                    returnResult = null;
                    ((VoidCallback) cb).run();
                }
                if (cb instanceof ParameterVoidCallback) {
                    ((ParameterVoidCallback) cb).run(returnResult);
                    returnResult = null;
                }
                if (cb instanceof ReturnTypeCallback) returnResult = ((ReturnTypeCallback) cb).run();
                if (cb instanceof ParameterReturnTypeCallback) returnResult = ((ParameterReturnTypeCallback) cb).run(returnResult);
            }
            runOnComplete();
        }catch (Exception e){
            runOnError(e);
            if(catchErrorCallback != null) catchErrorCallback.run(e);
        }
        if(finallyCallback != null) finallyCallback.run();
        isCompleted = true;
    }
}












