package template.algo;

/**
 * (a, b) take the same effect as (b, a)
 */
public abstract class CommutativeUndoOperation implements UndoOperation {
    boolean flag;
    public static CommutativeUndoOperation NIL = new CommutativeUndoOperation() {
        @Override
        public void apply() {
        }

        @Override
        public void undo() {
        }
    };
}
