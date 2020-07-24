package contest;

import template.io.FastInput;
import template.io.FastOutput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.utils.Debug;

import java.util.Arrays;

public class BUnmerge {
    Debug debug = new Debug(false);

    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int[] p = new int[2 * n];
        in.populate(p);
        int[] indices = new int[2 * n];
        for (int i = 0; i < 2 * n; i++) {
            p[i]--;
            indices[p[i]] = i;
        }

        IntegerArrayList block = new IntegerArrayList(n);
        int cur = 2 * n;
        for (int i = 2 * n - 1; i >= 0; i--) {
            if (indices[i] >= cur) {
                continue;
            }
            int l = indices[i];
            int r = cur - 1;
            block.add(r - l + 1);
            cur = l;
        }
        debug.debug("block", block);
        boolean[] last = new boolean[n + 1];
        boolean[] next = new boolean[n + 1];
        last[0] = true;
        for (int i = 0; i < block.size(); i++) {
            int val = block.get(i);
            for(int j = 0; j <= n; j++){
                next[j] = last[j];
            }
            for (int j = 0; j + val <= n; j++) {
                next[j + val] = next[j + val] || last[j];
            }
            boolean[] tmp = last;
            last = next;
            next = tmp;
            debug.debug("last", last);
        }

        if (last[n]) {
            out.println("YES");
        } else {
            out.println("NO");
        }
    }
}
