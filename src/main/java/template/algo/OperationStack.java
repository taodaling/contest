package template.algo;

import java.util.ArrayDeque;
import java.util.Deque;

public class OperationStack {
    private Deque<Operation> dq;

    public OperationStack(int size) {
        dq = new ArrayDeque<>(size);
    }

    public void push(Operation op) {
        dq.addLast(op);
        op.apply();
    }

    public void pop() {
        dq.removeLast().undo();
    }

    public int size() {
        return dq.size();
    }

    public boolean isEmpty() {
        return dq.isEmpty();
    }

    public void clear() {
        while (!isEmpty()) {
            pop();
        }
    }
}
