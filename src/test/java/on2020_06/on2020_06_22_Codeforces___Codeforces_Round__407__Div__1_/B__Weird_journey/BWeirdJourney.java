package on2020_06.on2020_06_22_Codeforces___Codeforces_Round__407__Div__1_.B__Weird_journey;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

public class BWeirdJourney {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.readInt();
        int m = in.readInt();

        int selfLoop = 0;
        int[][] edges = new int[m][2];
        int[] deg = new int[n];

        DSU dsu = new DSU(n + m);
        for (int i = 0; i < m; i++) {
            int a = in.readInt() - 1;
            int b = in.readInt() - 1;
            edges[i][0] = a;
            edges[i][1] = b;
            if (a == b) {
                selfLoop++;
            } else {
                deg[a]++;
                deg[b]++;
            }
            dsu.merge(a, i + n);
            dsu.merge(b, i + n);
        }
        for (int i = n; i < n + m; i++) {
            if (dsu.find(i) != dsu.find(n)) {
                out.println(0);
                return;
            }
        }

        long ans = 0;
        ans += (long) selfLoop * (selfLoop - 1) / 2;
        for (int i = 0; i < n; i++) {
            ans += (long)deg[i] * (deg[i] - 1) / 2;
        }
        ans += (long)selfLoop * (m - selfLoop);


        out.println(ans);
    }
}
