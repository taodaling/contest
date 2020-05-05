package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.LongList;
import template.problem.KthSmallestCardGroup;

public class Task {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int k = in.readInt();
        int n = in.readInt();
        long[][] groups = new long[k][];
        for (int i = 0; i < k; i++) {
            int c = in.readInt();
            groups[i] = new long[c];
            for (int j = 0; j < c; j++) {
                groups[i][j] = -in.readInt();
            }
        }

        KthSmallestCardGroup cg = new KthSmallestCardGroup(groups);
        LongList ans = cg.theFirstKSmallestSet(n);
        for(int i = 0; i < n; i++){
            long val = -ans.get(i);
            out.append(val).append(' ');
        }
    }
}
