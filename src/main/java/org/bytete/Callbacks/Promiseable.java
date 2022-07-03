package org.bytete.Callbacks;

@FunctionalInterface
public interface Promiseable<R, J> {
    void run(ParameterVoidCallback<R> resolve, ParameterVoidCallback<J> reject) throws Exception;
}
