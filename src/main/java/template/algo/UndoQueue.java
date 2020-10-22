package template.algo;

import java.util.ArrayDeque;
import java.util.Deque;

public class UndoQueue {


    private Deque<CommutativeUndoOperation> dq;
    private Deque<CommutativeUndoOperation> bufA;
    private Deque<CommutativeUndoOperation> bufB;

    public UndoQueue(int size) {
        dq = new ArrayDeque<>(size);
        bufA = new ArrayDeque<>(size);
        bufB = new ArrayDeque<>(size);
    }

    public void add(CommutativeUndoOperation op) {
        pushAndDo(op);
    }

    private void pushAndDo(CommutativeUndoOperation op) {
        dq.addLast(op);
        op.apply();
    }

    private void popAndUndo() {
        CommutativeUndoOperation ans = dq.removeLast();
        ans.undo();
        if (ans.flag) {
            bufA.addLast(ans);
        } else {
            bufB.addLast(ans);
        }
    }

    public void remove() {
        if (!dq.peekLast().flag) {
            popAndUndo();
            while (!dq.isEmpty() && bufB.size() != bufA.size()) {
                popAndUndo();
            }
            if (dq.isEmpty()) {
                while (!bufB.isEmpty()) {
                    CommutativeUndoOperation ans = bufB.removeFirst();
                    ans.flag = true;
                    pushAndDo(ans);
                }
            } else {
                while (!bufB.isEmpty()) {
                    CommutativeUndoOperation ans = bufB.removeLast();
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
