package org.bytete.Callbacks;

@FunctionalInterface
public interface ParameterReturnTypeCallback<T, R> extends Callback{
    R run(T parameter) throws Exception;
}