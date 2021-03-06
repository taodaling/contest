package template.utils;

import java.util.ArrayDeque;
import java.util.Deque;
import java.util.function.Consumer;
import java.util.function.Supplier;

public class Buffer<T> {
    private Deque<T> deque;
    private Supplier<T> supplier;
    private Consumer<T> cleaner;
    private int allocTime;
    private int releaseTime;


    public Buffer(Supplier<T> supplier) {
        this(supplier, (x) -> {
        });
    }

    public Buffer(Supplier<T> supplier, Consumer<T> cleaner) {
        this(supplier, cleaner, 0);
    }

    public Buffer(Supplier<T> supplier, Consumer<T> cleaner, int exp) {
        this.supplier = supplier;
        this.cleaner = cleaner;
        deque = new ArrayDeque<>(exp);
    }

    public T alloc() {
        allocTime++;
        return deque.isEmpty() ? supplier.get() : deque.removeFirst();
    }

    public void release(T e) {
        releaseTime++;
        cleaner.accept(e);
        deque.addLast(e);
    }

    public void check(){
        if(allocTime != releaseTime){
            throw new IllegalStateException("Buffer alloc " + allocTime + " but release " + releaseTime);
        }
    }
}
