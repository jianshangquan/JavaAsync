package org.bytete.Callbacks;

@FunctionalInterface
public interface FinallyCallback extends VoidCallback{
    @Override
    void run();
}