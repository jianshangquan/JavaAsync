package org.bytete;

public interface Asyncable {
    void async();
    <R> R await();

    void interrupt();
}
