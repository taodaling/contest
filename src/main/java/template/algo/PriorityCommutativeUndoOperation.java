package template.algo;

public class PriorityCommutativeUndoOperation implements UndoOperation {
    public long priority;
    int offsetToBottom;
    UndoOperation op;

    public PriorityCommutativeUndoOperation(long priority, UndoOperation op) {
        this.priority = priority;
        this.op = op;
    }

    @Override
    public void apply() {
        op.apply();
    }

    @Override
    public void undo() {
        op.undo();
    }
}
