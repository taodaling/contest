package contest;

import template.algo.CommutativeUndoOperation;
import template.algo.UndoKnapsack;
import template.algo.UndoQueue;
import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class NecessaryCoins {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int x = in.ri();
        int[] a = new int[n];
        in.populate(a);
        UndoKnapsack bag = new UndoKnapsack(x);
        UndoQueue queue = new UndoQueue(n);
        for (int i = 0; i < n; i++) {
            queue.add(bag.add(a[i], 0));
        }
        assert bag.dp[x] == 0;
        List<Integer> ans = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            CommutativeUndoOperation op = queue.remove();
            if (bag.dp[x] != 0) {
                ans.add(a[i]);
            }
            queue.add(op);
        }
        out.println(ans.size());
        for(int t : ans){
            out.append(t).append(' ');
        }
    }
}
