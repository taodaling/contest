package contest;

import template.datastructure.MultiWayStack;
import template.io.FastInput;
import template.primitve.generated.datastructure.IntegerArrayList;
import template.primitve.generated.datastructure.IntegerDiscreteMap;
import template.primitve.generated.datastructure.IntegerIterator;
import template.primitve.generated.datastructure.IntegerMultiWayStack;

import java.io.PrintWriter;

public class MovieFestival {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int n = in.readInt();
        int[] a = new int[n];
        int[] b = new int[n];
        IntegerArrayList list = new IntegerArrayList();
        for (int i = 0; i < n; i++) {
            a[i] = in.readInt();
            b[i] = in.readInt();
        }
        IntegerDiscreteMap.discrete(a, b);
        int size = n * 2;
        int[] dp = new int[size + 1];
        dp[0] = 0;
        IntegerMultiWayStack stack = new IntegerMultiWayStack(size + 1, n);
        for (int i = 0; i < n; i++) {
            stack.addLast(b[i], i);
        }

        for (int i = 1; i <= size; i++) {
            dp[i] = dp[i - 1];
            for (IntegerIterator iterator = stack.iterator(i); iterator.hasNext(); ) {
                int next = iterator.next();
                dp[i] = Math.max(dp[i], dp[a[next]] + 1);
            }
        }
        int ans = dp[size];
        out.println(ans);
    }
}
