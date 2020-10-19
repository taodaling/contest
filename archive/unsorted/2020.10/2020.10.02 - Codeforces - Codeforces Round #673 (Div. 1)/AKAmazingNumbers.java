package contest;

import template.datastructure.MultiWayDeque;
import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerMultiWayDeque;
import template.utils.Debug;

import java.io.PrintWriter;
import java.util.Arrays;

public class AKAmazingNumbers {
    Debug debug = new Debug(true);
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        in.populate(a);
        int[] ans = new int[n + 1];

        MultiWayDeque<Integer> stack = new MultiWayDeque<>(n + 1, n);
        for (int i = 0; i < n; i++) {
            stack.addLast(a[i], i);
        }

        int inf = (int) 1e9;
        Arrays.fill(ans, inf);
        for (int i = 1; i <= n; i++) {
            int last = -1;
            int block = 0;
            for (int x : stack.queue(i)) {
                block = Math.max(block, x - last - 1);
                last = x;
            }
            block = Math.max(block, n - last - 1);
            if(block + 1 <= n){
                ans[block + 1] = Math.min(ans[block + 1], i);
            }
        }

        debug.run(() -> {
            debug.debug("ans", Arrays.toString(ans));
        });
        for(int i = 1; i <= n; i++){
            ans[i] = Math.min(ans[i], ans[i - 1]);
        }
        for(int i = 1; i <= n; i++){
            out.print(ans[i] == inf ? -1 : ans[i]);
            out.print(' ');
        }
        out.println();
    }
}
