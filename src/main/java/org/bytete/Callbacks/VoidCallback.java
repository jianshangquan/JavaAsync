package org.bytete.Callbacks;

@FunctionalInterface
public interface VoidCallback extends Callback{
    void run() throws Exception;
}
