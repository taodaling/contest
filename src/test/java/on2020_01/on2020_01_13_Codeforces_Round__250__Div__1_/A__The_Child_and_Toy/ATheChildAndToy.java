package on2020_01.on2020_01_13_Codeforces_Round__250__Div__1_.A__The_Child_and_Toy;



import template.io.FastInput;
import template.io.FastOutput;

public class ATheChildAndToy {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();
        int[] v = new int[n];
        for (int i = 0; i < n; i++) {
            v[i] = in.readInt();
        }
        int[][] edges = new int[m][2];
        for (int i = 0; i < m; i++) {
            edges[i][0] = in.readInt() - 1;
            edges[i][1] = in.readInt() - 1;
        }

        long ans = 0;
        for (int i = 0; i < m; i++) {
            ans += Math.min(v[edges[i][0]], v[edges[i][1]]);
        }

        out.println(ans);
    }
}
