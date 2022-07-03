package org.bytete;

import org.bytete.Callbacks.CatchErrorsCallback;
import org.bytete.Callbacks.FinallyCallback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

public class AsyncAll implements Asyncable {
    private List<Async> asyncs = new ArrayList<>();
    private FinallyCallback onCompleteCallback = null;
    private CatchErrorsCallback onErrorCallback = null;
    private int taskCompletedCount = 0;
    private int errorCount = 0;
    private boolean isErrorOccured = false;
    private List<Exception> exceptions = new ArrayList<>();


    public AsyncAll(Async...async){
        for(int i = 0; i < async.length; i++){
            asyncs.add(async[i]);
        }
    }

    public AsyncAll onComplete(FinallyCallback voidCallback){
        onCompleteCallback = voidCallback;
        return this;
    }

    public AsyncAll onError(CatchErrorsCallback catchErrorCallback){
        onErrorCallback = catchErrorCallback;
        return this;
    }

    @Override
    public void async() {
        asyncs.forEach(async -> {
            async.addOnComplete(() -> {
                taskCompletedCount++;
                if(taskCompletedCount == asyncs.size()) onCompleteCallback.run();
                if(taskCompletedCount + errorCount == asyncs.size() && isErrorOccured && onErrorCallback != null) onErrorCallback.run(exceptions);
            });
            async.addOnError((e) -> {
                isErrorOccured = true;
                errorCount++;
                exceptions.add(e);
                if(taskCompletedCount + errorCount == asyncs.size() && onErrorCallback != null) onErrorCallback.run(exceptions);
            });
            async.async();
        });
    }

    @Override
    public void interrupt() {
        asyncs.forEach(async -> async.interrupt());
        exceptions.add(new Exception("Client request to interrupt"));
        onErrorCallback.run(exceptions);
    }

    public void forceInterrupt(){
        asyncs.forEach(async -> async.interrupt());
        exceptions.add(new Exception("Client request to interrupt"));
        onErrorCallback.run(exceptions);
    }

    @Override
    public <R> R await() {
        AtomicReference<Object> returnResult = new AtomicReference<>();
        asyncs.forEach(async -> {
            async.addOnError((e) -> {
                async.interrupt();
            });
            returnResult.set(async.<R>await());
        });
        if(onCompleteCallback != null) onCompleteCallback.run();
        return (R) returnResult.get();
    }
}
