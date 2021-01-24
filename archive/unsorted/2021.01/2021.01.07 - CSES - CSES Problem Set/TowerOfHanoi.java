package contest;

import template.io.FastInput;
import template.io.FastOutput;

import java.util.ArrayList;
import java.util.List;

public class TowerOfHanoi {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        dfs(n, 1, 3, 2);
        out.println(ans.size());
        for(int[] x : ans){
            out.append(x[0]).append(' ').append(x[1]).println();
        }
    }

    List<int[]> ans = new ArrayList<>((int) 65535);

    void dfs(int n, int src, int dst, int spare) {
        if (n == 0) {
            return;
        }
        dfs(n - 1, src, spare, dst);
        ans.add(new int[]{src, dst});
        dfs(n - 1, spare, dst, src);
    }
}
