package contest;

import template.io.FastInput;

import java.io.PrintWriter;
import java.util.Arrays;

public class AFourtuneCookies {
    public void solve(int testNumber, FastInput in, PrintWriter out) {
        int[] a = new int[4];
        in.populate(a);
        int sum = Arrays.stream(a).sum();
        if (dfs(a, 0, 0, sum)) {
            out.print("Yes");
        }else{
            out.print("No");
        }
    }

    public boolean dfs(int[] a, int i, int cur, int sum) {
        if (i == a.length) {
            return cur == sum - cur;
        }
        return dfs(a, i + 1, cur + a[i], sum) ||
                dfs(a, i + 1, cur, sum);
    }
}
