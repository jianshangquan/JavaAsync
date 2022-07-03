package org.bytete.Callbacks;

@FunctionalInterface
public interface CatchErrorCallback extends Callback{
    void run(Exception e);
}
