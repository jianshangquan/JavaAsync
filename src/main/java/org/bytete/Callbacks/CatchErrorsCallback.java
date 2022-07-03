package org.bytete.Callbacks;


import java.util.List;

@FunctionalInterface
public interface CatchErrorsCallback extends Callback{
    void run(List<Exception> e);
}
