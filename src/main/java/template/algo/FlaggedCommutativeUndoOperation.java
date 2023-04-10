package template.algo;

/**
 * (a, b) take the same effect as (b, a)
 */
public class FlaggedCommutativeUndoOperation implements UndoOperation {
    boolean flag;
    UndoOperation op;

    public FlaggedCommutativeUndoOperation(UndoOperation op) {
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
