package contest;

import template.datastructure.IntList;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.BitOperator;

public class TaskF {
    BitOperator bo = new BitOperator();

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int p = in.readInt();
        int[] s = new int[n];
        IntList[] order = new IntList[n];
        for (int i = 0; i < n; i++) {
            s[i] = in.readChar() - 'a';
        }
        for (int i = n - 1; i >= 0; i--) {
            order[i] = new IntList(p);
            if (i == n - 1) {
                continue;
            }
            int mask = 0;
            order[i].add(s[i + 1]);
            mask = bo.setBit(mask, s[i + 1], true);
            for (int j = 0; j < order[i + 1].size(); j++) {
                int next = order[i + 1].get(j);
                if (bo.bitAt(mask, next) == 1) {
                    continue;
                }
                order[i].add(next);
                mask = bo.setBit(mask, next, true);
            }
        }
        int[] edge = new int[p];
        for (int i = 0; i < p; i++) {
            for (int j = 0; j < p; j++) {
                edge[i] = bo.setBit(edge[i], j, in.readInt() == 1);
            }
        }

    }
}
