package on2021_07.on2021_07_21_DMOJ___Wesley_s_Anger_Contest_3.Problem_4___Canadian_Construction_Crew;



import template.datastructure.DSU;
import template.io.FastInput;
import template.io.FastOutput;

public class Problem4CanadianConstructionCrew {
    public void solve(int testNumber, FastInput in, FastOutput out) {
        int n = in.ri();
        int q = in.ri();
        boolean[] visited = new boolean[n];
        int cc = 0;
        DSU dsu = new DSU(n);
        dsu.init();
        int[] deg = new int[n];
        int oddSum = 0;

        for (int i = 0; i < q; i++) {
            int a = in.ri() - 1;
            int b = in.ri() - 1;
            int x = in.ri();
            if (!visited[a]) {
                visited[a] = true;
                cc++;
            }
            if (!visited[b]) {
                visited[b] = true;
                cc++;
            }
            if (dsu.find(a) != dsu.find(b)) {
                cc--;
                dsu.merge(a, b);
            }
            oddSum -= deg[a] & 1;
            oddSum -= deg[b] & 1;
            deg[a] += x;
            deg[b] += x;
            oddSum += deg[a] & 1;
            oddSum += deg[b] & 1;

            boolean ok = cc == 1 && oddSum <= 2;
            out.println(ok ? "YES" : "NO");
        }
    }


}


