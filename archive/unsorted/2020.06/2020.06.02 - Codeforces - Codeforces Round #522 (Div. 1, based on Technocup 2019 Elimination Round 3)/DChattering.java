package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerSparseTable;
import template.utils.ArrayIndex;
import template.utils.Debug;

public class DChattering {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] w = new int[n];
        in.populate(w);

        int[] left = new int[3 * n];
        int[] right = new int[3 * n];
        for (int i = 0; i < n * 3; i++) {
            left[i] = Math.max(i - w[i % n], 0);
            right[i] = Math.min(i + w[i % n], 3 * n - 1);
        }
        IntegerSparseTable leftIST = new IntegerSparseTable(i -> i, n * 3, (a, b) -> left[a] < left[b] ? a : b);
        IntegerSparseTable rightIST = new IntegerSparseTable(i -> i, n * 3, (a, b) -> right[a] > right[b] ? a : b);

        debug.debug("left", left);
        debug.debug("right", right);
        ArrayIndex ai = new ArrayIndex(3 * n, 21, 2);
        int[] jump = new int[ai.totalSize()];
        for (int i = 0; i < n * 3; i++) {
            jump[ai.indexOf(i, 0, 0)] = left[i];
            jump[ai.indexOf(i, 0, 1)] = right[i];
        }
        for (int i = 0; i + 1 <= 20; i++) {
            for (int j = 0; j < 3 * n; j++) {
                int l = jump[ai.indexOf(j, i, 0)];
                int r = jump[ai.indexOf(j, i, 1)];
                jump[ai.indexOf(j, i + 1, 0)] = jump[ai.indexOf(leftIST.query(l, r), i, 0)];
                jump[ai.indexOf(j, i + 1, 1)] = jump[ai.indexOf(rightIST.query(l, r), i, 1)];
            }
        }

        for (int i = n; i < 2 * n; i++) {
            int l = i;
            int r = i;
            int time = 0;
            for (int j = 20; j >= 0; j--) {
                if (jump[ai.indexOf(r, j, 1)] - jump[ai.indexOf(l, j, 0)] + 1 < n) {
                    int oldL = jump[ai.indexOf(l, j, 0)];
                    int oldR = jump[ai.indexOf(r, j, 1)];
                    r = rightIST.query(oldL, oldR);
                    l = leftIST.query(oldL, oldR);
                    time += 1 << j;
                }
            }
            if (r - l + 1 < n) {
                time++;
            }
            out.println(time);
        }
    }
}
