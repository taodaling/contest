package on2019_12.on2019_12_04_Codeforces_Round__569__Div__1_.D___Fedor_Runs_for_President;





import template.geometry.ConvexHullTrick;
import template.geometry.LongConvexHullTrick;
import template.io.FastInput;
import template.io.FastOutput;
import template.math.DigitUtils;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayDeque;

import java.util.LinkedList;
import java.util.List;

public class TaskD {
    IntegerMultiWayDeque edges;
    long[] dp0;
    long[] dp1;
    int n;
    int[] size;

    public void solve(int testNumber, FastInput in, FastOutput out) {
        n = in.readInt();
        dp0 = new long[n + 1];
        dp1 = new long[n + 1];
        size = new int[n + 1];
        edges = new IntegerMultiWayDeque(n + 1, 2 * n);
        for (int i = 1; i < n; i++) {
            int a = in.readInt();
            int b = in.readInt();
            edges.addLast(a, b);
            edges.addLast(b, a);
        }
        dfs(1, 0);
        long ans = 0;
        for (int i = 1; i <= n; i++) {
            ans = Math.max(ans, dp0[i]);
            ans = Math.max(ans, dp1[i]);
        }
        long origin = (long) n * (n - 1) / 2;
        out.println(ans + origin);
    }


    public void dfs(int root, int p) {
        size[root] = 1;
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if(node == p){
                continue;
            }
            dfs(node, root);
            size[root] += size[node];
        }
        ConvexHullTrick cht = new ConvexHullTrick();
        for (IntegerIterator iterator = edges.iterator(root); iterator.hasNext(); ) {
            int node = iterator.next();
            if(node == p){
                continue;
            }
            dp0[root] = Math.max( dp0[root],  dp0[node] + (long) (size[root] - size[node]) * size[node]);
            if (!cht.isEmpty()) {
                dp1[root] = Math.max(dp1[root], dp0[node] + (long) (n - size[node]) * size[node] + DigitUtils.round(cht.query(size[node])));
            }
            cht.insert(-size[node], dp0[node] + (long) (n - size[node]) * size[node]);
        }
    }
}
