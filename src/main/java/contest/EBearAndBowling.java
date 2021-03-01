package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayDeque;
import java.util.Deque;

public class EBearAndBowling {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int[] perm = new int[]{1, 2, 3, 0};
        Deque<int[]> dq = new ArrayDeque<>();
        assert dfs(0, perm, dq);
        for (int[] x : dq) {
            out.append(x[0] + 1).append(' ').append(x[1] + 1).println();
        }
    }

    void modify(int[] perm, int i, int j) {
        int tmp = perm[i];
        perm[i] = -perm[j];
        perm[j] = -tmp;
    }

    boolean dfs(int i, int[] perm, Deque<int[]> dq) {
        if (i == perm.length + 1) {
            for (int j = 0; j < perm.length; j++) {
                if (perm[j] != j) {
                    return false;
                }
            }
            return true;
        }
        for (int l = 0; l < perm.length; l++) {
            for (int r = l + 1; r < perm.length; r++) {
                modify(perm, l, r);
                if (dfs(i + 1, perm, dq)) {
                    dq.addFirst(new int[]{l, r});
                    return true;
                }
                modify(perm, l, r);
            }
        }
        return false;
    }
}
