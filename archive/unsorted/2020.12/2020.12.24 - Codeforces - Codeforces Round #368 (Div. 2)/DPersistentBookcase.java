package contest;

import template.algo.OfflinePersistentTree;
import template.algo.UndoOperation;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;

import java.util.function.IntSupplier;

public class DPersistentBookcase {
    static Bookcase bc;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int m = in.ri();
        int q = in.ri();
        bc = new Bookcase(n, m);
        OfflinePersistentTree pt = new OfflinePersistentTree(q);
        IntSupplier getAns = () -> bc.sum;
        OpExt[] op = new OpExt[q];
        for (int i = 0; i < q; i++) {
            int t = in.ri();
            if (t == 1) {
                int a = in.ri() - 1;
                int b = in.ri() - 1;
                op[i] = new OpExt() {
                    int old;

                    @Override
                    public void apply() {
                        old = bc.set(a, b, 1);
                        ans = bc.sum;
                    }

                    @Override
                    public void undo() {
                        bc.set(a, b, old);
                    }
                };
            } else if (t == 2) {
                int a = in.ri() - 1;
                int b = in.ri() - 1;
                op[i] = new OpExt() {
                    int old;

                    @Override
                    public void apply() {
                        old = bc.set(a, b, 0);
                        ans = bc.sum;
                    }

                    @Override
                    public void undo() {
                        bc.set(a, b, old);
                    }
                };
            } else if (t == 3) {
                int a = in.ri() - 1;
                op[i] = new OpExt() {
                    @Override
                    public void apply() {
                        bc.inv(a);
                        ans = bc.sum;
                    }

                    @Override
                    public void undo() {
                        bc.inv(a);
                    }
                };
            } else {
                op[i] = new OpExt() {
                    @Override
                    public void apply() {
                        ans = bc.sum;
                    }

                    @Override
                    public void undo() {

                    }
                };
                pt.switchVersion(in.ri());
            }
            pt.apply(op[i]);
        }
        pt.solve();
        for (OpExt ext : op) {
            out.println(ext.ans);
        }
    }
}

abstract class OpExt implements UndoOperation {
    int ans;
}

class Bookcase {
    int[][] shelf;
    int[] xor;
    int[] size;
    int sum;

    public Bookcase(int n, int m) {
        shelf = new int[n][m];
        xor = new int[n];
        size = new int[n];
    }

    public int get(int i, int j) {
        return shelf[i][j] ^ xor[i];
    }

    public int set(int i, int j, int v) {
        int old = get(i, j);
        if (old == 1) {
            size[i]--;
            sum--;
        }
        shelf[i][j] = v ^ xor[i];
        if (v == 1) {
            size[i]++;
            sum++;
        }
        return old;
    }

    public void inv(int i) {
        xor[i] ^= 1;
        sum -= size[i];
        size[i] = shelf[i].length - size[i];
        sum += size[i];
    }

}
