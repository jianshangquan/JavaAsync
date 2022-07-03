package org.bytete.Callbacks;

@FunctionalInterface
public interface ParameterVoidCallback<T> extends Callback{
    void run(T parameter) throws Exception;
}