package template.algo;

import java.util.ArrayDeque;
import java.util.Deque;

public class OperationQueue {
    /**
     * (a, b) take the same effect as (b, a)
     */
    public static abstract class CommutativeOperation implements Operation {
        private boolean a;
        public static CommutativeOperation NIL = new CommutativeOperation() {
            @Override
            public void apply() {
            }

            @Override
            public void undo() {
            }
        };
    }


    private Deque<CommutativeOperation> dq;
    private Deque<CommutativeOperation> bufA;
    private Deque<CommutativeOperation> bufB;

    public OperationQueue(int size) {
        dq = new ArrayDeque<>(size);
        bufA = new ArrayDeque<>(size);
        bufB = new ArrayDeque<>(size);
    }

    public void add(CommutativeOperation op) {
        pushAndDo(op);
    }

    private void pushAndDo(CommutativeOperation op) {
        dq.addLast(op);
        op.apply();
    }

    private void popAndUndo() {
        CommutativeOperation ans = dq.removeLast();
        ans.undo();
        if (ans.a) {
            bufA.addLast(ans);
        } else {
            bufB.addLast(ans);
        }
    }

    public void remove() {
        if (!dq.peekLast().a) {
            popAndUndo();
            while (!dq.isEmpty() && bufB.size() != bufA.size()) {
                popAndUndo();
            }
            if (dq.isEmpty()) {
                while (!bufB.isEmpty()) {
                    CommutativeOperation ans = bufB.removeFirst();
                    ans.a = true;
                    pushAndDo(ans);
                }
            } else {
                while (!bufB.isEmpty()) {
                    CommutativeOperation ans = bufB.removeLast();
                    pushAndDo(ans);
                }
            }
            while (!bufA.isEmpty()) {
                pushAndDo(bufA.removeLast());
            }
        }

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
            popAndUndo();
        }
        bufA.clear();
        bufB.clear();
    }
}
